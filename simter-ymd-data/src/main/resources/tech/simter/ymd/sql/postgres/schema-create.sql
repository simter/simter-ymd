/**
 * Create table script.
 * @author RJ
 */
create table st_ymd (
  id    serial primary key,
  type  varchar(50) not null,
  year  smallint    not null,
  month smallint    not null default 0,
  day   smallint    not null default 0
);
comment on table st_ymd is 'the date of massive data';
comment on column st_ymd.type is 'the belong type identity';
comment on column st_ymd.year is '4 digits year, such as 2018';
comment on column st_ymd.month is 'month from 1 to 12. 0 means ignored';
comment on column st_ymd.day is 'day from 1 to 31. 0 means ignored';