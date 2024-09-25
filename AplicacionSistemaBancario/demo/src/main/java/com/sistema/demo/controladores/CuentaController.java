package com.sistema.demo.controladores;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.sistema.demo.modelos.Cliente;
import com.sistema.demo.modelos.Cuenta;
import com.sistema.demo.modelos.Movimiento;
import com.sistema.demo.repositorios.CuentaRepository;
import com.sistema.demo.repositorios.MovimientoRepository;
import com.sistema.demo.servicios.ClienteService;
import com.sistema.demo.servicios.CuentaService;

@Controller
@RequestMapping("/cuentas")
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
    public String listarCuentas(Model model) {
        List<Cuenta> cuentas = cuentaService.obtenerTodasLasCuentas();
        model.addAttribute("cuentas", cuentas);
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        return "cuenta-list"; // Vista de la lista de cuentas
    }

    @PostMapping("/crear")
    public String crearCuenta(@RequestParam String tipoCuenta, @RequestParam Float saldo,
            @RequestParam Long clienteId, Model model) {
        try {
            Cuenta cuenta = new Cuenta();
            cuenta.setTipoCuenta(tipoCuenta);
            cuenta.setSaldo(saldo);
            cuenta.setEstado(true); // Estado por defecto como activo
            Cliente cliente = clienteService.findById(clienteId);
            cuenta.setCliente(cliente);
            cuentaService.crearCuenta(cuenta);
            return "redirect:/cuentas"; // Redirigir a la lista de cuentas
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear la cuenta: " + e.getMessage());
            return "cuenta-form"; // Redirigir al formulario de creación en caso de error
        }
    }

    @PostMapping("/eliminar")
    public String eliminarCuenta(@RequestParam Long id, Model model) {
        try {
            cuentaService.eliminarCuenta(id);
            return "redirect:/cuentas"; // Redirigir a la lista de cuentas
        } catch (Exception e) {
            model.addAttribute("error", "Error al eliminar la cuenta: " + e.getMessage());
            return "cuenta-list"; // Redirigir a la lista de cuentas en caso de error
        }
    }

    @PostMapping("/depositar")
    public String depositar(@RequestParam Long idCuenta, @RequestParam Float monto, Model model) {
        try {
            cuentaService.depositar(idCuenta, monto);
            return "redirect:/cuentas"; // Redirigir a la lista de cuentas después de un depósito exitoso
        } catch (Exception e) {
            model.addAttribute("error", "Error al depositar: " + e.getMessage());
            return "cuenta-list"; // Vuelve a mostrar la lista de cuentas en caso de error
        }
    }

    @PostMapping("/retirar")
    public String retirar(@RequestParam Long idCuenta, @RequestParam Float monto, Model model) {
        try {
            cuentaService.retirar(idCuenta, monto);
            return "redirect:/cuentas";

        } catch (Exception e) {
            model.addAttribute("error", "Error al retirar dinero: " + e.getMessage());
            return "retiro"; // Vuelve a mostrar la vista de retiro en caso de error
        }
    }

    @GetMapping("/{id}/movimientos")
    public String verMovimientos(@PathVariable Long id, Model model) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findById(id);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            List<Movimiento> movimientos = movimientoRepository.findByCuentaId(id);
            model.addAttribute("cuenta", cuenta);
            model.addAttribute("movimientos", movimientos);
        } else {
            model.addAttribute("error", "Cuenta no encontrada.");
        }
        return "movimientos-list";
    }
}
