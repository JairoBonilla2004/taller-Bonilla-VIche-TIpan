package com.reservas.service;

import com.reservas.dto.LoginRequest;
import com.reservas.dto.LoginResponse;
import com.reservas.entity.Usuario;
import com.reservas.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Estas pruebas evidencian la Vulnerabilidad #1: AuthService no genera ni valida
 * un JWT real. El "token" es un string trivial ("admin-token-" + timestamp) y
 * validarToken() solo comprueba el prefijo, por lo que cualquiera puede
 * falsificar un token válido sin conocer ninguna contraseña.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private Usuario admin;

    @BeforeEach
    void setUp() {
        admin = new Usuario("Administrador", "1234567890", "admin@reservas.com", Usuario.Rol.ADMINISTRADOR);
        admin.setIdUsuario(1L);
        admin.setPassword("$2a$10$hashFalsoDeEjemplo");
        admin.setActivo(true);
    }

    @Test
    void login_conPasswordIncorrecta_debeLanzarExcepcion() {
        LoginRequest request = new LoginRequest("admin@reservas.com", "passwordIncorrecta");

        when(usuarioRepository.findByEmail("admin@reservas.com")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("passwordIncorrecta", admin.getPassword())).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.login(request));
        assertEquals("Contraseña incorrecta", ex.getMessage());
    }

    @Test
    void validarToken_conTokenFalsificadoManualmente_esAceptadoComoValido() {
        // El "atacante" nunca inició sesión: solo construye un string con el
        // prefijo esperado. No requiere contraseña, usuario real, ni firma.
        String tokenForjado = "admin-token-" + System.currentTimeMillis();

        boolean esValido = authService.validarToken(tokenForjado);

        // Esta aserción demuestra la vulnerabilidad: el sistema confía en un
        // token que jamás fue emitido por login(), evidenciando que no hay
        // verificación criptográfica real (no es un JWT firmado).
        assertTrue(esValido, "El token forjado fue aceptado como válido: la validación no verifica autenticidad real");
    }
}
