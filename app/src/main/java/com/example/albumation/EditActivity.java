package com.example.albumation;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditActivity extends AppCompatActivity {
    public DBHelper db;
    EditText title, artist, duration, year, react;
    RatingBar liked, tr_recn, your;
    ImageButton imgBut;
    Button cancel, updateBut;
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
        year = (EditText) findViewById(R.id.edit_text_year_edit);
        react = (EditText) findViewById(R.id.react_edit);
        EP = (CheckBox) findViewById(R.id.checkBox_edit);
        imgBut = (ImageButton) findViewById(R.id.imageButton_edit);
        liked = (RatingBar) findViewById(R.id.liked_ratingBar_edit);
        tr_recn = (RatingBar) findViewById(R.id.tr_recogn_ratingBar_edit);
        your = (RatingBar) findViewById(R.id.your_ratingBar_edit);
        updateBut = (Button) findViewById(R.id.button_update);
        cancel = (Button) findViewById(R.id.button_cancel);


        String idd = getIntent().getStringExtra("id");
        id = Integer.parseInt(idd);
        id -= 1;
//        id = id - 1;
//        id = Integer.parseInt(idd);

        float timeText = AlbumListFragment.duration.get(id);
//                getIntent().getStringExtra("Duration");

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


        title.setText(AlbumListFragment.titles.get(id));
        artist.setText(AlbumListFragment.artists.get(id));
        duration.setText(h + ":"+ btw + m + ":" + btw2 + s);
        year.setText(String.valueOf(AlbumListFragment.year.get(id)));
        react.setText(AlbumListFragment.Reacts.get(id));
        liked.setRating(AlbumListFragment.r1.get(id));
        tr_recn.setRating(AlbumListFragment.r2.get(id));
        your.setRating(AlbumListFragment.r3.get(id));
        boolean ep = (AlbumListFragment.IsEP.get(id) == 1);
        EP.setChecked(ep);


        Bitmap bitmap = AlbumListFragment.images.get(id);
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

//                Bitmap bitmap = ((BitmapDrawable) imgBut.getDrawable()).getBitmap();
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                imgByte = baos.toByteArray();
                int isep = EP.isChecked() ? 1 : 0;
                String[] timeTexts = duration.getText().toString().split(":");

                Float time = Float.valueOf(timeTexts[0]) + (Float.valueOf(timeTexts[1]) / 60) +
                        (Float.valueOf(timeTexts[2]) / 3600);
                db.UpdateData(String.valueOf(id+1), title.getText().toString(),
                        artist.getText().toString(), react.getText().toString(),
                        isep, Integer.parseInt(
                        year.getText().toString()), time,
                        liked.getRating(), tr_recn.getRating(), your.getRating(), imgByte);

                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditActivity.this, MainActivity.class);
        finish();
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