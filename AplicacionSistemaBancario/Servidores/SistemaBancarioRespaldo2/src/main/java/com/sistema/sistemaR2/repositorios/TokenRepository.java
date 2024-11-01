package com.sistema.sistemaR2.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.sistemaR2.modelos.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long>{
    
}
