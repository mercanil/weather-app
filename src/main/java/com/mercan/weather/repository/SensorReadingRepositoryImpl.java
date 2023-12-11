package com.mercan.weather.repository;

import com.mercan.weather.entity.Reading;
import com.mercan.weather.model.AppConstants;
import com.mercan.weather.model.MetricFilter;
import com.mercan.weather.model.MetricStatistic;
import com.mercan.weather.model.SensorQueryResponse;
import com.mercan.weather.service.QueryBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Transactional
@Repository
@RequiredArgsConstructor
public class SensorReadingRepositoryImpl {

    private final EntityManager entityManager;

    public void save(Reading reading) {
        entityManager.persist(reading);
    }

    public List<SensorQueryResponse> query(Set<UUID> sensorIds, List<AppConstants.Metric> metrics,
                                           AppConstants.Statistic statisticOpt, Optional<Date> startDate,
                                           Optional<Date> endDate) {
        List<SensorQueryResponse> queryResponseList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<Reading> root = criteriaQuery.from(Reading.class);

        List<MetricFilter> metricFilters = new ArrayList<>();
        criteriaQuery.multiselect(buildSelection(metricFilters, root, criteriaBuilder, statisticOpt, metrics));

        for (UUID sensorId : sensorIds) {
            Predicate predicate = buildPredicate(root, criteriaBuilder, sensorId, startDate, endDate);
            criteriaQuery.where(predicate);

            TypedQuery<Tuple> query = entityManager.createQuery(criteriaQuery);
            List<Tuple> results = query.getResultList();
            SensorQueryResponse response = processResult(results, metricFilters, sensorId);
            queryResponseList.add(response);
        }
        return queryResponseList;

    }

    private Selection[] buildSelection(List<MetricFilter> metricFilters, Root<Reading> root, CriteriaBuilder criteriaBuilder, AppConstants.Statistic statisticOpt, List<AppConstants.Metric> metrics) {

        List<Selection> selections = new ArrayList<>();
        for (AppConstants.Metric metric : metrics) {
            selections.add(QueryBuilder.createSelection(root, criteriaBuilder, statisticOpt, metric, metricFilters));
        }
        return selections.toArray(new Selection[0]);

    }

    private Predicate buildPredicate(Root<Reading> root, CriteriaBuilder criteriaBuilder, UUID sensorId,
                                     Optional<Date> startDateOpt, Optional<Date> endDateOpt) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get(AppConstants.SENSOR_ID_COLUMN_NAME), sensorId));
        startDateOpt.ifPresent(date -> predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(AppConstants.READING_COLUMN_NAME), date)));
        endDateOpt.ifPresent(date -> predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(AppConstants.READING_COLUMN_NAME), date)));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private SensorQueryResponse processResult(List<Tuple> results, List<MetricFilter> metricFilters, UUID sensorId) {
        SensorQueryResponse response = new SensorQueryResponse();
        List<MetricStatistic> metricStatistics = new ArrayList<>();
        for (MetricFilter metricFilter : metricFilters) {
            HashMap<String, String> metricResult = new HashMap<>();
            if (results.get(0).get(metricFilter.getMetricAlias()) != null) {
                metricResult.put(metricFilter.getMetric(), results.get(0).get(metricFilter.getMetricAlias()).toString());
                metricStatistics.add(MetricStatistic.builder().metric(metricFilter.getMetricField().toString()).values(metricResult).build());
            }
        }


        response.setSensorId(sensorId);
        response.setStatistics(metricStatistics);
        return response;
    }
}
