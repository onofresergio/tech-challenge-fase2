package br.com.fiap.techchallenge.infrastructure.controller.exception;

import br.com.fiap.techchallenge.domain.exception.BusinessException;
import br.com.fiap.techchallenge.domain.exception.RestauranteNotFoundException;
import br.com.fiap.techchallenge.domain.exception.TipoUsuarioNotFoundException;
import br.com.fiap.techchallenge.domain.exception.UnauthorizedUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        when(webRequest.getDescription(false)).thenReturn("uri=/api/v2/recurso");
    }

    @Test
    @DisplayName("handleValidationErrors: Deve processar FieldError e ObjectError gerando chaves no param customizado")
    void handleValidationErrorsDeveMapearErrosDeFormularioComCamposEFallback() {
        MethodArgumentNotValidException mockEx = mock(MethodArgumentNotValidException.class);
        BindingResult mockBindingResult = mock(BindingResult.class);

        FieldError fieldError = new FieldError("usuarioRequest", "email", "O e-mail é obrigatório.");
        ObjectError globalError = new ObjectError("usuarioRequest", "As senhas não coincidem.");

        when(mockBindingResult.getAllErrors()).thenReturn(List.of(fieldError, globalError));
        when(mockEx.getBindingResult()).thenReturn(mockBindingResult);

        ResponseEntity<ProblemDetail> response = exceptionHandler.handleValidationErrors(mockEx, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        ProblemDetail body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getTitle()).isEqualTo("Erro na validação dos dados enviados");
        assertThat(body.getInstance()).isEqualTo(URI.create("/api/v2/recurso"));

        @SuppressWarnings("unchecked")
        List<Map<String, String>> invalidParams = (List<Map<String, String>>) body.getProperties().get("invalid-params");
        assertThat(invalidParams).hasSize(2);
        assertThat(invalidParams.get(0)).containsEntry("name", "email");
        assertThat(invalidParams.get(0)).containsEntry("reason", "O e-mail é obrigatório.");
        assertThat(invalidParams.get(1)).containsEntry("name", "usuarioRequest");
        assertThat(invalidParams.get(1)).containsEntry("reason", "As senhas não coincidem.");
    }

    @Test
    @DisplayName("handleBusinessException: Deve converter BusinessException para o padrão ProblemDetail 422")
    void handleBusinessExceptionDeveRetornarStatus422() {
        BusinessException exception = new BusinessException("E-mail já cadastrado no sistema.") {};

        ResponseEntity<ProblemDetail> response = exceptionHandler.handleBusinessException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        ProblemDetail body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getDetail()).isEqualTo("E-mail já cadastrado no sistema.");
        assertThat(body.getTitle()).isEqualTo("Violação de regra de negócio");
    }

    @Test
    @DisplayName("handleNotFoundException: Deve converter TipoUsuarioNotFoundException para 404 Not Found")
    void handleNotFoundExceptionDeveMapearTipoUsuarioNotFound() {
        TipoUsuarioNotFoundException exception = new TipoUsuarioNotFoundException(1L);

        ResponseEntity<ProblemDetail> response = exceptionHandler.handleNotFoundException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ProblemDetail body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getTitle()).isEqualTo("Recurso não encontrado");
        assertThat(body.getDetail()).isEqualTo(exception.getMessage());
    }

    @Test
    @DisplayName("handleNotFoundException: Deve converter RestauranteNotFoundException para 404 Not Found")
    void handleNotFoundExceptionDeveMapearRestauranteNotFound() {
        RestauranteNotFoundException exception = new RestauranteNotFoundException(2L);

        ResponseEntity<ProblemDetail> response = exceptionHandler.handleNotFoundException(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ProblemDetail body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getTitle()).isEqualTo("Recurso não encontrado");
        assertThat(body.getDetail()).isEqualTo(exception.getMessage());
    }

    @Test
    @DisplayName("handleUnauthorizedUser: Deve converter UnauthorizedUserException para 403 Forbidden")
    void handleUnauthorizedUserDeveRetornarStatus403ComTimestamp() {
        UnauthorizedUserException exception = new UnauthorizedUserException(10L);

        ResponseEntity<ProblemDetail> response = exceptionHandler.handleUnauthorizedUser(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        ProblemDetail body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getTitle()).isEqualTo("Usuário não é dono de restaurante");
        assertThat(body.getProperties()).containsKey("timestamp");
    }

    @Test
    @DisplayName("handleInternalServerError: Deve capturar exceções genéricas e retornar 500 Internal Server Error")
    void handleInternalServerErrorDeveRetornarStatus500() {
        Exception exception = new Exception("Erro interno imprevisto simulado.");

        ResponseEntity<ProblemDetail> response = exceptionHandler.handleInternalServerError(exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        ProblemDetail body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getTitle()).isEqualTo("Erro interno do servidor");
        assertThat(body.getDetail()).isEqualTo("Ocorreu um erro interno inesperado no servidor. Por favor, tente novamente mais tarde.");
    }
}