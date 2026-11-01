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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
class UsuarioRestAssuredIT {

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
        jdbcTemplate.execute("DELETE FROM endereco");
        jdbcTemplate.execute("DELETE FROM tipo_usuario");

        jdbcTemplate.update("INSERT INTO tipo_usuario (nome) VALUES (?)", "Dono de Restaurante");
        jdbcTemplate.update("INSERT INTO tipo_usuario (nome) VALUES (?)", "Cliente");
        jdbcTemplate.update("INSERT INTO endereco (rua, numero, cidade, estado, cep) VALUES (?, ?, ?, ?, ?)",
                "Rua das Flores", "123", "São Paulo", "SP", "01000-000");
    }

    @Test
    @DisplayName("POST /api/v2/usuarios deve criar um usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        Map<String, Object> payload = Map.of(
                "nome", "Maria Souza",
                "email", "maria@email.com",
                "senha", "Senha@123",
                "login", "maria.souza",
                "enderecoId", 1L,
                "tipoUsuarioId", 1L
        );

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/v2/usuarios")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("nome", equalTo("Maria Souza"))
                .body("email", equalTo("maria@email.com"))
                .body("login", equalTo("maria.souza"))
                .body("tipoUsuario.id", equalTo(1));
    }

    @Test
    @DisplayName("GET /api/v2/usuarios deve listar os usuários cadastrados")
    void deveListarUsuariosCadastrados() {
        jdbcTemplate.update(
                "INSERT INTO usuario (nome, email, senha, login, data_ultima_alteracao, endereco_id, tipo_usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?)",
                "Maria Souza",
                "maria@email.com",
                "$2a$10$N3KKs7k0IxPUPnKkM9Tn3OxMq4JbZqZsH2n7QdIuF9nWQ7eQ4Yb0m",
                "maria.souza",
                null,
                1L,
                1L
        );

        var response = given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/v2/usuarios");

        response.then().statusCode(200);

        var json = response.asString();
        assertThat(json).contains("Maria Souza");
        assertThat(json).contains("maria@email.com");
        assertThat(json).contains("maria.souza");
    }
}
