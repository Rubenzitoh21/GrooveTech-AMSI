package com.example.groovetech.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class LinhasFaturas {
    private int id, quantidade, faturas_id, produtos_id;
    private float preco_venda, valor_iva, subtotal, subtotalLinhas, valorTotalIva;

    public LinhasFaturas(int id, int quantidade, float preco_venda, float valor_iva, float subtotal,
                         int faturas_id, int produtos_id) {
        this.id = id;
        this.quantidade = quantidade;
        this.preco_venda = preco_venda;
        this.valor_iva = valor_iva;
        this.subtotal = subtotal;
        this.faturas_id = faturas_id;
        this.produtos_id = produtos_id;
    }

    public void setSubtotalLinhas(float subtotalLinhas) {
        this.subtotalLinhas = subtotalLinhas;
    }

    public void setValorTotalIva(float valorTotalIva) {
        this.valorTotalIva = valorTotalIva;
    }

    public float getSubtotalLinhas() {
        return subtotalLinhas;
    }

    public float getValorTotalIva() {
        return valorTotalIva;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setFaturas_id(int faturas_id) {
        this.faturas_id = faturas_id;
    }

    public void setProdutos_id(int produtos_id) {
        this.produtos_id = produtos_id;
    }


    public void setPreco_venda(float preco_venda) {
        this.preco_venda = preco_venda;
    }

    public void setValor_iva(float valor_iva) {
        this.valor_iva = valor_iva;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public int getId() {
        return id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public int getFaturas_id() {
        return faturas_id;
    }

    public int getProdutos_id() {
        return produtos_id;
    }

    public float getPreco_venda() {
        return preco_venda;
    }

    public float getValor_iva() {
        return valor_iva;
    }

    public float getSubtotal() {
        return subtotal;
    }
}
