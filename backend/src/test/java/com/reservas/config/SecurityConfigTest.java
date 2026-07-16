package com.reservas.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Estas pruebas evidencian la Vulnerabilidad #2: SecurityConfig permite
 * explícitamente "/api/usuarios" y termina con anyRequest().permitAll(),
 * por lo que endpoints administrativos sensibles quedan accesibles sin
 * autenticación (no hay filtro JWT que rechace peticiones anónimas).
 */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarUsuarios_sinAutenticacion_deberiaSerRechazadoPeroEsPermitido() throws Exception {
        // Ningún header Authorization: una petición completamente anónima.
        mockMvc.perform(get("/api/usuarios"))
                // Lo correcto en un sistema seguro sería 401 Unauthorized.
                // La aserción documenta el comportamiento real (vulnerable): 200 OK.
                .andExpect(status().isOk());
    }

    @Test
    void eliminarUsuario_sinAutenticacionNiRolAdmin_esPermitidoPorConfiguracion() throws Exception {
        // Un endpoint destructivo (desactivar/eliminar usuario) no debería
        // ejecutarse nunca sin credenciales de administrador. Aquí no se
        // verifica que devuelva 4xx: se ejecuta el flujo normal de negocio
        // (404 solo si el id no existe, nunca 401/403 por falta de auth).
        mockMvc.perform(delete("/api/usuarios/999999"))
                .andExpect(status().is4xxClientError()); // 404 de "no encontrado", NO 401/403 de seguridad
    }
}
