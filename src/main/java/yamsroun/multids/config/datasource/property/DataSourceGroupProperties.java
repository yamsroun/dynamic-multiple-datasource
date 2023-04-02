package yamsroun.multids.config.datasource.property;

import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import yamsroun.multids.config.datasource.exception.InvalidDataSourcePropertiesException;
import yamsroun.multids.config.datasource.type.DataSourceGroupType;
import yamsroun.multids.config.datasource.type.DataSourceNameType;

import java.util.Objects;

@ConstructorBinding
public class DataSourceGroupProperties {

    private final boolean primary;
    private final DataSourceGroupType type;

    @NestedConfigurationProperty
    private final EachDataSourceProperties writer;

    @NestedConfigurationProperty
    private final EachDataSourceProperties reader;

    @NestedConfigurationProperty
    private final EachDataSourceProperties single;

    public DataSourceGroupProperties(
        DataSourceGroupType type,
        Boolean primary,
        EachDataSourceProperties writer,
        EachDataSourceProperties reader,
        EachDataSourceProperties single
    ) {
        this.primary = Objects.requireNonNullElse(primary, false);
        this.type = Objects.requireNonNullElse(type, DataSourceGroupType.SINGLE);
        if (this.type.isCluster()) {
            if (Objects.isNull(writer)) {
                throw new InvalidDataSourcePropertiesException("CLUSTER 타입인 데이터소스 그룹에는 writer 데이터소스가 필수입니다.");
            }
            if (Objects.isNull(reader)) {
                throw new InvalidDataSourcePropertiesException("CLUSTER 타입인 데이터소스 그룹에는 reader 데이터소스가 필수입니다.");
            }
        } else {
            if (Objects.isNull(single)) {
                throw new InvalidDataSourcePropertiesException("SINGLE 타입인 데이터소스 그룹에는 single 데이터소스가 필수입니다.");
            }
        }

        this.writer = writer;
        this.reader = reader;
        this.single = single;
    }

    public boolean isPrimary() {
        return primary;
    }

    public boolean isCluster() {
        return type == DataSourceGroupType.CLUSTER;
    }

    public boolean isSingle() {
        return type == DataSourceGroupType.SINGLE;
    }

    public EachDataSourceProperties getEachProperties(DataSourceNameType nameType) {
        if (nameType == DataSourceNameType.WRITER && isCluster()) {
            return writer;
        }
        if (nameType == DataSourceNameType.READER && isCluster()) {
            return reader;
        }
        if (nameType == DataSourceNameType.SINGLE && isSingle()) {
            return single;
        }
        return null;
    }

    public EachDataSourceProperties getWriterProperties() {
        return isCluster() ? writer : null;
    }

    public EachDataSourceProperties getReaderProperties() {
        return isCluster() ? reader : null;
    }

    public EachDataSourceProperties getSingleProperties() {
        return isSingle() ? single : null;
    }
}
