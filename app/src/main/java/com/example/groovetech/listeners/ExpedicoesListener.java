package com.example.groovetech.listeners;

import com.example.groovetech.Modelo.Expedicao;

import java.util.ArrayList;

public interface ExpedicoesListener {
    void onExpedicoesDataLoaded(ArrayList<Expedicao> listaExpedicoes);
}
