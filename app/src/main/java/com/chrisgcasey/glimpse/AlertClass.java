package com.chrisgcasey.glimpse;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by Chris on 2/16/2015.
 */
public class AlertClass {

    public static final AlertDialog.Builder alertDialog(Context context, String message){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage(message).setTitle(R.string.error_title)
                .setPositiveButton(android.R.string.ok, null).create();
        alertDialog.show();
        return alertDialog;

    }
}
