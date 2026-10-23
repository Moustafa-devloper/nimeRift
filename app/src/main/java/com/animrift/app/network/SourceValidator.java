package com.animrift.app.repository;

import com.animrift.app.model.Anime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnimeRepository implements AnimeDataSource {

    @Override
    public List<Anime> getCatalog() {
        List<Anime> list = new ArrayList<>();

        list.add(new Anime(
                "anime_01",
                "Shadow Blade",
                "Action",
                "A rogue swordsman fights through a city ruled by ancient spirits and hidden clans.",
                "",
                "https://example.com/source/shadow-blade",
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
                12
        ));

        list.add(new Anime(
                "anime_02",
                "Moonlit Garden",
                "Drama",
                "A gentle librarian uncovers a secret branch of the royal archive and a forgotten love story.",
                "",
                "https://example.com/source/moonlit-garden",
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                10
        ));

        list.add(new Anime(
                "anime_03",
                "Skybound Run",
                "Adventure",
                "Three students cross floating islands to recover the last surviving map of the sky sea.",
                "",
                "https://example.com/source/skybound-run",
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                14
        ));

        list.add(new Anime(
                "anime_04",
                "Echo Café",
                "Comedy",
                "A small coffee shop becomes a meeting point for dreamers, musicians, and stubborn neighbors.",
                "",
                "https://example.com/source/echo-cafe",
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                8
        ));

        list.add(new Anime(
                "anime_05",
                "Crimson Magic",
                "Fantasy",
                "An apprentice mage learns that the kingdom's magic is feeding on a forgotten dimension.",
                "",
                "https://example.com/source/crimson-magic",
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                24
        ));

        return Collections.unmodifiableList(list);
    }
}
