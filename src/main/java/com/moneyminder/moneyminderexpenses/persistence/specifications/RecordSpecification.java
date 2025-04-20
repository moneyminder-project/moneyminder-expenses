package com.moneyminder.moneyminderexpenses.persistence.specifications;

import com.moneyminder.moneyminderexpenses.persistence.entities.BudgetEntity;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import com.moneyminder.moneyminderexpenses.utils.RecordType;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class RecordSpecification {
    public static Specification<RecordEntity> filterByMoneyGreaterOrEqualThan(Double money) {
        return (root, query, cb) -> {
            if (money == null) {
                return null;
            }

            return cb.greaterThanOrEqualTo(root.get("money"), money);
        };
    }

    public static Specification<RecordEntity> filterByMoneyLessOrEqualThan(Double money) {
        return (root, query, cb) -> {
            if (money == null) {
                return null;
            }

            return cb.lessThanOrEqualTo(root.get("money"), money);
        };
    }

    public static Specification<RecordEntity> filterByDateBeforeOrEqualThan(LocalDate date) {
        return (root, query, cb) -> {
            if (date == null) {
                return null;
            }
            return cb.lessThanOrEqualTo(root.get("date"), SpecificationUtils.convertToDate(date));
        };
    }

    public static Specification<RecordEntity> filterByDateAfterOrEqualThan(LocalDate date) {
        return (root, query, cb) -> {
            if (date == null) {
                return null;
            }
            return cb.greaterThanOrEqualTo(root.get("date"), SpecificationUtils.convertToDate(date));
        };
    }

    public static Specification<RecordEntity> filterByBudgetsIn (List<String> budgetIds) {
        return (root, query, cb) -> {
            if (budgetIds == null) {
                return null;
            }

            Join<RecordEntity, BudgetEntity> budgetJoin = root.join("budgets");
            return budgetJoin.get("id").in(budgetIds);
        };
    }

    public static Specification<RecordEntity> filterByOwner (String owner) {
        return (root, query, cb) -> {
            if (owner == null) {
                return null;
            }

            return cb.equal(root.get("owner"), owner);
        };
    }

    public static Specification<RecordEntity> searchByComment (String comment) {
        return (root, query, cb) -> {
            if (comment == null) {
                return null;
            }

            return SpecificationUtils.databaseTextNormalizer(cb, root.get("comment"), comment);
        };
    }
    public static Specification<RecordEntity> searchByName (String name) {
        return (root, query, cb) -> {
            if (name == null) {
                return null;
            }

            return SpecificationUtils.databaseTextNormalizer(cb, root.get("name"), name);
        };
    }

    public static Specification<RecordEntity> filterByType (RecordType type) {
        return (root, query, cb) -> {
            if (type == null) {
                return null;
            }

            return cb.equal(root.get("type"), type);
        };
    }
}
