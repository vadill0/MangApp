package com.example.mangapp.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context){
        dbHelper = new DatabaseHelper(context);
    }

    public void open(){
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public long insertUser(String userId, String email){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FB_ID, userId);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        return database.insert(DatabaseHelper.TABLE_USERS, null, values);
    }

    public long insertReadManga(String mangaId, String userId){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MANGA_ID, mangaId);
        values.put(DatabaseHelper.COLUMN_FB_ID, userId);
        return database.insert(DatabaseHelper.TABLE_READ, null, values);
    }

    public long insertPendingManga(String mangaId, String userId){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MANGA_ID, mangaId);
        values.put(DatabaseHelper.COLUMN_FB_ID, userId);
        return database.insert(DatabaseHelper.TABLE_PENDING, null, values);
    }

    public long insertFavoriteManga(String mangaId, String userId){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MANGA_ID, mangaId);
        values.put(DatabaseHelper.COLUMN_FB_ID, userId);
        return database.insert(DatabaseHelper.TABLE_FAVORITES, null, values);
    }

    public long insertReadingManga(String mangaId, String userId){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MANGA_ID, mangaId);
        values.put(DatabaseHelper.COLUMN_FB_ID, userId);
        return database.insert(DatabaseHelper.TABLE_READING, null, values);
    }

    public Cursor getReadMangaFromUser(String userId){
     return database.query(DatabaseHelper.TABLE_READ, new String[]{DatabaseHelper.COLUMN_MANGA_ID}, DatabaseHelper.COLUMN_FB_ID + "=?", new String[]{userId}, null, null, null);
    }

    public Cursor getPendingMangaFromUser(String userId){
        return database.query(DatabaseHelper.TABLE_PENDING, new String[]{DatabaseHelper.COLUMN_MANGA_ID}, DatabaseHelper.COLUMN_FB_ID + "=?", new String[]{userId}, null, null, null);
    }

    public Cursor getFavoriteMangaFromUser(String userId){
        return database.query(DatabaseHelper.TABLE_FAVORITES, new String[]{DatabaseHelper.COLUMN_MANGA_ID}, DatabaseHelper.COLUMN_FB_ID + "=?", new String[]{userId}, null, null, null);
    }

    public Cursor getReadingMangaFromUser(String userId){
        return database.query(DatabaseHelper.TABLE_READING, new String[]{DatabaseHelper.COLUMN_MANGA_ID}, DatabaseHelper.COLUMN_FB_ID + "=?", new String[]{userId}, null, null, null);
    }

}
