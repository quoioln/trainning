package com.ntt.smartpbx.asterisk.model;

import java.util.ArrayList;

/**
 * 名称:着信グループ一括設定機能のCSV取込結果を管理するクラス
 * 機能概要:着信グループ一括設定機能で操作種別:追加でグループ種別がコールピックアップのCSVが取込まれた際の情報するためのクラス
 */

public class IncomingGroupDataForAddPickupCsv {

    private long incomingGroupInfoIdForAddPickupCsv = -1;
    private ArrayList<Long> addExtensionNumberInfoIdArray = new ArrayList<Long>();
    
    /**
     * コンストラクタ
     * @param incomingGroupInfoIdForAddPickupCsv　追加対象の着信グループ情報.着信グループ情報ID
     * @param addExtensionNumberInfoIdArray      追加対象の着信グループに含まれている子番号の「内線番号情報.内線番号情報ID」のリスト　※追加された子番号が存在しない場合(null)はエラーとする
     */
    public IncomingGroupDataForAddPickupCsv(
            long incomingGroupInfoIdForAddPickupCsv,
            ArrayList<Long> addExtensionNumberInfoIdArray) {
        
        this.incomingGroupInfoIdForAddPickupCsv = incomingGroupInfoIdForAddPickupCsv;
        setAddExtensionNumberInfoIdArray(addExtensionNumberInfoIdArray);
    }

    public long getIncomingGroupInfoIdForAddPickupCsv() {
        return incomingGroupInfoIdForAddPickupCsv;
    }

    public void setIncomingGroupInfoIdForAddPickupCsv(long incomingGroupInfoIdForAddPickupCsv) {
        this.incomingGroupInfoIdForAddPickupCsv = incomingGroupInfoIdForAddPickupCsv;
    }

    public ArrayList<Long> getAddExtensionNumberInfoIdArray() {
        return addExtensionNumberInfoIdArray;
    }

    public void setAddExtensionNumberInfoIdArray(ArrayList<Long> addExtensionNumberInfoIdArray) {
        if (addExtensionNumberInfoIdArray == null) {
            throw new IllegalArgumentException("追加対象の着信グループに含まれている子番号の「内線番号情報.内線番号情報ID」のリストがありません");
        }
        this.addExtensionNumberInfoIdArray = addExtensionNumberInfoIdArray;
    }
    
}

//(C) NTT Communications  2014  All Rights Reserved