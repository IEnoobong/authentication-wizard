package co.enoobong.authenticationwizard.service;

import co.enoobong.authenticationwizard.event.SignUpCompleteEvent;
import co.enoobong.authenticationwizard.model.Role;
import co.enoobong.authenticationwizard.model.RoleName;
import co.enoobong.authenticationwizard.model.SignUpVerificationToken;
import co.enoobong.authenticationwizard.model.User;
import co.enoobong.authenticationwizard.payload.request.LoginDto;
import co.enoobong.authenticationwizard.payload.request.SignUpDto;
import co.enoobong.authenticationwizard.payload.response.MessageResponse;
import co.enoobong.authenticationwizard.payload.response.SignUpResponse;
import co.enoobong.authenticationwizard.repository.RoleRepository;
import co.enoobong.authenticationwizard.repository.SignUpVerificationRepository;
import co.enoobong.authenticationwizard.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private SignUpVerificationRepository signUpVerificationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private AuthenticationManager authenticationManager;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService =
                new AuthServiceImpl(
                        userRepository,
                        roleRepository,
                        passwordEncoder,
                        eventPublisher,
                        signUpVerificationRepository,
                        authenticationManager);
    }

    @Test
    void signUpShouldSignUpUser() {
        final SignUpDto signUpDto = new SignUpDto("Ibanga", "Eno", "ibangaenoobong@yahoo.com", "yagba");

        final String encryptedPassword = "$2a$10$Xbp6VzfJUdv4OTFT3tima.Q2nwCedG0T7lTKCHXYqeqGgqW8..gOG";
        given(passwordEncoder.encode(anyString())).willReturn(encryptedPassword);

        final Role userRole = new Role(1L, RoleName.ROLE_USER);
        given(roleRepository.findRoleByName(RoleName.ROLE_USER)).willReturn(Optional.of(userRole));

        final User user =
                new User(
                        signUpDto.getFirstName(),
                        signUpDto.getLastName(),
                        signUpDto.getEmail(),
                        encryptedPassword,
                        userRole);
        user.setId(1L);
        given(userRepository.save(any(User.class))).willReturn(user);

        final SignUpResponse signUpResponse = authService.signUp(signUpDto, "");

        assertEquals(user.getId().longValue(), signUpResponse.getId());

        final InOrder inOrder = inOrder(passwordEncoder, roleRepository, userRepository, eventPublisher);
        inOrder.verify(passwordEncoder).encode(anyString());
        inOrder.verify(roleRepository).findRoleByName(RoleName.ROLE_USER);
        inOrder.verify(userRepository).save(any(User.class));
        inOrder.verify(eventPublisher).publishEvent(any(SignUpCompleteEvent.class));

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void verifyEmailShouldVerifyEmail() {
        final Role userRole = new Role(1L, RoleName.ROLE_USER);
        final User user = new User("Eno", "Ibanga", "ibanga@yagaba.com", "vagaba", userRole);
        final String token = "$2a$10$Xbp6VzfJUdv4OTFT3tima.Q2nwCedG0T7lTKCHXYqeqGgqW8..gOG";
        final LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(1);
        final SignUpVerificationToken verificationToken = new SignUpVerificationToken(user, token, expirationTime);
        given(signUpVerificationRepository.findByToken(anyString())).willReturn(Optional.of(verificationToken));

        final MessageResponse response = authService.verifyEmail(token);

        assertAll("verify email response",
                () -> assertNotNull(response.getMessage()),
                () -> assertFalse(response.getMessage().isEmpty()));

        final InOrder inOrder = inOrder(signUpVerificationRepository, userRepository);
        inOrder.verify(signUpVerificationRepository).findByToken(anyString());
        inOrder.verify(userRepository).save(any(User.class));

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void loginShouldLogin() {
        final LoginDto loginDto = new LoginDto("ibangaenoobong@yahoo.com", "yagaba");
        final Authentication authentication = mock(Authentication.class);
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willReturn(authentication);

        final MessageResponse response = authService.login(loginDto);

        assertAll("login response",
                () -> assertNotNull(response.getMessage()),
                () -> assertFalse(response.getMessage().isEmpty()));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        verifyNoMoreInteractions(authenticationManager);
    }

    @Test
    void saveVerificationTokenShouldSaveToken() {
        final Role userRole = new Role(1L, RoleName.ROLE_USER);
        final User user = new User("Eno", "Ibanga", "ibanga@yagaba.com", "vagaba", userRole);
        final String token = "$2a$10$Xbp6VzfJUdv4OTFT3tima.Q2nwCedG0T7lTKCHXYqeqGgqW8..gOG";

        authService.saveVerificationToken(user, token);

        verify(signUpVerificationRepository).save(any(SignUpVerificationToken.class));
        verifyNoMoreInteractions(signUpVerificationRepository);
    }
}
