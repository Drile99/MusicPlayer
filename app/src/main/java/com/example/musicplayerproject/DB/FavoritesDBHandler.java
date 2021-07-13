package com.example.musicplayerproject.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritesDBHandler extends SQLiteOpenHelper {

    //Postavljanje imena baze/Konfiguracija baze
    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;
    //inicijalizacija tabela i kolona iz baze
    public static final String TABLE_SONGS           = "favorites";
    public static final String COLUMN_ID             = "songID";
    public static final String COLUMN_TITLE          = "title";
    public static final String COLUMN_SUBTITLE       = "subtitle";
    public static final String COLUMN_PATH           = "songpath";

    //Query za kreiranje tabeele
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_SONGS + " (" + COLUMN_ID
            + " INTEGER, " + COLUMN_TITLE + " TEXT, " + COLUMN_SUBTITLE
            + " TEXT, " + COLUMN_PATH + " TEXT PRIMARY KEY " + ")";
    //Konstruktor
    public FavoritesDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //Sadrzi metodu za kreiranje baze
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);

    }
    //Ukoliko postoji baza ona se brise i ponovo se kreira
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);
        db.execSQL(TABLE_CREATE);
    }
}
