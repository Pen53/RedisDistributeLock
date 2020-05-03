package com.mepeng.cn.pen.common.annotation;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * DruidDataSource 连接池
 */
@Component
public class DataSourceProvider4DruidDS {
    @Autowired
    private Environment env;

    public DataSource buildDataSource() {
        return this.buildDataSource(this.env.getProperty("f4.common.druid.url"), this.env.getProperty("f4.common.druid.driverClassName"), this.env.getProperty("f4.common.druid.username"), this.env.getProperty("f4.common.druid.password"));
    }
    /**
     * 返回 DruidDataSource 数据源
     * @param url
     * @param driver
     * @param user
     * @param password
     * @return
     */
    public DataSource buildDataSource(String url, String driver, String user, String password) {
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl(url);
        ds.setDriverClassName(driver);
        ds.setUsername(user);
        ds.setPassword(password);
        ds.setInitialSize((Integer)this.env.getProperty("druid.initialSize", Integer.class, 5));
        ds.setMinIdle((Integer)this.env.getProperty("druid.minIdle", Integer.class, 5));
        ds.setMaxActive((Integer)this.env.getProperty("druid.maxActive", Integer.class, 20));
        ds.setMaxWait((Long)this.env.getProperty("druid.maxWait", Long.class, 60000L));
        ds.setTimeBetweenEvictionRunsMillis((Long)this.env.getProperty("druid.timeBetweenEvictionRunsMillis", Long.class, 60000L));
        ds.setMinEvictableIdleTimeMillis((Long)this.env.getProperty("druid.minEvictableIdleTimeMillis", Long.class, 300000L));
        ds.setValidationQuery((String)this.env.getProperty("druid.validationQuery", String.class, "SELECT 1 FROM DUAL"));
        ds.setTestWhileIdle((Boolean)this.env.getProperty("druid.testWhileIdle", Boolean.class, true));
        ds.setTestOnBorrow((Boolean)this.env.getProperty("druid.testOnBorrow", Boolean.class, false));
        ds.setTestOnReturn((Boolean)this.env.getProperty("druid.testOnReturn", Boolean.class, false));
        ds.setPoolPreparedStatements((Boolean)this.env.getProperty("druid.poolPreparedStatements", Boolean.class, true));
        ds.setMaxPoolPreparedStatementPerConnectionSize((Integer)this.env.getProperty("druid.maxPoolPreparedStatementPerConnectionSize", Integer.class, 20));
        ds.setRemoveAbandoned((Boolean)this.env.getProperty("druid.removeAbandoned", Boolean.class, true));
        ds.setRemoveAbandonedTimeout((Integer)this.env.getProperty("druid.removeAbandonedTimeout", Integer.class, 1800));
        ds.setLogAbandoned((Boolean)this.env.getProperty("druid.logAbandoned", Boolean.class, true));
        return ds;
    }

    public PlatformTransactionManager buildTransactionManager(DataSource dataSource) {
        DataSourceTransactionManager txn = new DataSourceTransactionManager(dataSource);
        return txn;
    }
}
