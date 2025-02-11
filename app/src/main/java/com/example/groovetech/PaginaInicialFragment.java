package com.example.groovetech;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.groovetech.Modelo.Produto;
import com.example.groovetech.Modelo.Singleton;
import com.example.groovetech.adaptadores.HomeProdutosAdaptador;
import com.example.groovetech.databinding.FragmentPaginaInicialBinding;
import com.example.groovetech.listeners.HomeProdutosListener;
import com.example.groovetech.listeners.HomeProdutosSearchListener;
import com.google.android.material.badge.BadgeDrawable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaginaInicialFragment extends Fragment implements HomeProdutosListener, HomeProdutosSearchListener {

    private FragmentPaginaInicialBinding binding;
    private SharedPreferences preferences;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inicialização do View Binding
        binding = FragmentPaginaInicialBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE); // Inicializa SharedPreferences

        // Vai buscar a lista de produtos à API
        Singleton.getInstance(getContext()).getAllProdutosAPI(requireContext().getApplicationContext(), this);

        String username = getUsername();
        if (username == null) {
            return;
        }

        binding.txtUsername.setText(username);
        binding.imgLogout.setOnClickListener(this::showLogoutDialog);


        //Pesquisa de produtos
        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Singleton.getInstance(getContext()).getSearchProdutosAPI(requireContext(), query, PaginaInicialFragment.this);
                binding.searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    // se o texto da pesquisa estiver vazio, mostra todos os produtos
                    Singleton.getInstance(getContext()).getAllProdutosAPI(requireContext().getApplicationContext(),
                            PaginaInicialFragment.this);
                    binding.searchView.clearFocus();
                    binding.tituloTxt.setText(getString(R.string.txt_titulo_home));
                } else {
                    binding.searchView.setVisibility(View.VISIBLE);
                }
                return false;
            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Evita fugas de memória anulando a binding
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.searchView.clearFocus();

    }

    private String getUsername() {
        return preferences.getString("username", null);
    }

    // Exibe um diálogo de confirmação para o logout
    public void showLogoutDialog(View view) {
        new AlertDialog.Builder(requireContext()).setMessage("Tem certeza que deseja sair?").setCancelable(false) // Impede o fechamento
                // do diálogo ao tocar fora dele
                .setPositiveButton("Sim", (dialog, id) -> {
                    logout();
                }).setNegativeButton("Cancelar", (dialog, id) -> {
                    dialog.dismiss();
                }).create().show();
    }

    private void logout() {
        preferences.edit().clear().apply();
        //Termina a atividade atual
        requireActivity().finish();
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        startActivity(intent);

    }

    @Override
    public void onRefreshHomeProdutos(ArrayList<Produto> listaProdutos) {

        // Configuração do layout da recyclerview
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        binding.HomeProdutosRV.setLayoutManager(gridLayoutManager);

        // Cria o adaptador com a lista limitada
        HomeProdutosAdaptador adapter = new HomeProdutosAdaptador(getContext(), listaProdutos);

        // Define o adaptador para o RecyclerView
        binding.HomeProdutosRV.setAdapter(adapter);

        // Oculta a barra de progresso
        binding.progressBarHomeProdutos.setVisibility(View.GONE);
    }

    @Override
    public void onSearchResults(ArrayList<Produto> listaProdutos) {
        if (binding.HomeProdutosRV.getAdapter() instanceof HomeProdutosAdaptador) {
            HomeProdutosAdaptador adapter = (HomeProdutosAdaptador) binding.HomeProdutosRV.getAdapter();
            adapter.updateProdutos(listaProdutos); // atualiza a lista de produtos com os resultados da pesquisa

        }
    }
}