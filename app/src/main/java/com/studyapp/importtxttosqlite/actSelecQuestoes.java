package com.studyapp.importtxttosqlite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;


import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.studyapp.importtxttosqlite.R;

import java.io.File;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;


public class actSelecQuestoes extends BaseAPIActivity {

    private String idAno;
    private String idConteudo;

    private String question;

    private String tableName ="tb_perguntas_disciplinas";

    private ArrayAdapter<String> questionsAdaptador;
    private ArrayList<String> questions;

    private ListView listaQuestions;

    private String dsDisciplina;
    private Button btnVoltar;
    private Button btnAvancar;
    private TextView txtAssunto;

    private ImageView iconLoad;

    private Handler handler;
    private String idDisciplina;
    private ArrayList descricoes;
    private ArrayList ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_selec_questoes);

        handler = new Handler();

        btnAvancar = (Button) findViewById(R.id.btnAvancar);
        btnVoltar = (Button) findViewById(R.id.btnVoltar);
        txtAssunto = (TextView)findViewById(R.id.txtAssunto);
        iconLoad = (GifImageView)findViewById(R.id.LoadingImage);

        listaQuestions = (ListView) findViewById(R.id.lstQuestions);

        Intent intent= this.getIntent();
        dsDisciplina = intent.getStringExtra("dsDisciplina");
        idDisciplina = intent.getStringExtra("idDisciplina");
        idAno = intent.getStringExtra("idAno");
        idConteudo = intent.getStringExtra("idConteudo");

        txtAssunto.setText(dsDisciplina);
        iconLoad.setVisibility(View.VISIBLE);

        //preencherQuestoes();
        ObterDuvidasFrequentes();

        btnAvancar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FormMoveNext();
            }

            private void FormMoveNext() {

                /* Avançar para Conteudos */
                Intent switchActivityIntent = new Intent(actSelecQuestoes.this, MainActivity.class);
                switchActivityIntent.putExtra("dsDisciplina", dsDisciplina);
                switchActivityIntent.putExtra("idAno", idAno.toString());
                switchActivityIntent.putExtra("idConteudo", idConteudo.toString());
                switchActivityIntent.putExtra("question", question.toString());
                startActivity(switchActivityIntent);

            }


        });

        listaQuestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                question = questions.get(i);
                btnAvancar.setEnabled(true);

            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FormMovePrevious();
            }


            private void FormMovePrevious() {


                Intent switchActivityIntent = new Intent(actSelecQuestoes.this, actDisciplinas.class);
                switchActivityIntent.putExtra("idAno", idAno.toString());
                switchActivityIntent.putExtra("idConteudo", idConteudo.toString());
                switchActivityIntent.putExtra("dsDisciplina", "");
                startActivity(switchActivityIntent);

            }


        });

    }


    /*
    Insere a lista de dúvidas frequentes de um id_disciplina
     */
    private void inserirDuvidasFrequentes(String descricao)
    {
        if (!descricao.equalsIgnoreCase("Failed to load response due to timeout")) {
            String str1 = "INSERT INTO tb_perguntas_disciplinas (descricao,id_disciplina) values (";

            File dbpath = this.getDatabasePath("StudyApp");

            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);

            db.beginTransaction();
            StringBuilder sb = new StringBuilder(str1);

            sb.append("'" + descricao + "', ");
            sb.append("'" + idDisciplina + "') ");
            db.execSQL(sb.toString());
            Log.i("Msg: ", "Importar: " + sb.toString());

            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
    }

    /*
    Lista as dúvidas frequentes de um id_disciplina
     */
    private int listarDuvidasFrequentesFromDB()
    {

        try {


            File dbpath = this.getDatabasePath("StudyApp");

            if (!dbpath.getParentFile().exists()) {
                dbpath.getParentFile().mkdirs();
            }

            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);


            db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
            db.execSQL("PRAGMA encoding = 'UTF-8'");
            Cursor cr = db.rawQuery("SELECT id, descricao, id_disciplina FROM tb_perguntas_disciplinas" , null );


            int indColId = cr.getColumnIndex("id");
            int indColDesc = cr.getColumnIndex("descricao");

            descricoes = new ArrayList<String>();
            ids = new ArrayList<Integer>();
            ArrayAdapter<String> itensAdaptador = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_2
                    , android.R.id.text1
                    , descricoes);


            listaQuestions.setAdapter(itensAdaptador);


            cr.moveToFirst();
            do
            {
                //Log.i("Logx", "ID" + cr.getString(indColId));
                ids.add(cr.getInt(indColId));
                //Log.i("Logx", "DESCRICAO" + cr.getString(indColDesc));
                descricoes.add(cr.getString(indColDesc));

                cr.moveToNext();
                if(cr.isLast()) {
                    //Log.i("Logx", "ID" + cr.getString(indColId));
                    ids.add(cr.getInt(indColId));
                    //Log.i("Logx", "DESCRICAO" + cr.getString(indColDesc));
                    descricoes.add(cr.getString(indColDesc));
                }

            }while (!cr.isLast());



        }
        catch(Exception ex) {
            ex.printStackTrace();
        }

        return 0;

    }


    private void ObterDuvidasFrequentes()
    {


        String question = "Cite as 10 dúvidas frequentes de " + txtAssunto.getText();


        int qtdLinhas = listarDuvidasFrequentesFromDB();
        if(qtdLinhas == 0) {
            callAPI(question);
        }

    }

    void addResponse(String response){

        new Thread(){
            public void run(){
                try{

                    messageList.remove(messageList.size()-1);
                    //addToChat(response,Message.SENT_BY_BOT);
                    txtResponse = response;

                }
                catch(Exception e)
                {
                    Log.i("ERRO: ", "run: " + e.getMessage());
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listarQuestoes();
                    }
                });
            }
        }.start();


    }

    private void listarQuestoes() {


        ArrayList<String> questions = preencherQuestoes();
        questionsAdaptador= new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_2
                , android.R.id.text1
                , questions);

        listaQuestions.setAdapter(questionsAdaptador);
    }

    private ArrayList<String> preencherQuestoes() {

        questions = new ArrayList<String>();

        listFromAPI();

        question = "Faça um resumo sobre " + txtAssunto.getText();
        questions.add(question);
        question = "Me dê exemplos de uso de " + txtAssunto.getText();
        questions.add(question);
        question = "Me dê exemplos de situações reais com frases, fórmulas ou algoritmos de " + txtAssunto.getText();
        questions.add(question);
        question = "Elabore 10 exercícios de fixação sobre " + txtAssunto.getText();
        questions.add(question);
        question = "Formule 5 exercícios de fixação sobre " + txtAssunto.getText() + " com resolução.";
        questions.add(question);

        iconLoad.setVisibility(View.INVISIBLE);


        return questions;


    }

    private void listFromAPI() {

        /*Esta questão será buscada diretamente do chat gpt
        question = "Cite as 10 dúvidas frequentes de " + txtAssunto.getText();
        */

        String[] vetorQuestoesFrequentes = txtResponse.split("\n");
        if(vetorQuestoesFrequentes != null)
        {
            for (String item: vetorQuestoesFrequentes) {
                questions.add(item);
                inserirDuvidasFrequentes(item);
            }
        }
    }


}