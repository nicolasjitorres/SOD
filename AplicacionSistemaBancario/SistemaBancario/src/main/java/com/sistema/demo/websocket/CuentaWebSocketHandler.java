package com.sistema.demo.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.sistema.demo.modelos.Cuenta;

@Controller
public class CuentaWebSocketHandler {

    private final SimpMessagingTemplate messagingTemplate;

    public CuentaWebSocketHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Método para enviar actualizaciones a los clientes
    public void sendUpdate(Cuenta cuenta) {
        // Enviar la actualización a los clientes suscritos
        messagingTemplate.convertAndSend("/topic/cuentas/" + cuenta.getId(), cuenta);
    }
}
