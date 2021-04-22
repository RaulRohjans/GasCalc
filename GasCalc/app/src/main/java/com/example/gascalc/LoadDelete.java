package com.example.gascalc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class LoadDelete extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_delete);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        try {
            int gTag = getIntent().getIntExtra("gTag", 0);

            SQLiteDatabase GasMediaDB = openOrCreateDatabase("GasMediaDB", MODE_PRIVATE,null);
            GasMediaDB.execSQL("delete from GasCount where GasID = '" + gTag + "'");

            setResult(RESULT_OK);
        }
        catch (Exception ee)
        {
            setResult(58);
        }
        this.finish();
    }
}