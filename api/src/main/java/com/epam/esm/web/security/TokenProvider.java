package com.epam.esm.web.security;

import com.epam.esm.service.dto.RoleDto;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * Interface with basic methods of security token provider
 */
public interface TokenProvider {

    /**
     * Creates token
     * @param username user login
     * @param roles user roles
     * @return created token
     */
    String createToken(String username, Set<RoleDto> roles);

    /**
     * validates token
     * @param token token value
     * @return true if valid, else - false
     */
    boolean validateToken(String token);

    /**
     * Gets authentication
     * @param token token value
     * @return authentication
     */
    Authentication getAuthentication(String token);

    /**
     * Resolves token
     * @param request http request
     * @return resolved token
     */
    String resolveToken(HttpServletRequest request);
}
