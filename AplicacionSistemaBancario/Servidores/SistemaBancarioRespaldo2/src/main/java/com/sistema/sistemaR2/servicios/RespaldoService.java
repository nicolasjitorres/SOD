package com.sistema.sistemaR2.servicios;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RespaldoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String principalUrl = "http://localhost:8080/mensajes/recibir";
    private final String urlSiguiente = "http://localhost:8081/mensajes/suplente";
    private final String urlSiguienteLider = "http://localhost:8081/mensajes/lider";
    private final int miID = 8082;

    private boolean principalVivo = true;
    private boolean respaldoVivo = true;
    private boolean soyLider = false;
    private int lider = 8080;
    private int i = 0;

    @Scheduled(fixedRate = 10000)
    public void enviarPulso() {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(principalUrl,
                    "¿Estás vivo? Soy Respaldo " + miID,
                    String.class);
            System.out.println("Respuesta del sistema principal: " + response.getBody());
            principalVivo = true;
            soyLider = false;
            i = 0;
            lider = 8080;
        } catch (Exception e) {
            if (i == 1) {
                if (lider == 8080) {
                    System.out.println("Como ningún nodo responde, me declaro el nuevo Lider.");
                    respaldoVivo = false;
                    this.setSoyLider(true);
                    lider = 8082;
                    i=0;
                }
            }
            if (principalVivo) {
                System.err.println("Servidor principal caído. Activando protocolo de elección de líder.");
                principalVivo = false;
                i += 1;
                return;
            }
            if (!soyLider) {
                enviarPulsoRespaldo();
            }
        }
    }

    public void enviarPulsoRespaldo() {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(urlSiguienteLider,
                    "¿Estás vivo? Soy Respaldo " + miID,
                    String.class);
            System.out.println("Respuesta del sistema respaldo 8081: " + response.getBody());
            respaldoVivo = true;
        } catch (Exception e) {
            if (respaldoVivo) {
                System.err.println("Servidor respaldo 1 caído. Me declaro lider.");
                respaldoVivo = false;
                this.setSoyLider(true);
                lider = 8082;
            }
        }
    }

    public String recibirMensaje(Map<String, Object> mensaje) {
        int iniciador = (int) mensaje.get("iniciador");
        int liderActual = (int) mensaje.get("lider");
        int nodeId = this.getMiID();

        System.out.println("Mensaje recibido: El nodo " + iniciador
                + " inicio el algoritmo de eleccion, el candidato a lider es el nodo: " + liderActual);

        if (liderActual > nodeId) {
            System.out.println("Como mi ID " + nodeId + " es mayor al ID del nodo candidato, no me postulo como lider.");
            mensaje.put("lider", nodeId);
        }

        if (iniciador == nodeId) {
            this.setSoyLider((int) mensaje.get("lider") == nodeId);
            lider = (int) mensaje.get("lider");
            return "Nuevo lider seleccionado: " + mensaje.get("lider");
        }

        ResponseEntity<String> response = restTemplate.postForEntity(urlSiguiente, mensaje, String.class);
        System.out.println("Mensaje recibido: El nuevo lider ahora es el nodo: " + response.getBody());
        if (response.getBody().equals("8081")) {
            lider = 8081;
        }else{
            lider = 8082;
        }
        return "Mensaje procesado y reenviado";
    }

    public boolean esLider() {
        return soyLider;
    }

    public void setSoyLider(boolean soyLider) {
        this.soyLider = soyLider;
    }

    public Integer getMiID() {
        return miID;
    }
}
