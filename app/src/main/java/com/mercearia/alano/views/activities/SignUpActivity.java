package com.mercearia.alano.views.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kinda.alert.KAlertDialog;
import com.mercearia.alano.R;
import com.mercearia.alano.database.FirebaseConnect;
import com.mercearia.alano.models.User;
import com.mercearia.alano.utils.Helper;

import java.util.HashMap;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText text_email;
    private TextInputEditText text_pass;
    private TextInputEditText text_securityCode;
    private final Context context;
    private final User user;
    private final FirebaseAuth mAuth;
    public SignUpActivity() {
        user = new User();
        context = this;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        text_email = findViewById(R.id.tv_email);
        text_securityCode = findViewById(R.id.tv_security_code);
        text_pass = findViewById(R.id.tv_pass_reg);
        TextView text_login = findViewById(R.id.login);
        Button btn_reg = findViewById(R.id.bt_signUp);
        text_securityCode.setVisibility(View.VISIBLE);


        text_login.setOnClickListener(v -> {
            startActivity(new Intent(context, AccessActivity.class));
            finish();
        });
        btn_reg.setOnClickListener(v -> {

            if (isValid()) {

                user.setEmail(String.valueOf(text_email.getText()));
                String email = String.valueOf(text_email.getText())
                        .replaceAll("@.*", "");
                user.setName(email);
                user.setSecurityCode(String.valueOf(text_securityCode.getText()));

                String pass = String.valueOf(text_pass.getText());
                createUser(pass);
            }
        });


    }

    //Validate form
    @SuppressLint("SetTextI18n")
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
                String.valueOf(text_pass.getText()).length() < 6
        ) {
            text_pass.setError("Introduza uma senha com pelo menos 4 digitos");
            text_pass.requestFocus();
        } else if (Objects.requireNonNull(text_securityCode.getText()).toString().isEmpty()) {
            text_securityCode.setText("Defina o teu código de seguraça para a aplicação");
            text_securityCode.requestFocus();
        } else if (Objects.requireNonNull(text_securityCode.getText()).toString().length() < 4) {
            text_securityCode.setError("O teu código deve ter pelo menos 4 digitos");
            text_securityCode.requestFocus();
        } else {
            isValid = true;
        }
        return isValid;
    }


    private void createUser(@NonNull String password) {
        KAlertDialog dialog = new KAlertDialog(context, KAlertDialog.PROGRESS_TYPE);
        dialog.setCancelable(false);
        dialog.setTitleText("");
        dialog.show();

        mAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser fireUser = mAuth.getCurrentUser();
                        if (fireUser != null) {
                            saveCode(fireUser.getUid());
                            startActivity(new Intent(SignUpActivity.this, AccessActivity.class));
                            finish();
                            Toast.makeText(context, "Criou uma conta", Toast.LENGTH_SHORT).show();
                        }
                        mAuth.signOut();

                    } else {
                        Toast.makeText(this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "Não foi possivel registar usuario", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                });
    }

    private void saveCode(@NonNull String uid) {

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("username", user.getName());
        hashMap.put("securityCode", user.getSecurityCode());

        FirebaseFirestore firestore = FirebaseConnect.getFireStore(context);

        firestore.collection(Helper.COLLECTION_USER).document(uid)
                .set(hashMap).addOnSuccessListener(aVoid ->
                Toast.makeText(context, "Registado", Toast.LENGTH_SHORT).show());
    }

}
