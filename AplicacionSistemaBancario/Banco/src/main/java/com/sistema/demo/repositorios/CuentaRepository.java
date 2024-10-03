package com.sistema.demo.repositorios;

import com.sistema.demo.modelos.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    @Query("SELECT c FROM Cuenta c WHERE c.id = :id AND c.estado = true")
    Optional<Cuenta> findByIdAndEstadoTrue(@Param("id") Long id);

    @Query("SELECT c FROM Cuenta c WHERE c.id = :id")
    Cuenta findByIdWithoutCache(@Param("id") Long id);

    List<Cuenta> findByTipoCuenta(String tipoCuenta);

    boolean existsByClienteId(Long clienteId);

    Cuenta findByIdAndContrasenia(Long id, String contrasenia);
}
