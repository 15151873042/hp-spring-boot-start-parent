package com.got.common.core.vo;

import java.io.Serializable;

import com.got.common.core.enums.Enums;
import com.got.common.core.enums.Enums.SysCode;



/**
 * 
* @ClassName: RestResult
* @Description: Rest调用Json返回结果封装类
* @author Administrator
* @date 2016年3月1日 下午6:40:13
*
 */
public class RestResult<T> implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;

    /**
     * 返回结果：状态码
     */
    private String code;

    /**
     * 返回结果：信息
     */
    private String msg;

    /**
     * 返回结果：数据
     */
    private T data;

    public RestResult() {
        this(SysCode.SUCCESS);
    }

    public RestResult(Enums code) {
        this.code = code.getCode();
        this.msg = code.getDesc();
    }

    public RestResult(T data) {
        this();
        this.data = data;
    }

    public RestResult(Enums code, T data) {
        this.code = code.getCode();
        this.msg = code.getDesc();
        this.data = data;
    }

    public RestResult(String code, T data, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public RestResult(Enums code, T data, String msg) {
        this.code = code.getCode();
        this.data = data;
        this.msg = msg;
    }

    public RestResult(Enums code, Object[] msgObjs) {
        this.code = code.getCode();
        this.msg = code.getDesc(msgObjs);
    }

    public RestResult(Enums code, T data, Object[] msgObjs) {
        this.code = code.getCode();
        this.msg = code.getDesc(msgObjs);
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setEnums(Enums code) {
        this.code = code.getCode();
        this.msg = code.getDesc();
    }
}