package com.a.shopping.entity;

import lombok.Data;

@Data
public class Result {
    private int code;
    private String msg;
    private long total;
    private Object data;
    public static Result fail(){
        return result(400,"失败",0,null);
    }
    public static Result fail(String msg){
        return result(400,msg,0,null);
    }
    public static Result fail(String msg,Object data){
        return result(400,msg,0,data);
    }
    public static Result suc(){
        return result(200,"成功",0,null);
    }
    public static Result suc(Object data){
        return result(200,"成功",0,data);
    }
    public static Result suc(String msg,Object data){
        return result(200,msg,0,data);
    }

    public static Result suc(Object data,long total){
        return result(200,"成功",total,data);
    }
    public static  Result result(int code,String msg,long total,Object data){
        Result res=new Result();
        res.setCode(code);
        res.setMsg(msg);
        res.setTotal(total);
        res.setData(data);
        return res;
    }

}
