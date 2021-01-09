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
  trainingId UUID,
  name varchar,
  timeslot int,
  PRIMARY KEY (trainingID, name, timeslot)
);

CREATE TABLE Reservations (
  user UUID,
  training UUID,
  reservationTime timestamp,
  PRIMARY KEY (training, user)
);
