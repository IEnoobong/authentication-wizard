package co.enoobong.authenticationwizard.controller;

import co.enoobong.authenticationwizard.payload.request.LoginDto;
import co.enoobong.authenticationwizard.payload.request.SignUpDto;
import co.enoobong.authenticationwizard.payload.response.MessageResponse;
import co.enoobong.authenticationwizard.payload.response.SignUpResponse;
import co.enoobong.authenticationwizard.service.AuthService;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "v1/auth", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("signUp")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpDto signupDto) {
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return ResponseEntity.ok(authService.signUp(signupDto, baseUrl));
    }

    @GetMapping("verifyEmail/{token}")
    public ResponseEntity<MessageResponse> verifyEmail(@PathVariable("token") String token) {
        return ResponseEntity.ok(authService.verifyEmail(token));
    }

    @PostMapping("login")
    public ResponseEntity<MessageResponse> login(@Valid @RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }
}
