package com.craft3r.JustAlbums;

import static com.craft3r.JustAlbums.LoadingActivity.IsEP;
import static com.craft3r.JustAlbums.LoadingActivity.Reacts;
import static com.craft3r.JustAlbums.LoadingActivity.artists;
import static com.craft3r.JustAlbums.LoadingActivity.counts;
import static com.craft3r.JustAlbums.LoadingActivity.duration;
import static com.craft3r.JustAlbums.LoadingActivity.ids;
import static com.craft3r.JustAlbums.LoadingActivity.linkNames;
import static com.craft3r.JustAlbums.LoadingActivity.links;
import static com.craft3r.JustAlbums.LoadingActivity.titles;
import static com.craft3r.JustAlbums.LoadingActivity.year;
import static com.craft3r.JustAlbums.LoadingActivity.images;
import static com.craft3r.JustAlbums.LoadingActivity.r1;
import static com.craft3r.JustAlbums.LoadingActivity.r2;
import static com.craft3r.JustAlbums.LoadingActivity.r3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class LoadAlbum extends AsyncTask<Void, Void, Void> {
    public Activity activity;
    public Context context;


    public LoadAlbum(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        Cursor cursor = LoadingActivity.db.readAllData();
        while (cursor.moveToNext()) {
            ids.add(cursor.getString(0));
            titles.add(cursor.getString(1));
            artists.add(cursor.getString(2));
            year.add(cursor.getInt(3));
            duration.add(cursor.getFloat(4));
            counts.add(cursor.getInt(5));
            links.add(cursor.getString(6));
            linkNames.add(cursor.getString(7));
            Reacts.add(cursor.getString(8));
            IsEP.add(cursor.getInt(9));
            Bitmap bmp = BitmapFactory.decodeByteArray(cursor.getBlob(10), 0,
                    cursor.getBlob(10).length);
            images.add(bmp);
            r1.add(cursor.getFloat(11));
            r2.add(cursor.getFloat(12));
            r3.add(cursor.getFloat(13));
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
