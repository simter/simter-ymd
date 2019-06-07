create table st_ymd (
  id    varchar(36) primary key,
  type  varchar(100) not null comment 'the belong type identity',
  year  smallint     not null comment '4 digits year, such as 2018',
  month smallint     not null default 0 comment 'month from 1 to 12. 0 means ignored',
  day   smallint     not null default 0 comment 'day from 1 to 31. 0 means ignored',
  constraint st_ymd_uid unique (type, year, month, day)
);