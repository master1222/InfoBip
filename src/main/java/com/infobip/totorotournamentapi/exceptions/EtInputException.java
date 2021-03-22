package com.infobip.totorotournamentapi.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EtInputException extends  RuntimeException{

    public EtInputException( String message){
        super(message);
    }

}
