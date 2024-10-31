package com.sistema.sistemaR2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SistemaBancarioRespaldo2 {

	public static void main(String[] args) {
		SpringApplication.run(SistemaBancarioRespaldo2.class, args);
	}

}
