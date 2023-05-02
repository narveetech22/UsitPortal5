package com.narvee.usit.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket getDocket() {
		Contact contact = new Contact("Narvee team", "http://narveetech.com/", "tech@narvee.com");
		List<VendorExtension> extensions = new ArrayList<VendorExtension>();
		ApiInfo apiInfo = new ApiInfo("USIT Portal API documnet", "API to track the clients",
				"narvee_tech snapshot 1.0.1", "https://narveetech.com", contact, "Licence 1172",
				"http://jsonbeautifier.org/", extensions);

		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.narvee.usit")).build().apiInfo(apiInfo)
				.useDefaultResponseMessages(false);
	}

}
