package com.elv.web.model;

import com.elv.sill.framework.common.IStatusCodeEnum;
import com.elv.sill.framework.constant.FrameworkErrorEnum;
import com.elv.sill.framework.constant.StatusCodeEnum;
import com.elv.sill.framework.model.PagingServiceResult;
import com.elv.sill.framework.model.ServiceResult;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lxh
 * @date 2020-03-20
 */
public class ApiResult implements Serializable {

    private static final long serialVersionUID = 5608340469539149236L;

    private int code = 0;
    private String msg = "";
    private ValidationResult errors = null;
    private Object data = null;
    private int total = 0;
    private long cost = 0;
    private long instant = Instant.now().toEpochMilli();
    private Map<String, Object> attachments = new HashMap<>();

    public ApiResult() {

    }

    public ApiResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiResult(IStatusCodeEnum statusCodeEnum) {
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

    public ValidationResult getErrors() {
        return errors;
    }

    public void setErrors(ValidationResult errors) {
        this.errors = errors;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public long getInstant() {
        return instant;
    }

    public void setInstant(long instant) {
        this.instant = instant;
    }

    public Map<String, Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, Object> attachments) {
        this.attachments = attachments;
    }

    public boolean isSuccess() {
        return StatusCodeEnum.isSuccess(this.getCode());
    }

    public static ApiResult success() {
        return new ApiResult(StatusCodeEnum.SUCCESS);
    }

    public static ApiResult success(Object data) {
        ApiResult result = new ApiResult(StatusCodeEnum.SUCCESS);
        result.setData(data);
        return result;
    }

    public static ApiResult success(ServiceResult<?> serviceResult) {
        ApiResult result = new ApiResult(StatusCodeEnum.SUCCESS);
        result.setData(serviceResult.getData());
        result.setAttachments(serviceResult.getAttachments());
        if (serviceResult instanceof PagingServiceResult) {
            //分页总计
            result.setTotal(((PagingServiceResult<?>) serviceResult).getTotal());
        }

        return result;
    }

    public static ApiResult transmit(ServiceResult<?> serviceResult) {
        if (serviceResult.isSuccess()) {
            return ApiResult.success(serviceResult);
        }

        if (serviceResult.getCause() == null) {
            return ApiResult.error(serviceResult.getCode(), serviceResult.getMsg());
        } else {
            return ApiResult.error("unknow error1");
        }
    }

    public static ApiResult fail() {
        return new ApiResult(StatusCodeEnum.FAIL);
    }

    public static ApiResult fail(IStatusCodeEnum statusCodeEnum) {
        return new ApiResult(statusCodeEnum);
    }

    public static ApiResult fail(ValidationResult validationResult) {
        ApiResult result = new ApiResult(FrameworkErrorEnum.PARAM_ERROR);
        result.setErrors(validationResult);
        return result;
    }

    public static ApiResult error(String msg) {
        return new ApiResult(StatusCodeEnum.FAIL.getCode(), msg);
    }

    public static ApiResult error(int code, String msg) {
        return new ApiResult(code, msg);
    }

}
