package br.metodista.ads.fretesuite;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.metodista.ads.fretesuite.models.MDLPrazo;


/**
 * Created by leonardo.lima on 07/05/2017.
 */

public class CalcularPrazo extends AppCompatActivity {

    List<String> lstServicos = new ArrayList<>();
    Map<String, String> spinnerMap = new LinkedHashMap<>();
    ArrayAdapter<String> adapter;
    final String SOAP_ACTION = "http://tempuri.org/CalcPrazo";
    final String METHOD_NAME = "CalcPrazo";
    final String NAMESPACE = "http://tempuri.org/";
    final String URL = "http://ws.correios.com.br/calculador/CalcPrecoPrazo.asmx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.calcular_prazo);

        carregarSpinner();
    }

    public void calcularPrazo(View view) {
        Util util = new Util();
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        SoapObject body;
        SoapObject subResposta;
        SoapObject result = null;
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        PropertyInfo pi;
        MDLPrazo mdlPrazo;
        String response = "";
        String servico = "";
        String cepOrigem = "";
        String cepDestino = "";

        // Preenche o Serviço, CEP de Origem e CEP de Destino
        servico = spinnerMap.get(((Spinner) findViewById(R.id.spinner)).getSelectedItem().toString());
        cepOrigem = ((EditText) findViewById(R.id.txtCepOrigem)).getText().toString();
        cepDestino = ((EditText) findViewById(R.id.txtCepDestino)).getText().toString();

        if (!(cepOrigem.isEmpty() || cepDestino.isEmpty())) {

            // Preenche o código do serviço
            pi = new PropertyInfo();
            pi.setName("nCdServico");
            pi.setValue(servico);
            pi.setType(String.class);
            request.addProperty(pi);

            // Preenche o CEP de Origem
            pi = new PropertyInfo();
            pi.setName("sCepOrigem");
            pi.setValue(cepOrigem);
            pi.setType(String.class);
            request.addProperty(pi);

            // Preenche o CEP de Destino
            pi = new PropertyInfo();
            pi.setName("sCepDestino");
            pi.setValue(cepDestino);
            pi.setType(String.class);
            request.addProperty(pi);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(URL);

            // Chama o WebService
            try {

                // O retorno do WebService dos correios é composto de dois nós, além do response, sendo:
                // <CalcPrecoPrazoResult>
                // <Servicos> (Variável Body)
                // <cServico> (Variável subResposta)
                // <Propriedades>
                // Portanto, primeiro é necessário pegar o nó "Serviços", seguido do "cServico",
                // onde no "cServico" contém as propriedades desejadas

                httpTransport.call(SOAP_ACTION, envelope);
                result = (SoapObject) envelope.getResponse();
                body = (SoapObject) result.getProperty("Servicos");
                subResposta = (SoapObject) body.getProperty("cServico");

                // Preenche a MDL com base na resposta do WebService
                mdlPrazo = popularMDLPrazo(subResposta);

                // Se a mensagem de erro for "anyType{}" (vazio), então a requisição ocorreu com sucesso
                if (mdlPrazo.getMsgErro().equals("anyType{}")) {
                    util.exibirMensagemAlerta("Resultado", formatarMensagemPrazo(mdlPrazo), this);
                } else {
                    util.exibirMensagemAlerta("Aviso", mdlPrazo.getMsgErro(), this);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            util.exibirMensagemAlerta("Aviso", "Preencha todos os campos", this);
        }
    }

    // Carrega os itens do spinner
    private void carregarSpinner() {
        PrazoDB prazoDB = new PrazoDB(this);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Carrega o MAP que preencherá o spinner
        // O motivo de usar MAP e ter uma relação chave-valor (código do serviço e descrição)
        spinnerMap = prazoDB.listarServicos();

        // Preenche a lista de serviços
        for (int count = 0; count < spinnerMap.size(); count++) {
            lstServicos.add(spinnerMap.keySet().toArray()[count].toString());
        }

        // Carrega o spinner
        adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, lstServicos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // Popula o MDLPrazo com base no retorno do WebService
    private MDLPrazo popularMDLPrazo(SoapObject soapObject) {
        MDLPrazo mdlPrazo = new MDLPrazo();

        mdlPrazo.setCodigo(soapObject.getPropertyAsString("Codigo"));
        mdlPrazo.setPrazoEntrega(soapObject.getPropertyAsString("PrazoEntrega"));
        mdlPrazo.setMsgErro(soapObject.getPropertyAsString("MsgErro"));
        mdlPrazo.setEntregaDomiciliar(soapObject.getPropertyAsString("EntregaDomiciliar").equals("S"));
        mdlPrazo.setEntregaSabado(soapObject.getPropertyAsString("EntregaSabado").equals("S"));

        return mdlPrazo;
    }

    private String formatarMensagemPrazo(MDLPrazo mdlPrazo) {
        StringBuilder builder = new StringBuilder();

        builder.append("Código do Serviço:");
        builder.append(mdlPrazo.getCodigo());
        builder.append("\n");
        builder.append("Serviço:");
        builder.append(((Spinner) findViewById(R.id.spinner)).getSelectedItem().toString());
        builder.append("\n");
        builder.append("Prazo estimado de entrega:");
        builder.append(mdlPrazo.getPrazoEntrega());
        builder.append(" dia(s)");
        builder.append("\n");
        builder.append("Entrega Domiciliar:");
        builder.append(mdlPrazo.isEntregaDomiciliar() ? "Sim" : "Não");
        builder.append("\n");
        builder.append("Entrega de Sábado:");
        builder.append(mdlPrazo.isEntregaSabado() ? "Sim" : "Não");

        return builder.toString();
    }
}