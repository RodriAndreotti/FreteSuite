package br.metodista.ads.fretesuite;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.metodista.ads.fretesuite.models.MDLCliente;

/**
 * Created by leonardo.lima on 05/05/2017.
 */

public class ListarCliente extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private List<String> clientes = new ArrayList();
    private MDLCliente clienteSaida = null;
    private AlertDialog dialog;
    private ClienteDB clienteDB;
    List<MDLCliente> listaClientes = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar_cliente);

        prepararListaClientes();
        listarClientes();
    }

    private void prepararListaClientes() {
        ListView lista = (ListView) findViewById(R.id.lstClientes);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, clientes);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clienteSaida = listaClientes.get(position);
                dialog = confirmarSaida();
                dialog.show();
            }
        });
    }

    public AlertDialog confirmarSaida() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmação");
        builder.setMessage("Excluir o cliente abaixo?\n" + clienteSaida.getNome());
        builder.setPositiveButton(getString(R.string.Sim), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Remove o cliente
                clienteDB.removerCliente(clienteSaida.get_id());

                //Remove o cliente da lista
                listaClientes.remove(clienteSaida);
                clientes.remove(clienteSaida.getNome() + " - " + clienteSaida.getCep() + " - " + clienteSaida.getEndereco());
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(getString(R.string.Nao), null);
        return builder.create();
    }

    public void listarClientes()
    {
        clienteDB = new ClienteDB(this);
        listaClientes = clienteDB.listarClientes();

        for (MDLCliente cliente : listaClientes) {
            clientes.add(cliente.getNome() + " - " + cliente.getCep() + " - " + cliente.getEndereco());
        }
    }
}
