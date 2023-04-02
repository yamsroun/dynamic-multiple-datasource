package yamsroun.multids.config.datasource;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import yamsroun.multids.config.datasource.factory.DataSourceGroupFactory;

import javax.annotation.PostConstruct;

@Profile("!test")
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class MultipleDataSourceConfig {

    private final DataSourceGroupFactory dataSourceGroupFactory;

    @PostConstruct
    private void postConstruct() {
        dataSourceGroupFactory.initialize();
    }
}
