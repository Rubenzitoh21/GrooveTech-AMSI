package com.example.groovetech;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.groovetech.Modelo.Expedicao;
import com.example.groovetech.Modelo.Fatura;
import com.example.groovetech.Modelo.Pagamento;
import com.example.groovetech.Modelo.Singleton;
import com.example.groovetech.databinding.ActivityCheckoutBinding;
import com.example.groovetech.listeners.ExpedicoesListener;
import com.example.groovetech.listeners.FaturasListener;
import com.example.groovetech.listeners.PagamentosListener;

import java.util.ArrayList;


public class CheckoutActivity extends AppCompatActivity implements PagamentosListener, ExpedicoesListener {
    private FaturasListener faturaListener;
    private PagamentosListener pagamentosListener;
    private String selectedShippingMethod, selectedPaymentMethod;
    private ActivityCheckoutBinding binding;
    private ArrayList<Pagamento> listaPagamentos;
    private ArrayList<Expedicao> listaExpedicoes;
    private int selectedPagamentoID, selectedExpedicaoID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Singleton.getInstance(getApplicationContext()).getAllPagamentosAPI(this, this);
        Singleton.getInstance(getApplicationContext()).getAllExpedicoesAPI(this, this);


        binding.buttonProceedCheckout.setOnClickListener(v -> {
            Singleton.getInstance(getApplicationContext()).createFaturaAndLinhasFaturaAPI(getApplicationContext(), selectedPagamentoID,
                    selectedExpedicaoID);

            // Guardar o id do pagamento e da expedicao selecionados
            Singleton.getInstance(getApplicationContext()).setSelectedPagamentoID(selectedPagamentoID);
            Singleton.getInstance(getApplicationContext()).setSelectedExpedicaoID(selectedExpedicaoID);

            Toast.makeText(getApplicationContext(), getString(R.string.encomenda_finalizada_com_sucesso), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);


        });
    }

    @Override
    public void onPagamentosDataLoaded(ArrayList<Pagamento> listaPagamentos) {
        // Prepara o Spinner
        ArrayAdapter<Pagamento> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaPagamentos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.SpinnerPagamento.setAdapter(adapter);

        // Responsável pela seleção do método de pagamento selecionado
        binding.SpinnerPagamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                Pagamento selectedPagamento = (Pagamento) parent.getItemAtPosition(position);
                int selectedId = selectedPagamento.getId();

                // Passa o ID do método de pagamento selecionado para a variável selectedPagamentoID da classe
                CheckoutActivity.this.selectedPagamentoID = selectedId;
                String selectedMetodoPagamento = selectedPagamento.getMetodoPagamento();
                Toast.makeText(getApplicationContext(), getString(R.string.metodo_pagamento_selecionado) + selectedMetodoPagamento,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onExpedicoesDataLoaded(ArrayList<Expedicao> listaExpedicoes) {
        // Prepara o Spinner
        ArrayAdapter<Expedicao> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaExpedicoes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerExpedicao.setAdapter(adapter);

        // Responsável pela seleção do método de expedicao
        binding.spinnerExpedicao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                Expedicao selectedExpedicao = (Expedicao) parent.getItemAtPosition(position);
                int selectedId = selectedExpedicao.getId();
                CheckoutActivity.this.selectedExpedicaoID = selectedId;
                String selectedMetodoExpedicao = selectedExpedicao.getMetodoExpedicao();
                Toast.makeText(getApplicationContext(), getString(R.string.metodo_expedicao_selecionado) + selectedMetodoExpedicao,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
