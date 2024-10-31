package com.sistema.sistemaR1.servicios;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RespaldoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String principalUrl = "http://localhost:8080/mensajes/recibir";
    private final String respaldo2lUrl = "http://localhost:8082/mensajes/recibir";
    private boolean principalVivo = true;
    private boolean actualPrincipal = false;
    private boolean respaldo2Principal = false;

    @Scheduled(fixedRate = 10000)
    public void enviarPulso() {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(principalUrl, "Hola, soy respaldo 1.", String.class);
            System.out.println("Respuesta del sistema principal: " + response.getBody());
            principalVivo = true;
        } catch (Exception e) {
            if (principalVivo) {
                System.err.println("Servidor principal caído. Activando protocolo de respaldo.");
                principalVivo = false;
                verificarRespaldo2();
                actualPrincipal = true;
            }
        }
    }

    public boolean getIsPrincipal(){
        return actualPrincipal;
    }

    public void verificarRespaldo2(){
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(respaldo2lUrl, "¿Suplantas al principal?", String.class);
            if (response.getBody() == "positivo") {
                System.out.println("El respaldo 2 suplanta al principal momentaneamente.");
                respaldo2Principal = true;
            }
        } catch (Exception e) {
            System.out.println("El sistema de respaldo 2 se encuentra caído.");
        }
    }
}
