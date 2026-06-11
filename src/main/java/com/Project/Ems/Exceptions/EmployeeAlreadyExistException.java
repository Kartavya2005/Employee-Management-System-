package com.Project.Ems.Exceptions;

public class EmployeeAlreadyExistException extends RuntimeException{

    public EmployeeAlreadyExistException(String message) {
        super(message);
    }
}
