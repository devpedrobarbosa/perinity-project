package me.pedrobarbosa.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("Um usuário com esse nome já existe");
    }
}