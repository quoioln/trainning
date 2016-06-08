package com.ntt.smartpbx.asterisk.model;

/**
 * 名称:着信グループ一括設定機能のCSV取込結果を管理するクラス
 * 機能概要:着信グループ一括設定機能で操作種別:追加でグループ種別が一斉のCSVが取込まれた際の情報するためのクラス
 */

public class IncomingGroupDataForAddVolleyCsv {

    private long incomingGroupInfoIdForAddVolleyCsv = -1;
    
    /**
     * コンストラクタ
     * @param incomingGroupInfoIdForAddVolleyCsv　追加された着信グループ情報.着信グループ情報ID
     */
    public IncomingGroupDataForAddVolleyCsv(
            long incomingGroupInfoIdForAddVolleyCsv) {
        
        this.incomingGroupInfoIdForAddVolleyCsv = incomingGroupInfoIdForAddVolleyCsv;
    }

    public long getIncomingGroupInfoIdForAddVolleyCsv() {
        return incomingGroupInfoIdForAddVolleyCsv;
    }

    public void setIncomingGroupInfoIdForAddVolleyCsv(long incomingGroupInfoIdForAddVolleyCsv) {
        this.incomingGroupInfoIdForAddVolleyCsv = incomingGroupInfoIdForAddVolleyCsv;
    }
    
}

//(C) NTT Communications  2014  All Rights Reserved