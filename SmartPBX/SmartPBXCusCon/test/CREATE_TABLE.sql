

DROP TABLE IF EXISTS ABSENCE_BEHAVIOR_INFO;
DROP TABLE IF EXISTS ACCOUNT_INFO;
DROP TABLE IF EXISTS CALL_HISTORY_INFO;
DROP TABLE IF EXISTS CALL_REGULATION_INFO;
DROP TABLE IF EXISTS INCOMING_GROUP_CHILD_NUMBER_INFO;
DROP TABLE IF EXISTS INCOMING_GROUP_INFO;
DROP TABLE IF EXISTS OUTSIDE_CALL_INCOMING_INFO;
DROP TABLE IF EXISTS OUTSIDE_CALL_SENDING_INFO;
DROP TABLE IF EXISTS EXTENSION_NUMBER_INFO;
DROP TABLE IF EXISTS INFOMATION_INFO;
DROP TABLE IF EXISTS OFFICE_CONSTRUCT_INFO;
DROP TABLE IF EXISTS TRAFFIC_INFO;
DROP TABLE IF EXISTS SITE_ADDRESS_INFO;
DROP TABLE IF EXISTS OUTSIDE_CALL_INFO;
DROP TABLE IF EXISTS RESERVED_CALL_NUMBER_INFO;
DROP TABLE IF EXISTS VM_TRANSFER_QUEUE_INFO;
DROP TABLE IF EXISTS PBX_FILE_BACKUP_INFO;
DROP TABLE IF EXISTS SERVER_RENEW_QUEUE_INFO;
DROP TABLE IF EXISTS VM_INFO;
DROP TABLE IF EXISTS SO_CONTROL_INFO;
DROP TABLE IF EXISTS SO_INFO;
DROP TABLE IF EXISTS VM_RESOURCE_TYPE_MASTER;
DROP TABLE IF EXISTS N_NUMBER_INFO;
DROP TABLE IF EXISTS EXTERNAL_GW_CONNECT_CHOICE_INFO;
DROP TABLE IF EXISTS MAC_ADDRESS_INFO;
DROP TABLE IF EXISTS MUSIC_INFO;



/* Drop Sequences */

DROP SEQUENCE IF EXISTS SO_SEND_ID_SEQ;
DROP SEQUENCE IF EXISTS ADDITIONAL_ORDER_SEQ;




/* Create Sequences */

-- SO電文の送信時に使用する送信番号。
CREATE SEQUENCE SO_SEND_ID_SEQ INCREMENT 1 MINVALUE 1 MAXVALUE 9999999999 START 1 CYCLE;



/* Create Tables */

-- 不在時の動作についての情報。
CREATE TABLE ABSENCE_BEHAVIOR_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	ABSENCE_BEHAVIOR_INFO_ID SERIAL NOT NULL UNIQUE,
	-- 不在時動作設定対象の内線番号のID。
	EXTENSION_NUMBER_INFO_ID INT NOT NULL,
	-- 不在時の動作のタイプを指定する。
	-- 　1：転送／留守番電話設定
	-- 　2：シングルナンバーリーチ設定
	ABSENCE_BEHAVIOR_TYPE INT NOT NULL,
	-- 転送先電話番号。
	-- 不在時動作タイプが1（転送／留守番電話設定）の時に使用する。
	-- 暗号化した状態で保持する。
	FORWARD_PHONE_NUMBER VARCHAR(128),
	-- 転送動作（無条件）のタイプを指定する。
	-- 不在時動作タイプが1（転送／留守番電話設定）の時に使用する。
	-- 　1：転送
	-- 　2：留守番電話
	-- 　3：設定しない
	FORWARD_BEHAVIOR_TYPE_UNCONDITIONAL INT,
	-- 転送動作（話中）のタイプを指定する。
	-- 不在時動作タイプが1（転送／留守番電話設定）の時に使用する。
	-- 　1：転送
	-- 　2：留守番電話
	-- 　3：設定しない
	FORWARD_BEHAVIOR_TYPE_BUSY INT,
	-- 転送動作（圏外）のタイプを指定する。
	-- 不在時動作タイプが1（転送／留守番電話設定）の時に使用する。
	-- 　1：転送
	-- 　2：留守番電話
	-- 　3：設定しない
	FORWARD_BEHAVIOR_TYPE_OUTSIDE INT,
	-- 転送動作（無応答）のタイプを指定する。
	-- 不在時動作タイプが1（転送／留守番電話設定）の時に使用する。
	-- 　1：転送
	-- 　2：留守番電話
	-- 　3：設定しない
	FORWARD_BEHAVIOR_TYPE_NO_ANSWER INT,
	-- 無応答の場合の着信呼出時間。
	-- 不在時動作タイプが1（転送／留守番電話設定）の時に使用する。
	CALL_TIME INT,
	-- 接続先番号1。
	-- 不在時動作タイプが2（シングルナンバーリーチ設定）の時に使用する。
	-- 暗号化した状態で保持する。
	CONNECT_NUMBER_1 VARCHAR(128),
	-- 接続先番号2。
	-- 不在時動作タイプが2（シングルナンバーリーチ設定）の時に使用する。
	-- 暗号化した状態で保持する。
	CONNECT_NUMBER_2 VARCHAR(128),
	-- 接続先番号1の呼出開始時間。
	-- 不在時動作タイプが2（シングルナンバーリーチ設定）の時に使用する。
	CALL_START_TIME_1 INT,
	-- 接続先番号2の呼出開始時間。
	-- 不在時動作タイプが2（シングルナンバーリーチ設定）の時に使用する。
	CALL_START_TIME_2 INT,
	-- 呼出終了時間
	-- 不在時動作タイプが2（シングルナンバーリーチ設定）の時に使用する。
	CALL_END_TIME INT,
	-- 留守番電話フラグ。
	-- 不在時動作タイプが2（シングルナンバーリーチ設定）の時に使用する。
	-- 　false：off
	-- 　true：on
	ANSWERPHONE_FLAG BOOLEAN,
	-- 留守番電話を聞く時に必要なパスワード。
	-- 暗号化した状態で保持する。
	ANSWERPHONE_PASSWORD VARCHAR(128),
	-- 最後に更新を実施したアカウントのアカウント情報ID。
	-- SOなど、カスコン画面からの更新でない場合は、0 を設定する。
	LAST_UPDATE_ACCOUNT_INFO_ID INT DEFAULT 0 NOT NULL,
	-- 最後に更新が実施された日時。
	LAST_UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	-- 論理削除を示すフラグ。
	-- 　false：有効データ
	-- 　true：削除データ
	DELETE_FLAG BOOLEAN DEFAULT 'false' NOT NULL,
	PRIMARY KEY (ABSENCE_BEHAVIOR_INFO_ID)
) ;


-- カスコン画面にログインするために必要なアカウント情報。
CREATE TABLE ACCOUNT_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	ACCOUNT_INFO_ID SERIAL NOT NULL UNIQUE,
	-- ユーザがログイン時に指定するID
	LOGIN_ID VARCHAR(16) NOT NULL,
	-- ユーザがログイン時に指定するパスワード。
	-- 暗号化した状態で保持する。
	PASSWORD VARCHAR(256) NOT NULL,
	-- アカウントが所属するN番のID。
	-- 
	N_NUMBER_INFO_ID INT,
	-- アカウント種別。
	-- 　1:オペレータ
	-- 　　※本システムのシステムオペレータ
	-- 　　　内線番号には紐づかない
	-- 　2:ユーザ管理者
	-- 　　※各契約（N番ごと）のユーザ管理者
	-- 　　　内線番号には紐づかない
	-- 　3:端末ユーザ
	-- 　　※個々のエンドユーザ
	-- 　　　内線番号には紐づく
	-- 
	ACCOUNT_TYPE INT NOT NULL,
	-- 該当アカウントの内線番号のID。
	EXTENSION_NUMBER_INFO_ID INT,
	-- パスワードの有効期限。
	PASSWORD_LIMIT TIMESTAMP NOT NULL,
	-- ログイン時にパスワード間違いを一定回数以上した場合に、ログイン不可（ロック）にするためのフラグ。
	-- 　false：ログイン可能
	-- 　true：ログイン不可
	LOCK_FLAG BOOLEAN DEFAULT 'false' NOT NULL,
	-- パスワード変更履歴1。
	-- 暗号化した状態で保持する。
	PASSWORD_HISTORY_1 VARCHAR(256),
	-- パスワード変更履歴2。
	-- 暗号化した状態で保持する。
	PASSWORD_HISTORY_2 VARCHAR(256),
	-- パスワード変更履歴3。
	-- 暗号化した状態で保持する。
	PASSWORD_HISTORY_3 VARCHAR(256),
	-- SOから作成されたアカウントか、カスコン画面から追加されたアカウントかを示すフラグ。
	-- SO新設時に作成されたアカウントは、カスコン画面から削除できない。
	-- 　false：SOから作成されたアカウント
	-- 　true：カスコン画面から追加されたアカウント
	ADD_ACCOUNT_FLAG BOOLEAN DEFAULT 'false' NOT NULL,
	-- 最後に更新を実施したアカウントのアカウント情報ID。
	-- SOなど、カスコン画面からの更新でない場合は、0 を設定する。
	LAST_UPDATE_ACCOUNT_INFO_ID INT DEFAULT 0 NOT NULL,
	-- 最後に更新が実施された日時。
	LAST_UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	-- 論理削除を示すフラグ。
	-- 　false：有効データ
	-- 　true：削除データ
	DELETE_FLAG BOOLEAN DEFAULT 'false' NOT NULL,
	PRIMARY KEY (ACCOUNT_INFO_ID)
) ;




-- 通話ログ情報を保持する。
-- AsteriskのCDRで取得した情報を保持する。
CREATE TABLE CALL_HISTORY_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	CALL_HISTORY_INFO_ID SERIAL NOT NULL UNIQUE,
	-- どのN番の通話ログかを示すID。
	-- 
	N_NUMBER_INFO_ID INT NOT NULL,
	-- 発信者の電話番号。
	CALLER_PHONE_NUMBER VARCHAR(32),
	-- 着信日時。
	CALL_DATE TIMESTAMP,
	-- 通話開始日時。
	TALK_START_DATE TIMESTAMP,
	-- 通話終了日時。
	TALK_END_DATE TIMESTAMP,
	-- 着信者の電話番号。
	CALLEE_PHONE_NUMBER VARCHAR(32),
	-- 通話時間。単位は秒。
	TALK_TIME INT,
	-- 通話ステータス。
	TALK_STATUS VARCHAR(16),
	-- 通話種別。
	TALK_TYPE VARCHAR(16),
	-- 最後に更新を実施したアカウントのアカウント情報ID。
	-- SOなど、カスコン画面からの更新でない場合は、0 を設定する。
	LAST_UPDATE_ACCOUNT_INFO_ID INT DEFAULT 0 NOT NULL,
	-- 最後に更新が実施された日時。
	LAST_UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	-- 論理削除を示すフラグ。
	-- 　false：有効データ
	-- 　true：削除データ
	DELETE_FLAG BOOLEAN DEFAULT 'false' NOT NULL,
	PRIMARY KEY (CALL_HISTORY_INFO_ID)
) ;


-- 発信規制先情報。
-- N番号ごとに設定される。
CREATE TABLE CALL_REGULATION_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	CALL_REGULATION_INFO_ID SERIAL NOT NULL UNIQUE,
	-- 発信規制先の設定対象のN番を示すID。
	N_NUMBER_INFO_ID INT NOT NULL,
	-- 規制対象番号1。
	-- 暗号化した状態で保持する。
	CALL_REGULATION_NUMBER_1 VARCHAR(128),
	-- 規制対象番号2。
	-- 暗号化した状態で保持する。
	CALL_REGULATION_NUMBER_2 VARCHAR(128),
	-- 規制対象番号3。
	-- 暗号化した状態で保持する。
	CALL_REGULATION_NUMBER_3 VARCHAR(128),
	-- 規制対象番号4。
	-- 暗号化した状態で保持する。
	CALL_REGULATION_NUMBER_4 VARCHAR(128),
	-- 規制対象番号5。
	-- 暗号化した状態で保持する。
	CALL_REGULATION_NUMBER_5 VARCHAR(128),
	-- 規制対象番号6。
	-- 暗号化した状態で保持する。
	CALL_REGULATION_NUMBER_6 VARCHAR(128),
	-- 規制対象番号7。
	-- 暗号化した状態で保持する。
	CALL_REGULATION_NUMBER_7 VARCHAR(128),
	-- 規制対象番号8。
	-- 暗号化した状態で保持する。
	CALL_REGULATION_NUMBER_8 VARCHAR(128),
	-- 規制対象番号9。
	-- 暗号化した状態で保持する。
	CALL_REGULATION_NUMBER_9 VARCHAR(128),
	-- 規制対象番号10。
	-- 暗号化した状態で保持する。
	CALL_REGULATION_NUMBER_10 VARCHAR(128),
	-- 規制対象番号11。
	-- 暗号化した状態で保持する。
	CALL_REGULATION_NUMBER_11 VARCHAR(128),
	-- 規制対象番号12。
	-- 暗号化した状態で保持する。
	CALL_REGULATION_NUMBER_12 VARCHAR(128),
	-- 規制対象番号13。
	-- 暗号化した状態で保持する。
	CALL_REGULATION_NUMBER_13 VARCHAR(128),
	-- 規制対象番号14。
	-- 暗号化した状態で保持する。
	CALL_REGULATION_NUMBER_14 VARCHAR(128),
	-- 規制対象番号15。
	-- 暗号化した状態で保持する。
	CALL_REGULATION_NUMBER_15 VARCHAR(128),
	-- 規制対象番号16。
	-- 暗号化した状態で保持する。
	CALL_REGULATION_NUMBER_16 VARCHAR(128),
	-- 規制対象番号17。
	-- 暗号化した状態で保持する。
	CALL_REGULATION_NUMBER_17 VARCHAR(128),
	-- 規制対象番号18。
	-- 暗号化した状態で保持する。
	CALL_REGULATION_NUMBER_18 VARCHAR(128),
	-- 規制対象番号19。
	-- 暗号化した状態で保持する。
	CALL_REGULATION_NUMBER_19 VARCHAR(128),
	-- 規制対象番号20。
	-- 暗号化した状態で保持する。
	CALL_REGULATION_NUMBER_20 VARCHAR(128),
	-- 最後に更新を実施したアカウントのアカウント情報ID。
	-- SOなど、カスコン画面からの更新でない場合は、0 を設定する。
	LAST_UPDATE_ACCOUNT_INFO_ID INT DEFAULT 0 NOT NULL,
	-- 最後に更新が実施された日時。
	LAST_UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	-- 論理削除を示すフラグ。
	-- 　false：有効データ
	-- 　true：削除データ
	DELETE_FLAG BOOLEAN DEFAULT 'false' NOT NULL,
	PRIMARY KEY (CALL_REGULATION_INFO_ID)
) ;


