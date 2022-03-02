package com.example.albumation;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SettingsActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST_STORAGE = 1;
    public String path = "/data/data/com.example.albumation/databases";
    ImageButton arrow;
    LinearLayout import_export, delete_db, drkmode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        arrow = (ImageButton) findViewById(R.id.fromStToMain);
        import_export = (LinearLayout) findViewById(R.id.import_export);
        delete_db = (LinearLayout) findViewById(R.id.delete_db);
        drkmode = (LinearLayout) findViewById(R.id.night_mode_switch_layout);

        if (ContextCompat.checkSelfPermission(SettingsActivity.this,
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            if (ActivityCompat.shouldShowRequestPermissionRationale(SettingsActivity.this,
                    WRITE_EXTERNAL_STORAGE))
                ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{
                        WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_STORAGE);
            else
                ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{
                        WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_STORAGE);

        drkmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                String[] txt = {"On", "Off", "By System Default"};
                builder.setItems(txt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Not working yet", Toast.LENGTH_LONG).show();
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
                alertDialog.setTitle("Dark mode");
                alertDialog.show();
            }
        });

        // IMPORT / EXPORT
        //-----------------------------------------------------------------------------------------
        import_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                String[] txt = {"Import", "Export(by sharing)", "Export(by saving file)"};
                builder.setItems(txt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (txt[i] == txt[0]){
                            Intent intent = new Intent()
                                    .setType("*/*")
                                    .setAction(Intent.ACTION_GET_CONTENT);

                            startActivityForResult(Intent.createChooser(intent,
                                    "Select a file"), 123);
                        }else if (txt[i] == txt[1]){
                       /*

//                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//                            startActivityForResult(intent, 100);

//                            Intent shareIntent = new Intent();
//                            shareIntent.setAction(Intent.ACTION_SEND);
//                            shareIntent.putExtra(Intent.EXTRA_STREAM,
//                                    "file://" + path + "/JustAlbums.db");
//                            shareIntent.setType("application/x-sqlite3");
//                            startActivity(shareIntent);

                            

//                            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                            File file = new File(path + "/JustAlbums.db");

                            if(file.exists()) {
//                                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//                                StrictMode.setVmPolicy(builder.build());
//                                intentShareFile.setType("application/x-sqlite3");
//                                intentShareFile.putExtra(Intent.EXTRA_STREAM,
//                                        Uri.parse("file://" + path + "/JustAlbums.db"));
//                                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
//                                        "Sharing File...");
//                                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
//                                startActivity(Intent.createChooser(intentShareFile, "Share My File"));

//                                try {
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    Uri uri = Uri.parse("file://" + path + ".provider" + "/JustAlbums.db");
//                                    Uri uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
                                Uri uri = FileProvider.getUriForFile(SettingsActivity.this, "com.gs.common.fileprovider", file);
                                intent.putExtra(Intent.EXTRA_STREAM, uri);
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    intent.setType("application/x-sqlite3");
                                    startActivity(Intent.createChooser(intent, "Share by"));
//
//                                }catch (Exception e){
//                                    e.printStackTrace();
//                                    Log.e("tag", e.getMessage());
//                                }
                            }else
                                Toast.makeText(getApplicationContext(), "Database not exists",
                                        Toast.LENGTH_SHORT).show();
                                        
                                        

//                            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//                            StrictMode.setVmPolicy(builder.build());
                            /*
                            Intent intent = new Intent(Intent.ACTION_SEND);
//                            intent.setType("application/octet-stream");
                            intent.setType("text/plain");



                            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + path + "/JustAlbums.db"));
//                            intent.putExtra(Intent.EXTRA_STREAM, "It's..... Sharing?");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(Intent.createChooser(intent, "Share by..."));
                            */
                            ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{
                                    READ_EXTERNAL_STORAGE,
                                    WRITE_EXTERNAL_STORAGE},
                                    PackageManager.PERMISSION_GRANTED);
                            StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
                            StrictMode.setVmPolicy(builder1.build());


                            File file = new File(path + "/JustAlbums.db");
                            System.out.println(path + "/JustAlbums.db" + "--AAAA");
                            if(!file.exists()){
                                Toast.makeText(getApplicationContext(), "File not exists", Toast.LENGTH_SHORT).show();
                            }
                            else {

                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("application/x-sqlite3");
                                Uri uri = Uri.parse("file://" + file);
                                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file));

                                startActivity(Intent.createChooser(intent, "Share database"));
                            }
                        }else if (txt[i] == txt[2]){
//                            copyFile(Uri.fromFile(file), Uri.fromFile(dst), false);
                            copyAsset("JustAlbums.db");

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
        arrow.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            SettingsActivity.this.finish();
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        SettingsActivity.this.finish();
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    if (ContextCompat.checkSelfPermission(SettingsActivity.this,
                            WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        Toast.makeText(this, "Please give permission for program",
                                Toast.LENGTH_SHORT).show();
        }
    }

    private void copyAsset(String filename){
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyDatabases";
        File dir = new File(dirPath);
        if(!dir.exists()) {
            dir.mkdirs();
            if (!dir.exists()){
                dirPath = "/self/primary/MyDatabases";
                dir = new File(dirPath);
                dir.mkdir();
                System.out.println("Chgidem");
                if (!dir.exists()) {
                    System.out.println("ne smeshno");
                }
                }
        }
        MediaScannerConnection.scanFile(this, new String[] { dir.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(String.valueOf(Uri.parse(path + "/" + filename)));
            File outFile = new File(dirPath, filename);
            out = new FileOutputStream(outFile);
            copyFileToExternal(in, out);
            Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();
        }catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (out != null)
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private void copyFileToExternal(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == RESULT_OK) {
            Uri selectedfile = data.getData();
            if(copyFile(selectedfile, Uri.parse(path), true))
                Toast.makeText(getApplicationContext(), "Imported successfully",
                        Toast.LENGTH_SHORT).show();

        }
//        else if (requestCode == 100 && resultCode == RESULT_OK){
//            Uri pathToSave = Uri.parse(data.getData().toString().split("content:")[1]);
//
//            copyFile(Uri.parse("file://" + path + "/JustAlbums.db"), pathToSave,
//                                            false);
//        }
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

            return true;

        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
            for (int i =0; i < e.getStackTrace().length; i++)
                Log.e("tag", String.valueOf(e.getStackTrace()[i]));
            Toast.makeText(getApplicationContext(), "Can't import file",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

    }


}
