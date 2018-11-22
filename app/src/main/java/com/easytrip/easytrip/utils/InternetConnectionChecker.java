package com.easytrip.easytrip.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class InternetConnectionChecker {

    public static final String NO_INTERNET = "There is no Internet connection";

    public boolean checkInternetConnection(Context context){
        ConnectivityManager conMgr =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            showError(context);
            return false;
        }
        else
            return true;
    }

    public void showError(Context context){
        Toast.makeText(context, NO_INTERNET, Toast.LENGTH_SHORT).show();
    }
}
