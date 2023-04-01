package yamsroun.multids.config.datasource.type;

public enum DataSourceNameType {
    WRITER, READER, SINGLE;

    public boolean isWriter() {
        return this == DataSourceNameType.WRITER;
    }

    public boolean isReader() {
        return this == DataSourceNameType.READER;
    }

    public boolean isSingle() {
        return this == DataSourceNameType.SINGLE;
    }
}
