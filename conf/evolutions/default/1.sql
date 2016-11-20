# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table application_model (
  taskid                    bigint,
  applicantid               bigint)
;

create table message (
  messageid                 bigint not null,
  taskid                    bigint,
  senderid                  bigint,
  receiverid                bigint,
  mskind                    integer,
  text                      varchar(255),
  timestamp                 timestamp,
  constraint pk_message primary key (messageid))
;

create table registered_category (
  userid                    bigint,
  category                  varchar(255))
;

create table registrant (
  id                        bigint not null,
  username                  varchar(255),
  password                  varchar(255),
  screenname                varchar(255),
  prefecture                integer,
  age                       integer,
  introduction              varchar(255),
  constraint pk_registrant primary key (id))
;

create table task (
  taskid                    bigint not null,
  employerid                bigint,
  workerid                  bigint,
  title                     TEXT,
  introduction              TEXT,
  contractmoney             integer,
  status                    integer,
  category                  varchar(255),
  boshu_shuryo              timestamp,
  keiyaku_kakutei           timestamp,
  nouhin_kanryo             timestamp,
  boshu_kaishi              timestamp not null,
  constraint pk_task primary key (taskid))
;

create sequence message_seq;

create sequence registrant_seq;

create sequence task_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists application_model;

drop table if exists message;

drop table if exists registered_category;

drop table if exists registrant;

drop table if exists task;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists message_seq;

drop sequence if exists registrant_seq;

drop sequence if exists task_seq;

