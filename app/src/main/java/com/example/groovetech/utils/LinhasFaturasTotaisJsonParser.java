package com.example.groovetech.utils;

import android.util.Log;

import com.example.groovetech.Modelo.LinhasFaturasTotais;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LinhasFaturasTotaisJsonParser {

    public static LinhasFaturasTotais parserJsonTotals(JSONArray response) {
        float totalIva = 0;
        float subTotalLinhas = 0;

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject item = response.getJSONObject(i);

                if (item.has("totals")) {
                    JSONObject totals = item.getJSONObject("totals");
                    totalIva = (float) totals.getDouble("totalIva");
                    subTotalLinhas = (float) totals.getDouble("subTotalLinhas");
                    break;
                }
            }

        } catch (JSONException e) {
            Log.e("LinhasFaturasTotaisJsonParser", "Error parsing JSON", e);
        }

        return new LinhasFaturasTotais(totalIva, subTotalLinhas);
    }
}
