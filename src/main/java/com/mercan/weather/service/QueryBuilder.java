package com.mercan.weather.service;

import com.mercan.weather.model.AppConstants;
import com.mercan.weather.model.MetricFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;

import java.util.List;

public class QueryBuilder {

    public enum Alias {
        SUM("sum_"),
        AVG("avg_"),
        MIN("min_"),
        MAX("max_");
        public final String prefix;

        Alias(String prefix) {
            this.prefix = prefix;
        }
    }

    public static Selection<?> createSelection(Root<?> root, CriteriaBuilder criteriaBuilder,
                                               AppConstants.Statistic statistic, AppConstants.Metric metric,
                                               List<MetricFilter> metricQueries) {

        String metricName = metric.name();
        String aliasPrefix = getAliasPrefix(statistic);
        String alias = aliasPrefix + metricName;

        MetricFilter metricFilter = MetricFilter.builder()
                .metric(statistic.name().toLowerCase())
                .metricAlias(alias)
                .metricField(metric)
                .build();
        metricQueries.add(metricFilter);

        return getSelectionByStatistic(criteriaBuilder, root, metricName, alias, statistic);
    }

    private static String getAliasPrefix(AppConstants.Statistic statistic) {
        return switch (statistic) {
            case SUM -> Alias.SUM.prefix;
            case AVERAGE -> Alias.AVG.prefix;
            case MIN -> Alias.MIN.prefix;
            case MAX -> Alias.MAX.prefix;
        };
    }

    private static Selection<?> getSelectionByStatistic(CriteriaBuilder cb, Root<?> root,
                                                        String metricName, String alias,
                                                        AppConstants.Statistic statistic) {
        return switch (statistic) {
            case SUM -> cb.sum(root.get(metricName)).alias(alias);
            case AVERAGE -> cb.avg(root.get(metricName)).alias(alias);
            case MIN -> cb.min(root.get(metricName)).alias(alias);
            case MAX -> cb.max(root.get(metricName)).alias(alias);
        };
    }
}
