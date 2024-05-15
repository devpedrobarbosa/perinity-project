package me.pedrobarbosa.exception;

public class DepartmentNotFoundException extends RuntimeException {

    public DepartmentNotFoundException() {
        super("Departamento não encontrado");
    }
}