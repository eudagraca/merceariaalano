package com.mercearia.alano.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.kinda.alert.KAlertDialog;
import com.mercearia.alano.R;
import com.mercearia.alano.adapters.ProdutoAdapter;
import com.mercearia.alano.models.Produto;
import com.mercearia.alano.utils.FirebaseConnect;
import com.mercearia.alano.utils.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class ProdutoFragment extends Fragment {

    private List<Produto> mProdutoList;
    private Context mContext;
    private ProdutoAdapter mProdutoAdapter;
    private RecyclerView rv_product;
    private FragmentManager fragmentManager;
    private LinearLayout ll_produto;
    private int fl_framelayout;
    private String pid;
    private SpinnerDialog spinnerDialog;
    private GridLayoutManager mGridLayoutManager;

    public ProdutoFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view            = inflater.inflate(R.layout.fragment_produto, container, false);
        rv_product           = view.findViewById(R.id.rv_product);
        ll_produto           = view.findViewById(R.id.ll_produto);
        fragmentManager      = getFragmentManager();
        fl_framelayout       = R.id.framelayout_container;
        mContext             = getContext();
        KAlertDialog pDialog = new KAlertDialog(Objects.requireNonNull(mContext), KAlertDialog.PROGRESS_TYPE);
        mGridLayoutManager   = new GridLayoutManager(mContext, 2);

        rv_product.setHasFixedSize(true);
        rv_product.setLayoutManager(mGridLayoutManager);

        //Init my database!!
        FirebaseFirestore mFirestore = FirebaseConnect.getFireStore(mContext);

        pDialog.getProgressHelper().setBarColor(Color.parseColor(Helper.COR_SECUNDARIA));
        pDialog.setTitleText("");
        pDialog.setCancelable(false);
        pDialog.show();

        mProdutoList = new ArrayList<>();
        final ArrayList<String> produtos = new ArrayList<>();

        mFirestore.collection(Helper.COLLECTION_PRODUTOS)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Produto produto = new Produto();
                        produto.setNome((String) snapshot.get("nome"));
                        produto.setId(snapshot.getId());
                        produtos.add(produto.getNome());
                    }

                    String selec = "Seleccione o produto";
                    String close = "Fechar";

                    spinnerDialog = new SpinnerDialog((Activity) mContext, produtos, selec, R.style.DialogAnimations_SmileWindow, close);// With 	Animation

                    pDialog.dismissWithAnimation();

                    spinnerDialog.setCancellable(true); // for cancellable
                    spinnerDialog.setShowKeyboard(false);// for open keyboard by default

                    spinnerDialog.bindOnSpinerListener((item, position) -> {
                        ll_produto.setVisibility(View.GONE);
                        pDialog.show();
                        mGridLayoutManager = new GridLayoutManager(mContext, 1);
                        rv_product.setLayoutManager(mGridLayoutManager);
                        mFirestore.collection(Helper.COLLECTION_PRODUTOS).whereEqualTo("nome", item)
                                .get().addOnSuccessListener(querySnapshots -> {
                            if (!querySnapshots.isEmpty()) {
                                ll_produto.setVisibility(View.VISIBLE);
                                pDialog.dismiss();
                                mProdutoList.clear();
                                for (QueryDocumentSnapshot documentSnapshot : querySnapshots) {
                                    Produto produto = new Produto();
                                    produto.setNome(documentSnapshot.getString("nome"));
                                    produto.setPrecoVenda(Float.parseFloat(String.valueOf(documentSnapshot.get("precoUnitario"))));
                                    produto.setId(documentSnapshot.getId());
                                    mProdutoList.add(produto);
                                }

                                mProdutoAdapter = new ProdutoAdapter(mContext, mProdutoList);
                                if (mProdutoAdapter.getItemCount() > 0) {
                                    rv_product.setAdapter(mProdutoAdapter);
                                    mProdutoAdapter.notifyDataSetChanged();
                                }
                                mProdutoAdapter.setOnClickListener(v -> {
                                    pid = getSelectedUserId(v);
                                    Intent intent = new Intent(mContext, DetailsActivity.class);
                                    intent.putExtra("product_id", pid);
                                    startActivity(intent);
                                });
                            }
                        });

                    });
                });

        view.findViewById(R.id.search).setOnClickListener(v -> spinnerDialog.showSpinerDialog());

        mFirestore.collection(Helper.COLLECTION_PRODUTOS)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                ll_produto.setVisibility(View.VISIBLE);
                pDialog.dismiss();
                mProdutoList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Produto produto = new Produto();
                    produto.setNome(documentSnapshot.getString("nome"));
                    produto.setPrecoVenda(Float.parseFloat(String.valueOf(documentSnapshot.get("precoUnitario"))));
                    produto.setId(documentSnapshot.getId());
                    mProdutoList.add(produto);
                }

                mProdutoAdapter = new ProdutoAdapter(mContext, mProdutoList);
                if (mProdutoAdapter.getItemCount() > 0) {
                    rv_product.setAdapter(mProdutoAdapter);
                    mProdutoAdapter.notifyDataSetChanged();
                }
                mProdutoAdapter.setOnClickListener(v -> {
                    pid = getSelectedUserId(v);

                    Intent intent = new Intent(mContext, DetailsActivity.class);
                    intent.putExtra("product_id", pid);
                    startActivity(intent);

                });
            }
        });
        return view;
    }

    private String getSelectedUserId(int position) {
        TextView tv = Objects.requireNonNull(rv_product.findViewHolderForAdapterPosition(position)).itemView.findViewById(R.id.tv_id);
        return tv.getText().toString();
    }
}