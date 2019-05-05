package com.mercearia.alano.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.mercearia.alano.R;
import com.mercearia.alano.models.Product;

import java.util.List;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.produtoViewHolder> {

    private final Context mContext;
    private final List<Product> mProductList;
    private OnItemClick mListener;

    public interface OnItemClick {
        void onClick(int position);
    }

    public void setOnClickListener(OnItemClick listener) {
        mListener = listener;
    }

    public ProdutoAdapter(Context mContext, List<Product> mProductList) {
        this.mContext = mContext;
        this.mProductList = mProductList;
    }

    @NonNull
    @Override
    public produtoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View mView = layoutInflater.inflate(R.layout.product_item_list, viewGroup, false);


        return new produtoViewHolder(mView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull produtoViewHolder holder, int position) {

        Product product = mProductList.get(position);
        holder.tv_id.setText(product.getId());
        holder.tv_nome.setText(product.getNome());
        holder.tv_preco.setText(String.valueOf(product.getPrecoVenda()));
    }

    @Override
    public int getItemCount() {

        return mProductList.size();
    }

    static class produtoViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_nome;
        private final TextView tv_preco;
        private final TextView tv_id;

        produtoViewHolder(@NonNull View itemView, @Nullable final OnItemClick itemClick) {
            super(itemView);
            tv_nome = itemView.findViewById(R.id.tv_nome);
            tv_preco = itemView.findViewById(R.id.tv_price);
            tv_id = itemView.findViewById(R.id.tv_id);

            itemView.setOnClickListener(view -> {
                if (itemClick != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) { // this check, is to prevent errors when clicking a card that has no position bound to it (for example, a card that we deleted seconds ago)
                        itemClick.onClick(position);
                    }
                }
            });
        }

    }
}
