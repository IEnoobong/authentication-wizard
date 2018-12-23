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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SignUpDto signUpDto = (SignUpDto) o;

        if (!email.equals(signUpDto.email)) return false;
        return password.equals(signUpDto.password);
    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }
}
