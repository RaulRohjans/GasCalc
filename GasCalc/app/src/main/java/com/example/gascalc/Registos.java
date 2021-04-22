package com.example.gascalc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class Registos extends AppCompatActivity {

    RecyclerView rcv_;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registos);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();


        rcv_ = findViewById(R.id.recyclerView);
        rcv_.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new MyAdapter(this, getMyList());
        rcv_.setAdapter(myAdapter);
    }

    private ArrayList<Model> getMyList()
    {
        int qtdGas = 0;
        ArrayList<Model> models = new ArrayList<>();

        SQLiteDatabase GasMediaDB = openOrCreateDatabase("GasMediaDB", MODE_PRIVATE, null);

        //Obter qtd de itens para fazer uma verificacao
        Cursor countSet = GasMediaDB.rawQuery("select count(*) from GasCount", null);
        countSet.moveToFirst();

            qtdGas = countSet.getInt(0);

        if(qtdGas > 0) {

            Cursor resultSet = GasMediaDB.rawQuery("Select * from GasCount order by GasID desc", null);
            resultSet.moveToFirst();

            //Fazer para o primeiro registo
            Model m1 = new Model();
            m1.setID(resultSet.getString(0));
            m1.setDist(getText(R.string.lbl_dist) + " " + String.format("%.2f", resultSet.getDouble(1)) + " Km");
            m1.setGas(getText(R.string.lbl_gas) + " " + String.format("%.2f", resultSet.getDouble(2)) + " l");
            m1.setPreco(getText(R.string.lbl_preco) + " " + String.format("%.2f", resultSet.getDouble(3)) + " €");
            m1.setMedia(String.format("%.1f", resultSet.getDouble(4)));

            models.add(m1);

            //Inserir o resto
            while (resultSet.moveToNext()) {
                Model m = new Model();
                m.setID(resultSet.getString(0));
                m.setDist(getText(R.string.lbl_dist) + " " + String.format("%.2f", resultSet.getDouble(1)) + " Km");
                m.setGas(getText(R.string.lbl_gas) + " " + String.format("%.2f", resultSet.getDouble(2)) + " l");
                m.setPreco(getText(R.string.lbl_preco) + " " + String.format("%.2f", resultSet.getDouble(3)) + " €");
                m.setMedia(String.format("%.1f", resultSet.getDouble(4)));

                models.add(m);
            }
        }
        else
            Toast.makeText(getApplicationContext(), R.string.ToastQtdError, Toast.LENGTH_LONG).show();

        return models;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1)
        {
            if(resultCode == RESULT_OK)
            {
                Toast.makeText(getApplicationContext(), getText(R.string.SucRemoved), Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), getText(R.string.BadRemoved), Toast.LENGTH_SHORT).show();
            }
            myAdapter = new MyAdapter(this, getMyList());
            rcv_.setAdapter(myAdapter);

        }
    }
}