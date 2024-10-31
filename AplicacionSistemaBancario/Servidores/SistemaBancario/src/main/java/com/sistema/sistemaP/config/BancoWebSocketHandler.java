package com.sistema.sistemaP.config;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;

@Component
public class BancoWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // Aquí puedes manejar el mensaje recibido
        String payload = message.getPayload();
        // Procesar el mensaje y enviar una respuesta si es necesario
        System.out.println("Mensaje recibido: " + payload);
    }

    // Opcional: manejar la conexión y desconexión de los clientes
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("Cliente conectado: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("Cliente desconectado: " + session.getId());
    }
}
