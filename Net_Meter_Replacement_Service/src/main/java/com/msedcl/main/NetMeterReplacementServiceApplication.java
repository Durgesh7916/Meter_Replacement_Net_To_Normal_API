package com.msedcl.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.msedcl.main.config.DataSourceProperties;

@SpringBootApplication
@EnableConfigurationProperties(DataSourceProperties.class)
public class NetMeterReplacementServiceApplication  extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(NetMeterReplacementServiceApplication.class, args);
	}

	
	 @Override
	    protected SpringApplicationBuilder configure(
	            SpringApplicationBuilder application) {

	        return application.sources(
	        		NetMeterReplacementServiceApplication.class
	        );
	    }

}
