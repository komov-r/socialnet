CREATE TABLE if not exists UserProfile
(
    id        INT auto_increment primary key,
    username  varchar(255) unique,
    password  varchar(255),
    firstName varchar(255),
    surname   varchar(255),
    city      varchar(255),
    birthDate date,
    gender    varchar(2),
    interests varchar(255)
);

CREATE TABLE if not exists Friend
(
    id       INT auto_increment primary key,
    userId   INT,
    friendId INT,
    unique key fr (userId, friendId)
);


CREATE TABLE if not exists HistoryItem
(
    id                INT auto_increment primary key,
    ownerId           INT,
    eventType         varchar(50),
    userId            INT,
    eventDate         date,
    userDescription   varchar(255),
    objectDescription varchar(255),
    objectId          INT,
    objectType        varchar(50)
);