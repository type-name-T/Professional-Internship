-- DBUPDATE-033-0.SQL

alter table T_USER add column USE_IDDEPARTMENT_C varchar(36);

update T_CONFIG set CFG_VALUE_C = '33' where CFG_ID_C = 'DB_VERSION';
