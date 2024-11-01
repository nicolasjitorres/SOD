package com.sistema.sistemaR2.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.sistemaR2.modelos.Cuenta;
import com.sistema.sistemaR2.modelos.Espera;
import java.util.List;


@Repository
public interface EsperaRepository extends JpaRepository<Espera, Long> {
    Optional<Espera> findFirstByOrderByIdAsc();
    List<Espera> findByCuenta(Cuenta cuenta);
}
