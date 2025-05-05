package com.moneyminder.moneyminderexpenses.processors.record;

import com.moneyminder.moneyminderexpenses.mappers.BudgetMapper;
import com.moneyminder.moneyminderexpenses.mappers.RecordMapper;
import com.moneyminder.moneyminderexpenses.models.Budget;
import com.moneyminder.moneyminderexpenses.persistence.entities.BudgetEntity;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import com.moneyminder.moneyminderexpenses.models.Record;
import com.moneyminder.moneyminderexpenses.persistence.repositories.BudgetRepository;
import com.moneyminder.moneyminderexpenses.persistence.repositories.RecordRepository;
import com.moneyminder.moneyminderexpenses.processors.budget.SaveBudgetProcessor;
import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import com.moneyminder.moneyminderexpenses.utils.RecordType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SaveRecordProcessorTest {
    @Mock
    private RecordRepository recordRepository;

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private RecordMapper recordMapper;

    @Mock private
    SaveBudgetProcessor saveBudgetProcessor;

    @Mock
    private BudgetMapper budgetMapper;

    @InjectMocks
    private SaveRecordProcessor saveRecordProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("save record successfully")
    void saveRecordTest() {
        Record record = Record.builder()
                .type(RecordType.EXPENSE)
                .name("Test")
                .money(10.0)
                .date(LocalDate.now())
                .budgets(List.of())
                .build();

        RecordEntity entity = new RecordEntity();
        entity.setBudgets(new ArrayList<>());

        when(recordMapper.toEntity(record)).thenReturn(entity);
        when(recordRepository.save(entity)).thenReturn(entity);
        when(recordMapper.toModel(entity)).thenReturn(new Record());

        Record result = saveRecordProcessor.saveRecord(record);

        assertNotNull(result);
        verify(recordRepository).save(entity);
    }


    @Test
    @DisplayName("save record not null id test")
    void saveRecordThrowsWhenIdIsNotNullTest() {
        Record record = Record.builder().id("not-null").build();
        var ex = assertThrows(IllegalArgumentException.class, () -> saveRecordProcessor.saveRecord(record));
        assertEquals("Record id must be null", ex.getMessage());
    }

    @Test
    @DisplayName("save record no attributes error test")
    void saveRecordThrowsWhenRecordIsInvalidTest() {
        Record invalidRecord = Record.builder().build();
        var ex = assertThrows(IllegalArgumentException.class, () -> saveRecordProcessor.saveRecord(invalidRecord));
        assertEquals("Record type must not be null", ex.getMessage());
    }

    @Test
    @DisplayName("save record list test")
    void saveRecordListTest() {
        SaveRecordProcessor spy = spy(saveRecordProcessor);
        doReturn(new Record()).when(spy).saveRecord(any());

        List<Record> result = spy.saveRecordList(List.of(new Record(), new Record()));
        assertEquals(2, result.size());
        verify(spy, times(2)).saveRecord(any());
    }

    @Test
    @DisplayName("update record test")
    void updateRecordTest() {
        String id = UUID.randomUUID().toString();
        Record record = Record.builder()
                .id(id)
                .type(RecordType.INCOME)
                .name("Salary")
                .money(1000.0)
                .date(LocalDate.now())
                .budgets(List.of("budget1"))
                .build();

        RecordEntity dbEntity = new RecordEntity();
        dbEntity.setOwner("user");
        BudgetEntity budgetEntity = new BudgetEntity();
        budgetEntity.setId("budget1");

        RecordEntity toSaveEntity = new RecordEntity();
        toSaveEntity.setBudgets(List.of(budgetEntity));

        when(recordRepository.findById(id)).thenReturn(Optional.of(dbEntity));
        mockStatic(AuthUtils.class);
        when(AuthUtils.getUsername()).thenReturn("user");
        when(recordMapper.toEntity(record)).thenReturn(toSaveEntity);
        when(budgetRepository.findAllByIdIn(record.getBudgets())).thenReturn(List.of(budgetEntity));
        when(recordRepository.save(toSaveEntity)).thenReturn(toSaveEntity);
        when(recordMapper.toModel(toSaveEntity)).thenReturn(record);
        when(budgetMapper.toModel(budgetEntity)).thenReturn(new Budget());

        Record result = saveRecordProcessor.updateRecord(id, record);

        assertNotNull(result);
        verify(recordRepository).save(toSaveEntity);
        verify(saveBudgetProcessor).updateBudget(eq("budget1"), any(Budget.class));
    }

    @Test
    @DisplayName("update record no id test")
    void updateRecordIdMismatchThrowsTest() {
        Record record = Record.builder().id("different").build();
        var ex = assertThrows(IllegalArgumentException.class, () -> saveRecordProcessor.updateRecord("original", record));
        assertEquals("Records don't match", ex.getMessage());
    }

    @Test
    @DisplayName("save record record no params error test")
    void updateRecordListTest() {
        Record r1 = Record.builder().id("1").type(RecordType.EXPENSE).name("r1").money(1.0).date(LocalDate.now()).build();
        Record r2 = Record.builder().id("2").type(RecordType.INCOME).name("r2").money(2.0).date(LocalDate.now()).build();

        SaveRecordProcessor spy = spy(saveRecordProcessor);
        doReturn(new Record()).when(spy).updateRecord(anyString(), any());

        List<Record> result = spy.updateRecordList(List.of(r1, r2));
        assertEquals(2, result.size());
        verify(spy, times(2)).updateRecord(anyString(), any());
    }


}