CREATE KEYSPACE IF NOT EXISTS Trainings
  WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 };

USE Trainings;

CREATE TABLE Users (
  userId UUID,
  name varchar,
  phone int,
  PRIMARY KEY (name, userId)
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
  PRIMARY KEY (name, trainingId)
);

CREATE TABLE Reservations (
  user UUID,
  userName varchar,
  training UUID,
  trainingName varchar,
  reservationTime timestamp,
  PRIMARY KEY (training, user)
);
