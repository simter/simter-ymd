create table st_ymd (
  id varchar(36)  primary key,
  t  varchar(100) not null           comment 'the belong type identity',
  y  smallint     not null           comment '4 digits year, such as 2018',
  m  smallint     not null default 0 comment 'month from 1 to 12. 0 means ignored',
  d  smallint     not null default 0 comment 'day from 1 to 31. 0 means ignored',
  constraint st_ymd_uid unique (t, y, m, d)
) comment = 'the date of massive data';
