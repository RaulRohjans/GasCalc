package com.example.gascalc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    CardView crd_novo;
    CardView crd_Registos;
    ImageView img_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        crd_novo = findViewById(R.id.crd_novo);
        crd_Registos = findViewById(R.id.crd_registos);
        img_settings = findViewById(R.id.img_settings);

        SQLiteDatabase GasMediaDB = openOrCreateDatabase("GasMediaDB", MODE_PRIVATE, null);
        GasMediaDB.execSQL("CREATE TABLE IF NOT EXISTS GasCount(GasID Integer primary key, Dist Real, Gas Real, Preco Real, Media Real)");

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        {
            requestStorageReadPermission();
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        {
            requestStorageWritePermission();
        }

        crd_novo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), NovoRegisto.class);
                        startActivity(i);
                    }
                }
        );

        crd_Registos.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), Registos.class);
                        startActivity(i);
                    }
                }
        );

        img_settings.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), settings.class);
                        startActivity(i);
                    }
                }
        );
    }

    private void requestStorageWritePermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
            new AlertDialog.Builder(this).setTitle(getText(R.string.PermTitle)).setMessage(getText(R.string.ReasonWrite)).setPositiveButton(getText(R.string.ButtonOk), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }).setNegativeButton(getText(R.string.ButtonCancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void requestStorageReadPermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            new AlertDialog.Builder(this).setTitle(getText(R.string.PermTitle)).setMessage(getText(R.string.ReasonRead)).setPositiveButton(getText(R.string.ButtonOk), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
            }).setNegativeButton(getText(R.string.ButtonCancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle(getText(R.string.LeaveTitle)).setMessage(getText(R.string.LeaveMessage)).setPositiveButton(getText(R.string.LeaveYes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
                System.exit(0);
            }
        }).setNegativeButton(getText(R.string.LeaveNo), null).create().show();
    }

}

