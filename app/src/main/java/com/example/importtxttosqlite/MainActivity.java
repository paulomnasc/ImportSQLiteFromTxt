package com.example.importtxttosqlite;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class MainActivity extends BaseAPIActivity {


    EditText messageEditText;
    ImageButton sendButton;
    /*RecyclerView recyclerView;
    List<Message> messageList;
    MessageAdapter messageAdapter;*/
    private String idAno;
    private String idConteudo;
    private String dsDisciplina;
    private String question;
    private Button btnVoltar;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);

        Intent intent= this.getIntent();
        question = intent.getStringExtra("question");
        dsDisciplina = intent.getStringExtra("dsDisciplina");
        idAno = intent.getStringExtra("idAno");
        idConteudo = intent.getStringExtra("idConteudo");
        messageEditText.setText(question);

        //setup recycler view
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        sendButton.setOnClickListener((v)->{
            String question = messageEditText.getText().toString().trim();
            addToChat(question,Message.SENT_BY_ME);
            messageEditText.setText("");
            callAPI(question);

        });

        btnVoltar = (Button) findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FormMovePrevious();
            }


            private void FormMovePrevious() {

                /* Retornar para Lista de Anos */
                Intent switchActivityIntent = new Intent(MainActivity.this, actSelecQuestoes.class);
                switchActivityIntent.putExtra("dsDisciplina", dsDisciplina);
                switchActivityIntent.putExtra("idAno", idAno.toString());
                switchActivityIntent.putExtra("idConteudo", idConteudo.toString());
                switchActivityIntent.putExtra("question", "");
                startActivity(switchActivityIntent);

            }


        });

    }

    void addToChat(String message,String sentBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message,sentBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }

    void addResponse(String response){
        messageList.remove(messageList.size()-1);

        //Criar uma classe para tratar o retorno de matemática que vem com formato pouco natural
        // para os alunos
        String resposta = "";
        // 3 = MATEMATICA
        if(idConteudo.equalsIgnoreCase("3")) {
            RespostaMatematicaHelper formatador = new RespostaMatematicaHelper();
            resposta = formatador.FormataResposta(response);
        }else{
            resposta = response;
        }

        addToChat(resposta,Message.SENT_BY_BOT);
    }



}




















