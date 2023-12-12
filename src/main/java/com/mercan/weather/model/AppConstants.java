package com.mercan.weather.model;

public class AppConstants {

    public static final String SENSOR_ID_COLUMN_NAME = "sensorId";
    public static final String READING_COLUMN_NAME = "readingTime";

    public enum Statistic {
        SUM,
        AVERAGE,
        MIN,
        MAX
    }

    public enum Metric {
        humidity,
        windSpeed,
        temperature
    }

}
