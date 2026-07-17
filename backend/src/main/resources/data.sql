INSERT INTO servicios (nombre_servicio, precio, descripcion, duracion_minutos, activo, created_at) VALUES
('Consulta General', 25, 'Consulta medica general', 30, true, CURRENT_TIMESTAMP);

INSERT INTO usuarios (nombre, telefono, email, rol, password, activo, departamento, created_at, updated_at) VALUES
('Administrador', '1234567890', 'admin@reservas.com', 'ADMINISTRADOR', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, 'TI', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
