package com.moneyminder.moneyminderexpenses.processors.record;

import com.moneyminder.moneyminderexpenses.mappers.RecordMapper;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import com.moneyminder.moneyminderexpenses.persistence.repositories.RecordRepository;
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

import static org.mockito.Mockito.*;

class DeleteRecordProcessorTest {

    @Mock
    private RecordRepository recordRepository;

    @Mock
    private RecordMapper recordMapper;

    @InjectMocks
    private DeleteRecordProcessor deleteRecordProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("delete record test")
    void deleteRecordTest() {
        String id = UUID.randomUUID().toString();
        RecordEntity entity = new RecordEntity();
        entity.setOwner("user");

        when(recordRepository.findById(id)).thenReturn(Optional.of(entity));
        when(recordRepository.existsById(id)).thenReturn(true);
        mockStatic(AuthUtils.class);
        when(AuthUtils.getUsername()).thenReturn("user");

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