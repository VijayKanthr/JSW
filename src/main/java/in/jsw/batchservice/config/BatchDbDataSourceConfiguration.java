package in.jsw.batchservice.config;

import com.zaxxer.hikari.HikariDataSource;
import in.jsw.batchservice.model.batch.BatchJobInstance;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "in.jsw.batchservice.repository.batch",
        entityManagerFactoryRef = "batchEntityManagerFactory",
        transactionManagerRef= "batchTransactionManager"
)
public class BatchDbDataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.batch")
    public DataSourceProperties batchDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.batch.configuration")
    public DataSource batchDataSource() {
        return batchDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "batchEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean batchEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(batchDataSource())
                .packages(BatchJobInstance.class)
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager batchTransactionManager(
            final @Qualifier("batchEntityManagerFactory") LocalContainerEntityManagerFactoryBean
                    batchEntityManagerFactory) {
        return new JpaTransactionManager(batchEntityManagerFactory.getObject());
    }



}
