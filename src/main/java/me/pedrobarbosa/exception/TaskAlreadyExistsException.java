package me.pedrobarbosa.exception;

public class TaskAlreadyExistsException extends RuntimeException {

    public TaskAlreadyExistsException() {
        super("Uma task com este nome já existe");
    }
}