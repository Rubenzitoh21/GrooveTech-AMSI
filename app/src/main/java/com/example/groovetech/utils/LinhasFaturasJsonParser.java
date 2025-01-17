package com.example.groovetech.utils;

import com.example.groovetech.Modelo.LinhasFaturas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class LinhasFaturasJsonParser {

    public static ArrayList<LinhasFaturas> parserJsonLinhasFaturas(JSONArray response) {
        ArrayList<LinhasFaturas> linhasFaturas = new ArrayList<LinhasFaturas>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject linhaJson = (JSONObject) response.get(i);
                int id = linhaJson.getInt("id");
                int quantidade = linhaJson.getInt("quantidade");
                float preco_venda = (float) linhaJson.getDouble("preco_venda");
                int iva = linhaJson.getInt("valor_iva");
                float subtotal = (float) linhaJson.getDouble("subtotal");
                int faturas_id = linhaJson.getInt("faturas_id");
                int produtos_id = linhaJson.getInt("produtos_id");

                LinhasFaturas linha = new LinhasFaturas(id, quantidade, preco_venda, iva, subtotal, faturas_id, produtos_id);
                linhasFaturas.add(linha);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return linhasFaturas;


    }


}
