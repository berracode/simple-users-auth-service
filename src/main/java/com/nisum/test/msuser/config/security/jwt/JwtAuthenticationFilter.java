package com.nisum.test.msuser.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nisum.test.msuser.dtos.UserLoginDto;
import com.nisum.test.msuser.dtos.UserLoginResponseDto;
import com.nisum.test.msuser.exceptions.UnknownErrorException;
import com.nisum.test.msuser.utils.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static com.nisum.test.msuser.constants.Constants.BEARER;
import static com.nisum.test.msuser.constants.Constants.UNKNOWN_SERVER_ERROR;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        var userLoginDto = new UserLoginDto();
        try {
            userLoginDto = new ObjectMapper().readValue(request.getReader(), UserLoginDto.class);
        } catch (Exception e) {
            logger.error("Error mapping request object");
            throw new UnknownErrorException(UNKNOWN_SERVER_ERROR, e);
        }
        var userPAT = new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword(),
            Collections.emptyList());
        return getAuthenticationManager().authenticate(userPAT);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            org.springframework.security.core.Authentication authResult) throws IOException,
        ServletException {
        String token = JWTUtils.generateToken(authResult);
        response.setContentType("application/json");
        var userAuth = UserLoginResponseDto.builder().token(token).tokenType(BEARER).build();
        var standardResponse = new ApiResponse<>(userAuth);
        var writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(standardResponse));
        writer.close();

        response.getWriter().flush();
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
