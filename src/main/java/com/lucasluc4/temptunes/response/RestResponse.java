package com.lucasluc4.temptunes.response;

public class RestResponse {

    private Integer code;
    private ErrorDetails errorDetails;
    private Object data;

    public RestResponse(Integer code, ErrorDetails errorDetails, Object data) {
        this.code = code;
        this.errorDetails = errorDetails;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(ErrorDetails errorDetails) {
        this.errorDetails = errorDetails;
    }

    public Object getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
