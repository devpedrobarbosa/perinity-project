package me.pedrobarbosa.exception;

public class DepartmentAlreadyExistsException extends RuntimeException {

    public DepartmentAlreadyExistsException() {
        super("Um departamento com esse nome já existe");
    }
}
