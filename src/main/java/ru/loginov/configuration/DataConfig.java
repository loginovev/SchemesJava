package ru.loginov.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

/**
 *
 */
@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class DataConfig {

    @Resource
    private Environment env;

    private static final String[] PROP_ENTITYMANAGER_PACKAGES_TO_SCAN = {
            "ru.loginov.security.entity",
            "ru.loginov.references.scheme.entity"
    };

    private static final String PROP_DATABASE_DRIVER = "db.driver";
    private static final String PROP_DATABASE_USERNAME = "db.username";
    private static final String PROP_DATABASE_PASSWORD = "db.password";
    private static final String PROP_DATABASE_NAME = "db.name";
    private static final String PROP_DATABASE_SERVER = "db.server";
    private static final String PROP_DATABASE_MAX_POOL_SIZE = "db.max-pool-size";

    private static final String PROP_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String PROP_HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";

    @Bean
    public DataSource dataSource(){

        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(env.getRequiredProperty(PROP_DATABASE_DRIVER));
        config.setUsername(env.getRequiredProperty(PROP_DATABASE_USERNAME));
        config.setPassword(env.getRequiredProperty(PROP_DATABASE_PASSWORD));

        config.addDataSourceProperty("databaseName", env.getRequiredProperty(PROP_DATABASE_NAME));
        config.addDataSourceProperty("serverName", env.getRequiredProperty(PROP_DATABASE_SERVER));
        config.setMaximumPoolSize(env.getRequiredProperty(PROP_DATABASE_MAX_POOL_SIZE,Integer.class));

        return new HikariDataSource(config);
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setPackagesToScan(PROP_ENTITYMANAGER_PACKAGES_TO_SCAN);

        entityManagerFactoryBean.setJpaProperties(hibProperties());

        return entityManagerFactoryBean;
    }

    private Properties hibProperties() {
        Properties properties = new Properties();

        properties.put(PROP_HIBERNATE_HBM2DDL_AUTO, env.getRequiredProperty(PROP_HIBERNATE_HBM2DDL_AUTO));
        properties.put(PROP_HIBERNATE_SHOW_SQL, env.getRequiredProperty(PROP_HIBERNATE_SHOW_SQL));

        properties.put("hibernate.format_sql",true);
        properties.put("hibernate.use_sql_comments",true);

        properties.put("hibernate.globally_quoted_identifiers", true);

        properties.put("hibernate.cache.region.factory_class","org.hibernate.cache.ehcache.EhCacheRegionFactory");
        properties.put("hibernate.generate_statistics",true);

        return properties;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}