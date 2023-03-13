package com.example.importtxttosqlite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.File;
import java.util.ArrayList;

public class actConteudos extends AppCompatActivity {

    private ListView listaDisciplinas;
    private AppBarConfiguration appBarConfiguration;

    private ArrayAdapter<String> itensAdaptador;
    private ArrayList<Integer> ids;
    private ArrayList<String> descricoes;
    private String idSelecionado;

    private SQLiteDatabase db;
    actConteudos mContext = this;
    private String tableName ="tb_conteudos";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_act_disciplinas);

        listaDisciplinas = (ListView) findViewById(R.id.lstDisciplinas);

        //setSupportActionBar(binding.toolbar);

        Intent intent= this.getIntent();
        idSelecionado = intent.getStringExtra("idSelecionado");

        ListarConteudos();

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_act_disciplinas);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void ListarConteudos() {
        try {


            File dbpath = mContext.getDatabasePath("StudyApp");

            if (!dbpath.getParentFile().exists()) {
                dbpath.getParentFile().mkdirs();
            }


            db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);

            Cursor cr = db.rawQuery("SELECT id, descricao FROM "
                    + tableName  , null );

            //if(cr.getCount() == 0)
           //     ImportarAnos();

            int indColId = cr.getColumnIndex("id");
            int indColDesc = cr.getColumnIndex("descricao");

            descricoes = new ArrayList<String>();
            ids = new ArrayList<Integer>();
            itensAdaptador= new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_2
                    , android.R.id.text1
                    , descricoes);

            listaDisciplinas.setAdapter(itensAdaptador);


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
    }

}