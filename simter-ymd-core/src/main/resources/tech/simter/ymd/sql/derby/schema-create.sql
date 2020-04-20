create table st_ymd (
  id varchar(36)  primary key,
  t  varchar(100) not null,
  y  smallint     not null,
  m  smallint     not null default 0,
  d  smallint     not null default 0,
  constraint st_ymd_uid unique (t, y, m, d)
);
comment on table st_ymd is 'the date of massive data';
comment on column st_ymd.t is 'the belong type identity';
comment on column st_ymd.y is '4 digits year, such as 2018';
comment on column st_ymd.m is 'month from 1 to 12. 0 means ignored';
comment on column st_ymd.d is 'day from 1 to 31. 0 means ignored';
