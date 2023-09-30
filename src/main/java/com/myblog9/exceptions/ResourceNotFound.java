package com.myblog9.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFound extends RuntimeException{

    public ResourceNotFound(String msg){//when i  create a object of resourcenot found i will suply message to this constructor

        super(msg);//super keyword will automatically display msg in postman response
    }
}

