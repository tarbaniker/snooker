package com.example.snooker;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class Frames {
    OutputStreamWriter outputStreamWriter;
    public void Obrir(Context context, String [] Jugadors) {
        Log.i("Frames","Anem a crear el fitxer frame.csv");
        try {
            outputStreamWriter = new OutputStreamWriter(context.openFileOutput("frame.csv", Context.MODE_PRIVATE));
            outputStreamWriter.write(Jugadors[0]+";;"+Jugadors[1]+";\n");
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
        try {
            outputStreamWriter.write(Jugadors[0]+";"+punts1+";"+Jugadors[1]+";"+punts2+"\n");
            outputStreamWriter.flush();
        } catch (IOException e) {
            Log.e("Exception", "Error a l'escriure fitxer: " + e);
        }
    }
}
