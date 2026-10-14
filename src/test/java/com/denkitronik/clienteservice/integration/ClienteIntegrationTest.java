package com.denkitronik.clienteservice.integration;

import com.denkitronik.clienteservice.domain.entities.Cliente;
import com.denkitronik.clienteservice.domain.entities.Region;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureTestRestTemplate
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClienteIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:16");

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String url;

    private static Long clienteId;

    @BeforeEach
    void configurar() {
        url = "http://localhost:" + port
                + "/api/v1/cliente-service/clientes";

        jdbcTemplate.update(
                "INSERT INTO regiones (id, nombre) VALUES (1, ?) "
                        + "ON CONFLICT (id) DO NOTHING",
                "Sur América"
        );
    }

    @Test
    @Order(1)
    void crearCliente() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan");
        cliente.setApellido("Torres");
        cliente.setEmail("juan.integration@example.com");

        Region region = new Region();
        region.setId(1L);
        cliente.setRegion(region);

        ResponseEntity<Cliente> respuesta =
                restTemplate.postForEntity(
                        url,
                        cliente,
                        Cliente.class
                );

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());

        clienteId = respuesta.getBody().getId();
        assertNotNull(clienteId);
    }

    @Test
    @Order(2)
    void consultarClienteCreado() {
        assertNotNull(clienteId, "Primero debe crearse un cliente");

        ResponseEntity<Cliente> respuesta =
                restTemplate.getForEntity(
                        url + "/" + clienteId,
                        Cliente.class
                );

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals("Juan", respuesta.getBody().getNombre());
    }

    @Test
    @Order(3)
    void listarClientes() {
        ResponseEntity<Cliente[]> respuesta =
                restTemplate.getForEntity(
                        url,
                        Cliente[].class
                );

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertTrue(respuesta.getBody().length > 0);
    }

    @Test
    @Order(4)
    void listarRegiones() {
        ResponseEntity<Region[]> respuesta =
                restTemplate.getForEntity(
                        "http://localhost:" + port
                                + "/api/v1/cliente-service/clientes/regiones",
                        Region[].class
                );

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
    }

    @Test
    @Order(5)
    void actualizarCliente() {
        assertNotNull(clienteId, "Primero debe crearse un cliente");

        Cliente actualizado = new Cliente();
        actualizado.setId(clienteId);
        actualizado.setNombre("Juan David");
        actualizado.setApellido("Torres");
        actualizado.setEmail("juan.integration@example.com");

        Region region = new Region();
        region.setId(1L);
        actualizado.setRegion(region);

        restTemplate.put(
                url + "/" + clienteId,
                actualizado
        );

        ResponseEntity<Cliente> respuesta =
                restTemplate.getForEntity(
                        url + "/" + clienteId,
                        Cliente.class
                );

        assertNotNull(respuesta.getBody());
        assertEquals("Juan David", respuesta.getBody().getNombre());
    }

    @Test
    @Order(6)
    void consultarClienteInexistente() {
        ResponseEntity<String> respuesta =
                restTemplate.getForEntity(
                        url + "/999999",
                        String.class
                );

        assertEquals(
                HttpStatus.NOT_FOUND,
                respuesta.getStatusCode()
        );
    }

    @Test
    @Order(7)
    void eliminarCliente() {
        assertNotNull(clienteId, "Primero debe crearse un cliente");

        restTemplate.delete(url + "/" + clienteId);

        ResponseEntity<String> respuesta =
                restTemplate.getForEntity(
                        url + "/" + clienteId,
                        String.class
                );

        assertEquals(
                HttpStatus.NOT_FOUND,
                respuesta.getStatusCode()
        );
    }

}