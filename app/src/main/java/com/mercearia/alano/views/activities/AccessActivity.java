package com.mercearia.alano.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kinda.alert.KAlertDialog;
import com.mercearia.alano.R;
import com.mercearia.alano.database.CurrentUser;
import com.mercearia.alano.utils.Helper;

import java.util.Objects;

public class AccessActivity extends Activity {

    private TextInputEditText text_email;
    private TextInputEditText text_pass;
    private FirebaseAuth mAuth;
    private KAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        text_email = findViewById(R.id.tv_email);
        TextView text_rg = findViewById(R.id.rg);
        text_pass = findViewById(R.id.tv_pass);
        Button btn_reg = findViewById(R.id.bt_access);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        dialog = new KAlertDialog(AccessActivity.this, KAlertDialog.PROGRESS_TYPE);

        SharedPreferences sp = getSharedPreferences(Helper.SP_NAME, Context.MODE_PRIVATE);
        if (!sp.getBoolean("first", false)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("first", true);
            editor.apply();
            Intent intent = new Intent(this, IntroActivity.class); // Call the AppIntro java class
            startActivity(intent);
        }

        //  Initialize SharedPreferences
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

        //  If the activity has never started before...
        if (isFirstStart) {
            //  Launch app intro
            final Intent i = new Intent(getBaseContext(), IntroActivity.class);

            runOnUiThread(() -> startActivity(i));

            //  Make a new preferences editor
            SharedPreferences.Editor e = getPrefs.edit();

            //  Edit preference to make it false because we don't want this to run again
            e.putBoolean("firstStart", false);
            //  Apply changes
            e.apply();
        } else {
            btn_reg.setOnClickListener(v -> {

                if (isValid()) {
                    login();
                }
                dialog.dismiss();
            });

            text_rg.setOnClickListener(v -> {

                Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        dialog = new KAlertDialog(AccessActivity.this, KAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("");
        dialog.setCancelable(false);
        dialog.show();

        FirebaseUser currentUser = CurrentUser.currentUser(this);

        if (currentUser != null) {
            dialog.dismiss();
            startActivity(new Intent(this, SecurityActivity.class));
            finish();

        } else {
            dialog.dismiss();

        }

    }

    private boolean isValid() {
        boolean isValid = false;
        if (Objects.requireNonNull(text_email.getText()).toString().isEmpty()) {
            text_email.setError("Introduza um email");
            text_email.requestFocus();
        } else if (!Objects.requireNonNull(text_email.getText()).toString().contains("@")) {
            text_email.setError("Introduza um email válido");
            text_email.requestFocus();
        } else if (!Objects.requireNonNull(text_email.getText()).toString().contains(".")
        ) {
            text_email.setError("Introduza um email válido");
            text_email.requestFocus();
        } else if (Objects.requireNonNull(text_pass.getText()).toString().isEmpty() ||
                String.valueOf(text_pass.getText()).length() < 4
        ) {
            text_pass.setError("Introduza uma senha com pelo menos 4 digitos");
            text_pass.requestFocus();
        } else {
            isValid = true;
        }
        return isValid;
    }

    private void login() {
        KAlertDialog alertDialog = new KAlertDialog(AccessActivity.this, KAlertDialog.PROGRESS_TYPE);
        alertDialog.setTitleText("");
        alertDialog.setCancelable(false);
        alertDialog.show();

        mAuth.signInWithEmailAndPassword(String.valueOf(text_email.getText()), String.valueOf(text_pass.getText()))
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                            startActivity(new Intent(AccessActivity.this, SecurityActivity.class));
                            finish();
                            alertDialog.dismiss();

                    } else {
                        alertDialog.dismiss();
                        Toast.makeText(AccessActivity.this, "Accesso Proibido.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> alertDialog.dismiss());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dialog!=null){
            dialog.dismiss();
            dialog=null;
        }
    }
}