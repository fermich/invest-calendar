create table option_quotes (ticker varchar(20), dtyymmdd varchar(10), open Decimal(10,4), high Decimal(10,4), low Decimal(10,4), close Decimal(10,4), volatility Decimal(20,4), volume int, primary key (ticker, dtyymmdd));

create table option_code(code varchar(1), type varchar(1), month tinyint);

insert into option_code(code, type, month) values ('A', 'C', 1);
insert into option_code(code, type, month) values ('B', 'C', 2);
insert into option_code(code, type, month) values ('C', 'C', 3);
insert into option_code(code, type, month) values ('D', 'C', 4);
insert into option_code(code, type, month) values ('E', 'C', 5);
insert into option_code(code, type, month) values ('F', 'C', 6);
insert into option_code(code, type, month) values ('G', 'C', 7);
insert into option_code(code, type, month) values ('H', 'C', 8);
insert into option_code(code, type, month) values ('I', 'C', 9);
insert into option_code(code, type, month) values ('J', 'C', 10);
insert into option_code(code, type, month) values ('K', 'C', 11);
insert into option_code(code, type, month) values ('L', 'C', 12);
insert into option_code(code, type, month) values ('M', 'P', 1);
insert into option_code(code, type, month) values ('N', 'P', 2);
insert into option_code(code, type, month) values ('O', 'P', 3);
insert into option_code(code, type, month) values ('P', 'P', 4);
insert into option_code(code, type, month) values ('Q', 'P', 5);
insert into option_code(code, type, month) values ('R', 'P', 6);
insert into option_code(code, type, month) values ('S', 'P', 7);
insert into option_code(code, type, month) values ('T', 'P', 8);
insert into option_code(code, type, month) values ('U', 'P', 9);
insert into option_code(code, type, month) values ('V', 'P', 10);
insert into option_code(code, type, month) values ('W', 'P', 11);
insert into option_code(code, type, month) values ('X', 'P', 12);
