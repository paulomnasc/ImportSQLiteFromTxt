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
import java.io.BufferedReader;
import java.io.FileReader;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private String tableName ="tb_ano";
    private String columns = "id, descricao";

    private void ImportarTxt()
    {


        String fileName = "D:\\Users\\cblna\\Documents\\Paulo\\Capacitacao\\AndroidStudio\\Data\\anosEM.CSV";
        try
        {
            FileReader file = new FileReader(fileName);
            BufferedReader buffer = new BufferedReader(file);
            String line = "";



            db = SQLiteDatabase.openOrCreateDatabase("StudyApp", null ,null);

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
            Log.w(TAG, "ImportarTxt: ", ex);
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnImportar = (Button) findViewById(R.id.btnImport);

        Button btnListar = (Button) findViewById(R.id.btnListar);

        btnImportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImportarTxt();

            }
        });

        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor cr = db.rawQuery("SELECT descricao FROM " + tableName , null );
                cr.moveToFirst();
                while (cr!= null)
                {

                }


            }
        });


    }
}