-- 内線番号情報。
CREATE TABLE EXTENSION_NUMBER_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	EXTENSION_NUMBER_INFO_ID SERIAL NOT NULL UNIQUE,
	-- 内線番号が属しているN番を示すID。
	-- 
	N_NUMBER_INFO_ID INT NOT NULL,
	-- 内線番号。
	-- 2～22桁。
	EXTENSION_NUMBER VARCHAR(32) NOT NULL,
	-- 拠点番号。
	-- 1～11桁。
	-- 端末種別が3（VoIP-GW）の場合は、拠点番号のみで端末番号はNULL。
	LOCATION_NUMBER VARCHAR(16) NOT NULL,
	-- 端末番号。
	-- 1～11桁。
	-- 端末種別が3（VoIP-GW）の場合は、拠点番号のみで端末番号はNULL。
	TERMINAL_NUMBER VARCHAR(16),
	-- 端末種別。
	-- 　0：IP Phone
	-- 　1：スマートフォン
	-- 　2：ソフトフォン
	-- 　3：VoIP-GW
	-- 　4：VoIP-GW（拠点RTなし）
	-- 
	TERMINAL_TYPE INT NOT NULL,
	-- 端末の提供形態。
	-- 　1:自営
	-- 　2:アプリ提供
	-- 　3:お買い上げ
	-- 　4:レンタル
	SUPPLY_TYPE INT,
	-- 内線番号に紐づく端末が設置される場所を示すID。
	SITE_ADDRESS_INFO_ID INT,
	-- 内線ID。
	-- 暗号化した状態で保持する。
	EXTENSION_ID VARCHAR(256) NOT NULL,
	-- 内線パスワード。
	-- 暗号化した状態で保持する。
	EXTENSION_PASSWORD VARCHAR(256) NOT NULL,
	-- 同じ拠点番号で、VoIP-GWを複数台利用する場合の、何台目かを示す値。
	LOCATION_NUM_MULTI_USE INT,
	-- VoIP-GWが持っているチャネルの数。
	EXTRA_CHANNEL INT,
	-- 外線発信設定済みか否かを示すフラグ。
	-- 　false：未設定
	-- 　true：設定済み
	OUTBOUND_FLAG BOOLEAN NOT NULL,
	-- 不在時動作が設定済みか否かを示すフラグ
	-- 　false：未設定
	-- 　true：設定済み
	ABSENCE_FLAG BOOLEAN NOT NULL,
	-- 発信規制が設定済みか否かを示すフラグ
	-- 　false：未設定
	-- 　true：設定済み
	CALL_REGULATION_FLAG BOOLEAN NOT NULL,
	-- 端末自動設定フラグを実施するか否かを示すフラグ
	-- ※Step1では使用しない。
	-- 　false：実施しない
	-- 　true：実施する
	AUTOMATIC_SETTING_FLAG BOOLEAN,
	--"端末自動設定の接続種別を示す値。
	--0：インタネット
	--1：VPN"
	AUTO_SETTING_TYPE INT,
	-- 端末のMACアドレス。
	-- 暗号化した状態で保持する。
	-- ※Step1では使用しない。
	TERMINAL_MAC_ADDRESS VARCHAR(256),
	--"VoIP-GWの回線種別を示す値。
	--0：Arcstar Universal One
	--1：IP-VPN
	--2：eVLAN
	--3：GroupVPN"
	VPN_ACCESS_TYPE INT,
	--VPN拠点単位に付与されているVPN拠点N番情報。
	VPN_LOCATION_N_NUMBER VARCHAR(16),
	-- SO工事予約を示すフラグ。
	-- 　false：予約なし
	-- 　true：予約あり
	-- true(予約あり)の場合、該当レコードはカスコン画面からの操作は不可となる。
	-- SO工事実施後、false(予約なし)に変更される。
	SO_ACTIVATION_RESERVE_FLAG BOOLEAN DEFAULT 'false' NOT NULL,
	-- 最後に更新を実施したアカウントのアカウント情報ID。
	-- SOなど、カスコン画面からの更新でない場合は、0 を設定する。
	LAST_UPDATE_ACCOUNT_INFO_ID INT DEFAULT 0 NOT NULL,
	-- 最後に更新が実施された日時。
	LAST_UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	-- 論理削除を示すフラグ。
	-- 　false：有効データ
	-- 　true：削除データ
	DELETE_FLAG BOOLEAN DEFAULT 'false' NOT NULL,
	PRIMARY KEY (EXTENSION_NUMBER_INFO_ID)
) ;


-- 着信グループに属している、内線番号の一覧。
CREATE TABLE INCOMING_GROUP_CHILD_NUMBER_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	INCOMING_GROUP_CHILD_NUMBER_INFO_ID SERIAL NOT NULL UNIQUE,
	-- 本テーブルで定義している子番号が属している着信グループを示すID。
	INCOMING_GROUP_INFO_ID INT NOT NULL,
	-- 着信する順番。
	INCOMING_ORDER INT NOT NULL,
	-- 着信グループに属している内線番号情報ID。
	EXTENSION_NUMBER_INFO_ID INT NOT NULL,
	PRIMARY KEY (INCOMING_GROUP_CHILD_NUMBER_INFO_ID)
) ;


-- 着信グループの設定情報。
CREATE TABLE INCOMING_GROUP_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	INCOMING_GROUP_INFO_ID SERIAL NOT NULL UNIQUE,
	-- 着信グループの名前。
	INCOMING_GROUP_NAME VARCHAR(128) NOT NULL,
	-- 着信グループが属するＮ番を示すID。
	N_NUMBER_INFO_ID INT NOT NULL,
	-- 着信グループの代表番号を示すID。
	EXTENSION_NUMBER_INFO_ID INT,
	-- 着信グループの呼出方式。
	-- 　1：順次着信
	-- 　2：一斉着信
	-- 　3：コールピックアップ
	GROUP_CALL_TYPE INT NOT NULL,
	-- 呼出方式を1（順次着信）とした時に、グループ内の端末がビジー・故障・圏外（未レジ）・無応答・端末着信拒否だった場合に、グループ内の次の端末を着信状態にするまでの時間。単位は秒。
	NO_ANSWER_TIMER INT,
	-- 最後に更新を実施したアカウントのアカウント情報ID。
	-- SOなど、カスコン画面からの更新でない場合は、0 を設定する。
	LAST_UPDATE_ACCOUNT_INFO_ID INT DEFAULT 0 NOT NULL,
	-- 最後に更新が実施された日時。
	LAST_UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	-- 論理削除を示すフラグ。
	-- 　false：有効データ
	-- 　true：削除データ
	DELETE_FLAG BOOLEAN DEFAULT 'false' NOT NULL,
	PRIMARY KEY (INCOMING_GROUP_INFO_ID)
) ;


-- オペレータによって登録されるお知らせ情報。
-- カスコン画面に表示される。
CREATE TABLE INFOMATION_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	INFOMATION_INFO_ID SERIAL NOT NULL UNIQUE,
	-- お知らせ情報の本文。
	INFOMATION_INFO VARCHAR(1024) NOT NULL,
	-- 最後に更新を実施したアカウントのアカウント情報ID。
	-- SOなど、カスコン画面からの更新でない場合は、0 を設定する。
	LAST_UPDATE_ACCOUNT_INFO_ID INT DEFAULT 0 NOT NULL,
	-- 最後に更新が実施された日時。
	LAST_UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	-- 論理削除を示すフラグ。
	-- 　false：有効データ
	-- 　true：削除データ
	DELETE_FLAG BOOLEAN DEFAULT 'false' NOT NULL,
	PRIMARY KEY (INFOMATION_INFO_ID)
) ;


-- 契約ごとに上位SOが払い出すN番情報。
-- 上位SOからAPIによって通知される。
CREATE TABLE N_NUMBER_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	N_NUMBER_INFO_ID SERIAL NOT NULL UNIQUE,
	-- 契約者ごとに付与される番号。
	-- 上位装置よりSOにて通知される。
	N_NUMBER_NAME VARCHAR(16) NOT NULL,
	-- 拠点番号の桁数。
	SITE_DIGIT INT NOT NULL,
	-- 端末番号の桁数。
	TERMINAL_DIGIT INT NOT NULL,
	-- Asteriskのダイアルプランで使用するチャネル数。
	CH_NUM INT NOT NULL,
	-- 外線Prefix設定。
	-- 　1：0+外線番号で発信
	-- 　2：外線番号のみで発信
	OUTSIDE_CALL_PREFIX INT NOT NULL,
	-- SIP-IDに使用する文字列。
	-- SIP-IDは内線ランダム文字列+内線番号で生成される。
	EXTENSION_RANDOM_WORD VARCHAR(32) NOT NULL UNIQUE,
	-- チュートリアルが完了しているか否かを示すフラグ。
	-- 　false：チュートリアル未完了
	-- 　true ：チュートリアル完了済み
	TUTORIAL_FLAG BOOLEAN DEFAULT 'false' NOT NULL,
		-- 保留音に、デフォルト音を使うか、カスコン画面から登録した音を使用するかを示すフラグ。
	-- 　false：デフォルトの保留音を利用する。
	-- 　true ：個別の保留音を利用する。
	MUSIC_HOLD_FLAG BOOLEAN DEFAULT 'false' NOT NULL,
	-- 最後に更新を実施したアカウントのアカウント情報ID。
	-- SOなど、カスコン画面からの更新でない場合は、0 を設定する。
	LAST_UPDATE_ACCOUNT_INFO_ID INT DEFAULT 0 NOT NULL,
	-- 最後に更新が実施された日時。
	LAST_UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	-- 論理削除を示すフラグ。
	-- 　false：有効データ
	-- 　true：削除データ
	DELETE_FLAG BOOLEAN DEFAULT 'false' NOT NULL,
	PRIMARY KEY (N_NUMBER_INFO_ID)
) ;


-- オフィス構築セットの申し込み拠点情報。
CREATE TABLE OFFICE_CONSTRUCT_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	OFFICE_CONSTRUCT_INFO_ID SERIAL NOT NULL UNIQUE,
	-- オフィス構築セット情報が属しているN番のID。
	N_NUMBER_INFO_ID INT NOT NULL,
	-- オフィス構築セット情報を管理する番号。
	MANAGE_NUMBER VARCHAR(128) NOT NULL,
	-- オフィス構築セットの申し込み拠点情報の拠点名。
	-- 暗号化して保持する。
	LOCATION_NAME VARCHAR(256) NOT NULL,
	-- オフィス構築セットの申し込み拠点情報の拠点住所。
	-- 暗号化して保持する。
	LOCATION_ADDRESS VARCHAR(1024) NOT NULL,
	-- オフィス構築セットの申し込み拠点情報の外線情報。
	OUTSIDE_INFO VARCHAR(1024),
	-- オフィス構築セットの申し込み拠点情報の備考欄。
	MEMO VARCHAR(1024),
	-- 最後に更新を実施したアカウントのアカウント情報ID。
	-- SOなど、カスコン画面からの更新でない場合は、0 を設定する。
	LAST_UPDATE_ACCOUNT_INFO_ID INT DEFAULT 0 NOT NULL,
	-- 最後に更新が実施された日時。
	LAST_UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	PRIMARY KEY (OFFICE_CONSTRUCT_INFO_ID)
) ;


-- 外線着信時の設定情報。
CREATE TABLE OUTSIDE_CALL_INCOMING_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	OUTSIDE_CALL_INCOMING_INFO_ID SERIAL NOT NULL UNIQUE,
	-- 外線着信時の外線情報。
	OUTSIDE_CALL_INFO_ID INT NOT NULL,
	-- 外線着信時の着信先の内線番号ID。
	-- 内線番号情報IDと着信グループ情報IDのいずれか一方のカラムが設定されていること。
	-- 
	EXTENSION_NUMBER_INFO_ID INT,
	-- 外線着信先がVoIP-GWであった場合に登録される端末番号。
	-- VoIP-GWの場合には、内線番号情報の端末番号がないため、内線番号情報の拠点番号と本カラムの値とで、外線着信先の内線番号となる。
	VOIPGW_INCOMING_TERMINAL_NUMBER VARCHAR(16),
	-- 最後に更新を実施したアカウントのアカウント情報ID。
	-- SOなど、カスコン画面からの更新でない場合は、0 を設定する。
	LAST_UPDATE_ACCOUNT_INFO_ID INT DEFAULT 0 NOT NULL,
	-- 最後に更新が実施された日時。
	LAST_UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	-- 論理削除を示すフラグ。
	-- 　false：有効データ
	-- 　true：削除データ
	DELETE_FLAG BOOLEAN DEFAULT 'false' NOT NULL,
	PRIMARY KEY (OUTSIDE_CALL_INCOMING_INFO_ID)
) ;


-- N番が持っている外線の情報。
CREATE TABLE OUTSIDE_CALL_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	OUTSIDE_CALL_INFO_ID SERIAL NOT NULL UNIQUE,
	-- 外線の持ち主であるN番のN番情報ID。
	N_NUMBER_INFO_ID INT NOT NULL,
	-- 外線のサービス種別。
	-- 　1：050plus for Biz
	-- 　2：IP Voice for SmartPBX
	-- 　3：自営SIPサーバ(1)
	-- 　4：自営SIPサーバ(2)
	OUTSIDE_CALL_SERVICE_TYPE INT NOT NULL,
	-- 外線のアクセス回線の種別。
	-- 　1：OCN/提携ISP
	-- 　2：非提携ISP
	OUTSIDE_CALL_LINE_TYPE INT,
	-- 外線番号。
	OUTSIDE_CALL_NUMBER VARCHAR(32) NOT NULL,
	-- 外線サービス種別が 3（自前SIPサーバ）の場合、本システムの内線サーバが自前SIPサーバに対してRegistする時に使用する番号。
	-- 自前SIPサーバが払い出した内線番号。
	SIP_CVT_REGIST_NUMBER VARCHAR(32),
	-- 外線が基本か追加かを示すフラグ。
	-- 　false：基本
	-- 　true：追加
	ADD_FLAG BOOLEAN NOT NULL,
	-- SIP-IP。
	-- 暗号化した状態で保持する。
	SIP_ID VARCHAR(256) NOT NULL,
	-- SIPパスワード。
	-- 暗号化した状態で保持する。
	SIP_PASSWORD VARCHAR(256) NOT NULL,
	-- 外線サーバのアドレス。
	-- 暗号化した状態で保持する。
	SERVER_ADDRESS VARCHAR(512),
	-- ポート番号。
	PORT_NUMBER INT,
	-- 最後に更新を実施したアカウントのアカウント情報ID。
	-- SOなど、カスコン画面からの更新でない場合は、0 を設定する。
	LAST_UPDATE_ACCOUNT_INFO_ID INT DEFAULT 0 NOT NULL,
	-- 最後に更新が実施された日時。
	LAST_UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	-- 論理削除を示すフラグ。
	-- 　false：有効データ
	-- 　true：削除データ
	DELETE_FLAG BOOLEAN DEFAULT 'false' NOT NULL,
	EXTERNAL_GW_PRIVATE_IP VARCHAR(128),
	PRIMARY KEY (OUTSIDE_CALL_INFO_ID)
) ;


