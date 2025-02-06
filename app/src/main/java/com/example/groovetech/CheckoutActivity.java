package com.example.groovetech;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.groovetech.Modelo.Expedicao;
import com.example.groovetech.Modelo.Fatura;
import com.example.groovetech.Modelo.Pagamento;
import com.example.groovetech.Modelo.Singleton;
import com.example.groovetech.Modelo.UtilizadorAndPerfil;
import com.example.groovetech.databinding.ActivityCheckoutBinding;
import com.example.groovetech.listeners.ExpedicoesListener;
import com.example.groovetech.listeners.FaturasListener;
import com.example.groovetech.listeners.PagamentosListener;
import com.example.groovetech.listeners.PerfilEditListener;
import com.example.groovetech.listeners.PerfilListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class CheckoutActivity extends AppCompatActivity implements PagamentosListener, ExpedicoesListener, PerfilListener,
        PerfilEditListener {
    private FaturasListener faturaListener;
    private PagamentosListener pagamentosListener;
    private String selectedShippingMethod, selectedPaymentMethod;
    private ActivityCheckoutBinding binding;
    private ArrayList<Pagamento> listaPagamentos;
    private ArrayList<Expedicao> listaExpedicoes;
    private int selectedPagamentoID, selectedExpedicaoID;
    private JSONObject profileData;

    // Validation patterns
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{9}$");
    private static final Pattern POSTAL_CODE_PATTERN = Pattern.compile("^\\d{4}-\\d{3}$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Singleton.getInstance(getApplicationContext()).getAllPagamentosAPI(this, this);
        Singleton.getInstance(getApplicationContext()).getAllExpedicoesAPI(this, this);
        Singleton.getInstance(getApplicationContext()).getUserProfileAPI(this, this);
        setupInputListeners();


        binding.buttonProceedCheckout.setOnClickListener(v -> {
            Singleton.getInstance(getApplicationContext()).createFaturaAndLinhasFaturaAPI(getApplicationContext(), selectedPagamentoID,
                    selectedExpedicaoID);
            updateUserProfile();

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

    private void setupInputListeners() {
        TextWatcher formValidationWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateForm();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        // Add the TextWatcher to all EditText fields
        binding.etNome.addTextChangedListener(formValidationWatcher);
        binding.etApelido.addTextChangedListener(formValidationWatcher);
        binding.etRua.addTextChangedListener(formValidationWatcher);
        binding.etCodigoPostal.addTextChangedListener(formValidationWatcher);
        binding.etLocalidade.addTextChangedListener(formValidationWatcher);

        // Perform initial validation when the activity starts
        validateForm();
    }

    // Method to validate all fields
    private void validateForm() {
        boolean isNomeValid = validateNome();
        boolean isApelidoValid = validateApelido();
        boolean isRuaValid = validateRua();
        boolean isCodigoPostalValid = validateCodigoPostal();
        boolean isLocalidadeValid = validateLocalidade();
        boolean isTelemovelValid = validateTelemovel();

        // Enable the button only if all fields are valid
        binding.buttonProceedCheckout.setEnabled(
                isNomeValid && isApelidoValid && isRuaValid && isCodigoPostalValid && isLocalidadeValid && isTelemovelValid
        );
    }

    private boolean validateCodigoPostal() {
        String postalCode = binding.etCodigoPostal.getText().toString().trim();
        if (!postalCode.isEmpty() && !POSTAL_CODE_PATTERN.matcher(postalCode).matches()) {
            binding.etCodigoPostal.setError(getString(R.string.codigopostal_invalido));
            return false;
        }
        if (postalCode.isEmpty()) {
            binding.etCodigoPostal.setError(getString(R.string.codigopostal_obrigatorio));
            return false;
        }
        binding.etCodigoPostal.setError(null);
        return true;
    }

    private boolean validateNome() {
        String nome = binding.etNome.getText().toString().trim();
        if (nome.isEmpty()) {
            binding.etNome.setError(getString(R.string.nome_obrigatorio));
            return false;
        }
        binding.etNome.setError(null);
        return true;
    }

    private boolean validateApelido() {
        String apelido = binding.etApelido.getText().toString().trim();
        if (apelido.isEmpty()) {
            binding.etApelido.setError(getString(R.string.apelido_obrigatorio));
            return false;
        }
        binding.etApelido.setError(null);
        return true;
    }

    // Validate localidade
    private boolean validateLocalidade() {
        String localidade = binding.etLocalidade.getText().toString().trim();
        if (localidade.isEmpty()) {
            binding.etLocalidade.setError(getString(R.string.localidade_obrigatoria));
            return false;
        }
        binding.etLocalidade.setError(null);
        return true;
    }

    //validate rua
    private boolean validateRua() {
        String rua = binding.etRua.getText().toString().trim();
        if (rua.isEmpty()) {
            binding.etRua.setError(getString(R.string.rua_obrigatoria));
            return false;
        }
        binding.etRua.setError(null);
        return true;
    }

    //validate telemovel
    private boolean validateTelemovel() {
        String phone = binding.etTelemovel.getText().toString().trim();
        if (!phone.isEmpty() && !PHONE_PATTERN.matcher(phone).matches()) {
            binding.etTelemovel.setError(getString(R.string.telemovel_invalido));
            return false;
        }
        binding.etTelemovel.setError(null);
        return true;
    }

    @Override
    public void onProfileDataLoaded(UtilizadorAndPerfil utilizadorAndPerfil) {
        //get user profile data if they're not null
        if (utilizadorAndPerfil != null) {
            binding.etNome.setText(utilizadorAndPerfil.getPerfil().getPrimeironome());
            binding.etApelido.setText(utilizadorAndPerfil.getPerfil().getApelido());
            binding.etRua.setText(utilizadorAndPerfil.getPerfil().getRua());
            binding.etCodigoPostal.setText(utilizadorAndPerfil.getPerfil().getCodigopostal());
            binding.etLocalidade.setText(utilizadorAndPerfil.getPerfil().getLocalidade());
            binding.etTelemovel.setText(utilizadorAndPerfil.getPerfil().getTelefone());
        }
    }

    private void updateUserProfile() {
        try {
            profileData = new JSONObject();
            addFieldToProfile("primeironome", binding.etNome.getText().toString());
            addFieldToProfile("apelido", binding.etApelido.getText().toString());
            addFieldToProfile("telefone", binding.etTelemovel.getText().toString());
            addFieldToProfile("rua", binding.etRua.getText().toString());
            addFieldToProfile("localidade", binding.etLocalidade.getText().toString());
            addFieldToProfile("codigopostal", binding.etCodigoPostal.getText().toString());

            Log.d("CHECKOUT", "Profile data: " + profileData);
            Singleton.getInstance(this).updateUserProfileAPI(this, this, profileData);

        } catch (Exception e) {
            Log.e("ERROR CHECKOUT", "Error updating profile", e);
            Toast.makeText(this, R.string.erro_atualizar_perfil, Toast.LENGTH_SHORT).show();
        }
    }

    private void addFieldToProfile(String key, String value) throws Exception {
        profileData.put(key, value == null ? "" : value.trim());
    }

    @Override
    public void onUpdateProfile() {

    }
}
