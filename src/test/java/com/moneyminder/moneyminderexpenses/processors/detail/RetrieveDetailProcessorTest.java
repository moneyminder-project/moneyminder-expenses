package com.moneyminder.moneyminderexpenses.processors.detail;

import com.moneyminder.moneyminderexpenses.mappers.DetailMapper;
import com.moneyminder.moneyminderexpenses.models.Detail;
import com.moneyminder.moneyminderexpenses.persistence.entities.DetailEntity;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import com.moneyminder.moneyminderexpenses.persistence.repositories.DetailRepository;
import com.moneyminder.moneyminderexpenses.requestParams.DetailRequestParams;
import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RetrieveDetailProcessorTest {

    private MockedStatic<AuthUtils> mockedStatic;

    @Mock
    private DetailRepository detailRepository;

    @Mock
    private DetailMapper detailMapper;

    @InjectMocks
    private RetrieveDetailProcessor retrieveDetailProcessor;

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
    @DisplayName("retrieve details test")
    void retrieveDetailsTest() {
        DetailRequestParams params = new DetailRequestParams();
        List<DetailEntity> entities = List.of(new DetailEntity());
        List<Detail> details = List.of(new Detail());

        when(detailRepository.findAll(any(Specification.class))).thenReturn(entities);
        when(detailMapper.toModelList(entities)).thenReturn(details);

        List<Detail> result = retrieveDetailProcessor.retrieveDetails(params);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(detailRepository).findAll(any(Specification.class));
        verify(detailMapper).toModelList(entities);
    }

    @Test
    @DisplayName("retrieve detail by id test")
    void retrieveDetailByIdTest() {
        String id = UUID.randomUUID().toString();
        DetailEntity entity = new DetailEntity();
        RecordEntity recordEntity = new RecordEntity();
        recordEntity.setOwner("user");
        entity.setRecord(recordEntity);

        when(detailRepository.findById(id)).thenReturn(Optional.of(entity));

        Detail detail = new Detail();
        when(detailMapper.toModel(any(DetailEntity.class))).thenReturn(detail);

        Detail result = retrieveDetailProcessor.retrieveDetailById(id);

        assertNotNull(result);
        verify(detailRepository, times(2)).findById(id);
        verify(detailMapper).toModel(any(DetailEntity.class));
    }

    @Test
    @DisplayName("retrieve detail by id not found test")
    void retrieveDetailByIdNotFoundTest() {
        String id = UUID.randomUUID().toString();
        when(detailRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> retrieveDetailProcessor.retrieveDetailById(id));
        assertEquals("Detail not found", exception.getMessage());

        verify(detailRepository).findById(id);
    }

}