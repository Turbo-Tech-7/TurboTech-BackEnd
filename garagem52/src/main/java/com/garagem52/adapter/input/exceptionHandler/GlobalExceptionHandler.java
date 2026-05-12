package com.garagem52.adapter.input.exceptionHandler;

import com.garagem52.adapter.input.dto.response.ErroResponse;
import com.garagem52.domain.exception.BusinessException;
import com.garagem52.domain.exception.fornecedor.FornecedorNotFoundException;
import com.garagem52.domain.exception.orcamento.MotivoCancelamentoObrigatorioException;
import com.garagem52.domain.exception.orcamento.MotivoInvalidoException;
import com.garagem52.domain.exception.orcamento.OrcamentoNotFoundException;
import com.garagem52.domain.exception.peca.PecaNotFoundException;
import com.garagem52.domain.exception.servico.ServicoNotFoundException;
import com.garagem52.domain.exception.user.EmailAlreadyExistsException;
import com.garagem52.domain.exception.user.UserNotFoundException;
import com.garagem52.domain.exception.veiculo.VeiculoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErroResponse> handleBusiness(BusinessException ex) {
        return ResponseEntity.badRequest().body(buildError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErroResponse> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErroResponse> handleEmailExists(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildError(HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErroResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildError(HttpStatus.UNAUTHORIZED, ex.getMessage()));
    }

    @ExceptionHandler(VeiculoNotFoundException.class)
    public ResponseEntity<ErroResponse> handleVeiculoNotFound(VeiculoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(PecaNotFoundException.class)
    public ResponseEntity<ErroResponse> handlePecaNotFound(PecaNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(OrcamentoNotFoundException.class)
    public ResponseEntity<ErroResponse> handleOrcamentoNotFound(OrcamentoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(ServicoNotFoundException.class)
    public ResponseEntity<ErroResponse> handleServicoNotFound(ServicoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(FornecedorNotFoundException.class)
    public ResponseEntity<ErroResponse> handleFornecedorNotFound(FornecedorNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(MotivoCancelamentoObrigatorioException.class)
    public ResponseEntity<ErroResponse> handleMotivoCancelamento(MotivoCancelamentoObrigatorioException ex) {
        return ResponseEntity.badRequest().body(buildError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(MotivoInvalidoException.class)
    public ResponseEntity<ErroResponse> handleMotivoInvalido(MotivoInvalidoException ex) {
        return ResponseEntity.badRequest().body(buildError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", "Erro de validação");
        body.put("errors", fieldErrors);
        body.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponse> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Erro interno: " + ex.getMessage()));
    }

    private ErroResponse buildError(HttpStatus status, String message) {
        ErroResponse error = new ErroResponse();
        error.setStatus(status.value());
        error.setMessage(message);
        return error;
    }
}
