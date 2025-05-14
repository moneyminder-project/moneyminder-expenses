package com.moneyminder.moneyminderexpenses.persistence.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class SpecificationUtils {
//    public static Expression<String> concat(String delimiter, CriteriaBuilder cb, Expression<String>... expressions) {
//        Expression<String> result = null;
//        for (int i = 0; i < expressions.length; i++) {
//            final boolean first = i == 0, last = i == (expressions.length - 1);
//            final Expression<String> expression = expressions[i];
//            if (first && last) {
//                result = expression;
//            } else if (first) {
//                result = cb.concat(expression, delimiter);
//            } else {
//                result = cb.concat(expression, delimiter);
//                if (!last) {
//                    result = cb.concat(expression, delimiter);
//                }
//            }
//        }
//        return result;
//    }
//
//
//    public static String normalizeText(String text) {
//        return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
//    }

//    SOLO FUNCIONA EN ORACLE DB
//    public static Predicate databaseTextNormalizer(CriteriaBuilder cb, Expression<String> expression, String s) {
//        return cb.like(cb.function("utl_raw.cast_to_varchar2", String.class, cb.function("nlssort", String.class, expression, cb.literal("nls_sort=binary_ai"))),
//                "%" + Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase() + "%");
//    }

    public static Predicate databaseTextNormalizer(CriteriaBuilder cb, Expression<String> expression, String s) {
        String normalizedInput = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();

        Expression<String> normalizedExpression = cb.lower(
                cb.function("replace", String.class,
                        cb.function("replace", String.class,
                                cb.function("replace", String.class,
                                        cb.function("replace", String.class,
                                                cb.function("replace", String.class,
                                                        expression,
                                                        cb.literal("á"), cb.literal("a")),
                                                cb.literal("é"), cb.literal("e")),
                                        cb.literal("í"), cb.literal("i")),
                                cb.literal("ó"), cb.literal("o")),
                        cb.literal("ú"), cb.literal("u"))
        );

        return cb.like(normalizedExpression, "%" + normalizedInput + "%");
    }

    public static Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
