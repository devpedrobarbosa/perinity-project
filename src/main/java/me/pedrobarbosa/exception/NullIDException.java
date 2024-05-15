package me.pedrobarbosa.exception;

public class NullIDException extends RuntimeException {

    public NullIDException() {
        super("O ID não pode ser nulo");
    }
}
