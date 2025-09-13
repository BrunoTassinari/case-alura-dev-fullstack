package br.com.alura.projeto.util;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorDTO {

    private final LocalDateTime timestamp;
    private final Integer status;
    private final String error;
    private final String message;
    private List<ErrorItemDTO> errors;

    public ApiErrorDTO(Integer status, String error, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public ApiErrorDTO(Integer status, String error, String message, List<ErrorItemDTO> errors) {
        this(status, error, message);
        this.errors = errors;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public Integer getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public List<ErrorItemDTO> getErrors() { return errors; }
}