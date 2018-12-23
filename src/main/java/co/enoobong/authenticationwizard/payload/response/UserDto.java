package co.enoobong.authenticationwizard.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public class UserDto {

    private long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String email;

    @JsonProperty("registered_at")
    private Instant registeredAt;

    public UserDto() {
        //Empty Constructor for (de)serialization
    }

    public UserDto(long id, String firstName, String lastName, String email, Instant registeredAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registeredAt = registeredAt;
    }

    public long getId() {
        return id;
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

    public Instant getRegisteredAt() {
        return registeredAt;
    }
}
