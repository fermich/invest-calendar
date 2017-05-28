create table events (id varchar(100), timestamp varchar(50), country varchar(50), currency varchar(50), sentiment varchar(50), event varchar(100), feature varchar(100), actual varchar(50), forecast varchar(50), previous varchar(50), revision varchar(100));

create view v_events as select left(replace(timestamp, '-', ''), 8) as dtyymmdd, right(replace(timestamp, ':', ''), 6) as dthhmmss, e.* from events e;
