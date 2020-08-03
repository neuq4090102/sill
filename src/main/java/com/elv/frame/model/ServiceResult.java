package com.elv.frame.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.elv.frame.constant.StatusCode;
import com.elv.frame.itf.IStatusCode;

/**
 * @author lxh
 * @since 2020-03-24
 */
public class ServiceResult<T> implements Serializable {

    private static final long serialVersionUID = -5736318257782607073L;

    private int code = 0;
    private String msg = "";
    private Throwable cause;
    private T data;
    private Map<String, Object> attachments = new HashMap<String, Object>();

    public ServiceResult() {
    }

    public ServiceResult(T data) {
        this.data = data;
    }

    private ServiceResult(IStatusCode statusCodeEnum) {
        this.code = statusCodeEnum.getCode();
        this.msg = statusCodeEnum.getMsg();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Map<String, Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, Object> attachments) {
        this.attachments = attachments;
    }

    public boolean isSuccess() {
        return StatusCode.isSuccess(this.getCode());
    }

    public static <T> ServiceResult<T> success() {
        return new ServiceResult<>(StatusCode.SUCCESS);
    }

    public static <T> ServiceResult<T> success(T data) {
        ServiceResult<T> result = new ServiceResult<>(StatusCode.SUCCESS);
        result.setData(data);
        return result;
    }

    public static <T> ServiceResult<T> error(String msg) {
        return ServiceResult.error(StatusCode.FAIL.getCode(), msg, null);
    }

    public static <T> ServiceResult<T> error(int code, String msg) {
        return ServiceResult.error(code, msg, null);
    }

    public static <T> ServiceResult<T> error(int code, String msg, Throwable cause) {
        ServiceResult<T> result = new ServiceResult<T>();
        result.setCode(code);
        result.setMsg(msg);
        result.setCause(cause);
        return result;
    }

    public static <T> ServiceResult<T> error(IStatusCode statusCodeEnum) {
        return ServiceResult.error(statusCodeEnum.getCode(), statusCodeEnum.getMsg());
    }
}
