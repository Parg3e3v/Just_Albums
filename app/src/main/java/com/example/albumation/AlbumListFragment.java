package com.example.albumation;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class AlbumListFragment extends Fragment {
    View view;

    int rollerDrUp, rollerDrDown, settingsDr;

    public static final int tempid = 9999;

    public LinearLayout lay;
    public ImageButton roller, stBut;
    boolean IsDown = true;
    public NestedScrollView contCont;
    public TextView first;
    ArrayList<AlbumTupple> Albums;
    public DBHelper db;
    public static ArrayList<String> ids;
    public static ArrayList<String> titles, artists, Reacts;
    public static ArrayList<Bitmap> images;
    public static ArrayList<Float> r1, r2, r3, duration;
    public static ArrayList<Integer> year, IsEP;

    public Spinner deb;

    public class AlbumTupple<linearLayout, pos_as_default, title, artist, rating, duration, year> {
        public  LinearLayout linearLayout;
        public int pos_as_default, year;
        public String title, artist;
        public float rating, duration;
        public TextView id;


        public AlbumTupple(LinearLayout linearLayout, int pos_as_default, String title,
                           String artist, float rating, float duration, int year, TextView id) {
            this.linearLayout = linearLayout;
            this.pos_as_default = pos_as_default;
            this.title = title;
            this.artist = artist;
            this.rating = rating;
            this.duration = duration;
            this.year = year;
            this.id = id;
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
        roller = (ImageButton) view.findViewById(R.id.roller);
        stBut = (ImageButton) view.findViewById(R.id.settings);

        Albums = new ArrayList<AlbumTupple>();


        // -------------------------------------------------------------------------
        int nightModeFlags =
                getContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                rollerDrDown = R.drawable.down_white;
                rollerDrUp = R.drawable.up_white;
                settingsDr = R.drawable.settings_white;
                break;

            case Configuration.UI_MODE_NIGHT_NO:

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                rollerDrDown = R.drawable.down;
                rollerDrUp = R.drawable.up;
                settingsDr = R.drawable.settings;
                break;
        }
        roller.setBackgroundResource(IsDown ? rollerDrDown: rollerDrUp);
        stBut.setBackgroundResource(settingsDr);
        // ------------------------------------------------------------------------
        roller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsDown = !IsDown;
                roller.setBackgroundResource(IsDown ? rollerDrDown: rollerDrUp);
                lay.removeAllViews();
                Collections.reverse(Albums);
                for (int i = 0 ; i < Albums.size(); i++) {
                    lay.addView(Albums.get(i).linearLayout);
                    Albums.get(i).id.setText(String.valueOf(i+1));
                }
            }
        });

        stBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });

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

        lay.removeAllViews();

        if (ids.size() == 0) {
            first.setVisibility(View.VISIBLE);
        } else {
            first.setVisibility(View.GONE);
        }

            for (int i = 0; i < ids.size(); i++) {
                Album alb = new Album(titles.get(i), artists.get(i), Reacts.get(i), IsEP.get(i),
                        images.get(i), new float[]{r1.get(i), r2.get(i), r3.get(i)}, i+1,
                        duration.get(i), year.get(i));

                int tempid_ = tempid + i;

                alb.LoadAlbum(alb.id, alb.name, alb.artist, alb.react, alb.IsEP, alb.img,
                        alb.ratings, lay, view.getContext(), tempid_);

                LinearLayout example = view.findViewById(tempid_);

                float rating = (alb.ratings[0] + alb.ratings[1] + alb.ratings[2]) / 3;

                AlbumTupple temp = new AlbumTupple(example, i, alb.name, alb.artist, rating,
                        alb.duration, alb.year, (TextView) view.findViewById(tempid_ + tempid));

                Albums.add(temp);

                initspinnerfooter();


                example.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), EditActivity.class);
                        intent.putExtra("id", String.valueOf(alb.id));
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
                lay.removeAllViews();
                switch (temp){
                    case "time added":
                        Collections.sort(Albums, new Comparator<AlbumTupple>() {
                            @Override
                            public int compare(AlbumTupple albumTupple, AlbumTupple t1) {
                                if(albumTupple.pos_as_default > t1.pos_as_default)
                                    return 1;
                                else if (albumTupple.pos_as_default < t1.pos_as_default)
                                    return -1;
                                else
                                    return 0;
                            }
                        });
                        break;
                    case "title":
                        Collections.sort(Albums, new Comparator<AlbumTupple>() {
                            @Override
                            public int compare(AlbumTupple albumTupple, AlbumTupple t1) {
                                if(albumTupple.title.compareToIgnoreCase(t1.title) > 0)
                                    return 1;
                                else if (albumTupple.title.compareToIgnoreCase(t1.title) < 0)
                                    return -1;
                                else
                                    return 0;
                            }
                        });
                        break;
                    case "artist name":
                        Collections.sort(Albums, new Comparator<AlbumTupple>() {
                            @Override
                            public int compare(AlbumTupple albumTupple, AlbumTupple t1) {
                                if(albumTupple.artist.compareToIgnoreCase(t1.artist) > 0)
                                    return 1;
                                else if (albumTupple.artist.compareToIgnoreCase(t1.artist) < 0)
                                    return -1;
                                else
                                    return 0;
                            }
                        });
                        break;
                    case "rating":
                        Collections.sort(Albums, new Comparator<AlbumTupple>() {
                            @Override
                            public int compare(AlbumTupple albumTupple, AlbumTupple t1) {
                                if(albumTupple.rating < t1.rating)
                                    return 1;
                                else if (albumTupple.rating > t1.rating)
                                    return -1;
                                else
                                    return 0;
                            }
                        });
                        break;
                    case "duration":
                        Collections.sort(Albums, new Comparator<AlbumTupple>() {
                            @Override
                            public int compare(AlbumTupple albumTupple, AlbumTupple t1) {
                                if(albumTupple.duration < t1.duration)
                                    return 1;
                                else if (albumTupple.duration > t1.duration)
                                    return -1;
                                else
                                    return 0;
                            }
                        });
                        break;
                    case "year":
                        Collections.sort(Albums, new Comparator<AlbumTupple>() {
                            @Override
                            public int compare(AlbumTupple albumTupple, AlbumTupple t1) {
                                if(albumTupple.year < t1.year)
                                    return 1;
                                else if (albumTupple.year > t1.year)
                                    return -1;
                                else
                                    return 0;
                            }
                        });
                        break;
                    default:

                    }

                for (int i = 0 ; i < Albums.size(); i++) {
                    lay.addView(Albums.get(i).linearLayout);
                    Albums.get(i).id.setText(String.valueOf(i+1));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }
}