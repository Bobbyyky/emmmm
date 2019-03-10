package com.example.shinelon.notebook.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Shinelon on 2019/3/9.
 */

public class Util {

    public boolean checkPermission(String[] permission, AppCompatActivity activity){
        boolean flag = false;
        if (Build.VERSION.SDK_INT >= 23){
            int permission_granted = PackageManager.PERMISSION_GRANTED;
            for (int i = 0;i < permission.length;i++){
                int checkPermisssion = ActivityCompat.checkSelfPermission(activity,permission[i]);
                if (permission_granted != checkPermisssion){
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public AlertDialog getDialog(Context context, View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setView(view);
        return builder.create();
    }
}
