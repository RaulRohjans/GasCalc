package com.example.gascalc;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.database.sqlite.SQLiteDatabase.openDatabase;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;
import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;
import static androidx.core.app.ActivityCompat.startActivityForResult;
import static androidx.core.content.ContextCompat.startActivity;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

    Context c;
    ArrayList<Model> models;


    public MyAdapter(Context c, ArrayList<Model> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gas, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.mMedia.setText(models.get(position).getMedia());
        holder.mPreco.setText(models.get(position).getPreco());
        holder.mGas.setText(models.get(position).getGas());
        holder.mDist.setText(models.get(position).getDist());
        holder.mCrv.setTag(models.get(position).getID());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemCLickListener(View v, int position) {
                int gTag = Integer.valueOf(models.get(position).getID());


                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(c);
                dlgAlert.setMessage(R.string.AvisoDel);
                dlgAlert.setTitle(R.string.AvisoTitulo);
                dlgAlert.setPositiveButton(R.string.AvisoSim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(ContextCompat.checkSelfPermission(c, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(c, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        {
                            Intent i = new Intent(c, LoadDelete.class);
                            i.putExtra("gTag", gTag);
                            startActivityForResult((Activity) c, i, 1, null);
                        }

                    }
                });
                dlgAlert.setNegativeButton(R.string.AvisoNao, null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
