package com.denkitronik.clienteservice.domain.services;

import com.denkitronik.clienteservice.domain.entities.Cliente;
import com.denkitronik.clienteservice.domain.entities.Region;
import com.denkitronik.clienteservice.domain.exception.ClienteNotFoundException;
import com.denkitronik.clienteservice.domain.exception.ClienteServiceException;
import com.denkitronik.clienteservice.domain.repositories.IClienteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private IClienteDao clienteDao;

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> findAll(Pageable pageable) {
        return clienteDao.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return clienteDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findById(Long id) {
        return clienteDao.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));
    }

    @Override
    @Transactional
    public Cliente save(Cliente cliente) {
        asociarRegionValida(cliente);
        try {
            return clienteDao.save(cliente);
        } catch (DataIntegrityViolationException ex) {
            throw new ClienteServiceException(
                    "Error al guardar el cliente: datos duplicados o restricción violada",
                    ex
            );
        }
    }

    @Override
    @Transactional
    public Cliente update(Long id, Cliente cliente) {
        Cliente actual = clienteDao.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));

        actual.setNombre(cliente.getNombre());
        actual.setApellido(cliente.getApellido());
        actual.setEmail(cliente.getEmail());
        actual.setFoto(cliente.getFoto());

        if (cliente.getRegion() != null) {
            actual.setRegion(cliente.getRegion());
            asociarRegionValida(actual);
        }

        try {
            return clienteDao.save(actual);
        } catch (DataIntegrityViolationException ex) {
            throw new ClienteServiceException(
                    "Error al actualizar el cliente: datos duplicados o restricción violada",
                    ex
            );
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!clienteDao.existsById(id)) {
            throw new ClienteNotFoundException(id);
        }

        clienteDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Region> findAllRegiones() {
        return clienteDao.findAllRegiones();
    }

    private void asociarRegionValida(Cliente cliente) {
        if (cliente == null || cliente.getRegion() == null) {
            return;
        }

        Region regionCliente = cliente.getRegion();
        List<Region> regionesExistentes = clienteDao.findAllRegiones();

        if (regionesExistentes != null && !regionesExistentes.isEmpty()) {
            // 1. Intentar hacer match por ID
            if (regionCliente.getId() != null) {
                Region porId = regionesExistentes.stream()
                        .filter(r -> r.getId() != null && r.getId().equals(regionCliente.getId()))
                        .findFirst()
                        .orElse(null);
                if (porId != null) {
                    cliente.setRegion(porId);
                    return;
                }
            }

            // 2. Intentar hacer match por nombre
            if (regionCliente.getNombre() != null) {
                Region porNombre = regionesExistentes.stream()
                        .filter(r -> r.getNombre() != null &&
                                r.getNombre().equalsIgnoreCase(regionCliente.getNombre()))
                        .findFirst()
                        .orElse(null);
                if (porNombre != null) {
                    cliente.setRegion(porNombre);
                    return;
                }
            }

            // 3. Si se envió un ID que no está en la BD, asignar la primera región de la lista para evitar FK violation
            cliente.setRegion(regionesExistentes.get(0));
        }
    }
}