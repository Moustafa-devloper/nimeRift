package com.animrift.app.model;

public class Anime {
    private final String id;
    private final String title;
    private final String genre;
    private final String description;
    private final String coverUrl;
    private final String sourceUrl;
    private final String sampleVideoUrl;
    private final int episodeCount;

    public Anime(String id, String title, String genre, String description,
                 String coverUrl, String sourceUrl, String sampleVideoUrl, int episodeCount) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.description = description;
        this.coverUrl = coverUrl;
        this.sourceUrl = sourceUrl;
        this.sampleVideoUrl = sampleVideoUrl;
        this.episodeCount = episodeCount;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getSampleVideoUrl() {
        return sampleVideoUrl;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }
}
