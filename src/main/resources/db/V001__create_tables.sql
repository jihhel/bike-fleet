CREATE TABLE users IF NOT EXISTS (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
);

INSERT INTO users(name) VALUES ('Alice'), ('Bob'), ('Charles');

CREATE TABLE stations IF NOT EXISTS (
    id SERIAL PRIMARY KEY
);

CREATE TABLE bikes IF NOT EXISTS (
    id SERIAL PRIMARY KEY,
    station_id INT,
    CONSTRAINT fk_station FOREIGN KEY(station_id) REFERENCES stations(id)
)