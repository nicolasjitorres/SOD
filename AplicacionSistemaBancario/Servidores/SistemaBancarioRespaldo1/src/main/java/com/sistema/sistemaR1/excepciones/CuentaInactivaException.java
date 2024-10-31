package com.sistema.sistemaR1.excepciones;

public class CuentaInactivaException extends RuntimeException {
    public CuentaInactivaException(String mensaje) {
        super(mensaje);
    }
}
