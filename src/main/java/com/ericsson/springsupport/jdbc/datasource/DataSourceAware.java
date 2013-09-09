package com.ericsson.springsupport.jdbc.datasource;

import javax.sql.DataSource;

public interface DataSourceAware {
    public abstract DataSource getDataSource();
}
