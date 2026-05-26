package com.software.software_development.core.configuration;

public class Constants {
    public static final String API_URL = "/api/v1";
    public static final String ADMIN_PREFIX = "/admin";
    public static final String LOGIN_URL = "/login";
    public static final String REGISTER_URL = "/register";
    public static final String AUTH_URL = "/auth";
    public static final String EMPLOYEES_URL = "/employees";
    public static final String DEFAULT_PAGE_SIZE = "10";

    public static final String PHONE_PATTERN = "^((8|\\+7|\\+375)[\\- ]?)?\\(?\\d{3,5}\\)?[\\- ]?\\d{1}[\\- ]?\\d{1}[\\- ]?\\d{1}[\\- ]?\\d{1}[\\- ]?\\d{1}(([\\- ]?\\d{1})?[\\- ]?\\d{1})?$";
    public static final String PASSWORD_PATTERN = "^.{6,60}$";
    public static final String NAME_PATTERN = "^[A-Za-zА-Яа-яЁё\\-\\s]{1,50}$";
    public static final String LOGIN_PATTERN = "^[a-zA-Z0-9_]{3,30}$";
    public static final String LOG_PATH = "logs";

    private Constants() {
    }
}
