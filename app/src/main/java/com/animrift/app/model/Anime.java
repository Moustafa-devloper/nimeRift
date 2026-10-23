package com.animrift.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.animrift.app.model.Anime;
import com.animrift.app.player.VideoPlayerActivity;
import com.animrift.app.repository.AnimeRepository;
import com.animrift.app.storage.FavoritesHistoryStore;
import com.animrift.app.ui.AnimeAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText searchInput;
    private Spinner genreSpinner;
    private Button searchButton;
    private Button favoritesButton;
    private Button clearHistoryButton;
    private ListView animeListView;
    private TextView statusTextView;

    private AnimeRepository repository;
    private FavoritesHistoryStore store;
    private List<Anime> allAnime;
    private List<Anime> filteredAnime;
    private AnimeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = new AnimeRepository();
        store = new FavoritesHistoryStore(this);

        searchInput = findViewById(R.id.searchInput);
        genreSpinner = findViewById(R.id.genreSpinner);
        searchButton = findViewById(R.id.searchButton);
        favoritesButton = findViewById(R.id.favoritesButton);
        clearHistoryButton = findViewById(R.id.clearHistoryButton);
        animeListView = findViewById(R.id.animeListView);
        statusTextView = findViewById(R.id.statusText);

        allAnime = repository.getCatalog();
        filteredAnime = new ArrayList<>(allAnime);

        ArrayAdapter<CharSequence> genreAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.genre_array,
                android.R.layout.simple_spinner_item
        );
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(genreAdapter);

        adapter = new AnimeAdapter(this, filteredAnime, store);
        animeListView.setAdapter(adapter);

        searchButton.setOnClickListener(v -> applyFilter());
        favoritesButton.setOnClickListener(v -> showFavoritesOnly());
        clearHistoryButton.setOnClickListener(v -> {
            store.clearHistory();
            statusTextView.setText(R.string.history_cleared);
        });

        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        animeListView.setOnItemClickListener((parent, view, position, id) -> {
            Anime anime = adapter.getItem(position);
            if (anime != null) {
                openPlayer(anime);
            }
        });

        updateStatus();
    }

    private void applyFilter() {
        String query = searchInput.getText() == null ? "" : searchInput.getText().toString().trim();
        String selectedGenre = genreSpinner.getSelectedItem() == null
                ? getString(R.string.genre_all)
                : genreSpinner.getSelectedItem().toString();

        filteredAnime.clear();
        for (Anime anime : allAnime) {
            boolean matchesQuery = TextUtils.isEmpty(query)
                    || anime.getTitle().toLowerCase().contains(query.toLowerCase());
            boolean matchesGenre = getString(R.string.genre_all).equals(selectedGenre)
                    || selectedGenre.equals(anime.getGenre());

            if (matchesQuery && matchesGenre) {
                filteredAnime.add(anime);
            }
        }

        adapter.notifyDataSetChanged();
        updateStatus();
    }

    private void showFavoritesOnly() {
        String selectedGenre = genreSpinner.getSelectedItem() == null
                ? getString(R.string.genre_all)
                : genreSpinner.getSelectedItem().toString();

        filteredAnime.clear();
        for (Anime anime : allAnime) {
            boolean isFavorite = store.isFavorite(anime.getId());
            boolean matchesGenre = getString(R.string.genre_all).equals(selectedGenre)
                    || selectedGenre.equals(anime.getGenre());

            if (isFavorite && matchesGenre) {
                filteredAnime.add(anime);
            }
        }

        adapter.notifyDataSetChanged();
        updateStatus();
    }

    private void updateStatus() {
        if (filteredAnime.isEmpty()) {
            statusTextView.setText(R.string.empty_state);
            return;
        }

        statusTextView.setText(getString(R.string.catalog_status, filteredAnime.size()));
    }

    public void openPlayer(Anime anime) {
        if (anime == null) {
            return;
        }

        store.addHistory(anime, 0L);

        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtra(VideoPlayerActivity.EXTRA_ANIME_TITLE, anime.getTitle());
        intent.putExtra(VideoPlayerActivity.EXTRA_ANIME_GENRE, anime.getGenre());
        intent.putExtra(VideoPlayerActivity.EXTRA_VIDEO_URL, anime.getSampleVideoUrl());
        intent.putExtra(VideoPlayerActivity.EXTRA_SOURCE_URL, anime.getSourceUrl());
        startActivity(intent);
    }
}
