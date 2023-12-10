-- ----------------------------
-- Schema for weather service
-- ----------------------------
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- ----------------------------
-- Table structure for sensor
-- ----------------------------
CREATE TABLE IF NOT EXISTS sensor (
                                 id uuid DEFAULT uuid_generate_v4 (),
                                 name VARCHAR,
                                 PRIMARY KEY (id)
);
COMMENT ON TABLE sensor IS 'sensor table';



CREATE TABLE IF NOT EXISTS reading (
                                 id uuid DEFAULT uuid_generate_v4 (),
                                 sensor_id uuid,
                                 temperature smallint,
                                 humidity smallint,
                                 wind_speed smallint,
                                 reading_time timestamp,
                                 PRIMARY KEY (id),
                                 CONSTRAINT reading
                                       FOREIGN KEY(sensor_id)
                                 	  REFERENCES sensor(id)
);

COMMENT ON TABLE reading IS 'sensor reading table';