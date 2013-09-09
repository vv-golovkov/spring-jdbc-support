package com.ericsson.springsupport.jdbc.datasource;

import java.util.Properties;

import javax.naming.Context;
import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import com.ericsson.springsupport.jdbc.logging.SmartLogger;

public class DataSourceProvider implements DataSourceAware {
    private static final SmartLogger logger = SmartLogger.getLogger(DataSourceProvider.class);
    private DataSource dataSource;

    private String datasourceJndiName;
    private String contextFactory;
    private String providerUrl;

    public DataSourceProvider(String providerUrl, String contextFactory, String jndi) {
        this.contextFactory = contextFactory;
        this.providerUrl = providerUrl;
        this.datasourceJndiName = jndi;
    }

    public DataSourceProvider(DataSourceAware customDataSource) {
        if (customDataSource != null) {
            dataSource = customDataSource.getDataSource();
        } else {
            logger.info("Custom data source is null.");
        }
    }

    public synchronized DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = lookUpDataSource();
        }
        return dataSource;
    }

    private DataSource lookUpDataSource() {
        Properties jndiEnvironment = new Properties();
        jndiEnvironment.setProperty(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
        jndiEnvironment.setProperty(Context.PROVIDER_URL, providerUrl);

        JndiDataSourceLookup jndiDataSourceLookUp = new JndiDataSourceLookup();
        jndiDataSourceLookUp.setJndiEnvironment(jndiEnvironment);
        return jndiDataSourceLookUp.getDataSource(datasourceJndiName);
    }

}
