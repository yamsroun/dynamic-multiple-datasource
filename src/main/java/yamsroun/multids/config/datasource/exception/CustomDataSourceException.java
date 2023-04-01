package yamsroun.multids.config.datasource.exception;

public class CustomDataSourceException extends RuntimeException {

    public CustomDataSourceException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
