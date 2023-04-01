package yamsroun.multids.config.datasource.factory;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;
import yamsroun.multids.config.datasource.property.EachDataSourceProperties;
import yamsroun.multids.config.datasource.property.HikariDataSourceProperties;
import yamsroun.multids.config.datasource.type.DataSourceGroupType;
import yamsroun.multids.config.datasource.type.DataSourceNameType;
import yamsroun.multids.config.datasource.util.NamingUtils;

@Slf4j
@RequiredArgsConstructor
@Component
public class EachDataSourceFactory {

    private final HikariDataSourceProperties hikariProps;

    public HikariDataSource createClusterWriter(String groupName, EachDataSourcePropertiesGetter propsGetter) {
        var groupType = DataSourceGroupType.CLUSTER;
        var nameType = DataSourceNameType.WRITER;
        return create(groupName, groupType, nameType, propsGetter.get(nameType));
    }

    public HikariDataSource createClusterReader(String groupName, EachDataSourcePropertiesGetter propsGetter) {
        var groupType = DataSourceGroupType.CLUSTER;
        var nameType = DataSourceNameType.READER;
        return create(groupName, groupType, nameType, propsGetter.get(nameType));
    }

    public HikariDataSource createSingle(String groupName, EachDataSourcePropertiesGetter propsGetter) {
        var groupType = DataSourceGroupType.SINGLE;
        var nameType = DataSourceNameType.SINGLE;
        return create(groupName, groupType, nameType, propsGetter.get(nameType));
    }

    private HikariDataSource create(
        String groupName,
        DataSourceGroupType groupType,
        DataSourceNameType nameType,
        EachDataSourceProperties eachProps
    ) {
        HikariDataSource dataSource = DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .url(eachProps.getJdbcUrl())
            .username(eachProps.getUsername())
            .password(eachProps.getPassword())
            .build();

        if (nameType.isReader()) {
            dataSource.setReadOnly(true);
        }
        dataSource.setPoolName(getPoolName(groupName, groupType, nameType));
        dataSource.setMinimumIdle(eachProps.getMinimumIdle());
        dataSource.setMaximumPoolSize(eachProps.getMaximumPoolSize());

        dataSource.setMaxLifetime(hikariProps.getMaxLifetime());
        dataSource.setConnectionTimeout(hikariProps.getConnectionTimeout());
        dataSource.setValidationTimeout(hikariProps.getValidationTimeout());
        dataSource.setLeakDetectionThreshold(hikariProps.getLeakDetectionThreshold());
        dataSource.setDataSourceProperties(hikariProps.getDataSourceProperties());

        return dataSource;
    }

    private static String getPoolName(String groupName, DataSourceGroupType groupType, DataSourceNameType nameType) {
        if (groupType.isCluster()) {
            return NamingUtils.toLowerCamelCase(groupName, nameType.name().toLowerCase(), "Pool");
        }
        return NamingUtils.toLowerCamelCase(groupName, "Pool");
    }
}
