package com.example.groovetech.utils;

import com.example.groovetech.Modelo.Fatura;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FaturasJsonParser {

    public static ArrayList<Fatura> parserJsonFaturas(JSONArray response) {
        ArrayList<Fatura> listaFaturas = new ArrayList<Fatura>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject faturaJson = (JSONObject) response.get(i);
                int id = faturaJson.getInt("id");
                String data = faturaJson.getString("data");
                float valorTotal = (float) faturaJson.getDouble("valortotal");
                String status = faturaJson.getString("status");
                int user_id = faturaJson.getInt("user_id");
                int pagamentosID = faturaJson.getInt("pagamentos_id");
                int expedicoesID = faturaJson.getInt("expedicoes_id");

                Fatura fatura = new Fatura(id, data, valorTotal, status, user_id, pagamentosID, expedicoesID);
                listaFaturas.add(fatura);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return listaFaturas;
    }
}
