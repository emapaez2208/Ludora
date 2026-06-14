package ExperienceGroup.Ludora.auth.filters;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

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

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("error", errorMessage);
        responseData.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        responseData.put("path", request.getRequestURI());

        response.getWriter().write(mapper.writeValueAsString(responseData));

        response.getWriter().flush();
    }
}
