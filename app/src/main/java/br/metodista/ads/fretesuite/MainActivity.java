package br.metodista.ads.fretesuite;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;


import br.metodista.ads.fretesuite.models.MDLProduto;
import br.metodista.ads.fretesuite.service.CorreioService;

public class MainActivity extends AppCompatActivity {

    ListAdapter adapter;
    List<String> produtos;
    List<MDLProduto> oProdutos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(MainActivity.this, EditarProdutoActivity.class));
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                Log.v("ID Cliecado: ", String.valueOf(id));

                if (id == R.id.nav_rastreio) {
                    Intent intent =  new Intent(MainActivity.this, ListarCliente.class);
                    startActivity(intent);
                } else if (id == R.id.nav_configuracoes) {
                    Intent intent =  new Intent(MainActivity.this, ConfiguracoesActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_preferenciais) {
                    Intent intent =  new Intent(MainActivity.this, ListarCliente.class);
                    startActivity(intent);
                } else if (id == R.id.nav_endereco) {
                    Intent intent =  new Intent(MainActivity.this, ConfiguracoesActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_calcular) {
                    Intent intent =  new Intent(MainActivity.this, CalculadoraActivity.class);
                    startActivity(intent);
                }

                return true;
            }
        });


        prepararProdutos();
        prepararListView();


    }

    private void prepararProdutos() {
        List<MDLProduto> produtos = ProdutoDB.getInstance(this).listarProdutos();

        this.produtos = new ArrayList();

        for(MDLProduto p : produtos) {
            this.produtos.add(p.toString());
        }
        this.oProdutos = produtos;
    }

    private void prepararListView() {
        ListView listaProdutos = (ListView) findViewById(R.id.listaProdutos);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.produtos);
        listaProdutos.setAdapter(adapter);

        listaProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);

                rastreiaProduto(oProdutos.get(position));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.cadastro_cliente){
            Intent intent = new Intent(MainActivity.this, CadastroCliente.class);
            this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void rastreiaProduto(MDLProduto produto) {

        Util util = new Util();

        if (produto != null) {
            String situacao = CorreioService.rastrear(produto.getCodigoRastreio());
            System.out.println(situacao);
            util.exibirMensagemAlerta("Situação do Produto", situacao, this);

        } else {
            util.exibirMensagemAlerta("Aviso", "Preencha todos os campos", this);
        }
    }

    public void mostrarMain(MenuItem item) {
        Intent intent =  new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void mostrarClientes(MenuItem item) {
        Intent intent =  new Intent(MainActivity.this, ListarCliente.class);
        startActivity(intent);
    }
}
