package org.zerock.allivery.exception;

import lombok.Getter;
import org.zerock.allivery.exception.handler.ErrorCode;

@Getter
public class CUserNotFoundException extends RuntimeException{

    private ErrorCode errorCode;

    public CUserNotFoundException(String msg){
        super(msg);
    }

    public CUserNotFoundException(String msg, ErrorCode errorCode){
        super(msg);
        this.errorCode = errorCode;
    }
}
