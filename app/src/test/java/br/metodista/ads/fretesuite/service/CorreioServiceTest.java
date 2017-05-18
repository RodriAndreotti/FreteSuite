package br.metodista.ads.fretesuite.service;


import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Apartamento on 16/05/2017.
 */

public class CorreioServiceTest {

    @org.junit.Test
    public void rastreiaNaoEncontradoTest() {
        String situacao = CorreioService.rastrear("JF598971235BR");
        Assert.assertTrue("Objeto não encontrado na base de dados dos Correios.".equals(situacao));
    }

    @Test
    public void rastreiaObjetoEncaminhado() {
        String situacao = CorreioService.rastrear("PO064272664BR");
        System.out.println(situacao);
        Assert.assertTrue("Objeto encaminhado".equals(situacao));
    }
}
