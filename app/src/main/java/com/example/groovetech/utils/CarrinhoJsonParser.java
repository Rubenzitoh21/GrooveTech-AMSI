package com.example.groovetech.utils;

import android.util.Log;

import com.example.groovetech.Modelo.Carrinho;

import org.json.JSONException;
import org.json.JSONObject;


public class CarrinhoJsonParser {

    public static Carrinho parserJsonCarrinho(JSONObject response) {
        Carrinho carrinho = null;
        try {
            JSONObject carrinhoJSON = new JSONObject(response.toString());
            int id = carrinhoJSON.getInt("id");
            String dtapedido = carrinhoJSON.getString("dtapedido");
            String status = carrinhoJSON.getString("status");
            float valortotal = (float) carrinhoJSON.getDouble("valortotal");
            int userid = carrinhoJSON.getInt("user_id");

            carrinho = new Carrinho(id, userid, status, dtapedido, valortotal);


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return carrinho;
    }
}
