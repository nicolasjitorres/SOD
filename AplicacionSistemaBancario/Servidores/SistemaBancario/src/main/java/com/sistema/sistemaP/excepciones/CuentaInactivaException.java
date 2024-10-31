package com.sistema.sistemaP.excepciones;

public class CuentaInactivaException extends RuntimeException {
    public CuentaInactivaException(String mensaje) {
        super(mensaje);
    }
}
