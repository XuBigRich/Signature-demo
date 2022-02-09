package com.sign.signaturedemo.common.entity.bo;

import com.sign.signaturedemo.common.enums.ResultEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回类
 *
 * @author 刘雪
 * @since 2021/12/27 15:10:36
 */
@Data
public class ResultBO<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 返回码
     */
    private Integer code = ResultEnum.SUCCESS.getCode();
    /**
     * 返回消息
     */
    private String msg = ResultEnum.SUCCESS.getMsg();
    /**
     * 返回数据
     */
    private T data;

    /**
     * 默认构造
     */
    public ResultBO() {
        super();
    }

    /**
     * 带数据
     */
    public ResultBO(T data) {
        super();
        this.data = data;
    }

    /**
     * 带数据和消息
     */
    public ResultBO(T data, String msg) {
        super();
        this.data = data;
        this.msg = msg;
    }

    /**
     * 指定返回
     */
    public ResultBO(ResultEnum re) {
        super();
        this.code = re.getCode();
        this.msg = re.getMsg();
    }

    public ResultBO(ResultEnum re, T data) {
        super();
        this.code = re.getCode();
        this.msg = re.getMsg();
        this.data = data;
    }

    /**
     * 异常返回
     */
    public ResultBO(Throwable e) {
        super();
        this.code = ResultEnum.FAIL.getCode();
        this.msg = e.getMessage();
    }

    public ResultBO(Integer code, String message, T data) {
        this.code = code;
        this.msg = message;
        this.data = data;
    }

    public ResultBO(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected ResultBO ResultBO(int rows) {
        return rows > 0 ? new ResultBO<>(ResultEnum.SUCCESS) : new ResultBO<>(ResultEnum.FAIL);
    }

    /**
     * 响应返回结果
     *
     * @param flag 判断是否
     * @return 操作结果
     */
    protected ResultBO ResultBO(boolean flag) {
        return flag ? new ResultBO<>(ResultEnum.SUCCESS) : new ResultBO<>(ResultEnum.FAIL);
    }


    /**
     * 返回错误结果
     */
    public static <T> ResultBO<T> fail(String msg) {
        return new ResultBO<T>(ResultEnum.FAIL.getCode(), msg);
    }

}
