package co.enoobong.authenticationwizard.controller;

import co.enoobong.authenticationwizard.config.TestingConfig;
import co.enoobong.authenticationwizard.payload.request.LoginDto;
import co.enoobong.authenticationwizard.payload.request.SignUpDto;
import co.enoobong.authenticationwizard.payload.response.MessageResponse;
import co.enoobong.authenticationwizard.payload.response.SignUpResponse;
import co.enoobong.authenticationwizard.payload.response.UserDto;
import co.enoobong.authenticationwizard.security.UserDetailsServiceImpl;
import co.enoobong.authenticationwizard.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(AuthController.class)
@Import({UserDetailsServiceImpl.class, TestingConfig.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    private static String toJsonString(Object value) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper.writeValueAsString(value);
    }

    @Test
    void signUpShouldSignUpUser() throws Exception {
        final SignUpDto signUpDto = new SignUpDto("Ibanga", "Eno", "ibangaenoobong@yahoo.com", "yagba");
        final UserDto userDto = new UserDto(1, signUpDto.getFirstName(), signUpDto.getLastName(), signUpDto.getEmail(), Instant.now());
        final SignUpResponse signUpResponse = new SignUpResponse(userDto);
        given(authService.signUp(any(SignUpDto.class), anyString())).willReturn(signUpResponse);

        mockMvc.perform(post("/v1/auth/signUp")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(toJsonString(signUpDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.user.id").value(userDto.getId()))
                .andExpect(jsonPath("$.user.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.user.first_name").value(userDto.getFirstName()))
                .andExpect(jsonPath("$.user.last_name").value(userDto.getLastName()))
                .andExpect(jsonPath("$.user.registered_at").exists());
    }

    @Test
    void verifyEmailShouldVerifyEmail() throws Exception {
        final String token = "4be8fede-a705-44e0-b6f6-e9ad0859f9e0";
        final MessageResponse response = new MessageResponse("Email has been verified");
        given(authService.verifyEmail(token)).willReturn(response);

        mockMvc.perform(get("/v1/auth/verifyEmail/" + token)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message").value(response.getMessage()));
    }

    @Test
    void loginShouldLogin() throws Exception {
        final LoginDto loginDto = new LoginDto("ibangaenoobong@yahoo.com", "yagaba");
        final MessageResponse response = new MessageResponse("Successfully logged in");
        given(authService.login(any(LoginDto.class))).willReturn(response);

        mockMvc.perform(post("/v1/auth/login/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(toJsonString(loginDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message").value(response.getMessage()));
    }
}
