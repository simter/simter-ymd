-- upgrade from 0.3.0 to 0.7.0

-- rename column name `type, year, month, day` to `t, y, m, d`
alter table st_ymd rename type to t;
alter table st_ymd rename year to y;
alter table st_ymd rename month to m;
alter table st_ymd rename day to d;