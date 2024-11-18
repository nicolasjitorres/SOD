package com.sistema.sistemaR1.servicios;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RespaldoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String principalUrl = "http://localhost:8080/mensajes/recibir";
    private final String urlSiguiente = "http://localhost:8082/mensajes/suplente";
    private final String urlSiguienteLider = "http://localhost:8082/mensajes/lider";
    private final int miID = 8081;

    private boolean principalVivo = true;
    private boolean respaldoVivo = true;
    private boolean soyLider = false;
    private int lider = 8080;

    @Scheduled(fixedRate = 10000)
    public void enviarPulso() {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(principalUrl,
                    "¿Estás vivo? Soy Respaldo " + miID,
                    String.class);
            System.out.println("Respuesta del sistema principal: " + response.getBody());
            principalVivo = true;
            soyLider = false;
            lider = 8080;
        } catch (Exception e) {
            if (principalVivo) {
                System.err.println("Servidor principal caído. Activando protocolo de elección de líder.");
                principalVivo = false;
                iniciarEleccion();
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
            System.out.println("Respuesta del sistema respaldo 8082: " + response.getBody());
            respaldoVivo = true;
        } catch (Exception e) {
            if (respaldoVivo) {
                System.err.println("Servidor de respaldo caído. Activando protocolo de elección de líder.");
                respaldoVivo = false;
                iniciarEleccion();
            }
        }
    }

    public void iniciarEleccion() {
        System.out.println("Iniciando algoritmo de elección de líder. Mi ID es " + miID);
        try {
            Map<String, Object> mensaje = new HashMap<>();
            mensaje.put("iniciador", miID);
            mensaje.put("lider", miID);
            restTemplate.postForEntity(urlSiguiente, mensaje, String.class);
        } catch (Exception e) {
            System.out.println("No se pudo contactar con el siguiente nodo en el anillo.");
            this.setSoyLider(true);
            System.out.println("Soy Respaldo " + miID + " y me declaro líder porque el siguiente nodo está caído.");
        }
    }

    public String recibirMensaje(Map<String, Object> mensaje){
        int iniciador = (int) mensaje.get("iniciador");
        int liderActual = (int) mensaje.get("lider");
        int nodeId = this.getMiID();
        
        System.out.println("Mensaje recibido: El nodo "+ iniciador + " inicio el algoritmo de eleccion, el candidato a lider es el nodo: " + liderActual);

        if (iniciador == nodeId) {
            System.out.println("Como yo fui el iniciador del algoritmo, declaro como nuevo lider al nodo " + liderActual);
            this.setSoyLider(liderActual == nodeId);
            lider = liderActual;
            return "" + liderActual;
        }

        restTemplate.postForEntity(urlSiguiente, mensaje, Integer.class);
        return "Mensaje procesado y reenviado";
    }

    public boolean esLider() {
        return soyLider;
    }

    public void setSoyLider(boolean soyLider){
        this.soyLider = soyLider;
    }

    public Integer getMiID() {
        return miID;
    }
}
