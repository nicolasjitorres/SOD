package com.sistema.sistemaP.controladores;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:3002"})
public class NotificacionesController {

    private final SimpMessagingTemplate template;

    public NotificacionesController(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void notificarCambioSaldo(Long idCuenta, Float nuevoSaldo) {
        template.convertAndSend("/topic/cuentas/" + idCuenta, "Nuevo saldo: " + nuevoSaldo);
    }
}

