package br.metodista.ads.fretesuite;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Created by marcelo on 5/9/17.
 */

public class EnderecoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endereco);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
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
            Intent intent =  new Intent(this, MainActivity.class);
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
}
