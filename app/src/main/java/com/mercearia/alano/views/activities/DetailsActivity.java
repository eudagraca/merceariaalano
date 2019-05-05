package com.mercearia.alano.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.kinda.alert.KAlertDialog;
import com.mercearia.alano.R;
import com.mercearia.alano.database.FirebaseConnect;
import com.mercearia.alano.models.Product;
import com.mercearia.alano.utils.Helper;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends Activity {

    private String nomeApagado;
    private Button tv_nome;
    private TextView tv_quantidadeStock, tv_quant_vendida;
    private TextView tv_precoVenda, tv_valor_caixa;
    private TextView tv_precoCompra, tv_qauntIn;
    private TextView tv_data;
    private LinearLayout linearLayout;
    private FirebaseFirestore mFirestore;
    private String productId;
    private KAlertDialog dialog;
    private LinearLayout llAddQuantity;
    private NumberPicker np_quantidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tv_quantidadeStock = findViewById(R.id.tv_quantidade_stock);
        tv_precoVenda = findViewById(R.id.tv_preco);
        tv_precoCompra = findViewById(R.id.tv_preco_compra);
        tv_data = findViewById(R.id.tv_data);
        tv_valor_caixa = findViewById(R.id.tv_valor_caixa_prod);
        llAddQuantity       = findViewById(R.id.ll_update);
        tv_quant_vendida = findViewById(R.id.tv_quantidade_vendida);
        tv_qauntIn       = findViewById(R.id.tv_qauntIn);
        np_quantidade    = findViewById(R.id.np_more_quant);
        linearLayout = findViewById(R.id.buttons);

        //buttons
        Button btnDelete = findViewById(R.id.bt_delete_product);
        tv_nome = findViewById(R.id.tv_nome);
        Button add  = findViewById(R.id.add);
        Button bt_add_more = findViewById(R.id.bt_add_more);

        Context context = getBaseContext();

        tv_nome.setOnClickListener(v -> onBackPressed());


        ScrollView scrollView = findViewById(R.id.scroll);
        scrollView.setVisibility(View.GONE);

        KAlertDialog kdialog = new KAlertDialog(DetailsActivity.this, KAlertDialog.PROGRESS_TYPE);
        kdialog.setTitleText("");
        kdialog.setCancelable(false);
        kdialog.show();

        //Geting  product id  comes from recycle  view

        productId = getIntent().getStringExtra("product_id");
        Product product = new Product();
        mFirestore = FirebaseConnect.getFireStore(context);

        //Geting data from Database CLoud FireStore
        assert productId != null;
        mFirestore.collection("produtos").document(productId).get().addOnSuccessListener(documentSnapshot -> {
            scrollView.setVisibility(View.VISIBLE);
            kdialog.dismiss();
            product.setNome(documentSnapshot.getString("nome"));
            product.setPrecoCompra(Float.parseFloat(String.valueOf(documentSnapshot.get("precoCompra"))));
            product.setPrecoVenda(Float.parseFloat(String.valueOf(documentSnapshot.get("precoUnitario"))));
            product.setQuantidade(Integer.parseInt(String.valueOf((documentSnapshot.get("quantidadeActual")))));
            product.setData(documentSnapshot.getString("dataRegisto"));
            product.setQuantidadeVendida(Integer.parseInt(String.valueOf(documentSnapshot.get("quantVendida"))));
            product.setValorEmCaixa(Float.parseFloat(String.valueOf(documentSnapshot.get("valorCaixa"))));
            int quantidadeTotal = Integer.parseInt(String.valueOf(documentSnapshot.get("quantidadeTotal")));

            tv_nome.setText(product.getNome());
            tv_valor_caixa.setText(String.valueOf(product.getValorEmCaixa()));
            nomeApagado = tv_nome.getText().toString();
            tv_precoVenda.setText(String.valueOf(product.getPrecoVenda()));
            tv_precoCompra.setText(String.valueOf(product.getPrecoCompra()));
            tv_quantidadeStock.setText(String.valueOf(product.getQuantidade()));
            tv_data.setText(product.getData());
            tv_qauntIn.setText(String.valueOf(quantidadeTotal));
            tv_quant_vendida.setText(String.valueOf(product.getQuantidadeVendida()));
        });

        //Deleting  product
        btnDelete.setOnClickListener(v -> {
            dialog = new KAlertDialog(DetailsActivity.this, KAlertDialog.WARNING_TYPE);
            dialog.setTitleText("Deseja apagar "+ tv_nome.getText().toString());
            dialog.setConfirmText("Sim");
            dialog.setCancelText("Nao");
            dialog.setConfirmClickListener(kAlertDialog -> {
                kdialog.show();
                kdialog.setCancelable(false);
                if (!mFirestore.collection(Helper.COLLECTION_PRODUTOS).document(productId).getPath().isEmpty()) {
                    mFirestore.collection(Helper.COLLECTION_PRODUTOS).document(productId).delete();
                    if (!mFirestore.collection(Helper.COLLECTION_DEBITS).document(productId).getPath().isEmpty()) {
                        mFirestore.collection(Helper.COLLECTION_DEBITS).document(productId).delete();
                    }
                    Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
                    intent.putExtra("message", nomeApagado);
                    startActivity(intent);
                    finish();
                    kdialog.dismiss();
                } else {
                    kdialog.dismiss();
                    dialog = new KAlertDialog(DetailsActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Nao foi possivel apagar");
                    dialog.show();
                }
            }).setCancelClickListener(kAlertDialog -> dialog.dismiss()).show();
        });

        //Add quantity to product
        bt_add_more.setOnClickListener(v -> {
            linearLayout.setVisibility(View.GONE);
            llAddQuantity.setVisibility(View.VISIBLE);
        });

        add.setOnClickListener(v->{

            KAlertDialog pDialog = new KAlertDialog(DetailsActivity.this, KAlertDialog.PROGRESS_TYPE);
            pDialog.setTitleText("");
            pDialog.setCancelable(false);
            pDialog.show();

            int quantidadeActual = Integer.parseInt(tv_quantidadeStock.getText().toString())
                    + np_quantidade.getValue();


            Map<String, Object> updateQuant = new HashMap<>();
            updateQuant.put("quantidadeActual",quantidadeActual);

            mFirestore.collection(Helper.COLLECTION_PRODUTOS).document(productId)
                    .update(updateQuant)
            .addOnSuccessListener(aVoid -> {

                KAlertDialog sdialog = new KAlertDialog(DetailsActivity.this, KAlertDialog.SUCCESS_TYPE);
                pDialog.dismissWithAnimation();
               sdialog.setTitleText("Adicionou mais quantidade");
               sdialog.setConfirmText("Ok");
               sdialog.setConfirmClickListener(kAlertDialog -> {
                   llAddQuantity.setVisibility(View.GONE);
                   startActivity(new Intent(DetailsActivity.this, MainActivity.class));
               }).show();

            });
            pDialog.dismissWithAnimation();
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog!= null){
            dialog.dismiss();
            dialog=null;
        }
    }
}
