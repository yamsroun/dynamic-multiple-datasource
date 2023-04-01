package yamsroun.multids.config.datasource.property;

import lombok.Getter;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Objects;

@Getter
@ConstructorBinding
public class EachDataSourceProperties {

    private final String jdbcUrl;
    private final String username;
    private final String password;
    private final int minimumIdle;
    private final int maximumPoolSize;

    public EachDataSourceProperties(
        String jdbcUrl,
        String username,
        String password,
        Integer minimumIdle,
        Integer maximumPoolSize
    ) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.minimumIdle = Objects.requireNonNullElse(minimumIdle, 10);
        this.maximumPoolSize = Objects.requireNonNullElse(maximumPoolSize, 10);
    }
}
