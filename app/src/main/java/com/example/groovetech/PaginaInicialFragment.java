package com.example.groovetech;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.groovetech.databinding.FragmentPaginaInicialBinding;

public class PaginaInicialFragment extends Fragment {

    private FragmentPaginaInicialBinding binding;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inicialização do View Binding
        binding = FragmentPaginaInicialBinding.inflate(inflater, container, false);
        preferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE); // Inicializa SharedPreferences
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String username = getUsername();
        if (username == null) {
            return;
        }

        binding.txtUsername.setText(username);
        binding.imgLogout.setOnClickListener(this::showLogoutDialog);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Evita fugas de memória anulando a binding
    }

    private String getUsername() {
        return preferences.getString("username", null);
    }

    // Exibe um diálogo de confirmação para o logout
    public void showLogoutDialog(View view) {
        new AlertDialog.Builder(requireContext())
                .setMessage("Tem certeza que deseja sair?")
                .setCancelable(false) // Impede o fechamento do diálogo ao tocar fora dele
                .setPositiveButton("Sim", (dialog, id) -> {
                    // O utilizador confirmou o logout
                    logout();
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    // O utilizador cancelou o logout
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    private void logout() {
        preferences.edit().clear().apply();
        //Termina a atividade atual
        requireActivity().finish();
    }
}
