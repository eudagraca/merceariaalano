package com.mercearia.alano.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mercearia.alano.R;
import com.mercearia.alano.models.Debit;

import java.util.List;

public class DebitAdapter extends RecyclerView.Adapter<DebitAdapter.debitAdapter> {

    private final Context mContext;
    private final List<Debit> mDebitList;

    public DebitAdapter(Context mContext, List<Debit> mDebitList) {
        this.mContext = mContext;
        this.mDebitList = mDebitList;
    }

    @NonNull
    @Override
    public debitAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View mView = layoutInflater.inflate(R.layout.history_list, parent, false);

        return new DebitAdapter.debitAdapter(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull debitAdapter holder, int position) {

        Debit debit = mDebitList.get(position);
        holder.tv_nome.setText(debit.getName());
        holder.tv_quantActual.setText(String.valueOf(debit.getQuantidadeRemanescente()));
        holder.tv_quant_Debit.setText(String.valueOf(debit.getQuantidadeVendida()));
        holder.tv_data.setText(debit.getData());
    }

    @Override
    public int getItemCount() {
        return mDebitList.size();
    }

    static class debitAdapter extends RecyclerView.ViewHolder {

        private final TextView tv_nome;
        private final TextView tv_data;
        private final TextView tv_quantActual;
        private final TextView tv_quant_Debit;


        debitAdapter(@NonNull View itemView) {
            super(itemView);

            tv_data = itemView.findViewById(R.id.product_date_debit);
            tv_nome = itemView.findViewById(R.id.product_name_debit);
            tv_quant_Debit = itemView.findViewById(R.id.product_quant_vendida__debit);
            tv_quantActual = itemView.findViewById(R.id.product_quantAct_debit);
        }
    }
}
