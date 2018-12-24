package co.enoobong.authenticationwizard.service;

import co.enoobong.authenticationwizard.model.User;
import co.enoobong.authenticationwizard.payload.request.LoginDto;
import co.enoobong.authenticationwizard.payload.request.SignUpDto;
import co.enoobong.authenticationwizard.payload.response.MessageResponse;
import co.enoobong.authenticationwizard.payload.response.SignUpResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface AuthService {

    @NotNull
    SignUpResponse signUp(@Valid SignUpDto signUpDto, String baseUrl);

    @NotNull
    MessageResponse verifyEmail(@NotNull String token);

    @NotNull
    MessageResponse login(@Valid LoginDto loginDto);

    void saveVerificationToken(User user, String token);
}
