package com.example.groovetech;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.groovetech.Modelo.Perfil;
import com.example.groovetech.Modelo.Singleton;
import com.example.groovetech.Modelo.UtilizadorAndPerfil;
import com.example.groovetech.databinding.ActivityPerfilEditBinding;
import com.example.groovetech.listeners.PerfilEditListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;

public class PerfilEditActivity extends AppCompatActivity implements PerfilEditListener {
    private static final String TAG = "PerfilEditActivity";
    private static final String[] GENDER_OPTIONS = {"M", "F"};
    private static final int DEFAULT_YEAR = 2000;
    private static final int DEFAULT_MONTH = 0;
    private static final int DEFAULT_DAY = 1;

    // Validation patterns
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{9}$");
    private static final Pattern NIF_PATTERN = Pattern.compile("^[0-9]{9}$");
    private static final Pattern POSTAL_CODE_PATTERN = Pattern.compile("^\\d{4}-\\d{3}$");

    private ActivityPerfilEditBinding binding;
    private JSONObject profileData;
    private Perfil perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializarViewBinding();
        getUserProfile();
        setupClickListeners();
        setupGenderAutoComplete();
        setupInputListeners();
    }

    private void inicializarViewBinding() {
        binding = ActivityPerfilEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void setupClickListeners() {
        binding.btnCancelar.setOnClickListener(v -> finish());
        binding.dataPickerButton.setOnClickListener(v -> showDatePicker());
        binding.btnAtualizarDados.setOnClickListener(v -> {
            if (validateAllFields()) {
                updateUserProfile();
            }
        });
    }

    private void setupGenderAutoComplete() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                GENDER_OPTIONS
        );

        binding.autoCompleteTxtGenero.setAdapter(adapter);
        binding.autoCompleteTxtGenero.setThreshold(1);
        binding.autoCompleteTxtGenero.setOnItemClickListener((parent, view, position, id) -> {
            String selectedGender = (String) parent.getItemAtPosition(position);
            Toast.makeText(this, getString(R.string.genero_selecionado, selectedGender), Toast.LENGTH_SHORT).show();
        });
    }

    private void setupInputListeners() {
        binding.etNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.etNome.setError(null);
            }
        });
        // Setup validation for phone number
        binding.etTelemovel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validatePhone();
            }
        });

        // Setup validation for NIF
        binding.etNif.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validateNIF();
            }
        });

        // Setup validation for postal code
        binding.etCodigoPostal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validatePostalCode();

            }
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String formattedDate = formatDate(dayOfMonth, month + 1, year);
                    binding.dataPickerButton.setText(formattedDate);
                },
                DEFAULT_YEAR,
                DEFAULT_MONTH,
                DEFAULT_DAY
        );
        datePickerDialog.show();
    }

    private String formatDate(int day, int month, int year) {
        return String.format(Locale.getDefault(), "%02d-%02d-%d", day, month, year);
    }

    private boolean validatePhone() {
        String phone = binding.etTelemovel.getText().toString().trim();
        if (!phone.isEmpty() && !PHONE_PATTERN.matcher(phone).matches()) {
            binding.etTelemovel.setError(getString(R.string.telemovel_invalido));
            return false;
        }
        binding.etTelemovel.setError(null);
        return true;
    }

    private boolean validateNIF() {
        String nif = binding.etNif.getText().toString().trim();
        if (!nif.isEmpty() && !NIF_PATTERN.matcher(nif).matches()) {
            binding.etNif.setError(getString(R.string.nif_invalido));
            return false;
        }
        binding.etNif.setError(null);
        return true;
    }

    private boolean validatePostalCode() {
        String postalCode = binding.etCodigoPostal.getText().toString().trim();
        if (!postalCode.isEmpty() && !POSTAL_CODE_PATTERN.matcher(postalCode).matches()) {
            binding.etCodigoPostal.setError(getString(R.string.codigopostal_invalido));
            return false;
        }
        binding.etCodigoPostal.setError(null);
        return true;
    }

    private boolean validateAllFields() {
        return validatePhone() && validateNIF() && validatePostalCode();
    }

    private void getUserProfile() {
        Singleton.getInstance(this).getUserProfileAPI(this, null);

        UtilizadorAndPerfil utilizadorAndPerfil = Singleton.getInstance(this).getUtilizadorAndPerfil();
        perfil = utilizadorAndPerfil.getPerfil();

        binding.etNome.setText(perfil.getPrimeironome());
        binding.etApelido.setText(perfil.getApelido());
        binding.etTelemovel.setText(perfil.getTelefone());
        binding.etNif.setText(perfil.getNif());
        binding.etRua.setText(perfil.getRua());
        binding.etLocalidade.setText(perfil.getLocalidade());
        binding.etCodigoPostal.setText(perfil.getCodigopostal());
        binding.autoCompleteTxtGenero.setText(perfil.getGenero());

        //Formata a data de nascimento antes de a apresentar nos campos de texto
        if (perfil.getDtanasc() != null && !perfil.getDtanasc().isEmpty()) {
            binding.dataPickerButton.setText(formatDate(utilizadorAndPerfil.getPerfil().getDtanasc()));
        }
    }

    private void updateUserProfile() {
        try {
            profileData = new JSONObject();
            addFieldToProfile("primeironome", binding.etNome.getText().toString());
            addFieldToProfile("apelido", binding.etApelido.getText().toString());
            addFieldToProfile("telefone", binding.etTelemovel.getText().toString());
            addFieldToProfile("nif", binding.etNif.getText().toString());
            addFieldToProfile("rua", binding.etRua.getText().toString());
            addFieldToProfile("localidade", binding.etLocalidade.getText().toString());
            addFieldToProfile("codigopostal", binding.etCodigoPostal.getText().toString());
            addFieldToProfile("genero", binding.autoCompleteTxtGenero.getText().toString());
            addFieldToProfile("dtanasc", binding.dataPickerButton.getText().toString());

            Log.d(TAG, "Profile data: " + profileData);
            Singleton.getInstance(this).updateUserProfileAPI(this, this, profileData);

        } catch (Exception e) {
            Log.e(TAG, "Error updating profile", e);
            Toast.makeText(this, R.string.erro_atualizar_perfil, Toast.LENGTH_SHORT).show();
        }
    }

    private String formatDate(String dataOriginal) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

            return outputFormat.format(inputFormat.parse(dataOriginal));
        } catch (Exception e) {
            Log.e(TAG, "Error formatting date: " + dataOriginal, e);
            return dataOriginal;
        }
    }

    private void addFieldToProfile(String key, String value) throws Exception {
        profileData.put(key, value == null ? "" : value.trim());
    }


    @Override
    public void onUpdateProfile() {
        Toast.makeText(this, R.string.perfil_atualizado_sucesso, Toast.LENGTH_SHORT).show();
        finish();
    }
}