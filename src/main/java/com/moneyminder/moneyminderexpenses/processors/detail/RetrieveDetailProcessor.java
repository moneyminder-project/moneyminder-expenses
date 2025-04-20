package com.moneyminder.moneyminderexpenses.processors.detail;

import com.moneyminder.moneyminderexpenses.mappers.DetailMapper;
import com.moneyminder.moneyminderexpenses.models.Detail;
import com.moneyminder.moneyminderexpenses.persistence.entities.DetailEntity;
import com.moneyminder.moneyminderexpenses.persistence.repositories.DetailRepository;
import com.moneyminder.moneyminderexpenses.persistence.specifications.DetailSpecification;
import com.moneyminder.moneyminderexpenses.requestParams.DetailRequestParams;
import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RetrieveDetailProcessor {
    private final DetailRepository detailRepository;
    private final DetailMapper detailMapper;

    public List<Detail> retrieveDetails(DetailRequestParams params) {
        return this.detailMapper.toModelList(this.detailRepository.findAll(this.buildSpecification(params)));
    }

    public Detail retrieveDetailById(final String id) {
        final DetailEntity detail = this.detailRepository.findById(id).orElse(null);
        Assert.notNull(detail, "Detail not found");
        Assert.isTrue(detail.getRecord().getOwner().equals(AuthUtils.getUsername()), "Owner is not the owner of the detail");

        return this.detailMapper.toModel(this.detailRepository.findById(id).orElseThrow());
    }

    private Specification<DetailEntity> buildSpecification(final DetailRequestParams params) {
        return DetailSpecification.searchByName(params.getName())
                .and(DetailSpecification.searchByIdIn(params.getIds()));
    }
}
