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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.annotation.NonNull;


import com.example.kr_recycleview.R;
import com.example.kr_recycleview.data.Movie;
import com.example.kr_recycleview.viewmodel.MovieViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private MovieViewModel viewModel;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1) Тулбар
        MaterialToolbar tb = findViewById(R.id.toolbarMain);
        setSupportActionBar(tb);

        // 2) RecyclerView + Adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        movieAdapter = new MovieAdapter(this, movie -> {
            Intent intent = new Intent(this, AddEditMovieActivity.class);
            intent.putExtra("id", movie.id);
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(movieAdapter);

        new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override public boolean onMove(@NonNull RecyclerView rv,
                                                    @NonNull RecyclerView.ViewHolder vh,
                                                    @NonNull RecyclerView.ViewHolder tgt) {
                        // Перетаскивания не нужны
                        return false;
                    }

                    @Override public void onSwiped(@NonNull RecyclerView.ViewHolder vh,
                                                   int direction) {
                        // Берём фильм по позиции и удаляем через ViewModel
                        Movie m = movieAdapter.getMovieAt(vh.getAdapterPosition());
                        viewModel.delete(m);
                        // Никакого notifyDataSetChanged() — LiveData сама обновит список
                    }
                }
        ).attachToRecyclerView(recyclerView);

        // 3) ViewModel
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.getAll().observe(this, list -> movieAdapter.setMovieList(list));

        // 4) FAB для добавления
        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v ->
                startActivity(new Intent(this, AddEditMovieActivity.class))
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Меню только с поиском
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView sv = (SearchView) searchItem.getActionView();
        sv.setQueryHint("Поиск по названию или жанру…");
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

    // Больше не нужно обрабатывать R.id.action_add
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // (можно оставить для обработки home/up, если нужно)
        return super.onOptionsItemSelected(item);
    }
}
