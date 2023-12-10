package com.mercan.weather.service;

import com.mercan.weather.model.AppConstants;
import com.mercan.weather.model.MetricQuery;
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

        private Alias(String prefix) {
            this.prefix = prefix;
        }
    }

    public static Selection createSelection(Root root, CriteriaBuilder criteriaBuilder, AppConstants.Statistic statistic, AppConstants.Metric metric, List<MetricQuery> metricQueries) {

        String metricName = metric.name();
        switch (statistic) {
            case SUM -> {
                String alias = Alias.SUM.prefix + metric;
                metricQueries.add(MetricQuery.builder().metric("sum").metricAlias(alias).metricField(metric).build());
                return criteriaBuilder.sum(root.get(metricName)).alias(alias);
            }
            case AVERAGE -> {
                String alias = Alias.AVG.prefix + metric;
                metricQueries.add(MetricQuery.builder().metric("avg").metricAlias(alias).metricField(metric).build());
                return criteriaBuilder.avg(root.get(metricName)).alias(alias);
            }
            case MIN -> {
                String alias = Alias.MIN.prefix + metric;
                metricQueries.add(MetricQuery.builder().metric("min").metricAlias(alias).metricField(metric).build());
                return criteriaBuilder.min(root.get(metricName)).alias(alias);
            }
            case MAX -> {
                String alias = Alias.MAX.prefix + metric;
                metricQueries.add(MetricQuery.builder().metric("max").metricAlias(alias).metricField(metric).build());
                return criteriaBuilder.max(root.get(metricName)).alias(alias);
            }
        }
        return null;
    }
}
