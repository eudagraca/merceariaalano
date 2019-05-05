package com.mercearia.alano.views.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kinda.alert.KAlertDialog;
import com.mercearia.alano.R;
import com.mercearia.alano.database.FirebaseConnect;
import com.mercearia.alano.utils.Helper;

public class SecurityActivity extends AppCompatActivity {

    @NonNull
    private final Context context = this;
    @Nullable
    private FirebaseUser currentUser;
    private AlertDialog alertDialogAndroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View mView = layoutInflaterAndroid.inflate(R.layout.dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);

        // Initialize Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        final TextInputEditText userInputDialogEditText = mView.findViewById(R.id.scode);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setNegativeButton("Cancelar",
                        (dialogBox, id) -> System.exit(0));

        alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

        final Button buttonCodeAccess = mView.findViewById(R.id.btscode);
        buttonCodeAccess.setOnClickListener(click -> {

            if (String.valueOf(userInputDialogEditText.getText()).isEmpty()) {
                userInputDialogEditText.setError("Informe o codigo");
                userInputDialogEditText.requestFocus();
            } else {
                String code = String.valueOf(userInputDialogEditText.getText());
                getUser(code);
            }
        });
    }

    private void getUser(@NonNull String securityCode) {
        KAlertDialog dialog = new KAlertDialog(context, KAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("");
        dialog.setCancelable(false);
        dialog.show();

        FirebaseFirestore firestore = FirebaseConnect.getFireStore(context);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        firestore.collection(Helper.COLLECTION_USER).document(currentUser.getUid())
                .get().addOnSuccessListener(documentSnapshot -> {

            String n = documentSnapshot.getString("securityCode");
            assert n != null;
            if (securityCode.equals(n)) {
                dialog.dismiss();
                startActivity(new Intent(SecurityActivity.this, MainActivity.class));
                finish();
            } else {
                dialog.dismiss();
                startActivity(new Intent(SecurityActivity.this, SecurityActivity.class));
                finish();
                Toast.makeText(context, "Acesso negado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser == null) {
            startActivity(new Intent(this, AccessActivity.class));
            finish();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialogAndroid != null) {
            alertDialogAndroid.dismiss();
            alertDialogAndroid = null;
        }
    }
}