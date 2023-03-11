package com.example.importtxttosqlite;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private String tableName ="tb_ano";
    private String columns = "id, descricao";


    MainActivity mContext = this;

    private void ImportarTxt()
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
            Log.i("Erro: ", "ImportarTxt: ", ex);
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnImportar = (Button) findViewById(R.id.btnImport);
        Button btnListar = (Button) findViewById(R.id.btnListar);
        ListView listaAnos = (ListView) findViewById(R.id.lstAnos);


        btnImportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImportarTxt();

            }
        });

        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {


                    File dbpath = mContext.getDatabasePath("StudyApp");

                    if (!dbpath.getParentFile().exists()) {
                        dbpath.getParentFile().mkdirs();
                    }


                    db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);

                    Cursor cr = db.rawQuery("SELECT descricao FROM " + tableName , null );
                    int indColDesc = cr.getColumnIndex("descricao");
                    cr.moveToFirst();

                    while (cr != null)
                    {
                        Log.i("Logx", "DESCRICAO" + cr.getString(indColDesc));
                        cr.moveToNext();
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }




            }
        });


    }
}