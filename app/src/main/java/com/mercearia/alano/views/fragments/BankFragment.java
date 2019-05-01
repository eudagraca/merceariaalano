package com.mercearia.alano.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.kinda.alert.KAlertDialog;
import com.mercearia.alano.R;
import com.mercearia.alano.controllers.Produtos;
import com.mercearia.alano.database.FirebaseConnect;
import com.mercearia.alano.models.Produto;
import com.mercearia.alano.utils.Helper;
import com.mercearia.alano.views.activities.MainActivity;

import java.util.Objects;

public class BankFragment extends Fragment {


    private TextView tv_valor;

    private double money;

    public BankFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bank, container, false);

        tv_valor = view.findViewById(R.id.tv_valor_total);
        Button button_reset = view.findViewById(R.id.btn_reset);

        Context context = getContext();

        FirebaseFirestore firestore = FirebaseConnect.getFireStore(context);

        KAlertDialog dialog = new KAlertDialog(Objects.requireNonNull(context), KAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("");
        dialog.setCancelable(false);
        dialog.show();

        firestore.collection(Helper.COLLECTION_PRODUTOS).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    money = 0;
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        money += Double.parseDouble(String.valueOf(snapshot.get("valorCaixa")));
                    }
                    if (money > 0) {
                        tv_valor.setText(String.valueOf(money));
                    } else {
                        tv_valor.setText(String.valueOf(money));
                        Helper.alertaNegativa(context, "Não faturou nada neste mês");
                    }
                    dialog.dismissWithAnimation();
                }).addOnFailureListener(e -> dialog.dismissWithAnimation());


        button_reset.setOnClickListener(v -> {
            Produto produto = new Produto();
            produto.setQuantidadeVendida(0);
            produto.setValorEmCaixa(0);
            Produtos produtos = new Produtos(produto, context);

            if (produtos.isReseted()) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
