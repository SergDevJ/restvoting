package ru.ssk.restvoting.config;


import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"ru.ssk.restvoting.model",
        "ru.ssk.restvoting.repository",
        "ru.ssk.restvoting.service",
        "ru.ssk.restvoting.util"})
@EnableJpaRepositories(basePackages = {"ru.ssk.restvoting.repository"})

public class DataJpaConfig {
    @Autowired
    private Environment env;

    @Profile("hsqldb")
    @Bean("dataSource")
    public javax.sql.DataSource hsqldbDataSource() {
        DriverManagerDataSource dataSource = new org.springframework.jdbc.datasource.DriverManagerDataSource();
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(new ClassPathResource("db/hsqldb.properties").getFile()));
        } catch (IOException e) {
            throw new IllegalStateException("Error creating HSQLDB datasource", e);
        }
        dataSource.setDriverClassName(props.getProperty("database.driverClassName"));
        dataSource.setUrl(props.getProperty("database.url"));
        dataSource.setUsername(props.getProperty("database.username"));
        dataSource.setPassword(props.getProperty("database.password"));

        // schema init
        //https://stackoverflow.com/questions/38040572/spring-boot-loading-initial-data/38047021#38047021
        Resource initSchema = new ClassPathResource("db/initDB_hsql.sql");
        Resource initData = new ClassPathResource("db/populateDB.sql");
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchema, initData);
        databasePopulator.setSqlScriptEncoding("UTF-8");
        DatabasePopulatorUtils.execute(databasePopulator, dataSource);
        return dataSource;
    }

    @Profile("postgres")
    @Bean("dataSource")
    public javax.sql.DataSource postgresDataSource() {
        PoolProperties poolProperties = new org.apache.tomcat.jdbc.pool.PoolProperties();
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(new ClassPathResource("db/postgres.properties").getFile()));
        } catch (IOException e) {
            throw new IllegalStateException("Error creating Postgres datasource", e);
        }
        poolProperties.setDriverClassName(props.getProperty("database.driverClassName"));
        poolProperties.setUrl(props.getProperty("database.url"));
        poolProperties.setUsername(props.getProperty("database.username"));
        poolProperties.setPassword(props.getProperty("database.password"));
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource(poolProperties);
        dataSource.setPoolProperties(poolProperties);

        // schema init
        //https://stackoverflow.com/questions/38040572/spring-boot-loading-initial-data/38047021#38047021
        Resource initSchema = new ClassPathResource("db/initDB.sql");
        Resource initData = new ClassPathResource("db/populateDB.sql");
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchema, initData);
        databasePopulator.setSqlScriptEncoding("UTF-8");
        DatabasePopulatorUtils.execute(databasePopulator, dataSource);
        return dataSource;
    }

    @Profile("heroku")
    @Bean("dataSource")
    public javax.sql.DataSource herokuPostgresDataSource() {
        URI dbUri;
        try {
            dbUri = new URI(System.getenv("DATABASE_URL"));
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Error creating Heroku Postgresql datasource.", e);
        }

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        PoolProperties poolProperties = new org.apache.tomcat.jdbc.pool.PoolProperties();
        poolProperties.setDriverClassName("org.postgresql.Driver");
        poolProperties.setUrl(dbUrl);
        poolProperties.setUsername(username);
        poolProperties.setPassword(password);
        poolProperties.setRemoveAbandoned(true);
        poolProperties.setValidationQuery("SELECT 1");
        poolProperties.setMaxActive(10);
        poolProperties.setMinIdle(2);
        poolProperties.setMaxWait(20000);
        poolProperties.setInitialSize(2);
        poolProperties.setMaxIdle(5);
        poolProperties.setTestOnBorrow(true);
        poolProperties.setTestOnConnect(true);
        poolProperties.setTestWhileIdle(true);
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource(poolProperties);
        dataSource.setPoolProperties(poolProperties);
        return dataSource;
    }

    @Bean
    PlatformTransactionManager transactionManager(@Autowired EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public Properties hibernateProperties() {
        Properties hibernateProp = new Properties();
        List<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains("postgres") || activeProfiles.contains("heroku")) {
            hibernateProp.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        }
        if (activeProfiles.contains("hsqldb")) {
            hibernateProp.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        }
        hibernateProp.put("hibernate.format_sql", true);
        hibernateProp.put("hibernate.use_sql_comments", true);
        hibernateProp.put("hibernate.show_sql", true);
        hibernateProp.put("hibernate.max_fetch_depth", 3);
        hibernateProp.put("hibernate.jpa.compliance.proxy", false);
        hibernateProp.put("hibernate.jdbc.batch_size", 10);
        hibernateProp.put("hibernate.jdbc.fetch_size", 50);

        hibernateProp.put("hibernate.cache.region.factory_class", "org.hibernate.cache.jcache.internal.JCacheRegionFactory");
        hibernateProp.put("org.ehcache.jsr107.EhcacheCachingProvider", "org.ehcache.jsr107.EhcacheCachingProvider");
        if (activeProfiles.contains("test")) {
            hibernateProp.put("hibernate.cache.use_second_level_cache", "false");
            hibernateProp.put("hibernate.cache.use_query_cache", "false");
        } else {
            hibernateProp.put("hibernate.cache.use_second_level_cache", "true");
            hibernateProp.put("hibernate.cache.use_query_cache", "true");
        }

        hibernateProp.put("javax.persistence.validation.group.pre-persist", "ru.ssk.restvoting.util.validation.ValidationGroup$Persist");
        hibernateProp.put("javax.persistence.validation.group.pre-update", "ru.ssk.restvoting.util.validation.ValidationGroup$Persist");
        return hibernateProp;
    }

    @Bean
    @Autowired
    public EntityManagerFactory entityManagerFactory(javax.sql.DataSource dataSource,
            Properties hibernateProperties, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan("ru.ssk.restvoting.model");
        factoryBean.setDataSource(dataSource);
        factoryBean.setJpaProperties(hibernateProperties);
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

}