-- 外線発信時の設定情報。
CREATE TABLE OUTSIDE_CALL_SENDING_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	OUTSIDE_CALL_SENDING_INFO_ID SERIAL NOT NULL UNIQUE,
	-- 外線への発信を設定する内線のID。
	EXTENSION_NUMBER_INFO_ID INT NOT NULL,
	-- 外線への発信する時に使用する外線のID。
	OUTSIDE_CALL_INFO_ID INT,
	-- 最後に更新を実施したアカウントのアカウント情報ID。
	-- SOなど、カスコン画面からの更新でない場合は、0 を設定する。
	LAST_UPDATE_ACCOUNT_INFO_ID INT DEFAULT 0 NOT NULL,
	-- 最後に更新が実施された日時。
	LAST_UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	-- 論理削除を示すフラグ。
	-- 　false：有効データ
	-- 　true：削除データ
	DELETE_FLAG BOOLEAN DEFAULT 'false' NOT NULL,
	PRIMARY KEY (OUTSIDE_CALL_SENDING_INFO_ID)
) ;


-- 内線サーバの最新1世代分の設定ファイルを保持する。
CREATE TABLE PBX_FILE_BACKUP_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	PBX_FILE_BACKUP_INFO_ID SERIAL NOT NULL UNIQUE,
	-- バックアップ対象の内線サーバのVM情報ID。
	VM_INFO_ID INT NOT NULL,
	-- 内線サーバ設定ファイルのファイルパスとファイル名の組み合わせ。
	-- ファイルパスは、内線サーバのベースディレクトリからの相対パスとする。
	PBX_FILE_PATH VARCHAR(256) NOT NULL,
	-- 内線サーバの設定ファイルの内容。
	PBX_FILE_DATA TEXT NOT NULL,
	-- 最後に更新を実施したアカウントのアカウント情報ID。
	-- SOなど、カスコン画面からの更新でない場合は、0 を設定する。
	LAST_UPDATE_ACCOUNT_INFO_ID INT DEFAULT 0 NOT NULL,
	-- 最後に更新が実施された日時。
	LAST_UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	PRIMARY KEY (PBX_FILE_BACKUP_INFO_ID),
	CONSTRAINT ukey_pbx_file_backup_info UNIQUE (VM_INFO_ID, PBX_FILE_PATH)
) ;


-- 予約された電話番号（内線番号もしくは外線番号）の情報。
-- 工事にて、該当の電話番号（内線番号もしくは外線番号）が処理されたのち、本テーブルの該当レコードは物理削除する。
CREATE TABLE RESERVED_CALL_NUMBER_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	RESERVED_CALL_NUMBER_INFO_ID SERIAL NOT NULL UNIQUE,
	-- 予約された電話番号（内線番号もしくは外線番号）。
	RESERVED_CALL_NUMBER VARCHAR(32) NOT NULL,
	-- 電話番号（内線番号もしくは外線番号）を予約する対象のN番。
	N_NUMBER_INFO_ID INT NOT NULL,
	PRIMARY KEY (RESERVED_CALL_NUMBER_INFO_ID)
) ;


-- 端末設置場所の住所等の情報。
CREATE TABLE SITE_ADDRESS_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	SITE_ADDRESS_INFO_ID SERIAL NOT NULL UNIQUE,
	-- 拠点が所属するN番のID。
	N_NUMBER_INFO_ID INT NOT NULL,
	-- 拠点を識別するID。
	-- 同じN番の中では一意となる。
	-- 
	LOCATION_ID VARCHAR(16) NOT NULL,
	-- 端末の設置場所の拠点名。
	LOCATION_NAME VARCHAR(128) NOT NULL,
	-- 端末の設置場所の郵便番号。
	-- 暗号化して保持する。
	ZIP_CODE VARCHAR(32),
	-- 端末の設置場所の住所。
	-- 暗号化して保持する。
	ADDRESS VARCHAR(256),
	-- 端末の設置場所のビル、マンション名。
	-- 暗号化して保持する。
	BUILDING_NAME VARCHAR(256),
	-- 端末の設置場所の担当者。
	-- 暗号化して保持する。
	SUPPORT_STAFF VARCHAR(256),
	-- 端末の設置場所の連絡先。
	-- 暗号化して保持する。
	CONTACT_INFO VARCHAR(256),
	-- 最後に更新を実施したアカウントのアカウント情報ID。
	-- SOなど、カスコン画面からの更新でない場合は、0 を設定する。
	LAST_UPDATE_ACCOUNT_INFO_ID INT DEFAULT 0 NOT NULL,
	-- 最後に更新が実施された日時。
	LAST_UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	-- 論理削除を示すフラグ。
	-- 　false：有効データ
	-- 　true：削除データ
	DELETE_FLAG BOOLEAN DEFAULT 'false' NOT NULL,
	PRIMARY KEY (SITE_ADDRESS_INFO_ID)
) ;


-- SO規制対象のN番を保持する。
-- SO規制対象となっているN番は、規制されている間、SO電文の受信を受け付けない。
-- 規制解除する場合には、本テーブルの該当レコードを物理削除する。
CREATE TABLE SO_CONTROL_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	SO_CONTROL_INFO_ID SERIAL NOT NULL UNIQUE,
	-- SO規制対象とするN番。
	-- 
	N_NUMBER_NAME VARCHAR(16) NOT NULL UNIQUE,
	PRIMARY KEY (SO_CONTROL_INFO_ID)
) ;


-- 上位装置から通知されるSO情報。
CREATE TABLE SO_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	SO_INFO_ID SERIAL NOT NULL UNIQUE,
	-- SO通知にて、通知されるSO番号。
	-- SO情報取得時に、使用する。
	SO_NUMBER VARCHAR(16),
	-- SO通知にて、通知されるSO番号の枝番。
	-- SO情報取得時に、使用する。
	SO_BRANCH_NUMBER VARCHAR(16),
	-- SO通知にて、通知されるWebエントリID。
	-- ※物理名と論理名で整合性が取れていないが仕様である。
	SO_NUMBER_RESERVED VARCHAR(16) NOT NULL UNIQUE,
	-- SO通知にて取得できる商品ツリーコード。
	COMM_TREE_CODE VARCHAR(8) NOT NULL,
	-- SO通知にて取得できるN番。
	N_NUMBER VARCHAR(16) NOT NULL,
	-- SO種別。
	-- 　1:新設
	-- 　2:変更(追加)
	-- 　3:廃止
	-- 　4:SO記載変（使わない）
	-- 　5:SO取消
	-- 　6:変更(変更)
	-- 　7:変更(削除)
	SO_TYPE VARCHAR(1) NOT NULL,
	-- SO通知を受信した日時。
	RECEIVE_DATE TIMESTAMP NOT NULL,
	-- 工事日時。
	ACTIVATION_DATE TIMESTAMP,
	-- SO処理のステータスを示す。
	-- 　1：SO通知受信成功（SO情報未取得）
	-- 　2：SO通知受信成功→SO情報取得成功（処理結果未通知）
	-- 　3：SO通知受信成功→SO情報取得成功→処理結果通知成功（工事未実施）
	-- 　4：SO通知受信成功→SO情報取得成功→処理結果通知成功→工事実施成功（処理結果未通知）
	-- 　5：SO通知受信成功→SO情報取得成功→処理結果通知成功→工事実施成功→処理結果通知成功
	-- 　80：関連SO工事失敗（同一N番のSOの工事失敗）
	-- 　91：SO通知受信エラー
	-- 　92：SO通知受信成功→SO情報取得エラー
	-- 　93：SO通知受信成功→SO情報取得成功→工事実施エラー
	-- 　94：SO通知受信成功→SO情報取得成功→処理結果通知エラー
	-- 　95：SO通知受信成功→SO情報取得成功→処理結果通知成功→工事実施エラー（処理結果未通知）
	-- 　96：SO通知受信成功→SO情報取得成功→処理結果通知成功→工事実施エラー→処理結果通知成功
	-- 　97：SO通知受信成功→SO情報取得成功→処理結果通知成功→工事実施エラー→処理結果通知エラー
	-- 　98：SO通知受信成功→SO情報取得成功→処理結果通知成功→工事実施成功→処理結果通知エラー
	-- 
	-- 
	SO_STATUS INT NOT NULL,
	-- SO情報取得で取得した電文を保持する。
	-- 暗号化した状態で保持する。
	SO_INFO TEXT,
	-- 処理結果通知（工事結果）で送る電文を保持する。
	-- 暗号化した状態で保持する。
	ACTIVATION_RESULT_INFO TEXT,
	-- 処理結果通知（工事結果）を送信した回数。
	ACTIVATION_RESULT_SEND_COUNT INT,
	-- 最後に上位装置に送信した時に使用した送信番号。
	-- ゼロ埋めした状態のまま保持する。
	LAST_SO_SEND_ID VARCHAR(16),
	-- 最後に更新を実施したアカウントのアカウント情報ID。
	-- SOなど、カスコン画面からの更新でない場合は、0 を設定する。
	LAST_UPDATE_ACCOUNT_INFO_ID INT DEFAULT 0 NOT NULL,
	-- 最後に更新が実施された日時。
	LAST_UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	PRIMARY KEY (SO_INFO_ID)
) ;


-- トラフィック情報を保持する。
-- Asteriskのsip show channelsで取得した情報を保持する。
CREATE TABLE TRAFFIC_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	TRAFFIC_INFO_ID SERIAL NOT NULL UNIQUE,
	-- どのN番のトラフィック情報かを示すN番情報ID。
	-- 
	N_NUMBER_INFO_ID INT NOT NULL,
	-- トラフィックを計測した日時。
	MEASUREMENT_DATE TIMESTAMP NOT NULL,
	-- 利用できる上限の回線数。
	SUBSCRIBE_NUMBER INT NOT NULL,
	-- 実際に利用された回線数。
	USE_NUMBER INT NOT NULL,
	-- トラフィックの集計対象の拠点番号。
	-- 
	LOCATION_NUMBER VARCHAR(16),
	-- 最後に更新を実施したアカウントのアカウント情報ID。
	-- SOなど、カスコン画面からの更新でない場合は、0 を設定する。
	LAST_UPDATE_ACCOUNT_INFO_ID INT DEFAULT 0 NOT NULL,
	-- 最後に更新が実施された日時。
	LAST_UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	-- 論理削除を示すフラグ。
	-- 　false：有効データ
	-- 　true：削除データ
	DELETE_FLAG BOOLEAN DEFAULT 'false' NOT NULL,
	PRIMARY KEY (TRAFFIC_INFO_ID)
) ;


-- 内線サーバが稼働するVMの情報。
CREATE TABLE VM_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	VM_INFO_ID SERIAL NOT NULL UNIQUE,
	-- VMを識別するID。
	VM_ID VARCHAR(128) NOT NULL,
	-- VMのグローバルIPアドレス。
	-- 暗号化した状態で保持する。
	VM_GLOBAL_IP VARCHAR(256) NOT NULL,
	-- VMのフロント側プライベートIPアドレス。
	VM_PRIVATE_IP_F VARCHAR(128) NOT NULL,
	-- VMのバック側プライベートIPアドレス。
	VM_PRIVATE_IP_B VARCHAR(128) NOT NULL,
	-- VMのFQDN。
	-- 暗号化した状態で保持する。
	FQDN VARCHAR(512) NOT NULL,
	-- APGWのグローバルIPアドレス。
	-- 暗号化した状態で保持する。
	--
	APGW_GLOBAL_IP VARCHAR(256),
	-- VPNに割り当てられるグローバルIPアドレス。
	--
	VPN_GLOBAL_IP VARCHAR(256),
	-- VPNに割り当てられるプライベートIPアドレス。
	VPN_PRIVATE_IP VARCHAR(128),
	-- VPN用のIPアドレス。
	-- 暗号化した状態で保持する。
	VPN_FQDN_IP VARCHAR(128),
	-- FQDN/IPアドレスVPN第4オクテット
	VPN_FQDN_OCTET_FOUR INT,
	-- VMのOSにログインするためのID。
	-- 暗号化した状態で保持する。
	-- 
	OS_LOGIN_ID VARCHAR(256) NOT NULL,
	-- VMのOSにログインするためのパスワード。
	-- 暗号化した状態で保持する。
	-- 
	OS_PASSWORD VARCHAR(256) NOT NULL,
	-- VMのリソースタイプ。
	VM_RESOURCE_TYPE_MASTER_ID INT NOT NULL,
	-- 内線サーバのファイルバージョン
	FILE_VERSION VARCHAR(32),
	-- VMを払い出した対象のN番を示すN番情報ID。
	-- 
	N_NUMBER_INFO_ID INT UNIQUE,
	
	VPN_N_NUMBER VARCHAR(16),
	--APGWの利用機能番号。
	APGW_FUNCTION_NUMBER VARCHAR(16),
	-- 内線サーバのBHEC N番。
	BHEC_N_NUMBER VARCHAR(16),
	--APGWを識別するN番。
	APGW_N_NUMBER VARCHAR(16),
	--VPN対応/非対応を判別するフラグ。
	-- false:VPN非対応
	-- true:VPN対応
	VPN_USABLE_FLAG BOOLEAN,
	WHOLESALE_USABLE_FLAG BOOLEAN,
	WHOLESALE_TYPE INT,
	WHOLESALE_PRIVATE_IP VARCHAR(128),
	WHOLESALE_FQDN_IP VARCHAR(256),
	CONNECT_TYPE INT,
	-- VMの状態を示す。
	-- 　1：正常
	-- 　　※正常（未割付）、正常（割付済）がある。
	-- 　　　未割付/割付済は、N番情報IDの有無で判断可能。
	-- 　2：工事中
	-- 　　※SOでの工事処理を実施している状態。
	-- 　3：再利用待ち
	-- 　　※SO廃止後に、オペレータによるネットワーク設定変更待ちの状態。
	-- 　4：移転中
	-- 　　※VM支障移転で、移転先として指定され、工事中の状態。
	-- 　9：故障
	-- 　　※VM支障移転で、移転元として指定されたVM。故障状態。
	-- 
	VM_STATUS INT DEFAULT 1 NOT NULL,
	-- SO廃止された時の日時。
	-- SO廃止されていない、もしくはSO廃止後に正常にN番に割り付けられたVMについては、NULLが設定される。
	-- 
	ABOLITION_TIME TIMESTAMP,
	-- 最後に更新を実施したアカウントのアカウント情報ID。
	-- SOなど、カスコン画面からの更新でない場合は、0 を設定する。
	LAST_UPDATE_ACCOUNT_INFO_ID INT DEFAULT 0 NOT NULL,
	-- 最後に更新が実施された日時。
	LAST_UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	-- 論理削除を示すフラグ。
	-- 　false：有効データ
	-- 　true：削除データ
	DELETE_FLAG BOOLEAN DEFAULT 'false' NOT NULL,
	PRIMARY KEY (VM_INFO_ID)
) ;




