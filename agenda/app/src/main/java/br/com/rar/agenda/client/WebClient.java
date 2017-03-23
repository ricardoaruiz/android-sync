package br.com.rar.agenda.client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import br.com.rar.agenda.client.exception.AgendaWebClientIOException;

/**
 * Created by ralmendro on 1/3/17.
 */

public class WebClient {

    public String post(String json) throws AgendaWebClientIOException {

        try {
            URL url = new URL("https://www.caelum.com.br/mobile");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Content-type", "application/json");
            connection.addRequestProperty("Accept", "application/json");

            //Indica que a requisição tera os dados de envio no BODY
            connection.setDoOutput(true);

            //Escreve o json no BODY da requisição
            PrintStream body = new PrintStream(connection.getOutputStream());
            body.println(json);

            //Realiza efetivamente a conexão com o serviço
            connection.connect();

            //Pega o resultado da requisição
            Scanner result = new Scanner(connection.getInputStream());
            String retorno = result.next();

            return retorno;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new AgendaWebClientIOException();
        }

        return null;
    }

}
