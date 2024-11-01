package com.sistema.sistemaP.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sistema.sistemaP.modelos.Cuenta;
import com.sistema.sistemaP.modelos.Espera;
import com.sistema.sistemaP.modelos.Token;
import com.sistema.sistemaP.repositorios.EsperaRepository;
import com.sistema.sistemaP.repositorios.TokenRepository;
import com.sistema.sistemaP.servicios.CuentaService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3001", "http://localhost:3002" })
public class LoginController {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private EsperaRepository esperaRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        Long accountId = Long.parseLong(loginRequest.get("accountId"));
        String password = loginRequest.get("password");

        Cuenta cuenta = cuentaService.buscarPorIdYContrasenia(accountId, password);

        if (cuenta == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas.");
        }
        List<Token> tokens = tokenRepository.findAll();

        if (tokens.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe token.");
        }

        Token token = tokens.get(0);

        if (token.getCuenta() != null) {

            if (token.getCuenta().getId().equals(accountId)) {
                if (token.isEnUso()) {
                    System.out.println("chau");
                    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                            .body("¡Esta cuenta esta siendo usada desde otro cliente!");
                }
                System.out.println("hola");
                token.setEnUso(true);
                tokenRepository.saveAndFlush(token);
                return ResponseEntity.ok()
                        .body(Map.of("accountId", cuenta.getId(), "message", "Login exitoso", "saldo",
                                cuenta.getSaldo()));
            }

            List<Espera> listaDeEsperas = esperaRepository.findByCuenta(cuenta);
            System.out.println(listaDeEsperas);
            if (listaDeEsperas.size() != 0) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Lo sentimos, se encuentra en espera...");
            }
            Espera espera = new Espera();
            espera.setCuenta(cuenta);
            esperaRepository.saveAndFlush(espera);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("No hay token disponible, en espera...");
        }

        token.setCuenta(cuenta);
        tokenRepository.saveAndFlush(token);
        return ResponseEntity.ok()
                .body(Map.of("accountId", cuenta.getId(), "message", "Login exitoso", "saldo", cuenta.getSaldo()));

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, Long> logoutRequest) {
        Long accountId = logoutRequest.get("accountId");

        List<Token> tokenOpt = tokenRepository.findAll();

        if (!tokenOpt.isEmpty()) {
            Token token = tokenOpt.get(0);
            if (token.getCuenta() != null && token.getCuenta().getId().equals(accountId)) {
                token.setCuenta(null);
                token.setEnUso(false);
                tokenRepository.save(token);

                Optional<Espera> esperaOpt = esperaRepository.findFirstByOrderByIdAsc();
                if (esperaOpt.isPresent()) {
                    Token newToken = tokenRepository.findById(1L).get();
                    newToken.setCuenta(esperaOpt.get().getCuenta());
                    newToken.setEnUso(false);
                    tokenRepository.save(newToken);
                    esperaRepository.delete(esperaOpt.get());
                }
                return ResponseEntity.ok("Logout exitoso.");
            }
        }
        return ResponseEntity.badRequest().body("Token no encontrado o no pertenece al usuario.");
    }
}
