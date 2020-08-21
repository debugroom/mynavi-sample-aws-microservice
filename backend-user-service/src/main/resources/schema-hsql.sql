DROP TABLE IF EXISTS CREDENTIAL;
DROP TABLE IF EXISTS USR;

-- 認証
CREATE TABLE CREDENTIAL
(
    -- ユーザID
    USER_ID bigint NOT NULL,
    -- 認証タイプ
    CREDENTIAL_TYPE varchar(512) NOT NULL,
    -- 認証キー
    CREDENTIAL_KEY varchar(512),
    -- 有効日時
    VALID_DATE timestamp,
    -- バージョン
    VER int,
    -- 最終更新日時
    LAST_UPDATED_DATE timestamp,
    PRIMARY KEY (USER_ID, CREDENTIAL_TYPE)
);

-- ユーザ
CREATE TABLE USR
(
	-- ユーザID
	USER_ID bigint NOT NULL,
	-- 名前
	FIRST_NAME varchar(255),
	-- 苗字
	FAMILY_NAME varchar(255),
	-- ログインID
	LOGIN_ID varchar(32) UNIQUE,
	-- ログイン中
	IS_LOGIN boolean,
    -- 管理者
    IS_ADMIN boolean,
	-- バージョン
	VER int,
	-- 最終更新日時
	LAST_UPDATED_AT timestamp,
	PRIMARY KEY (USER_ID)
);

ALTER TABLE CREDENTIAL
    ADD FOREIGN KEY (USER_ID)
        REFERENCES USR (USER_ID)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
;