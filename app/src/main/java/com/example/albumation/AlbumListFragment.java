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
import java.util.Arrays;

public class AlbumListFragment extends Fragment {
    View view;

    public LinearLayout lay;
    public NestedScrollView contCont;
    public TextView first;
    ArrayList<AlbumTupple> Albums;
    public DBHelper db;
    public static ArrayList<String> ids;
//    ArrayList<LinearLayout> ALbIds;
    public static ArrayList<String> titles, artists, Reacts;
    public static ArrayList<Bitmap> images;
    public static ArrayList<Float> r1, r2, r3, duration;
    public static ArrayList<Integer> year, IsEP;

    public Spinner deb;

    public class AlbumTupple<linearLayout, pos_as_default, title, artist, rating, duration, year> {
        public  LinearLayout linearLayout;
        public int pos_as_default;
        public String title;
        public String artist;
        public float rating;
        public float duration;
        public int year;


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

        public void Update(AlbumTupple tp){
            this.title = tp.title;
            this.pos_as_default = tp.pos_as_default;
            this.title = tp.title;
            this.artist = tp.artist;
            this.rating = tp.rating;
            this.duration = tp.duration;
            this.year = tp.year;
            this.linearLayout = tp.linearLayout;
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
//        ALbIds = new ArrayList<LinearLayout>();

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
                    "You have not added any album yet, add your firs one!", Toast.LENGTH_LONG).show();
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


//        if (Albums.size() == 0) {
            for (int i = 0; i < ids.size(); i++) {
                Album alb = new Album(titles.get(i), artists.get(i), Reacts.get(i), IsEP.get(i),
                        images.get(i), new float[]{r1.get(i), r2.get(i), r3.get(i)}, i+1,
                        duration.get(i), year.get(i));

                int tempid = 9999 + i;

                alb.LoadAlbum(alb.id, alb.name, alb.artist, alb.react, alb.IsEP, alb.img,
                        alb.ratings, lay, view.getContext(), tempid);

                LinearLayout example = view.findViewById(tempid);

                float rating = (alb.ratings[0] + alb.ratings[1] + alb.ratings[2]) / 3;

                AlbumTupple temp = new AlbumTupple(example, i, alb.name, alb.artist, rating,
                        alb.duration, alb.year);

                Albums.add(temp);

                initspinnerfooter();


                example.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), EditActivity.class);
                        intent.putExtra("id", String.valueOf(alb.id));
//                        intent.putExtra("Rating1", alb.ratings);
                        getActivity().finish();
                        startActivity(intent);
                    }
                });
            }

    }
    private void initspinnerfooter() {
        deb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String temp = parent.getItemAtPosition(position).toString();
                switch (temp){
                    case "time added":
                        System.out.println("TIME ADDED");
//                        lay.setVisibility(View.GONE);
                        lay.removeAllViews();
                        for(int i = 0; i < Albums.size(); i++){
                            lay.addView(Albums.get(i).linearLayout);
                        }
                        break;
                    case "title":
                        Toast.makeText(view.getContext(), "TITLE", Toast.LENGTH_LONG).show();
                        lay.removeAllViews();

                        /*

                        String tempp;
                        String[] alb = {"A", "C", "V", "B", "T"};
                        for(int i=0;i<alb.length;i++){
                            for(int j=i+1;j<alb.length;j++){
                                if(alb[i].compareToIgnoreCase(alb[j])>0){
                                    tempp = alb[i];
                                    alb[i] = alb[j];
                                    alb[j] = tempp;
                                }
                            }

                        }
                        for(int i = 0; i < alb.length; i++)
                            System.out.println(alb[i]);
                            */

                        AlbumTupple tempp;
                        for(int i=0;i<Albums.size();i++){
                            for(int j=i+1;j<Albums.size();j++){
                                if(Albums.get(i).title.compareToIgnoreCase(Albums.get(j).title)>0){
                                    tempp = Albums.get(i);
                                    Albums.get(i).Update(Albums.get(j));
                                    Albums.get(j).Update(tempp);
                                }
                            }

                        }
                        for (int a = 0 ; a < Albums.size(); a++) {
//                            if(Albums.get(i).linearLayout.getParent() != null) {
//                                ((ViewGroup)Albums.get(i).linearLayout.getParent())
//                                        .removeView(Albums.get(i).linearLayout); // <- fix
//                            }
//                            lay.addView(Albums.get(i).linearLayout);
                            System.out.println(Albums.get(a).title + " AHA");
                            System.out.println(Albums.get(a).artist);
                        }

                        System.out.println(Albums.get(0).title + " AHA");
                        System.out.println(Albums.get(0).artist);
                        System.out.println(Albums.size());
                        break;

                    default:

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