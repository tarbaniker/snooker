package com.example.snooker;

//import android.provider.CalendarContract;
import android.util.Log;

import java.util.ArrayList;

public class Lexic {
    public ArrayList<Integer> tokens = new ArrayList<>();
    public Boolean complet = false;
    public Boolean error = false;
    public Boolean final_detectat = false;

    public void reset() {   // Per inicialitzar la llista de tokens
            tokens.clear();
            Log.i("Lexic","tokens després del clear() ->"+tokens+"<-");
    }
    private void afegir(String mot) {     // Per afegir a la llista de tokens les paraules detectades
        switch (mot) {
            case "bola":
            case "vola":
            case "mola":
            case "hola":
                tokens.add(Constants.BOLA);
                break;
            case "vermella":
                tokens.add(Constants.VERMELLA);
                break;
            case "groga":
                tokens.add(Constants.GROGA);
                break;
            case "verda":
            case "merda":
                tokens.add(Constants.VERDA);
                break;
            case "marró":
                tokens.add(Constants.MARRO);
                break;
            case "blava":
                tokens.add(Constants.BLAVA);
                break;
            case "rosa":
                tokens.add(Constants.ROSA);
                break;
            case "negra":
            case "negre":
                tokens.add(Constants.NEGRA);
                break;
            case "canvi":
                tokens.add(Constants.CANVI);
                break;
            case "falta":
                tokens.add(Constants.FALTA);
                break;
            case "de":
                tokens.add(Constants.DE);
                break;
            case "quatre":
                tokens.add(Constants.QUATRE);
                break;
            case "cinc":
            case "d'ací":
                tokens.add(Constants.CINC);
                break;
            case "sis":
                tokens.add(Constants.SIS);
                break;
            case "set":
                tokens.add(Constants.SET);
                break;
            case "jugador":
                tokens.add(Constants.JUGADOR);
                break;
            case "enric":
                tokens.add(Constants.ENRIC);
                break;
            case "francesc":
                tokens.add(Constants.FRANCESC);
                break;
            case "josep":
                tokens.add(Constants.JOSEP);
                break;
            case "final":
                tokens.add(Constants.FINAL);
                break;
            default:
                Log.i("Lexic", "Paraula no reconeguda ->" + mot + "<-");
                error =true;
                complet = true;
                break;
        }

    }

    public void tokenitzar(String resultats) {
        Log.i("Lexic","tokenitzar. resultats rebut ->"+resultats+"<-");
        reset();
        complet = false;
        error = false;

        // Per cada paraula de resultats
        String[] words = resultats.split(" ", 10);

        for (String w : words) {
            Log.i("Lexic", "tokenitzar. dins del for. mot extret ->" + w + "<-");
            if ( !w.trim().isEmpty()) {
                Log.i("Lexic","tokenitzar. anem a afegir ->"+w.trim()+"<-");
                afegir(w.trim());
            }
        }
        if (!error) {
            Log.i("Lexic","tokenitzar. no hi ha error anem a analitzar");
            analitzar();
        }
        else Log.i("Lexic","tokenitzar. hi ha error");
    }
    private void analitzar() {
    if ( !tokens.isEmpty() ) {
        if ( ( tokens.get(0) == Constants.BOLA ) ) fer_bola();
        else if ( (tokens.get(0) == Constants.FALTA) ) fer_falta();
        else if ( (tokens.get(0) == Constants.CANVI) ) fer_canvi();
        else if ( (tokens.get(0) == Constants.FINAL )) fer_final();
        else if ( (tokens.get(0) == Constants.JUGADOR) ) fer_jugador();
        else {
            reset();
            error = true;
            complet = true;
            }
        }
    else Log.i("Lexic","analitzar. tokens està buit");
    }
    private void fer_bola() {
        if ( tokens.size() > 1) {
            if ((tokens.get(1) == Constants.VERMELLA) ||
                    (tokens.get(1) == Constants.GROGA) ||
                    (tokens.get(1) == Constants.VERDA) ||
                    (tokens.get(1) == Constants.MARRO) ||
                    (tokens.get(1) == Constants.BLAVA) ||
                    (tokens.get(1) == Constants.ROSA) ||
                    (tokens.get(1) == Constants.NEGRA)
            ) {
                complet = true;
            } else {
                error = true;
                complet = true;
            }
        }
    }

