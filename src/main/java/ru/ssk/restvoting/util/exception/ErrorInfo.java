package ru.ssk.restvoting.util.exception;

public class ErrorInfo {
    private final CharSequence url;
    private final String messageText;
    private final String[] details;
    private final ErrorType errorType;

    public ErrorInfo(CharSequence url, String messageText, String[] details, ErrorType errorType) {
        this.url = url;
        this.messageText = messageText;
        this.details = details;
        this.errorType = errorType;
    }

    public CharSequence getUrl() {
        return url;
    }

    public String getMessageText() {
        return messageText;
    }

    public String[] getDetails() {
        return details;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
