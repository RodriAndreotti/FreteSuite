package br.metodista.ads.fretesuite;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import br.metodista.ads.fretesuite.models.MDLProduto;

/**
 * Created by Apartamento on 01/05/2017.
 */

public class EditarProdutoActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.editar_produto);
    }

    public void salvarProduto(View view) {
        MDLProduto produto = new MDLProduto();
        EditText txtRastreio = (EditText) findViewById(R.id.txtRastreio);
        EditText txtDescricao = (EditText) findViewById(R.id.txtProduto);
        produto.setCodigoRastreio(txtRastreio.getText().toString());
        produto.setDescricao(txtDescricao.getText().toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.salvar_produto_title);
        builder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(EditarProdutoActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });



        if(ProdutoDB.getInstance(this).salvarProduto(produto).getId() != null) {
            builder.setMessage("Produto salvo com sucesso!");
        }
        else {
            builder.setMessage("Erro ao salvar produto!");
        }

        AlertDialog alert = builder.create();
        alert.show();
    }

}
