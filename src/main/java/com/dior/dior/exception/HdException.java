package com.dior.dior.exception;


import com.dior.dior.response.ResultVoStatus;

public class HdException extends RuntimeException {

    ResultVoStatus resultVoStatus;
    Exception e;


    public HdException(ResultVoStatus resultVoStatus){
        super(resultVoStatus.getMsg());
        this.resultVoStatus=resultVoStatus;
    }

    public HdException(ResultVoStatus resultVoStatus, Exception e){
        super(resultVoStatus.getMsg());
        this.resultVoStatus=resultVoStatus;
        this.e=e;


    }

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }

    public ResultVoStatus getResultVoStatus() {
        return resultVoStatus;
    }

    public void setResultVoStatus(ResultVoStatus resultVoStatus) {
        this.resultVoStatus = resultVoStatus;
    }

}
