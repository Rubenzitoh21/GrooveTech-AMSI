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
            //get json Object "carrinho"
            carrinhoJSON = carrinhoJSON.getJSONObject("carrinho");
            int id = carrinhoJSON.getInt("id");
            String dtapedido = carrinhoJSON.getString("dtapedido");
            String status = carrinhoJSON.getString("status");
            float valortotal = (float) carrinhoJSON.getDouble("valortotal");
            int userid = carrinhoJSON.getInt("user_id");

            //Valores Totais do Carrinho
            double totalIva = response.getDouble("total_iva");
            double totalSubtotal = response.getDouble("subtotal");

            carrinho = new Carrinho(id, userid, status, dtapedido, valortotal, totalIva, totalSubtotal);


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return carrinho;
    }
}
