package com.software.software_development.core.security.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiPathExclusion {
    WEBJARS("/webjars/**"),
    STATICS("/static/**"),
    SWAGGER_UI("/swagger-ui/**"),
    SWAGGER_UI_HTML("/swagger-ui.html"),
    API_DOCS("/v3/api-docs/**");

    private final String path;
}
