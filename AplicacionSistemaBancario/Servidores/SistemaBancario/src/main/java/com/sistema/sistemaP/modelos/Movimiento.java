package com.sistema.sistemaP.modelos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_cuenta", nullable = false)
    @JsonIgnore
    private Cuenta cuenta;

    private String descripcion;

    @Column(nullable = false)
    private Float monto;

}
