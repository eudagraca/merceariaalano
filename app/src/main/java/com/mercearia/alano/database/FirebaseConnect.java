package com.mercearia.alano.database;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseConnect {
    private static FirebaseFirestore mFirestore;


    public static FirebaseFirestore getFireStore(@NonNull Context context) {
        if (mFirestore == null) {
            mFirestore = FirebaseFirestore.getInstance();
            FirebaseApp.initializeApp(context);
        }
        return mFirestore;
    }

}
