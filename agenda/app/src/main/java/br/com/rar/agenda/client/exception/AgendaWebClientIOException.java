package br.com.rar.agenda.client.exception;

/**
 * Created by ralmendro on 1/5/17.
 */
public class AgendaWebClientIOException extends Exception {

    public AgendaWebClientIOException() {
    }

    public AgendaWebClientIOException(String message) {
        super(message);
    }

    public AgendaWebClientIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public AgendaWebClientIOException(Throwable cause) {
        super(cause);
    }

}
