package com.mercearia.alano.views.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.kinda.alert.KAlertDialog;
import com.mercearia.alano.R;
import com.mercearia.alano.adapters.ProdutoAdapter;
import com.mercearia.alano.database.FirebaseConnect;
import com.mercearia.alano.models.Product;
import com.mercearia.alano.utils.Helper;
import com.mercearia.alano.views.activities.DetailsActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class ProdutoFragment extends Fragment {

    private List<Product> mProductList;
    @Nullable
    private Context mContext;
    @Nullable
    private ProdutoAdapter mProdutoAdapter;
    private RecyclerView rv_product;
    private LinearLayout ll_produto;
    private String pid;
    @Nullable
    private SpinnerDialog spinnerDialog;
    @Nullable
    private KAlertDialog pDialog;
    private Button search;

    public ProdutoFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_produto, container, false);
        rv_product = view.findViewById(R.id.rv_product);
        ll_produto = view.findViewById(R.id.ll_produto);
        search = view.findViewById(R.id.search);
        mContext = getActivity();

        int orientation = getResources().getConfiguration().orientation;
        GridLayoutManager mGridLayoutManager;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mGridLayoutManager = new GridLayoutManager(mContext, 4);
        } else {
            mGridLayoutManager = new GridLayoutManager(mContext, 2);
        }
        rv_product.setHasFixedSize(true);
        rv_product.setLayoutManager(mGridLayoutManager);


        return view;
    }

    private String getSelectedUserId(int position) {
        TextView tv = Objects.requireNonNull(rv_product.findViewHolderForAdapterPosition(position)).itemView.findViewById(R.id.tv_id);
        return tv.getText().toString();
    }

    @Override
    public void onStart() {
        super.onStart();

        pDialog = new KAlertDialog(Objects.requireNonNull(mContext), KAlertDialog.PROGRESS_TYPE);
        Objects.requireNonNull(pDialog).getProgressHelper().setBarColor(Color.parseColor(Helper.COR_SECUNDARIA));
        pDialog.setTitleText("");
        pDialog.setCancelable(false);
        pDialog.show();

        //Init my database!!
        FirebaseFirestore mFirestore = FirebaseConnect.getFireStore(Objects.requireNonNull(mContext));

        Objects.requireNonNull(pDialog).show();
        mProductList = new ArrayList<>();
        final ArrayList<String> produtos = new ArrayList<>();

        mFirestore.collection(Helper.COLLECTION_PRODUTOS).orderBy("nome")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Product product = new Product();
                        product.setNome((String) snapshot.get("nome"));
                        product.setId(snapshot.getId());
                        produtos.add(product.getNome());
                    }
                    pDialog.dismiss();

                    spinnerDialog = new SpinnerDialog((Activity) mContext, produtos, Helper.SELECT, R.style.DialogAnimations_SmileWindow, Helper.CLOSE);// With 	Animation

                    spinnerDialog.setCancellable(true); // for cancellable
                    spinnerDialog.setShowKeyboard(false);// for open keyboard by defaul
                    spinnerDialog.bindOnSpinerListener((item, position) -> {
                        ll_produto.setVisibility(View.GONE);
                        pDialog.show();
                        mFirestore.collection(Helper.COLLECTION_PRODUTOS).whereEqualTo("nome", item)
                                .get().addOnSuccessListener(querySnapshots -> {
                            if (!querySnapshots.isEmpty()) {
                                ll_produto.setVisibility(View.VISIBLE);
                                pDialog.dismiss();
                                mProductList.clear();
                                for (QueryDocumentSnapshot documentSnapshot : querySnapshots) {
                                    Product product = new Product();
                                    product.setNome(documentSnapshot.getString("nome"));
                                    product.setPrecoVenda(Float.parseFloat(String.valueOf(documentSnapshot.get("precoUnitario"))));
                                    product.setId(documentSnapshot.getId());
                                    mProductList.add(product);
                                }

                                mProdutoAdapter = new ProdutoAdapter(mContext, mProductList);
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

        search.setOnClickListener(v -> Objects.requireNonNull(spinnerDialog).showSpinerDialog());

        mFirestore.collection(Helper.COLLECTION_PRODUTOS)
                .orderBy("dataRegisto")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                ll_produto.setVisibility(View.VISIBLE);
                pDialog.dismiss();
                mProductList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Product product = new Product();
                    product.setNome(documentSnapshot.getString("nome"));
                    product.setPrecoVenda(Float.parseFloat(String.valueOf(documentSnapshot.get("precoUnitario"))));
                    product.setId(documentSnapshot.getId());
                    mProductList.add(product);
                }

                mProdutoAdapter = new ProdutoAdapter(mContext, mProductList);
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
    }

    @Override
    public void onStop() {

        super.onStop();

        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


}