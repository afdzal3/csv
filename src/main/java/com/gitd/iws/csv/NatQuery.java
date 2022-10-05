/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gitd.iws.csv;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.h2.jdbcx.JdbcDataSource;

/**
 *
 * @author afdzal
 */
public class NatQuery {

        
        private Connection conn;

        //@Value("${spring.datasource.username}")
        private String username = PropCache.getInstance().getProperty("spring.datasource.username"); 

        //@Value("${spring.datasource.url}")
        private String url = PropCache.getInstance().getProperty("spring.datasource.url");

        //@Value("${spring.datasource.driverClassName}")
        private String driverClassName = PropCache.getInstance().getProperty("spring.datasource.driverClassName");

        //@Value("${spring.datasource.password}")
        private String password = PropCache.getInstance().getProperty("spring.datasource.password");

        public NatQuery() {

            try {
                System.out.println("this.url");
                System.out.println(this.url);
                Class.forName("org.h2.Driver");
                     

                JdbcDataSource dataSource = new JdbcDataSource();
                dataSource.setURL(this.url+";DB_CLOSE_DELAY=-1"); // Set `DB_CLOSE_DELAY` to `-1` to keep in-memory database in existence after connection closes.
                dataSource.setUser(this.username);
                dataSource.setPassword(this.password);
                this.conn = dataSource.getConnection() ;
               // con = DriverManager.getConnection(url);

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CsvData.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(CsvData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        protected void truncateCsvData() {
        String query    = "TRUNCATE TABLE CSVDATA";
        String query2   = "TRUNCATE TABLE CSVDATA2";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
           // stmt.executeUpdate(query2);
        }   catch (SQLException ex) {
                System.out.println("error in truncating");
                ex.printStackTrace();
                Logger.getLogger(NatQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    }
