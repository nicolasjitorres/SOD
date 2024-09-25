package com.sistema.demo.controladores;

import com.sistema.demo.modelos.Cliente;
import com.sistema.demo.servicios.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        return "cliente-list"; // Vista de la lista de clientes
    }

    @PostMapping("/crear")
    public String crearCliente(@RequestParam String nombre, Model model) {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                model.addAttribute("error", "El nombre del cliente no puede estar vacío.");
                return "cliente-list"; // Vuelve a la lista si hay un error
            }

            Cliente cliente = new Cliente();
            cliente.setNombre(nombre);
            cliente.setFechaRegistro(new Date()); // Establecer la fecha de registro
            clienteService.crearCliente(cliente);
            return "redirect:/clientes"; // Redirigir a la lista de clientes
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear el cliente: " + e.getMessage());
            return "cliente-list"; // Vuelve a la lista en caso de error
        }
    }

    @PostMapping("/eliminar")
    public String eliminarCliente(@RequestParam Long id, Model model) {
        try {
            clienteService.eliminarCliente(id);
            return "redirect:/clientes"; // Redirigir a la lista de clientes
        } catch (Exception e) {
            model.addAttribute("error", "Error al eliminar el cliente: " + e.getMessage());
            return "cliente-list"; // Vuelve a la lista en caso de error
        }
    }
}