-- VMのリソースタイプを管理するマスタ情報。
CREATE TABLE VM_RESOURCE_TYPE_MASTER
(
	-- 本テーブルのインデックスとして使用する一意なID。
	VM_RESOURCE_TYPE_MASTER_ID INT NOT NULL UNIQUE,
	-- VMリソースタイプ名。
	VM_RESOURCE_TYPE_NAME VARCHAR(32) NOT NULL,
	-- CPUコア数。
	CPU_CORE_NUMBER INT,
	-- メモリサイズ。
	MEMORY_SIZE VARCHAR(16),
	-- HDDサイズ。
	HDD_SIZE VARCHAR(16),
	-- VM払い出し処理時の判定に使用する、判定基準の下限値。
	-- VM払い出し判定下限値と上限値のセットで使用する。
	-- 例）
	-- VMリソースタイプA　VM払い出し判定下限値：0    上限値：100
	-- VMリソースタイプB　VM払い出し判定下限値：101  上限値：200
	-- ・N番のスコアが50の場合 → VMリソースタイプA
	-- ・N番のスコアが150の場合 → VMリソースタイプB
	-- ・N番のスコアが250の場合 → 該当なし
	-- スコアは、以下の式により算出する。
	-- 　スコア＝[N番情報.チャネル数]×[係数1]＋[N番に紐づくDBレコード「内線番号情報」の有効レコード数（内線番号数）]×[係数2]
	-- 　※係数1：機能設計書 [別紙２]ファイル設計書の「アプリケーション設定ファイル」のシートの「cuscon_vm_channel_rate」の項を参照。
	-- 　　係数2：機能設計書 [別紙２]ファイル設計書の「アプリケーション設定ファイル」のシートの「cuscon_vm_terminal_rate」の項を参照。
	-- 
	VM_DICIDE_UNDER INT NOT NULL,
	-- VM払い出し処理時の判定に使用する、判定基準の上限値。
	-- VM払い出し判定下限値と上限値のセットで使用する。
	-- 例）
	-- VMリソースタイプA　VM払い出し判定下限値：0    上限値：100
	-- VMリソースタイプB　VM払い出し判定下限値：101  上限値：200
	-- ・N番のスコアが50の場合 → VMリソースタイプA
	-- ・N番のスコアが150の場合 → VMリソースタイプB
	-- ・N番のスコアが250の場合 → 該当なし
	-- スコアは、以下の式により算出する。
	-- 　スコア＝[N番情報.チャネル数]×[係数1]＋[N番に紐づくDBレコード「内線番号情報」の有効レコード数（内線番号数）]×[係数2]
	-- 　※係数1：機能設計書 [別紙２]ファイル設計書の「アプリケーション設定ファイル」のシートの「cuscon_vm_channel_rate」の項を参照。
	-- 　　係数2：機能設計書 [別紙２]ファイル設計書の「アプリケーション設定ファイル」のシートの「cuscon_vm_terminal_rate」の項を参照。
	-- 
	VM_DICIDE_TOP INT NOT NULL,
	PRIMARY KEY (VM_RESOURCE_TYPE_MASTER_ID)
) ;


-- VM支障移転の予約情報。
CREATE TABLE VM_TRANSFER_QUEUE_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	VM_TRANSFER_QUEUE_INFO_ID SERIAL NOT NULL UNIQUE,
	-- VM支障移転元のVM情報ID。
	VM_TRANSFER_SRC_VM_INFO_ID INT NOT NULL,
	-- VM支障移転先のVM情報ID。
	VM_TRANSFER_DST_VM_INFO_ID INT NOT NULL,
	-- VM支障移転が予約された日時。
	VM_TRANSFER_RESERVE_DATE TIMESTAMP NOT NULL,
	-- VM支障移転処理を開始した日時。
	VM_TRANSFER_START_DATE TIMESTAMP,
	-- VM支障移転処理が完了した日時。
	VM_TRANSFER_END_DATE TIMESTAMP,
	-- VM支障移転の処理状況を示す。
	-- 　1：移転待ち
	-- 　2：移転中
	-- 　3：正常終了
	-- 　4：正常終了・ファイル移転NG
	-- 　9：異常終了
	-- 
	VM_TRANSFER_STATUS INT NOT NULL,
	-- VM支障移転を予約したアカウントのアカウント情報ID。
	RESERVER_ACCOUNT_INFO_ID INT NOT NULL,
	PRIMARY KEY (VM_TRANSFER_QUEUE_INFO_ID)
) ;


-- 内線サーバ設定反映の予約情報。
CREATE TABLE SERVER_RENEW_QUEUE_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	SERVER_RENEW_QUEUE_INFO_ID SERIAL NOT NULL UNIQUE,
	-- 内線サーバ設定反映の対象となるVMを示すVM情報ID。
	VM_INFO_ID INT NOT NULL UNIQUE,
	-- 内線サーバ設定反映が予約された日時。
	SERVER_RENEW_RESERVE_DATE TIMESTAMP NOT NULL,
	-- 内線サーバ設定反映を開始した日時。
	SERVER_RENEW_START_DATE TIMESTAMP,
	-- 内線サーバ設定反映が完了した日時。
	SERVER_RENEW_END_DATE TIMESTAMP,
	-- 内線サーバ設定反映の処理状況を示す。
	--　1：反映待ち
	--　2：反映中
	--　3：正常終了
	--　9：異常終了
	SERVER_RENEW_STATUS INT NOT NULL,
	-- 内線サーバ設定反映で、異常発生時の要因。
	SERVER_RENEW_ERR_CAUSE VARCHAR(256),
	-- 内線サーバ設定反映を予約したアカウントのアカウント情報ID。
	SERVER_RENEW_ACCOUNT_INFO_ID INT NOT NULL,
	PRIMARY KEY (SERVER_RENEW_QUEUE_INFO_ID)
) ;

CREATE TABLE EXTERNAL_GW_CONNECT_CHOICE_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	EXTERNAL_GW_CONNECT_CHOICE_INFO_ID SERIAL NOT NULL UNIQUE,
	N_NUMBER_INFO_ID INT,
	APGW_GLOBAL_IP VARCHAR(256),
	EXTERNAL_GW_PRIVATE_IP VARCHAR(128),
	LAST_UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	LAST_UPDATE_ACCOUNT_INFO_ID INT DEFAULT 0 NOT NULL,
	PRIMARY KEY (EXTERNAL_GW_CONNECT_CHOICE_INFO_ID)
) ;

CREATE SEQUENCE ADDITIONAL_ORDER_SEQ INCREMENT 1 MINVALUE 1 START 1;


/* CREATE MAC_ADDRESS_INFO */
-- SO処理の中でIP Phoneに対して払い出すMACアドレスの情報。
CREATE TABLE MAC_ADDRESS_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	MAC_ADDRESS_INFO_ID SERIAL NOT NULL UNIQUE,
	-- 端末の提供形態、内線番号情報テーブルの提供形態と同じ値を利用する。(Step2.8では2種のIP phoneのみ利用)
	-- 3:KX-UT136N
	-- 5:KX-UT123N
	SUPPLY_TYPE INT NOT NULL,
	-- 登録されたレコードがINSERTで追加されたものかAPPENDで追加されたものかを示す。
	-- 1:INSERTで追加されたレコード
	-- 2:APPENDで追加されたレコード
	ADDITIONAL_STYLE INT NOT NULL,
	-- 端末のMACアドレス。
	MAC_ADDRESS VARCHAR(12) NOT NULL UNIQUE,
	-- シーケンス値。
	-- MACアドレスを登録するcsvを読み込むごとに増加する。
	-- 1ファイルで登録したレコードは全て同じ値となる。
	ADDITIONAL_ORDER INT NOT NULL,
	-- 1つのcsvファイルの中で、追加形式ごとに何番目に記載されていたかを登録する
	ORDER_LINE INT NOT NULL,
	-- 最後に更新を実施したアカウントのアカウント情報ID。
	-- 
	-- SOなど、カスコン画面からの更新でない場合は、0 を設定する。
	LAST_UPDATE_ACCOUNT_INFO_ID INT DEFAULT 0 NOT NULL,
	-- 最後に更新が実施された日時。
	LAST_UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	PRIMARY KEY (MAC_ADDRESS_INFO_ID)
);

-- 保留音などの音源情報。
-- カスコン画面より登録される。
CREATE TABLE MUSIC_INFO
(
	-- 本テーブルのインデックスとして使用する一意なID。
	MUSIC_INFO_ID SERIAL NOT NULL UNIQUE,
	-- どのN番の音源情報かを示すN番情報ID。
	N_NUMBER_INFO_ID INT NOT NULL,
	-- 音源の種別（使用用途）を示す。
	-- 　1：保留音
	MUSIC_TYPE INT NOT NULL,
	-- カスコンから登録された音源の元のファイル名。
	MUSIC_ORI_NAME VARCHAR(128) NOT NULL,
	-- 変換後の音源のバイナリデータ。
	MUSIC_ENCODE_DATA BYTEA NOT NULL,
	-- 最後に更新を実施したアカウントのアカウント情報ID。
	-- SOなど、カスコン画面からの更新でない場合は、0 を設定する。
	LAST_UPDATE_ACCOUNT_INFO_ID INT NOT NULL,
	-- 最後に更新が実施された日時。
	LAST_UPDATE_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (MUSIC_INFO_ID),
	CONSTRAINT ucmp_music_info UNIQUE (N_NUMBER_INFO_ID, MUSIC_TYPE)
);

/* Create Foreign Keys */

