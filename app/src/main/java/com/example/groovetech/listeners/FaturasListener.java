package com.example.groovetech.listeners;

import com.example.groovetech.Modelo.Fatura;

import java.util.ArrayList;


public interface FaturasListener {
    void onFaturasDataLoaded(ArrayList<Fatura> listaFaturas);
}
