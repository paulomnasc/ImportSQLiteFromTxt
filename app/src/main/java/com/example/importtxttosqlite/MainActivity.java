package com.example.importtxttosqlite;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private String tableName ="tb_ano";
    private String columns = "id, descricao";

    private ArrayAdapter<String> itensAdaptador;
    private ArrayList<Integer> ids;
    private ArrayList<String> descricoes;

    private ListView listaAnos;

    MainActivity mContext = this;

    private void ImportarAnos()
    {


        //String fileName = "D:\\Users\\cblna\\Documents\\Paulo\\github\\ImportSQLiteFromTxt\\Data\\anosEM.CSV";
        String fileName = "anosEM.CSV";
        try
        {
            //FileReader file = new FileReader(fileName);

            InputStreamReader file = new InputStreamReader(getAssets()
                    .open(fileName));

            BufferedReader buffer = new BufferedReader(file);
            String line = "";


            File dbpath = mContext.getDatabasePath("StudyApp");

            if (!dbpath.getParentFile().exists()) {
                dbpath.getParentFile().mkdirs();
            }

            db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);

            db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + "(id INTEGER PRIMARY KEY AUTOINCREMENT, descricao VARCHAR(255) NOT NULL)");


            String str1 = "INSERT INTO " + tableName + " (descricao) values (";
            String str2 = ");";

            db.beginTransaction();
            while ((line = buffer.readLine()) != null) {
                StringBuilder sb = new StringBuilder(str1);
                String[] str = line.split(";");
                /* sb.append("'" + str[0] + "',");
                sb.append(str[1] + "',");
                sb.append(str[2] + "',");
                sb.append(str[3] + "'");
                sb.append(str[4] + "'");*/

                sb.append("'" + str[0] + "'");
                sb.append(str2);
                db.execSQL(sb.toString());
            }
            db.setTransactionSuccessful();
            db.endTransaction();

        }
        catch(Exception ex)
        {
            Log.i("Erro: ", "ImportarAnos: ", ex);
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listaAnos = (ListView) findViewById(R.id.lstAnos);

        ListarAnos();



    }

    private void ListarAnos() {
        try {


            File dbpath = mContext.getDatabasePath("StudyApp");

            if (!dbpath.getParentFile().exists()) {
                dbpath.getParentFile().mkdirs();
            }


            db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);

            Cursor cr = db.rawQuery("SELECT id, descricao FROM " + tableName , null );

            if(cr==null)
                ImportarAnos();

            int indColId = cr.getColumnIndex("id");
            int indColDesc = cr.getColumnIndex("descricao");

            descricoes = new ArrayList<String>();
            ids = new ArrayList<Integer>();
            itensAdaptador= new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_2
                    , android.R.id.text1
                    , descricoes);

            listaAnos.setAdapter(itensAdaptador);


            cr.moveToFirst();
            do
            {
                Log.i("Logx", "ID" + cr.getString(indColId));
                ids.add(cr.getInt(indColId));
                Log.i("Logx", "DESCRICAO" + cr.getString(indColDesc));
                descricoes.add(cr.getString(indColDesc));
                cr.moveToNext();
                if(cr.isLast()) {
                    Log.i("Logx", "ID" + cr.getString(indColId));
                    ids.add(cr.getInt(indColId));
                    Log.i("Logx", "DESCRICAO" + cr.getString(indColDesc));
                    descricoes.add(cr.getString(indColDesc));
                }

            }while (!cr.isLast());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}