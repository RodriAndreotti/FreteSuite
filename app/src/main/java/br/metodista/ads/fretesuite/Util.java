package br.metodista.ads.fretesuite;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import br.metodista.ads.fretesuite.models.MDLEndereco;

/**
 * Created by leonardo.lima on 06/05/2017.
 */

public class Util {

    public String downloadURL(String urlString) throws Exception {
        BufferedReader reader = null;
        StringBuffer buffer = null;
        URL url = null;
        int lido = 0;
        char[] caractere = new char[1024];

        try {
            url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            buffer = new StringBuffer();

            while ((lido = reader.read(caractere)) != -1)
                buffer.append(caractere, 0, lido);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    // Deserializa um endereço em Json
    public MDLEndereco desserializarEndereco(String jsonString) {
        Gson gson = new Gson();
        MDLEndereco mdlEndereco;

        // Deserializa um endereço
        if (!jsonString.isEmpty()) {
            mdlEndereco = gson.fromJson(jsonString, MDLEndereco.class);
        } else {
            mdlEndereco = null;
        }
        return mdlEndereco;
    }

    // Exibe uma mensagem de Alerta (Botão OK)
    public void exibirMensagemAlerta(String titulo, String mensagem, Context context) {

        // Constroi um diálogo de Alerta
        new AlertDialog.Builder(context)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                }).show();
    }
}
