package com.animrift.app.repository;

import com.animrift.app.model.Anime;

import java.util.List;

public interface AnimeDataSource {
    List<Anime> getCatalog();
}
