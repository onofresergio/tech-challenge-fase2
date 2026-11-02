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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
class RestauranteRestAssuredIT {

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

        jdbcTemplate.execute("DELETE FROM menu");
        jdbcTemplate.execute("DELETE FROM restaurante");
        jdbcTemplate.execute("DELETE FROM usuario");
        jdbcTemplate.execute("DELETE FROM endereco");
        jdbcTemplate.execute("DELETE FROM tipo_cozinha");
        jdbcTemplate.execute("DELETE FROM tipo_usuario");

        jdbcTemplate.update("INSERT INTO tipo_usuario (nome) VALUES (?)", "Dono de Restaurante");
        jdbcTemplate.update("INSERT INTO tipo_cozinha (nome) VALUES (?)", "Italiana");
        jdbcTemplate.update("INSERT INTO endereco (rua, numero, cidade, estado, cep) VALUES (?, ?, ?, ?, ?)",
                "Rua das Flores", "123", "São Paulo", "SP", "01000-000");
        jdbcTemplate.update(
                "INSERT INTO usuario (nome, email, senha, login, data_ultima_alteracao, endereco_id, tipo_usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?)",
                "João Dono",
                "joao@email.com",
                "$2a$10$8SxN3Vf8jz0Yh4QfM0H8QeSh0l6P0VvC8M9Qxw8kL6m3gI0E3/By",
                "joao_dono",
                null,
                1L,
                1L
        );
    }

    @Test
    @DisplayName("POST /api/v2/restaurantes deve cadastrar um restaurante com sucesso")
    void deveCadastrarRestaurante() {
        Map<String, Object> payload = Map.of(
                "nome", "Sabor do Norte",
                "enderecoId", 1L,
                "tipoCozinhaId", 1L,
                "horarioFuncionamento", "2025-10-11T12:00:00",
                "usuarioId", 1L
        );

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/v2/restaurantes")
                .then()
                .statusCode(201)
                .body("nome", equalTo("Sabor do Norte"))
                .body("tipoCozinha.id", equalTo(1));
    }

    @Test
    @DisplayName("GET /api/v2/restaurantes deve listar os restaurantes cadastrados")
    void deveListarRestaurantes() {
        jdbcTemplate.update(
                "INSERT INTO restaurante (nome, endereco_id, tipo_cozinha_id, horario_funcionamento, data_ultima_alteracao, usuario_id) VALUES (?, ?, ?, ?, ?, ?)",
                "Sabor do Norte",
                1L,
                1L,
                "2025-10-11T12:00:00",
                null,
                1L
        );

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/v2/restaurantes")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("[0].nome", equalTo("Sabor do Norte"));
    }
}
