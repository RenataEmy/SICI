package br.ifrn.edu.sici.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RuleConflit extends RuntimeException {
    public RuleConflit(String message) { super(message); }
}
