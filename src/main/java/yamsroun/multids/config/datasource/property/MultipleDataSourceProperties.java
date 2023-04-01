package yamsroun.multids.config.datasource.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.*;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "spring.datasource")
public class MultipleDataSourceProperties {

    @Getter
    @NestedConfigurationProperty
    private final Map<String, DataSourceGroupProperties> groups = new HashMap<>(); //groupName, groupProperties
}
