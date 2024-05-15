package me.pedrobarbosa.exception;

public class TaskTerminatedException extends RuntimeException {

    public TaskTerminatedException() {
        super("Esta task já está terminada");
    }
}