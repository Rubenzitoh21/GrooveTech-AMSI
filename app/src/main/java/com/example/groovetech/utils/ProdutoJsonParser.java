package com.example.groovetech.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.groovetech.Modelo.Produto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ProdutoJsonParser {

    public static ArrayList<Produto> parserJsonProdutos(JSONArray response) {
        ArrayList<Produto> produtos = new ArrayList<Produto>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject produtoJSON = (JSONObject) response.get(i);
                int id = produtoJSON.getInt("id");
                String nome = produtoJSON.getString("nome");
                String descricao = produtoJSON.getString("descricao");
                String obs = produtoJSON.getString("obs");
                float preco = (float) produtoJSON.getDouble("preco");
                int iva = produtoJSON.getInt("iva");
                String categoria = produtoJSON.getString("categoria");
                String imagem = produtoJSON.getString("imagem");

                Produto produto = new Produto(id, nome, descricao, preco, obs, categoria, iva, imagem);
                produtos.add(produto);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return produtos;


    }

    public static Produto parserJsonProduto(String response) {
        Produto produto = null;
        try {
            JSONObject produtoJSON = new JSONObject(response);
            int id = produtoJSON.getInt("id");
            String nome = produtoJSON.getString("nome");
            String descricao = produtoJSON.getString("descricao");
            String obs = produtoJSON.getString("obs");
            float preco = (float) produtoJSON.getDouble("preco");
            int iva = produtoJSON.getInt("iva");
            String categoria = produtoJSON.getString("categoria");
            String imagem = produtoJSON.getString("imagem");

            produto = new Produto(id, nome, descricao, preco, obs, categoria, iva, imagem);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Log.d("ProdutosJsonParser", "parserJsonProduto: " + produto);
        return produto;
    }
}
