package co.enoobong.authenticationwizard.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class LoginDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    public LoginDto() {
        //Empty Constructor for (de)serialization
    }

    public LoginDto(@NotBlank @Email String email, @NotBlank String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
