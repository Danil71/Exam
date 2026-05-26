package com.software.software_development.core.utility;

import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.software.software_development.model.entity.UserEntity;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityUtils {

    public static User convert(UserEntity user) {
        return new User(String.valueOf(user.getId()), user.getPassword(), Set.of(user.getRole()));
    }

    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new IllegalStateException("User is not authenticated");
        }
        return Long.parseLong(auth.getName());
    }
}
