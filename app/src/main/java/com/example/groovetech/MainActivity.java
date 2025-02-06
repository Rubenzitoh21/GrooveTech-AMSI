package com.example.groovetech;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.groovetech.Modelo.Singleton;
import com.example.groovetech.databinding.ActivityMainBinding;
import com.google.android.material.badge.BadgeDrawable;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Singleton.getInstance(getApplicationContext()).createCarrinhoAPI(this);

        fragmentManager = getSupportFragmentManager();

        // Load the initial fragment
        CarregarFragmentAtual(new PaginaInicialFragment());

        // Setup BottomNavigationView listener
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);


    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();
    }

    public void updateCartBadge() {
        BadgeDrawable badge = binding.bottomNavigationView.getOrCreateBadge(R.id.carrinho);
        Singleton.getInstance(getApplicationContext()).getLinhasCarrinhoAPI(getApplicationContext(), linhasCarrinho -> {
            int total = 0;
            for (int i = 0; i < linhasCarrinho.size(); i++) {
                total += linhasCarrinho.get(i).getQuantidade();
            }

            badge.setNumber(total);


            if (total > 0) {
                badge.setVisible(true);
            } else {
                badge.setVisible(false);
                badge.clearNumber();
            }
        });
    }


    private boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.home) {
            CarregarFragmentAtual(new PaginaInicialFragment());
            return true;
        } else if (itemId == R.id.carrinho) {
            startActivity(new Intent(this, CarrinhoActivity.class));
            return true;
        } else if (itemId == R.id.perfil) {
            CarregarFragmentAtual(new PerfilFragment());
            return true;
        } else if (itemId == R.id.favoritos) {
            CarregarFragmentAtual(new FavoritosFragment());
            return true;
        }
        return false;
    }


    private void CarregarFragmentAtual(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Avoid potential memory leaks
    }
}
