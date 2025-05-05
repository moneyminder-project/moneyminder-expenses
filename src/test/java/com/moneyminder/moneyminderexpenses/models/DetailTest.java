package com.moneyminder.moneyminderexpenses.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DetailTest {

    private static Validator validator;

    @Test
    @DisplayName("Getters test with Builder")
    void testGettersWithBuilder() {
        Detail detail = Detail.builder()
                .id("detail1")
                .name("detail")
                .pricePerUnit(10.5)
                .units(5)
                .totalPrice(52.5)
                .record("recordId123")
                .build();

        assertEquals("detail1", detail.getId());
        assertEquals("detail", detail.getName());
        assertEquals(10.5, detail.getPricePerUnit());
        assertEquals(5, detail.getUnits());
        assertEquals(52.5, detail.getTotalPrice());
        assertEquals("recordId123", detail.getRecord());
    }

    @Test
    @DisplayName("Setters test with NoArgsConstructor")
    void testSettersWithNoArgsConstructor() {
        Detail detail = new Detail();

        detail.setId("detail2");
        detail.setName("detail");
        detail.setPricePerUnit(15.0);
        detail.setUnits(3);
        detail.setTotalPrice(45.0);
        detail.setRecord("recordId");

        assertEquals("detail2", detail.getId());
        assertEquals("detail", detail.getName());
        assertEquals(15.0, detail.getPricePerUnit());
        assertEquals(3, detail.getUnits());
        assertEquals(45.0, detail.getTotalPrice());
        assertEquals("recordId", detail.getRecord());
    }

    @Test
    @DisplayName("Detail name blank test")
    void testValidationFailWhenNameIsBlank() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Detail detail = Detail.builder()
                .id("detail3")
                .name(" ")
                .pricePerUnit(5.0)
                .units(2)
                .totalPrice(10.0)
                .record("recordId")
                .build();

        Set<ConstraintViolation<Detail>> violations = validator.validate(detail);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("El detalle debe tener un nombre")));
    }

    @Test
    @DisplayName("Detail price null test")
    void testValidationFailWhenPriceIsNull() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Detail detail = Detail.builder()
                .id("detail4")
                .name("detail")
                .pricePerUnit(null)
                .units(1)
                .totalPrice(5.0)
                .record("recordId")
                .build();

        Set<ConstraintViolation<Detail>> violations = validator.validate(detail);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("El detalle debe tener un precio por unidad")));
    }

    @Test
    @DisplayName("Detail units negative test")
    void testValidationFailWhenUnitsNegative() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Detail detail = Detail.builder()
                .id("detail5")
                .name("detail")
                .pricePerUnit(20.0)
                .units(-3)
                .totalPrice(60.0)
                .record("recordId")
                .build();

        Set<ConstraintViolation<Detail>> violations = validator.validate(detail);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("El número de unidades debe ser un número positivo")));
    }

    @Test
    @DisplayName("Detail record blank test")
    void testValidationFailWhenRecordIsBlank() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Detail detail = Detail.builder()
                .id("detail6")
                .name("detail")
                .pricePerUnit(30.0)
                .units(2)
                .totalPrice(60.0)
                .record(" ")
                .build();

        Set<ConstraintViolation<Detail>> violations = validator.validate(detail);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("El detalle debe estar asociado a un ingreso o gasto")));
    }

    @Test
    @DisplayName("All mandatory fields not blank or positive test")
    void testValidationPassWhenAllFieldsAreValid() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Detail detail = Detail.builder()
                .id("detail7")
                .name("detail")
                .pricePerUnit(12.0)
                .units(1)
                .totalPrice(12.0)
                .record("recordValid")
                .build();

        Set<ConstraintViolation<Detail>> violations = validator.validate(detail);

        assertTrue(violations.isEmpty());
    }

}