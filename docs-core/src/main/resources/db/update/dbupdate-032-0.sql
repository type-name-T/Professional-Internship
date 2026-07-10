-- DBUPDATE-032-0.SQL

create cached table T_DEPARTMENT (DEP_ID_C varchar(36) not null, DEP_NAME_C varchar(100) not null, DEP_IDPARENT_C varchar(36), DEP_CODE_C varchar(50), DEP_SORTORDER_N int default 0, DEP_CREATEDATE_D datetime not null, DEP_DELETEDATE_D datetime, primary key (DEP_ID_C));

create cached table T_POSITION (POS_ID_C varchar(36) not null, POS_NAME_C varchar(100) not null, POS_LEVEL_N int default 1, POS_IDDEPARTMENT_C varchar(36), POS_CREATEDATE_D datetime not null, POS_DELETEDATE_D datetime, primary key (POS_ID_C));

create cached table T_DOC_CLASSIFICATION (DCL_ID_C varchar(36) not null, DCL_NAME_C varchar(50) not null, DCL_CODE_C varchar(20), DCL_SORTORDER_N int default 0, DCL_CREATEDATE_D datetime not null, DCL_DELETEDATE_D datetime, primary key (DCL_ID_C));

alter table T_DOCUMENT add column DOC_IDCLASSIFICATION_C varchar(36);
alter table T_DOCUMENT add column DOC_SECRECYLEVEL_C varchar(20) default 'INTERNAL';
alter table T_DOCUMENT add column DOC_URGENCY_C varchar(20) default 'NORMAL';
alter table T_DOCUMENT add column DOC_DOCNO_C varchar(100);
alter table T_DOCUMENT add column DOC_FROMUNIT_C varchar(200);
alter table T_DOCUMENT add column DOC_IDHANDLERDEPT_C varchar(36);
alter table T_DOCUMENT add column DOC_IDHANDLERUSER_C varchar(36);
alter table T_DOCUMENT add column DOC_DOCDATE_D datetime;
alter table T_DOCUMENT add column DOC_RETENTION_C varchar(20);
alter table T_DOCUMENT add column DOC_ARCHIVENO_C varchar(100);
alter table T_DOCUMENT add column DOC_STATUS_C varchar(20) default 'DRAFT';

insert into T_DOC_CLASSIFICATION values ('cls-001', '收文', 'RECEIVE', 1, now(), null);
insert into T_DOC_CLASSIFICATION values ('cls-002', '发文', 'SEND', 2, now(), null);
insert into T_DOC_CLASSIFICATION values ('cls-003', '会议纪要', 'MINUTES', 3, now(), null);
insert into T_DOC_CLASSIFICATION values ('cls-004', '通知公告', 'NOTICE', 4, now(), null);
insert into T_DOC_CLASSIFICATION values ('cls-005', '工作报告', 'REPORT', 5, now(), null);
insert into T_DOC_CLASSIFICATION values ('cls-006', '请示报告', 'REQUEST', 6, now(), null);

insert into T_DEPARTMENT values ('dept-001', '办公室', null, 'OFFICE', 1, now(), null);
insert into T_DEPARTMENT values ('dept-002', '综合科', 'dept-001', 'ZHGK', 1, now(), null);
insert into T_DEPARTMENT values ('dept-003', '文秘科', 'dept-001', 'WMK', 2, now(), null);
insert into T_DEPARTMENT values ('dept-004', '财务处', null, 'CWC', 2, now(), null);
insert into T_DEPARTMENT values ('dept-005', '人事处', null, 'RSC', 3, now(), null);

insert into T_POSITION values ('pos-001', '处长', 5, 'dept-004', now(), null);
insert into T_POSITION values ('pos-002', '副处长', 4, 'dept-004', now(), null);
insert into T_POSITION values ('pos-003', '科长', 3, 'dept-002', now(), null);
insert into T_POSITION values ('pos-004', '副科长', 2, 'dept-002', now(), null);
insert into T_POSITION values ('pos-005', '科员', 1, 'dept-002', now(), null);

update T_CONFIG set CFG_VALUE_C = '32' where CFG_ID_C = 'DB_VERSION';
