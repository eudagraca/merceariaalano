package com.mercearia.alano.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.mercearia.alano.R;
import com.mercearia.alano.controllers.ProductsController;
import com.mercearia.alano.models.Product;
import com.mercearia.alano.utils.Helper;
import com.mercearia.alano.views.activities.MainActivity;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.Objects;

public class RegistoFragment extends Fragment {

    //Components
    private TextInputEditText editNome;
    private TextInputEditText editPrecoCompra;
    private TextInputEditText editPrecoUnidade;
    @Nullable
    private Context context;
    private int quantidade;

    public RegistoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registo, container, false);

        //Init Components
        editNome = view.findViewById(R.id.edit_nome);
        editPrecoCompra = view.findViewById(R.id.edit_preco_compra);
        NumberPicker np_quantidade = view.findViewById(R.id.np_quantidade);
        editPrecoUnidade = view.findViewById(R.id.edit_preco_unidade);
        Button buttonGravar = view.findViewById(R.id.button_gravar);
        //LinearLayout ll_register = view.findViewById(R.id.ll_register);

        context = getContext();
        //ll_register.setVisibility(View.GONE);

        //Quanti
        np_quantidade.setMaxValue(1000);
        np_quantidade.setOnValueChangedListener((picker, oldVal, newVal) -> quantidade = oldVal);

        Product product = new Product();

        //Save Product
        buttonGravar.setOnClickListener(v -> {
            if (isValid()) {
                product.setNome(Objects.requireNonNull(editNome.getText()).toString());
                product.setPrecoCompra(Float.parseFloat(Objects.requireNonNull(editPrecoCompra.getText()).toString()));
                product.setPrecoVenda(Float.parseFloat((Objects.requireNonNull(editPrecoUnidade.getText()).toString())));
                product.setQuantidade(quantidade);
                ProductsController prod = new ProductsController(product, Objects.requireNonNull(context));

                if (prod.isSaved()) {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    //Validate form
    private boolean isValid() {
        boolean isValid = false;
        if (Objects.requireNonNull(editNome.getText()).toString().length() < 6) {
            editNome.setError("Introduza o nome do Product");
            editNome.requestFocus();
        } else if (Objects.requireNonNull(editPrecoUnidade.getText()).toString().isEmpty() ||
                Double.parseDouble(editPrecoUnidade.getText().toString()) <= 0
        ) {
            editPrecoUnidade.setError("Introduza o preço da venda de cada um");
            editPrecoUnidade.requestFocus();
        } else if (Objects.requireNonNull(editPrecoCompra.getText()).toString().isEmpty() ||
                Double.parseDouble(editPrecoCompra.getText().toString()) <= 0
        ) {
            editPrecoCompra.setError("Introduza o preço da compra do produto");
            editPrecoCompra.requestFocus();
        } else if (quantidade <= 0) {
            Helper.alertaNegativa(Objects.requireNonNull(context), "Defina a quantidade a registar");
        } else {
            isValid = true;
        }
        return isValid;
    }
}