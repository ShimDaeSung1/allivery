package org.zerock.allivery.exception;

public class CEmailSignupFailedException extends RuntimeException{

    public CEmailSignupFailedException(String msg, Throwable t){
        super(msg,t);
    }

    public CEmailSignupFailedException(String msg){
        super(msg);
    }

    public CEmailSignupFailedException(){
        super("해당 계정이 이미 존재합니다.");
    }
}
