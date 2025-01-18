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

                if (linhaJson.has("totals")) {
                    continue;
                }

                int id = linhaJson.has("id") ? linhaJson.getInt("id") : -1; // Use default value if not found
                int quantidade = linhaJson.has("quantidade") ? Integer.parseInt(linhaJson.getString("quantidade")) : 0;
                float preco_venda = linhaJson.has("preco_venda") ? (float) linhaJson.getDouble("preco_venda") : 0.0f;
                int iva = linhaJson.has("valor_iva") ? linhaJson.getInt("valor_iva") : 0;
                float subtotal = linhaJson.has("subtotal") ? (float) linhaJson.getDouble("subtotal") : 0.0f;
                int faturas_id = linhaJson.has("faturas_id") ? linhaJson.getInt("faturas_id") : 0;
                int produtos_id = linhaJson.has("produtos_id") ? linhaJson.getInt("produtos_id") : 0;

                LinhasFaturas linha = new LinhasFaturas(id, quantidade, preco_venda, iva, subtotal, faturas_id, produtos_id);
                linhasFaturas.add(linha);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return linhasFaturas;
    }
}

