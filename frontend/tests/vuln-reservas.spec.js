const { test, expect } = require('@playwright/test');

test.describe('Vulnerabilidades en Reservas', () => {

  test('Confirmar una reserva sin autenticación', async ({ request }) => {
    const unique = Date.now();
    const createRes = await request.post('http://127.0.0.1:8080/api/reservas', {
      data: {
        nombre: 'Cliente Prueba',
        telefono: '0999999999',
        email: `prueba${unique}@correo.com`,
        idServicio: 1,
        fecha: '2026-08-01',
        hora: '10:00:00',
        observaciones: 'Reserva de prueba E2E'
      }
    });

    expect(createRes.ok()).toBeTruthy();
    const reserva = await createRes.json();
    expect(reserva).toHaveProperty('idReserva');
    const id = reserva.idReserva;

    // Ninguna cabecera Authorization: petición completamente anónima.
    const confirmRes = await request.put(`http://127.0.0.1:8080/api/reservas/${id}/confirmar`);
    expect(confirmRes.ok()).toBeTruthy();
    const confirmada = await confirmRes.json();
    expect(confirmada.estado).toBe('Confirmada');
  });

  test('IDOR: consultar el detalle de una reserva ajena por id', async ({ request }) => {
    const unique = Date.now();
    const createRes = await request.post('http://127.0.0.1:8080/api/reservas', {
      data: {
        nombre: 'Victima Reserva',
        telefono: '0988888888',
        email: `victima${unique}@correo.com`,
        idServicio: 1,
        fecha: '2026-08-02',
        hora: '11:00:00',
        observaciones: 'Datos privados de la victima'
      }
    });

    expect(createRes.ok()).toBeTruthy();
    const reserva = await createRes.json();
    const id = reserva.idReserva;

    // Un "atacante" sin sesión ni relación con la reserva la consulta
    // solo conociendo (o adivinando) el id.
    const getRes = await request.get(`http://127.0.0.1:8080/api/reservas/${id}`);
    expect(getRes.ok()).toBeTruthy();
    const detalle = await getRes.json();
    expect(detalle.nombreCliente).toBe('Victima Reserva');
    expect(detalle.emailCliente).toBe(`victima${unique}@correo.com`);
  });

});
