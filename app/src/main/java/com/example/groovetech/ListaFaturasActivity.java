package com.example.groovetech;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groovetech.Modelo.Fatura;
import com.example.groovetech.Modelo.Singleton;
import com.example.groovetech.adaptadores.ListaFaturasAdaptador;
import com.example.groovetech.databinding.ActivityListaFaturasBinding;
import com.example.groovetech.listeners.FaturaListener;
import com.example.groovetech.listeners.FaturasListener;

import java.util.ArrayList;
import java.util.Collections;


public class ListaFaturasActivity extends AppCompatActivity implements FaturasListener, FaturaListener {
    private ActivityListaFaturasBinding binding;
    private ListaFaturasAdaptador adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaFaturasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Singleton.getInstance(getApplicationContext()).getAllFaturasByUserIdAPI(this, this);

        binding.backButton.setOnClickListener(v -> onBackPressed());

    }


    @Override
    public void onFaturasDataLoaded(ArrayList<Fatura> listaFaturas) {

        // Reverte a ordem das faturas para mostrar a mais recente primeiro
        Collections.reverse(listaFaturas);

        adapter = new ListaFaturasAdaptador(this, listaFaturas, this);
        binding.rvListaFaturas.setLayoutManager(new GridLayoutManager(this, 1));
        binding.rvListaFaturas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFaturaClick(int position, Fatura fatura) {
        Intent intent = new Intent(this, FaturaActivity.class);
        intent.putExtra("faturaID", fatura.getId());
        startActivity(intent);
    }
}