package com.mercearia.alano.logic;

import android.content.Context;
import android.graphics.Color;

import com.google.firebase.firestore.FirebaseFirestore;
import com.kinda.alert.KAlertDialog;
import com.mercearia.alano.models.Categoria;
import com.mercearia.alano.models.Produto;
import com.mercearia.alano.utils.FirebaseConnect;
import com.mercearia.alano.utils.MyUtils;

import java.util.HashMap;
import java.util.Map;

public class LogicaProdutos {
    private Produto produtos;
    private Categoria categoria;
    private Context context;
    private FirebaseFirestore mFirestore;
    private KAlertDialog pDialog;

    public LogicaProdutos(Produto produtos, Categoria categoria, Context context) {
        this.produtos = produtos;
        this.categoria = categoria;
        this.context = context;
        mFirestore = FirebaseConnect.getFireStore(context);
    }

    public void isSaved() {
        pDialog = new KAlertDialog(context, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor(MyUtils.COR_SECUNDARIA));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        // Create a new item
        Map<String, Object> produto = new HashMap<>();
        produto.put("nome", produtos.getNome());
        produto.put("categoria", categoria.getNome());
        produto.put("precoCompra", produtos.getPrecoCompra());
        produto.put("quantidade", produtos.getQuantidade());
        produto.put("precoUnitario", produtos.getPrecoUnitario());
        produto.put("quantidadeUnitaria", produtos.getQuantidadeUnitaria());
        produto.put("dataRegisto", produtos.getDataRegisto());


        mFirestore.collection(MyUtils.COLLECTION_PRODUTOS)
                .add(produto)
                .addOnSuccessListener(documentReference -> {
                    pDialog.dismiss();
                    new KAlertDialog(context, KAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Adiccionou novo produto")
                            .show();
                }).addOnFailureListener(e -> {
            pDialog.dismiss();
            new KAlertDialog(context, KAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Algo correu mal. Não foi possível adicionar novo produto!")
                    .show();
        });
    }
}
