create table events (
    id varchar(100),
    dtyymmdd varchar(20),
    timestamp varchar(50),
    country varchar(50),
    currency varchar(50),
    sentiment varchar(50),
    event varchar(100),
    feature varchar(100),
    actual varchar(50),
    forecast varchar(50),
    previous varchar(50),
    revision varchar(100));

create view v_events as
    select left(replace(timestamp, '-', ''), 8) as dtyymmdd,
           right(replace(timestamp, ':', ''), 6) as dthhmmss,
           e.*
      from events e;

create view f_events as
    select e.*,
           case when (e.actual - e.previous <= 0) THEN 0 ELSE 1 END as chg
      from (
        select g.dtyymm, g.event_id, sum(g.actual) actual, sum(g.previous) previous from (
          select left(dtyymmdd, 7) dtyymm,
            id as event_id,
            cast(replace(actual, ',','.') as decimal(10,3)) actual,
            cast(replace(previous, ',','.') as decimal(10,3)) previous
           from events
        ) g group by g.dtyymm, g.event_id) e;
