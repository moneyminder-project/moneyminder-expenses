package com.moneyminder.moneyminderexpenses.processors.detail;

import com.moneyminder.moneyminderexpenses.mappers.DetailMapper;
import com.moneyminder.moneyminderexpenses.persistence.entities.DetailEntity;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import com.moneyminder.moneyminderexpenses.persistence.repositories.DetailRepository;
import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeleteDetailProcessorTest {

    private MockedStatic<AuthUtils> mockedStatic;

    @Mock
    private DetailRepository detailRepository;

    @Mock
    private DetailMapper detailMapper;

    @InjectMocks
    private DeleteDetailProcessor deleteDetailProcessor;

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
    @DisplayName("delete detail test")
    void deleteDetailTest() {
        String id = UUID.randomUUID().toString();
        DetailEntity entity = new DetailEntity();
        RecordEntity recordEntity = new RecordEntity();
        recordEntity.setOwner("user");
        entity.setRecord(recordEntity);

        when(detailRepository.findById(id)).thenReturn(Optional.of(entity));
        when(detailRepository.existsById(id)).thenReturn(true);

        deleteDetailProcessor.deleteDetail(id);

        verify(detailRepository).deleteById(id);
    }

    @Test
    @DisplayName("delete detail not found test")
    void deleteDetailNotFoundTest() {
        String id = UUID.randomUUID().toString();
        when(detailRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> deleteDetailProcessor.deleteDetail(id));

        assertEquals("Detail not found", exception.getMessage());
    }

    @Test
    @DisplayName("delete detail list test")
    void deleteDetailListTest() {
        DeleteDetailProcessor spyProcessor = spy(deleteDetailProcessor);
        doNothing().when(spyProcessor).deleteDetail(anyString());

        List<String> ids = List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        spyProcessor.deleteDetailList(ids);

        verify(spyProcessor, times(2)).deleteDetail(anyString());
    }

}