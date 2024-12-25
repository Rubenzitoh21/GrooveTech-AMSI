package com.example.groovetech.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Produto implements Parcelable {
    private int id, iva;
    private String nome, descricao, obs, categoria, imagem;
    private float preco;

    public Produto(int id, String nome, String descricao, float preco, String obs, String categoria, int iva, String imagem) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.obs = obs;
        this.categoria = categoria;
        this.iva = iva;
        this.preco = preco * ((float) iva / 100) + preco;
        this.imagem = imagem;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getObs() {
        return obs;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public int getIva() {
        return iva;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getImagem() {
        return imagem;
    }

    //get all produtos

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", iva=" + iva +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", obs='" + obs + '\'' +
                ", categoria='" + categoria + '\'' +
                ", imagem='" + imagem + '\'' +
                ", preco=" + preco +
                '}';
    }

    protected Produto(Parcel in) {
        id = in.readInt();
        nome = in.readString();
        descricao = in.readString();
        obs = in.readString();
        categoria = in.readString();
        imagem = in.readString();
        preco = in.readFloat();
        iva = in.readInt();

    }

    public static final Creator<Produto> CREATOR = new Creator<Produto>() {
        @Override
        public Produto createFromParcel(Parcel in) {
            return new Produto(in);
        }

        @Override
        public Produto[] newArray(int size) {
            return new Produto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(nome);
        dest.writeString(descricao);
        dest.writeString(obs);
        dest.writeString(categoria);
        dest.writeString(imagem);
        dest.writeFloat(preco);
        dest.writeInt(iva);
    }

}
