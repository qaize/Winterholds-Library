drop table request;

create table request (
id bigint primary key,
membershipNumber varchar(20),
bookCode varchar(20),
requestDate datetime,
status bit
);