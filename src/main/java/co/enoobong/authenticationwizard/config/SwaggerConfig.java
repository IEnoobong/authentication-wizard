package co.enoobong.authenticationwizard.config;

import co.enoobong.authenticationwizard.AuthenticationWizardApplication;
import java.util.ArrayList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    private static ApiInfo generateApiInfo() {
        return new ApiInfo(
                "Authentication Wizard",
                "An account-password backend system in Java, using Spring Boot ",
                "1",
                "urn:tos",
                new Contact("Ibanga Enoobong", "https://www.linkedin.com/in/ienoobong/", "ibangaenoobong@yahoo.com"),
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>()
        );
    }

    @Bean
    public Docket apiDoc() {
        return new Docket(DocumentationType.SPRING_WEB)
                .apiInfo(generateApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(AuthenticationWizardApplication.class.getPackage().getName()))
                .paths(PathSelectors.any())
                .build();

    }
}
