package com.consume.api.response;

public class GenericApiResponse<T> {
    private int statusCode;
    private String statusText;
    private T data;

    public GenericApiResponse() {
    }

    public GenericApiResponse(int statusCode, String statusText,T data) {
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GenericApiResponse{" +
                "statusCode='" + statusCode + '\'' +
                ", statusText='" + statusText + '\'' +
                ", data=" + data +
                '}';
    }
}
