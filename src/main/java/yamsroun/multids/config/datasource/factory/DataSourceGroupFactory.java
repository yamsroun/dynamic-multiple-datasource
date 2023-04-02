package yamsroun.multids.config.datasource.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import yamsroun.multids.config.datasource.property.DataSourceGroupProperties;
import yamsroun.multids.config.datasource.property.MultipleDataSourceProperties;

@Slf4j
@Component
public class DataSourceGroupFactory {

    private final MultipleDataSourceProperties multipleDataSourceProperties;

    private final ClusterDataSourceGroupFactory clusterDataSourceGroupFactory;
    private final SingleDataSourceGroupFactory singleDataSourceGroupFactory;

    public DataSourceGroupFactory(
        MultipleDataSourceProperties multipleDataSourceProperties,
        EachDataSourceFactory eachDataSourceFactory
    ) {
        this.multipleDataSourceProperties = multipleDataSourceProperties;
        this.clusterDataSourceGroupFactory = new ClusterDataSourceGroupFactory(eachDataSourceFactory);
        this.singleDataSourceGroupFactory = new SingleDataSourceGroupFactory(eachDataSourceFactory);
    }


    public void initialize() {
        multipleDataSourceProperties.getGroups()
            .forEach(this::createGroupDataSource);
    }

    private void createGroupDataSource(String groupName, DataSourceGroupProperties groupProps) {
        if (groupProps.isCluster()) {
            clusterDataSourceGroupFactory.create(groupName, groupProps);
        } else {
            singleDataSourceGroupFactory.create(groupName, groupProps);
        }
    }

}
