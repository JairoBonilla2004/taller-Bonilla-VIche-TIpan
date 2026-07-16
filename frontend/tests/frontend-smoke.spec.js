const { test, expect } = require('@playwright/test');

test.describe('Pruebas E2E Frontend', () => {
  test('Login muestra elementos principales', async ({ page }) => {
    await page.goto('/');

    await expect(page.getByRole('heading', { name: 'Iniciar Sesión' })).toBeVisible();
    await expect(page.getByPlaceholder('admin@reservas.com')).toBeVisible();
    await expect(page.getByPlaceholder('Tu contraseña')).toBeVisible();
    await expect(page.getByRole('button', { name: 'Iniciar Sesión' })).toBeVisible();
  });

  test('Formulario de reserva valida campos requeridos', async ({ page }) => {
    await page.goto('/reservar');

    await expect(page.getByRole('heading', { name: 'Nueva Reserva' })).toBeVisible();
    await page.getByRole('button', { name: 'Confirmar Reserva' }).click();

    await expect(page.getByText('El nombre es obligatorio')).toBeVisible();
    await expect(page.getByText('El teléfono es obligatorio')).toBeVisible();
    await expect(page.getByText('El email es obligatorio')).toBeVisible();
    await expect(page.getByText('Debes seleccionar un servicio')).toBeVisible();
    await expect(page.getByText('La fecha es obligatoria')).toBeVisible();
    await expect(page.getByText('La hora es obligatoria')).toBeVisible();
  });
});
