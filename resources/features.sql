create table features (
    id bigint,
    name varchar(50),
    index_name varchar(50),
    event_id varchar(50),
    event_name varchar(50),
    country varchar(50)
);

insert into features (id, name, index_name, event_id, event_name, country) values (1, 'WIG20_exp', 'WIG20', '873', 'CPI (MoM)', 'Poland');
insert into features (id, name, index_name, event_id, event_name, country) values (2, 'WIG20_exp', 'WIG20', '445', 'CPI (YoY)', 'Poland');
insert into features (id, name, index_name, event_id, event_name, country) values (3, 'WIG20_exp', 'WIG20', '879', 'GDP (QoQ)', 'Poland');
insert into features (id, name, index_name, event_id, event_name, country) values (4, 'WIG20_exp', 'WIG20', '446', 'GDP (YoY)', 'Poland');
insert into features (id, name, index_name, event_id, event_name, country) values (5, 'WIG20_exp', 'WIG20', '1304', 'Manufacturing PMI', 'Poland');
insert into features (id, name, index_name, event_id, event_name, country) values (6, 'WIG20_exp', 'WIG20', '652', 'PPI (YoY)', 'Poland');
insert into features (id, name, index_name, event_id, event_name, country) values (7, 'WIG20_exp', 'WIG20', '653', 'Retail Sales (YoY)', 'Poland');
insert into features (id, name, index_name, event_id, event_name, country) values (8, 'WIG20_exp', 'WIG20', '650', 'Unemployment Rate', 'Poland');
insert into features (id, name, index_name, event_id, event_name, country) values (9, 'WIG20_exp', 'WIG20', '447', 'Interest Rate Decision', 'Poland');

insert into features (id, name, index_name, event_id, event_name, country) values (1, 'DAX_exp', 'DAX', '128', 'CPI (MoM)', 'Germany');
insert into features (id, name, index_name, event_id, event_name, country) values (2, 'DAX_exp', 'DAX', '737', 'CPI (YoY)', 'Germany');
insert into features (id, name, index_name, event_id, event_name, country) values (3, 'DAX_exp', 'DAX', '131', 'GDP (QoQ)', 'Germany');
insert into features (id, name, index_name, event_id, event_name, country) values (4, 'DAX_exp', 'DAX', '738', 'GDP (YoY)', 'Germany');
insert into features (id, name, index_name, event_id, event_name, country) values (5, 'DAX_exp', 'DAX', '136', 'Manufacturing PMI', 'Germany');
insert into features (id, name, index_name, event_id, event_name, country) values (6, 'DAX_exp', 'DAX', '739', 'PPI (YoY)', 'Germany');
insert into features (id, name, index_name, event_id, event_name, country) values (7, 'DAX_exp', 'DAX', '740', 'Retail Sales (YoY)', 'Germany');
insert into features (id, name, index_name, event_id, event_name, country) values (8, 'DAX_exp', 'DAX', '142', 'Unemployment Rate', 'Germany');
insert into features (id, name, index_name, event_id, event_name, country) values (9, 'DAX_exp', 'DAX', '164', 'Interest Rate Decision', 'Germany');


create table features_binary_input (
    dtyymm varchar(20),
    feature_id bigint,
    binary_value int
);

insert into features_binary_input (dtyymm, feature_id, binary_value) select distinct  e.dtyymm, f.id, e.chg from f_events e, features f where e.event_id = f.event_id and f.country='Poland';
delete from features_binary_input where dtyymm < '2012-07';
delete from features_binary_input where feature_id = 3;
delete from features_binary_input where feature_id = 4;
delete from features_binary_input where feature_id = 9;


insert into features_binary_input (dtyymm, feature_id, binary_value) select distinct e.dtyymm, f.id, e.chg from f_events e, features f where e.event_id = f.event_id and f.country='Germany';

create view features_pivot as
select f1.dtyymm,
    f1.binary_value f1,
    f2.binary_value f2,
    f5.binary_value f5,
    f6.binary_value f6,
    f7.binary_value f7,
    f8.binary_value f8
from
    (select * from features_binary_input where feature_id = 1) f1,
    (select * from features_binary_input where feature_id = 2) f2,
    (select * from features_binary_input where feature_id = 5) f5,
    (select * from features_binary_input where feature_id = 6) f6,
    (select * from features_binary_input where feature_id = 7) f7,
    (select * from features_binary_input where feature_id = 8) f8
where
    f1.dtyymm = f2.dtyymm and
    f1.dtyymm = f5.dtyymm and
    f1.dtyymm = f6.dtyymm and
    f1.dtyymm = f7.dtyymm and
    f1.dtyymm = f8.dtyymm;