package utez.edu.mx.services.kernel;

import org.springframework.http.HttpStatus;

public class AppiResponse {
    private String message;
    private Object data;
    private boolean error;
    private HttpStatus status;

    // Éxito sin datos
    public AppiResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    // Éxito con datos
    public AppiResponse(String message, Object data, HttpStatus status) {
        this.message = message;
        this.data = data;
        this.status = status;
    }

    // Error sin datos
    public AppiResponse(String message, boolean error, HttpStatus status) {
        this.message = message;
        this.error = error;
        this.status = status;
    }

    // Error con datos
    public AppiResponse(String message, HttpStatus status, boolean error, Object data) {
        this.message = message;
        this.status = status;
        this.error = error;
        this.data = data;
    }

    public HttpStatus getStatus() { return status; }
    public boolean isError() { return error; }
    public Object getData() { return data; }
    public String getMessage() { return message; }
}