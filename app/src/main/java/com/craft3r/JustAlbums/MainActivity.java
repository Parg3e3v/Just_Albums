package com.craft3r.JustAlbums;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.ActionMode;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    public static FloatingActionButton fl_but, to_top_but;
    public static int Mode = 0;
    LoadingDialog loadingDialog;
    @Override
    protected void onStop() {
        super.onStop();
        AlbumListFragment.activated = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingDialog = new LoadingDialog(this);

        loadingDialog.startLoadingDialog();
        recourse();
        fl_but = findViewById(R.id.fl_button);
        to_top_but = findViewById(R.id.to_top_button);

        to_top_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlbumListFragment.contCont.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(SettingsActivity.SHARED_PREFS, MODE_PRIVATE);
        switch (sharedPreferences.getString(SettingsActivity.TEXT, "System Default")){
            case "Light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Mode = 0;
                break;
            case "Dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Mode = 1;
                break;
            case "System Default":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                Mode = 2;
                break;
        }

        fl_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,new AlbumListFragment());
        fragmentTransaction.commit();

    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.left, R.anim.right);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    void recourse(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AlbumListFragment.activated){
                    loadingDialog.dismissDialog();
                }else{
                    recourse();
                }
            }
        }, 1000);

    }

}