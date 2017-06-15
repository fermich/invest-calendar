create table stock_quotes (
    ticker varchar(20),
    dtyymmdd varchar(10),
    open Decimal(10,4),
    high Decimal(10,4),
    low Decimal(10,4),
    close Decimal(10,4),
    vol int,
    primary key (ticker, dtyymmdd));

create table stock_stats_filter (
    ticker varchar(20),
    primary key (ticker));

insert into stock_stats_filter(ticker) values ('WIG20');

create view v_stock_stats as
select sq.ticker,
       concat(substring(sq.dtyymmdd, 1, 4), '-', substring(sq.dtyymmdd, 5, 2), '-', substring(sq.dtyymmdd, 7, 2)) dtyymmdd,
       sq.close,
       avg(ss.close) avg_close,
       std(ss.close) std_close
       from stock_quotes sq, stock_quotes ss, stock_stats_filter ssf
       where sq.ticker = ssf.ticker
         and sq.ticker = ss.ticker
         and sq.dtyymmdd > '20140318'
         and sq.dtyymmdd <= ss.dtyymmdd
       group by sq.ticker, sq.dtyymmdd;
