package co.enoobong.authenticationwizard.payload.response;

public class SignUpResponse {

    private UserDto user;

    public SignUpResponse() {
        //Empty Constructor for (de)serialization
    }

    public SignUpResponse(UserDto user) {
        super();
        this.user = user;
    }

    public UserDto getUser() {
        return user;
    }
}
