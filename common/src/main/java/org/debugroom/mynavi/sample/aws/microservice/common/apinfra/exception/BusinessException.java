package org.debugroom.mynavi.sample.aws.microservice.common.apinfra.exception;

public class BusinessException extends Exception{

    private String code;
    private Object[] args;

    public BusinessException(){
        super();
    }

    public BusinessException(String code){
        this.code = code;
    }

    public BusinessException(String code, String message){
        super(message);
        this.code = code;
    }


    public BusinessException(String code, String message, Object... args){
        super(message);
        this.code = code;
        this.args = args;
    }

    public BusinessException(String code, Throwable cause){
        super(cause);
        this.code = code;
    }

    public BusinessException(String code, Throwable cause, Object... args){
        super(cause);
        this.code = code;
        this.args = args;
    }

    public BusinessException(String code, String message, Throwable cause){
        super(message, cause);
        this.code = code;
    }

    public BusinessException(String code, String message, Throwable cause, Object... args){
        super(message, cause);
        this.code = code;
        this.args = args;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

}
