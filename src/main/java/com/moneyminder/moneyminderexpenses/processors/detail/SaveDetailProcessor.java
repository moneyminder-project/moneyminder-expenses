package com.moneyminder.moneyminderexpenses.processors.detail;

import com.moneyminder.moneyminderexpenses.mappers.DetailMapper;
import com.moneyminder.moneyminderexpenses.models.Detail;
import com.moneyminder.moneyminderexpenses.persistence.entities.DetailEntity;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import com.moneyminder.moneyminderexpenses.persistence.repositories.DetailRepository;
import com.moneyminder.moneyminderexpenses.persistence.repositories.RecordRepository;
import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import com.moneyminder.moneyminderexpenses.utils.MoneyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaveDetailProcessor {
    private final DetailRepository detailRepository;
    private final RecordRepository recordRepository;
    private final DetailMapper detailMapper;

    public Detail saveDetail(final Detail detail) {
        Assert.isNull(detail.getId(), "detail id must be null");
        this.checkDetailAttributes(detail);

        detail.setPricePerUnit(MoneyUtils.round(detail.getPricePerUnit()));
        detail.setTotalPrice(MoneyUtils.round(detail.getPricePerUnit() * detail.getUnits()));

        final DetailEntity detailEntity = detailMapper.toEntity(detail);

        final RecordEntity recordEntity = this.recordRepository.findById(detail.getRecord()).orElse(null);
        Assert.notNull(recordEntity, "Record not found");

        detailEntity.setRecord(recordEntity);

        return this.detailMapper.toModel(this.detailRepository.save(detailEntity));
    }

    public List<Detail> saveDetailList(final List<Detail> detailList) {
        List<Detail> savedDetailList = new ArrayList<>();

        if (detailList != null && !detailList.isEmpty()) {
            detailList.forEach(detail -> savedDetailList.add(this.saveDetail(detail)));
        }

        return savedDetailList;
    }

    public Detail updateDetail(final String id, final Detail detail) {
        Assert.isTrue(id.equals(detail.getId()), "Detail don't match");
        final DetailEntity detailFromDb = this.detailRepository.findById(id).orElse(null);
        Assert.notNull(detail, "Detail not found");
        Assert.isTrue(detailFromDb.getRecord().getOwner().equals(AuthUtils.getUsername()), "Owner is not the owner of the detail");
        this.checkDetailAttributes(detail);

        detail.setPricePerUnit(MoneyUtils.round(detail.getPricePerUnit()));
        detail.setTotalPrice(MoneyUtils.round(detail.getPricePerUnit() * detail.getUnits()));

        final DetailEntity detailEntity = detailMapper.toEntity(detail);

        final RecordEntity recordEntity = this.recordRepository.findById(detail.getRecord()).orElse(null);
        Assert.notNull(recordEntity, "Record not found");

        detailEntity.setRecord(recordEntity);

        return this.detailMapper.toModel(this.detailRepository.save(detailEntity));
    }

    public List<Detail> updateDetailList(final List<Detail> detailList) {
        List<Detail> updatedDetails = new ArrayList<>();

        if (detailList != null && !detailList.isEmpty()) {
            detailList.forEach(detail -> updatedDetails.add(this.updateDetail(detail.getId(), detail)));
        }

        return updatedDetails;
    }

    private void checkDetailAttributes(final Detail detail) {
        Assert.notNull(detail, "detail cannot be null");
        Assert.notNull(detail.getName(), "detail name must not be null");
        Assert.hasLength(detail.getName(), "detail name must not be empty");
        Assert.notNull(detail.getPricePerUnit(), "detail pricePerUnit must not be null");
        Assert.notNull(detail.getUnits(), "detail units must not be null");
        Assert.notNull(detail.getRecord(), "detail record must not be null");
        Assert.hasLength(detail.getRecord(), "detail record must not be empty");
    }

}
