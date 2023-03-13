package com.example.importtxttosqlite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.importtxttosqlite.databinding.ActivityActDisciplinasBinding;

import java.io.File;
import java.util.ArrayList;

public class actDisciplinas extends AppCompatActivity {

    private ListView listaDisciplinas;
    private AppBarConfiguration appBarConfiguration;

    private ArrayAdapter<String> itensAdaptador;
    private ArrayList<Integer> ids;
    private ArrayList<String> descricoes;
    private String dsDisciplina;
    private String idAno;

    private SQLiteDatabase db;
    actDisciplinas mContext = this;
    private String tableName ="tb_disciplinas";

    private Button btnAvancar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_act_disciplinas);

        btnAvancar = (Button) findViewById(R.id.btnAvancar);

        listaDisciplinas = (ListView) findViewById(R.id.lstDisciplinas);

        //setSupportActionBar(binding.toolbar);

        Intent intent= this.getIntent();
        dsDisciplina = intent.getStringExtra("dsConteudo");
        idAno = intent.getStringExtra("idAno");

        ListarDisciplinas(idAno, dsDisciplina);

        btnAvancar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                switchActivities();
            }

            private void switchActivities() {

                /* Avançar para Conteudos */
                //Intent switchActivityIntent = new Intent(actDisciplinas.this, actConteudos.class);
                //switchActivityIntent.putExtra("idDisciplina", idDisciplina.toString());
                //startActivity(switchActivityIntent);

            }

        });

    }



    /*@Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_act_disciplinas);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }*/

    private void ListarDisciplinas(String idAno, String dsConteudo) {
        try {


            File dbpath = mContext.getDatabasePath("StudyApp");

            if (!dbpath.getParentFile().exists()) {
                dbpath.getParentFile().mkdirs();
            }


            db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);

            Cursor cr = db.rawQuery("SELECT id, descricao_subitem FROM "
                    + tableName + " WHERE id_ano = " + idAno
                    + " AND descricao = '" + dsConteudo + "'", null );

            //if(cr.getCount() == 0)
           //     ImportarAnos();

            int indColId = cr.getColumnIndex("id");
            int indColDesc = cr.getColumnIndex("descricao_subitem");

            descricoes = new ArrayList<String>();
            ids = new ArrayList<Integer>();
            itensAdaptador= new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_2
                    , android.R.id.text1
                    , descricoes);

            listaDisciplinas.setAdapter(itensAdaptador);

            if(cr.getCount() == 0){
                Log.i("Logx", "A consulta não retornou registros");
                return;
            }

            cr.moveToFirst();
            for(int i = 0; i < cr.getCount();i++)
            {
                Log.i("Logx", "ID" + cr.getInt(indColId));
                ids.add(cr.getInt(indColId));
                Log.i("Logx", "DESCRICAO" + cr.getString(indColDesc));
                descricoes.add(cr.getString(indColDesc));
                cr.moveToNext();
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}