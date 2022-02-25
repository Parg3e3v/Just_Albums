package com.example.albumation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class SettingsActivity extends AppCompatActivity {

    ImageButton arrow;
    LinearLayout import_export;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        arrow = (ImageButton) findViewById(R.id.fromStToMain);
        import_export = (LinearLayout) findViewById(R.id.import_export);

        import_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent()
                        .setType("*/*")
                        .setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
            }
        });

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                SettingsActivity.this.finish();
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        SettingsActivity.this.finish();
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == RESULT_OK) {
            Uri selectedfile = data.getData();
            //"file//" + selectedfile.getPath().split("/document/raw:")[1]
//            String path = selectedfile.getPath().split("/document/raw:")[1];
            copyFile(selectedfile,
                    "/data/data/com.example.albumation/databases");
        }
    }

    private void copyFile(Uri uri, String outputPath) {

        try {
            // delete the original file
            new File(outputPath + "/JustAlbums.db").delete();
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(getApplicationContext().getContentResolver().openFileDescriptor(uri, "r").getFileDescriptor());
            out = new FileOutputStream(outputPath + "/JustAlbums.db");

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        }  catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
            Toast.makeText(getApplicationContext(), "Can't import file", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
            Toast.makeText(getApplicationContext(), "Can't import file", Toast.LENGTH_SHORT).show();
        }

    }
}
