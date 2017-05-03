package br.metodista.ads.fretesuite;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by marcelo on 5/2/17.
 */

public class ConfiguracoesActivity extends Activity {

    public void AdicionarCep(View view) {
        EditText descricao = (EditText) findViewById(R.id.InputCep);
        if ( descricao.getText().toString().trim().length() > 0) {

        }
    }
}
