package com.sistema.sistemaR1.modelos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    private String tipoCuenta;

    @Column(nullable = false)
    private Float saldo;

    private Boolean estado;

    private String contrasenia;

    @Override
    public String toString() {
        return "Cuenta{" +
                "id=" + id +
                ", tipoCuenta='" + tipoCuenta + '\'' +
                // Puedes mostrar solo una parte del cliente para evitar recursión
                ", clienteId=" + (cliente != null ? cliente.getId() : null) +
                '}';
    }

}
