package com.gleica.hubacessibilidade.exception;

public class ScanNotFoundException
        extends RuntimeException {

    public ScanNotFoundException(Long id) {
        super(
                "A análise com o identificador "
                        + id
                        + " não foi encontrada."
        );
    }
}