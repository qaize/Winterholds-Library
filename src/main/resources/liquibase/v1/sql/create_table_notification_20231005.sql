create table notification (
Id varchar(36),
recipientId varchar(20),
notificationHeader varchar(50),
notificationMessage varchar(100),
isNew bit,
isViewed bit,
createdDate datetime,
createdBy varchar(50)
);