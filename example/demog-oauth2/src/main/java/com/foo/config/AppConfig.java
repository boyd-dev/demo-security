package com.foo.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.foo.myapp"})
@ComponentScan(basePackages = {"com.foo.myapp"})
public class AppConfig {
	
	@Bean
	public DataSource dataSource() {
		
		BasicDataSource dataSource = new BasicDataSource();		
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");		
		dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/mydb");	
		dataSource.setConnectionProperties("serverTimezone=UTC;characterEncoding=UTF-8");
		dataSource.setUsername("scott");
		dataSource.setPassword("1234");
		dataSource.setDefaultAutoCommit(false);
		
		return dataSource;		
	}
	
	
	private Properties getHibernateProperties() {
		Properties props = new Properties();
		props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
		props.setProperty("hibernate.show_sql", "false");		
		props.setProperty("hibernate.jdbc.batch_size", "3");
		props.setProperty("hibernate.jdbc.fetch_size", "100");
		props.setProperty("hibernate.hbm2ddl.auto", "create");
		
		return props;		
	}	
	
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {		
		return new HibernateJpaVendorAdapter();
	}
	
	@Bean
	public EntityManagerFactory entityManagerFactory() {
		
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setPackagesToScan(new String[]{"com.foo.myapp"});
		factoryBean.setDataSource(dataSource());
		factoryBean.setJpaProperties(getHibernateProperties());
		factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
		factoryBean.afterPropertiesSet();
		
		return factoryBean.getNativeEntityManagerFactory();
	}	
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new JpaTransactionManager(entityManagerFactory());
		
	}
	
	
}
