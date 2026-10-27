package br.ifrn.edu.sici.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TypeUserDenied extends RuntimeException {
    public TypeUserDenied(String message) { super(message); }
}
