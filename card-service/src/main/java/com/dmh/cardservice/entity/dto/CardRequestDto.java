package com.dmh.cardservice.entity.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CardRequestDto {
    @NotNull(message = "El código es obligatorio")
    @Positive(message = "El código debe ser positivo")
    private Integer cod;

    @NotNull(message = "La fecha de expiración es obligatoria")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/(20\\d{2})$", message = "La fecha de expiración debe tener el formato MM/YYYY")
    private String expirationDate;

    @NotNull(message = "El nombre completo es obligatorio")
    private String firstLastName;

    @NotNull(message = "El número de identificación es obligatorio")
    @Positive(message = "El número de identificación debe ser positivo")
    private Long numberId;
}
