package co.enoobong.authenticationwizard.event;

import co.enoobong.authenticationwizard.model.User;
import co.enoobong.authenticationwizard.service.AuthService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SignUpCompleteListener {

    private final JavaMailSender mailSender;

    private final AuthService authService;

    @Value("${app.support.email}")
    private String supportEmail;

    @Autowired
    public SignUpCompleteListener(JavaMailSender mailSender, AuthService authService) {
        this.mailSender = mailSender;
        this.authService = authService;
    }

    @Async
    @EventListener
    public void handleRegistrationCompleted(SignUpCompleteEvent signUpCompleteEvent) {
        confirmSignUp(signUpCompleteEvent.getUser(), signUpCompleteEvent.getBaseUrl());
    }

    private void confirmSignUp(User user, String baseUrl) {
        final String token = UUID.randomUUID().toString();
        authService.saveVerificationToken(user, token);

        final String recipient = user.getEmail();
        final String subject = "Registration Confirmation";

        final String confirmationUrl = baseUrl + "/v1/auth/verifyEmail/" + token;
        final String message = String.format("Dear %s, %n%nThanks for signing up, kindly verify your email by " +
                "clicking this link %s", user.getFirstName(), confirmationUrl);

        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(recipient);
        simpleMailMessage.setFrom(supportEmail);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);

        mailSender.send(simpleMailMessage);
    }
}
