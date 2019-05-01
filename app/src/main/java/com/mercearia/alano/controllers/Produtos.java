package com.mercearia.alano.controllers;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.kinda.alert.KAlertDialog;
import com.mercearia.alano.database.FirebaseConnect;
import com.mercearia.alano.models.Produto;
import com.mercearia.alano.utils.Helper;

import java.util.HashMap;
import java.util.Map;

public class Produtos {
    private final Produto produtos;
    @NonNull
    private final Context context;
    private final FirebaseFirestore mFirestore;
    private KAlertDialog pDialog;
    private boolean save = true;

    public Produtos(Produto produtos, @NonNull Context context) {
        this.produtos = produtos;
        this.context = context;
        mFirestore = FirebaseConnect.getFireStore(context);
    }

    public boolean isSaved() {


        pDialog = new KAlertDialog(context, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor(Helper.COR_SECUNDARIA));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        // Create a new item
        Map<String, Object> produto = new HashMap<>();
        produto.put("nome", produtos.getNome());
        produto.put("precoCompra", produtos.getPrecoCompra());
        produto.put("quantidadeActual", produtos.getQuantidade());
        produto.put("quantidadeTotal", produtos.getQuantidade());
        produto.put("precoUnitario", produtos.getPrecoVenda());
        produto.put("dataRegisto", produtos.getDataRegisto());
        produto.put("lucro", produtos.getLucro());
        produto.put("valorCaixa", produtos.getValorEmCaixa());
        produto.put("quantVendida", produtos.getQuantidadeVendida());

        mFirestore.collection(Helper.COLLECTION_PRODUTOS)
                .add(produto)
                .addOnCompleteListener(documentReference -> pDialog.dismiss()).addOnFailureListener(e -> {
            pDialog.dismiss();
            new KAlertDialog(context, KAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Algo correu mal. Não foi possível adicionar novo produto!")
                    .show();
            save = false;
        });

        return save;
    }


    public boolean isReseted() {

        pDialog = new KAlertDialog(context, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor(Helper.COR_SECUNDARIA));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        // Create a new item
        Map<String, Object> produto = new HashMap<>();
        produto.put("valorCaixa", produtos.getValorEmCaixa());
        produto.put("quantVendida", produtos.getQuantidadeVendida());

        mFirestore.collection(Helper.COLLECTION_PRODUTOS)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {

                mFirestore.collection(Helper.COLLECTION_PRODUTOS)
                        .document(snapshot.getId())
                        .update(produto)
                        .addOnCompleteListener(documentReference ->
                                pDialog.dismiss()).addOnFailureListener(e -> {
                    pDialog.dismiss();
                    new KAlertDialog(context, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Algo correu mal. Não foi possível adicionar novo produto!")
                            .show();
                    save = false;

                });
            }
        });

        return save;
    }
}
