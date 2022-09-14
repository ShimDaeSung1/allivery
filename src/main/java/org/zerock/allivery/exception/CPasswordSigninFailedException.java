package org.zerock.allivery.exception;

public class CPasswordSigninFailedException extends RuntimeException{
    public CPasswordSigninFailedException(String msg, Throwable t){
        super(msg, t);
    }

    public CPasswordSigninFailedException(String msg){
        super(msg);
    }

    public CPasswordSigninFailedException(){
        super("해당 계정이 존재하지 않음");
    }
}
