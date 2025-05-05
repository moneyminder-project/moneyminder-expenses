package com.moneyminder.moneyminderexpenses.processors.detail;

import com.moneyminder.moneyminderexpenses.mappers.DetailMapper;
import com.moneyminder.moneyminderexpenses.persistence.entities.DetailEntity;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import com.moneyminder.moneyminderexpenses.persistence.repositories.DetailRepository;
import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteDetailProcessorTest {

    @Mock
    private DetailRepository detailRepository;

    @Mock
    private DetailMapper detailMapper;

    @InjectMocks
    private DeleteDetailProcessor deleteDetailProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
        mockStatic(AuthUtils.class);
        when(AuthUtils.getUsername()).thenReturn("user");

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