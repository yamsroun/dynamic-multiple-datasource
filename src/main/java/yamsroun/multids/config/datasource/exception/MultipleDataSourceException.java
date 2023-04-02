package yamsroun.multids.config.datasource.exception;

public class MultipleDataSourceException extends RuntimeException {

    public MultipleDataSourceException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
