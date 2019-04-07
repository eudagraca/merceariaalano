package com.mercearia.alano.views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mercearia.alano.R;
import com.mercearia.alano.adapters.ProdutoAdapter;
import com.mercearia.alano.models.Produto;
import com.mercearia.alano.utils.FirebaseConnect;
import com.mercearia.alano.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProdutoFragment extends Fragment {

    private List<Produto> mProdutoList;
    private Context mContext;
    private ProdutoAdapter mProdutoAdapter;
    private RecyclerView rv_product;
    private FirebaseFirestore mFirestore;
    private ProductDetails productDetails;
    private FragmentManager fragmentManager;
    private int fl_framelayout;
    public ProdutoFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_produto, container, false);
        rv_product = view.findViewById(R.id.rv_product);
        mContext   = getContext();
        rv_product.setHasFixedSize(true);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(mContext, 2);
        rv_product.setLayoutManager(mGridLayoutManager);
        productDetails = new ProductDetails();
        fragmentManager = getFragmentManager();
        fl_framelayout = R.id.framelayout_container;

        //Init my database!!
        mFirestore = FirebaseConnect.getFireStore(mContext);

        mProdutoList = new ArrayList<>();
        mFirestore.collection(MyUtils.COLLECTION_PRODUTOS)
                .get()
                .addOnCompleteListener(task -> {
                    mProdutoList.clear();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                            Produto produto = new Produto();
                            produto.setNome((String) documentSnapshot.get("nome"));
                            produto.setPrecoUnitario((Double) documentSnapshot.get("precoUnitario"));
                            produto.setId(documentSnapshot.getId());
                            mProdutoList.add(produto);
                        }
                    }
                    mProdutoAdapter = new ProdutoAdapter(mContext, mProdutoList);
                    if (mProdutoAdapter.getItemCount() > 0) {
                        rv_product.setAdapter(mProdutoAdapter);
                        mProdutoAdapter.notifyDataSetChanged();
                    }
                    mProdutoAdapter.setOnClickListener(v->{
                        String id = getSelectedUserId(v);
                        Bundle  bundle = new Bundle();
                        bundle.putString("product_id", id);
                        productDetails.setArguments(bundle);
                        MyUtils.changeFragment(fl_framelayout, productDetails, fragmentManager);
                    });
                });

        return view;
    }

    private String getSelectedUserId(int position) {
        TextView tv = Objects.requireNonNull(rv_product.findViewHolderForAdapterPosition(position)).itemView.findViewById(R.id.tv_id);
        return tv.getText().toString();
    }
}