--------------------------------------------------------
--  File created - Thursday-October-18-2018   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table TASK
--------------------------------------------------------

  CREATE TABLE "PMTL2"."TASK" 
   (	"TASK_ID" NUMBER, 
	"ID_TASK_GROUP" NUMBER, 
	"STATUS" NUMBER(2,0), 
	"END_TIME" DATE, 
	"START_TIME" DATE, 
	"CREATE_TASK_CYCLE" NUMBER(6,0), 
	"NOTE_TASK" NVARCHAR2(2000)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
REM INSERTING into PMTL2.TASK
SET DEFINE OFF;
Insert into PMTL2.TASK (TASK_ID,ID_TASK_GROUP,STATUS,END_TIME,START_TIME,CREATE_TASK_CYCLE,NOTE_TASK) values (1,5,1,to_date('18-OCT-18','DD-MON-RR'),to_date('20-OCT-18','DD-MON-RR'),10,'cong viec chan vl');
Insert into PMTL2.TASK (TASK_ID,ID_TASK_GROUP,STATUS,END_TIME,START_TIME,CREATE_TASK_CYCLE,NOTE_TASK) values (3,1,1,to_date('18-NOV-18','DD-MON-RR'),to_date('18-OCT-18','DD-MON-RR'),47,null);
Insert into PMTL2.TASK (TASK_ID,ID_TASK_GROUP,STATUS,END_TIME,START_TIME,CREATE_TASK_CYCLE,NOTE_TASK) values (4,2,1,to_date('20-NOV-18','DD-MON-RR'),to_date('18-OCT-18','DD-MON-RR'),10,null);
--------------------------------------------------------
--  DDL for Index TASK_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PMTL2"."TASK_PK" ON "PMTL2"."TASK" ("TASK_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  Constraints for Table TASK
--------------------------------------------------------

  ALTER TABLE "PMTL2"."TASK" MODIFY ("TASK_ID" NOT NULL ENABLE);
 
  ALTER TABLE "PMTL2"."TASK" ADD CONSTRAINT "TASK_PK" PRIMARY KEY ("TASK_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
