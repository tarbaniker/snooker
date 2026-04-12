package com.example.snooker;

import android.util.Log;

public class LogIt {
    private static Boolean ferLog = false;

    public static void i(String tag, String msg) { // Executar Log.i si ferlog és cert

        if ( ferLog) {
            Log.i(tag, msg);
        }
    }

    public void setFerLog(boolean tf) {
        ferLog = tf;
        if (ferLog) Log.i("LogIt","s'ha activat el logging");
        else Log.i("LogIt","Logging desactivat");
    }
}
