package com.craft3r.JustAlbums;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.UiModeManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    public String path = "/data/data/com.craft3r.JustAlbums/databases";
    ImageButton arrow;
    LinearLayout import_export, delete_db, dark_mode;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public boolean NEED_TO_BE_UPDATED;

    public void saveData(String txt){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT, txt);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        arrow = (ImageButton) findViewById(R.id.fromStToMain);
        import_export = (LinearLayout) findViewById(R.id.import_export);
        delete_db = (LinearLayout) findViewById(R.id.delete_db);
        dark_mode = (LinearLayout) findViewById(R.id.night_mode_switch_layout);
        NEED_TO_BE_UPDATED = false;
        // NIGHT MODE
        //-----------------------------------------------------------------------------------------

        dark_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                String[] txt = {"Light", "Dark", "System Default"};

                builder.setSingleChoiceItems(txt, MainActivity.Mode, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String temp = "System Default";
                        switch (txt[i]){
                            case "Light":
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                temp = txt[i];
                                MainActivity.Mode = 0;
                                break;
                            case "Dark":
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                MainActivity.Mode = 1;
                                temp = txt[i];
                                break;
                            case "System Default":
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                                MainActivity.Mode = 2;
                                temp = txt[i];
                                break;
                        }
                        saveData(temp);

                    }
                }
                )
                        .setCancelable(true)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("Set dark mode");
                alertDialog.show();
            }
        });

        // IMPORT / EXPORT
        //-----------------------------------------------------------------------------------------
        import_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Export not working yet :3", Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                String[] txt = {"Import", "Export"};
                builder.setItems(txt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (txt[0].equals(txt[i])) {
                            Intent intent = new Intent()
                                    .setType("*/*")
                                    .setAction(Intent.ACTION_GET_CONTENT);

                            startActivityForResult(Intent.createChooser(intent,
                                    "Select a file"), 123);
                        } else if (txt[1].equals(txt[i])) {

                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("application/octet-stream");

                            Uri uri = new FileProvider().getDatabaseURI(getApplicationContext());

                            intent.putExtra(Intent.EXTRA_STREAM, uri);
                            intent.putExtra(Intent.EXTRA_TEXT, "JustAlbums: top proga dlya top pacanov");
                            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                            for (ResolveInfo resolveInfo : resInfoList) {
                                String packageName = resolveInfo.activityInfo.packageName;
                                grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                            startActivity(Intent.createChooser(intent, "Backup via:"));
                        } else if (txt[2].equals(txt[i])) {
                            Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                            startActivityForResult(Intent.createChooser(intent1,
                                    "Select a folder"), 100);
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
                                NEED_TO_BE_UPDATED = true;
                                try {
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
                GoToMain();
            }
        });
    }

    @Override
    public void onBackPressed() {
        GoToMain();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == RESULT_OK) {
            Uri selectedfile = data.getData();
            if(copyFile(selectedfile, Uri.parse(path), true)) {
                Toast.makeText(getApplicationContext(), "Imported successfully",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingsActivity.this, LoadingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else
                Toast.makeText(getApplicationContext(), "Import error",
                        Toast.LENGTH_SHORT).show();

        }
    }


    // Copy Function
    private boolean copyFile(Uri uri, Uri outputPath, boolean Delete) {
        if (Delete) {
            try {
                new File(outputPath + "/JustAlbums.db").delete();
            } catch (Exception e) {
                Log.e("tag", e.getMessage());
            }
        }
        InputStream in = null;
        OutputStream out = null;
        try {
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
            Toast.makeText(getApplicationContext(), "Can't import file",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

    }
    public void GoToMain() {
        Intent intent = new Intent(SettingsActivity.this,
                                NEED_TO_BE_UPDATED ? LoadingActivity.class : MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
