package com.moneyminder.moneyminderexpenses.persistence.specifications;

import com.moneyminder.moneyminderexpenses.persistence.entities.DetailEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class DetailSpecification {
    public static Specification<DetailEntity> searchByName (String name) {
        return (root, query, cb) -> {
            if (name == null) {
                return null;
            }

            return SpecificationUtils.databaseTextNormalizer(cb, root.get("name"), name);
        };
    }

    public static Specification<DetailEntity> searchByIdIn(List<String> ids) {
        return(root, query, cb) -> {
            if (ids == null) {
                return null;
            }

            return root.get("id").in(ids);
        };
    }
}
