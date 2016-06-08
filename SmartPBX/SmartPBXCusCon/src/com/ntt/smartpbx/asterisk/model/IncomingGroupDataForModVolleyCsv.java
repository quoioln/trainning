package com.ntt.smartpbx.asterisk.model;

/**
 * 名称:着信グループ一括設定機能のCSV取込結果を管理するクラス
 * 機能概要:着信グループ一括設定機能で操作種別:変更でグループ種別が一斉のCSVが取込まれた際の情報するためのクラス
 */

public class IncomingGroupDataForModVolleyCsv {

    private long oldExtensionNumberInfoIdForModVolleyCsv = -1;
    private long newIncomingGroupInfoIdForModVolleyCsv = -1;
    
    /**
     * コンストラクタ
     * @param oldExtensionNumberInfoIdForModVolleyCsv　変更前の代表番号の内線番号情報.内線番号情報ID
     * @param newIncomingGroupInfoIdForModVolleyCsv　変更された着信グループ情報.着信グループ情報ID
     */
    public IncomingGroupDataForModVolleyCsv(
            long oldExtensionNumberInfoIdForModVolleyCsv,
            long newIncomingGroupInfoIdForModVolleyCsv) {
        
        this.oldExtensionNumberInfoIdForModVolleyCsv = oldExtensionNumberInfoIdForModVolleyCsv;
        this.newIncomingGroupInfoIdForModVolleyCsv = newIncomingGroupInfoIdForModVolleyCsv;
    }

    public long getoldExtensionNumberInfoIdForModVolleyCsv() {
        return oldExtensionNumberInfoIdForModVolleyCsv;
    }

    public void setoldExtensionNumberInfoIdForModVolleyCsv(long oldExtensionNumberInfoIdForModVolleyCsv) {
        this.oldExtensionNumberInfoIdForModVolleyCsv = oldExtensionNumberInfoIdForModVolleyCsv;
    }

    public long getNewIncomingGroupInfoIdForModVolleyCsv() {
        return newIncomingGroupInfoIdForModVolleyCsv;
    }

    public void setNewincomingGroupInfoIdForModVolleyCsv(long newIncomingGroupInfoIdForModVolleyCsv) {
        this.newIncomingGroupInfoIdForModVolleyCsv = newIncomingGroupInfoIdForModVolleyCsv;
    }

}

//(C) NTT Communications  2014  All Rights Reserved