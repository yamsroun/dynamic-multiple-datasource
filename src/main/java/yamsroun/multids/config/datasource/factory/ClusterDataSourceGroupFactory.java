package yamsroun.multids.config.datasource.factory;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import yamsroun.multids.config.datasource.property.DataSourceGroupProperties;
import yamsroun.multids.config.datasource.util.BeanRegistrationUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class ClusterDataSourceGroupFactory extends AbstractGroupDataSourceFactory {

    private final EachDataSourceFactory eachDsFactory;

    public void create(String groupName, DataSourceGroupProperties groupProps) {
        var writerDataSource = eachDsFactory.createClusterWriter(groupName, groupProps::getEachProperties);
        var readerDataSource = eachDsFactory.createClusterReader(groupName, groupProps::getEachProperties);
        createExplicitlyConnectionPoolOf(readerDataSource, readerDataSource.getPoolName());

        var clusterRoutingDataSource = createClusterRoutingDataSource(writerDataSource, readerDataSource);
        BeanRegistrationUtils.register(getBeanName(groupName, "clusterRoutingDataSource"), clusterRoutingDataSource);

        var lazyConnectionDataSource = createLazyConnectionDataSource(clusterRoutingDataSource);
        BeanRegistrationUtils.register(getBeanName(groupName, "lazyConnectionDataSource"), lazyConnectionDataSource);
    }

    private DataSource createClusterRoutingDataSource(DataSource writer, DataSource reader) {
        return ClusterRoutingDataSource.create(writer, reader);
    }

    private DataSource createLazyConnectionDataSource(DataSource dataSource) {
        return new LazyConnectionDataSourceProxy(dataSource);
    }

    @Slf4j
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ClusterRoutingDataSource extends AbstractRoutingDataSource {

        public enum Target {
            WRITER, READER
        }

        public static DataSource create(DataSource writer, DataSource reader) {
            Map<Object, Object> targetDataSources = new HashMap<>();
            targetDataSources.put(Target.WRITER, writer);
            targetDataSources.put(Target.READER, reader);

            ClusterRoutingDataSource dataSource = new ClusterRoutingDataSource();
            dataSource.setTargetDataSources(targetDataSources);
            dataSource.setDefaultTargetDataSource(writer);
            return dataSource;
        }

        @Override
        protected Object determineCurrentLookupKey() {
            boolean active = TransactionSynchronizationManager.isActualTransactionActive();
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            Target target = readOnly ? Target.READER : Target.WRITER;
            if (active) {
                log.debug("routing: {}", target);
            }
            return target;
        }
    }
}
