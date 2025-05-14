package com.moneyminder.moneyminderexpenses.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    @InjectMocks
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("secured endpoints security filter chain bean test")
    void securedEndpointsSecurityFilterChainTest() throws Exception {
        HttpSecurity httpSecurity = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        SecurityBuilder<SecurityFilterChain> builder = mock(SecurityBuilder.class);
        DefaultSecurityFilterChain securityFilterChain = mock(DefaultSecurityFilterChain.class);

        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.oauth2ResourceServer(any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(securityFilterChain);

        SecurityFilterChain result = securityConfig.securedEndpoints(httpSecurity);

        assertNotNull(result);
        verify(httpSecurity).authorizeHttpRequests(any());
        verify(httpSecurity).csrf(any());
        verify(httpSecurity).oauth2ResourceServer(any());
        verify(httpSecurity).build();
    }

    @Test
    @DisplayName("jwtAuthenticationConverter bean created test")
    void jwtAuthenticationConverterBeanCreationTest() {
        JwtAuthenticationConverter converter = securityConfig.jwtAuthenticationConverter();
        assertNotNull(converter);
    }

}