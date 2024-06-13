package com.example.mangapp.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    public long insertManga(String TABLE_NAME, String userId, String mangaId){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FB_ID, userId);
        values.put(DatabaseHelper.COLUMN_MANGA_ID, mangaId);
        long result = -1;
        try {
            result = database.insert(TABLE_NAME, null, values);
            Log.d("DatabaseManager", "Insert result: " + result);
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error inserting manga", e);
        }
        return result;
    }
    public boolean deleteMangaFromList(String TABLE_NAME, String userId, String mangaId) {
        // Define the where clause and where arguments
        String whereClause = DatabaseHelper.COLUMN_FB_ID + "=? AND " + DatabaseHelper.COLUMN_MANGA_ID + "=?";
        String[] whereArgs = new String[]{userId, mangaId};

        // Execute the delete command
        int rowsDeleted = database.delete(TABLE_NAME, whereClause, whereArgs);

        // Return true if at least one row was deleted
        return rowsDeleted > 0;
    }


    public Cursor getMangaFromUser(String TABLE_NAME, String userId){
     return database.query(TABLE_NAME, new String[]{DatabaseHelper.COLUMN_MANGA_ID}, DatabaseHelper.COLUMN_FB_ID + "=?", new String[]{userId}, null, null, null);
    }

    public Cursor getUser(String userId) {
        return database.query(DatabaseHelper.TABLE_USERS, new String[]{DatabaseHelper.COLUMN_FB_ID}, DatabaseHelper.COLUMN_FB_ID + "=?", new String[]{userId}, null, null, null);
    }

    public boolean userExists(String userId) {
        Cursor cursor = null;
        try {

            cursor = database.query(DatabaseHelper.TABLE_USERS, new String[]{DatabaseHelper.COLUMN_FB_ID}, DatabaseHelper.COLUMN_FB_ID + "=?", new String[]{userId}, null, null, null);

            return cursor != null && cursor.getCount() > 0;
        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public boolean isMangaInList(String TABLE_NAME, String userId, String mangaId) {
        Cursor cursor = null;
        try {

            cursor = database.query(
                    TABLE_NAME,
                    new String[]{DatabaseHelper.COLUMN_MANGA_ID},
                    DatabaseHelper.COLUMN_FB_ID + "=? AND " + DatabaseHelper.COLUMN_MANGA_ID + "=?",
                    new String[]{userId, mangaId},
                    null,
                    null,
                    null
            );

            return cursor != null && cursor.getCount() > 0;
        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
