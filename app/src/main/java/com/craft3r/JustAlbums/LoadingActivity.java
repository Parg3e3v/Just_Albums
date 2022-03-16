package com.craft3r.JustAlbums;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class LoadingActivity extends AppCompatActivity {
    public static DBHelper db;
    public static ArrayList<String> ids;
    public static ArrayList<String> titles, artists, Reacts, links, linkNames;
    public static ArrayList<Bitmap> images;
    public static ArrayList<Float> r1, r2, r3, duration;
    public static ArrayList<Integer> year, IsEP, counts;

    ImageView j, a, a_layer, back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        db = new DBHelper(getApplicationContext());
        ids = new ArrayList<String>();
        titles = new ArrayList<String>();
        artists = new ArrayList<String>();
        Reacts = new ArrayList<String>();
        links = new ArrayList<String>();
        linkNames = new ArrayList<String>();
        IsEP = new ArrayList<Integer>();
        images = new ArrayList<Bitmap>();
        r1 = new ArrayList<Float>();
        r2 = new ArrayList<Float>();
        r3 = new ArrayList<Float>();
        duration = new ArrayList<Float>();
        year = new ArrayList<Integer>();
        counts = new ArrayList<Integer>();
//        j = (ImageView) findViewById(R.id.j_imgview);
//        a = (ImageView) findViewById(R.id.aa_imgview);
//        a_layer = (ImageView) findViewById(R.id.aa_layer_imgview);
//        back = (ImageView) findViewById(R.id.back);

        try {
            new LoadAlbum(LoadingActivity.this, getApplicationContext()).execute();
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    new LoadAlbum(LoadingActivity.this, getApplicationContext()).execute();
//                }
//            }, 2000);
        }catch (Exception e){
            Log.e("tag", e.getMessage());
            Toast.makeText(getApplicationContext(), "Can't load database", Toast.LENGTH_SHORT).show();
            ids.removeAll(ids);
            Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

//        Animation anim = AnimationUtils.loadAnimation(this, R.anim.j);
//        anim.setDuration(1000);
//        anim.setRepeatCount(1);
//        anim.setFillBefore(true);
//        anim.setFillAfter(true);
//        j.startAnimation(anim);
//
//        Animation anim_a = AnimationUtils.loadAnimation(this, R.anim.a);
//        anim_a.setDuration(1000);
//        anim_a.setRepeatCount(1);
//        anim_a.setFillBefore(true);
//        anim_a.setFillAfter(true);
//        a.startAnimation(anim_a);
//        a_layer.startAnimation(anim_a);
    }
}