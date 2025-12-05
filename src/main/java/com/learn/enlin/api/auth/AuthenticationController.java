package com.learn.enlin.api.auth;

import com.learn.enlin.api.auth.dto.request.AuthenticationRequest;
import com.learn.enlin.api.auth.dto.request.IntrospectRequest;
import com.learn.enlin.api.auth.dto.request.LogoutRequest;
import com.learn.enlin.api.auth.dto.request.RefreshRequest;
import com.learn.enlin.api.auth.dto.response.AuthenticationResponse;
import com.learn.enlin.api.auth.dto.response.IntrospectResponse;
import com.learn.enlin.api.auth.service.AuthenticationService;
import com.learn.enlin.base.ApiResponse;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController{
    AuthenticationService authenticationService;

    @PostMapping("/login")
    ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest request, HttpServletResponse response){
        var result = authenticationService.authenticate(request);
        // Tạo cookie an toàn
        ResponseCookie cookie = ResponseCookie.from("auth_token", result.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        
        ApiResponse<AuthenticationResponse> apiResponse = ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> authenticate(@CookieValue("auth_token") String token, HttpServletResponse response)
            throws ParseException, JOSEException {
        RefreshRequest request = RefreshRequest.builder()
                .token(token)
                .build();
        var result = authenticationService.refreshToken(request);
        
        ResponseCookie cookie = ResponseCookie.from("auth_token", result.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .build();
    }
}
