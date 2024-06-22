package com.example.spidermangame.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

public class Signal {
    @SuppressLint("StaticFieldLeak")
    private static Signal mySignal = null;
    private final Context context;

    private Signal(Context context){this.context = context;}

    public static void init(Context context){
        if (mySignal == null) {
            mySignal = new Signal(context);
        }
    }
    public static Signal getInstance(){return mySignal;}

    public void toast(String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }

}