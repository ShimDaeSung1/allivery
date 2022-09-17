package org.zerock.allivery.exception;

import lombok.Getter;
import org.zerock.allivery.exception.handler.ErrorCode;

@Getter
public class CQrAuthenticationFailedException extends RuntimeException{

    private ErrorCode errorCode;

    public CQrAuthenticationFailedException(String msg){
        super(msg);
    }

    public CQrAuthenticationFailedException(String msg, ErrorCode errorCode){
        super(msg);
        this.errorCode = errorCode;
    }
}
