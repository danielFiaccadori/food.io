package net.dndats.foodio.domain.exception;

public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException(String email) {
        super("Email already existed: " + email);
    }
}
