package com.example.importtxttosqlite;

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
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class actAnos extends AppCompatActivity {

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

    actAnos mContext = this;

    private Integer idSelecionado;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_anos);


        listaAnos = (ListView) findViewById(R.id.lstAnos);
        btnRecriar = (Button) findViewById(R.id.btnRecreate);
        btnAvancar = (Button) findViewById(R.id.btnAvancar);

        listaAnos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                idSelecionado = ids.get(i);
                btnAvancar.setEnabled(true);

            }
        });

        ListarAnos();



        btnAvancar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                switchActivities();
            }

            private void switchActivities() {

                /* Avançar para Conteudos */
                Intent switchActivityIntent = new Intent(actAnos.this, actConteudos.class);
                switchActivityIntent.putExtra("idAno", idSelecionado.toString());
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

                Imports importador = new Imports();
                importador.setmContex(mContext);
                importador.ImportarDados();


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
            db.execSQL("PRAGMA encoding = 'UTF-8'");
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