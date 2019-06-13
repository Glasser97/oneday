package com.coderziyang.oneday;

public class Token {
    private String status;
    private String data;

    public Token(){}
    public Token(String status,String data){
        this.status=status;
        this.data=status;
    }
    public String getStatus(){
        return this.status;
    }
    public String getData(){
        return this.data;
    }
    public void setData(String data){
        this.data=data;
    }
    public void setStatus(String status){
        this.status=status;
    }
}
