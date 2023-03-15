package com.example.importtxttosqlite;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;


public class actSelecQuestoes extends AppCompatActivity {

    private String idAno;
    private String idConteudo;

    private String dsDisciplina;
    private Button btnVoltar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_selec_questoes);

        btnVoltar = (Button) findViewById(R.id.btnVoltar);



        Intent intent= this.getIntent();
        dsDisciplina = intent.getStringExtra("dsDisciplina");
        idAno = intent.getStringExtra("idAno");
        idConteudo = intent.getStringExtra("idConteudo");
        /*
        btnVoltar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FormMovePrevious();
            }


            private void FormMovePrevious() {


                Intent switchActivityIntent = new Intent(actSelecQuestoes.this, actDisciplinas.class);
                startActivity(switchActivityIntent);

            }


        });
        */
    }

}