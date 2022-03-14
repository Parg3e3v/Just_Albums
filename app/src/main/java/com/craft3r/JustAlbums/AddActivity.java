package com.craft3r.JustAlbums;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
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

public class AddActivity extends AppCompatActivity {

    public EditText titleInput,artistInput, Duratio, Year, React, link, linkName, count;
    public RatingBar Liked,Recogn,Your;
    public Button button;
    public ImageButton imgSelect;
    public Uri imgURI;
    public byte[] img;
    private static final int PICK_IMG = 1;
    CheckBox IsEpBut;

    int IsEP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        titleInput = (EditText) findViewById(R.id.title_edittext);
        artistInput = (EditText) findViewById(R.id.artist_edittext);
        Duratio = (EditText) findViewById(R.id.editTextTime);
        linkName = (EditText) findViewById(R.id.link_text_edittext);
        link = (EditText) findViewById(R.id.link_edittext);
        count = (EditText) findViewById(R.id.track_count_edittext);
        Year = (EditText) findViewById(R.id.edit_text_year);
        Liked = (RatingBar) findViewById(R.id.liked_ratingBar);
        Recogn = (RatingBar) findViewById(R.id.tr_recogn_ratingBar);
        Your = (RatingBar) findViewById(R.id.your_ratingBar);
        button = (Button) findViewById(R.id.button);
        imgSelect = (ImageButton) findViewById(R.id.imageButton);
        React = (EditText) findViewById(R.id.emoji);
        IsEpBut = (CheckBox) findViewById(R.id.IsEP);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (imgURI.toString() != "") {
                        DBHelper db = new DBHelper(AddActivity.this);

                        String timeText = Duratio.getText().toString();

                        String[] timeTexts = timeText.split(":");
                        Float time = Float.valueOf(timeTexts[0]) + (Float.valueOf(timeTexts[1]) / 60) +
                                (Float.valueOf(timeTexts[2]) / 3600);

                        IsEP = IsEpBut.isChecked() ? 1 : 0;
                        db.AddAlbum(titleInput.getText().toString(), artistInput.getText().toString(),
                                React.getText().toString(), IsEP,
                                Integer.valueOf(Year.getText().toString()),
                                time, Integer.parseInt(count.getText().toString()),
                                link.getText().toString(), linkName.getText().toString(),
                                Liked.getRating(), Recogn.getRating(), Your.getRating(), img);

                        Intent intent = new Intent(AddActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Failed to add album", Toast.LENGTH_LONG).show();

                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Failed to add album", Toast.LENGTH_LONG).show();
                }
            }
        });

        imgSelect.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery, "Select picture"), PICK_IMG);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddActivity.this, MainActivity.class);
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
                img = byteArray.toByteArray();

                imgSelect.setImageDrawable(drawable);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}