package com.moneyminder.moneyminderexpenses.mappers;

import com.moneyminder.moneyminderexpenses.models.Record;
import com.moneyminder.moneyminderexpenses.persistence.entities.BudgetEntity;
import com.moneyminder.moneyminderexpenses.persistence.entities.DetailEntity;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import com.moneyminder.moneyminderexpenses.utils.RecordType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

class RecordMapperTest {

    private final RecordMapper mapper = Mappers.getMapper(RecordMapper.class);

    @Test
    @DisplayName("Entity to Model mapping test")
    void testEntityToModel() {
        DetailEntity detail = new DetailEntity();
        detail.setId("detail-uuid-123");

        BudgetEntity budget = new BudgetEntity();
        budget.setId("budget-uuid-123");

        RecordEntity entity = new RecordEntity();
        entity.setId("record-uuid-123");
        entity.setType(RecordType.EXPENSE);
        entity.setName("name");
        entity.setMoney(150.0);
        entity.setDate(LocalDate.of(2025, 5, 1));
        entity.setComment("comment");
        entity.setOwner("user123");
        entity.setDetails(List.of(detail));
        entity.setBudgets(List.of(budget));

        Record model = mapper.toModel(entity);

        assertEquals("record-uuid-123", model.getId());
        assertEquals(RecordType.EXPENSE, model.getType());
        assertEquals("name", model.getName());
        assertEquals(150.0, model.getMoney());
        assertEquals(LocalDate.of(2025, 5, 1), model.getDate());
        assertEquals("comment", model.getComment());
        assertEquals("user123", model.getOwner());
        assertEquals(List.of("detail-uuid-123"), model.getDetails());
        assertEquals(List.of("budget-uuid-123"), model.getBudgets());
    }

    @Test
    @DisplayName("Model to Entity mapping test")
    void testModelToEntity() {
        Record model = Record.builder()
                .id("record-uuid-456")
                .type(RecordType.INCOME)
                .name("name")
                .money(3000.0)
                .date(LocalDate.of(2025, 5, 5))
                .comment("comment")
                .owner("user456")
                .details(List.of("detail-uuid-456"))
                .budgets(List.of("budget-uuid-456"))
                .build();

        RecordEntity entity = mapper.toEntity(model);

        assertEquals("record-uuid-456", entity.getId());
        assertEquals(RecordType.INCOME, entity.getType());
        assertEquals("name", entity.getName());
        assertEquals(3000.0, entity.getMoney());
        assertEquals(LocalDate.of(2025, 5, 5), entity.getDate());
        assertEquals("comment", entity.getComment());
        assertEquals("user456", entity.getOwner());
        assertEquals(1, entity.getDetails().size());
        assertEquals("detail-uuid-456", entity.getDetails().get(0).getId());
        assertEquals(1, entity.getBudgets().size());
        assertEquals("budget-uuid-456", entity.getBudgets().get(0).getId());
    }

    @Test
    @DisplayName("Map Details to UUIDs")
    void testMapDetailsToUUIDs() {
        DetailEntity detail1 = new DetailEntity();
        detail1.setId("detail1");
        DetailEntity detail2 = new DetailEntity();
        detail2.setId("detail2");

        List<String> ids = mapper.mapDetailsToUUIDs(List.of(detail1, detail2));

        assertEquals(List.of("detail1", "detail2"), ids);
    }

    @Test
    @DisplayName("Map Budgets to UUIDs")
    void testMapBudgetsToUUIDs() {
        BudgetEntity budget1 = new BudgetEntity();
        budget1.setId("budget1");
        BudgetEntity budget2 = new BudgetEntity();
        budget2.setId("budget2");

        List<String> ids = mapper.mapBudgetsToUUIDs(List.of(budget1, budget2));

        assertEquals(List.of("budget1", "budget2"), ids);
    }

    @Test
    @DisplayName("Map UUIDs to Details")
    void testMapUUIDsToDetails() {
        List<DetailEntity> details = mapper.mapUUIDsToDetails(List.of("id1", "id2"));

        assertEquals(2, details.size());
        assertEquals("id1", details.get(0).getId());
        assertEquals("id2", details.get(1).getId());
    }

    @Test
    @DisplayName("Map UUIDs to Budgets")
    void testMapUUIDsToBudgets() {
        List<BudgetEntity> budgets = mapper.mapUUIDsToBudgets(List.of("id3", "id4"));

        assertEquals(2, budgets.size());
        assertEquals("id3", budgets.get(0).getId());
        assertEquals("id4", budgets.get(1).getId());
    }

}