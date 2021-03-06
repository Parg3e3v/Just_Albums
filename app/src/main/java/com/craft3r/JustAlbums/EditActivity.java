package com.craft3r.JustAlbums;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditActivity extends AppCompatActivity {
    public DBHelper db;
    EditText title, artist, duration, count, link, lName, year, react;
    RatingBar liked, tr_recn, your;
    ImageButton imgBut;
    Button cancel, updateBut, linkBut;
    public Uri imgURI;
    CheckBox EP;

    private static final int PICK_IMG = 1;

    byte[] imgByte;
    int id;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        db = new DBHelper(getApplicationContext());


        title = (EditText) findViewById(R.id.title_edittext_edit);
        artist = (EditText) findViewById(R.id.artist_edittext_edit);
        duration = (EditText) findViewById(R.id.editTextTime_edit);
        count = (EditText) findViewById(R.id.track_count_edittext_edit);
        link = (EditText) findViewById(R.id.link_edittext_edit);
        lName = (EditText) findViewById(R.id.link_name_edittext_edit);
        year = (EditText) findViewById(R.id.edit_text_year_edit);
        react = (EditText) findViewById(R.id.react_edit);
        EP = (CheckBox) findViewById(R.id.checkBox_edit);
        imgBut = (ImageButton) findViewById(R.id.imageButton_edit);
        liked = (RatingBar) findViewById(R.id.liked_ratingBar_edit);
        tr_recn = (RatingBar) findViewById(R.id.tr_recogn_ratingBar_edit);
        your = (RatingBar) findViewById(R.id.your_ratingBar_edit);
        updateBut = (Button) findViewById(R.id.button_update);
        cancel = (Button) findViewById(R.id.button_cancel);
        linkBut = (Button) findViewById(R.id.buttonLink);


        String idd = getIntent().getStringExtra("id");
        id = Integer.parseInt(idd);
        id -= 1;

        float timeText = LoadingActivity.duration.get(id);

        double time = Double.valueOf(timeText);
        double t = time*3600;
        int h = (int) time;
        int m = (int) Math.floor(t / 60 % 60);
        int s = (int) (Math.floor(t % 60));

        String btw = "";
        String btw2 = "";

        if(m < 10)
            btw = "0";
        if(s < 10)
            btw2 = "0";


        title.setText(LoadingActivity.titles.get(id));
        artist.setText(LoadingActivity.artists.get(id));
        duration.setText(h + ":"+ btw + m + ":" + btw2 + s);
        count.setText(String.valueOf(LoadingActivity.counts.get(id)));
        link.setText(LoadingActivity.links.get(id));
        lName.setText(LoadingActivity.linkNames.get(id));
        year.setText(String.valueOf(LoadingActivity.year.get(id)));
        react.setText(LoadingActivity.Reacts.get(id));
        liked.setRating(LoadingActivity.r1.get(id));
        tr_recn.setRating(LoadingActivity.r2.get(id));
        your.setRating(LoadingActivity.r3.get(id));
        linkBut.setText(LoadingActivity.linkNames.get(id));
        boolean ep = (LoadingActivity.IsEP.get(id) == 1);
        EP.setChecked(ep);

        linkBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String url = LoadingActivity.links.get(id);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Link not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Bitmap bitmap = LoadingActivity.images.get(id);
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        int wh =  AlbumListFragment.dp(getApplicationContext(),320);
        Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, wh, wh, true);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        Drawable drawable = new BitmapDrawable(bitmapScaled);
        imgByte = byteArray.toByteArray();

        imgBut.setImageDrawable(drawable);

        imgBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery, "Select picture"), PICK_IMG);
            }
        });

        updateBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int isep = EP.isChecked() ? 1 : 0;
                String[] timeTexts = duration.getText().toString().split(":");

                Float time = Float.valueOf(timeTexts[0]) + (Float.valueOf(timeTexts[1]) / 60) +
                        (Float.valueOf(timeTexts[2]) / 3600);
                db.UpdateData(String.valueOf(id+1), title.getText().toString(),
                        artist.getText().toString(), react.getText().toString(),
                        isep, Integer.parseInt(year.getText().toString()), time,
                        Integer.parseInt(count.getText().toString()), link.getText().toString(),
                        lName.getText().toString(), liked.getRating(), tr_recn.getRating(),
                        your.getRating(), imgByte);

                Intent intent = new Intent(EditActivity.this, LoadingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMG && resultCode == RESULT_OK){
            imgURI = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgURI);
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                int wh =  AlbumListFragment.dp(getApplicationContext(),320);
                Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, wh, wh, true);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                Drawable drawable = new BitmapDrawable(bitmapScaled);
                imgByte = byteArray.toByteArray();

                imgBut.setImageDrawable(drawable);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}