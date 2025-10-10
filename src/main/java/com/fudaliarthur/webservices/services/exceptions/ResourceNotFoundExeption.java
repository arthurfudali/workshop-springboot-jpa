package com.fudaliarthur.webservices.services.exceptions;

public class ResourceNotFoundExeption extends RuntimeException{
    public ResourceNotFoundExeption(Object id){
        super("Resource not found with id " + id);
    }
}
