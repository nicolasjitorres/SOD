package com.sistema.sistemaP.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.sistemaP.modelos.Cliente;
import com.sistema.sistemaP.repositorios.ClienteRepository;
import com.sistema.sistemaP.repositorios.CuentaRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll(); 
    }

    public List<Cliente> obtenerClientesSinCuenta() {
        return clienteRepository.findAll().stream()
                .filter(cliente -> !cuentaRepository.existsByClienteId(cliente.getId()))
                .collect(Collectors.toList());
    }

    public void crearCliente(Cliente cliente) {
        cliente.setFechaRegistro(new Date()); 
        clienteRepository.save(cliente); 
    }

    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    public Cliente findById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
    }

}
