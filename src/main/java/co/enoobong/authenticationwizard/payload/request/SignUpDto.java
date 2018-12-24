package co.enoobong.authenticationwizard.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignUpDto {
    @NotBlank
    @Size(max = 40)
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank
    @Size(max = 40)
    @JsonProperty("last_name")
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public SignUpDto() {
        //Empty Constructor for (de)serialization
    }

    public SignUpDto(@NotBlank @Size(max = 40) String firstName, @NotBlank @Size(max = 40) String lastName, @Email @NotBlank String email, @NotBlank String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
