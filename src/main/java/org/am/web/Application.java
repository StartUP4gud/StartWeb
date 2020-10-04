package org.am.web;

import org.apache.camel.Exchange;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@ComponentScan
@EntityScan("org.am.web.entity")
@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories("org.am.web.dao")
@PropertySources({@PropertySource(value = "file:/etc/nf/conf/flowgen.properties")})
public class Application {

	public static void main(String[] args) {
		new Application().trigger(null);
	}

	public void trigger(Exchange exchange){

		ApplicationContext ctx = SpringApplication.run(Application.class);
		
	}

}