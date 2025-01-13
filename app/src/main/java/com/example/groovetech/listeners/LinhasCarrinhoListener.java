package com.example.groovetech.listeners;

import com.example.groovetech.Modelo.LinhaCarrinho;

import java.util.ArrayList;


public interface LinhasCarrinhoListener {
    void onRefreshListaLinhasCarrinhos(ArrayList<LinhaCarrinho> listaLinhaCarrinho);

    void onItemUpdate();

    void onItemDelete(ArrayList<LinhaCarrinho> listaLinhaCarrinho, LinhaCarrinho linhaCarrinho);
}

