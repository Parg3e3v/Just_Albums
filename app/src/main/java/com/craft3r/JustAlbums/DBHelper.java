package com.craft3r.JustAlbums;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_NAME = "JustAlbums.db";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "Albums";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "_name";
    private static final String COLUMN_ARTIST = "_artist";
    private static final String COLUMN_YEAR = "_year";
    private static final String COLUMN_DURATION = "_duration";
    private static final String COLUMN_TRCOUNT = "_trcount";
    private static final String COLUMN_LINK = "_link";
    private static final String COLUMN_LINKTEXT = "_linktext";
    private static final String COLUMN_REACT = "_react";
    private static final String COLUMN_ISEP = "_IsEP";
    private static final String COLUMN_IMAGE = "_img";
    private static final String COLUMN_RATING1 = "_rating1";
    private static final String COLUMN_RATING2 = "_rating2";
    private static final String COLUMN_RATING3 = "_rating3";




    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {



        String query =
                "CREATE TABLE " + DB_TABLE +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_ARTIST + " TEXT, " +
                        COLUMN_YEAR + " INTEGER, " +
                        COLUMN_DURATION + " REAL, " +
                        COLUMN_TRCOUNT + " INT, " +
                        COLUMN_LINK + " TEXT, " +
                        COLUMN_LINKTEXT + " TEXT, " +
                        COLUMN_REACT + " TEXT, " +
                        COLUMN_ISEP + " INTEGER, " +
                        COLUMN_IMAGE + " BLOB, " +
                        COLUMN_RATING1 + " REAL, " +
                        COLUMN_RATING2 + " REAL, " +
                        COLUMN_RATING3 + " REAL);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);
    }

    void AddAlbum(String title, String artist, String react, int IsEP,
                  int year, float dur, int tr_count, String link, String linkText, float rating1,
                  float rating2, float rating3, byte[] img){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, title);
        cv.put(COLUMN_ARTIST, artist);
        cv.put(COLUMN_YEAR, year);
        cv.put(COLUMN_DURATION, dur);
        cv.put(COLUMN_TRCOUNT, tr_count);
        cv.put(COLUMN_LINK, link);
        cv.put(COLUMN_LINKTEXT, linkText);
        cv.put(COLUMN_REACT, react);
        cv.put(COLUMN_ISEP, IsEP);
        cv.put(COLUMN_RATING1, rating1);
        cv.put(COLUMN_RATING2, rating2);
        cv.put(COLUMN_RATING3, rating3);
        cv.put(COLUMN_IMAGE, img);

        long result = db.insert(DB_TABLE, null, cv);
        if (result == -1){
            Toast.makeText(context, "Failed to add album", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context, "Added Successfully", Toast.LENGTH_LONG).show();

        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + DB_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void UpdateData(String row_id,String title, String artist, String react, int IsEP,
                           int year, float dur, int tr_count, String link, String linkText,
                           float rating1, float rating2, float rating3, byte[] img){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, title);
        cv.put(COLUMN_ARTIST, artist);
        cv.put(COLUMN_YEAR, year);
        cv.put(COLUMN_DURATION, dur);
        cv.put(COLUMN_TRCOUNT, tr_count);
        cv.put(COLUMN_LINK, link);
        cv.put(COLUMN_LINKTEXT, linkText);
        cv.put(COLUMN_REACT, react);
        cv.put(COLUMN_ISEP, IsEP);
        cv.put(COLUMN_RATING1, rating1);
        cv.put(COLUMN_RATING2, rating2);
        cv.put(COLUMN_RATING3, rating3);
        cv.put(COLUMN_IMAGE, img);

        long result = db.update(DB_TABLE, cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to update", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
