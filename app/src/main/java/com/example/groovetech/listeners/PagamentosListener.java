package com.example.groovetech.listeners;

import com.example.groovetech.Modelo.Pagamento;

import java.util.ArrayList;

public interface PagamentosListener {
    void onPagamentosDataLoaded(ArrayList<Pagamento> listaPagamentos);
}
