package com.software.software_development.core.configuration;

public class Constants {
    public static final String API_URL = "/api/v1";
    public static final String ADMIN_PREFIX = "/admin";
    public static final String LOGIN_URL = "/login";
    public static final String REGISTER_URL = "/register";
    public static final String AUTH_URL = "/auth";
    public static final String PRODUCTS_URL = "/products";
    public static final String REVIEWS_URL = "/reviews";
    public static final String PURCHASES_URL = "/purchases";
    public static final String CATEGORIES_URL = "/categories";
    public static final String DEFAULT_PAGE_SIZE = "10";

    public static final String PASSWORD_PATTERN = "^.{6,60}$";
    public static final String LOGIN_PATTERN = "^[a-zA-Z0-9_]{3,30}$";
    public static final String LOG_PATH = "logs";

    private Constants() {
    }
}
