package com.moneyminder.moneyminderexpenses.processors.detail;

import com.moneyminder.moneyminderexpenses.mappers.DetailMapper;
import com.moneyminder.moneyminderexpenses.persistence.entities.DetailEntity;
import com.moneyminder.moneyminderexpenses.persistence.repositories.DetailRepository;
import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteDetailProcessor {
    private final DetailRepository detailRepository;
    private final DetailMapper detailMapper;

    public void deleteDetail(String id) {
        final DetailEntity detail = this.detailRepository.findById(id).orElse(null);
        Assert.notNull(detail, "Detail not found");
        Assert.isTrue(detail.getRecord().getOwner().equals(AuthUtils.getUsername()), "Owner is not the owner of the detail");

        Assert.isTrue(detailRepository.existsById(id), "The detail does not exist");
        this.detailRepository.deleteById(id);
    }

    public void deleteDetailList(List<String> ids) {
        ids.forEach(this::deleteDetail);
    }
}