    private void fer_jugador() {
        if ( tokens.size() > 1) {
            if ( (tokens.get(1) == Constants.ENRIC) ||
                    (tokens.get(1) == Constants.FRANCESC) ||
                    (tokens.get(1) == Constants.JOSEP)
            ) {
                complet = true;
            } else {
                error = true;
                complet = true;
            }
        }
    }

    private void fer_falta() {
        if ( tokens.size() > 1) {
            if (tokens.get(1) == Constants.DE) {
                fer_valors();
            }
            else {
                error = true;
                complet = true;
            }
        }
    }

    private void fer_canvi() {
        complet = true;
    }

    private void fer_final() {
        complet = true;
        final_detectat = true;
    }
    private void fer_valors() {
        if (tokens.size() > 2) {
            if ( (tokens.get(2) == Constants.QUATRE) ||
                 (tokens.get(2) == Constants.CINC) ||
                 (tokens.get(2) == Constants.SIS) ||
                 (tokens.get(2) == Constants.SET)
            ) {
                complet = true;
            } else {
                error = true;
                complet = true;
            }
        }
    }

    public String print_tokens() {
        Log.i("Lexic","print_tokens - Inici");
        String resultat = "";
        for ( int i=0; i<tokens.size(); i++) {
            switch ( tokens.get(i)) {
                case Constants.BOLA:
                    resultat = resultat.concat(" BOLA");
                    break;
                case Constants.FALTA:
                    resultat = resultat.concat(" FALTA");
                    break;
                case Constants.CANVI:
                    resultat = resultat.concat(" CANVI");
                    break;
                case Constants.DE:
                    resultat = resultat.concat(" DE");
                    break;
                case Constants.VERMELLA:
                    resultat = resultat.concat(" VERMELLA");
                    break;
                case Constants.GROGA:
                    resultat = resultat.concat(" GROGA");
                    break;
                case Constants.VERDA:
                    resultat = resultat.concat(" VERDA");
                    break;
                case Constants.MARRO:
                    resultat = resultat.concat(" MARRÓ");
                    break;
                case Constants.BLAVA:
                    resultat = resultat.concat(" BLAVA");
                    break;
                case Constants.ROSA:
                    resultat = resultat.concat(" ROSA");
                    break;
                case Constants.NEGRA:
                    resultat = resultat.concat(" NEGRA");
                    break;
                case Constants.QUATRE:
                    resultat = resultat.concat(" QUATRE");
                    break;
                case Constants.CINC:
                    resultat = resultat.concat(" CINC");
                    break;
                case Constants.SIS:
                    resultat = resultat.concat(" SIS");
                    break;
                case Constants.SET:
                    resultat = resultat.concat(" SET");
                    break;
                case Constants.JUGADOR:
                    resultat = resultat.concat(" JUGADOR");
                    break;
                case Constants.ENRIC:
                    resultat = resultat.concat(" ENRIC");
                    break;
                case Constants.FRANCESC:
                    resultat = resultat.concat(" FRANCESC");
                    break;
                case Constants.JOSEP:
                    resultat = resultat.concat(" JOSEP");
                    break;
                case Constants.FINAL:
                    resultat = resultat.concat(" FINAL");
                    break;
                default:
                    resultat = resultat.concat(" ***");
                    break;
            }

        }
        Log.i("Lexic","print_tokens - Final retornarà ->"+resultat+"<-");
        return (resultat.trim());
    }

}
