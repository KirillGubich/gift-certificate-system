package com.epam.esm.web.model;

import java.util.Objects;

/**
 * Model for error information representation
 */
public class ErrorInfo {
    private final String errorMessage;
    private final int errorCode;

    public ErrorInfo(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorInfo errorInfo = (ErrorInfo) o;
        return errorCode == errorInfo.errorCode && Objects.equals(errorMessage, errorInfo.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorMessage, errorCode);
    }

    @Override
    public String toString() {
        return "ErrorInfo{" +
                "errorMessage='" + errorMessage + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }
}
