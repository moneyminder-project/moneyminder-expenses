package com.moneyminder.moneyminderexpenses.persistence.repositories;

import com.moneyminder.moneyminderexpenses.persistence.entities.DetailEntity;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import com.moneyminder.moneyminderexpenses.utils.RecordType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class DetailRepositoryTest {

    @Autowired
    private DetailRepository detailRepository;

    @Autowired
    private RecordRepository recordRepository;

    private RecordEntity record;

    private DetailEntity detail;

    @BeforeEach
    void setup() {
        record = new RecordEntity();
        record.setType(RecordType.EXPENSE);
        record.setName("Test Record");
        record.setDate(LocalDate.now());
        record.setMoney(100.00);
        record.setOwner("owner");
        record = recordRepository.save(record);

        detail = new DetailEntity();
        detail.setName("Test Detail");
        detail.setPricePerUnit(10.0);
        detail.setUnits(5);
        detail.setTotalPrice(50.0);
        detail.setRecord(record);
    }

    @Test
    @DisplayName("save and find by ID test")
    void saveAndFindById() {
        DetailEntity savedDetail = detailRepository.save(detail);

        Optional<DetailEntity> found = detailRepository.findById(savedDetail.getId());

        assertTrue(found.isPresent());
        assertEquals("Test Detail", found.get().getName());
        assertEquals(50.0, found.get().getTotalPrice());
        assertEquals(record.getId(), found.get().getRecord().getId());
    }

    @Test
    @DisplayName("existsById test")
    void existsByIdTest() {
        DetailEntity savedDetail = detailRepository.save(detail);

        boolean exists = detailRepository.existsById(savedDetail.getId());

        assertTrue(exists);
    }

    @Test
    @DisplayName("findAllByIdIn test")
    void findAllByIdInTest() {
        DetailEntity savedDetail1 = detailRepository.save(detail);

        DetailEntity detail2 = new DetailEntity();
        detail2.setName("Second Detail");
        detail2.setPricePerUnit(20.0);
        detail2.setUnits(2);
        detail2.setTotalPrice(40.0);
        detail2.setRecord(record);

        DetailEntity savedDetail2 = detailRepository.save(detail2);

        List<String> ids = List.of(savedDetail1.getId(), savedDetail2.getId());

        List<DetailEntity> details = detailRepository.findAllByIdIn(ids);

        assertEquals(2, details.size());
    }

}