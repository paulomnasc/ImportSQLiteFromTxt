package com.example.importtxttosqlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class actDoChatBot extends AppCompatActivity {

    private String idAno;
    private String idConteudo;

    private String question;

    private ArrayAdapter<String> questionsAdaptador;
    private ArrayList<String> questions;

    private ListView listaQuestions;

    private String dsDisciplina;
    private Button btnAvancar;
    private Button btnVoltar;
    private TextView txtAssunto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_do_cha_bot);

        btnAvancar = (Button) findViewById(R.id.btnAvancar);
        btnVoltar = (Button) findViewById(R.id.btnVoltar);
        txtAssunto = (TextView)findViewById(R.id.txtAssunto);
        listaQuestions = (ListView) findViewById(R.id.lstQuestions);


        Intent intent= this.getIntent();
        dsDisciplina = intent.getStringExtra("dsDisciplina");
        idAno = intent.getStringExtra("idAno");
        idConteudo = intent.getStringExtra("idConteudo");
        question = intent.getStringExtra("question");

        txtAssunto.setText(question);

        preencherQuestoes();

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


                Intent switchActivityIntent = new Intent(actDoChatBot.this, actDisciplinas.class);
                switchActivityIntent.putExtra("idAno", idAno);
                switchActivityIntent.putExtra("dsDisciplina", dsDisciplina);
                switchActivityIntent.putExtra("idConteudo", idConteudo);
                switchActivityIntent.putExtra("question", question);
                startActivity(switchActivityIntent);

            }


        });

    }

    private void preencherQuestoes() {

        questions = new ArrayList<String>();

        String question;

        question = "Cite as 20 dúvidas frequentes de " + txtAssunto.getText();

        questionsAdaptador= new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_2
                , android.R.id.text1
                , questions);

        listaQuestions.setAdapter(questionsAdaptador);


        questions.add(question);

    }

}