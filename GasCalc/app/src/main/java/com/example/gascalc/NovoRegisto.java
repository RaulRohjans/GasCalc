package com.example.gascalc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class NovoRegisto extends AppCompatActivity {

    Button btn_add;
    TextInputEditText txt_kmInic;
    TextInputEditText txt_kmFin;
    TextInputEditText txt_km;
    TextInputEditText txt_gas;
    TextInputEditText txt_preco;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_registo);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        btn_add = findViewById(R.id.btn_add);
        txt_kmInic = findViewById(R.id.txt_kmInic);
        txt_kmFin = findViewById(R.id.txt_kmFin);
        txt_km = findViewById(R.id.txt_km);
        txt_gas = findViewById(R.id.txt_gas);
        txt_preco = findViewById(R.id.txt_preco);

        if (savedInstanceState != null)
        {
            txt_kmInic.setText(savedInstanceState.getString("kmInic"));
            txt_kmFin.setText(savedInstanceState.getString("kmFin"));
            txt_km.setText(savedInstanceState.getString("km"));
            txt_gas.setText(savedInstanceState.getString("gas"));
            txt_preco.setText(savedInstanceState.getString("preco"));
        }

        btn_add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean erro0 = false, erroNeg = false;
                        if(txt_gas.getText().toString().isEmpty() == false && txt_preco.getText().toString().isEmpty() == false && (txt_km.getText().toString().isEmpty() == false || (txt_kmFin.getText().toString().isEmpty() == false && txt_kmInic.getText().toString().isEmpty() == false)))
                        {
                            if(txt_kmFin.getText().toString().isEmpty() == false && txt_kmInic.getText().toString().isEmpty() == false)
                            {
                                if(Double.parseDouble(txt_kmInic.getText().toString()) <= 0 || Double.parseDouble(txt_kmFin.getText().toString()) <= 0 || Double.parseDouble(txt_gas.getText().toString()) <= 0)
                                    erro0 = true;
                            }
                            else
                            {
                                if(txt_km.getText().toString().isEmpty() == false)
                                {
                                    if(Double.parseDouble(txt_km.getText().toString()) <= 0 || Double.parseDouble(txt_gas.getText().toString()) <= 0)
                                        erro0 = true;
                                }
                            }

                            if(txt_kmInic.getText().toString().isEmpty() == false && txt_kmFin.getText().toString().isEmpty() == false)
                            {
                                if(txt_km.getText().toString().isEmpty())
                                {
                                    if(Double.parseDouble(txt_kmFin.getText().toString()) <= Double.parseDouble(txt_kmInic.getText().toString()))
                                        erroNeg = true;
                                }

                            }
                            if(erro0 == false) {
                                if (erroNeg == false) {

                                    SQLiteDatabase GasMediaDB = openOrCreateDatabase("GasMediaDB", MODE_PRIVATE, null);
                                    GasMediaDB.execSQL("CREATE TABLE IF NOT EXISTS GasCount(GasID Integer primary key, Dist Real, Gas Real, Preco Real, Media Real)");
                                    MediaGas mediaGas = new MediaGas();

                                    //Ver qtd de registos
                                    Cursor resultSet = GasMediaDB.rawQuery("Select count(*) from GasCount", null);
                                    resultSet.moveToFirst();
                                    int count = Integer.parseInt(resultSet.getString(0));
                                    if (count == 0)
                                        mediaGas.setID(1);
                                    else {
                                        resultSet = GasMediaDB.rawQuery("Select GasID from GasCount order by GasID desc limit 1", null);
                                        resultSet.moveToFirst();
                                        mediaGas.setID(resultSet.getInt(0) + 1);
                                    }


                                    if (!txt_km.getText().toString().isEmpty()) {
                                        double km = Double.parseDouble(txt_km.getText().toString());
                                        mediaGas.setDistancia(km);
                                        mediaGas.setGas(Double.parseDouble(txt_gas.getText().toString()));
                                        mediaGas.setPreco(Double.parseDouble(txt_preco.getText().toString()));
                                        mediaGas.setMedia((Double.parseDouble(txt_gas.getText().toString()) * 100) / km);
                                    } else {
                                        double kmInic = Double.parseDouble(txt_kmInic.getText().toString());
                                        double kmFin = Double.parseDouble(txt_kmFin.getText().toString());

                                        mediaGas.setDistancia(kmFin - kmInic);
                                        mediaGas.setGas(Double.parseDouble(txt_gas.getText().toString()));
                                        mediaGas.setPreco(Double.parseDouble(txt_preco.getText().toString()));
                                        mediaGas.setMedia((Double.parseDouble(txt_gas.getText().toString()) * 100) / (kmFin - kmInic));
                                    }

                                    GasMediaDB.execSQL("Insert into GasCount values('" + mediaGas.getID() + "', '" + mediaGas.getDistancia() + "', '" + mediaGas.getGas() + "', '" + mediaGas.getPreco() + "', '" + mediaGas.getMedia() + "')");

                                    txt_km.setText("");
                                    txt_kmInic.setText("");
                                    txt_kmFin.setText("");
                                    txt_preco.setText("");
                                    txt_gas.setText("");
                                } else
                                    Toast.makeText(getApplicationContext(), R.string.NovoErroNegativoToast, Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(getApplicationContext(), R.string.NovoErroToast0, Toast.LENGTH_SHORT).show();

                        }
                        else
                            Toast.makeText(getApplicationContext(), R.string.NovoErrorToast, Toast.LENGTH_SHORT).show();

                    }
                }
        );
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("kmInic", txt_kmInic.getText().toString());
        savedInstanceState.putString("kmFin", txt_kmFin.getText().toString());
        savedInstanceState.putString("km", txt_km.getText().toString());
        savedInstanceState.putString("gas", txt_gas.getText().toString());
        savedInstanceState.putString("preco", txt_preco.getText().toString());
    }
}