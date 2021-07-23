package com.epam.esm.web.controller;

import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.maintenance.UserService;
import com.epam.esm.web.model.AuthenticationInfo;
import com.epam.esm.web.security.TokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller for authentication operations
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    public AuthenticationController(AuthenticationManager authenticationManager, UserService userService,
                                    TokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    /**
     * Authenticate in system
     *
     * @param userDto user login parameters
     * @return authentication info
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationInfo> authenticate(@RequestBody UserDto userDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getLogin(), userDto.getPassword()));
        UserDto user = userService.getByLogin(userDto.getLogin());
        String token = tokenProvider.createToken(user.getLogin(), user.getRoles());
        AuthenticationInfo authenticationInfo = new AuthenticationInfo(user.getLogin(), token, user.getRoles());
        return ResponseEntity.ok(authenticationInfo);
    }

    /**
     * Logout from system
     *
     * @param request  http request
     * @param response http response
     */
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}