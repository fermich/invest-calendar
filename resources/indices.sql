create table indices(
    name varchar(100),
    timestamp varchar(50),
    price varchar(50),
    open varchar(50),
    high varchar(50),
    low varchar(50),
    vol varchar(50),
    chg varchar(50));

create view d_indices as
    select left(from_unixtime(timestamp),10) as dtyymmdd,
           i.*,
           (case when (chg like '-%') THEN 0 ELSE 1 END) bin_chg
      from indices i;


create table indices_codes(curr varchar(20), sml varchar(20), name varchar(100), header varchar(200));

insert into indices_codes(curr, sml, name, header) values ('172', '2030173', 'DAX', 'DAX Historical Data');
insert into indices_codes(curr, sml, name, header) values ('14602', '2033010', 'WIG20', 'WIG20 Historical Data');
