package com.ntt.smartpbx.asterisk.model;

/**
 * 名称:着信グループ一括設定機能のCSV取込結果を管理するクラス
 * 機能概要:着信グループ一括設定機能で操作種別:追加でグループ種別が順次のCSVが取込まれた際の情報するためのクラス
 */

public class IncomingGroupDataForAddSlideCsv {

    private long incomingGroupInfoIdForAddSlideCsv = -1;
    
    /**
     * コンストラクタ
     * @param IncomingGroupDataForAddSlideCsv　追加された着信グループ情報.着信グループ情報ID
     */
    public IncomingGroupDataForAddSlideCsv(
            long incomingGroupInfoIdForAddSlideCsv) {
        
        this.incomingGroupInfoIdForAddSlideCsv = incomingGroupInfoIdForAddSlideCsv;
    }

    public long getIncomingGroupInfoIdForAddSlideCsv() {
        return incomingGroupInfoIdForAddSlideCsv;
    }

    public void setIncomingGroupInfoIdForAddSlideCsv(long incomingGroupInfoIdForAddSlideCsv) {
        this.incomingGroupInfoIdForAddSlideCsv = incomingGroupInfoIdForAddSlideCsv;
    }
    
}

//(C) NTT Communications  2014  All Rights Reserved