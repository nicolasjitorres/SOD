package com.sistema.demo.servicios;

import com.sistema.demo.modelos.Cuenta;
import com.sistema.demo.repositorios.CuentaRepository;
import com.sistema.demo.websocket.CuentaWebSocketHandler;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    // Inyectar el WebSocket handler para notificar a los clientes
    @Autowired
    private CuentaWebSocketHandler cuentaWebSocketHandler;

    @Autowired
    private EntityManager entityManager;

    private final Object lock = new Object();

    public List<Cuenta> obtenerTodasLasCuentas() {
        return cuentaRepository.findAll(); // Obtener todas las cuentas
    }

    public void crearCuenta(Cuenta cuenta) {
        cuentaRepository.save(cuenta); // Guardar la nueva cuenta
    }

    public void eliminarCuenta(Long id) {
        cuentaRepository.deleteById(id); // Eliminar la cuenta por ID
    }

    @Transactional
    public Cuenta retirar(Long idCuenta, Float monto) throws InterruptedException {
        synchronized (lock) {
            while (true) {
                entityManager.clear();

                Cuenta cuenta = cuentaRepository.findById(idCuenta)
                        .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

                if (cuenta.getSaldo() >= monto) {
                    cuenta.setSaldo(cuenta.getSaldo() - monto);
                    cuentaRepository.save(cuenta);
                    System.out.println("nuevo saldo: " + cuenta.getSaldo());
                    return cuenta;
                }

                System.out.println("Hilo entra en estado de espera por saldo insuficiente");
                lock.wait(); // Espera hasta que se notifique
                System.out.println("Esperamos 10 segundos...");
                Thread.sleep(10000);
            }
        }
    }

    @Transactional
    public Cuenta depositar(Long idCuenta, Float monto) {
        synchronized (lock) {
            Cuenta cuenta = cuentaRepository.findById(idCuenta)
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

            System.out.println("Saldo antes de deposito: " + cuenta.getSaldo());
            cuenta.setSaldo(cuenta.getSaldo() + monto);
            cuentaRepository.save(cuenta);
            System.out.println("Saldo después de deposito: " + cuenta.getSaldo());

            lock.notifyAll();
            return cuenta;
        }
    }

    public void transferir(Long idCuentaOrigen, Long idCuentaDestino, Float monto) {
        synchronized (lock) {
            Cuenta cuentaOrigen = cuentaRepository.findById(idCuentaOrigen)
                    .orElseThrow(() -> new RuntimeException("Cuenta de origen no encontrada"));
            Cuenta cuentaDestino = cuentaRepository.findById(idCuentaDestino)
                    .orElseThrow(() -> new RuntimeException("Cuenta de destino no encontrada"));

            while (cuentaOrigen.getSaldo() < monto) {
                try {
                    System.out.println("Hilo entra en espera en la transferencia por saldo insuficiente");
                    lock.wait(); // Esperar si no hay suficiente saldo
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restaura el estado de interrupción
                    throw new RuntimeException("La transferencia fue interrumpida.");
                }

            }

            cuentaOrigen.setSaldo(cuentaOrigen.getSaldo() - monto);
            cuentaDestino.setSaldo(cuentaDestino.getSaldo() + monto);

            cuentaRepository.save(cuentaOrigen);
            cuentaRepository.save(cuentaDestino);

            cuentaWebSocketHandler.sendUpdate(cuentaOrigen);
            cuentaWebSocketHandler.sendUpdate(cuentaDestino);

            lock.notifyAll(); // Notificar cambios
        }
    }
}
