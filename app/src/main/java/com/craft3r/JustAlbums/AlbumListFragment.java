package com.craft3r.JustAlbums;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class AlbumListFragment extends Fragment {
    View view;

    int rollerDrUp, rollerDrDown;
    public static final String SHARED_PREF = "sharedforsort";
    public static final String TEXT_SORT = "sort_method";
    public static final String BOOL_SORT = "IsDown";

    public static final int tempid = 9999;

    public LinearLayout lay;
    public ImageButton roller, stBut;
    public EditText search;
    boolean IsDown = true;
    public static NestedScrollView contCont;
    public FloatingActionButton to_top_but;
    public TextView first;
    ArrayList<AlbumTupple> Albums;
    public DBHelper db;
    public static ArrayList<String> ids;
    public static ArrayList<String> titles, artists, Reacts, links, linkNames;
    public static ArrayList<Bitmap> images;
    public static ArrayList<Float> r1, r2, r3, duration;
    public static ArrayList<Integer> year, IsEP, counts;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public Spinner deb;

    public class AlbumTupple<linearLayout, pos_as_default, title, artist, rating, duration, year> {
        public  LinearLayout linearLayout;
        public int pos_as_default, year, count;
        public String title, artist;
        public float rating, duration;
        public TextView id;


        public AlbumTupple(LinearLayout linearLayout, int pos_as_default,String title,String artist,
                           float rating, float duration, int count, int year, TextView id) {
            this.linearLayout = linearLayout;
            this.pos_as_default = pos_as_default;
            this.title = title;
            this.artist = artist;
            this.rating = rating;
            this.duration = duration;
            this.count = count;
            this.year = year;
            this.id = id;
        }
    }

    public void ChangeRoller(){
        contCont.fullScroll(ScrollView.FOCUS_UP);
        roller.setBackgroundResource(IsDown ? rollerDrDown: rollerDrUp);
        lay.removeAllViews();
        Collections.reverse(Albums);
        for (int i = 0 ; i < Albums.size(); i++) {
            lay.addView(Albums.get(i).linearLayout);
            Albums.get(i).id.setText(String.valueOf(i+1));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_album_list, container, false);
        lay = view.findViewById(R.id.cont);

        search = (EditText) view.findViewById(R.id.search);

        first = (TextView) view.findViewById(R.id.first);
        contCont = (NestedScrollView) view.findViewById(R.id.contCont);
        deb = (Spinner) view.findViewById(R.id.spinner);
        db = new DBHelper(view.getContext());
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
        roller = (ImageButton) view.findViewById(R.id.roller);
        stBut = (ImageButton) view.findViewById(R.id.settings);


        contCont.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (contCont.getScrollY() > 650) {
                    MainActivity.to_top_but.setVisibility(View.VISIBLE);
                }else {
                    MainActivity.to_top_but.setVisibility(View.GONE);

                }

            }
        });

        Albums = new ArrayList<AlbumTupple>();


        // SHARED PREFS -----------------------------------------------------------------
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        deb.setSelection(sharedPreferences.getInt(TEXT_SORT, 0));
        IsDown = sharedPreferences.getBoolean(BOOL_SORT, true);

        //-------------------------------------------------------------------------------

        // -------------------------------------------------------------------------
        int nightModeFlags =
                getContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                rollerDrDown = R.drawable.down_white;
                rollerDrUp = R.drawable.up_white;
                break;

            case Configuration.UI_MODE_NIGHT_NO:

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                rollerDrDown = R.drawable.down;
                rollerDrUp = R.drawable.up;
                break;
        }
        roller.setBackgroundResource(IsDown ? rollerDrDown: rollerDrUp);

        ChangeRoller();
        // ------------------------------------------------------------------------
        roller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsDown = !IsDown;
                ChangeRoller();
                editor.putBoolean(BOOL_SORT, IsDown);
                editor.apply();
            }
        });

        stBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int a, int i1, int i2) {
                lay.removeAllViews();
                if (search.getText().toString().matches("")){
                    for (int i = 0; i < Albums.size(); i++) {
                        lay.addView(Albums.get(i).linearLayout);
                        Albums.get(i).id.setText(String.valueOf(i+1));
                    }
                }
                else {
                    ArrayList<AlbumTupple> newTupList = new ArrayList<AlbumTupple>();
                    for (int i = 0; i < Albums.size(); i++) {
                        if (Albums.get(i).title.toLowerCase(Locale.ROOT).contains(search.getText().toString().toLowerCase(Locale.ROOT)))
                            newTupList.add(Albums.get(i));
                        if (Albums.get(i).artist.toLowerCase(Locale.ROOT).contains(search.getText().toString().toLowerCase(Locale.ROOT)))
                            newTupList.add(Albums.get(i));
                    }

                    Collections.sort(newTupList, new Comparator<AlbumTupple>() {
                        @Override
                        public int compare(AlbumTupple albumTupple, AlbumTupple t1) {
                            if (albumTupple.pos_as_default > t1.pos_as_default)
                                return 1;
                            else if (albumTupple.pos_as_default < t1.pos_as_default)
                                return -1;
                            else
                                return 0;
                        }
                    });

                    for (int i = 0; i < newTupList.size(); i++) {
                        try {
                            lay.addView(newTupList.get(i).linearLayout);
                            newTupList.get(i).id.setText(String.valueOf(i + 1));
                        }catch (Exception e){

                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
        try {
            Cursor cursor = db.readAllData();
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
        }catch (Exception e){
            Log.e("tag", e.getMessage());
            Toast.makeText(view.getContext(), "Can't load database", Toast.LENGTH_SHORT).show();
            ids.removeAll(ids);
        }

        lay.removeAllViews();

        if (ids.size() == 0) {
            first.setVisibility(View.VISIBLE);
        } else {
            first.setVisibility(View.GONE);
        }

            for (int i = 0; i < ids.size(); i++) {

                int id = i + 1;
                int tempid_ = tempid + i;

                LoadAlbum(id, titles.get(i), artists.get(i), IsEP.get(i), images.get(i),
                        new float[]{r1.get(i), r2.get(i), r3.get(i)}, lay, view.getContext(), tempid_);

                LinearLayout example = view.findViewById(tempid_);

                float rating = (r1.get(i) + r2.get(i) + r3.get(i)) / 3;

                AlbumTupple temp = new AlbumTupple(example, i, titles.get(i), artists.get(i), rating,
                        duration.get(i), counts.get(i), year.get(i),
                        (TextView) view.findViewById(tempid_ + tempid));

                Albums.add(temp);

                initspinnerfooter();


                example.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), EditActivity.class);
                        intent.putExtra("id", String.valueOf(id));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                    case "track count":
                        Collections.sort(Albums, new Comparator<AlbumTupple>() {
                            @Override
                            public int compare(AlbumTupple albumTupple, AlbumTupple t1) {
                                if(albumTupple.count < t1.count)
                                    return 1;
                                else if (albumTupple.count > t1.count)
                                    return -1;
                                else
                                    return 0;
                            }
                        });
                        break;
                    default:

                    }

                // Roller fix
                if (!IsDown){ Collections.reverse(Albums); }

                editor.putInt(TEXT_SORT, position);
                editor.apply();

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




    public static void LoadAlbum(int id,String name, String artist, int IsEp,
                                 Bitmap img, float[] ratings, LinearLayout lay, Context ct,
                                 int idL){
        // main container
        // ----------------------------------------------------------------------------------------
        LinearLayout example = new LinearLayout(ct);
        example.setId(idL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(AlbumListFragment.dp(ct,10), 0, AlbumListFragment.dp(ct,10), AlbumListFragment.dp(ct,20));
        example.setLayoutParams(params);
        example.setOrientation(LinearLayout.HORIZONTAL);


        // ----------------------------------------------------------------------------------------

        // Id
        // ----------------------------------------------------------------------------------------
        TextView txt = new TextView(ct);
        txt.setId(idL + 9999);

        LinearLayout.LayoutParams txtParams =  new LinearLayout.LayoutParams(AlbumListFragment.dp(ct,20), AlbumListFragment.dp(ct,20));
        txtParams.gravity = Gravity.CENTER_VERTICAL;
        txtParams.setMargins(0, 0, AlbumListFragment.dp(ct,5), 0);
        txt.setLayoutParams(txtParams);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setText(String.valueOf(id));
        txt.setTextSize(10);
        txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        // ----------------------------------------------------------------------------------------

        // Content
        // ----------------------------------------------------------------------------------------
        LinearLayout Cont = new LinearLayout(ct);
        Cont.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        Cont.setOrientation(LinearLayout.HORIZONTAL);
        // ----------------------------------------------------------------------------------------

        // Image
        // ----------------------------------------------------------------------------------------
        ImageView im = new ImageView(ct);
        im.setImageBitmap(img);
        im.setLayoutParams(new ViewGroup.LayoutParams(AlbumListFragment.dp(ct,84),AlbumListFragment.dp(ct,84)));
        Cont.addView(im);
        // ----------------------------------------------------------------------------------------

        // Info Layout
        // ----------------------------------------------------------------------------------------
        LinearLayout info = new LinearLayout(ct);
        LinearLayout.LayoutParams inf = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        inf.weight = 1;
        info.setOrientation(LinearLayout.VERTICAL);
        info.setLayoutParams(inf);
        Cont.addView(info);
        // ----------------------------------------------------------------------------------------

        // Name
        // ----------------------------------------------------------------------------------------
        LinearLayout l = new LinearLayout(ct);
        LinearLayout.LayoutParams lParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lParam.setMargins(AlbumListFragment.dp(ct,5), 0, 0, 0);
        l.setLayoutParams(lParam);

        l.setOrientation(LinearLayout.HORIZONTAL);
        TextView nameT = new TextView(ct);


        TextView EP = new TextView(ct);
        if(IsEp == 1)
            EP.setText("EP");

        EP.setTextColor(Color.RED);

        LinearLayout.LayoutParams EPP =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        EPP.gravity = Gravity.END;
        EPP.weight = 0;
        EPP.setMargins(AlbumListFragment.dp(ct,5), 0, 0, 0);
        EP.setLayoutParams(EPP);
        EP.setTextSize(16);


        LinearLayout.LayoutParams nameP =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        nameP.weight = 0;

        HorizontalScrollView horizScr = new HorizontalScrollView(ct);
        ViewGroup.MarginLayoutParams horParam = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        horParam.setMarginStart(dp(ct, 5));
        horizScr.setScrollBarSize(dp(ct, 1));
        horizScr.setLayoutParams(horParam);

        nameT.setLayoutParams(nameP);
        nameT.setText(name);
        nameT.setTextSize(16);

        horizScr.addView(nameT);

        l.addView(horizScr);
        l.addView(EP);

        info.addView(l);
        System.out.println(name.length() + "---A");
        if (name.length() > "miXXXtape II: Долгий путь".length()) {
            ViewGroup.LayoutParams horParams = horizScr.getLayoutParams();
            horParams.width = dp(ct, 220);
            horizScr.setLayoutParams(horParams);
        }
        // ----------------------------------------------------------------------------------------

        // Description
        // ----------------------------------------------------------------------------------------

        TextView desc = new TextView(ct);

        LinearLayout.LayoutParams descP =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        descP.weight = 0;
        desc.setLayoutParams(descP);
        desc.setText(artist);
        desc.setTextSize(12);

        HorizontalScrollView horizScr2 = new HorizontalScrollView(ct);
        ViewGroup.MarginLayoutParams horParam2 = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        horParam2.setMarginStart(dp(ct, 10));
        horizScr2.setScrollBarSize(dp(ct, 1));
        horizScr2.setLayoutParams(horParam2);

        horizScr2.addView(desc);

        info.addView(horizScr2);
        // ----------------------------------------------------------------------------------------

        // RatingBar
        // ----------------------------------------------------------------------------------------
        RatingBar rb = new RatingBar(ct,null, 0, R.style.Widget_App_CustomProgressBar);
        rb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                dp(ct,50)));
        rb.setIsIndicator(true);
        float rating = (ratings[0] + ratings[1] + ratings[2]) / 3;
        rb.setRating(rating);
        rb.setNumStars(5);
        rb.setStepSize(0.5f);
        rb.setScaleX(0.9f);
        rb.setScaleY(0.9f);
        rb.setPadding(0,0,0,0);

        info.addView(rb);
        // ----------------------------------------------------------------------------------------
        example.addView(txt);
        example.addView(Cont);
        lay.addView(example);

    }
}