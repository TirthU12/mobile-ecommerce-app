package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper  extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Application.db"; // Replace with your database file name.
    private static final int DATABASE_VERSION = 1;
    private final Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        //copyDatabase(); // Copy the database when DBHelper is initialized.
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // This method is called when the database is created for the first time.
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This method is called when the database needs to be upgraded to a new version.
    }

    // Method to copy the database from assets to the app's data directory
    private void copyDatabase() {
        try {
            File dbFile = context.getDatabasePath(DATABASE_NAME);

            if (!dbFile.exists()) {
                dbFile.getParentFile().mkdirs();
                InputStream in = context.getAssets().open(DATABASE_NAME);
                OutputStream out = new FileOutputStream(dbFile);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }

                out.flush();
                out.close();
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
