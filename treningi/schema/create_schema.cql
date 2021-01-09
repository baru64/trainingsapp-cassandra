CREATE KEYSPACE IF NOT EXISTS Trainings
  WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 };

USE Trainings;

CREATE TABLE Users (
  userId UUID,
  name varchar,
  phone int,
  PRIMARY KEY (userId, phone)
);

CREATE TABLE Rooms (
  roomId UUID,
  capacity int,
  PRIMARY KEY (roomId)
);

CREATE TABLE Trainings (
  name varchar,
  timeslot int,
  PRIMARY KEY (timeslot, name)
);

CREATE TABLE Reservations (
  user UUID,
  trainingName varchar,
  timeslot int,
  reservationTime timestamp,
  PRIMARY KEY (trainingName, user)
);
