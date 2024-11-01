package com.sistema.sistemaR2.servicios;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RespaldoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String principalUrl = "http://localhost:8080/mensajes/recibir";
    private final String respaldo1Url = "http://localhost:8081/mensajes/recibir";
    private boolean principalVivo = true;
    private boolean actualPrincipal = false;
    private boolean respaldo1Principal = false;

    @Scheduled(fixedRate = 10000)
    public void enviarPulso() {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(principalUrl, "¿Estas vivo? Soy respaldo 2.",
                    String.class);
            System.out.println("Respuesta del sistema principal: " + response.getBody());
            principalVivo = true;
            actualPrincipal = false;
            respaldo1Principal = false;
        } catch (Exception e) {
            if (principalVivo) {
                System.err.println("Servidor principal caído. Activando protocolo de respaldo.");
                principalVivo = false;
            }
            if (!actualPrincipal) {
                verificarRespaldo1();
            }
            if (!respaldo1Principal) {
                actualPrincipal = true;
                System.out.println("El sistema de respaldo 2 suplanta al principal momentaneamente.");
            }
        }
    }

    public boolean getIsPrincipal() {
        return actualPrincipal;
    }

    public void verificarRespaldo1() {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(respaldo1Url, "¿Estas vivo? Soy respaldo 2.",
                    String.class);
            System.out.println("El sistema de respaldo 1 suplanta al principal momentaneamente. " + response.getBody());
            respaldo1Principal = true;
            return;
        } catch (Exception e) {
            respaldo1Principal = false;
            System.out.println("El sistema de respaldo 1 se encuentra caído.");
        }
    }
}
