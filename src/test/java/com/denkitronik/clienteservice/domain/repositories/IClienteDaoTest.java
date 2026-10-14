package com.denkitronik.clienteservice.domain.repositories;

import com.denkitronik.clienteservice.domain.entities.Cliente;
import com.denkitronik.clienteservice.domain.entities.Region;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Integration tests (data slice) — IClienteDao con H2")
class IClienteDaoTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private IClienteDao clienteDao;

    private Region region;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        region = new Region();
        region.setNombre("Asia");
        em.persist(region);

        cliente = new Cliente();
        cliente.setNombre("Grace");
        cliente.setApellido("Hopper");
        cliente.setEmail("grace@navy.mil");
        cliente.setRegion(region);

        em.persist(cliente);
        em.flush();
    }

    // 1. Probar la consulta personalizada de regiones
    @Test
    @DisplayName("findAllRegiones devuelve las regiones persistidas")
    void findAllRegiones_debeRetornarListaDeRegiones() {
        List<Region> regiones = clienteDao.findAllRegiones();

        assertThat(regiones).hasSize(1);
        assertThat(regiones.get(0).getNombre()).isEqualTo("Asia");
    }

    // 2. Buscar un cliente existente
    @Test
    @DisplayName("findById encuentra un cliente existente")
    void findById_debeEncontrarCliente() {
        Optional<Cliente> resultado =
                clienteDao.findById(cliente.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Grace");
        assertThat(resultado.get().getApellido()).isEqualTo("Hopper");
    }

    // 3. Listar los clientes
    @Test
    @DisplayName("findAll devuelve los clientes registrados")
    void findAll_debeRetornarClientes() {
        List<Cliente> clientes = clienteDao.findAll();

        assertThat(clientes).hasSize(1);
        assertThat(clientes.get(0).getEmail())
                .isEqualTo("grace@navy.mil");
    }

    // 4. Probar la paginación
    @Test
    @DisplayName("findAll paginado devuelve una página de clientes")
    void findAllPaginado_debeRetornarPagina() {
        Cliente segundoCliente = new Cliente();
        segundoCliente.setNombre("Alan");
        segundoCliente.setApellido("Turing");
        segundoCliente.setEmail("alan@computing.uk");
        segundoCliente.setRegion(region);

        em.persist(segundoCliente);
        em.flush();

        Page<Cliente> pagina =
                clienteDao.findAll(PageRequest.of(0, 1));

        assertThat(pagina.getContent()).hasSize(1);
        assertThat(pagina.getTotalElements()).isEqualTo(2);
        assertThat(pagina.getTotalPages()).isEqualTo(2);
    }

    // 5. Guardar un cliente
    @Test
    @DisplayName("save guarda un nuevo cliente")
    void save_debeGuardarCliente() {
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre("Katherine");
        nuevoCliente.setApellido("Johnson");
        nuevoCliente.setEmail("katherine@nasa.gov");
        nuevoCliente.setRegion(region);

        Cliente guardado = clienteDao.save(nuevoCliente);
        em.flush();

        assertThat(guardado.getId()).isNotNull();

        Optional<Cliente> resultado =
                clienteDao.findById(guardado.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre())
                .isEqualTo("Katherine");
    }

    // 6. Eliminar un cliente y verificarlo directamente en la BD
    @Test
    @DisplayName("deleteById elimina el cliente")
    void deleteById_debeEliminarElCliente() {
        Long id = cliente.getId();

        clienteDao.deleteById(id);
        em.flush();
        em.clear();

        assertThat(em.find(Cliente.class, id)).isNull();
    }
}
