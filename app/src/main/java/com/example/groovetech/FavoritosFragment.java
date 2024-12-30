package com.example.groovetech;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.groovetech.Modelo.Favoritos;
import com.example.groovetech.Modelo.FavoritosBDHelper;
import com.example.groovetech.adaptadores.FavoritosAdaptador;
import com.example.groovetech.databinding.FragmentFavoritosBinding;
import com.example.groovetech.listeners.FavoritosListener;

import java.util.ArrayList;
import java.util.List;



public class FavoritosFragment extends Fragment implements FavoritosListener {

    private FragmentFavoritosBinding binding;
    private int userID;

    private FavoritosListener favoritosListener;

    private FavoritosAdaptador adapter;

    ArrayList<Favoritos> favoritosList = new ArrayList<>();

    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFavoritosBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        preferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);

        binding.rvFavoritos.setLayoutManager(new GridLayoutManager(getContext(), 2));
        //buscar o id do utilizador
        userID = preferences.getInt("user_id", 0);
        //buscar os dados da BD
        FavoritosBDHelper dbHelper = new FavoritosBDHelper(getContext());
        favoritosList = (ArrayList<Favoritos>) dbHelper.getAllFavoritos(userID);


        adapter = new FavoritosAdaptador(getContext(), favoritosList, this);
        binding.rvFavoritos.setAdapter(adapter);
        adapter.setFavoritosListener(this);
    }

    @Override
    public void onFavoritosItemRemoved(Favoritos favoritos) {
        FavoritosBDHelper dbHelper = new FavoritosBDHelper(getContext());
        dbHelper.removerEstadoFavoritosBD(favoritos.getId_user(), favoritos.getIdProduto());

        // Atualiza o arraylist removendo o item selecionado
        favoritosList.remove(favoritos);

        // Notifica o adaptador que os dados foram alterados
        adapter.notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }

}