package co.enoobong.authenticationwizard.payload.response;

public class MessageResponse {

    private String message;

    public MessageResponse() {
        //Empty Constructor for (de)serialization
    }

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
