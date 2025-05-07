package com.moneyminder.moneyminderexpenses.mappers;

import com.moneyminder.moneyminderexpenses.models.Budget;
import com.moneyminder.moneyminderexpenses.persistence.entities.BudgetEntity;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import org.junit.jupiter.api.*;
import org.mapstruct.factory.Mappers;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BudgetMapperTest {

    private final BudgetMapper mapper = Mappers.getMapper(BudgetMapper.class);
    private MockedStatic<AuthUtils> mockedStatic;

    @BeforeEach
    void beforeEach() {
        mockedStatic = mockStatic(AuthUtils.class);
        mockedStatic.when(AuthUtils::getUsername).thenReturn("currentUser");
    }

    @AfterEach
    void afterEach() {
        if (mockedStatic != null) {
            mockedStatic.close();
        }
    }

    @Test
    @DisplayName("Entity to Model mapping test")
    void testEntityToModel() {
        RecordEntity record = new RecordEntity();
        record.setId("record1");

        BudgetEntity entity = new BudgetEntity();
        entity.setId("budget-entity-1");
        entity.setName("Test Budget");
        entity.setComment("Comment for budget");
        entity.setStartDate(LocalDate.of(2025, 1, 1));
        entity.setEndDate(LocalDate.of(2025, 12, 31));
        entity.setExpensesLimit(5000.0);
        entity.setTotalExpenses(1000.0);
        entity.setTotalIncomes(2000.0);
        entity.setGroupId("group1");
        entity.setUsernames(List.of("currentUser"));
        entity.setRecords(List.of(record));

        Budget model = mapper.toModel(entity);

        assertEquals("budget-entity-1", model.getId());
        assertEquals("Test Budget", model.getName());
        assertEquals("Comment for budget", model.getComment());
        assertEquals(LocalDate.of(2025, 1, 1), model.getStartDate());
        assertEquals(LocalDate.of(2025, 12, 31), model.getEndDate());
        assertEquals(5000.0, model.getExpensesLimit());
        assertEquals(1000.0, model.getTotalExpenses());
        assertEquals(2000.0, model.getTotalIncomes());
        assertEquals("group1", model.getGroupId());
        assertTrue(model.getFavorite());
        assertEquals(List.of("record1"), model.getRecords());
    }

    @Test
    @DisplayName("Model to Entity mapping test")
    void testModelToEntity() {
        Budget model = Budget.builder()
                .id("budget-model-1")
                .name("Another Budget")
                .comment("Another comment")
                .startDate(LocalDate.of(2025, 2, 1))
                .endDate(LocalDate.of(2025, 11, 30))
                .expensesLimit(8000.0)
                .totalExpenses(1500.0)
                .totalIncomes(3000.0)
                .groupId("group2")
                .favorite(true)
                .records(List.of("record2"))
                .build();

        BudgetEntity entity = mapper.toEntity(model);

        assertEquals("budget-model-1", entity.getId());
        assertEquals("Another Budget", entity.getName());
        assertEquals("Another comment", entity.getComment());
        assertEquals(LocalDate.of(2025, 2, 1), entity.getStartDate());
        assertEquals(LocalDate.of(2025, 11, 30), entity.getEndDate());
        assertEquals(8000.0, entity.getExpensesLimit());
        assertEquals(1500.0, entity.getTotalExpenses());
        assertEquals(3000.0, entity.getTotalIncomes());
        assertEquals("group2", entity.getGroupId());
        assertEquals(List.of("currentUser"), entity.getUsernames());
        assertEquals(1, entity.getRecords().size());
        assertEquals("record2", entity.getRecords().get(0).getId());
    }

    @Test
    @DisplayName("Map Records to UUIDs")
    void testMapRecordsToUUIDs() {
        RecordEntity record1 = new RecordEntity();
        record1.setId("rec1");

        RecordEntity record2 = new RecordEntity();
        record2.setId("rec2");

        List<String> ids = mapper.mapRecordsToUUIDs(List.of(record1, record2));

        assertEquals(List.of("rec1", "rec2"), ids);
    }

    @Test
    @DisplayName("Map UUIDs to Records")
    void testMapUUIDsToRecords() {
        List<RecordEntity> records = mapper.mapUUIDsToRecords(List.of("id1", "id2"));

        assertEquals(2, records.size());
        assertEquals("id1", records.get(0).getId());
        assertEquals("id2", records.get(1).getId());
    }

}