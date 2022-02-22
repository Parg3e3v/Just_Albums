package com.example.albumation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class Album {
    public String name, artist, react;
    public float[] ratings;
    public int id;
    public Bitmap img;
    public int year, IsEP;
    public float duration;

    public Album(String name, String artist, String react, int IsEp, Bitmap img,
                 float[] ratings, int id, float duration, int year){
        this.name = name;
        this.artist = artist;
        this.react = react;
        this.IsEP = IsEp;
        this.img = img;
        this.ratings = ratings;
        this.id = id;
        this.duration = duration;
        this.year = year;
    }

    public static void LoadAlbum(int id,String name, String artist, String react, int IsEp,
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
//        im.setImageURI(Uri.fromFile(new File("android.resource://com.example.albumation/" + R.drawable.ic_launcher_background)));
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
//        EP.setText(react);
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

        nameT.setMaxLines(1);
        nameP.setMargins(AlbumListFragment.dp(ct,5), 0, 0, 0);
        nameT.setLayoutParams(nameP);
        nameT.setText(name);
        nameT.setMaxWidth(AlbumListFragment.dp(ct,210));
        nameT.setTextSize(16);

        TextView dt = new TextView(ct);
        LinearLayout.LayoutParams dot = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        dot.weight = 0;

        dt.setMaxLines(1);
        dt.setLayoutParams(dot);
        dt.setTextSize(16);
        nameT.measure(0, 0);
        System.out.println("Width: " + nameT.getMeasuredWidth() + artist);
        if (nameT.getMeasuredWidth() >= 551) {
            dt.setText("...");
        }
        l.addView(nameT);
        l.addView(dt);
        l.addView(EP);

        info.addView(l);
        // ----------------------------------------------------------------------------------------

        // Description
        // ----------------------------------------------------------------------------------------
//        LinearLayout descLay = new LinearLayout(ct);
//        LinearLayout.LayoutParams dsc = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
//        dsc.weight = 1;
//        descLay.setOrientation(LinearLayout.HORIZONTAL);
//        descLay.setLayoutParams(dsc);
//
//

        TextView desc = new TextView(ct);

        LinearLayout.LayoutParams descP =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        descP.weight = 0;
        descP.setMargins(AlbumListFragment.dp(ct,10), 0, 0, 0);
        desc.setLayoutParams(descP);
        desc.setText(artist);
        desc.setTextSize(12);

//        descLay.addView(desc);

        info.addView(desc);
        // ----------------------------------------------------------------------------------------

        // RatingBar
        // ----------------------------------------------------------------------------------------
        RatingBar rb = new RatingBar(ct,null, 0, R.style.Widget_App_CustomProgressBar);
        rb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                AlbumListFragment.dp(ct,50)));
        rb.setIsIndicator(true);
        float rating = (ratings[0] + ratings[1] + ratings[2]) / 3;
        rb.setRating(rating);
        rb.setNumStars(5);
        rb.setStepSize(0.5f);
        rb.setScaleX(0.9f);
        rb.setScaleY(0.9f);
        rb.setPadding(0,0,0,0);

//        rb.setProgressDrawable(getResources().getDrawable(R.drawable.ic_new_ratingbar));
        info.addView(rb);
        // ----------------------------------------------------------------------------------------


//        example.setBackgroundColor(0xffff0000);

        example.addView(txt);
        example.addView(Cont);
//        temp.addView(Cont);
        lay.addView(example);

    }
}
