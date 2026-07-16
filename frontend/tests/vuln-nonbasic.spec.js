const { test, expect } = require('@playwright/test');

test.describe('Vulnerabilidades no básicas', () => {

  test('Acceso a reportes sin autenticación (exposición de datos)', async ({ request }) => {
    const res = await request.get('http://127.0.0.1:8080/api/reportes/resumen');
    expect(res.ok()).toBeTruthy();
    const body = await res.json();
    expect(body).toHaveProperty('totalReservas');
    expect(body).toHaveProperty('reservasHoy');
  });

  test('Crear y eliminar un servicio sin autenticación (acción destructiva)', async ({ request }) => {
    const unique = Date.now();
    const createRes = await request.post('http://127.0.0.1:8080/api/servicios', {
      data: {
        nombreServicio: `Exploit Service ${unique}`,
        precio: 1,
        descripcion: 'Prueba de creación sin auth',
        duracionMinutos: 30,
        activo: true
      }
    });

    expect(createRes.ok()).toBeTruthy();
    const svc = await createRes.json();
    expect(svc).toHaveProperty('idServicio');
    const id = svc.idServicio;

    const delRes = await request.delete(`http://127.0.0.1:8080/api/servicios/${id}`);
    expect(delRes.status()).toBe(204);
  });

});
