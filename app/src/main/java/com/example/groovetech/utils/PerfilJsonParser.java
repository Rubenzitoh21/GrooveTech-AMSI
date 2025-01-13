package com.example.groovetech.utils;

import android.util.Log;

import com.example.groovetech.Modelo.Perfil;
import com.example.groovetech.Modelo.Utilizador;
import com.example.groovetech.Modelo.UtilizadorAndPerfil;

import org.json.JSONException;
import org.json.JSONObject;

public class PerfilJsonParser {
    public static UtilizadorAndPerfil parserJsonUtilizadorProfile(JSONObject response) {
        Perfil perfil = null;
        Utilizador utilizador = null;
        try {
            // Parse Utilizador
            JSONObject userJSON = response.getJSONObject("user");
            String username = userJSON.getString("username");
            String email = userJSON.getString("email");
            utilizador = new Utilizador(username, email);

            // Parse Perfil
            JSONObject userProfileJSON = response.getJSONObject("profile");
            int id = userProfileJSON.getInt("id");
            String primeironome = userProfileJSON.getString("primeironome");
            String apelido = userProfileJSON.getString("apelido");
            String codigopostal = userProfileJSON.getString("codigopostal");
            String localidade = userProfileJSON.getString("localidade");
            String rua = userProfileJSON.getString("rua");
            String nif = userProfileJSON.getString("nif");
            String dtanasc = userProfileJSON.getString("dtanasc");
            String dtaregisto = userProfileJSON.getString("dtaregisto");
            String telefone = userProfileJSON.getString("telefone");
            String genero = userProfileJSON.getString("genero");
            int user_id = userProfileJSON.getInt("user_id");
            perfil = new Perfil(id, primeironome, apelido, codigopostal, localidade, rua, nif,
                    dtanasc, dtaregisto, telefone, genero, user_id);

        } catch (JSONException e) {
            Log.e("PerfilJsonParser", "Error parsing JSON", e);
        }
        return new UtilizadorAndPerfil(utilizador, perfil);
    }
}
