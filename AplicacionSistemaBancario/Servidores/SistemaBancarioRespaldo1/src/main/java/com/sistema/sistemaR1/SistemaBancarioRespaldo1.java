package com.sistema.sistemaR1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SistemaBancarioRespaldo1 {

	public static void main(String[] args) {
		SpringApplication.run(SistemaBancarioRespaldo1.class, args);
	}

}
