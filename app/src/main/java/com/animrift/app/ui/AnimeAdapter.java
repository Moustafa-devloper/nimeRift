package com.animrift.app.storage;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.animrift.app.model.Anime;

import java.util.ArrayList;
import java.util.List;

public class FavoritesHistoryStore {

    private static final String PREFS_NAME = "animrift_prefs";
    private static final String KEY_PLAYBACK_POSITION = "last_position_ms";

    private final Context context;
    private final SharedPreferences prefs;
    private final DbHelper dbHelper;

    public FavoritesHistoryStore(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = this.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.dbHelper = new DbHelper(this.context);
    }

    public boolean toggleFavorite(String animeId) {
        if (isFavorite(animeId)) {
            removeFavorite(animeId);
            return false;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("anime_id", animeId);
        values.put("updated_at", System.currentTimeMillis());
        db.insertWithOnConflict("favorites", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    public boolean isFavorite(String animeId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "favorites",
                new String[]{"anime_id"},
                "anime_id=?",
                new String[]{animeId},
                null, null, null
        );

        boolean exists = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }

    public void removeFavorite(String animeId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("favorites", "anime_id=?", new String[]{animeId});
    }

    public void clearHistory() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("history", null, null);
    }

    public void addHistory(Anime anime, long lastPositionMs) {
        if (anime == null) {
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("anime_id", anime.getId());
        values.put("title", anime.getTitle());
        values.put("genre", anime.getGenre());
        values.put("last_position_ms", lastPositionMs);
        values.put("updated_at", System.currentTimeMillis());
        db.insertWithOnConflict("history", null, values, SQLiteDatabase.CONFLICT_REPLACE);

        prefs.edit().putLong(KEY_PLAYBACK_POSITION, lastPositionMs).apply();
    }

    public List<Anime> getHistory() {
        List<Anime> result = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "history",
                new String[]{"anime_id", "title", "genre"},
                null, null, null, null,
                "updated_at DESC"
        );

        if (cursor == null) {
            return result;
        }

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("anime_id"));
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String genre = cursor.getString(cursor.getColumnIndexOrThrow("genre"));
            result.add(new Anime(id, title, genre, "", "", "", "", 0));
        }
        cursor.close();
        return result;
    }

    public long getLastPlaybackPositionMs() {
        return prefs.getLong(KEY_PLAYBACK_POSITION, 0L);
    }

    private static class DbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "animrift.db";
        private static final int DATABASE_VERSION = 1;

        DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE favorites (anime_id TEXT PRIMARY KEY, updated_at INTEGER NOT NULL);");
            db.execSQL("CREATE TABLE history (anime_id TEXT PRIMARY KEY, title TEXT, genre TEXT, last_position_ms INTEGER DEFAULT 0, updated_at INTEGER NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS favorites");
            db.execSQL("DROP TABLE IF EXISTS history");
            onCreate(db);
        }
    }
}
