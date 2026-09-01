package com.bridgelabz.employeemanagement.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

public final class SecurityUtil {

    private SecurityUtil() {
    }

    public static String getCurrentUserEmail() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null) {
            return null;
        }

        Object principal =
                authentication.getPrincipal();

        if (principal instanceof OAuth2User oauth2User) {

            return oauth2User.getAttribute("email");
        }

        return authentication.getName();
    }
}