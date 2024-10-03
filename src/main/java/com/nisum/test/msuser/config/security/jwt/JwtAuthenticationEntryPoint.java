package com.nisum.test.msuser.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nisum.test.msuser.utils.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import static com.nisum.test.msuser.constants.Constants.AUTH_ERROR;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {

        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        var resp = new ApiResponse<>(
            AUTH_ERROR, "Contact with admins"
        );
        response.getWriter().write(new ObjectMapper().writeValueAsString(resp));
    }
}