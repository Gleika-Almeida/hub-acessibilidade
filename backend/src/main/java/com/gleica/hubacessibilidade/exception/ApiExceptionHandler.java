package com.gleica.hubacessibilidade.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ScanNotFoundException.class)
    public ProblemDetail handleScanNotFound(
            ScanNotFoundException exception,
            HttpServletRequest request
    ) {
        ProblemDetail problem = ProblemDetail.forStatus(
                HttpStatus.NOT_FOUND
        );

        problem.setTitle("Análise não encontrada");
        problem.setDetail(exception.getMessage());
        problem.setType(
                URI.create(
                        "https://accessibility-hub.local/problems/scan-not-found"
                )
        );
        problem.setInstance(
                URI.create(request.getRequestURI())
        );

        return problem;
    }
}
