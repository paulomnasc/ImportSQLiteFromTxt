package com.studyapp.importtxttosqlite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.navigation.ui.AppBarConfiguration;

import com.studyapp.importtxttosqlite.R;

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
    private Button btnVoltar;
    private EditText txtDisciplina;

    private int idDisciplina;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String idConteudo;
        setContentView(R.layout.activity_act_disciplinas);

        btnAvancar = (Button) findViewById(R.id.btnAvancar);
        btnVoltar = (Button) findViewById(R.id.btnVoltar);
        txtDisciplina= (EditText)findViewById(R.id.txtDisciplina);

        listaDisciplinas = (ListView) findViewById(R.id.lstDisciplinas);

        Intent intent= this.getIntent();
        idConteudo = intent.getStringExtra("idConteudo");
        idAno = intent.getStringExtra("idAno");

        ListarDisciplinas(idAno, idConteudo);

        txtDisciplina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        txtDisciplina.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listaDisciplinas.setVisibility(View.INVISIBLE);
                btnAvancar.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        /*btnAvancar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FormMoveNext();
            }

            private void FormMoveNext() {

                Intent switchActivityIntent = new Intent(actDisciplinas.this, actConteudos.class);
                switchActivityIntent.putExtra("idAno", idAno.toString());
                startActivity(switchActivityIntent);

            }

        });
*/

        listaDisciplinas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                dsDisciplina = descricoes.get(i);
                idDisciplina = ids.get(i);
                btnAvancar.setEnabled(true);

            }
        });

        btnAvancar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FormMoveNext();
            }

            private void FormMoveNext() {


                if(txtDisciplina.getText().length() >0)
                    dsDisciplina = txtDisciplina.getText().toString();

                /* Avançar para Conteudos */
                Intent switchActivityIntent = new Intent(actDisciplinas.this, actSelecQuestoes.class);
                switchActivityIntent.putExtra("dsDisciplina", dsDisciplina.toString());
                switchActivityIntent.putExtra("idDisciplina", idDisciplina);
                switchActivityIntent.putExtra("idAno", idAno.toString());
                switchActivityIntent.putExtra("idConteudo", idConteudo.toString());
                startActivity(switchActivityIntent);

            }


        });

        btnVoltar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FormMovePrevious();
            }


            private void FormMovePrevious() {

                /* Retornar para Lista de Anos */
                Intent switchActivityIntent = new Intent(actDisciplinas.this, actConteudos.class);
                switchActivityIntent.putExtra("idAno", idAno.toString());
                switchActivityIntent.putExtra("idConteudo","");
                startActivity(switchActivityIntent);

            }


        });

    }





    private void ListarDisciplinas(String idAno, String idConteudo) {
        try {


            File dbpath = mContext.getDatabasePath("StudyApp");

            if (!dbpath.getParentFile().exists()) {
                dbpath.getParentFile().mkdirs();
            }


            db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
            db.execSQL("PRAGMA encoding = 'UTF-8'");
            Cursor cr = db.rawQuery("SELECT id, descricao_subitem FROM "
                    + tableName + " WHERE id_ano = " + idAno
                    + " AND id_conteudo = '" + idConteudo + "'", null );

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