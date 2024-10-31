package com.sistema.sistemaR1.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sistema.sistemaR1.modelos.Cuenta;
import com.sistema.sistemaR1.servicios.CuentaService;

import java.util.Map;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:5173")
public class LoginController {

    @Autowired
    private CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        Long accountId = Long.parseLong(loginRequest.get("accountId"));
        String password = loginRequest.get("password");

        Cuenta cuenta = cuentaService.buscarPorIdYContrasenia(accountId, password);

        if (cuenta != null) {
            return ResponseEntity.ok().body(Map.of("accountId", cuenta.getId(), "message", "Login exitoso", "saldo", cuenta.getSaldo()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }
}
