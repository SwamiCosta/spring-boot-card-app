package com.example.cardapp.security;

import com.example.cardapp.service.TokenService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Interceptor to simulate authentication based on a Bearer token.
 * It validates static hardcoded tokens and dynamically generated time-based tokens.
 * It sets the AuthContext with the authenticated user's information.
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Value("${auth.token.static.prepaid-user}")
    private String staticPrepaidUserToken;

    @Value("${auth.token.static.limited-user}")
    private String staticLimitedUserToken;

    private final Map<String, AuthUser> staticValidTokens = new ConcurrentHashMap<>();

    private final TokenService tokenService;

    @Autowired
    public AuthInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Initializes the staticValidTokens map after properties are set.
     * Using @PostConstruct to ensure properties are injected before this runs.
     */
    @PostConstruct
    public void initStaticTokens() {
        staticValidTokens.put(staticPrepaidUserToken, new AuthUser("static_prepaid_user", AuthUser.Role.PREPAID_ONLY));
        staticValidTokens.put(staticLimitedUserToken, new AuthUser("static_limited_user", AuthUser.Role.LIMITED_USE_ONLY));
    }

    /**
     * Intercepts incoming requests to perform authentication.
     *
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param handler The handler (controller method) that will be executed.
     * @return True if authentication is successful, false otherwise.
     * @throws Exception if an error occurs during interception.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getRequestURI().equals("/api/auth/login")) {
            return true;
        }

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized: Missing or invalid Authorization header.");
            return false;
        }

        String token = authorizationHeader.substring(7);

        AuthUser authUser = staticValidTokens.get(token);
        if (authUser != null) {
            AuthContext.setCurrentUser(authUser);
            return true;
        }

        Optional<AuthUser> timeBasedAuthUser = tokenService.validateTimeBasedToken(token);
        if (timeBasedAuthUser.isPresent()) {
            AuthContext.setCurrentUser(timeBasedAuthUser.get());
            return true;
        }

        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized: Invalid or expired token.");
        return false;
    }

    /**
     * Cleans up the AuthContext after the request is completed.
     *
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param handler The handler (controller method) that was executed.
     * @param ex Any exception thrown during handler execution.
     * @throws Exception if an error occurs during post-completion.
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthContext.clear();
    }
}