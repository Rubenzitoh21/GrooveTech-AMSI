package com.example.groovetech.Modelo;

public class LinhasFaturasTotais {
    private static float totalIva;
    private static float subTotalLinhas;

    public LinhasFaturasTotais(float totalIva, float subTotalLinhas) {
        LinhasFaturasTotais.totalIva = totalIva;
        LinhasFaturasTotais.subTotalLinhas = subTotalLinhas;
    }

    public void setTotalIva(float totalIva) {
        LinhasFaturasTotais.totalIva = totalIva;
    }

    public void setSubTotalLinhas(float subTotalLinhas) {
        LinhasFaturasTotais.subTotalLinhas = subTotalLinhas;
    }

    public static float getTotalIva() {
        return totalIva;
    }

    public static float getSubTotalLinhas() {
        return subTotalLinhas;
    }
}
