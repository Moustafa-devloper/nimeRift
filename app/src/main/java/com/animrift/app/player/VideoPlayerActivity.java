package com.animrift.app.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.animrift.app.MainActivity;
import com.animrift.app.R;
import com.animrift.app.model.Anime;
import com.animrift.app.storage.FavoritesHistoryStore;

import java.util.List;

public class AnimeAdapter extends BaseAdapter {

    private final Context context;
    private final List<Anime> animeList;
    private final FavoritesHistoryStore store;

    public AnimeAdapter(Context context, List<Anime> animeList, FavoritesHistoryStore store) {
        this.context = context;
        this.animeList = animeList;
        this.store = store;
    }

    @Override
    public int getCount() {
        return animeList.size();
    }

    @Override
    public Anime getItem(int position) {
        return animeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_anime, parent, false);
            holder = new ViewHolder();
            holder.titleText = convertView.findViewById(R.id.titleText);
            holder.genreText = convertView.findViewById(R.id.genreText);
            holder.descriptionText = convertView.findViewById(R.id.descriptionText);
            holder.favoriteButton = convertView.findViewById(R.id.favoriteButton);
            holder.playButton = convertView.findViewById(R.id.playButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Anime anime = getItem(position);
        holder.titleText.setText(anime.getTitle());
        holder.genreText.setText(anime.getGenre());
        holder.descriptionText.setText(anime.getDescription());

        boolean favorite = store.isFavorite(anime.getId());
        holder.favoriteButton.setText(favorite ? "♥" : "♡");

        holder.favoriteButton.setOnClickListener(v -> {
            boolean nowFavorite = store.toggleFavorite(anime.getId());
            holder.favoriteButton.setText(nowFavorite ? "♥" : "♡");
        });

        holder.playButton.setOnClickListener(v -> {
            if (context instanceof MainActivity) {
                ((MainActivity) context).openPlayer(anime);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView titleText;
        TextView genreText;
        TextView descriptionText;
        Button favoriteButton;
        Button playButton;
    }
}
