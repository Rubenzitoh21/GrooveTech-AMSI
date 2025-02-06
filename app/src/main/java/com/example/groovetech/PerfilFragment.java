package com.example.groovetech;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.groovetech.Modelo.Perfil;
import com.example.groovetech.Modelo.Singleton;
import com.example.groovetech.Modelo.Utilizador;
import com.example.groovetech.Modelo.UtilizadorAndPerfil;
import com.example.groovetech.databinding.FragmentPerfilBinding;
import com.example.groovetech.listeners.PerfilListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PerfilFragment extends Fragment implements PerfilListener {
    private static final String TAG = "PerfilFragment";
    private static final String DATE_FORMAT_INPUT = "yyyy-MM-dd";
    private static final String DATE_FORMAT_OUTPUT = "dd/MM/yyyy";
    private boolean isDadosPessoaisActive = true;

    private FragmentPerfilBinding binding;
    private Perfil perfil;
    private Utilizador utilizador;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        fetchUserProfile();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupClickListeners();
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchUserProfile();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


        binding = null; // Prevent memory leaks
    }

    private void fetchUserProfile() {
        if (getContext() != null) {
            Singleton.getInstance(getContext())
                    .getUserProfileAPI(requireContext().getApplicationContext(), this);
        }
    }

    private void setupClickListeners() {
        binding.linearLayoutDadosMorada.setOnClickListener(v -> {
            displayDadosMorada();
            isDadosPessoaisActive = false;
        });
        binding.linearLayoutDadosPessoais.setOnClickListener(v -> {
            displayDadosPessoais();
            isDadosPessoaisActive = true;
        });
        binding.btnEditarPerfil.setOnClickListener(v -> navigateToEditProfile());

        binding.linearLayoutFaturas.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ListaFaturasActivity.class);
            startActivity(intent);
        });
    }

    private void displayDadosMorada() {
        binding.textoTelemovel.setText("Rua:");
        binding.textoGenero.setText("Localidade:");
        binding.textoNIF.setText("Código Postal:");

        // Display Address Data replacing Dados Pessoais data
        binding.tvTelemovel.setText(perfil.getRua());
        binding.tvNIF.setText(perfil.getCodigopostal());
        binding.tvGenero.setText(perfil.getLocalidade());

        binding.view3.setVisibility(View.GONE);
        binding.linearLayoutDtaNascimento.setVisibility(View.GONE);

    }

    private void displayDadosPessoais() {
        // Update labels

        binding.textoTelemovel.setText("Telemóvel:");
        binding.textoGenero.setText("Género:");
        binding.textoNIF.setText("NIF:");

        // Update Dados Pessoais
        binding.tvTelemovel.setText(perfil.getTelefone());
        binding.tvNIF.setText(perfil.getNif());
        binding.tvGenero.setText(formatGender(perfil.getGenero()));
        binding.tvDtaNascimento.setText(formatDtaNasc(perfil.getDtanasc()));

        // Show all views
        binding.view3.setVisibility(View.VISIBLE);
        binding.linearLayoutDtaNascimento.setVisibility(View.VISIBLE);
    }

    private void navigateToEditProfile() {
        Intent intent = new Intent(getContext(), PerfilEditActivity.class);
        intent.putExtra("perfil", perfil);
        startActivity(intent);
    }

    @Override
    public void onProfileDataLoaded(UtilizadorAndPerfil utilizadorAndPerfil) {
        if (utilizadorAndPerfil == null || binding == null) return;

        this.utilizador = utilizadorAndPerfil.getUtilizador();
        this.perfil = utilizadorAndPerfil.getPerfil();

        updateDefaultUI();
        Log.d(TAG, "Profile updated: " + utilizador);
    }

    private void updateDefaultUI() {
        if (utilizador == null || perfil == null || binding == null) return;

        if (isDadosPessoaisActive) {
            // Atualizar os dados do perfil
            binding.tvUsername.setText(utilizador.getUsername());
            binding.tvEmail.setText(utilizador.getEmail());
            binding.tvPrimeiroNome.setText(perfil.getPrimeironome());
            binding.tvApelido.setText(perfil.getApelido());
            binding.tvTelemovel.setText(perfil.getTelefone());
            binding.tvNIF.setText(perfil.getNif());
            binding.tvGenero.setText(formatGender(perfil.getGenero()));
            binding.tvDtaNascimento.setText(formatDtaNasc(perfil.getDtanasc()));
        } else {
            // Atualiza os dados da morada
            binding.tvTelemovel.setText(perfil.getRua());
            binding.tvNIF.setText(perfil.getCodigopostal());
            binding.tvGenero.setText(perfil.getLocalidade());
        }

    }

    private String formatGender(String gender) {
        if (gender == null) return "";

        return switch (gender) {
            case "M" -> "Masculino";
            case "F" -> "Feminino";
            default -> "";
        };
    }

    private static String formatDtaNasc(String dtaNasc) {
        if (dtaNasc == null || dtaNasc.isEmpty()) return "";

        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat(DATE_FORMAT_INPUT, Locale.US);
            SimpleDateFormat desiredFormat = new SimpleDateFormat(DATE_FORMAT_OUTPUT, Locale.getDefault());
            Date date = originalFormat.parse(dtaNasc);
            return date != null ? desiredFormat.format(date) : "";
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: " + dtaNasc, e);
            return dtaNasc;
        }
    }
}