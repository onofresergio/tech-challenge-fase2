package br.com.fiap.techchallenge;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class TechchallengeApplicationTests {

    // Removemos o @ServiceConnection e vamos mapear na mão usando o bloco abaixo
    @Container
    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("techchallenge_db")
            .withUsername("root")
            .withPassword("root");

    // Este bloco injeta os dados reais do container nas variáveis que seu DatabaseConfiguration lê
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);

        // DICA: Se a sua classe 'DatabaseConfiguration' ler chaves customizadas do YAML
        // (por exemplo: "db.url" ou "banco.dados.link"), mude as strings acima para as chaves exatas que ela usa!
    }

    @Test
    @DisplayName("main: Deve invocar o SpringApplication.run com os argumentos corretos para inicializar o boot")
    void mainDeveChamarSpringApplicationRunComSucesso() {
        String[] args = new String[]{"--server.port=0"};
        ConfigurableApplicationContext contextMock = mock(ConfigurableApplicationContext.class);

        try (MockedStatic<SpringApplication> springApplicationMock = mockStatic(SpringApplication.class)) {
            springApplicationMock.when(() -> SpringApplication.run(eq(TechchallengeApplication.class), any(String[].class)))
                    .thenReturn(contextMock);

            TechchallengeApplication.main(args);

            springApplicationMock.verify(() -> SpringApplication.run(TechchallengeApplication.class, args), Mockito.times(1));
        }
    }

    @Test
    @DisplayName("Constructor: Deve permitir instanciar a classe explicitamente para cobrir o construtor padrão implícito")
    void deveGarantirCoberturaDoConstrutorPadrao() {
        TechchallengeApplication application = new TechchallengeApplication();
        assertThat(application).isNotNull();
    }
}
