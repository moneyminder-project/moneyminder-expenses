package com.moneyminder.moneyminderexpenses.processors.detail;

import com.moneyminder.moneyminderexpenses.mappers.DetailMapper;
import com.moneyminder.moneyminderexpenses.models.Detail;
import com.moneyminder.moneyminderexpenses.persistence.entities.DetailEntity;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import com.moneyminder.moneyminderexpenses.persistence.repositories.DetailRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaveDetailProcessorTest {

    @Mock
    private DetailRepository detailRepository;

    @Mock
    private RecordRepository recordRepository;

    @Mock
    private DetailMapper detailMapper;

    @InjectMocks
    private SaveDetailProcessor saveDetailProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("save detail test")
    void saveDetailTest() {
        Detail detail = new Detail(null, "name", 10.0, 2, null, "recordId");
        DetailEntity entity = new DetailEntity();
        RecordEntity recordEntity = new RecordEntity();
        recordEntity.setId("recordId");

        when(recordRepository.findById("recordId")).thenReturn(Optional.of(recordEntity));
        when(detailMapper.toEntity(detail)).thenReturn(entity);
        when(detailRepository.save(entity)).thenReturn(entity);
        when(detailMapper.toModel(entity)).thenReturn(new Detail());

        Detail result = saveDetailProcessor.saveDetail(detail);

        assertNotNull(result);
        verify(detailRepository).save(entity);
    }

    @Test
    @DisplayName("save detail with id error test")
    void saveDetailWithIdShouldThrowTest() {
        Detail detail = new Detail("someId", "name", 10.0, 2, null, "recordId");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveDetailProcessor.saveDetail(detail));

        assertEquals("detail id must be null", exception.getMessage());
    }

    @Test
    @DisplayName("save detail list test")
    void saveDetailListTest() {
        SaveDetailProcessor spyProcessor = spy(saveDetailProcessor);
        doReturn(new Detail()).when(spyProcessor).saveDetail(any(Detail.class));

        List<Detail> result = spyProcessor.saveDetailList(List.of(new Detail(), new Detail()));

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(spyProcessor, times(2)).saveDetail(any(Detail.class));
    }

    @Test
    @DisplayName("update detail test")
    void updateDetailTest() {
        String id = UUID.randomUUID().toString();
        Detail detail = new Detail(id, "name", 10.0, 2, null, "recordId");

        DetailEntity existingEntity = new DetailEntity();
        RecordEntity recordEntity = new RecordEntity();
        recordEntity.setOwner("user");
        existingEntity.setRecord(recordEntity);

        when(detailRepository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(recordRepository.findById("recordId")).thenReturn(Optional.of(new RecordEntity()));
        mockStatic(AuthUtils.class);
        when(AuthUtils.getUsername()).thenReturn("user");

        DetailEntity entity = new DetailEntity();
        when(detailMapper.toEntity(detail)).thenReturn(entity);
        when(detailRepository.save(entity)).thenReturn(entity);
        when(detailMapper.toModel(entity)).thenReturn(new Detail());

        Detail result = saveDetailProcessor.updateDetail(id, detail);

        assertNotNull(result);
        verify(detailRepository).save(entity);
    }

    @Test
    @DisplayName("update detail with mismatched id test")
    void updateDetailIdMismatchTest() {
        Detail detail = new Detail("differentId", "name", 10.0, 2, null, "recordId");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveDetailProcessor.updateDetail(UUID.randomUUID().toString(), detail));

        assertEquals("Detail don't match", exception.getMessage());
    }

    @Test
    @DisplayName("update detail list test")
    void updateDetailListTest() {
        SaveDetailProcessor spyProcessor = spy(saveDetailProcessor);
        Detail detail1 = new Detail(UUID.randomUUID().toString(), "name1", 10.0, 2, null, "recordId");
        Detail detail2 = new Detail(UUID.randomUUID().toString(), "name2", 20.0, 1, null, "recordId");

        doReturn(new Detail()).when(spyProcessor).updateDetail(anyString(), any(Detail.class));

        List<Detail> result = spyProcessor.updateDetailList(List.of(detail1, detail2));

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(spyProcessor, times(2)).updateDetail(anyString(), any(Detail.class));
    }

}