package com.example.mangapp.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "mangApp.db";
    private static final int DB_VERSION = 1;

    //Tablas
    public static final String TABLE_USERS = "users";
    public static final String TABLE_READ = "read";
    public static final String TABLE_PENDING = "pending";
    public static final String TABLE_FAVORITES = "favorites";
    public static final String TABLE_READING = "reading";

    //Columnas comunes
    public static final String COLUMN_FB_ID = "fb_id";
    public static final String COLUMN_MANGA_ID = "manga_id";

    //Columnas users
    public static final String COLUMN_EMAIL = "email";

    //Sentencias para las tablas
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " (" +
            COLUMN_FB_ID + " TEXT PRIMARY KEY, " +
            COLUMN_EMAIL + " TEXT)";
    private static final String CREATE_TABLE_READ = "CREATE TABLE " + TABLE_READ + " (" +
            COLUMN_MANGA_ID + " TEXT PRIMARY KEY, " +
            COLUMN_FB_ID + " TEXT, FOREIGN KEY(" + COLUMN_FB_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_FB_ID + "))";
    private static final String CREATE_TABLE_PENDING = "CREATE TABLE " + TABLE_PENDING + " (" +
            COLUMN_MANGA_ID + " TEXT PRIMARY KEY, " +
            COLUMN_FB_ID + " TEXT, FOREIGN KEY(" + COLUMN_FB_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_FB_ID + "))";
    private static final String CREATE_TABLE_FAVORITES = "CREATE TABLE " + TABLE_FAVORITES + " (" +
            COLUMN_MANGA_ID + " TEXT PRIMARY KEY, " +
            COLUMN_FB_ID + " TEXT, FOREIGN KEY(" + COLUMN_FB_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_FB_ID + "))";
    private static final String CREATE_TABLE_READING = "CREATE TABLE " + TABLE_READING + " (" +
            COLUMN_MANGA_ID + " TEXT PRIMARY KEY, " +
            COLUMN_FB_ID + " TEXT, FOREIGN KEY(" + COLUMN_FB_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_FB_ID + "))";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_READ);
        db.execSQL(CREATE_TABLE_PENDING);
        db.execSQL(CREATE_TABLE_FAVORITES);
        db.execSQL(CREATE_TABLE_READING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_READ);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENDING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_READING);

        onCreate(db);
    }
}
