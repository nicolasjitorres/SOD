package com.sistema.demo.servicios;

import com.sistema.demo.modelos.Cliente;
import com.sistema.demo.repositorios.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll(); // Obtener todos los clientes
    }

    public void crearCliente(Cliente cliente) {
        cliente.setFechaRegistro(new Date()); // Establecer la fecha de registro al crear el cliente
        clienteRepository.save(cliente); // Guardar el cliente en la base de datos
    }

    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    public Cliente findById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
    }

}
