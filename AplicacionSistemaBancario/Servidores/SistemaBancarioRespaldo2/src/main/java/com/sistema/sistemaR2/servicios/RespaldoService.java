package com.sistema.sistemaR2.servicios;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RespaldoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String principalUrl = "http://localhost:8080/mensajes/recibir";
    private boolean isPrincipalAlive = true;

    @Scheduled(fixedRate = 10000)
    public void enviarHeartbeat() {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(principalUrl, "Hola", String.class);
            System.out.println("Respuesta del sistema principal: " + response.getBody());
            isPrincipalAlive = true;
        } catch (Exception e) {
            if (isPrincipalAlive) {
                System.err.println("Servidor principal caído");
                isPrincipalAlive = false;
            }
        }
    }
}
