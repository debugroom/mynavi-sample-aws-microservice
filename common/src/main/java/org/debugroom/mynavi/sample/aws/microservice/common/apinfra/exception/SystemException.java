package org.debugroom.mynavi.sample.aws.microservice.common.apinfra.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemException extends RuntimeException{

    private String code;
    private Object[] args;

    public SystemException(){
        super();
    }

    public SystemException(String code, String message, Object[] args, Throwable cause){
        super(message, cause);
        this.code = code;
        this.setArgs(args);
    }

    public SystemException(String code, String message, Throwable cause){
        super(message, cause);
        this.code = code;
    }

    public SystemException(String code, String message){
        super(message);
        this.code = code;
    }

    public SystemException(String code, Object[] args, Throwable cause){
        super(cause);
        this.code = code;
        this.setArgs(args);
    }

    public SystemException(String code, Throwable cause){
        super(cause);
        this.code = code;
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
