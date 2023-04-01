package yamsroun.multids.config.datasource.factory;

import lombok.extern.slf4j.Slf4j;
import yamsroun.multids.config.datasource.util.NamingUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public abstract class AbstractGroupDataSourceFactory {

    /**
     * 애플리케이션 구동 초기에 Reader 커넥션 풀도 함께 생성 처리<p>
     * AbstractRoutingDataSource에 의해 애플리케이션 구동 시 Writer 커넥션 풀만 생성되고
     * 구동 이후, 아래 중 하나의 시점에 Reader 커넥션 풀이 지연 생성되기 때문에,
     * Writer 커넥션 풀 생성과 동일한 시점에 Reader 커넥션 풀을 생성하기 위함
     * <ol>
     * <li>@Transactional(readOnly = true)이 선언된 메소드가 최초 호출될 때</li>
     * <li>애플리케이션 구동 최종 완료 이후 (ApplicationReadyEvent 발생 이후)</li>
     * </ol>
     */
    protected void createExplicitlyConnectionPoolOf(DataSource dataSource, String poolName) {
        log.debug("{} - Creating explicitly connection pool", poolName);
        try (Connection connection = dataSource.getConnection()) {
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    protected String getBeanName(String... str) {
        return NamingUtils.toLowerCamelCase(str);
    }
}
