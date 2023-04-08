package com.example.importtxttosqlite;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;


public class actSelecQuestoes extends BaseAPIActivity {

    private String idAno;
    private String idConteudo;

    private String question;

    private ArrayAdapter<String> questionsAdaptador;
    private ArrayList<String> questions;

    private ListView listaQuestions;

    private String dsDisciplina;
    private Button btnVoltar;
    private Button btnAvancar;
    private TextView txtAssunto;

    private ImageView iconLoad;

    private Handler handler;

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

    private void ObterDuvidasFrequentes()
    {
        String question = "Cite as 10 dúvidas frequentes de " + txtAssunto.getText();
        callAPI(question);

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



        /*Esta questão será buscada diretamente do chat gpt
        question = "Cite as 10 dúvidas frequentes de " + txtAssunto.getText();
        questions.add(question);
        */

        String[] vetorQuestoesFrequentes = txtResponse.split("\n");
        if(vetorQuestoesFrequentes != null)
        {
            for (String item: vetorQuestoesFrequentes) {
                questions.add(item);
            }
        }

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


}