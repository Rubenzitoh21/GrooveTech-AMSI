package com.example.groovetech.utils;

import com.example.groovetech.Modelo.Expedicao;
import com.example.groovetech.Modelo.Pagamento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExpedicoesJsonParser {
    public static ArrayList<Expedicao> parserJsonExpedicoes(JSONArray response) {
        ArrayList<Expedicao> expedicoes = new ArrayList<Expedicao>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject expedicaoJSON = (JSONObject) response.get(i);
                int id = expedicaoJSON.getInt("id");
                String metodoExpedicao = expedicaoJSON.getString("metodoexp");

                Expedicao expedicao = new Expedicao(id, metodoExpedicao);
                expedicoes.add(expedicao);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return expedicoes;


    }
}
