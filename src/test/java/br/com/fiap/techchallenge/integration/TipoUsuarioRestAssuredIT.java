package br.com.fiap.techchallenge.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
class TipoUsuarioRestAssuredIT {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("techchallenge_db")
            .withUsername("root")
            .withPassword("root");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        RestAssuredTestSupport.configureDatasource(registry, mysql);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssuredTestSupport.resetRestAssured(port);

        jdbcTemplate.execute("DELETE FROM usuario");
        jdbcTemplate.execute("DELETE FROM restaurante");
        jdbcTemplate.execute("DELETE FROM menu");
        jdbcTemplate.execute("DELETE FROM endereco");
        jdbcTemplate.execute("DELETE FROM tipo_cozinha");
        jdbcTemplate.execute("DELETE FROM tipo_usuario");
    }

    @Test
    @DisplayName("POST /api/v2/tipo-usuario deve cadastrar um tipo de usuário")
    void deveCadastrarTipoUsuario() {
        Map<String, String> payload = Map.of("nome", "Dono de Restaurante");

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/v2/tipo-usuario")
                .then()
                .statusCode(201)
                .body("nome", equalTo("Dono de Restaurante"));
    }

    @Test
    @DisplayName("GET /api/v2/tipo-usuario deve listar os tipos de usuário cadastrados")
    void deveListarTiposUsuario() {
        jdbcTemplate.update("INSERT INTO tipo_usuario (nome) VALUES (?)", "Cliente");
        jdbcTemplate.update("INSERT INTO tipo_usuario (nome) VALUES (?)", "Dono de Restaurante");

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/v2/tipo-usuario")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(2));
    }
}
