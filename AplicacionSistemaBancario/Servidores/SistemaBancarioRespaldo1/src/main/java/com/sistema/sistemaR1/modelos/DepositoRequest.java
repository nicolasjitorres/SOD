package com.sistema.sistemaR1.modelos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositoRequest {
    private Long idCuenta;
    private Float monto;
    private Long idDestino;
}
