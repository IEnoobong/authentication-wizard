package co.enoobong.authenticationwizard.event;

import co.enoobong.authenticationwizard.model.User;

public class SignUpCompleteEvent {

    private final User user;
    private final String baseUrl;

    public SignUpCompleteEvent(User user, String baseUrl) {
        this.user = user;
        this.baseUrl = baseUrl;
    }

    public User getUser() {
        return user;
    }

    String getBaseUrl() {
        return baseUrl;
    }
}
