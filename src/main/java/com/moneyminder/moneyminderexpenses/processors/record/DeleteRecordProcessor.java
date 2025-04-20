package com.moneyminder.moneyminderexpenses.processors.record;

import com.moneyminder.moneyminderexpenses.mappers.RecordMapper;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import com.moneyminder.moneyminderexpenses.persistence.repositories.RecordRepository;
import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteRecordProcessor {
    private final RecordRepository recordRepository;
    private final RecordMapper recordMapper;

    public void deleteRecord(String id) {
        final RecordEntity recordEntity = this.recordRepository.findById(id).orElse(new RecordEntity());
        Assert.isTrue(recordEntity.getOwner().equals(AuthUtils.getUsername()), "Owner is not the owner of the record");
        Assert.isTrue(this.recordRepository.existsById(id), "Record does not exist");
        this.recordRepository.deleteById(id);
    }

    public void deleteRecordList(List<String> ids) {
        ids.forEach(this::deleteRecord);
    }
}
