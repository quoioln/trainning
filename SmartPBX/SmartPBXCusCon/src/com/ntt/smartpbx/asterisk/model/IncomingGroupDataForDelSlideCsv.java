package com.ntt.smartpbx.asterisk.model;

/**
 * 名称:着信グループ一括設定機能のCSV取込結果を管理するクラス
 * 機能概要:着信グループ一括設定機能で操作種別:削除でグループ種別が順次のCSVが取込まれた際の情報するためのクラス
 */

public class IncomingGroupDataForDelSlideCsv {

    private long extensionNumberInfoIdForDelSlideCsv = -1;
    
    /**
     * コンストラクタ
     * @param extensionNumberInfoIdForDelSlideCsv　削除前の内線代表番号の「内線番号情報.内線番号情報ID」
     */
    public IncomingGroupDataForDelSlideCsv(
            long extensionNumberInfoIdForDelSlideCsv) {
        
        this.extensionNumberInfoIdForDelSlideCsv = extensionNumberInfoIdForDelSlideCsv;
    }

    public long getExtensionNumberInfoIdForDelSlideCsv() {
        return extensionNumberInfoIdForDelSlideCsv;
    }

    public void setExtensionNumberInfoIdForDelSlideCsv(long extensionNumberInfoIdForDelSlideCsv) {
        this.extensionNumberInfoIdForDelSlideCsv = extensionNumberInfoIdForDelSlideCsv;
    }

}

//(C) NTT Communications  2014  All Rights Reserved