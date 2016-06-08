package com.ntt.smartpbx.asterisk.model;

/**
 * 名称:着信グループ一括設定機能のCSV取込結果を管理するクラス
 * 機能概要:着信グループ一括設定機能で操作種別:変更でグループ種別が順次のCSVが取込まれた際の情報するためのクラス
 */

public class IncomingGroupDataForModSlideCsv {

    private long oldExtensionNumberInfoIdForModSlideCsv = -1;
    private long newIncomingGroupInfoIdForModSlideCsv = -1;
    
    /**
     * コンストラクタ
     * @param oldExtensionNumberInfoIdForModSlideCsv　変更前の代表番号の内線番号情報.内線番号情報ID
     * @param newIncomingGroupInfoIdForModSlideCsv　変更された着信グループ情報.着信グループ情報ID
     */
    public IncomingGroupDataForModSlideCsv(
            long oldExtensionNumberInfoIdForModSlideCsv,
            long newIncomingGroupInfoIdForModSlideCsv) {
        
        this.oldExtensionNumberInfoIdForModSlideCsv = oldExtensionNumberInfoIdForModSlideCsv;
        this.newIncomingGroupInfoIdForModSlideCsv = newIncomingGroupInfoIdForModSlideCsv;
    }

    public long getOldExtensionNumberInfoIdForModSlideCsv() {
        return oldExtensionNumberInfoIdForModSlideCsv;
    }

    public void setOldExtensionNumberInfoIdForModSlideCsv(long oldExtensionNumberInfoIdForModSlideCsv) {
        this.oldExtensionNumberInfoIdForModSlideCsv = oldExtensionNumberInfoIdForModSlideCsv;
    }

    public long getNewIncomingGroupInfoIdForModSlideCsv() {
        return newIncomingGroupInfoIdForModSlideCsv;
    }

    public void setNewIncomingGroupInfoIdForModSlideCsv(long newincomingGroupInfoIdForModSlideCsv) {
        this.newIncomingGroupInfoIdForModSlideCsv = newincomingGroupInfoIdForModSlideCsv;
    }
    
}

//(C) NTT Communications  2014  All Rights Reserved