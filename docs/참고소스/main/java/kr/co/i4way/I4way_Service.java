/*
package kr.co.i4way;

import org.springframework.boot.SpringApplication; 
import org.springframework.boot.autoconfigure.SpringBootApplication; 
import org.springframework.boot.builder.SpringApplicationBuilder; 
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//외장Tomcat 사용시
@SpringBootApplication
//@EnableScheduling
public class I4way_Service extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(I4way_Service.class, args);
	}
	
	@Override 
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) { 
		return builder.sources(I4way_Service.class); 
	}
}
*/

/* 내장Tomcat 사용시*/

package kr.co.i4way;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableScheduling
public class I4way_Service {

	public static void main(String[] args) {
		SpringApplication.run(I4way_Service.class, args);
	} 
}

