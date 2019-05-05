package com.mercearia.alano.database;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

public class CurrentUser {

    private static FirebaseAuth mAuth;
    private static com.google.firebase.auth.FirebaseUser mUser;

    public static com.google.firebase.auth.FirebaseUser currentUser(Context context) {

        if (mAuth == null){
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
        }

        return mUser;
    }
}
