package com.moneyminder.moneyminderexpenses.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CreateGroupByUsernameDtoTest {

    private static Validator validator;

    @Test
    @DisplayName("Getters test with Builder")
    void testGettersWithBuilder() {
        CreateGroupByUsernameDto dto = CreateGroupByUsernameDto.builder()
                .username("testUser")
                .groupName("Test Group")
                .build();

        assertEquals("testUser", dto.getUsername());
        assertEquals("Test Group", dto.getGroupName());
    }

    @Test
    @DisplayName("Setters test with NoArgsConstructor")
    void testSettersWithNoArgsConstructor() {
        CreateGroupByUsernameDto dto = new CreateGroupByUsernameDto();

        dto.setUsername("setterUser");
        dto.setGroupName("Setter Group");

        assertEquals("setterUser", dto.getUsername());
        assertEquals("Setter Group", dto.getGroupName());
    }

    @Test
    @DisplayName("Create group username blank test")
    void testValidationFailWhenUsernameIsBlank() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        CreateGroupByUsernameDto dto = CreateGroupByUsernameDto.builder()
                .username(" ")
                .groupName("Group A")
                .build();

        Set<ConstraintViolation<CreateGroupByUsernameDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Se debe indicar el nombre de usuario para crear el grupo")));
    }

    @Test
    @DisplayName("Create group groupName blank test")
    void testValidationFailWhenGroupNameIsBlank() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        CreateGroupByUsernameDto dto = CreateGroupByUsernameDto.builder()
                .username("validUser")
                .groupName(" ")
                .build();

        Set<ConstraintViolation<CreateGroupByUsernameDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Se debe indicar el nombre del grupo a crear")));
    }

    @Test
    @DisplayName("All mandatory fields not blank test")
    void testValidationPassWhenAllFieldsAreValid() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        CreateGroupByUsernameDto dto = CreateGroupByUsernameDto.builder()
                .username("validUser")
                .groupName("Valid Group")
                .build();

        Set<ConstraintViolation<CreateGroupByUsernameDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

}