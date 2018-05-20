create table bin_experiments (
    dtyymmdd varchar(20),
    feature_id bigint,
    bin_value int,
    name varchar(20),
    bin_result int
);

insert into bin_experiments
    select fbi.*, i.name, i.bin_chg
      from features_binary_input fbi, d_indices i
     where fbi.dtyymmdd = i.dtyymmdd
       and i.name = 'WIG20';
