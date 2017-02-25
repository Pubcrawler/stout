CREATE DATABASE STOUT;

CREATE TABLE Crawls
(
  id SERIAL PRIMARY KEY,
  title VARCHAR(50) NOT NULL,
  owner_id INT NOT NULL,
  date_time DATE NOT NULL,
  lat FLOAT NOT NULL,
  lng FLOAT NOT NULL,
  radius INT NOT NULL,
  city VARCHAR(35) NOT NULL,
  description VARCHAR(800) NOT NULL
);

CREATE TABLE CrawlParticipants
(
  crawl_id SERIAL PRIMARY KEY,
  user_id INT NOT NULL,
  status CHAR(1) NOT NULL
);

CREATE TABLE Routes
(
  route_id SERIAL PRIMARY KEY,
  owner_id INT NOT NULL
);

CREATE TABLE Stops
(
  stop_id SERIAL PRIMARY KEY,
  title VARCHAR(50) NOT NULL,
  address VARCHAR(100) NOT NULL,
  lat FLOAT,
  lng FLOAT
);

CREATE TABLE RouteStops
(
  route_stop_id SERIAL PRIMARY KEY,
  stop_id INT NOT NULL,
  "order" INT NOT NULL,
  stop_fk INT REFERENCES Stops(stop_id),
  route_fk INT REFERENCES Routes(route_id)
);

CREATE TABLE Users
(
  user_id SERIAL PRIMARY KEY,
  username VARCHAR(15) NOT NULL,
  birthdate DATE,
  gender CHAR(1),
  email VARCHAR(50),
  facebook_id INT NOT NULL
);

CREATE TABLE Wishes
(
  wish_id SERIAL PRIMARY KEY,
  user_id INT NOT NULL,
  stop_id INT NOT NULL,
  user_fk INT REFERENCES Users(user_id),
  stop_fk INT REFERENCES Stops(stop_id)
);