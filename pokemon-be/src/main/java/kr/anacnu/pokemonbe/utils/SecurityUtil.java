package kr.anacnu.pokemonbe.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static String getCurrentStudentId() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null || authentication.getName().equals("anonymousUser")) {
            throw new RuntimeException("이 작업을 수행하려면 로그인이 필요합니다.");
        }

        return authentication.getName();
    }
}
