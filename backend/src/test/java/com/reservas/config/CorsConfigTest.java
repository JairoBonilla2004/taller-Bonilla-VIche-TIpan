package com.reservas.config;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Evidencia la vulnerabilidad #3:
 * CORS acepta cualquier origen con credenciales habilitadas.
 */
class CorsConfigTest {

    @Test
    void corsConfig_combinaWildcardConCredenciales_habilitandoOrigenesArbitrarios() {
        CorsConfig corsConfig = new CorsConfig();
        CorsConfigurationSource source = corsConfig.corsConfigurationSource();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/usuarios/2");

        CorsConfiguration config = source.getCorsConfiguration(request);

        assertNotNull(config);
        assertTrue(config.getAllowedOriginPatterns().contains("*"),
                "La configuración permite cualquier origen mediante allowedOriginPatterns='*'");
        assertTrue(Boolean.TRUE.equals(config.getAllowCredentials()),
                "La configuración permite credenciales en solicitudes CORS");
    }
}
