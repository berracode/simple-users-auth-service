package com.nisum.test.msuser.controllers;

import com.nisum.test.msuser.dtos.UserDto;
import com.nisum.test.msuser.dtos.UserRegisterRequestDto;
import com.nisum.test.msuser.facades.UserFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserFacade userFacade;


    @PostMapping("/register")
    @Operation(summary = "Register user with phones")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Data inserted successfully"),
        @ApiResponse(responseCode = "400", description = "The request is invalid"),
        @ApiResponse(responseCode = "500", description = "Internal error processing response"),
    })
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserRegisterRequestDto request) {
        LOGGER.info("User register has started...");
        var user = userFacade.registerUser(request);
        return ResponseEntity.ok(user);
    }


    @PostMapping("/test-private-endpoint")
    @Operation(summary = "Endpoint de prueba, necesita autenticación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Correct request to Test endpoint"),
        @ApiResponse(responseCode = "400", description = "bad request"),
        @ApiResponse(responseCode = "500", description = "error"),
    })
    public ResponseEntity<com.nisum.test.msuser.utils.ApiResponse<String>> testMethod() {
        LOGGER.info("If you can see this text, you have a valid acces token");
        return ResponseEntity.ok(new com.nisum.test.msuser.utils.ApiResponse<>("User authenticated", "If you can see " +
            "this text, you have a valid access token"));
    }
}