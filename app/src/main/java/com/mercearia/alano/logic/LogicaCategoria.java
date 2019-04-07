package com.mercearia.alano.logic;

import android.content.Context;
import android.graphics.Color;

import com.google.firebase.firestore.FirebaseFirestore;
import com.kinda.alert.KAlertDialog;
import com.mercearia.alano.models.Categoria;
import com.mercearia.alano.utils.FirebaseConnect;
import com.mercearia.alano.utils.MyUtils;

import java.util.HashMap;
import java.util.Map;

public class LogicaCategoria {
    private Categoria categoria;
    private Context context;
    private FirebaseFirestore mFirestore;
    private KAlertDialog pDialog;

    public LogicaCategoria(Categoria categoria, Context context) {
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
        Map<String, Object> categorias = new HashMap<>();
        categorias.put("nome", categoria.getNome());

        mFirestore.collection(MyUtils.COLLETION_CATEGORIA)
                .add(categorias)
                .addOnSuccessListener(documentReference -> {
                    pDialog.dismiss();
                    new KAlertDialog(context, KAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Adiccionou nova categoria")
                            .show();
                }).addOnFailureListener(e -> {
            pDialog.dismiss();
            new KAlertDialog(context, KAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Algo correu mal. Não foi possível adicionar a categoria!")
                    .show();
        });
    }
}
