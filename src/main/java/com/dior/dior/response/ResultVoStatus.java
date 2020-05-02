package com.dior.dior.response;

public enum ResultVoStatus {

    //枚举字段以逗号隔开，结尾是分号，必须写在前面
    OK(200,"请求成功"),
    Bad(400,"请求失败"),
    Faild(400,"exportTPdf的异常"),
    wordTopdf(400,"word生成pdf异常"),
    FileOK(200,"文件生成成功！"),
    Success(200,"保存成功"),
    Error(400,"服务异常，插入失败！"),
    DelError(400,"服务异常，删除失败！"),
    ChangeError(400,"服务异常，修改失败！"),
    GetError(400,"服务异常，获取数据失败！"),
    ErrorUp(400,"OSS项目上传成功，更新数据失败"),
    FileNotFound(400,"文件未生成！") ,
    ErrorException(500,"内部服务异常"),



    GetCartItemSuccess(200,"获取用户的购物车信息成功"),
    OPCartItemException(500,"操作购物车项异常！"),
    DelCartItemException(500,"删除购物车项异常！"),

    AddUserAddressException(500,"新增用户地址异常！"),
    DelUserAddressException(500,"删除用户地址异常！"),
    ChangeUserAddressException(500,"修改用户地址异常！"),
    GetUserAddressException(500,"查询用户地址异常！"),

    //用户涉及异常
    AddUserException(500,"添加用户失败!"),

    GetDataException(500,"获取数据失败，数据可能为空！"),
    GetDataMoreException(500,"获取数据失败，结果为多条但只需要一条！"),
    test(500,"test");

    ;


    private  Integer status;
    private  String msg;

    private ResultVoStatus(Integer status, String msg){
        this.status=status;
        this.msg=msg;

    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
