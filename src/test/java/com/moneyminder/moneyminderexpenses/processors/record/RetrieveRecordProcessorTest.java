package com.moneyminder.moneyminderexpenses.processors.record;

import com.moneyminder.moneyminderexpenses.dto.RecordWithoutDetails;
import com.moneyminder.moneyminderexpenses.mappers.RecordMapper;
import com.moneyminder.moneyminderexpenses.persistence.entities.BudgetEntity;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import com.moneyminder.moneyminderexpenses.models.Record;
import com.moneyminder.moneyminderexpenses.persistence.repositories.BudgetRepository;
import com.moneyminder.moneyminderexpenses.persistence.repositories.RecordRepository;
import com.moneyminder.moneyminderexpenses.requestParams.RecordRequestParams;
import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import com.moneyminder.moneyminderexpenses.utils.RecordType;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RetrieveRecordProcessorTest {

    private MockedStatic<AuthUtils> mockedStatic;

    @Mock
    private RecordRepository recordRepository;

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private RecordMapper recordMapper;

    @InjectMocks
    private RetrieveRecordProcessor retrieveRecordProcessor;

    @BeforeAll
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void beforeEach() {
        mockedStatic = mockStatic(AuthUtils.class);
        mockedStatic.when(AuthUtils::getUsername).thenReturn("user");
    }

    @AfterEach
    void afterEach() {
        if (mockedStatic != null) {
            mockedStatic.close();
        }
    }

    @Test
    @DisplayName("retrieve records test")
    void retrieveRecordsTest() {
        RecordRequestParams params = new RecordRequestParams();
        String username = "user";

        List<RecordEntity> entities = List.of(new RecordEntity());
        List<Record> models = List.of(new Record());

        when(recordRepository.findAll(any(Specification.class))).thenReturn(entities);
        when(recordMapper.toModelList(entities)).thenReturn(models);

        List<Record> result = retrieveRecordProcessor.retrieveRecords(params, username);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(recordRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("retrieve record by id test")
    void retrieveRecordByIdTest() {
        String id = UUID.randomUUID().toString();
        RecordEntity entity = new RecordEntity();
        entity.setOwner("user");

        when(recordRepository.findById(id)).thenReturn(Optional.of(entity));
        when(recordMapper.toModel(any())).thenReturn(new Record());

        Record result = retrieveRecordProcessor.retrieveRecordById(id);

        assertNotNull(result);
        verify(recordRepository, times(2)).findById(id);
    }

    @Test
    @DisplayName("retrieve records by budget id test")
    void retrieveRecordsByBudgetIdTest() {
        String budgetId = UUID.randomUUID().toString();
        RecordEntity recordEntity = new RecordEntity();
        recordEntity.setId("1");
        recordEntity.setName("Test");
        recordEntity.setMoney(100.0);
        recordEntity.setType(RecordType.INCOME);
        recordEntity.setDate(LocalDate.now());
        recordEntity.setOwner("user");

        when(recordRepository.findAllByBudgets_Id(budgetId)).thenReturn(List.of(recordEntity));

        List<RecordWithoutDetails> result = retrieveRecordProcessor.retrieveRecordByBudgetId(budgetId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test", result.get(0).getName());
    }

}