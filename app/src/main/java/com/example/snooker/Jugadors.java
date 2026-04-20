package com.example.snooker;

import android.util.Log;

import java.util.Random;

public class Jugadors {
    String[] nomJugadors = {"Enric Josep", "Francesc"};
    String[] nomJugador = { null, null};

    private static final int[] punts = {0, 0};
    private static final int[] maxim = {0, 0};
    private static int entrada = 0;
    private static int jugadorActual = 0;

    public void sumar_punts(int quants) {
        entrada = entrada + quants;
        punts[jugadorActual] = punts[jugadorActual] + quants;
        if (entrada > maxim[jugadorActual]) {
            maxim[jugadorActual] = entrada;
        }
    }

    public void sumar_falta(int quants) {
        canviar_jugador();
        punts[jugadorActual] = punts[jugadorActual] + quants;
    }

    public void canviar_jugador() {
        if (jugadorActual == 0) {
            jugadorActual = 1;
        } else {
            jugadorActual = 0;
        }
        entrada = 0;
    }

    public String nom_jugador() {
        return nomJugador[jugadorActual];
    }

    public String nom_jugador(int jugador) { // Espera que el jugador sigui 1 o 2
        if (jugador == 1 || jugador == 2) {
           return nomJugador[jugador - 1];
        }
           else {
              return "Desconegut";
           }
    }

    public String nom_jugador_contrari() { // Retorna el nom del jugador que no és l'actual
        if (jugadorActual == 0) {
            return nomJugador[1];
        }
        else {
            return nomJugador[0];
        }
    }
    public int getEntrada() {
        return entrada;
    }

    public int getPuntsJugador() {
        return punts[jugadorActual];
    }

    public int getMaximJugador(int jugador) { // Espera que el jugador sigui 1 o 2
        if (jugador == 1 || jugador == 2) {
        return maxim[jugador -1];
        } else {
            return -1;
        }
    }

    public int getPuntsJugador(int jugador) { // Espera que el jugador sigui 1 o 2
        if (jugador == 1 || jugador == 2) {
            return punts[jugador - 1];
        } else {
            return -1;
        }
    }

    public void setJugadorInicial(int inicial) {
        /*
        int inicial;
        Random rand = new Random();
        // Escollim parella de jugadors
        inicial = rand.nextInt(3);
        */

        if (inicial > 1) {
            Log.e("Jugadors", "valor inicial" + inicial);
        }

        switch (inicial) {
            case 0:
                nomJugador[0] = nomJugadors[0];
                nomJugador[1] = nomJugadors[1];
                break;
            case 1:
                nomJugador[0] = nomJugadors[1];
                nomJugador[1] = nomJugadors[0];
                break;
            default:
                Log.e("Jugadors", "valor inicial" + inicial);
        }

        // el jugador a taula sempre serà el jugador 0
        jugadorActual = 0;

        entrada=0;
    }
}