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
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
class EnderecoRestAssuredIT {

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
    }

    @Test
    @DisplayName("POST /api/v2/enderecos deve cadastrar um endereço com sucesso")
    void deveCadastrarEndereco() {
        Map<String, Object> payload = Map.of(
                "rua", "Avenida Paulista",
                "numero", "1500",
                "cidade", "São Paulo",
                "estado", "SP",
                "cep", "01310-100"
        );

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/v2/enderecos")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("rua", equalTo("Avenida Paulista"))
                .body("cidade", equalTo("São Paulo"))
                .body("cep", equalTo("01310-100"));
    }

    @Test
    @DisplayName("GET /api/v2/enderecos deve listar os endereços cadastrados")
    void deveListarEnderecos() {
        jdbcTemplate.update(
                "INSERT INTO endereco (rua, numero, cidade, estado, cep) VALUES (?, ?, ?, ?, ?)",
                "Avenida Paulista",
                "1500",
                "São Paulo",
                "SP",
                "01310-100"
        );

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/v2/enderecos")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("[0].rua", equalTo("Avenida Paulista"))
                .body("[0].cidade", equalTo("São Paulo"));
    }
}
