package yamsroun.multids.config.datasource.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import yamsroun.multids.config.datasource.property.DataSourceGroupProperties;
import yamsroun.multids.config.datasource.property.MultipleDataSourceProperties;

@Slf4j
@Component
public class GroupDataSourceFactory {

    private final MultipleDataSourceProperties multipleDataSourceProperties;

    private final ClusterGroupDataSourceFactory clusterGroupDataSourceFactory;
    private final SingleGroupDataSourceFactory singleGroupDataSourceFactory;

    public GroupDataSourceFactory(
        MultipleDataSourceProperties multipleDataSourceProperties,
        EachDataSourceFactory eachDataSourceFactory
    ) {
        this.multipleDataSourceProperties = multipleDataSourceProperties;
        this.clusterGroupDataSourceFactory = new ClusterGroupDataSourceFactory(eachDataSourceFactory);
        this.singleGroupDataSourceFactory = new SingleGroupDataSourceFactory(eachDataSourceFactory);
    }


    public void initialize() {
        multipleDataSourceProperties.getGroups()
            .forEach(this::createGroupDataSource);
    }

    private void createGroupDataSource(String groupName, DataSourceGroupProperties groupProps) {
        if (groupProps.isCluster()) {
            clusterGroupDataSourceFactory.create(groupName, groupProps);
        } else {
            singleGroupDataSourceFactory.create(groupName, groupProps);
        }
    }

}
