package br.metodista.ads.fretesuite.service;

import android.os.StrictMode;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

/**
 * Created by Apartamento on 16/05/2017.
 */

public class CorreioService {
    private static final String NAMESPACE = "http://webservice.correios.com.br:80/service/rastro";
    private static final String SURL = "http://webservice.correios.com.br/service/rastro/Rastro.wsdl"; // SURL deve ser o WSDL
    private static final String METHOD_NAME = "buscaEventos";
    private static final String SOAP_ACTION = "http://webservice.correios.com.br/service/rastro/";  //NAMESPACE+METHOD_NAME specified as a String literal.
    private static final String USER = "ECT";
    private static final String PASSWD = "SRO";
    private static final String TIPO = "L";
    private static final String RESULT = "U";

    public static String rastrear(String codigoRastreio) {

        try {
            String msgRetorno = "";
            // Definir a SURL Do Serviço sem a ?WSDL no fim
            URL SURL = new URL("http://webservice.correios.com.br:80/service/rastro");
            URLConnection conn = SURL.openConnection();
            // Define que a Conexão terá uma saída retorno
            conn.setDoOutput(true);
            // Método a ser Consumido pela requisição
            conn.setRequestProperty("SOAPAction", "http://webservice.correios.com.br/service/rastro/Rastro.wsdl");
            // Propriedades da Mensagem SOAP
            conn.setRequestProperty("Type", "Request-Response");
            conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
            conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
            conn.setRequestProperty("User-Agent", "Apache-HttpClient/4.1.1 (java 1.5)");
            // Canal de Saída da Requisição
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            // Mensagem no Formato SOAP
            String xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:res=\"http://resource.webservice.correios.com.br/\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>\n" +
                    "      <res:buscaEventos>\n" +
                    "         <!--Optional:-->\n" +
                    "         <usuario>ECT</usuario>\n" +
                    "         <!--Optional:-->\n" +
                    "         <senha>SRO</senha>\n" +
                    "         <!--Optional:-->\n" +
                    "         <tipo>L</tipo>\n" +
                    "         <!--Optional:-->\n" +
                    "         <resultado>U</resultado>\n" +
                    "         <!--Optional:-->\n" +
                    "         <lingua>101</lingua>\n" +
                    "         <!--Optional:-->\n" +
                    "         <objetos>" + codigoRastreio + "</objetos>\n" +
                    "      </res:buscaEventos>\n" +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";
            wr.write(xml);
            wr.flush();
            //System.out.println("Requisição" + conn.getOutputStream()); // Leitura da Resposta do Serviço
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            // Leituras das Linhas da Resposta
            while (rd.ready()) {
                msgRetorno += rd.readLine();
            }
            wr.close();
            rd.close();
            conn.getInputStream().close();


            return parseResponse(msgRetorno);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Fim");
        }


        return null;

    }

    private static String parseResponse(String response) {

        InputSource src = new InputSource(new StringReader(response));

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(src);
            String status = null;


            if (doc.getElementsByTagName("objeto").item(0).getChildNodes().item(1).getNodeName().equals("erro")) {
                status = doc.getElementsByTagName("objeto").item(0).getChildNodes().item(1).getTextContent();
            } else {
                status = doc.getElementsByTagName("objeto").item(0).getChildNodes().item(4).getChildNodes().item(4).getTextContent();
            }

            return status.trim();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