ALTER TABLE EXTERNAL_GW_CONNECT_CHOICE_INFO
	ADD FOREIGN KEY (N_NUMBER_INFO_ID)
	REFERENCES N_NUMBER_INFO (N_NUMBER_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE ACCOUNT_INFO
	ADD FOREIGN KEY (EXTENSION_NUMBER_INFO_ID)
	REFERENCES EXTENSION_NUMBER_INFO (EXTENSION_NUMBER_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE INCOMING_GROUP_INFO
	ADD FOREIGN KEY (EXTENSION_NUMBER_INFO_ID)
	REFERENCES EXTENSION_NUMBER_INFO (EXTENSION_NUMBER_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE INCOMING_GROUP_CHILD_NUMBER_INFO
	ADD FOREIGN KEY (EXTENSION_NUMBER_INFO_ID)
	REFERENCES EXTENSION_NUMBER_INFO (EXTENSION_NUMBER_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE ABSENCE_BEHAVIOR_INFO
	ADD FOREIGN KEY (EXTENSION_NUMBER_INFO_ID)
	REFERENCES EXTENSION_NUMBER_INFO (EXTENSION_NUMBER_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE OUTSIDE_CALL_INCOMING_INFO
	ADD FOREIGN KEY (EXTENSION_NUMBER_INFO_ID)
	REFERENCES EXTENSION_NUMBER_INFO (EXTENSION_NUMBER_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE OUTSIDE_CALL_SENDING_INFO
	ADD FOREIGN KEY (EXTENSION_NUMBER_INFO_ID)
	REFERENCES EXTENSION_NUMBER_INFO (EXTENSION_NUMBER_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE INCOMING_GROUP_CHILD_NUMBER_INFO
	ADD FOREIGN KEY (INCOMING_GROUP_INFO_ID)
	REFERENCES INCOMING_GROUP_INFO (INCOMING_GROUP_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE OFFICE_CONSTRUCT_INFO
	ADD FOREIGN KEY (N_NUMBER_INFO_ID)
	REFERENCES N_NUMBER_INFO (N_NUMBER_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE TRAFFIC_INFO
	ADD FOREIGN KEY (N_NUMBER_INFO_ID)
	REFERENCES N_NUMBER_INFO (N_NUMBER_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE INCOMING_GROUP_INFO
	ADD FOREIGN KEY (N_NUMBER_INFO_ID)
	REFERENCES N_NUMBER_INFO (N_NUMBER_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE CALL_REGULATION_INFO
	ADD FOREIGN KEY (N_NUMBER_INFO_ID)
	REFERENCES N_NUMBER_INFO (N_NUMBER_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE SITE_ADDRESS_INFO
	ADD FOREIGN KEY (N_NUMBER_INFO_ID)
	REFERENCES N_NUMBER_INFO (N_NUMBER_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE OUTSIDE_CALL_INFO
	ADD FOREIGN KEY (N_NUMBER_INFO_ID)
	REFERENCES N_NUMBER_INFO (N_NUMBER_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE EXTENSION_NUMBER_INFO
	ADD FOREIGN KEY (N_NUMBER_INFO_ID)
	REFERENCES N_NUMBER_INFO (N_NUMBER_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE RESERVED_CALL_NUMBER_INFO
	ADD FOREIGN KEY (N_NUMBER_INFO_ID)
	REFERENCES N_NUMBER_INFO (N_NUMBER_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE ACCOUNT_INFO
	ADD FOREIGN KEY (N_NUMBER_INFO_ID)
	REFERENCES N_NUMBER_INFO (N_NUMBER_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE VM_INFO
	ADD FOREIGN KEY (N_NUMBER_INFO_ID)
	REFERENCES N_NUMBER_INFO (N_NUMBER_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE CALL_HISTORY_INFO
	ADD FOREIGN KEY (N_NUMBER_INFO_ID)
	REFERENCES N_NUMBER_INFO (N_NUMBER_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE OUTSIDE_CALL_INCOMING_INFO
	ADD FOREIGN KEY (OUTSIDE_CALL_INFO_ID)
	REFERENCES OUTSIDE_CALL_INFO (OUTSIDE_CALL_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE OUTSIDE_CALL_SENDING_INFO
	ADD FOREIGN KEY (OUTSIDE_CALL_INFO_ID)
	REFERENCES OUTSIDE_CALL_INFO (OUTSIDE_CALL_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE EXTENSION_NUMBER_INFO
	ADD FOREIGN KEY (SITE_ADDRESS_INFO_ID)
	REFERENCES SITE_ADDRESS_INFO (SITE_ADDRESS_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE VM_TRANSFER_QUEUE_INFO
	ADD FOREIGN KEY (VM_TRANSFER_SRC_VM_INFO_ID)
	REFERENCES VM_INFO (VM_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE VM_TRANSFER_QUEUE_INFO
	ADD FOREIGN KEY (VM_TRANSFER_DST_VM_INFO_ID)
	REFERENCES VM_INFO (VM_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE PBX_FILE_BACKUP_INFO
	ADD FOREIGN KEY (VM_INFO_ID)
	REFERENCES VM_INFO (VM_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE VM_INFO
	ADD FOREIGN KEY (VM_RESOURCE_TYPE_MASTER_ID)
	REFERENCES VM_RESOURCE_TYPE_MASTER (VM_RESOURCE_TYPE_MASTER_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE SERVER_RENEW_QUEUE_INFO
	ADD FOREIGN KEY (VM_INFO_ID)
	REFERENCES VM_INFO (VM_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;

ALTER TABLE MUSIC_INFO
	ADD FOREIGN KEY (N_NUMBER_INFO_ID)
	REFERENCES N_NUMBER_INFO (N_NUMBER_INFO_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;

/* Create Indexes */

-- 一意な部分インデックス。
-- インデックス作成時には、以下のWHERE句を追加する。
-- 　WHERE NOT delete_flag
-- 【例】
-- ・追加前
-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID);
-- ・追加後
-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID) WHERE NOT delete_flag;
CREATE UNIQUE INDEX UIDX_ABSENCE_BEHAVIOR_INFO ON ABSENCE_BEHAVIOR_INFO (EXTENSION_NUMBER_INFO_ID);
-- 一意な部分インデックス。
-- インデックス作成時には、以下のWHERE句を追加する。
-- 　WHERE NOT delete_flag
-- 【例】
-- ・追加前
-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID);
-- ・追加後
-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID) WHERE NOT delete_flag;
CREATE UNIQUE INDEX UIDX_ACCOUNT_INFO ON ACCOUNT_INFO (LOGIN_ID);
-- 一意な部分インデックス。
-- インデックス作成時には、以下のWHERE句を追加する。
-- 　WHERE NOT delete_flag
-- 【例】
-- ・追加前
-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID);
-- ・追加後
-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID) WHERE NOT delete_flag;
CREATE UNIQUE INDEX UIDX_CALL_REGULATION_INFO ON CALL_REGULATION_INFO (N_NUMBER_INFO_ID);
-- 一意な部分インデックス。-- インデックス作成時には、以下のWHERE句を追加する。-- 　WHERE NOT delete_flag-- 【例】-- ・追加前-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID);-- ・追加後-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID) WHERE NOT delete_flag;
CREATE UNIQUE INDEX UIDX_EXTENSION_NUMBER_INFO ON EXTENSION_NUMBER_INFO (TERMINAL_MAC_ADDRESS);
-- 一意な部分インデックス。
-- インデックス作成時には、以下のWHERE句を追加する。
-- 　WHERE NOT delete_flag
-- 【例】
-- ・追加前
-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID);
-- ・追加後
-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID) WHERE NOT delete_flag;
CREATE UNIQUE INDEX UIDX_INCOMING_GROUP_INFO ON INCOMING_GROUP_INFO (EXTENSION_NUMBER_INFO_ID);
-- 一意な部分インデックス。
-- インデックス作成時には、以下のWHERE句を追加する。
-- 　WHERE NOT delete_flag
-- 【例】
-- ・追加前
-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID);
-- ・追加後
-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID) WHERE NOT delete_flag;
CREATE UNIQUE INDEX UIDX_N_NUMBER_INFO ON N_NUMBER_INFO (N_NUMBER_NAME);
-- 一意な部分インデックス。
-- インデックス作成時には、以下のWHERE句を追加する。
-- 　WHERE NOT delete_flag
-- 【例】
-- ・追加前
-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID);
-- ・追加後
-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID) WHERE NOT delete_flag;
CREATE UNIQUE INDEX UIDX_OUTSIDE_CALL_INCOMING_INFO ON OUTSIDE_CALL_INCOMING_INFO (OUTSIDE_CALL_INFO_ID);
-- 一意な部分インデックス。
-- インデックス作成時には、以下のWHERE句を追加する。
-- 　WHERE NOT delete_flag
-- 【例】
-- ・追加前
-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID);
-- ・追加後
-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID) WHERE NOT delete_flag;
CREATE UNIQUE INDEX UIDX_OUTSIDE_CALL_SENDING_INFO ON OUTSIDE_CALL_SENDING_INFO (EXTENSION_NUMBER_INFO_ID);
-- 一意な部分インデックス。
-- インデックス作成時には、以下のWHERE句を追加する。
-- 　WHERE NOT delete_flag
-- 【例】
-- ・追加前
-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID);
-- ・追加後
-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID) WHERE NOT delete_flag;
CREATE UNIQUE INDEX UIDX_VM_INFO_1 ON VM_INFO (VM_ID);
-- 一意な部分インデックス。
-- インデックス作成時には、以下のWHERE句を追加する。
-- 　WHERE NOT delete_flag
-- 【例】
-- ・追加前
-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID);
-- ・追加後
-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID) WHERE NOT delete_flag;
CREATE UNIQUE INDEX UIDX_VM_INFO_2 ON VM_INFO (VM_PRIVATE_IP_B);
-- 一意な部分インデックス。
-- インデックス作成時には、以下のWHERE句を追加する。
-- 　WHERE NOT delete_flag
-- 【例】
-- ・追加前
-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID);
-- ・追加後
-- CREATE UNIQUE INDEX UNIQUE_INDEX_LOGIN_ID ON ACCOUNT_INFO (LOGIN_ID) WHERE NOT delete_flag;
CREATE UNIQUE INDEX UIDX_VM_INFO_3 ON VM_INFO (FQDN);




/* Comments */

COMMENT ON TABLE ABSENCE_BEHAVIOR_INFO IS '不在時の動作についての情報。';
COMMENT ON COLUMN ABSENCE_BEHAVIOR_INFO.ABSENCE_BEHAVIOR_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN ABSENCE_BEHAVIOR_INFO.EXTENSION_NUMBER_INFO_ID IS '不在時動作設定対象の内線番号のID。';
COMMENT ON COLUMN ABSENCE_BEHAVIOR_INFO.ABSENCE_BEHAVIOR_TYPE IS '不在時の動作のタイプを指定する。
　1：転送／留守番電話設定
　2：シングルナンバーリーチ設定';
COMMENT ON COLUMN ABSENCE_BEHAVIOR_INFO.FORWARD_PHONE_NUMBER IS '転送先電話番号。
不在時動作タイプが1（転送／留守番電話設定）の時に使用する。
暗号化した状態で保持する。';
COMMENT ON COLUMN ABSENCE_BEHAVIOR_INFO.FORWARD_BEHAVIOR_TYPE_UNCONDITIONAL IS '転送動作（無条件）のタイプを指定する。
不在時動作タイプが1（転送／留守番電話設定）の時に使用する。
　1：転送
　2：留守番電話
　3：設定しない';
COMMENT ON COLUMN ABSENCE_BEHAVIOR_INFO.FORWARD_BEHAVIOR_TYPE_BUSY IS '転送動作（話中）のタイプを指定する。
不在時動作タイプが1（転送／留守番電話設定）の時に使用する。
　1：転送
　2：留守番電話
　3：設定しない';
COMMENT ON COLUMN ABSENCE_BEHAVIOR_INFO.FORWARD_BEHAVIOR_TYPE_OUTSIDE IS '転送動作（圏外）のタイプを指定する。
不在時動作タイプが1（転送／留守番電話設定）の時に使用する。
　1：転送
　2：留守番電話
　3：設定しない';
COMMENT ON COLUMN ABSENCE_BEHAVIOR_INFO.FORWARD_BEHAVIOR_TYPE_NO_ANSWER IS '転送動作（無応答）のタイプを指定する。
不在時動作タイプが1（転送／留守番電話設定）の時に使用する。
　1：転送
　2：留守番電話
　3：設定しない';
COMMENT ON COLUMN ABSENCE_BEHAVIOR_INFO.CALL_TIME IS '無応答の場合の着信呼出時間。
不在時動作タイプが1（転送／留守番電話設定）の時に使用する。';
COMMENT ON COLUMN ABSENCE_BEHAVIOR_INFO.CONNECT_NUMBER_1 IS '接続先番号1。
不在時動作タイプが2（シングルナンバーリーチ設定）の時に使用する。
暗号化した状態で保持する。';
COMMENT ON COLUMN ABSENCE_BEHAVIOR_INFO.CONNECT_NUMBER_2 IS '接続先番号2。
不在時動作タイプが2（シングルナンバーリーチ設定）の時に使用する。
暗号化した状態で保持する。';
COMMENT ON COLUMN ABSENCE_BEHAVIOR_INFO.CALL_START_TIME_1 IS '接続先番号1の呼出開始時間。
不在時動作タイプが2（シングルナンバーリーチ設定）の時に使用する。';
COMMENT ON COLUMN ABSENCE_BEHAVIOR_INFO.CALL_START_TIME_2 IS '接続先番号2の呼出開始時間。
不在時動作タイプが2（シングルナンバーリーチ設定）の時に使用する。';
COMMENT ON COLUMN ABSENCE_BEHAVIOR_INFO.CALL_END_TIME IS '呼出終了時間
不在時動作タイプが2（シングルナンバーリーチ設定）の時に使用する。';
COMMENT ON COLUMN ABSENCE_BEHAVIOR_INFO.ANSWERPHONE_FLAG IS '留守番電話フラグ。
不在時動作タイプが2（シングルナンバーリーチ設定）の時に使用する。
　false：off
　true：on';
COMMENT ON COLUMN ABSENCE_BEHAVIOR_INFO.ANSWERPHONE_PASSWORD IS '留守番電話を聞く時に必要なパスワード。
暗号化した状態で保持する。';
COMMENT ON COLUMN ABSENCE_BEHAVIOR_INFO.LAST_UPDATE_ACCOUNT_INFO_ID IS '最後に更新を実施したアカウントのアカウント情報ID。
SOなど、カスコン画面からの更新でない場合は、0 を設定する。';
COMMENT ON COLUMN ABSENCE_BEHAVIOR_INFO.LAST_UPDATE_TIME IS '最後に更新が実施された日時。';
COMMENT ON COLUMN ABSENCE_BEHAVIOR_INFO.DELETE_FLAG IS '論理削除を示すフラグ。
　false：有効データ
　true：削除データ';
COMMENT ON TABLE ACCOUNT_INFO IS 'カスコン画面にログインするために必要なアカウント情報。';
COMMENT ON COLUMN ACCOUNT_INFO.ACCOUNT_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN ACCOUNT_INFO.LOGIN_ID IS 'ユーザがログイン時に指定するID';
COMMENT ON COLUMN ACCOUNT_INFO.PASSWORD IS 'ユーザがログイン時に指定するパスワード。
暗号化した状態で保持する。';
COMMENT ON COLUMN ACCOUNT_INFO.N_NUMBER_INFO_ID IS 'アカウントが所属するN番のID。
';
COMMENT ON COLUMN ACCOUNT_INFO.ACCOUNT_TYPE IS 'アカウント種別。
　1:オペレータ
　　※本システムのシステムオペレータ
　　　内線番号には紐づかない
　2:ユーザ管理者
　　※各契約（N番ごと）のユーザ管理者
　　　内線番号には紐づかない
　3:端末ユーザ
　　※個々のエンドユーザ
　　　内線番号には紐づく
';
COMMENT ON COLUMN ACCOUNT_INFO.EXTENSION_NUMBER_INFO_ID IS '該当アカウントの内線番号のID。';
COMMENT ON COLUMN ACCOUNT_INFO.PASSWORD_LIMIT IS 'パスワードの有効期限。';
COMMENT ON COLUMN ACCOUNT_INFO.LOCK_FLAG IS 'ログイン時にパスワード間違いを一定回数以上した場合に、ログイン不可（ロック）にするためのフラグ。
　false：ログイン可能
　true：ログイン不可';
COMMENT ON COLUMN ACCOUNT_INFO.PASSWORD_HISTORY_1 IS 'パスワード変更履歴1。
暗号化した状態で保持する。';
COMMENT ON COLUMN ACCOUNT_INFO.PASSWORD_HISTORY_2 IS 'パスワード変更履歴2。
暗号化した状態で保持する。';
COMMENT ON COLUMN ACCOUNT_INFO.PASSWORD_HISTORY_3 IS 'パスワード変更履歴3。
暗号化した状態で保持する。';
COMMENT ON COLUMN ACCOUNT_INFO.ADD_ACCOUNT_FLAG IS 'SOから作成されたアカウントか、カスコン画面から追加されたアカウントかを示すフラグ。
SO新設時に作成されたアカウントは、カスコン画面から削除できない。
　false：SOから作成されたアカウント
　true：カスコン画面から追加されたアカウント';
COMMENT ON COLUMN ACCOUNT_INFO.LAST_UPDATE_ACCOUNT_INFO_ID IS '最後に更新を実施したアカウントのアカウント情報ID。
SOなど、カスコン画面からの更新でない場合は、0 を設定する。';
COMMENT ON COLUMN ACCOUNT_INFO.LAST_UPDATE_TIME IS '最後に更新が実施された日時。';
COMMENT ON COLUMN ACCOUNT_INFO.DELETE_FLAG IS '論理削除を示すフラグ。
　false：有効データ
　true：削除データ';
COMMENT ON TABLE CALL_HISTORY_INFO IS '通話ログ情報を保持する。
AsteriskのCDRで取得した情報を保持する。';
COMMENT ON COLUMN CALL_HISTORY_INFO.CALL_HISTORY_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN CALL_HISTORY_INFO.N_NUMBER_INFO_ID IS 'どのN番の通話ログかを示すID。
';
COMMENT ON COLUMN CALL_HISTORY_INFO.CALLER_PHONE_NUMBER IS '発信者の電話番号。';
COMMENT ON COLUMN CALL_HISTORY_INFO.CALL_DATE IS '着信日時。';
COMMENT ON COLUMN CALL_HISTORY_INFO.TALK_START_DATE IS '通話開始日時。';
COMMENT ON COLUMN CALL_HISTORY_INFO.TALK_END_DATE IS '通話終了日時。';
COMMENT ON COLUMN CALL_HISTORY_INFO.CALLEE_PHONE_NUMBER IS '着信者の電話番号。';
COMMENT ON COLUMN CALL_HISTORY_INFO.TALK_TIME IS '通話時間。単位は秒。';
COMMENT ON COLUMN CALL_HISTORY_INFO.TALK_STATUS IS '通話ステータス。';
COMMENT ON COLUMN CALL_HISTORY_INFO.TALK_TYPE IS '通話種別。';
COMMENT ON COLUMN CALL_HISTORY_INFO.LAST_UPDATE_ACCOUNT_INFO_ID IS '最後に更新を実施したアカウントのアカウント情報ID。
SOなど、カスコン画面からの更新でない場合は、0 を設定する。';
COMMENT ON COLUMN CALL_HISTORY_INFO.LAST_UPDATE_TIME IS '最後に更新が実施された日時。';
COMMENT ON COLUMN CALL_HISTORY_INFO.DELETE_FLAG IS '論理削除を示すフラグ。
　false：有効データ
　true：削除データ';
COMMENT ON TABLE CALL_REGULATION_INFO IS '発信規制先情報。
N番号ごとに設定される。';
COMMENT ON COLUMN CALL_REGULATION_INFO.CALL_REGULATION_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN CALL_REGULATION_INFO.N_NUMBER_INFO_ID IS '発信規制先の設定対象のN番を示すID。';
COMMENT ON COLUMN CALL_REGULATION_INFO.CALL_REGULATION_NUMBER_1 IS '規制対象番号1。
暗号化した状態で保持する。';
COMMENT ON COLUMN CALL_REGULATION_INFO.CALL_REGULATION_NUMBER_2 IS '規制対象番号2。
暗号化した状態で保持する。';
COMMENT ON COLUMN CALL_REGULATION_INFO.CALL_REGULATION_NUMBER_3 IS '規制対象番号3。
暗号化した状態で保持する。';
COMMENT ON COLUMN CALL_REGULATION_INFO.CALL_REGULATION_NUMBER_4 IS '規制対象番号4。
暗号化した状態で保持する。';
COMMENT ON COLUMN CALL_REGULATION_INFO.CALL_REGULATION_NUMBER_5 IS '規制対象番号5。
暗号化した状態で保持する。';
COMMENT ON COLUMN CALL_REGULATION_INFO.CALL_REGULATION_NUMBER_6 IS '規制対象番号6。
暗号化した状態で保持する。';
COMMENT ON COLUMN CALL_REGULATION_INFO.CALL_REGULATION_NUMBER_7 IS '規制対象番号7。
暗号化した状態で保持する。';
COMMENT ON COLUMN CALL_REGULATION_INFO.CALL_REGULATION_NUMBER_8 IS '規制対象番号8。
暗号化した状態で保持する。';
COMMENT ON COLUMN CALL_REGULATION_INFO.CALL_REGULATION_NUMBER_9 IS '規制対象番号9。
暗号化した状態で保持する。';
COMMENT ON COLUMN CALL_REGULATION_INFO.CALL_REGULATION_NUMBER_10 IS '規制対象番号10。
暗号化した状態で保持する。';
COMMENT ON COLUMN CALL_REGULATION_INFO.CALL_REGULATION_NUMBER_11 IS '規制対象番号11。
暗号化した状態で保持する。';
COMMENT ON COLUMN CALL_REGULATION_INFO.CALL_REGULATION_NUMBER_12 IS '規制対象番号12。
暗号化した状態で保持する。';
COMMENT ON COLUMN CALL_REGULATION_INFO.CALL_REGULATION_NUMBER_13 IS '規制対象番号13。
暗号化した状態で保持する。';
COMMENT ON COLUMN CALL_REGULATION_INFO.CALL_REGULATION_NUMBER_14 IS '規制対象番号14。
暗号化した状態で保持する。';
COMMENT ON COLUMN CALL_REGULATION_INFO.CALL_REGULATION_NUMBER_15 IS '規制対象番号15。
暗号化した状態で保持する。';
COMMENT ON COLUMN CALL_REGULATION_INFO.CALL_REGULATION_NUMBER_16 IS '規制対象番号16。
暗号化した状態で保持する。';
COMMENT ON COLUMN CALL_REGULATION_INFO.CALL_REGULATION_NUMBER_17 IS '規制対象番号17。
暗号化した状態で保持する。';
COMMENT ON COLUMN CALL_REGULATION_INFO.CALL_REGULATION_NUMBER_18 IS '規制対象番号18。
暗号化した状態で保持する。';
COMMENT ON COLUMN CALL_REGULATION_INFO.CALL_REGULATION_NUMBER_19 IS '規制対象番号19。
暗号化した状態で保持する。';
COMMENT ON COLUMN CALL_REGULATION_INFO.CALL_REGULATION_NUMBER_20 IS '規制対象番号20。
暗号化した状態で保持する。';
COMMENT ON COLUMN CALL_REGULATION_INFO.LAST_UPDATE_ACCOUNT_INFO_ID IS '最後に更新を実施したアカウントのアカウント情報ID。
SOなど、カスコン画面からの更新でない場合は、0 を設定する。';
COMMENT ON COLUMN CALL_REGULATION_INFO.LAST_UPDATE_TIME IS '最後に更新が実施された日時。';
COMMENT ON COLUMN CALL_REGULATION_INFO.DELETE_FLAG IS '論理削除を示すフラグ。
　false：有効データ
　true：削除データ';
COMMENT ON TABLE EXTENSION_NUMBER_INFO IS '内線番号情報。';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.EXTENSION_NUMBER_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.N_NUMBER_INFO_ID IS '内線番号が属しているN番を示すID。
';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.EXTENSION_NUMBER IS '内線番号。
2～22桁。';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.LOCATION_NUMBER IS '拠点番号。
1～11桁。
端末種別が3（VoIP-GW）の場合は、拠点番号のみで端末番号はNULL。';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.TERMINAL_NUMBER IS '端末番号。
1～11桁。
端末種別が3（VoIP-GW）の場合は、拠点番号のみで端末番号はNULL。';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.TERMINAL_TYPE IS '端末種別。
　0：IP Phone
　1：スマートフォン
　2：ソフトフォン
　3：VoIP-GW
　4：VoIP-GW（拠点RTなし）
';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.SUPPLY_TYPE IS '端末の提供形態。
　1:自営
　2:アプリ提供
　3:お買い上げ
　4:レンタル';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.SITE_ADDRESS_INFO_ID IS '内線番号に紐づく端末が設置される場所を示すID。';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.EXTENSION_ID IS '内線ID。
暗号化した状態で保持する。';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.EXTENSION_PASSWORD IS '内線パスワード。
暗号化した状態で保持する。';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.LOCATION_NUM_MULTI_USE IS '同じ拠点番号で、VoIP-GWを複数台利用する場合の、何台目かを示す値。';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.EXTRA_CHANNEL IS 'VoIP-GWが持っているチャネルの数。';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.OUTBOUND_FLAG IS '外線発信設定済みか否かを示すフラグ。
　false：未設定
　true：設定済み';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.ABSENCE_FLAG IS '不在時動作が設定済みか否かを示すフラグ
　false：未設定
　true：設定済み';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.CALL_REGULATION_FLAG IS '発信規制が設定済みか否かを示すフラグ
　false：未設定
　true：設定済み';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.AUTOMATIC_SETTING_FLAG IS '端末自動設定フラグを実施するか否かを示すフラグ
※Step1では使用しない。
　false：実施しない
　true：実施する';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.TERMINAL_MAC_ADDRESS IS '端末のMACアドレス。
暗号化した状態で保持する。
※Step1では使用しない。';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.SO_ACTIVATION_RESERVE_FLAG IS 'SO工事予約を示すフラグ。
　false：予約なし
　true：予約あり
true(予約あり)の場合、該当レコードはカスコン画面からの操作は不可となる。
SO工事実施後、false(予約なし)に変更される。';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.LAST_UPDATE_ACCOUNT_INFO_ID IS '最後に更新を実施したアカウントのアカウント情報ID。
SOなど、カスコン画面からの更新でない場合は、0 を設定する。';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.LAST_UPDATE_TIME IS '最後に更新が実施された日時。';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.DELETE_FLAG IS '論理削除を示すフラグ。
　false：有効データ
　true：削除データ';
COMMENT ON TABLE INCOMING_GROUP_CHILD_NUMBER_INFO IS '着信グループに属している、内線番号の一覧。';
COMMENT ON COLUMN INCOMING_GROUP_CHILD_NUMBER_INFO.INCOMING_GROUP_CHILD_NUMBER_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN INCOMING_GROUP_CHILD_NUMBER_INFO.INCOMING_GROUP_INFO_ID IS '本テーブルで定義している子番号が属している着信グループを示すID。';
COMMENT ON COLUMN INCOMING_GROUP_CHILD_NUMBER_INFO.INCOMING_ORDER IS '着信する順番。';
COMMENT ON COLUMN INCOMING_GROUP_CHILD_NUMBER_INFO.EXTENSION_NUMBER_INFO_ID IS '着信グループに属している内線番号情報ID。';
COMMENT ON TABLE INCOMING_GROUP_INFO IS '着信グループの設定情報。';
COMMENT ON COLUMN INCOMING_GROUP_INFO.INCOMING_GROUP_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN INCOMING_GROUP_INFO.INCOMING_GROUP_NAME IS '着信グループの名前。';
COMMENT ON COLUMN INCOMING_GROUP_INFO.N_NUMBER_INFO_ID IS '着信グループが属するＮ番を示すID。';
COMMENT ON COLUMN INCOMING_GROUP_INFO.EXTENSION_NUMBER_INFO_ID IS '着信グループの代表番号を示すID。';
COMMENT ON COLUMN INCOMING_GROUP_INFO.GROUP_CALL_TYPE IS '着信グループの呼出方式。
　1：順次着信
　2：一斉着信
　3：コールピックアップ';
COMMENT ON COLUMN INCOMING_GROUP_INFO.NO_ANSWER_TIMER IS '呼出方式を1（順次着信）とした時に、グループ内の端末がビジー・故障・圏外（未レジ）・無応答・端末着信拒否だった場合に、グループ内の次の端末を着信状態にするまでの時間。単位は秒。';
COMMENT ON COLUMN INCOMING_GROUP_INFO.LAST_UPDATE_ACCOUNT_INFO_ID IS '最後に更新を実施したアカウントのアカウント情報ID。
SOなど、カスコン画面からの更新でない場合は、0 を設定する。';
COMMENT ON COLUMN INCOMING_GROUP_INFO.LAST_UPDATE_TIME IS '最後に更新が実施された日時。';
COMMENT ON COLUMN INCOMING_GROUP_INFO.DELETE_FLAG IS '論理削除を示すフラグ。
　false：有効データ
　true：削除データ';
COMMENT ON TABLE INFOMATION_INFO IS 'オペレータによって登録されるお知らせ情報。
カスコン画面に表示される。';
COMMENT ON COLUMN INFOMATION_INFO.INFOMATION_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN INFOMATION_INFO.INFOMATION_INFO IS 'お知らせ情報の本文。';
COMMENT ON COLUMN INFOMATION_INFO.LAST_UPDATE_ACCOUNT_INFO_ID IS '最後に更新を実施したアカウントのアカウント情報ID。
SOなど、カスコン画面からの更新でない場合は、0 を設定する。';
COMMENT ON COLUMN INFOMATION_INFO.LAST_UPDATE_TIME IS '最後に更新が実施された日時。';
COMMENT ON COLUMN INFOMATION_INFO.DELETE_FLAG IS '論理削除を示すフラグ。
　false：有効データ
　true：削除データ';
COMMENT ON TABLE N_NUMBER_INFO IS '契約ごとに上位SOが払い出すN番情報。
上位SOからAPIによって通知される。';
COMMENT ON COLUMN N_NUMBER_INFO.N_NUMBER_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN N_NUMBER_INFO.N_NUMBER_NAME IS '契約者ごとに付与される番号。
上位装置よりSOにて通知される。';
COMMENT ON COLUMN N_NUMBER_INFO.SITE_DIGIT IS '拠点番号の桁数。';
COMMENT ON COLUMN N_NUMBER_INFO.TERMINAL_DIGIT IS '端末番号の桁数。';
COMMENT ON COLUMN N_NUMBER_INFO.CH_NUM IS 'Asteriskのダイアルプランで使用するチャネル数。';
COMMENT ON COLUMN N_NUMBER_INFO.OUTSIDE_CALL_PREFIX IS '外線Prefix設定。
　1：0+外線番号で発信
　2：外線番号のみで発信';
COMMENT ON COLUMN N_NUMBER_INFO.EXTENSION_RANDOM_WORD IS 'SIP-IDに使用する文字列。
SIP-IDは内線ランダム文字列+内線番号で生成される。';
COMMENT ON COLUMN N_NUMBER_INFO.TUTORIAL_FLAG IS 'チュートリアルが完了しているか否かを示すフラグ。
　false：チュートリアル未完了
　true ：チュートリアル完了済み';
COMMENT ON COLUMN N_NUMBER_INFO.LAST_UPDATE_ACCOUNT_INFO_ID IS '最後に更新を実施したアカウントのアカウント情報ID。
SOなど、カスコン画面からの更新でない場合は、0 を設定する。';
COMMENT ON COLUMN N_NUMBER_INFO.LAST_UPDATE_TIME IS '最後に更新が実施された日時。';
COMMENT ON COLUMN N_NUMBER_INFO.DELETE_FLAG IS '論理削除を示すフラグ。
　false：有効データ
　true：削除データ';
COMMENT ON TABLE OFFICE_CONSTRUCT_INFO IS 'オフィス構築セットの申し込み拠点情報。';
COMMENT ON COLUMN OFFICE_CONSTRUCT_INFO.OFFICE_CONSTRUCT_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN OFFICE_CONSTRUCT_INFO.N_NUMBER_INFO_ID IS 'オフィス構築セット情報が属しているN番のID。';
COMMENT ON COLUMN OFFICE_CONSTRUCT_INFO.MANAGE_NUMBER IS 'オフィス構築セット情報を管理する番号。';
COMMENT ON COLUMN OFFICE_CONSTRUCT_INFO.LOCATION_NAME IS 'オフィス構築セットの申し込み拠点情報の拠点名。
暗号化して保持する。';
COMMENT ON COLUMN OFFICE_CONSTRUCT_INFO.LOCATION_ADDRESS IS 'オフィス構築セットの申し込み拠点情報の拠点住所。
暗号化して保持する。';
COMMENT ON COLUMN OFFICE_CONSTRUCT_INFO.OUTSIDE_INFO IS 'オフィス構築セットの申し込み拠点情報の外線情報。';
COMMENT ON COLUMN OFFICE_CONSTRUCT_INFO.MEMO IS 'オフィス構築セットの申し込み拠点情報の備考欄。';
COMMENT ON COLUMN OFFICE_CONSTRUCT_INFO.LAST_UPDATE_ACCOUNT_INFO_ID IS '最後に更新を実施したアカウントのアカウント情報ID。
SOなど、カスコン画面からの更新でない場合は、0 を設定する。';
COMMENT ON COLUMN OFFICE_CONSTRUCT_INFO.LAST_UPDATE_TIME IS '最後に更新が実施された日時。';
COMMENT ON TABLE OUTSIDE_CALL_INCOMING_INFO IS '外線着信時の設定情報。';
COMMENT ON COLUMN OUTSIDE_CALL_INCOMING_INFO.OUTSIDE_CALL_INCOMING_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN OUTSIDE_CALL_INCOMING_INFO.OUTSIDE_CALL_INFO_ID IS '外線着信時の外線情報。';
COMMENT ON COLUMN OUTSIDE_CALL_INCOMING_INFO.EXTENSION_NUMBER_INFO_ID IS '外線着信時の着信先の内線番号ID。
内線番号情報IDと着信グループ情報IDのいずれか一方のカラムが設定されていること。
';
COMMENT ON COLUMN OUTSIDE_CALL_INCOMING_INFO.VOIPGW_INCOMING_TERMINAL_NUMBER IS '外線着信先がVoIP-GWであった場合に登録される端末番号。
VoIP-GWの場合には、内線番号情報の端末番号がないため、内線番号情報の拠点番号と本カラムの値とで、外線着信先の内線番号となる。';
COMMENT ON COLUMN OUTSIDE_CALL_INCOMING_INFO.LAST_UPDATE_ACCOUNT_INFO_ID IS '最後に更新を実施したアカウントのアカウント情報ID。
SOなど、カスコン画面からの更新でない場合は、0 を設定する。';
COMMENT ON COLUMN OUTSIDE_CALL_INCOMING_INFO.LAST_UPDATE_TIME IS '最後に更新が実施された日時。';
COMMENT ON COLUMN OUTSIDE_CALL_INCOMING_INFO.DELETE_FLAG IS '論理削除を示すフラグ。
　false：有効データ
　true：削除データ';
COMMENT ON TABLE OUTSIDE_CALL_INFO IS 'N番が持っている外線の情報。';
COMMENT ON COLUMN OUTSIDE_CALL_INFO.OUTSIDE_CALL_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN OUTSIDE_CALL_INFO.N_NUMBER_INFO_ID IS '外線の持ち主であるN番のN番情報ID。';
COMMENT ON COLUMN OUTSIDE_CALL_INFO.OUTSIDE_CALL_SERVICE_TYPE IS '外線のサービス種別。
　1：050plus for Biz
　2：IP Voice for SmartPBX
　3：自営SIPサーバ（1）
　4：自営SIPサーバ（1）
';
COMMENT ON COLUMN OUTSIDE_CALL_INFO.OUTSIDE_CALL_LINE_TYPE IS '外線のアクセス回線の種別。
　1：OCN/提携ISP
　2：非提携ISP';
COMMENT ON COLUMN OUTSIDE_CALL_INFO.OUTSIDE_CALL_NUMBER IS '外線番号。';
COMMENT ON COLUMN OUTSIDE_CALL_INFO.SIP_CVT_REGIST_NUMBER IS '外線サービス種別が 3（自前SIPサーバ）の場合、本システムの内線サーバが自前SIPサーバに対してRegistする時に使用する番号。
自前SIPサーバが払い出した内線番号。';
COMMENT ON COLUMN OUTSIDE_CALL_INFO.ADD_FLAG IS '外線が基本か追加かを示すフラグ。
　false：基本
　true：追加';
COMMENT ON COLUMN OUTSIDE_CALL_INFO.SIP_ID IS 'SIP-IP。
暗号化した状態で保持する。';
COMMENT ON COLUMN OUTSIDE_CALL_INFO.SIP_PASSWORD IS 'SIPパスワード。
暗号化した状態で保持する。';
COMMENT ON COLUMN OUTSIDE_CALL_INFO.SERVER_ADDRESS IS '外線サーバのアドレス。
暗号化した状態で保持する。';
COMMENT ON COLUMN OUTSIDE_CALL_INFO.PORT_NUMBER IS 'ポート番号。';
COMMENT ON COLUMN OUTSIDE_CALL_INFO.LAST_UPDATE_ACCOUNT_INFO_ID IS '最後に更新を実施したアカウントのアカウント情報ID。
SOなど、カスコン画面からの更新でない場合は、0 を設定する。';
COMMENT ON COLUMN OUTSIDE_CALL_INFO.LAST_UPDATE_TIME IS '最後に更新が実施された日時。';
COMMENT ON COLUMN OUTSIDE_CALL_INFO.DELETE_FLAG IS '論理削除を示すフラグ。
　false：有効データ
　true：削除データ';
COMMENT ON TABLE OUTSIDE_CALL_SENDING_INFO IS '外線発信時の設定情報。';
COMMENT ON COLUMN OUTSIDE_CALL_SENDING_INFO.OUTSIDE_CALL_SENDING_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN OUTSIDE_CALL_SENDING_INFO.EXTENSION_NUMBER_INFO_ID IS '外線への発信を設定する内線のID。';
COMMENT ON COLUMN OUTSIDE_CALL_SENDING_INFO.OUTSIDE_CALL_INFO_ID IS '外線への発信する時に使用する外線のID。';
COMMENT ON COLUMN OUTSIDE_CALL_SENDING_INFO.LAST_UPDATE_ACCOUNT_INFO_ID IS '最後に更新を実施したアカウントのアカウント情報ID。
SOなど、カスコン画面からの更新でない場合は、0 を設定する。';
COMMENT ON COLUMN OUTSIDE_CALL_SENDING_INFO.LAST_UPDATE_TIME IS '最後に更新が実施された日時。';
COMMENT ON COLUMN OUTSIDE_CALL_SENDING_INFO.DELETE_FLAG IS '論理削除を示すフラグ。
　false：有効データ
　true：削除データ';
COMMENT ON TABLE PBX_FILE_BACKUP_INFO IS '内線サーバの最新1世代分の設定ファイルを保持する。';
COMMENT ON COLUMN PBX_FILE_BACKUP_INFO.PBX_FILE_BACKUP_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN PBX_FILE_BACKUP_INFO.VM_INFO_ID IS 'バックアップ対象の内線サーバのVM情報ID。';
COMMENT ON COLUMN PBX_FILE_BACKUP_INFO.PBX_FILE_PATH IS '内線サーバ設定ファイルのファイルパスとファイル名の組み合わせ。
ファイルパスは、内線サーバのベースディレクトリからの相対パスとする。';
COMMENT ON COLUMN PBX_FILE_BACKUP_INFO.PBX_FILE_DATA IS '内線サーバの設定ファイルの内容。';
COMMENT ON COLUMN PBX_FILE_BACKUP_INFO.LAST_UPDATE_ACCOUNT_INFO_ID IS '最後に更新を実施したアカウントのアカウント情報ID。
SOなど、カスコン画面からの更新でない場合は、0 を設定する。';
COMMENT ON COLUMN PBX_FILE_BACKUP_INFO.LAST_UPDATE_TIME IS '最後に更新が実施された日時。';
COMMENT ON TABLE RESERVED_CALL_NUMBER_INFO IS '予約された電話番号（内線番号もしくは外線番号）の情報。
工事にて、該当の電話番号（内線番号もしくは外線番号）が処理されたのち、本テーブルの該当レコードは物理削除する。';
COMMENT ON COLUMN RESERVED_CALL_NUMBER_INFO.RESERVED_CALL_NUMBER_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN RESERVED_CALL_NUMBER_INFO.RESERVED_CALL_NUMBER IS '予約された電話番号（内線番号もしくは外線番号）。';
COMMENT ON COLUMN RESERVED_CALL_NUMBER_INFO.N_NUMBER_INFO_ID IS '電話番号（内線番号もしくは外線番号）を予約する対象のN番。';
COMMENT ON TABLE SITE_ADDRESS_INFO IS '端末設置場所の住所等の情報。';
COMMENT ON COLUMN SITE_ADDRESS_INFO.SITE_ADDRESS_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN SITE_ADDRESS_INFO.N_NUMBER_INFO_ID IS '拠点が所属するN番のID。';
COMMENT ON COLUMN SITE_ADDRESS_INFO.LOCATION_ID IS '拠点を識別するID。
同じN番の中では一意となる。
';
COMMENT ON COLUMN SITE_ADDRESS_INFO.LOCATION_NAME IS '端末の設置場所の拠点名。';
COMMENT ON COLUMN SITE_ADDRESS_INFO.ZIP_CODE IS '端末の設置場所の郵便番号。
暗号化して保持する。';
COMMENT ON COLUMN SITE_ADDRESS_INFO.ADDRESS IS '端末の設置場所の住所。
暗号化して保持する。';
COMMENT ON COLUMN SITE_ADDRESS_INFO.BUILDING_NAME IS '端末の設置場所のビル、マンション名。
暗号化して保持する。';
COMMENT ON COLUMN SITE_ADDRESS_INFO.SUPPORT_STAFF IS '端末の設置場所の担当者。
暗号化して保持する。';
COMMENT ON COLUMN SITE_ADDRESS_INFO.CONTACT_INFO IS '端末の設置場所の連絡先。
暗号化して保持する。';
COMMENT ON COLUMN SITE_ADDRESS_INFO.LAST_UPDATE_ACCOUNT_INFO_ID IS '最後に更新を実施したアカウントのアカウント情報ID。
SOなど、カスコン画面からの更新でない場合は、0 を設定する。';
COMMENT ON COLUMN SITE_ADDRESS_INFO.LAST_UPDATE_TIME IS '最後に更新が実施された日時。';
COMMENT ON COLUMN SITE_ADDRESS_INFO.DELETE_FLAG IS '論理削除を示すフラグ。
　false：有効データ
　true：削除データ';
COMMENT ON TABLE SO_CONTROL_INFO IS 'SO規制対象のN番を保持する。
SO規制対象となっているN番は、規制されている間、SO電文の受信を受け付けない。
規制解除する場合には、本テーブルの該当レコードを物理削除する。';
COMMENT ON COLUMN SO_CONTROL_INFO.SO_CONTROL_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN SO_CONTROL_INFO.N_NUMBER_NAME IS 'SO規制対象とするN番。
';
COMMENT ON TABLE SO_INFO IS '上位装置から通知されるSO情報。';
COMMENT ON COLUMN SO_INFO.SO_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN SO_INFO.SO_NUMBER IS 'SO通知にて、通知されるSO番号。
SO情報取得時に、使用する。';
COMMENT ON COLUMN SO_INFO.SO_BRANCH_NUMBER IS 'SO通知にて、通知されるSO番号の枝番。
SO情報取得時に、使用する。';
COMMENT ON COLUMN SO_INFO.SO_NUMBER_RESERVED IS 'SO通知にて、通知されるWebエントリID。
※物理名と論理名で整合性が取れていないが仕様である。';
COMMENT ON COLUMN SO_INFO.COMM_TREE_CODE IS 'SO通知にて取得できる商品ツリーコード。';
COMMENT ON COLUMN SO_INFO.N_NUMBER IS 'SO通知にて取得できるN番。';
COMMENT ON COLUMN SO_INFO.SO_TYPE IS 'SO種別。
　1:新設
　2:変更(追加)
　3:廃止
　4:SO記載変（使わない）
　5:SO取消
　6:変更(変更)
　7:変更(削除)';
COMMENT ON COLUMN SO_INFO.RECEIVE_DATE IS 'SO通知を受信した日時。';
COMMENT ON COLUMN SO_INFO.ACTIVATION_DATE IS '工事日時。';
COMMENT ON COLUMN SO_INFO.SO_STATUS IS 'SO処理のステータスを示す。
　1：SO通知受信成功（SO情報未取得）
　2：SO通知受信成功→SO情報取得成功（処理結果未通知）
　3：SO通知受信成功→SO情報取得成功→処理結果通知成功（工事未実施）
　4：SO通知受信成功→SO情報取得成功→処理結果通知成功→工事実施成功（処理結果未通知）
　5：SO通知受信成功→SO情報取得成功→処理結果通知成功→工事実施成功→処理結果通知成功
　80：関連SO工事失敗（同一N番のSOの工事失敗）
　91：SO通知受信エラー
　92：SO通知受信成功→SO情報取得エラー
　93：SO通知受信成功→SO情報取得成功→工事実施エラー
　94：SO通知受信成功→SO情報取得成功→処理結果通知エラー
　95：SO通知受信成功→SO情報取得成功→処理結果通知成功→工事実施エラー（処理結果未通知）
　96：SO通知受信成功→SO情報取得成功→処理結果通知成功→工事実施エラー→処理結果通知成功
　97：SO通知受信成功→SO情報取得成功→処理結果通知成功→工事実施エラー→処理結果通知エラー
　98：SO通知受信成功→SO情報取得成功→処理結果通知成功→工事実施成功→処理結果通知エラー

';
COMMENT ON COLUMN SO_INFO.SO_INFO IS 'SO情報取得で取得した電文を保持する。
暗号化した状態で保持する。';
COMMENT ON COLUMN SO_INFO.ACTIVATION_RESULT_INFO IS '処理結果通知（工事結果）で送る電文を保持する。
暗号化した状態で保持する。';
COMMENT ON COLUMN SO_INFO.ACTIVATION_RESULT_SEND_COUNT IS '処理結果通知（工事結果）を送信した回数。';
COMMENT ON COLUMN SO_INFO.LAST_SO_SEND_ID IS '最後に上位装置に送信した時に使用した送信番号。
ゼロ埋めした状態のまま保持する。';
COMMENT ON COLUMN SO_INFO.LAST_UPDATE_ACCOUNT_INFO_ID IS '最後に更新を実施したアカウントのアカウント情報ID。
SOなど、カスコン画面からの更新でない場合は、0 を設定する。';
COMMENT ON COLUMN SO_INFO.LAST_UPDATE_TIME IS '最後に更新が実施された日時。';
COMMENT ON TABLE TRAFFIC_INFO IS 'トラフィック情報を保持する。
Asteriskのsip show channelsで取得した情報を保持する。';
COMMENT ON COLUMN TRAFFIC_INFO.TRAFFIC_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN TRAFFIC_INFO.N_NUMBER_INFO_ID IS 'どのN番のトラフィック情報かを示すN番情報ID。
';
COMMENT ON COLUMN TRAFFIC_INFO.MEASUREMENT_DATE IS 'トラフィックを計測した日時。';
COMMENT ON COLUMN TRAFFIC_INFO.SUBSCRIBE_NUMBER IS '利用できる上限の回線数。';
COMMENT ON COLUMN TRAFFIC_INFO.USE_NUMBER IS '実際に利用された回線数。';
COMMENT ON COLUMN TRAFFIC_INFO.LOCATION_NUMBER IS 'トラフィックの集計対象の拠点番号。
';
COMMENT ON COLUMN TRAFFIC_INFO.LAST_UPDATE_ACCOUNT_INFO_ID IS '最後に更新を実施したアカウントのアカウント情報ID。
SOなど、カスコン画面からの更新でない場合は、0 を設定する。';
COMMENT ON COLUMN TRAFFIC_INFO.LAST_UPDATE_TIME IS '最後に更新が実施された日時。';
COMMENT ON COLUMN TRAFFIC_INFO.DELETE_FLAG IS '論理削除を示すフラグ。
　false：有効データ
　true：削除データ';
COMMENT ON TABLE VM_INFO IS '内線サーバが稼働するVMの情報。';
COMMENT ON COLUMN VM_INFO.VM_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN VM_INFO.VM_ID IS 'VMを識別するID。';
COMMENT ON COLUMN VM_INFO.VM_GLOBAL_IP IS 'VMのグローバルIPアドレス。
暗号化した状態で保持する。';
COMMENT ON COLUMN VM_INFO.VM_PRIVATE_IP_F IS 'VMのフロント側プライベートIPアドレス。';
COMMENT ON COLUMN VM_INFO.VM_PRIVATE_IP_B IS 'VMのバック側プライベートIPアドレス。';
COMMENT ON COLUMN VM_INFO.FQDN IS 'VMのFQDN。
暗号化した状態で保持する。';
COMMENT ON COLUMN VM_INFO.OS_LOGIN_ID IS 'VMのOSにログインするためのID。
暗号化した状態で保持する。
';
COMMENT ON COLUMN VM_INFO.OS_PASSWORD IS 'VMのOSにログインするためのパスワード。
暗号化した状態で保持する。
';
COMMENT ON COLUMN VM_INFO.VM_RESOURCE_TYPE_MASTER_ID IS 'VMのリソースタイプ。';
COMMENT ON COLUMN VM_INFO.FILE_VERSION IS '内線サーバのファイルバージョン';
COMMENT ON COLUMN VM_INFO.N_NUMBER_INFO_ID IS 'VMを払い出した対象のN番を示すN番情報ID。
';
COMMENT ON COLUMN VM_INFO.VM_STATUS IS 'VMの状態を示す。
　1：正常
　　※正常（未割付）、正常（割付済）がある。
　　　未割付/割付済は、N番情報IDの有無で判断可能。
　2：工事中
　　※SOでの工事処理を実施している状態。
　3：再利用待ち
　　※SO廃止後に、オペレータによるネットワーク設定変更待ちの状態。
　4：移転中
　　※VM支障移転で、移転先として指定され、工事中の状態。
　9：故障
　　※VM支障移転で、移転元として指定されたVM。故障状態。
';
COMMENT ON COLUMN VM_INFO.ABOLITION_TIME IS 'SO廃止された時の日時。
SO廃止されていない、もしくはSO廃止後に正常にN番に割り付けられたVMについては、NULLが設定される。
';
COMMENT ON COLUMN VM_INFO.LAST_UPDATE_ACCOUNT_INFO_ID IS '最後に更新を実施したアカウントのアカウント情報ID。
SOなど、カスコン画面からの更新でない場合は、0 を設定する。';
COMMENT ON COLUMN VM_INFO.LAST_UPDATE_TIME IS '最後に更新が実施された日時。';
COMMENT ON COLUMN VM_INFO.DELETE_FLAG IS '論理削除を示すフラグ。
　false：有効データ
　true：削除データ';
COMMENT ON TABLE VM_RESOURCE_TYPE_MASTER IS 'VMのリソースタイプを管理するマスタ情報。';
COMMENT ON COLUMN VM_RESOURCE_TYPE_MASTER.VM_RESOURCE_TYPE_MASTER_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN VM_RESOURCE_TYPE_MASTER.VM_RESOURCE_TYPE_NAME IS 'VMリソースタイプ名。';
COMMENT ON COLUMN VM_RESOURCE_TYPE_MASTER.CPU_CORE_NUMBER IS 'CPUコア数。';
COMMENT ON COLUMN VM_RESOURCE_TYPE_MASTER.MEMORY_SIZE IS 'メモリサイズ。';
COMMENT ON COLUMN VM_RESOURCE_TYPE_MASTER.HDD_SIZE IS 'HDDサイズ。';
COMMENT ON COLUMN VM_RESOURCE_TYPE_MASTER.VM_DICIDE_UNDER IS 'VM払い出し処理時の判定に使用する、判定基準の下限値。
VM払い出し判定下限値と上限値のセットで使用する。
例）
VMリソースタイプA　VM払い出し判定下限値：0    上限値：100
VMリソースタイプB　VM払い出し判定下限値：101  上限値：200
・N番のスコアが50の場合 → VMリソースタイプA
・N番のスコアが150の場合 → VMリソースタイプB
・N番のスコアが250の場合 → 該当なし
スコアは、以下の式により算出する。
　スコア＝[N番情報.チャネル数]×[係数1]＋[N番に紐づくDBレコード「内線番号情報」の有効レコード数（内線番号数）]×[係数2]
　※係数1：機能設計書 [別紙２]ファイル設計書の「アプリケーション設定ファイル」のシートの「cuscon_vm_channel_rate」の項を参照。
　　係数2：機能設計書 [別紙２]ファイル設計書の「アプリケーション設定ファイル」のシートの「cuscon_vm_terminal_rate」の項を参照。
';
COMMENT ON COLUMN VM_RESOURCE_TYPE_MASTER.VM_DICIDE_TOP IS 'VM払い出し処理時の判定に使用する、判定基準の上限値。
VM払い出し判定下限値と上限値のセットで使用する。
例）
VMリソースタイプA　VM払い出し判定下限値：0    上限値：100
VMリソースタイプB　VM払い出し判定下限値：101  上限値：200
・N番のスコアが50の場合 → VMリソースタイプA
・N番のスコアが150の場合 → VMリソースタイプB
・N番のスコアが250の場合 → 該当なし
スコアは、以下の式により算出する。
　スコア＝[N番情報.チャネル数]×[係数1]＋[N番に紐づくDBレコード「内線番号情報」の有効レコード数（内線番号数）]×[係数2]
　※係数1：機能設計書 [別紙２]ファイル設計書の「アプリケーション設定ファイル」のシートの「cuscon_vm_channel_rate」の項を参照。
　　係数2：機能設計書 [別紙２]ファイル設計書の「アプリケーション設定ファイル」のシートの「cuscon_vm_terminal_rate」の項を参照。
';
COMMENT ON TABLE VM_TRANSFER_QUEUE_INFO IS 'VM支障移転の予約情報。';
COMMENT ON COLUMN VM_TRANSFER_QUEUE_INFO.VM_TRANSFER_QUEUE_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN VM_TRANSFER_QUEUE_INFO.VM_TRANSFER_SRC_VM_INFO_ID IS 'VM支障移転元のVM情報ID。';
COMMENT ON COLUMN VM_TRANSFER_QUEUE_INFO.VM_TRANSFER_DST_VM_INFO_ID IS 'VM支障移転先のVM情報ID。';
COMMENT ON COLUMN VM_TRANSFER_QUEUE_INFO.VM_TRANSFER_RESERVE_DATE IS 'VM支障移転が予約された日時。';
COMMENT ON COLUMN VM_TRANSFER_QUEUE_INFO.VM_TRANSFER_START_DATE IS 'VM支障移転処理を開始した日時。';
COMMENT ON COLUMN VM_TRANSFER_QUEUE_INFO.VM_TRANSFER_END_DATE IS 'VM支障移転処理が完了した日時。';
COMMENT ON COLUMN VM_TRANSFER_QUEUE_INFO.VM_TRANSFER_STATUS IS 'VM支障移転の処理状況を示す。
　1：移転待ち
　2：移転中
　3：正常終了
　4：正常終了・ファイル移転NG
　9：異常終了
';
COMMENT ON COLUMN VM_TRANSFER_QUEUE_INFO.RESERVER_ACCOUNT_INFO_ID IS 'VM支障移転を予約したアカウントのアカウント情報ID。';

COMMENT ON TABLE SERVER_RENEW_QUEUE_INFO IS '内線サーバ設定反映の予約情報。';
COMMENT ON COLUMN SERVER_RENEW_QUEUE_INFO.server_renew_queue_info_id IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN SERVER_RENEW_QUEUE_INFO.vm_info_id IS '内線サーバ設定反映の対象となるVMを示すVM情報ID。';
COMMENT ON COLUMN SERVER_RENEW_QUEUE_INFO.server_renew_reserve_date IS '内線サーバ設定反映が予約された日時。';
COMMENT ON COLUMN SERVER_RENEW_QUEUE_INFO.server_renew_start_date IS '内線サーバ設定反映を開始した日時。';
COMMENT ON COLUMN SERVER_RENEW_QUEUE_INFO.server_renew_end_date IS '内線サーバ設定反映が完了した日時。';
COMMENT ON COLUMN SERVER_RENEW_QUEUE_INFO.server_renew_status IS '内線サーバ設定反映の処理状況を示す。
　1：反映待ち
　2：反映中
　3：正常終了
　9：異常終了
';
COMMENT ON COLUMN SERVER_RENEW_QUEUE_INFO.server_renew_err_cause IS '内線サーバ設定反映で、異常発生時の要因。';
COMMENT ON COLUMN SERVER_RENEW_QUEUE_INFO.server_renew_account_info_id IS '内線サーバ設定反映を予約したアカウントのアカウント情報ID。';


/* COMMENT MAC_ADDRESS_INFO */
COMMENT ON TABLE MAC_ADDRESS_INFO IS 'SO処理の中でIP Phoneに対して払い出すMACアドレスの情報。';
COMMENT ON COLUMN MAC_ADDRESS_INFO.MAC_ADDRESS_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN MAC_ADDRESS_INFO.SUPPLY_TYPE IS '端末の提供形態、内線番号情報テーブルの提供形態と同じ値を利用する。(Step2.8では2種のIP phoneのみ利用)
3:KX-UT136N
5:KX-UT123N';
COMMENT ON COLUMN MAC_ADDRESS_INFO.ADDITIONAL_STYLE IS '登録されたレコードがINSERTで追加されたものかAPPENDで追加されたものかを示す。
1:INSERTで追加されたレコード
2:APPENDで追加されたレコード';
COMMENT ON COLUMN MAC_ADDRESS_INFO.MAC_ADDRESS IS '端末のMACアドレス。';
COMMENT ON COLUMN MAC_ADDRESS_INFO.ADDITIONAL_ORDER IS 'シーケンス値。
MACアドレスを登録するcsvを読み込むごとに増加する。
1ファイルで登録したレコードは全て同じ値となる。';
COMMENT ON COLUMN MAC_ADDRESS_INFO.ORDER_LINE IS '1つのcsvファイルの中で、追加形式ごとに何番目に記載されていたかを登録する';
COMMENT ON COLUMN MAC_ADDRESS_INFO.LAST_UPDATE_ACCOUNT_INFO_ID IS '最後に更新を実施したアカウントのアカウント情報ID。

SOなど、カスコン画面からの更新でない場合は、0 を設定する。';
COMMENT ON COLUMN MAC_ADDRESS_INFO.LAST_UPDATE_TIME IS '最後に更新が実施された日時。';

COMMENT ON TABLE MUSIC_INFO IS '保留音などの音源情報。
カスコン画面より登録される。';
COMMENT ON COLUMN MUSIC_INFO.MUSIC_INFO_ID IS '本テーブルのインデックスとして使用する一意なID。';
COMMENT ON COLUMN MUSIC_INFO.N_NUMBER_INFO_ID IS 'どのN番の音源情報かを示すN番情報ID。';
COMMENT ON COLUMN MUSIC_INFO.MUSIC_TYPE IS '音源の種別（使用用途）を示す。
　1：保留音';
COMMENT ON COLUMN MUSIC_INFO.MUSIC_ORI_NAME IS 'カスコンから登録された音源の元のファイル名。';
COMMENT ON COLUMN MUSIC_INFO.MUSIC_ENCODE_DATA IS '変換後の音源のバイナリデータ。';
COMMENT ON COLUMN MUSIC_INFO.LAST_UPDATE_ACCOUNT_INFO_ID IS '最後に更新を実施したアカウントのアカウント情報ID。
SOなど、カスコン画面からの更新でない場合は、0 を設定する。';
COMMENT ON COLUMN MUSIC_INFO.LAST_UPDATE_TIME IS '最後に更新が実施された日時。';


/* (C) NTT Communications  2015  All Rights Reserved */