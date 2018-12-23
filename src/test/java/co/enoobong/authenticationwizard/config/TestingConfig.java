package co.enoobong.authenticationwizard.config;

import co.enoobong.authenticationwizard.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

@Configuration
@Profile("test")
public class TestingConfig {

    @Bean
    public UserRepository userRepository() {
        return mock(UserRepository.class);
    }
}
