package com.sistema.sistemaR2.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sistema.sistemaR2.servicios.RespaldoService;

@Controller
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:3002"})
public class HomeController {

    @Autowired
    RespaldoService respaldoService;

    @GetMapping("/")
    public String rutaIndex() {
        return "index";
    }

    @PostMapping("/mensajes/suplente")
    public ResponseEntity<String> recibirMensaje(@RequestBody String mensaje) {
        System.out.println("Mensaje recibido: " + mensaje);
        if (respaldoService.getIsPrincipal()) {
            return ResponseEntity.ok("Estoy vivo");
        }
        return ResponseEntity.ok("negativo");
    }
}
