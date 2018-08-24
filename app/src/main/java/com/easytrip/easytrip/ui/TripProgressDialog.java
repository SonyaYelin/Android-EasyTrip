package com.easytrip.easytrip.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import static com.easytrip.easytrip.IConstants.MAX_DIALOG_TIME_IN_SEC;

public class TripProgressDialog {

    private static TripProgressDialog tripProgressDialog;

    private ProgressDialog progressDialog;

    private TripProgressDialog(){

    }

    public static TripProgressDialog getInstance() {
        if ( tripProgressDialog == null )
            tripProgressDialog = new TripProgressDialog();

        return tripProgressDialog;
    }

    public void show(Context context, String msg){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        };

        Handler dialogCanceller = new Handler();
        dialogCanceller.postDelayed(progressRunnable, MAX_DIALOG_TIME_IN_SEC);
    }

    public void dismiss(Context context){
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}
