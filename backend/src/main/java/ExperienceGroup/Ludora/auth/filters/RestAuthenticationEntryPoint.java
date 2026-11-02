package ExperienceGroup.Ludora.auth.filters;

import ExperienceGroup.Ludora.common.exception.dto.ErrorResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;


@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Autowired
    public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        int status;
        String errorMessage;
        String error;

        if (authException instanceof LockedException) {
            status = HttpServletResponse.SC_FORBIDDEN;
            error = "Forbidden";
            errorMessage = "Las credenciales están bloqueadas";
        } else {
            status = HttpServletResponse.SC_UNAUTHORIZED;
            error = "Unauthorized";

            errorMessage = switch (authException) {
                case BadCredentialsException e ->
                        "Invalid credentials";
                case CredentialsExpiredException e ->
                        "Credentials expired";
                case InsufficientAuthenticationException e ->
                        "Insufficient authentication";
                case AuthenticationServiceException e ->
                        "Authentication service error";
                default ->
                        "Authentication error: " + authException.getMessage();
            };
        }

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                LocalDateTime.now(),
                status,
                error,
                errorMessage
        );

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}

