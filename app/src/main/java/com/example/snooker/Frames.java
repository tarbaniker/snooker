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
        //LogIt.i("Frames","Crearem el fitxer frame.csv");

        Date date = new Date();
        SimpleDateFormat fds = new SimpleDateFormat("yyyyMMddHHmmss");
        String data_fitxer = fds.format(date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = sdf.format(date);
        //LogIt.i("Frames","escrivim al fitxer amb timestamp ->"+formattedDate+"<--");

        try {
            outputStreamWriter = new OutputStreamWriter(context.openFileOutput("frame"+data_fitxer+".csv", Context.MODE_PRIVATE));
            outputStreamWriter.write(formattedDate+"; "+Jugadors[0]+";;"+Jugadors[1]+";\n");
        } catch (IOException e) {
            Log.e("Exception", "Error al crear fitxer: " + e);
        }
    }

    public void Tancar() {
        //LogIt.i("Frames","Tancarem el fitxer frame.csv");
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

        LogIt.i("Frames","escrivim al fitxer amb timestamp ->"+formattedDate+"<--");
        try {
            outputStreamWriter.write(formattedDate+"; "+Jugadors[0]+";"+punts1+";"+Jugadors[1]+";"+punts2+"\n");
            outputStreamWriter.flush();
        } catch (IOException e) {
            Log.e("Exception", "Error a l'escriure fitxer: " + e);
        }

    }

    public void EscriureMaxim(String [] Jugadors, int punts1, int punts2) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = sdf.format(date);

        LogIt.i("Frames","escrivim al fitxer amb timestamp ->"+formattedDate+"<--");
        try {
            outputStreamWriter.write(formattedDate+"; Màxims ;"+Jugadors[0]+";"+punts1+";"+Jugadors[1]+";"+punts2+"\n");
            outputStreamWriter.flush();
        } catch (IOException e) {
            Log.e("Exception", "Error a l'escriure fitxer: " + e);
        }
    }
}
