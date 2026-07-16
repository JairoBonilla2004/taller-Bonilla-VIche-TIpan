package com.reservas.controller;

import com.reservas.dto.UsuarioResponse;
import com.reservas.entity.Usuario;
import com.reservas.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Evidencia la vulnerabilidad #4 (IDOR):
 * El controlador permite consultar recursos por id sin validar
 * que el solicitante sea dueño del recurso o administrador.
 */
@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerIdorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    void obtenerUsuarioPorId_sinAutenticacion_yConIdArbitrario_devuelve200() throws Exception {
        Long idObjetivo = 2L;
        UsuarioResponse victima = new UsuarioResponse(
                idObjetivo,
                "Usuario Víctima",
                "victima@reservas.com",
                Usuario.Rol.CLIENTE,
                "0999999999",
                "Atención",
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(usuarioService.obtenerUsuarioPorId(idObjetivo)).thenReturn(Optional.of(victima));

        mockMvc.perform(get("/api/usuarios/{id}", idObjetivo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(2))
                .andExpect(jsonPath("$.email").value("victima@reservas.com"));
    }
}
