create table fx_quotes (
    ticker varchar(20),
    per varchar(20),
    dtyymmdd varchar(10),
    dthhmmss varchar(10),
    open Decimal(10,4),
    high Decimal(10,4),
    low Decimal(10,4),
    close Decimal(10,4),
    vol int,
    primary key (ticker, dtyymmdd, dthhmmss));

create index date_index on fx_quotes (dtyymmdd);
