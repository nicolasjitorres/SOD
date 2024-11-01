package com.sistema.sistemaR1.servicios;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RespaldoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String principalUrl = "http://localhost:8080/mensajes/recibir";
    private final String respaldo2Url = "http://localhost:8082/mensajes/suplente";
    private boolean principalVivo = true;
    private boolean actualPrincipal = false;
    private boolean respaldo2Principal = false;

    @Scheduled(fixedRate = 10000)
    public void enviarPulso() {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(principalUrl, "¿Estas vivo? Soy respaldo 1.",
                    String.class);
            System.out.println("Respuesta del sistema principal: " + response.getBody());
            principalVivo = true;
            actualPrincipal = false;
            respaldo2Principal = false;
        } catch (Exception e) {
            if (principalVivo) {
                System.err.println("Servidor principal caído. Activando protocolo de respaldo.");
                principalVivo = false;
            }
            if (!actualPrincipal) {
                verificarRespaldo2();
            }
            if (!respaldo2Principal) {
                actualPrincipal = true;
                System.out.println("El sistema de respaldo 1 suplanta al principal momentaneamente.");
            }
        }
    }

    public boolean getIsPrincipal() {
        return actualPrincipal;
    }

    public void verificarRespaldo2() {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(respaldo2Url, "¿Estas vivo? Soy respaldo 1.",
                    String.class);
            if (response.getBody().equals("Estoy vivo")) {
                System.out.println("El sistema de respaldo 2 suplanta al principal momentaneamente. " + response.getBody());
                respaldo2Principal = true;
                return;
            }
            respaldo2Principal = false;
        } catch (Exception e) {
            respaldo2Principal = false;
            System.out.println("El sistema de respaldo 2 se encuentra caído.");
        }
    }
}
