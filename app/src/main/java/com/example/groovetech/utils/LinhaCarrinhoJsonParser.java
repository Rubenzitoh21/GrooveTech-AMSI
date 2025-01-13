package com.example.groovetech.utils;

import com.example.groovetech.Modelo.LinhaCarrinho;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class LinhaCarrinhoJsonParser {

    public static ArrayList<LinhaCarrinho> parserJsonLinhaCarrinho(JSONArray response) {
        ArrayList<LinhaCarrinho> linhasCarrinhos = new ArrayList<LinhaCarrinho>();
        try {
            for (int i = 0; i < response.length(); i++) {
                if (response.get(i) instanceof JSONObject) {
                    JSONObject linhaCarrinhoJSON = response.getJSONObject(i);
                    int id = linhaCarrinhoJSON.getInt("id");
                    int quantidade = linhaCarrinhoJSON.getInt("quantidade");
                    int carrinho_id = linhaCarrinhoJSON.getInt("carrinhos_id");
                    int produto_id = linhaCarrinhoJSON.getInt("produtos_id");
                    float preco_venda = (float) linhaCarrinhoJSON.getDouble("preco_venda");
                    float subtotal = (float) linhaCarrinhoJSON.getDouble("subtotal");
                    float valor_iva = (float) linhaCarrinhoJSON.getDouble("valor_iva");

                    LinhaCarrinho linhaCarrinho = new LinhaCarrinho(id, quantidade, preco_venda, valor_iva, subtotal, produto_id, carrinho_id);
                    linhasCarrinhos.add(linhaCarrinho);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return linhasCarrinhos;


    }

}
