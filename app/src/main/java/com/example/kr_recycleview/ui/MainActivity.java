package com.example.kr_recycleview.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kr_recycleview.R;
import com.example.kr_recycleview.data.Movie;
import com.example.kr_recycleview.viewmodel.MovieViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/** Главный экран каталога фильмов */
public class MainActivity extends AppCompatActivity {

    private MovieViewModel viewModel;
    private MovieAdapter   movieAdapter;

    // --- UI фильтр ---
    private Spinner spinnerCategory;     // «Все / Хочу посмотреть / Просмотрено»

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 1. Тулбар */
        MaterialToolbar tb = findViewById(R.id.toolbarMain);
        setSupportActionBar(tb);

        /* 2. RecyclerView + адаптер */
        RecyclerView rv = findViewById(R.id.recyclerView);
        movieAdapter = new MovieAdapter(this, movie -> {
            Intent i = new Intent(this, AddEditMovieActivity.class);
            i.putExtra("id", movie.id);
            startActivity(i);
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(movieAdapter);

        /* 3. свайп-удаление */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override public boolean onMove(@NonNull RecyclerView r,
                                            @NonNull RecyclerView.ViewHolder v,
                                            @NonNull RecyclerView.ViewHolder t) { return false; }

            @Override public void onSwiped(@NonNull RecyclerView.ViewHolder vh, int dir) {
                Movie m = movieAdapter.getMovieAt(vh.getAdapterPosition());
                viewModel.delete(m);
            }
        }).attachToRecyclerView(rv);

        /* 4. ViewModel */
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        // по умолчанию показываем все
        viewModel.getAll().observe(this, list -> movieAdapter.setMovieList(list));

        /* 5. Spinner-фильтр категорий */
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, android.view.View v,
                                                 int pos, long id) {
                switch (pos) {
                    case 0: // Все
                        viewModel.getAll()
                                .observe(MainActivity.this,
                                        list -> movieAdapter.setMovieList(list));
                        break;
                    case 1: // Хочу посмотреть  (status = 0)
                        viewModel.getByStatus(0)
                                .observe(MainActivity.this,
                                        list -> movieAdapter.setMovieList(list));
                        break;
                    case 2: // Просмотрено     (status = 1)
                        viewModel.getByStatus(1)
                                .observe(MainActivity.this,
                                        list -> movieAdapter.setMovieList(list));
                        break;
                }
            }
            @Override public void onNothingSelected(AdapterView<?> p) { }
        });

        /* 6. FloatingActionButton «+» */
        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v ->
                startActivity(new Intent(this, AddEditMovieActivity.class))
        );
    }

    /* ---------- Меню «лупа» ---------- */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // достаём action-view без cast’а
        MenuItem searchItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView sv =
                (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        sv.setQueryHint("Поиск по названию или жанру…");
        sv.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) { return false; }
            @Override public boolean onQueryTextChange(String query) {
                viewModel.search(query)
                        .observe(MainActivity.this,
                                movies -> movieAdapter.setMovieList(movies));
                return true;
            }
        });
        return true;
    }

    /* если нужны другие пункты меню – сюда */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
