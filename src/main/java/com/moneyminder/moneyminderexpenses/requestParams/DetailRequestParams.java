package com.moneyminder.moneyminderexpenses.requestParams;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailRequestParams {
    @Schema(description = "Nombre del detalle para filtrar", example = "Pan integral")
    private String name;

    @Schema(description = "Lista de IDs de detalles a incluir en la búsqueda", example = "")
    private List<String> ids;
}
