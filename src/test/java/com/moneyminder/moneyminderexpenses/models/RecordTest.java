package com.moneyminder.moneyminderexpenses.models;

import com.moneyminder.moneyminderexpenses.utils.RecordType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecordTest {

    private static Validator validator;

    @Test
    @DisplayName("Getters test with Builder")
    void testGettersWithBuilder() {
        Record record = Record.builder()
                .id("record1")
                .type(RecordType.EXPENSE)
                .name("record1")
                .money(150.0)
                .date(LocalDate.of(2025, 5, 1))
                .comment("record comment")
                .owner("user123")
                .details(List.of("detail1", "detail2"))
                .budgets(List.of("budget1"))
                .build();

        assertEquals("record1", record.getId());
        assertEquals(RecordType.EXPENSE, record.getType());
        assertEquals("record1", record.getName());
        assertEquals(150.0, record.getMoney());
        assertEquals(LocalDate.of(2025, 5, 1), record.getDate());
        assertEquals("record comment", record.getComment());
        assertEquals("user123", record.getOwner());
        assertEquals(2, record.getDetails().size());
        assertEquals(1, record.getBudgets().size());
    }

    @Test
    @DisplayName("Setters test with NoArgsConstructor")
    void testSettersWithNoArgsConstructor() {
        Record record = new Record();

        record.setId("record2");
        record.setType(RecordType.INCOME);
        record.setName("income");
        record.setMoney(3000.0);
        record.setDate(LocalDate.of(2025, 5, 5));
        record.setComment("income comment");
        record.setOwner("user456");
        record.setDetails(List.of("detail3"));
        record.setBudgets(List.of("budget2"));

        assertEquals("record2", record.getId());
        assertEquals(RecordType.INCOME, record.getType());
        assertEquals("income", record.getName());
        assertEquals(3000.0, record.getMoney());
        assertEquals(LocalDate.of(2025, 5, 5), record.getDate());
        assertEquals("income comment", record.getComment());
        assertEquals("user456", record.getOwner());
        assertEquals(1, record.getDetails().size());
        assertEquals(1, record.getBudgets().size());
    }

    @Test
    @DisplayName("Record type null test")
    void testValidationFailWhenTypeIsNull() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Record record = Record.builder()
                .id("record3")
                .type(null)
                .name("Food")
                .money(50.0)
                .date(LocalDate.now())
                .owner("user789")
                .build();

        Set<ConstraintViolation<Record>> violations = validator.validate(record);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("El registro debe tener un tipo (ingreso o gasto)")));
    }

    @Test
    @DisplayName("Record name blank test")
    void testValidationFailWhenNameIsBlank() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Record record = Record.builder()
                .id("record4")
                .type(RecordType.EXPENSE)
                .name(" ")
                .money(80.0)
                .date(LocalDate.now())
                .owner("user987")
                .build();

        Set<ConstraintViolation<Record>> violations = validator.validate(record);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("El registro debe tener un nombre")));
    }

    @Test
    @DisplayName("Record money not positive test")
    void testValidationFailWhenMoneyNotPositive() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Record record = Record.builder()
                .id("record5")
                .type(RecordType.INCOME)
                .name("record")
                .money(0.0)
                .date(LocalDate.now())
                .owner("user654")
                .build();

        Set<ConstraintViolation<Record>> violations = validator.validate(record);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("El registro debe tener un importe positivo")));
    }

    @Test
    @DisplayName("Record date null test")
    void testValidationFailWhenDateIsNull() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Record record = Record.builder()
                .id("record6")
                .type(RecordType.EXPENSE)
                .name("record")
                .money(800.0)
                .date(null)
                .owner("user321")
                .build();

        Set<ConstraintViolation<Record>> violations = validator.validate(record);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("El registro debe tener una fecha")));
    }

    @Test
    @DisplayName("Record owner blank test")
    void testValidationFailWhenOwnerIsBlank() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Record record = Record.builder()
                .id("record7")
                .type(RecordType.EXPENSE)
                .name("record")
                .money(10.0)
                .date(LocalDate.now())
                .owner(" ")
                .build();

        Set<ConstraintViolation<Record>> violations = validator.validate(record);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("El registro debe estar asociado a un usuario")));
    }

    @Test
    @DisplayName("All mandatory fields not blank or positive test")
    void testValidationPassWhenAllFieldsAreValid() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Record record = Record.builder()
                .id("record8")
                .type(RecordType.INCOME)
                .name("record")
                .money(500.0)
                .date(LocalDate.now())
                .comment("record comment")
                .owner("user999")
                .details(List.of("detailX"))
                .budgets(List.of("budgetX"))
                .build();

        Set<ConstraintViolation<Record>> violations = validator.validate(record);

        assertTrue(violations.isEmpty());
    }

}