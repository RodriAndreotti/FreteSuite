package br.metodista.ads.fretesuite;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import br.metodista.ads.fretesuite.models.MDLCliente;
import br.metodista.ads.fretesuite.models.MDLEndereco;

/**
 * Created by leonardo.lima on 04/05/2017.
 */

public class CadastroCliente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.cadastro_cliente);
    }

    public void salvarCliente(View view) {
        Util util = new Util();
        ClienteDB clienteDB = new ClienteDB(this);
        String nome = "";
        String cep = "";
        String endereco = "";

        // Reune os campos
        nome = ((EditText) findViewById(R.id.txtNome)).getText().toString();
        cep = ((EditText) findViewById(R.id.txtCEP)).getText().toString();
        endereco = ((EditText) findViewById(R.id.txtEndereco)).getText().toString();

        if (!(nome.isEmpty() || cep.isEmpty() || endereco.isEmpty())) {
            // Salva no banco de Dados
            clienteDB.salvarCliente(new MDLCliente(nome, cep, endereco));

            // Exibe a mensagem de confirmação
            util.exibirMensagemAlerta("Aviso", "Cliente salvo com sucesso!", this);
        } else {
            util.exibirMensagemAlerta("Aviso", "Preencha todos os campos", this);
        }
    }

    public void buscarEnderecoPorCep(View view) {
        Util util = new Util();
        MDLEndereco mdlEndereco = new MDLEndereco();
        String json = "";
        String cep = "";

        // Reúne o CEP
        cep = ((EditText) findViewById(R.id.txtCEP)).getText().toString();

        if (!cep.isEmpty()) {
            // Faz o download do JSON
            try {
                json = util.downloadURL(String.format(getString(R.string.EnderecoJsonCEP), cep));

                if (!json.contains("erro")) {
                    mdlEndereco = util.desserializarEndereco(json);
                    confirmarEndereco(mdlEndereco);
                } else {
                    util.exibirMensagemAlerta("Aviso", "CEP inválido ou não localizado!", this);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            util.exibirMensagemAlerta("CEP em branco", "O campo CEP não pode ficar vazio", this);
        }
    }

    private void confirmarEndereco(final MDLEndereco mdlEndereco) {
        // Constroi um diálogo de confirmação
        new AlertDialog.Builder(this)
                .setTitle("O endereço abaixo está correto?")
                .setMessage(formatarMensagemEndereco(mdlEndereco))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Escreve o endereço na caixa de texto
                        ((EditText) findViewById(R.id.txtEndereco)).setText(mdlEndereco.toString());
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    // Gera um endereço a partir de uma MDL
    private String formatarMensagemEndereco(MDLEndereco mdlEndereco) {
        StringBuilder builder = new StringBuilder();

        builder.append("Rua:");
        builder.append(mdlEndereco.getLogradouro());
        builder.append("\n");
        builder.append("Bairro: ");
        builder.append(mdlEndereco.getBairro());
        builder.append("\n");
        builder.append("Cidade: ");
        builder.append(mdlEndereco.getLocalidade());
        builder.append("\n");
        builder.append("UF: ");
        builder.append(mdlEndereco.getUf());
        builder.append("\n");
        builder.append("CEP: ");
        builder.append(mdlEndereco.getCep());
        return builder.toString();
    }
}
