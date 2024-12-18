package com.example.snooker;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Frames {
    OutputStreamWriter outputStreamWriter;
    public void Obrir(Context context, String [] Jugadors) {
        Log.i("Frames","Anem a crear el fitxer frame.csv");

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = sdf.format(date);
        Log.i("Frames","escrivim al fitxer amb timestamp ->"+formattedDate+"<--");

        try {
            outputStreamWriter = new OutputStreamWriter(context.openFileOutput("frame.csv", Context.MODE_PRIVATE));
            outputStreamWriter.write(formattedDate+"; "+Jugadors[0]+";;"+Jugadors[1]+";\n");
        } catch (IOException e) {
            Log.e("Exception", "Error al crear fitxer: " + e);
        }
    }

    public void Tancar() {
        Log.i("Frames","Anem a tancar el fitxer frame.csv");
        try {
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "Error al tancar fitxer: " + e);
        }
    }
    public void EscriurePunts(String [] Jugadors, int punts1, int punts2){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = sdf.format(date);
        Log.i("Frames","escrivim al fitxer amb timestamp ->"+formattedDate+"<--");
        try {
            outputStreamWriter.write(formattedDate+"; "+Jugadors[0]+";"+punts1+";"+Jugadors[1]+";"+punts2+"\n");
            outputStreamWriter.flush();
        } catch (IOException e) {
            Log.e("Exception", "Error a l'escriure fitxer: " + e);
        }
    }
}
