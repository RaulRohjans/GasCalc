package com.example.gascalc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.loader.content.CursorLoader;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class settings extends AppCompatActivity {

    ListView lsv_;
    String mTitles[] = new String[2];;
    String mDescription[] = new String[2];;
    int Imgs[] = new int[2];

    String xmlReaderFile = "";

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();
        context = this;

        mTitles[0] = getText(R.string.ImportTitle).toString();
        mTitles[1] = getText(R.string.ExportTitle).toString();
        mDescription[0] = getText(R.string.ImportDesc).toString();
        mDescription[1] = getText(R.string.ExportDesc).toString();
        Imgs[0] = R.drawable.import_db;
        Imgs[1] = R.drawable.export_db;

        lsv_ = findViewById(R.id.lsv_settings);
        MyAdapter adapter = new MyAdapter(this, mTitles, mDescription, Imgs);
        lsv_.setAdapter(adapter);

    }

    class MyAdapter extends ArrayAdapter<String>
    {
        Context context;
        String Title[];
        String Desc[];
        int Imgs[];

        MyAdapter(Context c, String title[], String desc[], int imgs[])
        {
            super(c, R.layout.lsv_settings_item, R.id.txt_lsvTitle, title);
            this.context = c;
            this.Title = title;
            this.Desc = desc;
            this.Imgs = imgs;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View lsv_item = layoutInflater.inflate(R.layout.lsv_settings_item, parent, false);
            ImageView images = lsv_item.findViewById(R.id.img_settings);
            TextView txtTitle = lsv_item.findViewById(R.id.txt_lsvTitle);
            TextView txtDesc = lsv_item.findViewById(R.id.txt_lsvDesc);

            images.setImageResource(Imgs[position]);
            txtTitle.setText(Title[position]);
            txtDesc.setText(Desc[position]);

            lsv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position == 0) //Importar
                    {
                        new AlertDialog.Builder(context).setTitle(getText(R.string.ImportarAvisoTitulo)).setMessage(getText(R.string.ImportarAvisoDesc)).setPositiveButton(getText(R.string.LeaveYes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                //Obter localizacao do ficheiro

                                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                                i.setType("text/xml");
                                i.addCategory(Intent.CATEGORY_OPENABLE);

                                try {
                                    startActivityForResult(i, 58);
                                } catch (android.content.ActivityNotFoundException ex) {
                                    // Potentially direct the user to the Market with a Dialog
                                    Toast.makeText(getApplicationContext(), getText(R.string.NoFileApp), Toast.LENGTH_SHORT).show();
                                }


                            }
                        }).setNegativeButton(getText(R.string.LeaveNo), null).create().show();
                    }
                    else
                    {
                        if(position == 1) //Exportar
                        {
                            try {
                                int qtdGas = 0;
                                //Abrir base de dados
                                SQLiteDatabase GasMediaDB = openOrCreateDatabase("GasMediaDB", MODE_PRIVATE, null);

                                //Obter qtd de itens para fazer uma verificacao
                                Cursor resultSet = GasMediaDB.rawQuery("select count(*) from GasCount", null);
                                resultSet.moveToFirst();

                                qtdGas = resultSet.getInt(0);

                                //ver se existem itens na tabela
                                if(qtdGas > 0) {
                                    resultSet = GasMediaDB.rawQuery("select * from GasCount", null);
                                    resultSet.moveToFirst();

                                    final String xmlFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "//" + "GasCalc" + "//" + "GasMediaDB" + ".xml";

                                    //Criar a pasta da app
                                    File folder = new File(Environment.getExternalStorageDirectory() + "//GasCalc");

                                    //verificar se a pasta existe e criala se n for o caso
                                    if(!folder.exists())
                                    {
                                        folder.mkdir();
                                    }

                                    //Criar o ficheiro
                                    FileOutputStream fos = new FileOutputStream(new File(xmlFileName));

                                    //Abrir o ficheiro criado e inicializar o writer
                                    XmlSerializer xmlSerializer = Xml.newSerializer();
                                    StringWriter writer = new StringWriter();
                                    xmlSerializer.setOutput(writer);

                                    //Comecar a escrever no documento
                                    xmlSerializer.startDocument("UTF-8", true);
                                    xmlSerializer.startTag(null, "GasCounts");

                                    //Fazer o primeiro registo
                                    xmlSerializer.startTag(null, "GasCount");

                                    xmlSerializer.startTag(null, "GasID");
                                    xmlSerializer.text(resultSet.getString(0));
                                    xmlSerializer.endTag(null, "GasID");

                                    xmlSerializer.startTag(null, "Dist");
                                    xmlSerializer.text(resultSet.getString(1));
                                    xmlSerializer.endTag(null, "Dist");

                                    xmlSerializer.startTag(null, "Gas");
                                    xmlSerializer.text(resultSet.getString(2));
                                    xmlSerializer.endTag(null, "Gas");

                                    xmlSerializer.startTag(null, "Preco");
                                    xmlSerializer.text(resultSet.getString(3));
                                    xmlSerializer.endTag(null, "Preco");

                                    xmlSerializer.startTag(null, "Media");
                                    xmlSerializer.text(resultSet.getString(4));
                                    xmlSerializer.endTag(null, "Media");

                                    xmlSerializer.endTag(null, "GasCount");

                                    while(resultSet.moveToNext())
                                    {
                                        xmlSerializer.startTag(null, "GasCount");

                                        xmlSerializer.startTag(null, "GasID");
                                        xmlSerializer.text(resultSet.getString(0));
                                        xmlSerializer.endTag(null, "GasID");

                                        xmlSerializer.startTag(null, "Dist");
                                        xmlSerializer.text(resultSet.getString(1));
                                        xmlSerializer.endTag(null, "Dist");

                                        xmlSerializer.startTag(null, "Gas");
                                        xmlSerializer.text(resultSet.getString(2));
                                        xmlSerializer.endTag(null, "Gas");

                                        xmlSerializer.startTag(null, "Preco");
                                        xmlSerializer.text(resultSet.getString(3));
                                        xmlSerializer.endTag(null, "Preco");

                                        xmlSerializer.startTag(null, "Media");
                                        xmlSerializer.text(resultSet.getString(4));
                                        xmlSerializer.endTag(null, "Media");

                                        xmlSerializer.endTag(null, "GasCount");
                                    }

                                    //Fechar a tag final e dar close no writer e file stream
                                    xmlSerializer.endTag(null, "GasCounts");
                                    xmlSerializer.endDocument();
                                    xmlSerializer.flush();
                                    String dataWrite = writer.toString();
                                    fos.write(dataWrite.getBytes());
                                    fos.close();

                                    Toast.makeText(getApplicationContext(), getText(R.string.ToastImportSucess), Toast.LENGTH_SHORT).show();
                                }
                                else
                                    Toast.makeText(getApplicationContext(), getText(R.string.ToastExportErrorQtd), Toast.LENGTH_SHORT).show();

                            }
                            catch (Exception ee)
                            {
                                Toast.makeText(getApplicationContext(), getText(R.string.ToastExportError), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            });
            return lsv_item;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 58)
        {
            if(resultCode != RESULT_CANCELED)
            {
                Uri FilePathURI = data.getData();
                try {
                    xmlReaderFile = UriUtils.getPathFromUri(this, FilePathURI);
                }
                catch (Exception ee)
                {
                    xmlReaderFile = FilePathURI.getPath();
                }

                String dbg = xmlReaderFile.substring(xmlReaderFile.length() - 14, xmlReaderFile.length());

                if(!xmlReaderFile.isEmpty() && dbg.equals("GasMediaDB.xml")) {
                    try {
                        int qtdGas = 0;

                        //Abrir base de dados
                        SQLiteDatabase GasMediaDB = openOrCreateDatabase("GasMediaDB", MODE_PRIVATE, null);

                        //Obter qtd de itens para fazer uma verificacao
                        Cursor resultSet = GasMediaDB.rawQuery("select count(*) from GasCount", null);
                        resultSet.moveToFirst();

                        qtdGas = resultSet.getInt(0);

                        //Ler o ficheiro

                        ArrayList<String> userData = new ArrayList<String>();

                        //FileInputStream fis1 = getApplicationContext().openFileInput();
                        FileInputStream fis1 = new FileInputStream(new File(xmlReaderFile));
                        InputStreamReader isr = new InputStreamReader(fis1);
                        char[] inputBuffer = new char[fis1.available()];
                        isr.read(inputBuffer);
                        String dataa = new String(inputBuffer);
                        isr.close();
                        fis1.close();

                        XmlPullParserFactory factory = null;
                        factory = XmlPullParserFactory.newInstance();
                        factory.setNamespaceAware(true);
                        XmlPullParser xpp = null;

                        xpp = factory.newPullParser();
                        xpp.setInput(new StringReader(dataa));

                        int eventType = 0;

                        eventType = xpp.getEventType();

                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if (eventType == XmlPullParser.START_DOCUMENT) {
                                System.out.println("Start document");
                            } else if (eventType == XmlPullParser.START_TAG) {
                                System.out.println("Start tag " + xpp.getName());
                            } else if (eventType == XmlPullParser.END_TAG) {
                                System.out.println("End tag " + xpp.getName());
                            } else if (eventType == XmlPullParser.TEXT) {
                                userData.add(xpp.getText());
                            }
                            eventType = xpp.next();
                        }

                        //Eliminar todos os itens da base de dados
                        if (qtdGas > 0) {

                            GasMediaDB.execSQL("delete from GasCount");
                        }

                        //Inserir dados na bd
                        for(int i = 0; i < userData.size(); i+=5)
                        {
                            GasMediaDB.execSQL("Insert into GasCount values('" + userData.get(i) + "', '" + userData.get(i+1) + "', '" + userData.get(i+2) + "', '" + userData.get(i+3) + "', '" + userData.get(i+4) + "')");
                        }

                        Toast.makeText(getApplicationContext(), getText(R.string.ToastImportSucess), Toast.LENGTH_SHORT).show();

                    } catch (Exception ee) {
                        Toast.makeText(getApplicationContext(), getText(R.string.ToastImportError), Toast.LENGTH_SHORT).show();
                    }

                }
                else
                    Toast.makeText(getApplicationContext(), getText(R.string.ToastNameError), Toast.LENGTH_SHORT).show();
            }

        }
    }
}