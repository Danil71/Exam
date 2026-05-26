package com.software.software_development.core.utility;

public final class ValidationUtils {

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private ValidationUtils() {
    }

    public static void validateEmailFormat(String email) {
        if (email == null || !email.matches(EMAIL_PATTERN)) {
            throw new IllegalArgumentException("Email has invalid format: " + email);
        }
    }
}
