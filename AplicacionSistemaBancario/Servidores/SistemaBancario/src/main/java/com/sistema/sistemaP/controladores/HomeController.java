package com.sistema.sistemaP.controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:3002"})
public class HomeController {

    @GetMapping("/")
    public String rutaIndex() {
        return "index";
    }

    @PostMapping("/mensajes/recibir")
    public ResponseEntity<String> recibirMensaje(@RequestBody String mensaje) {
        System.out.println("Mensaje recibido: " + mensaje);
        return ResponseEntity.ok("Estoy vivo");
    }
}
