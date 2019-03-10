package net.secudev.archi;

import javax.servlet.ServletContext;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class ArchiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArchiApplication.class, args);
	}
	
	  @Bean
	    public ModelMapper modelMapper() {
	        return new ModelMapper();
	    }
	  
	  @Bean
	    public Docket apiDocket(ServletContext servletContext) {
	        return new Docket(DocumentationType.SWAGGER_2)	        		
	                .select()
	                .apis(RequestHandlerSelectors.basePackage("net.secudev.archi.api"))  
	                .paths(PathSelectors.any())
	                .build()
	                .useDefaultResponseMessages(false);
	    }
}
