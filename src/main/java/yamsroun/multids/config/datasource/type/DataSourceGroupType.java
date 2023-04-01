package yamsroun.multids.config.datasource.type;

public enum DataSourceGroupType {
    CLUSTER, SINGLE;

    public boolean isCluster() {
        return this == CLUSTER;
    }
}
