package com.ntt.smartpbx.asterisk.model;

import java.util.ArrayList;

/**
 * 名称:着信グループ一括設定機能のCSV取込結果を管理するクラス
 * 機能概要:着信グループ一括設定機能で操作種別:削除でグループ種別がコールピックアップのCSVが取込まれた際の情報するためのクラス
 */

public class IncomingGroupDataForDelPickupCsv {

    private long incomingGroupInfoIdForDelPickupCsv = -1;
    private ArrayList<Long> delExtensionNumberInfoIdArray = new ArrayList<Long>();
    
    /**
     * コンストラクタ
     * @param incomingGroupInfoIdForDelPickupCsv　削除対象の着信グループ情報.着信グループ情報ID
     * @param delExtensionNumberInfoIdArray      削除対象の着信グループに含まれていた子番号の「内線番号情報.内線番号情報ID」のリスト　※削除された子番号が存在しない場合(null)はエラーとする
     */
    public IncomingGroupDataForDelPickupCsv(
            long incomingGroupInfoIdForDelPickupCsv,
            ArrayList<Long> delExtensionNumberInfoIdArray) {
        
        this.incomingGroupInfoIdForDelPickupCsv = incomingGroupInfoIdForDelPickupCsv;
        setDelExtensionNumberInfoIdArray(delExtensionNumberInfoIdArray);
    }

    public long getIncomingGroupInfoIdForDelPickupCsv() {
        return incomingGroupInfoIdForDelPickupCsv;
    }

    public void setIncomingGroupInfoIdForDelPickupCsv(long incomingGroupInfoIdForDelPickupCsv) {
        this.incomingGroupInfoIdForDelPickupCsv = incomingGroupInfoIdForDelPickupCsv;
    }

    public ArrayList<Long> getDelExtensionNumberInfoIdArray() {
        return delExtensionNumberInfoIdArray;
    }

    public void setDelExtensionNumberInfoIdArray(ArrayList<Long> delExtensionNumberInfoIdArray) {
        if (delExtensionNumberInfoIdArray == null) {
            throw new IllegalArgumentException("削除対象の着信グループに含まれていた子番号の「内線番号情報.内線番号情報ID」のリストがありません");
        }
        this.delExtensionNumberInfoIdArray = delExtensionNumberInfoIdArray;
    }

}

//(C) NTT Communications  2014  All Rights Reserved