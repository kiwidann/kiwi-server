package com.kiwi.kiwiserver.global.security.util;

import com.kiwi.kiwiserver.global.exception.BusinessException;
import com.kiwi.kiwiserver.global.exception.GlobalErrorCode;
import com.kiwi.kiwiserver.global.security.auth.CustomUserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static CustomUserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserPrincipal principal)) {
            throw new BusinessException(GlobalErrorCode.UNAUTHORIZED);
        }

        return principal;
    }

    public static Long getCurrentAccountId() {
        return getCurrentUser().getAccountId();
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getUserId();
    }
}
