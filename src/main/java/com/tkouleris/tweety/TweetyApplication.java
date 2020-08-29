package com.tkouleris.tweety;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class TweetyApplication {

	public static void main(String[] args) {
		SpringApplication.run(TweetyApplication.class, args);
	}

	@Bean
	public Docket swaggerConfiguration()
	{
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.paths(PathSelectors.ant("/tweety/**"))
				.build()
				.apiInfo(apiDetails());
		
	}
	
	private ApiInfo apiDetails()
	{
		return new ApiInfo("Tweety API"
			, "Twitter clone in Spring Boot. Allows user creation, user can follow another user, tweet and comment."
			, "1.0"
			, "Free to use"
			, new springfox.documentation.service.Contact("Thodoris Kouleris", "http://tkouleris.eu/", "tkouleris@gmail.com")
			, "http://tkouleris.eu/"
			,null, Collections.emptyList());
	}
}
