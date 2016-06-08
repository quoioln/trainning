package com.ntt.smartpbx.asterisk.model;

import java.util.ArrayList;

/**
 * 名称:着信グループ一括設定機能のCSV取込結果を管理するクラス
 * 機能概要:着信グループ一括設定機能で操作種別:変更でグループ種別が一斉のCSVが取込まれた際の情報するためのクラス
 */

public class IncomingGroupDataForModPickupCsv {

    private long incomingGroupInfoIdForModPickupCsv = -1;
    private ArrayList<Long> addExtensionNumberInfoIdArray = new ArrayList<Long>();
    private ArrayList<Long> delExtensionNumberInfoIdArray = new ArrayList<Long>();
    
    /**
     * コンストラクタ
     * @param incomingGroupInfoIdForModPickupCsv 変更対象の着信グループ情報.着信グループ情報ID
     * @param addExtensionNumberInfoIdArray      変更対象の着信グループに追加された子番号の内線番号情報.内線番号情報ID　※追加された子番号が存在しない場合(null)は許容する
     * @param delExtensionNumberInfoIdArray      変更対象の着信グループに削除された子番号の内線番号情報.内線番号情報ID　※削除された子番号が存在しない場合(null)は許容する
     */
    public IncomingGroupDataForModPickupCsv(
            long incomingGroupInfoIdForModPickupCsv,
            ArrayList<Long> addExtensionNumberInfoIdArray,
            ArrayList<Long> delExtensionNumberInfoIdArray) {
        
        this.incomingGroupInfoIdForModPickupCsv = incomingGroupInfoIdForModPickupCsv;
        setAddExtensionNumberInfoIdArray(addExtensionNumberInfoIdArray);
        setDelExtensionNumberInfoIdArray(delExtensionNumberInfoIdArray);
    }

    public long getIncomingGroupInfoIdForModPickupCsv() {
        return incomingGroupInfoIdForModPickupCsv;
    }

    public void setIncomingGroupInfoIdForModPickupCsv(long incomingGroupInfoIdForModPickupCsv) {
        this.incomingGroupInfoIdForModPickupCsv = incomingGroupInfoIdForModPickupCsv;
    }

    public ArrayList<Long> getAddExtensionNumberInfoIdArray() {
        return addExtensionNumberInfoIdArray;
    }

    public void setAddExtensionNumberInfoIdArray(ArrayList<Long> addExtensionNumberInfoIdArray) {
        if (addExtensionNumberInfoIdArray != null) {
            this.addExtensionNumberInfoIdArray = addExtensionNumberInfoIdArray;
        }
    }

    public ArrayList<Long> getDelExtensionNumberInfoIdArray() {
        return delExtensionNumberInfoIdArray;
    }

    public void setDelExtensionNumberInfoIdArray(ArrayList<Long> delExtensionNumberInfoIdArray) {
        if (delExtensionNumberInfoIdArray != null) {
            this.delExtensionNumberInfoIdArray = delExtensionNumberInfoIdArray;
        }
    }

}

//(C) NTT Communications  2014  All Rights Reserved