package com.sistema.sistemaP.controladores;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sistema.sistemaP.modelos.Cliente;
import com.sistema.sistemaP.modelos.Cuenta;
import com.sistema.sistemaP.modelos.DepositoRequest;
import com.sistema.sistemaP.modelos.Movimiento;
import com.sistema.sistemaP.repositorios.CuentaRepository;
import com.sistema.sistemaP.repositorios.MovimientoRepository;
import com.sistema.sistemaP.servicios.ClienteService;
import com.sistema.sistemaP.servicios.CuentaService;


@RestController
@RequestMapping("/cuentas")
@CrossOrigin(origins = {"http://sd.sod:3000", "http://sd.sod:3001", "http://sd.sod:3002"})
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @GetMapping
    public ResponseEntity<List<Cuenta>> listarCuentas() {
        List<Cuenta> cuentas = cuentaService.obtenerTodasLasCuentas();
        return ResponseEntity.ok(cuentas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> traerCuenta(@PathVariable Long id) {
        Optional<Cuenta> cuenta = cuentaService.obtenerCuenta(id);
        if (cuenta.isPresent()) {
            return ResponseEntity.ok(cuenta.get());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener la cuenta con id: " + id);
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearCuenta(@RequestParam String tipoCuenta, @RequestParam Float saldo,
            @RequestParam Long clienteId) {
        try {
            Cuenta cuenta = new Cuenta();
            cuenta.setTipoCuenta(tipoCuenta);
            cuenta.setSaldo(saldo);
            cuenta.setEstado(true);
            Cliente cliente = clienteService.findById(clienteId);
            cuenta.setCliente(cliente);

            cuentaService.crearCuenta(cuenta);

            return ResponseEntity.status(HttpStatus.CREATED).body("Cuenta creada exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la cuenta: " + e.getMessage());
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarCuenta(@PathVariable Long id) {
        try {
            cuentaService.eliminarCuenta(id);
            return ResponseEntity.ok("Cuenta eliminada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la cuenta: " + e.getMessage());
        }
    }

    @PostMapping("/depositar")
    public ResponseEntity<?> depositar(@RequestBody DepositoRequest request) {
        try {
            cuentaService.depositar(request.getIdCuenta(), request.getMonto());
            return ResponseEntity.ok("Depósito realizado con éxito por " + request.getMonto() + " en la cuenta ID: "
                    + request.getIdCuenta());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al depositar: " + e.getMessage());
        }
    }

    @PostMapping("/retirar")
    public ResponseEntity<?> retirar(@RequestBody DepositoRequest request) {
        try {
            cuentaService.retirar(request.getIdCuenta(), request.getMonto());
            return ResponseEntity.ok("Retiro realizado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al retirar: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/movimientos")
    public ResponseEntity<?> verMovimientos(@PathVariable Long id) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findById(id);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            List<Movimiento> movimientos = movimientoRepository.findByCuentaId(cuenta.getId());
            return ResponseEntity.ok(movimientos);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta no encontrada.");
        }
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestBody DepositoRequest request) {
        try {
            cuentaService.transferir(request.getIdCuenta(), request.getIdDestino(), request.getMonto());
            return ResponseEntity.ok("Transferencia de " + request.getMonto() + " realizada de la cuenta ID: "
                    + request.getIdCuenta() + " a la cuenta ID: " + request.getIdDestino());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al realizar la transferencia: " + e.getMessage());
        }
    }
}
