package com.example.groovetech.listeners;

import com.example.groovetech.Modelo.Produto;

import java.util.ArrayList;

public interface HomeProdutosListener {
    void onRefreshHomeProdutos(ArrayList<Produto> listaProdutos);
}
