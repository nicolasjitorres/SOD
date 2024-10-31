package com.sistema.sistemaR1.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sistema.sistemaR1.modelos.Cliente;
import com.sistema.sistemaR1.servicios.ClienteService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "http://localhost:5173")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
        return ResponseEntity.ok(clientes);
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearCliente(@RequestParam String nombre) {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre del cliente no puede estar vacío.");
            }

            Cliente cliente = new Cliente();
            cliente.setNombre(nombre);
            cliente.setFechaRegistro(new Date()); // Establecer la fecha de registro
            clienteService.crearCliente(cliente);

            return ResponseEntity.status(HttpStatus.CREATED).body("Cliente creado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el cliente: " + e.getMessage());
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
        try {
            clienteService.eliminarCliente(id);
            return ResponseEntity.ok("Cliente eliminado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el cliente: " + e.getMessage());
        }
    }
}
