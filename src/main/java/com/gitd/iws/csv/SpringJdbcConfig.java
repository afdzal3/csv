/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gitd.iws.csv;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 *
 * @author S52311
 * <p>
 * JDBCtemplate
 * </p>
 */
@Configuration
@ComponentScan(basePackages = {"com.gitd.iws.csv.*"})
public class SpringJdbcConfig {

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;
    
    @Value("${spring.datasource.password}")
    private String password;
    

    
    @Bean(name = "ds")
    @Primary
    public DataSource ds() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate JdbcTemplate(@Qualifier("ds") DataSource ds) {
        System.out.println("reach here datasource");
        return new JdbcTemplate(ds);
    }

}
