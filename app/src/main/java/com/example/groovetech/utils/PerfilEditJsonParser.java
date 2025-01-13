package com.example.groovetech.utils;

import android.util.Log;

import com.example.groovetech.Modelo.Perfil;

import org.json.JSONException;
import org.json.JSONObject;

public class PerfilEditJsonParser {
    public static Perfil parseJsonProfile(JSONObject response) {
        Perfil perfil = null;
        try {
            JSONObject perfilJSON = response.getJSONObject("profileOnUpdate");
            int id = perfilJSON.getInt("id");
            String primeironome = perfilJSON.getString("primeironome");
            String apelido = perfilJSON.getString("apelido");
            String telefone = perfilJSON.getString("telefone");
            String nif = perfilJSON.getString("nif");
            String rua = perfilJSON.getString("rua");
            String localidade = perfilJSON.getString("localidade");
            String codigopostal = perfilJSON.getString("codigopostal");
            String genero = perfilJSON.getString("genero");
            String dtanasc = perfilJSON.getString("dtanasc");
            perfil = new Perfil(id, primeironome, apelido, codigopostal, localidade, rua, nif, dtanasc, "", telefone, genero, 0);
        } catch (JSONException e) {
            Log.e("PerfilEditJsonParser", "Error parsing JSON", e);
        }
        return perfil;
    }
}