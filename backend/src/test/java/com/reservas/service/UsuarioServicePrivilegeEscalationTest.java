package com.reservas.service;

import com.reservas.dto.UsuarioRequest;
import com.reservas.dto.UsuarioResponse;
import com.reservas.entity.Usuario;
import com.reservas.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Evidencia la vulnerabilidad #5: Escalada de privilegios en la gestión de usuarios.
 *
 * El servicio permite crear y actualizar usuarios asignando cualquier rol
 * (incluso ADMINISTRADOR) sin verificar la identidad ni el rol de quien
 * realiza la operación. Combinado con SecurityConfig (cualquierRequest permitAll),
 * cualquier petición HTTP anónima puede crear una cuenta de administrador o
 * promover a un usuario existente a ADMINISTRADOR.
 */
@ExtendWith(MockitoExtension.class)
class UsuarioServicePrivilegeEscalationTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioExistente;

    @BeforeEach
    void setUp() {
        usuarioExistente = new Usuario("Cliente Normal", "0999999999", "cliente@reservas.com", Usuario.Rol.CLIENTE);
        usuarioExistente.setIdUsuario(10L);
        usuarioExistente.setPassword("$2a$10$hashCliente");
        usuarioExistente.setActivo(true);
    }

    @Test
    void crearUsuario_conRolADMINISTRADOR_esAceptadoSinValidarIdentidad() {
        UsuarioRequest request = new UsuarioRequest();
        request.setNombre("Atacante");
        request.setEmail("atacante@reservas.com");
        request.setPassword("Password1!");
        request.setRol(Usuario.Rol.ADMINISTRADOR);
        request.setTelefono("0988888888");
        request.setDepartamento("TI");
        request.setActivo(true);

        when(usuarioRepository.existsByEmail("atacante@reservas.com")).thenReturn(false);
        when(passwordEncoder.encode("Password1!")).thenReturn("$2a$10$hashAtacante");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setIdUsuario(99L);
            return u;
        });
        when(usuarioRepository.countReservasGestionadasByUsuario(any())).thenReturn(0);

        UsuarioResponse response = usuarioService.crearUsuario(request);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        Usuario guardado = captor.getValue();

        // La aserción evidencia la vulnerabilidad: el sistema asignó ADMINISTRADOR
        // a un usuario recién creado sin validar quién hizo la petición.
        assertEquals(Usuario.Rol.ADMINISTRADOR, guardado.getRol(),
                "El servicio asignó rol ADMINISTRADOR sin validar la identidad del solicitante");
        assertNotNull(response);
        assertEquals(Usuario.Rol.ADMINISTRADOR, response.getRol());
    }

    @Test
    void actualizarUsuario_promueveAClienteAADMINISTRADOR_sinVerificarAutorizacion() {
        UsuarioRequest request = new UsuarioRequest();
        request.setNombre("Cliente Normal");
        request.setEmail("cliente.renombrado@reservas.com");
        request.setPassword("");
        request.setRol(Usuario.Rol.ADMINISTRADOR);
        request.setTelefono("0999999999");
        request.setDepartamento("Atención");
        request.setActivo(true);

        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.existsByEmail("cliente.renombrado@reservas.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
        when(usuarioRepository.countReservasGestionadasByUsuario(any())).thenReturn(0);

        UsuarioResponse response = usuarioService.actualizarUsuario(10L, request);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        Usuario actualizado = captor.getValue();

        // La aserción evidencia la vulnerabilidad: un CLIENTE fue promovido a
        // ADMINISTRADOR únicamente con un PUT al endpoint, sin ningún token
        // ni verificación de rol del solicitante.
        assertEquals(Usuario.Rol.ADMINISTRADOR, actualizado.getRol(),
                "Un usuario CLIENTE fue promovido a ADMINISTRADOR sin verificación de autorización");
        assertEquals(Usuario.Rol.ADMINISTRADOR, response.getRol());
    }

    @Test
    void eliminarUsuario_cambiaSoloElFlagActivo_sinAuditoriaNiVerificacion() {
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        usuarioService.eliminarUsuario(10L);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        Usuario persistido = captor.getValue();

        // El método "elimina" al usuario simplemente desactivándolo (activo=false),
        // sin ningún log de auditoría, sin verificar que el solicitante sea
        // administrador y sin eliminar realmente el registro.
        assertFalse(persistido.getActivo(),
                "El usuario fue 'eliminado' solo cambiando activo=false (soft delete sin auditoría)");
    }
}
