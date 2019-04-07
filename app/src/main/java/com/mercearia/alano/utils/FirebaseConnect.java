package com.mercearia.alano.utils;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseConnect {
    private static FirebaseFirestore mFirestore;


    public static FirebaseFirestore getFireStore(Context context){
        if (mFirestore == null) {
            mFirestore = FirebaseFirestore.getInstance();
            FirebaseApp.initializeApp(context);
        }
        return mFirestore;
    }

}
