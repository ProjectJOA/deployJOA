CREATE TABLE TB_A001(S_NUM DECIMAL(30) NOT NULL, APP_NO DECIMAL(30) NOT NULL, MNG_ID VARCHAR(20) NULL)
ALTER TABLE PUBLIC.TB_A001 ADD PRIMARY KEY (S_NUM,APP_NO)

CREATE TABLE TB_B001(S_NUM DECIMAL(30) NOT NULL, B_NUM DECIMAL(30) NOT NULL, APP_STATE CHAR(2) NULL, WAS_RESTART_YN CHAR(1) NULL, DEPO_STATE CHAR(2) NULL, DEPO_TIME VARCHAR(14) NULL)
ALTER TABLE PUBLIC.TB_B001 ADD PRIMARY KEY (S_NUM,B_NUM)

CREATE TABLE TB_B002(S_NUM DECIMAL(30) NOT NULL, B_NUM DECIMAL(30) NOT NULL, B_SNUM DECIMAL(30) NOT NULL, FILE_CNT DECIMAL(5) NULL, DEV_COMMENT VARCHAR(1000) NULL, DEV_ID VARCHAR(20) NULL, SVN_REG_TIME VARCHAR(14) NULL )
ALTER TABLE PUBLIC.TB_B002 ADD PRIMARY KEY (S_NUM,B_NUM,B_SNUM)

CREATE TABLE TB_S001(S_NUM DECIMAL(30) NOT NULL, SYS_NM VARCHAR(100) NULL, SYS_DOMAIN VARCHAR(200) NULL, SYS_IP VARCHAR(20) NULL, APP_USE_YN CHAR(1) NULL, REG_DATE VARCHAR(14) NULL, REG_ID VARCHAR(20) NULL, UPDT_DATE VARCHAR(14) NULL, UPDT_ID VARCHAR(20) NULL)
ALTER TABLE PUBLIC.TB_S001 ADD PRIMARY KEY (S_NUM)

CREATE TABLE TB_S002(S_NUM DECIMAL(30) NOT NULL, VER_CONT_TYPE VARCHAR(5) NULL, REPOS_URL VARCHAR(200) NULL, VER_CONT_PORT VARCHAR(10) NULL, VER_CONT_USER_ID VARCHAR(20) NULL, VER_CONT_PWD VARCHAR(300) NULL, DEPO_PATH VARCHAR(300) NULL)
ALTER TABLE PUBLIC.TB_S002 ADD PRIMARY KEY (S_NUM)

CREATE TABLE TB_SB001(S_NUM DECIMAL(30) NOT NULL, B_NUM DECIMAL(30) NOT NULL, LAST_DEPO_VER DECIMAL(30) NULL, DEPO_STATE VARCHAR(4) NULL, DEPO_TIME VARCHAR(14) NULL)
ALTER TABLE PUBLIC.TB_SB001 ADD PRIMARY KEY (S_NUM,B_NUM)

CREATE TABLE TB_U001(MNG_ID VARCHAR(20) NOT NULL, PSWD VARCHAR(300) NULL, MNG_TYPE CHAR(2) NULL)
ALTER TABLE PUBLIC.TB_U001 ADD PRIMARY KEY (MNG_ID)