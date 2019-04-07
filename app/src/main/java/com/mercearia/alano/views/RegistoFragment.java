package com.mercearia.alano.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.kinda.alert.KAlertDialog;
import com.mercearia.alano.R;
import com.mercearia.alano.logic.LogicaCategoria;
import com.mercearia.alano.logic.LogicaProdutos;
import com.mercearia.alano.models.Categoria;
import com.mercearia.alano.models.Produto;
import com.mercearia.alano.utils.FirebaseConnect;
import com.mercearia.alano.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class RegistoFragment extends Fragment {

    //Components
    private EditText editNome;
    private Spinner spinnerCategoriaReg;
    private EditText editPrecoCompra;
    private EditText editQuantidade;
    private EditText editQuantidadeUn;
    private EditText editPrecoUnidade;
    private EditText editAddCategoria;
    private TextView tvAddCategria, tvSaveCategoria;
    private Context context;

    public RegistoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registo, container, false);
        //Init Components
        spinnerCategoriaReg = view.findViewById(R.id.spinner_categoria_reg);
        editNome            = view.findViewById(R.id.edit_nome);
        editPrecoCompra     = view.findViewById(R.id.edit_preco_compra);
        editQuantidade      = view.findViewById(R.id.edit_quantidade);
        editQuantidadeUn    = view.findViewById(R.id.edit_quantidade_un);
        editPrecoUnidade    = view.findViewById(R.id.edit_preco_unidade);
        Button buttonGravar = view.findViewById(R.id.button_gravar);
        tvAddCategria       = view.findViewById(R.id.tv_add_categoria);
        tvSaveCategoria     = view.findViewById(R.id.tv_save_categoria);
        editAddCategoria    = view.findViewById(R.id.edit_categoria);

        context = getContext();

        FirebaseFirestore mFirestore = FirebaseConnect.getFireStore(context);
        KAlertDialog pDialog = new KAlertDialog(context, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor(MyUtils.COR_SECUNDARIA));
        pDialog.setTitleText("Preparando a tela");
        pDialog.setCancelable(false);
        pDialog.show();

        final List<String> categories = new ArrayList<>();
        mFirestore.collection(MyUtils.COLLETION_CATEGORIA).orderBy("nome")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        pDialog.dismiss();
                        for (QueryDocumentSnapshot snapshot : Objects.requireNonNull(task.getResult())) {
                            String categoria = (String) snapshot.get("nome");
                            categories.add(categoria);
                        }
                        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, categories);
                        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCategoriaReg.setAdapter(myAdapter);
                    }
                });

        Produto produto = new Produto();
        Categoria categoria = new Categoria();

        Toast.makeText(context, produto.getDataRegisto(), Toast.LENGTH_SHORT).show();

        buttonGravar.setOnClickListener(v -> {

            if (isValid()) {
                produto.setNome(editNome.getText().toString());
                produto.setPrecoCompra(Double.parseDouble(editPrecoCompra.getText().toString()));
                produto.setPrecoUnitario(Double.parseDouble(editPrecoUnidade.getText().toString()));
                produto.setQuantidade(editQuantidade.getText().toString());
                produto.setQuantidadeUnitaria(editQuantidadeUn.getText().toString());
                categoria.setNome(spinnerCategoriaReg.getSelectedItem().toString());
                produto.setCategoria(categoria);
                LogicaProdutos prod = new LogicaProdutos(produto, categoria, context);
                prod.isSaved();
            }

        });

        tvAddCategria.setOnClickListener(v -> {
            tvAddCategria.setVisibility(View.GONE);
            spinnerCategoriaReg.setVisibility(View.GONE);
            tvSaveCategoria.setVisibility(View.VISIBLE);
            editAddCategoria.setVisibility(View.VISIBLE);

            tvSaveCategoria.setOnClickListener(c -> {
                if (isValidCategria()) {
                    LogicaCategoria logicaCategoria = new LogicaCategoria(categoria, context);
                    categoria.setNome(editAddCategoria.getText().toString());

                    logicaCategoria.isSaved();

                    tvAddCategria.setVisibility(View.VISIBLE);
                    spinnerCategoriaReg.setVisibility(View.VISIBLE);
                    tvSaveCategoria.setVisibility(View.GONE);
                    editAddCategoria.setVisibility(View.GONE);
                    refreshFragment();
                }
            });
        });
        return view;
    }

    private void refreshFragment() {
        int fl_framelayout;
        RegistoFragment registoFragment;
        registoFragment = new RegistoFragment();
        fl_framelayout = R.id.framelayout_container;
        assert getFragmentManager() != null;
        MyUtils.changeFragment(fl_framelayout, registoFragment, getFragmentManager());

    }

    private boolean isValid() {
        boolean isValid = false;
        if (editNome.getText().toString().length() < 6) {
            editNome.setError("Introduza o nome do Produto");
            editNome.requestFocus();
        } else if (spinnerCategoriaReg.getSelectedItem().toString().isEmpty()) {
            MyUtils.alertaNegativa(context, "Seleccione a categoria");
        } else if (editPrecoUnidade.getText().toString().isEmpty() ||
                Double.parseDouble(editPrecoUnidade.getText().toString()) <= 0
        ) {
            editPrecoUnidade.setError("Introduza o preço da venda de cada um");
            editPrecoUnidade.requestFocus();
        } else if (editPrecoCompra.getText().toString().isEmpty() ||
                Double.parseDouble(editPrecoCompra.getText().toString()) <= 0
        ) {
            editPrecoCompra.setError("Introduza o preço da compra do produto");
            editPrecoCompra.requestFocus();
        } else if (editQuantidade.getText().toString().isEmpty() ||
                Integer.parseInt(editQuantidade.getText().toString()) <= 0) {
            editQuantidade.setError("Introduza a quantidade");
            editQuantidade.requestFocus();

        } else if (editQuantidadeUn.getText().toString().isEmpty() ||
                Integer.parseInt(editQuantidadeUn.getText().toString()) <= 0) {
            editQuantidadeUn.setError("Introduza a quantidade total dos produtos");
            editQuantidadeUn.requestFocus();
        } else {
            isValid = true;
        }
        return isValid;
    }

    boolean isValidCategria() {

        boolean isValid = false;

        if (editAddCategoria.getText().toString().isEmpty()) {
            editAddCategoria.setError("Introduza a categoria");
            editAddCategoria.requestFocus();
        } else if (editAddCategoria.getText().toString().length() < 6) {
            editAddCategoria.setError("Dê um nome detalhado a categoria");
            editAddCategoria.requestFocus();
        } else {
            isValid = true;
        }
        return isValid;
    }
}
