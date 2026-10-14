package com.denkitronik.clienteservice.domain.services;

import com.denkitronik.clienteservice.domain.entities.Cliente;
import com.denkitronik.clienteservice.domain.entities.Region;
import com.denkitronik.clienteservice.domain.exception.ClienteNotFoundException;
import com.denkitronik.clienteservice.domain.exception.ClienteServiceException;
import com.denkitronik.clienteservice.domain.repositories.IClienteDao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests — ClienteServiceImpl con Mockito")
class ClienteServiceImplTest {

    @Mock
    private IClienteDao clienteDao;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private Region region;

    @BeforeEach
    void setUp() {
        region = new Region();
        region.setId(1L);
        region.setNombre("Sudamérica");

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Linus");
        cliente.setApellido("Torvalds");
        cliente.setEmail("linus@kernel.org");
        cliente.setRegion(region);
    }

    // 1. findAll paginado
    @Test
    @DisplayName("findAll paginado — devuelve la página")
    void findAll_paginado_debeRetornarPagina() {
        Pageable pageable = mock(Pageable.class);
        Page<Cliente> pagina = mock(Page.class);

        when(clienteDao.findAll(pageable)).thenReturn(pagina);

        Page<Cliente> resultado = clienteService.findAll(pageable);

        assertThat(resultado).isSameAs(pagina);
        verify(clienteDao).findAll(pageable);
    }

    // 2. findAll sin paginación
    @Test
    @DisplayName("findAll — devuelve la lista de clientes")
    void findAll_debeRetornarLista() {
        when(clienteDao.findAll()).thenReturn(List.of(cliente));

        List<Cliente> resultado = clienteService.findAll();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Linus");
        verify(clienteDao).findAll();
    }

    // 3. findById: cliente existente
    @Test
    @DisplayName("findById — ID existente devuelve cliente")
    void findById_idExistente_debeRetornarCliente() {
        when(clienteDao.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente resultado = clienteService.findById(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Linus");
        verify(clienteDao).findById(1L);
    }

    // 4. findById: cliente inexistente
    @Test
    @DisplayName("findById — ID inexistente lanza excepción")
    void findById_idInexistente_debeLanzarExcepcion() {
        when(clienteDao.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.findById(999L))
                .isInstanceOf(ClienteNotFoundException.class)
                .hasMessageContaining("999");
    }

    // 5. save: guardado correcto
    @Test
    @DisplayName("save — guarda y devuelve el cliente")
    void save_debeGuardarCliente() {
        when(clienteDao.save(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.save(cliente);

        assertThat(resultado).isSameAs(cliente);
        verify(clienteDao).save(cliente);
    }

    // 6. save: datos duplicados
    @Test
    @DisplayName("save — datos duplicados lanzan excepción del servicio")
    void save_emailDuplicado_debeLanzarClienteServiceException() {
        when(clienteDao.save(cliente))
                .thenThrow(new DataIntegrityViolationException("duplicate key"));

        assertThatThrownBy(() -> clienteService.save(cliente))
                .isInstanceOf(ClienteServiceException.class)
                .hasMessageContaining("duplicados");
    }

    // 7. update: cliente existente
    @Test
    @DisplayName("update — actualiza y devuelve el cliente")
    void update_idExistente_debeActualizarYRetornar() {
        Cliente datosNuevos = new Cliente();
        datosNuevos.setNombre("Linus Updated");
        datosNuevos.setApellido("Torvalds");
        datosNuevos.setEmail("linus2@kernel.org");
        datosNuevos.setRegion(region);

        when(clienteDao.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteDao.save(any(Cliente.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Cliente resultado = clienteService.update(1L, datosNuevos);

        assertThat(resultado.getNombre()).isEqualTo("Linus Updated");
        assertThat(resultado.getApellido()).isEqualTo("Torvalds");
        assertThat(resultado.getEmail()).isEqualTo("linus2@kernel.org");

        verify(clienteDao).findById(1L);
        verify(clienteDao).save(any(Cliente.class));
    }

    // 8. update: cliente inexistente
    @Test
    @DisplayName("update — ID inexistente lanza excepción")
    void update_idInexistente_debeLanzarExcepcion() {
        when(clienteDao.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.update(999L, cliente))
                .isInstanceOf(ClienteNotFoundException.class)
                .hasMessageContaining("999");

        verify(clienteDao, never()).save(any(Cliente.class));
    }

    // 9. delete: cliente existente
    @Test
    @DisplayName("delete — elimina cuando el cliente existe")
    void delete_idExistente_debeEliminarCliente() {
        when(clienteDao.existsById(1L)).thenReturn(true);

        clienteService.delete(1L);

        verify(clienteDao).existsById(1L);
        verify(clienteDao).deleteById(1L);
    }

    // 10. delete: cliente inexistente
    @Test
    @DisplayName("delete — ID inexistente no debe eliminar")
    void delete_idInexistente_noDebeEliminar() {
        when(clienteDao.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> clienteService.delete(999L))
                .isInstanceOf(ClienteNotFoundException.class);

        verify(clienteDao, never()).deleteById(any());
    }

    // 11. findAllRegiones
    @Test
    @DisplayName("findAllRegiones — devuelve las regiones")
    void findAllRegiones_debeRetornarLista() {
        when(clienteDao.findAllRegiones()).thenReturn(List.of(region));

        List<Region> resultado = clienteService.findAllRegiones();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Sudamérica");
        verify(clienteDao).findAllRegiones();
    }
}
