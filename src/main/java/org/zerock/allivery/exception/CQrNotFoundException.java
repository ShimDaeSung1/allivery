package org.zerock.allivery.exception;

import lombok.Getter;
import org.zerock.allivery.exception.handler.ErrorCode;

@Getter
public class CQrNotFoundException extends RuntimeException{

    private ErrorCode errorCode;

    public CQrNotFoundException(String msg){
        super(msg);
    }

    public CQrNotFoundException(String msg, ErrorCode errorCode){
        super(msg);
        this.errorCode = errorCode;
    }


}
