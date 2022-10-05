package com.gitd.iws.csv;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import com.gitd.iws.csv.CsvFrame;
import java.awt.EventQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class CsvApplication {

        @Autowired
    JdbcTemplate jdbcTemplate;
    public static void main(String[] args) {
        //	SpringApplication.run(SpringApplication.class, args);


        /*
                ConfigurableApplicationContext context = 
                        new SpringApplicationBuilder(CsvApplication.class).run(args);
               

		CsvFrame appFrame = context.getBean(CsvFrame.class);
		appFrame.setVisible(true);
         */
 /*
                CsvFrame c= new CsvFrame();
                c.setVisible(true);
         */
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(CsvApplication.class)
                .headless(false).run(args);

        EventQueue.invokeLater(() -> {
            CsvFrame ex = ctx.getBean(CsvFrame.class);
            ex.setVisible(true);
        });

        /*
        ConfigurableApplicationContext context = new SpringApplicationBuilder(CsvApplication.class).headless(false).run(args);
        CsvFrame appFrame = context.getBean(CsvFrame.class);
         */
    }

}
