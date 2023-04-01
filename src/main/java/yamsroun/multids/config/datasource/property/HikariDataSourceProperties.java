package yamsroun.multids.config.datasource.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Properties;

@Getter
@AllArgsConstructor
@ConstructorBinding
@ConfigurationProperties("spring.datasource.hikari")
public class HikariDataSourceProperties {

    private final Integer minimumIdle;
    private final Integer maximumPoolSize;

    private final Long maxLifetime;
    private final Long connectionTimeout;
    private final Long validationTimeout;
    private final Long leakDetectionThreshold;

    private final Properties dataSourceProperties;
}
