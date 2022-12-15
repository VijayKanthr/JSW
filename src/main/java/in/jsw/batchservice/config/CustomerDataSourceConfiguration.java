package in.jsw.batchservice.config;


import in.jsw.batchservice.model.customer.Customer;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "in.jsw.batchservice.repository.customer",
        entityManagerFactoryRef = "customerEntityManagerFactory",
        transactionManagerRef= "customerTransactionManager")
public class CustomerDataSourceConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource.customer")
    public DataSourceProperties cardDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.customer.configuration")
    public DataSource customerDataSource() {
        return cardDataSourceProperties().initializeDataSourceBuilder()
                .type(BasicDataSource.class).build();
    }

    @Bean(name = "customerEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean customerEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(customerDataSource())
                .packages(Customer.class)
                .build();
    }

    @Bean
    public PlatformTransactionManager customerTransactionManager(
            final @Qualifier("customerEntityManagerFactory") LocalContainerEntityManagerFactoryBean customerEntityManagerFactory) {
        return new JpaTransactionManager(customerEntityManagerFactory.getObject());
    }


}
