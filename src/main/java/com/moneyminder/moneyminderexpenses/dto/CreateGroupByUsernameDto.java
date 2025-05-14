package com.moneyminder.moneyminderexpenses.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateGroupByUsernameDto {

    @NotBlank(message = "Se debe indicar el nombre de usuario para crear el grupo")
    private String username;
    @NotBlank(message = "Se debe indicar el nombre del grupo a crear")
    private String groupName;

}
