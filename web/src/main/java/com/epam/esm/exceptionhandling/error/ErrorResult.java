package com.epam.esm.exceptionhandling.error;

import java.util.List;

public class ErrorResult {
    private int errorCode;
    private List<String> messages;


    public ErrorResult() {
    }

    public ErrorResult(int errorCode, List<String> messages) {
        this.errorCode = errorCode;
        this.messages = messages;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public List<String> getMessages() {
        return messages;
    }
}
