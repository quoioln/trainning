
/* VM_INFO */
ALTER TABLE VM_INFO
ADD  WHOLESALE_USABLE_FLAG BOOLEAN DEFAULT 'false',
ADD  WHOLESALE_TYPE INT,
ADD  WHOLESALE_PRIVATE_IP INET,
ADD  WHOLESALE_FQDN_IP VARCHAR(256) ;

COMMENT ON COLUMN VM_INFO.WHOLESALE_USABLE_FLAG IS 'VMの卸（閉域網接続）対応/非対応を判別するフラグ。
 false:非対応
 true:対応';
COMMENT ON COLUMN VM_INFO.WHOLESALE_TYPE IS '卸事業者を識別する値
　1：（未使用）
　2：東発展';
COMMENT ON COLUMN VM_INFO.WHOLESALE_PRIVATE_IP IS 'VMの卸（閉域網接続）側のNICに割り当てられるプライベートIPアドレス。';
COMMENT ON COLUMN VM_INFO.WHOLESALE_FQDN_IP IS '卸（閉域網）でVMに割り当てられるFQDN/プライベートIPアドレス。
※IPアドレス、FQDNの両形式をとりうる。';
COMMENT ON COLUMN VM_INFO.CONNECT_TYPE IS 'VMの接続形態を示す値。
NULL：インタネット接続のみ（既設）
　　0：インタネット接続のみ（未使用値）
　　1：VPN接続のみ
　　2：インタネット接続/VPN接続の併用
　　3：卸専用閉域網接続のみ（未使用値）
　　4：インタネット接続/卸専用閉域網接続の併用
＜データ条件＞
VPN対応フラグ=falseの場合：NULL';
COMMENT ON COLUMN EXTENSION_NUMBER_INFO.AUTO_SETTING_TYPE IS '端末自動設定の接続種別を示す値。
NULL：インタネット
（VPN非契約）
　　0：インタネット
（VPNとインタネットの併用を契約で、インタネットを選択）
　　1：VPN
　　2：卸専用網
端末種別が0（IP Phone）の場合に使用する。
＜データ条件＞
端末種別が0（IP Phone）以外の場合はNULL。
端末種別が0（IP Phone）かつ、端末自動設定フラグがfalseの場合はNULL。
端末種別が0（IP Phone）かつ、端末自動設定フラグがtrueの場合は非NULL。';
COMMENT ON COLUMN OUTSIDE_CALL_INFO.OUTSIDE_CALL_SERVICE_TYPE IS '外線のサービス種別。
　1：050plus for Biz
　2：IP Voice for SmartPBX
　3：転送GW(C)＋ひかり電話/IP Voice（ひかり電話）
　4：転送GW(i)＋ひかり電話/IP Voice（ひかり電話）
　5：転送GW＋IP Voice(インターネット併用)
　6：転送GW＋IP Voice(VPN接続)
　7：転送GW(N)＋ひかり電話/IP Voice（ひかり電話）
　8：転送GW(N)＋ひかり電話(専用網接続)';
