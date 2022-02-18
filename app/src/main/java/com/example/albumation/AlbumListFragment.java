package com.example.albumation;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.solver.widgets.analyzer.WidgetGroup;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AlbumListFragment extends Fragment {
    View view;

    public LinearLayout lay;
    public NestedScrollView contCont;
    public TextView first;
    ArrayList<AlbumTupple> Albums;
    public DBHelper db;
    ArrayList<String> ids;
    public static ArrayList<String> titles, artists, Reacts;
    public static ArrayList<Bitmap> images;
    public static ArrayList<Float> r1, r2, r3, duration;
    public static ArrayList<Integer> year, IsEP;

    public Spinner deb;

    public class AlbumTupple<linearLayout, pos_as_default, title, artist, rating, duration, year> {
        public final LinearLayout linearLayout;
        public final int pos_as_default;
        public final String title;
        public final String artist;
        public final float rating;
        public final float duration;
        public final int year;


        public AlbumTupple(LinearLayout linearLayout, int pos_as_default, String title,
                           String artist, float rating, float duration, int year) {
            this.linearLayout = linearLayout;
            this.pos_as_default = pos_as_default;
            this.title = title;
            this.artist = artist;
            this.rating = rating;
            this.duration = duration;
            this.year = year;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_album_list, container, false);
        lay = view.findViewById(R.id.cont);
        first = (TextView) view.findViewById(R.id.first);
        contCont = (NestedScrollView) view.findViewById(R.id.contCont);
        deb = (Spinner) view.findViewById(R.id.spinner);
        initspinnerfooter();
        db = new DBHelper(view.getContext());
        ids = new ArrayList<String>();
        titles = new ArrayList<String>();
        artists = new ArrayList<String>();
        Reacts = new ArrayList<String>();
        IsEP = new ArrayList<Integer>();
        images = new ArrayList<Bitmap>();
        r1 = new ArrayList<Float>();
        r2 = new ArrayList<Float>();
        r3 = new ArrayList<Float>();
        duration = new ArrayList<Float>();
        year = new ArrayList<Integer>();

        Albums = new ArrayList<AlbumTupple>();


        UpdateViewContent();

        return view;
    }
    public static int dp(Context ct, int dp){
        final float scale = ct.getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }

    public void UpdateViewContent() {

        Cursor cursor = db.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(view.getContext(),
                    "You have not added any album yet, add your firs one!", Toast.LENGTH_LONG);
        } else {
            while (cursor.moveToNext()) {
                ids.add(cursor.getString(0));
                titles.add(cursor.getString(1));
                artists.add(cursor.getString(2));
                year.add(cursor.getInt(3));
                duration.add(cursor.getFloat(4));
                Reacts.add(cursor.getString(5));
                IsEP.add(cursor.getInt(6));
                Bitmap bmp = BitmapFactory.decodeByteArray(cursor.getBlob(7), 0,
                        cursor.getBlob(7).length);
                images.add(bmp);
                r1.add(cursor.getFloat(8));
                r2.add(cursor.getFloat(9));
                r3.add(cursor.getFloat(10));

            }
        }
        lay.removeAllViews();

        if (ids.size() == 0) {
            first.setVisibility(View.VISIBLE);
        } else {
            first.setVisibility(View.GONE);
        }


        if (Albums.size() == 0) {
            for (int i = 0; i < titles.size(); i++) {
                Album alb = new Album(titles.get(i), artists.get(i), Reacts.get(i), IsEP.get(i),
                        images.get(i), new float[]{r1.get(i), r2.get(i), r3.get(i)}, i + 1,
                        duration.get(i), year.get(i));

                alb.LoadAlbum(alb.id, alb.name, alb.artist, alb.react, alb.IsEP, alb.img,
                        alb.ratings, lay, view.getContext(), 9999 + i);

                LinearLayout example = view.findViewById(9999 + i);

                example.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), EditActivity.class);
                        intent.putExtra("Name", alb.name);
                        intent.putExtra("Artist", alb.artist);
                        intent.putExtra("Id", alb.id);
                        intent.putExtra("Rating1", String.valueOf(alb.ratings[0]));
                        intent.putExtra("Rating2", String.valueOf(alb.ratings[1]));
                        intent.putExtra("Rating3", String.valueOf(alb.ratings[2]));

                        intent.putExtra("Id", String.valueOf(alb.id));
                        getActivity().finish();
                        startActivity(intent);
                    }
                });

                // pos_as_default, title, artist, rating, duration, year
                float rt = (alb.ratings[0] + alb.ratings[1] + alb.ratings[2]) / 3;
                AlbumTupple temp = new AlbumTupple(example, i, alb.name, alb.artist, rt,
                        alb.duration, alb.year);
                Albums.add(temp);
            }

        }else{
            for(int i = 0; i < Albums.size(); i++){
                lay.addView(Albums.get(i).linearLayout);
            }
        }
    }
    private void initspinnerfooter() {
        ArrayList<String> Sort = ids;
        deb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String temp = parent.getItemAtPosition(position).toString();
                switch (temp){
                    case "time added":
                        System.out.println("TIME ADDED");

                        System.out.println(Albums.size() + "SIZE");
                        ArrayList<LinearLayout> tempArr = new ArrayList<LinearLayout>();
                        for (int i = 0; i < Albums.size(); i++){
                            //Albums.get(i).pos_as_default
                        }
                        UpdateViewContent();
                }
//                UpdateViewContent(Sort);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }
}