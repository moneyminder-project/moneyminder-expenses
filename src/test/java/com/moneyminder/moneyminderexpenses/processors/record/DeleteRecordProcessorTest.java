package com.moneyminder.moneyminderexpenses.processors.record;

import com.moneyminder.moneyminderexpenses.mappers.RecordMapper;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import com.moneyminder.moneyminderexpenses.persistence.repositories.RecordRepository;
import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeleteRecordProcessorTest {

    private MockedStatic<AuthUtils> mockedStatic;

    @Mock
    private RecordRepository recordRepository;

    @Mock
    private RecordMapper recordMapper;

    @InjectMocks
    private DeleteRecordProcessor deleteRecordProcessor;

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
    @DisplayName("delete record test")
    void deleteRecordTest() {
        String id = UUID.randomUUID().toString();
        RecordEntity entity = new RecordEntity();
        entity.setOwner("user");

        when(recordRepository.findById(id)).thenReturn(Optional.of(entity));
        when(recordRepository.existsById(id)).thenReturn(true);

        deleteRecordProcessor.deleteRecord(id);

        verify(recordRepository).deleteById(id);
    }

    @Test
    void deleteRecordListTest() {
        DeleteRecordProcessor spy = spy(deleteRecordProcessor);
        doNothing().when(spy).deleteRecord(anyString());

        List<String> ids = List.of("1", "2");

        spy.deleteRecordList(ids);

        verify(spy, times(2)).deleteRecord(anyString());
    }

}