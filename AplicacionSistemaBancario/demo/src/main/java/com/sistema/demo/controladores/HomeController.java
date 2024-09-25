package com.sistema.demo.controladores;

import com.sistema.demo.servicios.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping("/")
    public String rutaIndex() {
        return "index"; // Página principal
    }

    @GetMapping("/deposito")
    public String rutaDeposito() {
        return "deposito"; // Página de depósito
    }

    @GetMapping("/retiro")
    public String rutaRetiro() {
        return "retiro"; // Página de retiro
    }

    @GetMapping("/transferencia")
    public String rutaTransferencia() {
        return "transferencia"; // Página de transferencia
    }

    @PostMapping("/deposito")
    public String realizarDeposito(@RequestParam Long idCuenta, @RequestParam float monto, Model model) {
        try {
            if (monto <= 0) {
                throw new RuntimeException("El monto debe ser mayor que cero.");
            }
            cuentaService.depositar(idCuenta, monto);
            model.addAttribute("resultado", "Depósito exitoso.");
        } catch (RuntimeException e) {
            model.addAttribute("resultado", "Error: " + e.getMessage());
        }
        return "resultado"; // Página de resultado
    }

    @PostMapping("/retiro")
    public String realizarRetiro(@RequestParam Long idCuenta, @RequestParam float monto, Model model) {
        try {
            if (monto <= 0) {
                throw new RuntimeException("El monto debe ser mayor que cero.");
            }
            cuentaService.retirar(idCuenta, monto);
            model.addAttribute("resultado", "Retiro exitoso.");
        } catch (RuntimeException e) {
            model.addAttribute("resultado", "Error: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            model.addAttribute("resultado", "El retiro fue interrumpido.");
        }
        return "resultado"; // Página de resultado
    }

    @PostMapping("/transferencia")
    public String realizarTransferencia(@RequestParam Long idCuentaOrigen, @RequestParam Long idCuentaDestino, @RequestParam float monto, Model model) {
        try {
            if (monto <= 0) {
                throw new RuntimeException("El monto debe ser mayor que cero.");
            }
            cuentaService.transferir(idCuentaOrigen, idCuentaDestino, monto);
            model.addAttribute("resultado", "Transferencia exitosa.");
        } catch (RuntimeException e) {
            model.addAttribute("resultado", "Error: " + e.getMessage());
        }
        return "resultado"; // Página de resultado
    }
}
