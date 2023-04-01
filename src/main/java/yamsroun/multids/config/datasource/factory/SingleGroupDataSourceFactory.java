package yamsroun.multids.config.datasource.factory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import yamsroun.multids.config.datasource.property.DataSourceGroupProperties;
import yamsroun.multids.config.datasource.util.BeanRegistrationUtils;

@Slf4j
@RequiredArgsConstructor
public class SingleGroupDataSourceFactory extends AbstractGroupDataSourceFactory {

    private final EachDataSourceFactory eachDataSourceFactory;

    public void create(String groupName, DataSourceGroupProperties groupProps) {
        var dataSource = eachDataSourceFactory.createSingle(groupName, groupProps::getEachProperties);
        createExplicitlyConnectionPoolOf(dataSource, dataSource.getPoolName());
        BeanRegistrationUtils.register(getBeanName(groupName, "dataSource"), dataSource);
    }
}
