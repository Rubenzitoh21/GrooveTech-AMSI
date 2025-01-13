package com.example.groovetech.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class LinhasFaturas implements Parcelable {
    private int quantidade, iva;
    private String nomeProduto;
    private float valor, valor_iva, total;

    public LinhasFaturas(int quantidade, int iva, String nomeProduto, float valor, float valor_iva, float total) {
        this.quantidade = quantidade;
        this.iva = iva;
        this.nomeProduto = nomeProduto;
        this.valor = valor;
        this.valor_iva = valor_iva;
        this.total = total;
    }

    protected LinhasFaturas(Parcel in) {
        quantidade = in.readInt();
        iva = in.readInt();
        nomeProduto = in.readString();
        valor = in.readFloat();
        valor_iva = in.readFloat();
        total = in.readFloat();
    }



    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getIva() {
        return iva;
    }

    public void setIva(int iva) {
        this.iva = iva;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public float getValor_iva() {
        return valor_iva;
    }

    public void setValor_iva(float valor_iva) {
        this.valor_iva = valor_iva;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<LinhasFaturas> CREATOR = new Creator<LinhasFaturas>() {
        @Override
        public LinhasFaturas createFromParcel(Parcel in) {
            return new LinhasFaturas(in);
        }

        @Override
        public LinhasFaturas[] newArray(int size) {
            return new LinhasFaturas[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(quantidade);
        dest.writeInt(iva);
        dest.writeString(nomeProduto);
        dest.writeFloat(valor);
        dest.writeFloat(valor_iva);
        dest.writeFloat(total);
    }
}
