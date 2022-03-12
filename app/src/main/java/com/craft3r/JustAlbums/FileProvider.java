package com.craft3r.JustAlbums;

import android.content.Context;
import android.net.Uri;

import java.io.File;

public class FileProvider extends androidx.core.content.FileProvider {

    public Uri getDatabaseURI(Context c) {
        // https://developer.android.com/reference/android/support/v4/content/FileProvider.html
        // old approach that worked until 2020-ish
        // File data = Environment.getDataDirectory();
        // String dbName = "UserContent.db";
        // String currentDBPath = "//data//com.url.myapp//databases//" + dbName;

        // File exportFile = new File(data, currentDBPath);
        File exportFile = c.getDatabasePath("JustAlbums.db"); // new approach

        return getFileUri(c, exportFile);
    }

    public Uri getFileUri(Context c, File f){
        return getUriForFile(c, "com.url.myapp.fileprovider", f);
    }

}
