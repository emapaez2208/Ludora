package ExperienceGroup.Ludora.auth.filters;

import ExperienceGroup.Ludora.common.exception.dto.ErrorResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String errorMessage = switch (authException) {
            case BadCredentialsException badCredentialsException ->
                    "Invalid credentials";
            case DisabledException disabledException ->
                    "Account disabled";
            case LockedException lockedException ->
                    "Account locked";
            case AccountExpiredException accountExpiredException ->
                    "Account expired";
            case CredentialsExpiredException credentialsExpiredException ->
                    "Credentials expired";
            case InsufficientAuthenticationException insufficientAuthenticationException ->
                    "Insufficient authentication";
            case AuthenticationServiceException authenticationServiceException ->
                    "Authentication service error";
            default -> "Authentication error: " + authException.getMessage();
        };

        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized",
                errorMessage
        );

        objectMapper.writeValue(response.getWriter(), error);
    }
}
