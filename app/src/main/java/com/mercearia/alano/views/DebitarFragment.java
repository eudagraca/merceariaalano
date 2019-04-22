package com.mercearia.alano.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.kinda.alert.KAlertDialog;
import com.mercearia.alano.R;
import com.mercearia.alano.models.Produto;
import com.mercearia.alano.utils.FirebaseConnect;
import com.mercearia.alano.utils.Helper;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class DebitarFragment extends Fragment {
    private TextView tv_quant, tv_productSelected, tv_product_id;
    private TextView textView_preco;
    private Context context;
    private int quantidade;
    private int quantidadeDebiitada;
    private int quantidadeVendida;
    private NumberPicker numberPicker;
    private KAlertDialog pDialog;
    private FirebaseFirestore mFirestore;
    private SpinnerDialog spinnerDialog;
    private Produto p;
    private KAlertDialog dialog;

    public DebitarFragment() {
        // Required empty public constructor
        p = new Produto();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_debitar, container, false);

        textView_preco = view.findViewById(R.id.tv_priced);
        tv_productSelected = view.findViewById(R.id.tv_product_selected);
        tv_quant = view.findViewById(R.id.tv_quant);
        Button btnDebit = view.findViewById(R.id.btn_vender);
        Button bs = view.findViewById(R.id.search);
        numberPicker = view.findViewById(R.id.np_quant);
        tv_product_id = view.findViewById(R.id.tv_product_id);

        context = getActivity();
        assert context != null;

        //Laoding
        pDialog = new KAlertDialog(context, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor(Helper.COR_SECUNDARIA));
        pDialog.setTitleText("");
        pDialog.setCancelable(false);
        pDialog.show();

        //init database firestore
        mFirestore = FirebaseConnect.getFireStore(context);

        numberPicker.setFadingEdgeEnabled(true);

        // Set scroller enabled
        numberPicker.setScrollerEnabled(true);
        // Set wrap selector wheel
        numberPicker.setWrapSelectorWheel(true);
        // OnClickListener
        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            quantidadeDebiitada = newVal;
             });

        //Search and list products
        final ArrayList<String> produtos = new ArrayList<>();
        mFirestore.collection(Helper.COLLECTION_PRODUTOS).orderBy("nome")
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
                    spinnerDialog = new SpinnerDialog((Activity) context, produtos, selec, close);// With 	Animation
                    pDialog.dismissWithAnimation();
                    spinnerDialog.setCancellable(true); // for cancellable
                    spinnerDialog.setShowKeyboard(false);// for open keyboard by default
                    spinnerDialog.bindOnSpinerListener((item, position) -> {
                        tv_productSelected.setText(item);
                        getData(item);
                    });
                });

        bs.setOnClickListener(v -> spinnerDialog.showSpinerDialog());

        btnDebit.setOnClickListener(v -> {
            dialog = new KAlertDialog(context, KAlertDialog.WARNING_TYPE);
            dialog.setTitleText("Deseja debitar " + tv_productSelected.getText().toString());
            dialog.setConfirmText("Sim");
            dialog.setCancelText("Nao");
            dialog.setConfirmClickListener(kAlertDialog -> {

                if (isValid()) {
                    pDialog = new KAlertDialog(context, KAlertDialog.PROGRESS_TYPE);
                    pDialog.setTitleText("");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    debitQuantity();
                } else {
                    Helper.alertaNegativa(context, "Seleccione o produto a debitar");
                    bs.setTextColor(getResources().getColor(R.color.md_red_A700));
                    bs.setTextSize(20);
                    bs.requestFocus();
                    dialog.dismiss();
                }

            }).setCancelClickListener(kAlertDialog -> dialog.dismiss()).show();
        });

        return view;
    }

    /**
     * Get quantity of an especific product
     *
     * @param produto
     */
    private void getData(String produto) {
        pDialog = new KAlertDialog(context, KAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("");
        pDialog.setCancelable(false);
        pDialog.show();
        mFirestore.collection(Helper.COLLECTION_PRODUTOS).whereEqualTo("nome", produto)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                for (QueryDocumentSnapshot snapshot : Objects.requireNonNull(task.getResult())) {
                    quantidade = Integer.parseInt(String.valueOf(snapshot.get("quantidadeActual")));
                    tv_quant.setText(String.valueOf(quantidade));
                    tv_product_id.setText(snapshot.getId());
                    quantidadeVendida = Integer.parseInt(String.valueOf(snapshot.get("quantVendida")));
                    textView_preco.setText(String.valueOf(snapshot.get("precoUnitario")));
                    p.setValorEmCaixa(Float.parseFloat(String.valueOf(snapshot.get("valorCaixa"))));
                    p.setPrecoVenda(Float.parseFloat(String.valueOf(snapshot.get("precoUnitario"))));

                    // Set quantity
                    if (Integer.parseInt(tv_quant.getText().toString()) >= 1) {
                        numberPicker.setMaxValue(quantidade);
                        numberPicker.setMinValue(1);
                        numberPicker.setValue(quantidade);
                        numberPicker.setEnabled(true);
                        pDialog.dismissWithAnimation();
                    } else {
                        numberPicker.setValue(0);
                        numberPicker.setMinValue(0);
                        numberPicker.setEnabled(false);
                    }

                    pDialog.dismiss();
                }
            }
        });

    }

    private void debitQuantity() {
        p.setQuantidade(quantidade - numberPicker.getValue());
        Map<String, Object> data = new HashMap<>();
        data.put("quantidadeActual", p.getQuantidade());
        data.put("quantVendida", quantidadeVendida + quantidadeDebiitada);
        data.put("valorCaixa", p.getValorEmCaixa() + (quantidadeDebiitada * p.getPrecoVenda()));

        mFirestore.collection(Helper.COLLECTION_PRODUTOS).document(tv_product_id.getText().toString())
                .update(data).addOnSuccessListener(aVoid -> {
            data.put("data", p.getDataRegisto());

            FirebaseFirestore firestore = FirebaseConnect.getFireStore(context);
            firestore.collection("debitos").document(tv_product_id.getText().toString())
                    .set(data).addOnSuccessListener(s -> {

                KAlertDialog dialog = new KAlertDialog(context, KAlertDialog.SUCCESS_TYPE);
                dialog.setTitleText("Debitou um produto");
                dialog.setConfirmClickListener(kAlertDialog -> {
                    pDialog.dismissWithAnimation();
                    startActivity(new Intent(context, MainActivity.class));
                    onDestroy();
                }).show();


            });
        });
    }

    private boolean isValid() {
        return !tv_productSelected.getText().toString().equals("");
    }

}
