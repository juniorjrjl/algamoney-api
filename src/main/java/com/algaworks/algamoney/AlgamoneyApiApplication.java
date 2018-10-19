package com.algaworks.algamoney;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algamoney.config.property.AlgamoneyApiProperty;

@Configuration
@SpringBootApplication
@EnableConfigurationProperties(AlgamoneyApiProperty.class)
public class AlgamoneyApiApplication {

	@Autowired
	private AlgamoneyApiProperty algamoneyApiProperty;
	
	public static void main(String[] args) {
		SpringApplication.run(AlgamoneyApiApplication.class, args);
	}
	
	/*@Bean
    public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping(algamoneyApiProperty.getSeguranca().getOrigemPermitida())
                	.allowedMethods("POST", "GET", "DELETE", "PUT", "OPTIONS");
            }
        };
    }*/
	
}
