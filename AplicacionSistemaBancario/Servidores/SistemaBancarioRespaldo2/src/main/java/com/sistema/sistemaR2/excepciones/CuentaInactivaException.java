package com.sistema.sistemaR2.excepciones;

public class CuentaInactivaException extends RuntimeException {
    public CuentaInactivaException(String mensaje) {
        super(mensaje);
    }
}
