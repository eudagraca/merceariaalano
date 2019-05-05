package com.mercearia.alano.utils;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kinda.alert.KAlertDialog;

import am.appwise.components.ni.NoInternetDialog;

public class Helper {

    public static final String COLLECTION_PRODUTOS = "produtos";
    public static final String COLLECTION_DEBITS = "debitos";
    public static final String COR_SECUNDARIA = "#8BC34A";
    public static final String PATTERN_DATE = "dd/MM/yyyy";
    public static final String SP_NAME = "alano";
    public static final String SELECT = "Seleccione o produto";
    public static final String CLOSE = "Fechar";
    public static final String COLLECTION_USER = "user";
    private static NoInternetDialog.Builder builder;
    private static NoInternetDialog noInternetDialog;

    public static void changeFragment(int frameLayoutId, @NonNull Fragment fragmentName, FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayoutId, fragmentName);
        fragmentTransaction.commit();
    }

    public static void alertaNegativa(@NonNull Context context, String message) {
        KAlertDialog dialog = new KAlertDialog(context, KAlertDialog.ERROR_TYPE);
        dialog.setTitleText(message)
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void isConnected(Context ctx) {

        noInternetDialog = new NoInternetDialog.Builder(ctx).build();
    }

    public static void destroyInternetDialog(){

        noInternetDialog.onDestroy();

    }

}
