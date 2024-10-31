package com.sistema.sistemaP.servicios;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.sistemaP.modelos.Cuenta;
import com.sistema.sistemaP.modelos.Movimiento;
import com.sistema.sistemaP.repositorios.CuentaRepository;
import com.sistema.sistemaP.repositorios.MovimientoRepository;
import com.sistema.sistemaP.websocket.CuentaWebSocketHandler;

import java.util.List;
import java.util.Optional;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private CuentaWebSocketHandler cuentaWebSocketHandler;

    @Autowired
    private EntityManager entityManager;

    private final Object lock = new Object();

    public List<Cuenta> obtenerTodasLasCuentas() {
        System.out.println("Obteniendo todas las cuentas...");
        return cuentaRepository.findAll();
    }

    public Optional<Cuenta> obtenerCuenta(Long id) {
        System.out.println("Obteniendo cuenta con id: " + id + "...");
        return cuentaRepository.findById(id);
    }

    public void crearCuenta(Cuenta cuenta) {

        if (cuentaRepository.existsByClienteId(cuenta.getCliente().getId())) {
            throw new RuntimeException("El cliente ya tiene una cuenta. No se puede crear otra.");
        }

        System.out.println("Creando una nueva cuenta con ID: " + cuenta.getId());
        cuentaRepository.save(cuenta);
        System.out.println("Cuenta creada exitosamente.");

        Movimiento movimiento = new Movimiento(null, cuenta, "Saldo inicial", cuenta.getSaldo());
        movimientoRepository.save(movimiento);

    }

    public void eliminarCuenta(Long id) {
        System.out.println("Eliminando la cuenta con ID: " + id);
        cuentaRepository.deleteById(id);
        System.out.println("Cuenta eliminada exitosamente.");
    }

    @Transactional
    public Cuenta retirar(Long idCuenta, Float monto) throws InterruptedException {
        System.out.println("Intentando retirar: " + monto + " de la cuenta con ID: " + idCuenta);
        synchronized (lock) {
            while (true) {
                entityManager.clear();

                Cuenta cuenta = cuentaRepository.findById(idCuenta)
                        .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + idCuenta));
                System.out.println("Saldo actual de la cuenta: " + cuenta.getSaldo());

                if (cuenta.getSaldo() >= monto) {
                    System.out.println("Saldo suficiente encontrado, procediendo con el retiro.");
                    cuenta.setSaldo(cuenta.getSaldo() - monto);
                    cuentaRepository.save(cuenta);

                    Movimiento movimiento = new Movimiento(null, cuenta, "Retiro", monto);
                    movimientoRepository.save(movimiento);

                    System.out.println("Retiro exitoso. Nuevo saldo: " + cuenta.getSaldo());
                    return cuenta;
                }

                System.out.println("Saldo insuficiente, esperando para reintentar el retiro.");
                lock.wait();
                System.out.println("Reintentando después de esperar...");
                Thread.sleep(5000);
            }
        }
    }

    @Transactional
    public Cuenta depositar(Long idCuenta, Float monto) {
        System.out.println("Intentando depositar: " + monto + " en la cuenta con ID: " + idCuenta);
        synchronized (lock) {
            Cuenta cuenta = cuentaRepository.findById(idCuenta)
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + idCuenta));

            System.out.println("Saldo antes del depósito: " + cuenta.getSaldo());
            cuenta.setSaldo(cuenta.getSaldo() + monto);
            cuentaRepository.save(cuenta);

            Movimiento movimiento = new Movimiento(null, cuenta, "Deposito", monto);
            movimientoRepository.save(movimiento);

            System.out.println("Depósito exitoso. Saldo después del depósito: " + cuenta.getSaldo());

            System.out.println("Notificando a los hilos en espera después del depósito.");
            lock.notifyAll();
            return cuenta;
        }
    }

    public void transferir(Long idCuentaOrigen, Long idCuentaDestino, Float monto) {
        System.out.println("Iniciando transferencia de: " + monto + " de la cuenta con ID: " + idCuentaOrigen +
                " a la cuenta con ID: " + idCuentaDestino);
        synchronized (lock) {
            entityManager.clear();

            Cuenta cuentaOrigen = cuentaRepository.findById(idCuentaOrigen)
                    .orElseThrow(
                            () -> new RuntimeException("Cuenta de origen no encontrada con ID: " + idCuentaOrigen));
            Cuenta cuentaDestino = cuentaRepository.findById(idCuentaDestino)
                    .orElseThrow(
                            () -> new RuntimeException("Cuenta de destino no encontrada con ID: " + idCuentaDestino));

            System.out.println("Saldo en la cuenta origen: " + cuentaOrigen.getSaldo());
            while (cuentaOrigen.getSaldo() < monto) {
                try {
                    System.out.println("Saldo insuficiente en la cuenta origen, entrando en espera.");
                    lock.wait();
                } catch (InterruptedException e) {
                    System.out.println("Error: la transferencia fue interrumpida.");
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("La transferencia fue interrumpida.");
                }
            }

            cuentaOrigen.setSaldo(cuentaOrigen.getSaldo() - monto);
            cuentaDestino.setSaldo(cuentaDestino.getSaldo() + monto);

            System.out.println("Transferencia completada. Nuevo saldo en cuenta origen: " + cuentaOrigen.getSaldo());
            System.out.println("Nuevo saldo en cuenta destino: " + cuentaDestino.getSaldo());

            cuentaRepository.save(cuentaOrigen);
            cuentaRepository.save(cuentaDestino);

            Movimiento movimientoSalida = new Movimiento(null, cuentaOrigen,
                    "Transferencia a Cuenta ID: " + idCuentaDestino, monto);
            movimientoRepository.save(movimientoSalida);

            Movimiento movimientoEntrada = new Movimiento(null, cuentaDestino,
                    "Transferencia desde Cuenta ID: " + idCuentaOrigen, monto);
            movimientoRepository.save(movimientoEntrada);

            cuentaWebSocketHandler.sendUpdate(cuentaOrigen);
            cuentaWebSocketHandler.sendUpdate(cuentaDestino);

            System.out.println("Notificando a los hilos en espera después de la transferencia.");
            lock.notifyAll();
        }
    }

    public Cuenta buscarPorIdYContrasenia(Long id, String contrasenia) {
        return cuentaRepository.findByIdAndContrasenia(id, contrasenia);
    }
}
