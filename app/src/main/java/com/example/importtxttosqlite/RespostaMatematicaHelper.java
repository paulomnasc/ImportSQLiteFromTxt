package com.example.importtxttosqlite;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RespostaMatematicaHelper {


    public void FormataEspecial(String regex, String stringToBeMatched, String stringToBeReplaced)
    {

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher
                = pattern
                .matcher(stringToBeMatched);


                matcher
                        .quoteReplacement(stringToBeReplaced);

    }

    public String FormataResposta(String resposta)
    {
        String respostaFormatada = resposta.replace("$","").replace("frac{1}","fração: 1/");

        return respostaFormatada;

    }

}
