package io.andersori.led.api.app.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.andersori.led.api.app.web.dto.AccountDto;

@Configuration
@EnableJpaRepositories("io.andersori.led.api.resource.repository")
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
@ComponentScan("io.andersori.led.api")
public class LedConfig {

	@Bean
	public DataSource dataSource() {
		HikariConfig config = new HikariConfig();

		config.setDriverClassName("org.postgresql.Driver");
		config.setJdbcUrl("jdbc:postgresql://localhost:5432/led");
		config.setUsername("led");
		config.setPassword("postgres");

		return new HikariDataSource(config);
	}

	@Bean
	public EntityManagerFactory entityManagerFactory() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setShowSql(false);
		vendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setJpaProperties(additionalProperties());
		factory.setPackagesToScan("io.andersori.led.api.domain.entity");
		factory.setDataSource(dataSource());

		factory.afterPropertiesSet();
		return factory.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);

		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
	
	@Bean
    public AuditorAware<AccountDto> auditorProvider() {
        return new AuditorAwareImpl();
    }
	
	@Bean
	public Validator validator(){
		return Validation.buildDefaultValidatorFactory().getValidator();
	}

	private Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

		return properties;
	}
}
