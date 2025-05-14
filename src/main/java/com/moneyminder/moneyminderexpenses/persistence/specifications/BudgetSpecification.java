package com.moneyminder.moneyminderexpenses.persistence.specifications;

import com.moneyminder.moneyminderexpenses.persistence.entities.BudgetEntity;
import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class BudgetSpecification {
    public static Specification<BudgetEntity> filterByExpensesLimitGreaterOrEqualThan(Double expensesLimit) {
        return (root, query, cb) -> {
            if (expensesLimit == null) {
                return null;
            }
            return cb.greaterThanOrEqualTo(root.get("expensesLimit"), expensesLimit);
        };
    }

    public static Specification<BudgetEntity> filterByExpensesLimitLowerOrEqualThan(Double expensesLimit) {
        return (root, query, cb) -> {
            if (expensesLimit == null) {
                return null;
            }
            return cb.lessThanOrEqualTo(root.get("expensesLimit"), expensesLimit);
        };
    }

    public static Specification<BudgetEntity> filterByStartDateBeforeOrEqualThan(LocalDate startDate) {
        return (root, query, cb) -> {
            if (startDate == null) {
                return null;
            }
            return cb.lessThanOrEqualTo(root.get("startDate"), SpecificationUtils.convertToDate(startDate));
        };
    }

    public static Specification<BudgetEntity> filterByStartDateAfterOrEqualThan(LocalDate startDate) {
        return (root, query, cb) -> {
            if (startDate == null) {
                return null;
            }
            return cb.greaterThanOrEqualTo(root.get("startDate"), SpecificationUtils.convertToDate(startDate));
        };
    }

    public static Specification<BudgetEntity> filterByEndDateBeforeOrEqualThan(LocalDate endDate) {
        return (root, query, cb) -> {
            if (endDate == null) {
                return null;
            }
            return cb.lessThanOrEqualTo(root.get("endDate"), SpecificationUtils.convertToDate(endDate));
        };
    }

    public static Specification<BudgetEntity> filterByEndDateAfterOrEqualThan(LocalDate endDate) {
        return (root, query, cb) -> {
            if (endDate == null) {
                return null;
            }
            return cb.greaterThanOrEqualTo(root.get("endDate"), SpecificationUtils.convertToDate(endDate));
        };
    }

    public static Specification<BudgetEntity> filterByGroupIdIn(List<String> groupIds) {
        return (root, query, cb) -> {
            if (groupIds == null) {
                return null;
            }
            return root.get("groupId").in(groupIds);
        };
    }

    public static Specification<BudgetEntity> searchByName (String name) {
        return (root, query, cb) -> {
            if (name == null) {
                return null;
            }

            return SpecificationUtils.databaseTextNormalizer(cb, root.get("name"), name);
        };
    }

    public static Specification<BudgetEntity> searchByComment (String comment) {
        return (root, query, cb) -> {
            if (comment == null) {
                return null;
            }

            return SpecificationUtils.databaseTextNormalizer(cb, root.get("comment"), comment);
        };
    }

    public static Specification<BudgetEntity> filterByFavorite(Boolean isFavorite) {
        return (root, query, cb) -> {
            if (isFavorite == null) {
                return null;
            }

            final String username = AuthUtils.getUsername();

            if (username == null) {
                return null;
            }

            if (isFavorite) {
                return cb.isMember(username, root.get("usernames"));
            }

            return cb.isNotMember(username, root.get("usernames"));
        };
    }
}
