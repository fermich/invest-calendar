create table events (id varchar(100), timestamp varchar(50), country varchar(50), currency varchar(50), sentiment varchar(50), event varchar(100), feature varchar(100), actual varchar(50), forecast varchar(50), previous varchar(50), revision varchar(100));
create view v_events as select left(replace(timestamp, '-', ''), 8) as dtyymmdd, right(replace(timestamp, ':', ''), 6) as dthhmmss, e.* from events e;
create table quotes (ticker varchar(20), per varchar(20), dtyymmdd varchar(10), dthhmmss varchar(10), open Decimal(10,4), high Decimal(10,4), low Decimal(10,4), close Decimal(10,4), vol int, primary key (ticker, dtyymmdd, dthhmmss));
create index date_index on quotes (dtyymmdd);
