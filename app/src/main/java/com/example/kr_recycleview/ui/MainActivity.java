package com.example.kr_recycleview.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kr_recycleview.R;
import com.example.kr_recycleview.data.Movie;
import com.example.kr_recycleview.ui.AddEditMovieActivity;
import com.example.kr_recycleview.viewmodel.MovieViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/** Главный экран каталога фильмов */
public class MainActivity extends AppCompatActivity {

    private MovieViewModel viewModel;
    private MovieAdapter movieAdapter;

    private Spinner spinnerCategory, spinnerQualityFilter;

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
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView rv,
                                  @NonNull RecyclerView.ViewHolder vh,
                                  @NonNull RecyclerView.ViewHolder t) { return false; }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder vh, int dir) {
                Movie m = movieAdapter.getMovieAt(vh.getAdapterPosition());
                viewModel.delete(m);
            }
        }).attachToRecyclerView(rv);

        /* 4. ViewModel */
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.getAll().observe(this, list -> movieAdapter.setMovieList(list));

        /* 5. Spinner-фильтры */
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerQualityFilter = findViewById(R.id.spinnerQualityFilter);

        // Адаптер для статуса (All / Хочу / Просмотрено / Заброшено)
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.filter_categories,
                R.layout.spinner_item
        );
        statusAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerCategory.setAdapter(statusAdapter);

        // Адаптер для качества (Все / Не очень / Средне / Лучшее)
        ArrayAdapter<CharSequence> qualityAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.movie_quality_tags,
                R.layout.spinner_item
        );
        qualityAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerQualityFilter.setAdapter(qualityAdapter);

        // Общий слушатель фильтрации
        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                int statusPos  = spinnerCategory.getSelectedItemPosition() - 1;
                int qualityPos = spinnerQualityFilter.getSelectedItemPosition() - 1;

                if (statusPos >= 0 && qualityPos >= 0) {
                    viewModel.getByStatusAndQuality(statusPos, qualityPos)
                            .observe(MainActivity.this, movies -> movieAdapter.setMovieList(movies));
                } else if (statusPos >= 0) {
                    viewModel.getByStatus(statusPos)
                            .observe(MainActivity.this, movies -> movieAdapter.setMovieList(movies));
                } else if (qualityPos >= 0) {
                    viewModel.getByQuality(qualityPos)
                            .observe(MainActivity.this, movies -> movieAdapter.setMovieList(movies));
                } else {
                    viewModel.getAll()
                            .observe(MainActivity.this, movies -> movieAdapter.setMovieList(movies));
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        };

        /* привязываем слушатели */
        spinnerCategory.setOnItemSelectedListener(filterListener);
        spinnerQualityFilter.setOnItemSelectedListener(filterListener);
        // Инициализация списка при старте (необязательно)
        filterListener.onItemSelected(spinnerCategory, null,
                spinnerCategory.getSelectedItemPosition(), 0);

        /* 6. FloatingActionButton "+" */
        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v ->
                startActivity(new Intent(this, AddEditMovieActivity.class))
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView sv = (SearchView) searchItem.getActionView();
        sv.setQueryHint("Поиск по названию или жанру…");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) { return false; }
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
        return super.onOptionsItemSelected(item);
    }
}
