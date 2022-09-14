package org.zerock.allivery.exception;

public class CUserNameSignupFailedException extends RuntimeException{

    public CUserNameSignupFailedException(String msg, Throwable t){
        super(msg,t);
    }

    public CUserNameSignupFailedException(String msg){
        super(msg);
    }

    public CUserNameSignupFailedException(){
        super("이미 존재하는 닉네임 입니다.");
    }
}
