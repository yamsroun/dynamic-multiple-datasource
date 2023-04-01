package yamsroun.multids.config.datasource.property;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import yamsroun.multids.config.datasource.type.DataSourceGroupType;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
//@ExtendWith(SpringExtension.class)
//@EnableConfigurationProperties(MultipleDataSourceProperties.class)
//@TestPropertySource("classpath:multiple-datasource.yml")
@ActiveProfiles("test")
@SpringBootTest
class MultipleDataSourcePropertiesTest {

    @Autowired
    MultipleDataSourceProperties props;

    @Test
    void basic() {
        log.info(">>> Properties={}", props);
        props.getGroups()
            .forEach((name, props) -> log.info(">>> {} {}={}",
                props.isCluster() ? DataSourceGroupType.CLUSTER : DataSourceGroupType.SINGLE,
                name, props));
        assertThat(props).isNotNull();
        assertThat(props.getGroups())
            .isNotNull()
            .isNotEmpty();
    }
}