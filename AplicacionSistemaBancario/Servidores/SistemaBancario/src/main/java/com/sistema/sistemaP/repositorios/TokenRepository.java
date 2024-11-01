package com.sistema.sistemaP.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.sistemaP.modelos.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long>{
    
}
