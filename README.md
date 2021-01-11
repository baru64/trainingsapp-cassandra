# Treningi - aplikacja do rezerwacji zajęć

## Opis

Zadaniem aplikacji jest zbierać rezerwacje klientów na wybrane treningi. Użytkownik może sprawdzić status swojej rezerwacji lub ją anulować. W przypadku zbyt wielu rezerwacji na dany trening, osoby nie mieszczące się w sali są uznawane za umieszczone na liście rezerwowej.

Stan rezerwacji nie jest bezpośrednio przetrzymywany w bazie, lecz obliczany przez aplikację na podstawie stanu bazy, w ten sposób można uniknąć niespójności wywołanej przez sposób działania bazy danych.

## Schemat bazy danych

```CQL
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
  PRIMARY KEY (timeslot, name, trainingId)
);

CREATE TABLE Reservations (
  user UUID,
  userName varchar,
  training UUID,
  trainingName varchar,
  reservationTime timestamp,
  PRIMARY KEY (training, user)
);
```
