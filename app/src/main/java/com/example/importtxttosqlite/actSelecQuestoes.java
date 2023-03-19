package com.example.importtxttosqlite;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class actSelecQuestoes extends AppCompatActivity {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_selec_questoes);

        btnAvancar = (Button) findViewById(R.id.btnAvancar);
        btnVoltar = (Button) findViewById(R.id.btnVoltar);
        txtAssunto = (TextView)findViewById(R.id.txtAssunto);
        listaQuestions = (ListView) findViewById(R.id.lstQuestions);


        Intent intent= this.getIntent();
        dsDisciplina = intent.getStringExtra("dsDisciplina");
        idAno = intent.getStringExtra("idAno");
        idConteudo = intent.getStringExtra("idConteudo");

        txtAssunto.setText(dsDisciplina);

        preencherQuestoes();

        btnAvancar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FormMoveNext();
            }

            private void FormMoveNext() {

                /* Avançar para Conteudos */
                Intent switchActivityIntent = new Intent(actSelecQuestoes.this, actDoChatBot.class);
                switchActivityIntent.putExtra("dsDisciplina", dsDisciplina);
                switchActivityIntent.putExtra("idAno", idAno.toString());
                switchActivityIntent.putExtra("idConteudo", idConteudo.toString());
                switchActivityIntent.putExtra("question", question.toString());
                startActivity(switchActivityIntent);

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
                switchActivityIntent.putExtra("dsDisciplina", dsDisciplina.toString());
                switchActivityIntent.putExtra("idConteudo", idConteudo.toString());
                startActivity(switchActivityIntent);

            }


        });

    }

    private void preencherQuestoes() {

        questions = new ArrayList<String>();



        question = "Me faça um resumo do conteúdo de estudo de " + txtAssunto.getText();

        questions.add(question);

        questionsAdaptador= new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_2
                , android.R.id.text1
                , questions);

        listaQuestions.setAdapter(questionsAdaptador);


    }

}