create table st_ymd (
  id varchar(36)  not null,
  t  varchar(100) not null,
  y  smallint     not null,
  m  smallint     not null default 0,
  d  smallint     not null default 0,
  primary key (id),
  constraint st_ymd_uid unique (t, y, m, d)
);
