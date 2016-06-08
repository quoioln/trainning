package com.ntt.smartpbx.asterisk.model;

/**
 * 名称:着信グループ一括設定機能のCSV取込結果を管理するクラス
 * 機能概要:着信グループ一括設定機能で操作種別:削除でグループ種別が一斉のCSVが取込まれた際の情報するためのクラス
 */

public class IncomingGroupDataForDelVolleyCsv {

    private long extensionNumberInfoIdForDelVolleyCsv = -1;
    
    /**
     * コンストラクタ
     * @param extensionNumberInfoIdForDelVolleyCsv　削除前の内線代表番号の「内線番号情報.内線番号情報ID」
     */
    public IncomingGroupDataForDelVolleyCsv(
            long extensionNumberInfoIdForDelVolleyCsv) {
        
        this.extensionNumberInfoIdForDelVolleyCsv = extensionNumberInfoIdForDelVolleyCsv;
    }

    public long getExtensionNumberInfoIdForDelVolleyCsv() {
        return extensionNumberInfoIdForDelVolleyCsv;
    }

    public void setExtensionNumberInfoIdForDelVolleyCsv(long extensionNumberInfoIdForDelVolleyCsv) {
        this.extensionNumberInfoIdForDelVolleyCsv = extensionNumberInfoIdForDelVolleyCsv;
    }

}

//(C) NTT Communications  2014  All Rights Reserved