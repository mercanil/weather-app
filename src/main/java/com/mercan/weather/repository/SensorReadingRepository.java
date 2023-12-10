package com.mercan.weather.repository;

import com.mercan.weather.entity.Reading;
import com.mercan.weather.model.*;
import com.mercan.weather.service.QueryBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Transactional
@Repository
@RequiredArgsConstructor
public class SensorReadingRepository {

    private final EntityManager entityManager;
    CriteriaBuilder criteriaBuilder;
    CriteriaQuery criteriaQuery;
    Root root;

    @PostConstruct
    public void init() {
        criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaQuery = criteriaBuilder.createTupleQuery();
        root = criteriaQuery.from(Reading.class);
    }

    public void save(Reading reading) {
        entityManager.persist(reading);
    }

    public List<SensorQueryResponse> query(Set<UUID> sensorIds, List<String> metrics, String statistic, Optional<Date> startDate, Optional<Date> endDate) {
        List<SensorQueryResponse> queryResponseList = new ArrayList<>();
        List<Selection> selections = new ArrayList<>();


        List<MetricQuery> metricQueries = new ArrayList<>();
        for (String metric : metrics) {
            selections.add(QueryBuilder.createSelection(root, criteriaBuilder, AppConstants.Statistic.valueOf(statistic), AppConstants.Metric.valueOf(metric), metricQueries));
        }

        criteriaQuery.multiselect(selections.toArray(new Selection[0]));

        for (UUID sensorId : sensorIds) {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(AppConstants.SENSOR_ID_COLUMN_NAME), sensorId));
            startDate.ifPresent(start -> predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(AppConstants.READING_COLUMN_NAME), start)));
            endDate.ifPresent(end -> predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(AppConstants.READING_COLUMN_NAME), end)));
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
            TypedQuery query = entityManager.createQuery(criteriaQuery);
            Tuple result = (Tuple) query.getSingleResult();
            List<StatisticModel> statisticModels = new ArrayList<>();

            for (MetricQuery metricQuery : metricQueries) {
                HashMap<String, String> metricResult = new HashMap<>();
                metricResult.put(metricQuery.getMetric(), result.get(metricQuery.getMetricAlias()).toString());
                statisticModels.add(StatisticModel.builder().metric(metricQuery.getMetricField().toString()).values(metricResult).build());
            }

            SensorQueryResponse response = new SensorQueryResponse();
            response.setSensorId(sensorId);
            response.setStatistics(statisticModels);
            queryResponseList.add(response);
        }
        return queryResponseList;

    }
}
