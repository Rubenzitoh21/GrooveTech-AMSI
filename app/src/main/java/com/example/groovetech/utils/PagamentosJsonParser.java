package com.example.groovetech.utils;

import com.example.groovetech.Modelo.Pagamento;
import com.example.groovetech.Modelo.Produto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PagamentosJsonParser {
    public static ArrayList<Pagamento> parserJsonPagamentos(JSONArray response) {
        ArrayList<Pagamento> pagamentos = new ArrayList<Pagamento>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject pagamentoJSON = (JSONObject) response.get(i);
                int id = pagamentoJSON.getInt("id");
                String metodoPagamento = pagamentoJSON.getString("metodopag");


                Pagamento pagamento = new Pagamento(id, metodoPagamento);
                pagamentos.add(pagamento);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return pagamentos;


    }
}
