create table commodities(
    timestamp varchar(50),
    price varchar(50),
    open varchar(50),
    high varchar(50),
    low varchar(50),
    vol varchar(50),
    chg varchar(50));

create table commodity_codes(curr varchar(20), sml varchar(20), header varchar(200));

insert into commodity_codes (curr, sml, header) values ('8830', '300004', 'Gold Futures Historical Data');
