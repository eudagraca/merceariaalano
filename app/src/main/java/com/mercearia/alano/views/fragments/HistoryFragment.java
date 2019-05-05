package com.mercearia.alano.views.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.kinda.alert.KAlertDialog;
import com.mercearia.alano.R;
import com.mercearia.alano.adapters.DebitAdapter;
import com.mercearia.alano.database.FirebaseConnect;
import com.mercearia.alano.models.Debit;
import com.mercearia.alano.utils.Helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class HistoryFragment extends Fragment {

    private final HashSet<String> items = new HashSet<>();
    private RecyclerView recyclerView;
    @Nullable
    private DebitAdapter debitAdapter;
    private ArrayList<Debit> debitList;
    @Nullable
    private Context context;
    @NonNull
    private ArrayList<String> debitos = new ArrayList<>();
    @Nullable
    private SpinnerDialog spinnerDialog;

    public HistoryFragment() {
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
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = view.findViewById(R.id.rv_debit);

        int orientation = getResources().getConfiguration().orientation;
        LinearLayoutManager layoutManager;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(context, 2);
        } else {
            layoutManager = new GridLayoutManager(context, 1);
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        context = getContext();
        recyclerView.setHasFixedSize(true);
        debitList = new ArrayList<>();

        FirebaseFirestore mFirestore = FirebaseConnect.getFireStore(Objects.requireNonNull(context));

        KAlertDialog dialog = new KAlertDialog(Objects.requireNonNull(context), KAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("");
        dialog.setCancelable(false);
        dialog.show();

        mFirestore.collection(Helper.COLLECTION_DEBITS).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Debit debit = new Debit();

                        debit.setName(String.valueOf(snapshot.get("nome")));
                        items.add(debit.getName());
                    }
                    debitos = new ArrayList<>(items);
                    spinnerDialog = new SpinnerDialog(getActivity(), debitos, Helper.SELECT, R.style.DialogAnimations_SmileWindow, Helper.CLOSE);// With 	Animation
                    spinnerDialog.setCancellable(true); // for cancellable
                    spinnerDialog.setShowKeyboard(false);// for open keyboard by default

                    dialog.dismiss();

                    spinnerDialog.bindOnSpinerListener((item, position) -> {
                        debitList.clear();
                        KAlertDialog d = new KAlertDialog(Objects.requireNonNull(context), KAlertDialog.PROGRESS_TYPE);
                        d.setTitleText("");
                        d.setCancelable(false);
                        d.show();

                        mFirestore.collection(Helper.COLLECTION_PRODUTOS)
                                .whereEqualTo("nome", item).get()
                                .addOnSuccessListener(queryDocument -> {

                                    for (QueryDocumentSnapshot snapshot : queryDocument) {
                                        Debit debit = new Debit();

                                        debit.setQuantidadeVendida(Integer.valueOf(String.valueOf(snapshot.get("quantVendida"))));
                                        debit.setQuantidadeRemanescente(Integer.valueOf(String.valueOf(snapshot.get("quantidadeActual"))));
                                        debit.setData(String.valueOf(snapshot.get("data")));
                                        debit.setName(String.valueOf(snapshot.get("nome")));
                                        debitList.add(debit);
                                        d.dismiss();
                                    }
                                    debitAdapter = new DebitAdapter(context, debitList);
                                    recyclerView.setAdapter(debitAdapter);
                                    debitAdapter.notifyDataSetChanged();
                                    d.dismiss();
                                });
                    });
                });

        mFirestore.collection(Helper.COLLECTION_DEBITS).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    debitList.clear();
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Debit debit = new Debit();

                        debit.setQuantidadeVendida(Integer.valueOf(String.valueOf(snapshot.get("quantVendida"))));
                        debit.setQuantidadeRemanescente(Integer.valueOf(String.valueOf(snapshot.get("quantidadeActual"))));
                        debit.setData(String.valueOf(snapshot.get("data")));
                        debit.setName(String.valueOf(snapshot.get("nome")));
                        debitList.add(debit);
                    }
                    debitAdapter = new DebitAdapter(context, debitList);
                    recyclerView.setAdapter(debitAdapter);
                    debitAdapter.notifyDataSetChanged();

                    dialog.dismiss();
                });

        view.findViewById(R.id.search_history).setOnClickListener(v -> Objects.requireNonNull(spinnerDialog).showSpinerDialog());

        return view;
    }


}