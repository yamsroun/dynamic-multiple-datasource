package yamsroun.multids.config.datasource.factory;

import yamsroun.multids.config.datasource.property.EachDataSourceProperties;
import yamsroun.multids.config.datasource.type.DataSourceNameType;

@FunctionalInterface
public interface EachDataSourcePropertiesGetter {

    EachDataSourceProperties get(DataSourceNameType nameType);
}
