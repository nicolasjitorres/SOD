package com.sistema.demo.controladores;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/deposito")
    public String rutaDeposito() {
        return "deposito";
    }

    @GetMapping("/retiro")
    public String rutaRetiro() {
        return "retiro";
    }

    @GetMapping("/transferencia")
    public String rutaTransferencia() {
        return "transferencia";
    }

    @GetMapping
    public String listarCuentas(Model model) {
        List<Cuenta> cuentas = cuentaService.obtenerTodasLasCuentas();
        model.addAttribute("cuentas", cuentas);
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        model.addAttribute("clientesSC", clienteService.obtenerClientesSinCuenta());
        return "cuenta-list";
    }

    @PostMapping("/crear")
    public String crearCuenta(@RequestParam String tipoCuenta, @RequestParam Float saldo,
            @RequestParam Long clienteId, RedirectAttributes redirectAttributes) {
        try {
            Cuenta cuenta = new Cuenta();
            cuenta.setTipoCuenta(tipoCuenta);
            cuenta.setSaldo(saldo);
            cuenta.setEstado(true);
            Cliente cliente = clienteService.findById(clienteId);
            cuenta.setCliente(cliente);

            cuentaService.crearCuenta(cuenta);

            redirectAttributes.addFlashAttribute("success", "Cuenta creada exitosamente.");
            return "redirect:/cuentas";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cuentas"; 
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear la cuenta: " + e.getMessage());
            return "redirect:/cuentas";
        }
    }


    @PostMapping("/eliminar")
    public String eliminarCuenta(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            cuentaService.eliminarCuenta(id);
            redirectAttributes.addFlashAttribute("success", "Cuenta eliminada exitosamente.");
            return "redirect:/cuentas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la cuenta: " + e.getMessage());
            return "redirect:/cuentas"; 
        }
    }

    @PostMapping("/depositar")
    public String depositar(@RequestParam Long idCuenta, @RequestParam Float monto,
            RedirectAttributes redirectAttributes) {
        try {
            cuentaService.depositar(idCuenta, monto);
            redirectAttributes.addFlashAttribute("success",
                    "Depósito realizado por " + monto + " en la cuenta ID: " + idCuenta);
            return "redirect:/cuentas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al depositar: " + e.getMessage());
            return "redirect:/cuentas"; 
        }
    }

    @PostMapping("/retirar")
    public String retirar(@RequestParam Long idCuenta, @RequestParam Float monto,
            RedirectAttributes redirectAttributes) {
        try {
            cuentaService.retirar(idCuenta, monto);
            redirectAttributes.addFlashAttribute("success", "Retiro realizado con éxito.");
            return "redirect:/cuentas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al retirar dinero: " + e.getMessage());
            return "redirect:/cuentas"; 
        }
    }

    @GetMapping("/{id}/movimientos")
    public String verMovimientos(@PathVariable Long id, Model model) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findById(id);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            List<Movimiento> movimientos = movimientoRepository.findByCuentaId(cuenta.getId());
            System.out.println(movimientos);
            model.addAttribute("cuenta", cuenta);
            model.addAttribute("movimientos", movimientos);
        } else {
            model.addAttribute("error", "Cuenta no encontrada.");
        }
        return "movimientos-list";
    }

    @PostMapping("/transferir")
    public String transferir(@RequestParam Long idCuentaOrigen,
            @RequestParam Long idCuentaDestino,
            @RequestParam Float monto,
            RedirectAttributes redirectAttributes) {
        try {
            cuentaService.transferir(idCuentaOrigen, idCuentaDestino, monto);
            redirectAttributes.addFlashAttribute("success", "Transferencia de " + monto + " realizada de la cuenta ID: "
                    + idCuentaOrigen + " a la cuenta ID: " + idCuentaDestino);
            return "redirect:/cuentas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al realizar la transferencia: " + e.getMessage());
            return "redirect:/cuentas";
        }
    }
}
