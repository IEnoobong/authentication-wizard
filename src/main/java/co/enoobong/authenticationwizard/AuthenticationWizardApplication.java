package co.enoobong.authenticationwizard;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication
@EntityScan(basePackageClasses = {
        AuthenticationWizardApplication.class,
        Jsr310JpaConverters.class
})
public class AuthenticationWizardApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationWizardApplication.class, args);
    }

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}

