package com.example.groovetech.listeners;

import com.example.groovetech.Modelo.Produto;
import java.util.ArrayList;

public interface HomeProdutosSearchListener {
    void onSearchResults(ArrayList<Produto> searchedProdutos);
}
