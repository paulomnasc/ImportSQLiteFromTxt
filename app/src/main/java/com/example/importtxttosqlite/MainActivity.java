package com.example.importtxttosqlite;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    private Button btnRecriar;

    private Button btnAvancar;

    private Button btnImpDisciplinas;

    MainActivity mContext = this;

    private Integer idSelecionado;


    private void importarDisciplinas(int idAno, int ano)
    {

        tableName = "tb_disciplinas";

        String fileName = "Disciplicinas" + ano + "EM.CSV";
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

            db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT "
                    + ", descricao VARCHAR(255) NOT NULL"
                    + ", descricao_subitem VARCHAR(255) NOT NULL"
                    + ", id_ano INTEGER NOT NULL,  FOREIGN KEY(id_ano) REFERENCES tb_ano(id))");



            String str1 = "INSERT INTO " + tableName + " (descricao, id_ano, descricao_subitem) values (";
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

                sb.append("'" + str[1] + "', ");
                sb.append("'" + str[0] + "', ");
                sb.append("'" + str[2] + "')");
                db.execSQL(sb.toString());

            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            file.close();

        }
        catch(Exception ex)
        {
            Log.i("Erro: ", "ImportarAnos: ", ex);
            db.close();

        }


    }


    private void importarConteudos()
    {

        tableName = "tb_conteudos";

        String fileName = "DisciplicinasEM.CSV";
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

            db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT "
                    + ", descricao VARCHAR(255) NOT NULL"
                    + ")");



            String str1 = "INSERT INTO " + tableName + " (descricao) values (";
            String str2 = ");";

            db.beginTransaction();

            while ((line = buffer.readLine()) != null) {
                StringBuilder sb = new StringBuilder(str1);
                //String[] str = line.split(";");
                sb.append("'" + line + "')");
                /*sb.append(str[1] + "',");
                sb.append(str[2] + "',");
                sb.append(str[3] + "'");
                sb.append(str[4] + "'");*/


                db.execSQL(sb.toString());

            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            file.close();

        }
        catch(Exception ex)
        {
            Log.i("Erro: ", "ImportarAnos: ", ex);
            db.close();

        }


    }

    private void ImportarAnos()
    {


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
            db.close();
            file.close();

        }
        catch(Exception ex)
        {
            Log.i("Erro: ", "ImportarAnos: ", ex);
            db.close();

        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listaAnos = (ListView) findViewById(R.id.lstAnos);
        btnRecriar = (Button) findViewById(R.id.btnRecreate);
        btnAvancar = (Button) findViewById(R.id.btnAvancar);
        btnImpDisciplinas = (Button) findViewById(R.id.btnImportDisciplinas);

        listaAnos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                idSelecionado = ids.get(i);
                btnAvancar.setEnabled(true);

            }
        });

        ListarAnos();


        btnImpDisciplinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Comentado para não regerar os id´s, pois daria erro*/
                ArrayList<Integer> arlAnos = ListarAnos();
                for(int i = 0 ; i<= arlAnos.size(); i++)
                {
                    importarDisciplinas(arlAnos.get(i),i+1);
                    i++;
                }

                //importarConteudos();
            }
        });



        btnAvancar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                switchActivities();
            }

            private void switchActivities() {

                /* Avançar para Conteudos */
                Intent switchActivityIntent = new Intent(MainActivity.this, actConteudos.class);
                switchActivityIntent.putExtra("idSelecionado", idSelecionado.toString());
                startActivity(switchActivityIntent);

                /* Avançar para Disciplinas
                Intent switchActivityIntent = new Intent(MainActivity.this, actDisciplinas.class);
                switchActivityIntent.putExtra("idSelecionado", idSelecionado.toString());
                startActivity(switchActivityIntent);*/
            }

        });

        btnRecriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File dbpath = mContext.getDatabasePath("StudyApp");

                if (!dbpath.getParentFile().exists()) {
                    dbpath.getParentFile().mkdirs();
                }


                try {
                    db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);

                    /*db.execSQL("DELETE FROM " + "tb_ano");
                    db.close();*/
                    db.execSQL("DROP TABLE tb_disciplinas");
                    //db.execSQL("DELETE FROM " + "tb_disciplinas");
                    db.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }


    private ArrayList<Integer> ListarAnos() {
        try {


            File dbpath = mContext.getDatabasePath("StudyApp");

            if (!dbpath.getParentFile().exists()) {
                dbpath.getParentFile().mkdirs();
            }


            db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);

            Cursor cr = db.rawQuery("SELECT id, descricao FROM " + tableName , null );

            /*if(cr.getCount() == 0)
                ImportarAnos();*/

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
        catch(Exception ex) {
            ex.printStackTrace();
        }

        return ids;
    }



}