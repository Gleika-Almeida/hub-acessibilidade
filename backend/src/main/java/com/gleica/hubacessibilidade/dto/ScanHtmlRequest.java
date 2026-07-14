package com.gleica.hubacessibilidade.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ScanHtmlRequest(

        @NotBlank(message = "O código HTML não pode estar vazio.")
        @Size(
                max = 500_000,
                message = "O código HTML ultrapassou o limite permitido."
        )
        String html

){
}