package com.example.snooker;

import static java.lang.Boolean.TRUE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
// import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
// import androidx.appcompat.app.AppCompatActivity;

// import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
//import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    // Initialize SpeechRecognizer
    // des del PC del Josep
    private SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
    private TextToSpeech textToSpeech;
    private Button btnSpeak, btnListen;
    private EditText etInput;

    private TextView tJugador;
    private TextView tJugador1;
    private TextView tJugador2;
    private Jugadors els_jugadors;
    private Reproductor el_reproductor;
    //private Boolean resultats_parcials;
    private Context context;
    private Frames el_frame;
    // paraules reconegudes
    //private Paraules paraules;
    private Lexic lexic;
    private int inici_text = 0;
    private boolean resultats_trobats = false;
    private Bundle results_ant = new Bundle();
    private boolean continuar_escoltant = true;

    private Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    // Variable/s per a guardar paraules rebudes per onResults i onPartialResults
    private String ordre = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSpeak = findViewById(R.id.btnSpeak);
        btnListen = findViewById(R.id.btnListen);
        etInput = findViewById(R.id.etInput);
        tJugador = findViewById(R.id.jugador);
        tJugador1 = findViewById(R.id.jugador1);
        tJugador2 = findViewById(R.id.jugador2);

        // Initialize SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new SpeechRecognitionListener());

        // Initialize TextToSpeech
        textToSpeech = new TextToSpeech(this, this::onInit);

        // Set button click listeners
        //btnSpeak.setOnClickListener(this.startListening());

        // btnListen.setOnClickListener(this::speak);
        //btnListen.setOnClickListener(this.finalitzar());

        // Creem els jugadors
        els_jugadors = new Jugadors();
        Log.i("Inici jugadors", "Jugador actual ->" + els_jugadors.nom_jugador() + "<- Punts jugador ->" + els_jugadors.getPuntsJugador() + "<-");

        el_reproductor = new Reproductor();
        // Sonen els aplaudiments
        context = this;
        el_reproductor.reproduir(context, R.raw.applausecheer236786, 6500);

        lexic = new Lexic();

    }

    private void onInit(int status) {
        //Log.i("onInit", "inici");
        //resultats_parcials = false;
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.forLanguageTag("ca-ES"));
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(MainActivity.this, "Language not supported", Toast.LENGTH_SHORT).show();
                Log.e("onInit", "Language not supported");
            }
        } else {
            Toast.makeText(MainActivity.this, "Initialization failed", Toast.LENGTH_SHORT).show();
            Log.e("onInit", "Initialization failed");
        }

        els_jugadors.setJugadorInicial();
        tJugador.setText("Jugador a taula: " + els_jugadors.nom_jugador());

        tJugador1.setText(els_jugadors.nom_jugador(1) + " " + els_jugadors.getPuntsJugador(1));
        tJugador2.setText(els_jugadors.nom_jugador(2) + " " + els_jugadors.getPuntsJugador(2));

        // creació fitxer .csv
        el_frame = new Frames();
        el_frame.Obrir(context, els_jugadors.nomJugador);
        // el_frame.Tancar();

        String text = "Comença el freim. Trenca el jugador " + els_jugadors.nom_jugador();
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

        // Donem temps a sentir el missatge inicial
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            Log.e("onInit", "error sleep", e);
        }

        // Afegeixo que comenci el startListening
        startListening();

    }

    @SuppressLint("UnsafeOptInUsageError")
    private void startListening() {
        //Log.i("startListening", "Inici");

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.forLanguageTag("ca-ES"));
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, TRUE);

        //intent.putExtra(RecognizerIntent.EXTRA_SEGMENTED_SESSION, TRUE);
        //intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 1500);


        //intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Parleu ara");
        //Log.i("startListening", "Parleu ara");

        speechRecognizer.startListening(intent);
        // startActivityForResult(intent, 1); //Aquesta instrucció fa aparèixer l'aplicació de reconeixement de veu de GOOGLE
        //Log.i("startListening", "Després de speechRecognizer");
    return ;
    }


    private void finalitzar() {
        //Log.i("finalitzar", "Hem detectat la paraula final");

        // - primer donem les gràcies per utilitzar el programa
        continuar_escoltant = false;

        speechRecognizer.stopListening();
        // si no va, provarem amb el mètode cancel
        speechRecognizer.cancel();

        String text = "Final. Gràcies per utilitzar aquesta aplicació.";
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

        // tanquem el fitxer del frame
        //el_frame.Tancar();

        // Donem temps a sentir el missatge final
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            Log.e("Final", "error sleep", e);
        }

        // Tanquem l'aplicació
        finishAndRemoveTask();
    return ;
    }

    @Override
    protected void onDestroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private class SpeechRecognitionListener implements RecognitionListener {
        private Date date = new Date();
        private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String data_sdf;

        @Override
        public void onResults(Bundle results) {
            date = new Date();
            data_sdf = sdf.format(date);
            Log.i("SpeechRecognitionListen", "onResults. Entrada "+data_sdf);
            resultats_trobats = true;
            if (!results_ant.isEmpty()) {
                //Log.i("SpeechRecognitionListen", "onResults. results_ant no està buit");
                //Log.i("SpeechRecognitionListen", "onResults. results_ant.size() ->" + results_ant.size() + "<- "
                //        + " results.size() ->" + results.size() + "<-");
                if (results_ant.size() > results.size()) return;
                ArrayList<String> matches_ant = results_ant.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                //Log.i("SpeechRecognitionListen", "onResults longituds: matches_ant ->" + matches_ant.get(0).length()
                //        + "<- matches ->" + matches.get(0).length() + "<-");
                if (matches_ant.get(0).length() > matches.get(0).length()) {
                    return;
                }
            }
            results_ant = results;

            //Log.i("SpeechRecognitionListen", "onResults, results -> " + results + "<-");
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            //Log.i("SpeechRecognitionListen", "onResults, abans de guardar, matches ->" + matches + "<-");
            if (matches.get(0).isEmpty()) inici_text = 0; // Potser caldrà preguntar si és nul
            //Log.i("SpeechRecognitionListen", "onResults, abans de guardar" + " inici_text ->" + inici_text + "<-");
            //Log.i("SpeechRecognitionListen", "onResults, abans de guardar, matches ->"
            //        + matches.get(0).substring(inici_text) + "<-"
            //        + " inici_text ->" + inici_text + "<-");

            //Log.i("SpeechRecognitionListen", "onResults, ordre ->" + ordre + "<-");
            if (!matches.get(0).substring(inici_text).isEmpty())
                afegir_ordre(matches.get(0).substring(inici_text));

            //Posem l'inici del text
            inici_text = matches.get(0).length();
            lexic.tokenitzar(ordre);
            if (lexic.complet && !lexic.error) fer_ordre();
            if (lexic.complet || lexic.error) {
                ordre = "";
            }
            if (lexic.error) {
                fer_repetir();
            }

            if (continuar_escoltant) {
                startListeningAgain();
            }
            if (lexic.tokens.isEmpty()) {
                // Esperem 1000ms abans de sortir del onResults
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    Log.e("onInit", "error sleep", e);
                }
            }
            date = new Date();
            data_sdf = sdf.format(date);
            Log.i("SpeechRecognitionListen", "onResults. Sortida "+data_sdf);
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            date = new Date();
            data_sdf = sdf.format(date);
            Log.i("SpeechRecognitionListen", "onPartialResults. Entrada "+data_sdf);
            if (!resultats_trobats) {
                if (!results_ant.isEmpty()) {
                    //Log.i("SpeechRecognitionListen", "onPartialResults. results_ant no està buit");
                    //Log.i("SpeechRecognitionListen", "onPartialResults. results_ant.size() ->" + results_ant.size() + "<- "
                    //        + " partialResulta.size() ->" + partialResults.size() + "<-");
                    if (results_ant.size() > partialResults.size()) return;
                    ArrayList<String> matches_ant = results_ant.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    //Log.i("SpeechRecognitionListen", "onPartialResults longituds: matches_ant ->" + matches_ant.get(0).length()
                    //        + "<- matches ->" + matches.get(0).length() + "<-");
                    if (matches_ant.get(0).length() > matches.get(0).length()) {
                        return;
                    }
                }
            } else {
                inici_text = 0;
                resultats_trobats = false;
            }

            results_ant = partialResults;

            Log.i("SpeechRecognitionListen", "onPartialResults -->" + partialResults + "<--");
            ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            //Log.i("SpeechRecognitionListen", "onPartialResults, abans de guardar matches ->" + matches + "<-");
            if (matches.get(0).isEmpty()) inici_text = 0; // Potser caldrà preguntar si és nul
            //Log.i("SpeechRecognitionListen", "onPartialResults, abans de guardar" + " inici_text ->" + inici_text + "<-");
            //Log.i("SpeechRecognitionListen", "onPartialResults, abans de guardar, matches ->"
            //        + matches.get(0).substring(inici_text) + "<-"
            //        + " inici_text ->" + inici_text + "<-");
            // Guardem les paraules rebudes
            afegir_ordre(matches.get(0).substring(inici_text));
            //Log.i("SpeechRecognitionListen", "onPartialResults, ordre ->" + ordre + "<-");

            //posem l'inici de text
            inici_text = matches.get(0).length();
            lexic.tokenitzar(ordre);
            if (lexic.complet && !lexic.error) fer_ordre();
            if (lexic.complet || lexic.error) {
                ordre = "";
            }
            if (lexic.error) {
                fer_repetir();
            }
            if (lexic.tokens.isEmpty()) {
                // Esperem 1000ms abans de sortir del onPartialResults
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    Log.e("onInit", "error sleep", e);
                }
            }
            date = new Date();
            data_sdf = sdf.format(date);
            Log.i("SpeechRecognitionListen", "onPartialResults. Sortida "+data_sdf);
        }

        // Implement other required methods with empty bodies
        @Override
        public void onReadyForSpeech(Bundle params) {
            Log.i("SpeechRecognitionListen", "onReadyForSpeech " + params);
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.i("SpeechRecognitionListen", "onBeginningOfSpeech");
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            /* Log.i("SpeechRecognitionListen","onRmsChanged float value "+ rmsdB); */
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            Log.i("SpeechRecognitionListen", "onBufferReceived -->" + buffer + "<--");
        }

        @Override
        public void onEndOfSpeech() {
            Log.i("SpeechRecognitionListen", "onEndOfSpeech");
        }

        @Override
        public void onError(int error) {
            //Log.i("SpeechRecognitionListen", "onError");
            Log.e("SpeechRecognitionListen", "onError "+String.valueOf(error));

            String mError;
            switch (error) {
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    mError = " network timeout";
                    // startListening();
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    mError = " network";
                    //toast("Please check data bundle or network settings");
                    break;
                case SpeechRecognizer.ERROR_AUDIO:
                    mError = " audio";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    mError = " server";
                    //startListening();
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    mError = " client";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    mError = " speech time out";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    mError = " no match";
                    //startListening();
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    mError = " recogniser busy";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    mError = " insufficient permissions";
                    break;
                default:
                    //throw new IllegalStateException("Unexpected value: " + error);
                    mError = "Unexpected value";
                    break;
            }
            Log.i("SpeechRecognitionListen", "Error: " + error + " - " + mError);

            // Donem temps per a evitar el "busy"
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                Log.e("onError", "error sleep", e);
            }

            if (continuar_escoltant && error != SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS &&
                                       error != SpeechRecognizer.ERROR_NO_MATCH) {
                startListeningAgain();
            }

        }


        @Override
        public void onEvent(int eventType, Bundle params) {
            Log.i("SpeechRecognitionListen", "onEvent -->" + params + "<--");
        }

    }
        private void parsingOrdre() {
            //Log.i("parsingOrdre", "analitzem paraules rebudes a lexic.tokens .");
            //Analitzem l'ordre rebuda a lexic.tokens

            //resultats_parcials = false;
            Boolean complet = false;
            Boolean falta = false;
            String text = "";
            String jugador_contrari = els_jugadors.nom_jugador_contrari();

            if ( lexic.tokens.get(0) == Constants.BOLA ) {
                complet = true;

                switch (lexic.tokens.get(1)) {
                    case Constants.VERMELLA:
                        //fer vermella
                        els_jugadors.sumar_punts(1);
                        text = "vermella, " + els_jugadors.nom_jugador() + els_jugadors.getEntrada() +
                                (els_jugadors.getEntrada() > 1 ? " punts." : "punt");
                        break;
                    case Constants.GROGA:
                        //fer groga
                        els_jugadors.sumar_punts(2);
                        text = "groga, " + els_jugadors.nom_jugador() + els_jugadors.getEntrada() + " punts.";
                        break;
                    case Constants.VERDA:
                        //fer verda
                        els_jugadors.sumar_punts(3);
                        text = "verda, " + els_jugadors.nom_jugador() + els_jugadors.getEntrada() + " punts.";
                        break;
                    case Constants.MARRO:
                        //fer marró
                        els_jugadors.sumar_punts(4);
                        text = "marró, " + els_jugadors.nom_jugador() + els_jugadors.getEntrada() + " punts.";
                        break;
                    case  Constants.BLAVA:
                        //fer blava
                        els_jugadors.sumar_punts(5);
                        text = "blava, " + els_jugadors.nom_jugador() + els_jugadors.getEntrada() + " punts.";
                        break;
                    case Constants.ROSA:
                        //fer rosa
                        els_jugadors.sumar_punts(6);
                        text = "rosa, " + els_jugadors.nom_jugador() + els_jugadors.getEntrada() + " punts.";
                        break;
                    case Constants.NEGRA:
                        //fer negra
                        els_jugadors.sumar_punts(7);
                        text = "negra, " + els_jugadors.nom_jugador() + els_jugadors.getEntrada() + " punts.";
                        break;
                    default:
                        Log.e("parsingOrdre","Bola. Error en els tokens. No hauria de passar mai!");
                        text = "Bola. Error en els tokens. No hauria de passar mai!";
                        break;
                }
            };
            if ( lexic.tokens.get(0) == Constants.FALTA ) {
                complet = true;
                falta = true;

                switch (lexic.tokens.get(2)) {
                    case Constants.QUATRE:
                        //fer falta de 4
                        text = "falta, " + jugador_contrari + " 4 punts. " + els_jugadors.nom_jugador() + els_jugadors.getEntrada()
                                + (els_jugadors.getEntrada() > 1 ? " punts." : "punt.");
                        els_jugadors.sumar_falta(4);
                        break;
                    case Constants.CINC:
                        //fer falta de 5
                        text = "falta, " + jugador_contrari + " 5 punts. " + els_jugadors.nom_jugador() + els_jugadors.getEntrada()
                                + (els_jugadors.getEntrada() > 1 ? " punts." : "punt.");
                        els_jugadors.sumar_falta(5);
                        break;
                    case Constants.SIS:
                        //fer falta de 6
                        text = "falta, " + jugador_contrari + " 6 punts. " + els_jugadors.nom_jugador() + els_jugadors.getEntrada()
                                + (els_jugadors.getEntrada() > 1 ? " punts." : "punt.");
                        els_jugadors.sumar_falta(6);
                        break;
                    case Constants.SET:
                        //fer falta de 7
                        text = "falta, " + jugador_contrari + " 7 punts. " + els_jugadors.nom_jugador() + els_jugadors.getEntrada()
                                + (els_jugadors.getEntrada() > 1 ? " punts." : "punt.");
                        els_jugadors.sumar_falta(7);break;
                    default:
                        Log.e("parsingOrdre", "Falta. Error en els tokens. No hauria de passar mai!");
                        text = "Falta. Error en els tokens. No hauria de passar mai!";
                        break;
                };
            };
            if ( lexic.tokens.get(0) == Constants.CANVI ) {
                //fer canvi
                complet = true;
                text = "Jugador " + els_jugadors.nom_jugador() + els_jugadors.getEntrada()
                        + (els_jugadors.getEntrada() > 1 ? " punts." : "punt.");
                els_jugadors.canviar_jugador();
                text = text + "Entra jugador " + els_jugadors.nom_jugador();
                tJugador.setText("Jugador a taula: " + els_jugadors.nom_jugador());
            }

            // Revisar si falta fer res més


/*
            } else if (Objects.equals(match, "brec") || Objects.equals(match, "entrada")) {
                complet = true;
                text = "Jugador a taula " + els_jugadors.nom_jugador() + els_jugadors.getEntrada() +
                        (els_jugadors.getEntrada() > 1 ? " punts." : "punt.")
                        + ". Puntuació "
                        + els_jugadors.nom_jugador(1) + " " + els_jugadors.getPuntsJugador(1) + ". "
                        + els_jugadors.nom_jugador(2) + " " + els_jugadors.getPuntsJugador(2);
            } else {
                text = match + ", pendent de programar";
            }
            if (complet) {
*/
                //Log.i("parsingMatches", "Jugador actual ->" + els_jugadors.nom_jugador() + "<- Entrada ->"
                //        + els_jugadors.getEntrada() + "<- Punts ->" + els_jugadors.getPuntsJugador() + "<-");

                // actualitzem comptadors
                tJugador1.setText(els_jugadors.nom_jugador(1) + " " + els_jugadors.getPuntsJugador(1));
                tJugador2.setText(els_jugadors.nom_jugador(2) + " " + els_jugadors.getPuntsJugador(2));

                speechRecognizer.stopListening();
                speechRecognizer.cancel();
                //etInput.setText(match);
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                // al sumar els punts de falta, ja es canvia de jugador
                if (falta) {
                    tJugador.setText("Jugador a taula: " + els_jugadors.nom_jugador());
                }
                // Apuntem punts al fitxer del frame
                el_frame.EscriurePunts(els_jugadors.nomJugador, els_jugadors.getPuntsJugador(1), els_jugadors.getPuntsJugador(2));

                // Donem temps a sentir el missatge de la puntuació
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    Log.e("Final", "error sleep", e);
                }

                // Tornem a engegar el listener
                startListeningAgain();

        }

    private void fer_ordre() {
        Log.i("fer_ordre", "ordre rebuda ->" + ordre + "<-");
        lexic.tokenitzar(ordre);
        if (!lexic.error) {
            //Log.i("fer_ordre", "lexic correcte. Nombre de tokens ->" + lexic.tokens.size() + "<-");
            if (lexic.tokens.get(0) == Constants.FINAL) finalitzar();
        }

        //aquí cal fer l'ordre rebuda
        parsingOrdre();
        //esborrem l'ordre ja feta
        ordre = "";
    }


    private void afegir_ordre(String paraules) {
        //aquí afegirem a ordre les paraules que encara no hi siguin a ordre
        String[] words = paraules.split(" ", 10);

        for (String w : words) {
            //Log.i("SpeechRecognitionListen", "afegir_ordre. mot rebut ->" + w + "<-");
            if (!w.trim().isEmpty()) {
                //Log.i("SpeechRecognitionListen", "afegir_ordre. anem a afegir ->" + w.trim() + "<-");
                // si la paraula no existeix dins d'ordre l'afegim
                if (!ordre.contains(w.trim())) {
                    ordre = ordre + " " + w.trim();
                    //Log.i("SpeechRecognitionListen", "afegir_ordre. ordre no conté ->" + w.trim() + "<-");
                }
            }
        }

    }

    private void startListeningAgain() {
        if (continuar_escoltant) {
            // Donem temps per a evitar el "busy"
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                Log.e("startListeningAgain", "error sleep", e);
            }

            speechRecognizer.startListening(intent);
        }
    }

    private void fer_repetir() {

        //speechRecognizer.stopListening();

        String text = "Repeteix";
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

        // Donem temps a sentir el missatge inicial
        try {
            Thread.sleep(1500);
        } catch (Exception e) {
            Log.e("fer_repetir", "error sleep", e);
        }
        //startListeningAgain();

    }
}
