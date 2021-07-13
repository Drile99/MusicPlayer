package com.example.musicplayerproject.DB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.musicplayerproject.Model.SongsList;

import java.util.ArrayList;

public class FavoritesOperations {


    public static final String TAG = "Favorites Database";
    //kreiranje instance klasa
    SQLiteOpenHelper dbHandler;
    SQLiteDatabase database;
    //Kreiranje niza sa nazivima kolona
    private static final String[] allColumns = {
            FavoritesDBHandler.COLUMN_ID,
            FavoritesDBHandler.COLUMN_TITLE,
            FavoritesDBHandler.COLUMN_SUBTITLE,
            FavoritesDBHandler.COLUMN_PATH
    };

    //Konstruktor klase
    public FavoritesOperations(Context context) {
        dbHandler = new FavoritesDBHandler(context);
    }
    //Otvoranje (konektovanje) baze zbog upisa
    public void open() {
        Log.i(TAG, " Database Opened");
        database = dbHandler.getWritableDatabase();
    }
    //Zatvaranje baze
    public void close() {
        Log.i(TAG, "Database Closed");
        dbHandler.close();
    }
    //Dodavanje pesme u tabelu favorites
    public void addSongFav(SongsList songsList) {
        open();
        ContentValues values = new ContentValues();
        values.put(FavoritesDBHandler.COLUMN_TITLE, songsList.getTitle());
        values.put(FavoritesDBHandler.COLUMN_SUBTITLE, songsList.getSubTitle());
        values.put(FavoritesDBHandler.COLUMN_PATH, songsList.getPath());

        database.insertWithOnConflict(FavoritesDBHandler.TABLE_SONGS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        //zatvaranje baze
        close();
    }
    //Listanje svih pesama iz tabele favorites
    public ArrayList<SongsList> getAllFavorites() {
        //konektovanje sa bazom
        open();
        Cursor cursor = database.query(FavoritesDBHandler.TABLE_SONGS, allColumns,
                null, null, null, null, null);
        ArrayList<SongsList> favSongs = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SongsList songsList = new SongsList(cursor.getString(cursor.getColumnIndex(FavoritesDBHandler.COLUMN_TITLE))
                        , cursor.getString(cursor.getColumnIndex(FavoritesDBHandler.COLUMN_SUBTITLE))
                        , cursor.getString(cursor.getColumnIndex(FavoritesDBHandler.COLUMN_PATH)));
                favSongs.add(songsList);
            }
        }
        close();
        return favSongs;
    }

    //Brisanje omiljenih pesama
    public void removeSong(String songPath) {
        open();
        String whereClause =
                FavoritesDBHandler.COLUMN_PATH + "=?";
        String[] whereArgs = new String[]{songPath};

        database.delete(FavoritesDBHandler.TABLE_SONGS, whereClause, whereArgs);
        close();
    }

}
