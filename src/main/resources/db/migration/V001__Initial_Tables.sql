CREATE TABLE users
(
  user_id SERIAL PRIMARY KEY,
  username VARCHAR(15) NOT NULL,
  birthdate DATE,
  gender CHAR(1),
  email VARCHAR(50),
  facebook_id INT NOT NULL
);

CREATE TABLE routes
(
  route_id SERIAL PRIMARY KEY,
  owner_id INT REFERENCES users(user_id) NOT NULL
);

CREATE TABLE crawls
(
  crawl_id SERIAL PRIMARY KEY,
  title VARCHAR(50) NOT NULL,
  owner_id INT REFERENCES users(user_id) NOT NULL,
  route_id INT REFERENCES routes(route_id) NOT NULL,
  date_time DATE NOT NULL,
  address VARCHAR(100) NOT NULL,
  city VARCHAR(35) NOT NULL,
  lat REAL NOT NULL,
  lng REAL NOT NULL,
  radius REAL NOT NULL,
  description VARCHAR(1000)
);

CREATE TABLE crawl_participants
(
  crawl_id INT REFERENCES crawls(crawl_id),
  user_id INT REFERENCES users(user_id),
  status CHAR(1) NOT NULL
);

CREATE TABLE stops
(
  stop_id SERIAL PRIMARY KEY,
  title VARCHAR(50) NOT NULL,
  address VARCHAR(100) NOT NULL,
  city VARCHAR(35) NOT NULL,
  lat REAL,
  lng REAL
);

CREATE TABLE route_stops
(
  route_id INT REFERENCES routes(route_id) NOT NULL,
  stop_id INT REFERENCES stops(stop_id) NOT NULL,
  "order" INT NOT NULL
);

CREATE TABLE wishes
(
  user_id INT REFERENCES users(user_id) NOT NULL,
  stop_id INT REFERENCES stops(stop_id) NOT NULL
);
