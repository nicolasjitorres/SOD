package com.sistema.sistemaP.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistema.sistemaP.modelos.Cliente;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByNombre(String nombre);

    @Query("SELECT c FROM Cliente c WHERE c.nombre LIKE %:nombre%")
    List<Cliente> buscarPorNombre(@Param("nombre") String nombre);
}
