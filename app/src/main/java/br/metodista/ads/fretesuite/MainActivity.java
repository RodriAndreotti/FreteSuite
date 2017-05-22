package br.metodista.ads.fretesuite;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.metodista.ads.fretesuite.models.MDLProduto;
import br.metodista.ads.fretesuite.service.CorreioService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        prepararProdutos();
        prepararListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_rastreio) {
            Toast.makeText(getApplicationContext(), "Rastreio", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_configuracoes) {
            Toast.makeText(getApplicationContext(), "Configurações", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_preferenciais) {
            Toast.makeText(getApplicationContext(), "Preferências", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_endereco) {
            Toast.makeText(getApplicationContext(), "Endereço", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_calcular) {
            Toast.makeText(getApplicationContext(), "Calcular frete", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_rastreio) {
            Intent intent =  new Intent(this, RastreioActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_configuracoes) {
            Intent intent =  new Intent(this, ConfiguracoesActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_preferenciais) {
            Intent intent =  new Intent(this, PreferenciaisActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_endereco) {
            Intent intent =  new Intent(this, EnderecoActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_calcular) {
            Intent intent =  new Intent(this, CalculadoraActivity.class);
            this.startActivity(intent);
        }

        return true;
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