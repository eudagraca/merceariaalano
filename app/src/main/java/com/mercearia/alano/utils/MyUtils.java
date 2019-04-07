package com.mercearia.alano.utils;
import android.content.Context;

import com.kinda.alert.KAlertDialog;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MyUtils {

    public static final String COLLECTION_PRODUTOS = "produtos";
    public static final String OK = "OK";
    public static final String COR_SECUNDARIA = "#FFFFA96D";
    public static final String PATTERN_DATE = "\"dd/MM/yyyy HH:mm:ss\"";
    public static final String COLLETION_CATEGORIA = "categorias";

    public static void changeFragment(int frameLayoutId, Fragment fragmentName, FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayoutId, fragmentName);
        fragmentTransaction.commit();
    }

    public static void alertaPosetiva(Context context, String message){
        KAlertDialog dialog = new KAlertDialog(context, KAlertDialog.SUCCESS_TYPE);
        dialog.setTitleText(message)
                .show();
    }
    public static void alertaNegativa(Context context, String message){
        KAlertDialog dialog = new KAlertDialog(context, KAlertDialog.ERROR_TYPE);
        dialog.setTitleText(message)
                .show();
    }
}
