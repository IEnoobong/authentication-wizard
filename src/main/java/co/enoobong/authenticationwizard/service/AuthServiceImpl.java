package co.enoobong.authenticationwizard.service;

import co.enoobong.authenticationwizard.event.SignUpCompleteEvent;
import co.enoobong.authenticationwizard.exception.AppException;
import co.enoobong.authenticationwizard.exception.EmailTakenException;
import co.enoobong.authenticationwizard.exception.ResourceNotFoundException;
import co.enoobong.authenticationwizard.model.Role;
import co.enoobong.authenticationwizard.model.RoleName;
import co.enoobong.authenticationwizard.model.SignUpVerificationToken;
import co.enoobong.authenticationwizard.model.User;
import co.enoobong.authenticationwizard.payload.request.LoginDto;
import co.enoobong.authenticationwizard.payload.request.SignUpDto;
import co.enoobong.authenticationwizard.payload.response.MessageResponse;
import co.enoobong.authenticationwizard.payload.response.SignUpResponse;
import co.enoobong.authenticationwizard.payload.response.UserDto;
import co.enoobong.authenticationwizard.repository.RoleRepository;
import co.enoobong.authenticationwizard.repository.SignUpVerificationRepository;
import co.enoobong.authenticationwizard.repository.UserRepository;
import java.time.LocalDateTime;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger L = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SignUpVerificationRepository signUpVerificationRepository;

    private final PasswordEncoder passwordEncoder;

    private final ApplicationEventPublisher eventPublisher;

    private final AuthenticationManager authenticationManager;

    @Value("${app.verification.expiry.time}")
    private long verificationExpirationInHrs;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ApplicationEventPublisher eventPublisher, SignUpVerificationRepository signUpVerificationRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
        this.signUpVerificationRepository = signUpVerificationRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @NotNull
    public SignUpResponse signUp(@Valid SignUpDto signUpDto, String baseUrl) {
        final String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());

        final Role userRole = roleRepository.findRoleByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException(String.format("%s has not been set", RoleName.ROLE_USER)));

        final User user = new User(signUpDto.getFirstName(), signUpDto.getLastName(), signUpDto.getEmail(), encodedPassword,
                userRole);
        try {
            final User savedUser = userRepository.save(user);
            eventPublisher.publishEvent(new SignUpCompleteEvent(savedUser, baseUrl));
            final UserDto userDto = new UserDto(savedUser.getId(), savedUser.getFirstName(), savedUser.getLastName(), savedUser.getEmail(),
                    savedUser.getCreatedAt());
            return new SignUpResponse(userDto);
        } catch (DataIntegrityViolationException ex) {
            L.error("An error occurred when signing up", ex);
            throw new EmailTakenException(signUpDto.getEmail());
        }
    }

    @Override
    @NotNull
    public MessageResponse verifyEmail(@NotNull String token) {
        final SignUpVerificationToken verificationToken = signUpVerificationRepository.findByToken(token).
                orElseThrow(() -> new ResourceNotFoundException("Verification Token", "token", token));

        if (LocalDateTime.now().isAfter(verificationToken.getExpirationDate())) {
            //Token expired
            throw new IllegalArgumentException("Token has expired");
        }
        final User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        return new MessageResponse("Email has been verified");

    }

    @Override
    @NotNull
    public MessageResponse login(@Valid LoginDto loginDto) {
        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),
                loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new MessageResponse("Successfully logged in");
    }

    @Override
    public void saveVerificationToken(User user, String token) {
        final LocalDateTime localDateTime = LocalDateTime.now().plusHours(verificationExpirationInHrs);
        final SignUpVerificationToken signUpVerificationToken = new SignUpVerificationToken(user, token, localDateTime);
        signUpVerificationRepository.save(signUpVerificationToken);
    }
}
