package com.mercearia.alano.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mercearia.alano.R;
import com.mercearia.alano.models.Produto;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.produtoViewHolder> {

    private Context mContext;
    private List<Produto> mProdutoList;
    private OnItemClick mListener;

    public interface OnItemClick {
        void onClick(int position);
    }

    public void setOnClickListener(OnItemClick listener) {
        mListener = listener;
    }

    public ProdutoAdapter(Context mContext, List<Produto> mProdutoList) {
        this.mContext = mContext;
        this.mProdutoList = mProdutoList;
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

        Produto produto = mProdutoList.get(position);
        holder.tv_id.setText(produto.getId());
        holder.tv_nome.setText(produto.getNome());
        holder.tv_preco.setText(String.valueOf(produto.getPrecoVenda()));
    }

    @Override
    public int getItemCount() {

        return mProdutoList.size();
    }

    static class produtoViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_nome, tv_preco, tv_id;

        produtoViewHolder(@NonNull View itemView, final OnItemClick itemClick) {
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
