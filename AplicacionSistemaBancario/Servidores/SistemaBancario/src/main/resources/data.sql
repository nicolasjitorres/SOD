-- Limpiar las tablas existentes
DROP TABLE IF EXISTS movimiento;
DROP TABLE IF EXISTS cuenta;
DROP TABLE IF EXISTS cliente;

-- Crear la tabla Cliente
CREATE TABLE cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    fecha_registro DATE
);

-- Crear la tabla Cuenta
CREATE TABLE cuenta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cliente BIGINT NOT NULL,
    tipo_cuenta VARCHAR(50),
    saldo FLOAT NOT NULL,
    estado BOOLEAN,
    contrasenia VARCHAR(50),
    FOREIGN KEY (id_cliente) REFERENCES cliente(id) ON DELETE CASCADE
);

-- Crear la tabla Movimiento
CREATE TABLE movimiento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cuenta BIGINT NOT NULL,
    descripcion VARCHAR(255),
    monto FLOAT NOT NULL,
    FOREIGN KEY (id_cuenta) REFERENCES cuenta(id) ON DELETE CASCADE
);

-- Insertar datos de prueba en Cliente
INSERT INTO cliente (nombre, fecha_registro) VALUES
('Juan Perez', CURRENT_DATE),
('Maria Gomez', CURRENT_DATE),
('Pedro Rodriguez', CURRENT_DATE);

-- Insertar datos de prueba en Cuenta
INSERT INTO cuenta (id_cliente, tipo_cuenta, saldo, estado, contrasenia) VALUES
(1, 'Ahorro', 1000, TRUE, '1234'),
(2, 'Corriente', 500, TRUE, '5678'),
(3, 'Ahorro', 1500, TRUE, '9012');

-- Insertar datos de prueba en Movimiento
INSERT INTO movimiento (id_cuenta, descripcion, monto) VALUES
(1, 'Deposito inicial', 1000),
(2, 'Deposito inicial', 500),
(3, 'Deposito inicial', 1500),
(1, 'Retiro', 200),
(2, 'Transferencia', 100);
