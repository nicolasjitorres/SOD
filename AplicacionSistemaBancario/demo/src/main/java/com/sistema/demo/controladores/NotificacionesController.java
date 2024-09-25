package com.sistema.demo.controladores;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificacionesController {

    private final SimpMessagingTemplate template;

    public NotificacionesController(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void notificarCambioSaldo(Long idCuenta, Float nuevoSaldo) {
        template.convertAndSend("/topic/cuentas/" + idCuenta, "Nuevo saldo: " + nuevoSaldo);
    }
}

