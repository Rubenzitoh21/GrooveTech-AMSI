package com.example.groovetech.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Perfil implements Parcelable {
    private int id;
    private String primeironome;
    private String apelido;
    private String codigopostal;
    private String localidade;
    private String rua;
    private String nif;
    private String dtanasc;
    private String dtaregisto;
    private String telefone;
    private String genero;
    private int user_id;

    public Perfil(int id, String primeironome, String apelido, String codigopostal, String localidade, String rua, String nif,
                  String dtanasc, String dtaregisto, String telefone, String genero, int user_id) {
        this.id = id;
        this.primeironome = primeironome;
        this.apelido = apelido;
        this.codigopostal = codigopostal;
        this.localidade = localidade;
        this.rua = rua;
        this.nif = nif;
        this.dtanasc = dtanasc;
        this.dtaregisto = dtaregisto;
        this.telefone = telefone;
        this.genero = genero;
        this.user_id = user_id;
    }

    protected Perfil(Parcel in) {
        id = in.readInt();
        primeironome = in.readString();
        apelido = in.readString();
        codigopostal = in.readString();
        localidade = in.readString();
        rua = in.readString();
        nif = in.readString();
        dtanasc = in.readString();
        dtaregisto = in.readString();
        telefone = in.readString();
        genero = in.readString();
        user_id = in.readInt();
    }

    public static final Creator<Perfil> CREATOR = new Creator<Perfil>() {
        @Override
        public Perfil createFromParcel(Parcel in) {
            return new Perfil(in);
        }

        @Override
        public Perfil[] newArray(int size) {
            return new Perfil[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getPrimeironome() {
        return primeironome;
    }

    public String getApelido() {
        return apelido;
    }

    public String getCodigopostal() {
        return codigopostal;
    }

    public String getLocalidade() {
        return localidade;
    }

    public String getRua() {
        return rua;
    }

    public String getNif() {
        return nif;
    }

    public String getDtanasc() {
        return dtanasc;
    }

    public String getDtaregisto() {
        return dtaregisto;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getGenero() {
        return genero;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrimeironome(String primeironome) {
        this.primeironome = primeironome;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public void setCodigopostal(String codigopostal) {
        this.codigopostal = codigopostal;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public void setDtanasc(String dtanasc) {
        this.dtanasc = dtanasc;
    }

    public void setDtaregisto(String dtaregisto) {
        this.dtaregisto = dtaregisto;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(primeironome);
        dest.writeString(apelido);
        dest.writeString(codigopostal);
        dest.writeString(localidade);
        dest.writeString(rua);
        dest.writeString(nif);
        dest.writeString(dtanasc);
        dest.writeString(dtaregisto);
        dest.writeString(telefone);
        dest.writeString(genero);
        dest.writeInt(user_id);
    }

    @Override
    public String toString() {
        return "Perfil{" +
                "id=" + id +
                ", primeironome='" + primeironome + '\'' +
                ", apelido='" + apelido + '\'' +
                ", codigopostal='" + codigopostal + '\'' +
                ", localidade='" + localidade + '\'' +
                ", rua='" + rua + '\'' +
                ", nif='" + nif + '\'' +
                ", dtanasc='" + dtanasc + '\'' +
                ", dtaregisto='" + dtaregisto + '\'' +
                ", telefone='" + telefone + '\'' +
                ", genero='" + genero + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}
