package com.example.albumation;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SettingsActivity extends AppCompatActivity {

    public String path = "/data/data/com.example.albumation/databases";
    private static final int MY_PERMISSION_REQUEST_STORAGE = 1;
    ImageButton arrow;
    LinearLayout import_export, delete_db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        arrow = (ImageButton) findViewById(R.id.fromStToMain);
        import_export = (LinearLayout) findViewById(R.id.import_export);
        delete_db = (LinearLayout) findViewById(R.id.delete_db);



        // IMPORT / EXPORT
        //-----------------------------------------------------------------------------------------
        import_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                String[] txt = {"Import", "Export"};
                builder.setItems(txt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (txt[i] == txt[0]){
                            Intent intent = new Intent()
                                    .setType("*/*")
                                    .setAction(Intent.ACTION_GET_CONTENT);

                            startActivityForResult(Intent.createChooser(intent,
                                    "Select a file"), 123);
                        }else if (txt[i] == txt[1]) {

                            if (ContextCompat.checkSelfPermission(SettingsActivity.this,
                                    WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                if (ActivityCompat.shouldShowRequestPermissionRationale(SettingsActivity.this,
                                        WRITE_EXTERNAL_STORAGE))
                                    ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{
                                            WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_STORAGE);
                                else
                                    ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{
                                            WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_STORAGE);
                            }else {
                                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                                startActivityForResult(intent, 100);
                                Toast.makeText(getApplicationContext(), "Export", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                        .setCancelable(true)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("Import/Export");
                alertDialog.show();
            }
        });
        //-----------------------------------------------------------------------------------------



        // DELETE DB
        //-----------------------------------------------------------------------------------------
        delete_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setMessage("Are you sure you want to delete your Database?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    // delete the original file
                                    new File(path + "/JustAlbums.db").delete();
                                }
                                catch (Exception e) {
                                    Log.e("tag", e.getMessage());
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("Delete Database");
                alertDialog.show();

            }
        });
        //-----------------------------------------------------------------------------------------


        // BACK ARROW
        //-----------------------------------------------------------------------------------------
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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(SettingsActivity.this,
                            WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                        startActivityForResult(intent, 100);
                        Toast.makeText(getApplicationContext(), "Export", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(this, "No permission granted!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == RESULT_OK) {
            Uri selectedfile = data.getData();
            //"file//" + selectedfile.getPath().split("/document/raw:")[1]
//            String path = selectedfile.getPath().split("/document/raw:")[1];
            if(copyFile(selectedfile, Uri.parse(path), true))
                Toast.makeText(getApplicationContext(), "Imported successfully",
                        Toast.LENGTH_SHORT).show();

        }
        else if (requestCode == 100 && resultCode == RESULT_OK){
            String ur = data.getData().getPath();
            Uri pathToSave = Uri.parse(ur);
            File file = new File(ur);
            System.out.println(ur + "------AAAAA");
            MediaScannerConnection.scanFile(this, new String[] { file.toString() }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
//pathToSave
            File fl = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            if (!fl.exists())
                fl.mkdirs();
            copyFile(Uri.parse("file://" + path + "/JustAlbums.db"), Uri.fromFile(
                    fl), false);
        }
    }



    // Copy Function
    private boolean copyFile(Uri uri, Uri outputPath, boolean Delete) {
        if (Delete) {
            try {
                // delete the original file
                new File(outputPath + "/JustAlbums.db").delete();
            } catch (Exception e) {
                Log.e("tag", e.getMessage());
            }
        }
        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath.getPath());
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(getApplicationContext().getContentResolver()
                    .openFileDescriptor(uri, "r").getFileDescriptor());
            try {
                out = new FileOutputStream(outputPath + "/JustAlbums.db");
            }catch (Exception e){
                File gpxfile = new File(outputPath.getPath(), "JustAlbums.db");
                FileWriter writer = new FileWriter(gpxfile);
                writer.flush();
                writer.close();

                out = new FileOutputStream(outputPath + "/JustAlbums.db");

            }
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

            return true;

        }  catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
            Toast.makeText(getApplicationContext(), "File not found",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Can't import/export file",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

    }
}
