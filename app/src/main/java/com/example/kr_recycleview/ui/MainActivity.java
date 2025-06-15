package com.example.kr_recycleview.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.example.kr_recycleview.R;
import com.example.kr_recycleview.data.Movie;
import com.example.kr_recycleview.viewmodel.MovieViewModel;

public class MainActivity extends AppCompatActivity {

    private MovieViewModel viewModel;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1) Привязываем и устанавливаем наш тулбар
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        // 2) Настройка RecyclerView + Adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        movieAdapter = new MovieAdapter(this, movie -> {
            // при клике на элемент открываем экран редактирования
            Intent intent = new Intent(MainActivity.this, AddEditMovieActivity.class);
            intent.putExtra("id", movie.id);
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(movieAdapter);

        // 3) Инициализируем ViewModel и подписываемся на список
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.getAll().observe(this, movies -> {
            movieAdapter.setMovieList(movies);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Подключаем меню с поиском и кнопкой “+”
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Настраиваем SearchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView sv = (SearchView) searchItem.getActionView();
        sv.setQueryHint("Поиск по названию или жанру...");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override public boolean onQueryTextChange(String query) {
                viewModel.search(query)
                        .observe(MainActivity.this, movies -> movieAdapter.setMovieList(movies));
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Обработка кнопки “+”
        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(this, AddEditMovieActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
