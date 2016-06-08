package com.ntt.smartpbx.asterisk.config;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.asterisk.ami.Ami;
import com.ntt.smartpbx.asterisk.util.security.Sftp;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForAddPickupCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForAddSlideCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForAddVolleyCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForDelPickupCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForDelSlideCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForDelVolleyCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForModPickupCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForModSlideCsv;
import com.ntt.smartpbx.asterisk.model.IncomingGroupDataForModVolleyCsv;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.AbsenceBehaviorInfo;
import com.ntt.smartpbx.model.db.CallRegulationInfo;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.IncomingGroupInfo;
import com.ntt.smartpbx.model.db.NNumberInfo;
import com.ntt.smartpbx.model.db.OutsideCallIncomingInfo;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.db.PbxFileBackupInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称:Asteriskコンフィグ生成・反映クラス 機能概要:Asteriskコンフィグファイルの生成と反映を行う 
 */
public class ConfigCreator {

    private static class Inner{
        private static ConfigCreator theInstance = new ConfigCreator();
    }


    private static ConfigCreatorProperties configCreatorProperties = null;

    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(ConfigCreator.class);    
    // End step 2.5 #1946

    /**
     * 
     * コンストラクタ
     * @throws Exception 
     */
    private ConfigCreator(){
        try{
            configCreatorProperties = new ConfigCreatorProperties();
        }catch(Exception e){
            // 初期化失敗
            log.debug(e.toString());
            throw new RuntimeException("Singleton constructor failure!");
        }
    }

    /**
     * シングルトンモデルで実装する。<br>
     * 外部(カスコンGUI)からマルチスレッドで実行されると<br>
     * 次の課題があるため、まずは常にシングルスレッドで実行されるようにする。<br>
     * <br>
     * ①Asterisk設定ファイル作成されることで複数のディスクI/O処理が行われ、<br>
     * 　カスコンサーバーの負荷が増加し、カスコン自体のレスポンスが劣化する<br>
     * ②FTPとAsteriskコマンドが並列に実行されるが、安全に動作するか不明<br>
     * <br>
     * 改良方針として、並列に実行される数を制限するワーカースレッドモデルの使用と<br>
     * 1VMへのコンフィグ反映処理は並列に行わないようにする処理を実装することで<br>
     * 安全に性能を向上させることが可能となる。<br>
     */

    /**
     * コンフィグ生成クラスのインスタンスを取得する<br>
     * 
     * @return コンフィグ生成クラスのインスタンス
     */
    public static ConfigCreator getInstance()
    {
        try{
            return Inner.theInstance;
        }
        catch(ExceptionInInitializerError e){
            throw e;
        }
    }  

    /**
     * JUnitのみ使用するため、他のクラスが使用してはいけません
     * 
     * @param theInstance 
     */
    @SuppressWarnings("unused")
    private static void setInstance(ConfigCreator theInstance)
    {
            Inner.theInstance = theInstance;
    } 

    /** 
     * G0601_着信グループ設定画面(一覧表示)でCSV取込ボタン押下時に実行する。
     * 取り込んだCSVの内容をAsteriskへ反映する。
     * @param loginId　ログインID
     * @param dbConnection DBコネクション
     * @param nNumberInfoId　N番情報.N番情報ID
     * @param incomingGroupAddSlideList　　　　CSV取込でグループ種別「順次」で追加対象のリスト
     * @param incomingGroupAddVolleyList　　　CSV取込でグループ種別「一斉」で追加対象のリスト
     * @param incomingGroupAddPickupList　　　CSV取込でグループ種別「コールピックアップ」で追加対象のリスト
     * @param incomingGroupModSlideList　　　　CSV取込でグループ種別「順次」で変更対象のリスト
     * @param incomingGroupModVolleyList　　　CSV取込でグループ種別「一斉」で変更対象のリスト
     * @param incomingGroupModPickupList　　　CSV取込でグループ種別「コールピックアップ」で変更対象のリスト
     * @param incomingGroupDelSlideList　　　　CSV取込でグループ種別「順次」で削除対象のリスト
     * @param incomingGroupDelVolleyList　　　CSV取込でグループ種別「一斉」で削除対象のリスト
     * @param incomingGroupDelPickupList　　　CSV取込でグループ種別「コールピックアップ」で削除対象のリスト
     */
    //　step1.6 start
    public void importGroupCsv(
            String loginId,
            Connection dbConnection,
            long nNumberInfoId,
            List<IncomingGroupDataForAddSlideCsv> incomingGroupAddSlideList,
            List<IncomingGroupDataForAddVolleyCsv> incomingGroupAddVolleyList,
            List<IncomingGroupDataForAddPickupCsv> incomingGroupAddPickupList,
            List<IncomingGroupDataForModSlideCsv> incomingGroupModSlideList,
            List<IncomingGroupDataForModVolleyCsv> incomingGroupModVolleyList,
            List<IncomingGroupDataForModPickupCsv> incomingGroupModPickupList,
            List<IncomingGroupDataForDelSlideCsv> incomingGroupDelSlideList,
            List<IncomingGroupDataForDelVolleyCsv> incomingGroupDelVolleyList,
            List<IncomingGroupDataForDelPickupCsv> incomingGroupDelPickupList
            ) throws Exception {
        
        try {
            log.debug("method start : importGroupCsv() ");
            
            // 引数チェック
            if (dbConnection == null) { throw new IllegalArgumentException("DBコネクションがnullです。"); }
            if (loginId == null) { loginId = "null"; }
            
            //Step2.8 START #2252
            if( null == incomingGroupAddSlideList  ){ throw new IllegalArgumentException("操作種別:追加、グループ種別:順次着信の情報がnullです。"); }
            if( null == incomingGroupAddVolleyList ){ throw new IllegalArgumentException("操作種別:追加、グループ種別:一斉着信の情報がnullです。"); }
            if( null == incomingGroupAddPickupList ){ throw new IllegalArgumentException("操作種別:追加、グループ種別:コールピックアップの情報がnullです。"); }
            if( null == incomingGroupModSlideList  ){ throw new IllegalArgumentException("操作種別:変更、グループ種別:順次着信の情報がnullです。"); }
            if( null == incomingGroupModVolleyList ){ throw new IllegalArgumentException("操作種別:変更、グループ種別:一斉着信の情報がnullです。"); }
            if( null == incomingGroupModPickupList ){ throw new IllegalArgumentException("操作種別:変更、グループ種別:コールピックアップの情報がnullです。"); }
            if( null == incomingGroupDelSlideList  ){ throw new IllegalArgumentException("操作種別:削除、グループ種別:順次着信の情報がnullです。"); }
            if( null == incomingGroupDelVolleyList ){ throw new IllegalArgumentException("操作種別:削除、グループ種別:一斉着信の情報がnullです。"); }
            if( null == incomingGroupDelPickupList ){ throw new IllegalArgumentException("操作種別:削除、グループ種別:コールピックアップの情報がnullです。"); }
            //Step2.8 END #2252
            
            // 更新対象の内線サーバ設定ファイルバックアップ情報をロック(同じN番を全ロック)
            executeLock(dbConnection, nNumberInfoId);
            
            // 変数を宣言
            NNumberInfo nNumberInfo = getNNumberInfo(dbConnection, nNumberInfoId);
            List<AbsenceBehaviorInfo> absenceBehaviorInfoList = new ArrayList<AbsenceBehaviorInfo>();
            List<OutsideCallInfo> outsideCallInfoList = new ArrayList<OutsideCallInfo>();
            List<List<String>> childExtensionNumberInfoList = new ArrayList<List<String>>();
            List<Integer> groupCallTypeList = new ArrayList<Integer>();
            List<String> localFileList = new ArrayList<String>();
            List<String> dstFileList = new ArrayList<String>();
            List<String> dstFileBackUpList = new ArrayList<String>();
            List<String> dstFileTmpList = new ArrayList<String>();
            List<String> pickUpGroupList = new ArrayList<String>();
            
            // extension_内線番号_in.conf用
            List<ExtensionNumberInfo> extensionNumberInfoListExtIn = new ArrayList<ExtensionNumberInfo>();
            
            // sip_内線番号_in.conf用
            List<ExtensionNumberInfo> extNumberInfoListSipIn = new ArrayList<ExtensionNumberInfo>();
            
            // #768 START  (extensions_group.conf)
            // 変数を宣言(extensions_group.conf用)　追加コールピックアップ用
            List<IncomingGroupInfo> incomingGroupInfoListExtGroupForAdd = new ArrayList<IncomingGroupInfo>();
            List<List<String>> childExtensionNumberListExtGroupForAdd = new ArrayList<List<String>>();
            // #768 END    (extensions_group.conf)
            // 変数を宣言(extensions_group.conf用)　変更コールピックアップ用
            List<IncomingGroupInfo> incomingGroupInfoListExtGroupForMod = new ArrayList<IncomingGroupInfo>();
            List<List<String>> childExtensionNumberListExtGroupForMod = new ArrayList<List<String>>();
            // 変数を宣言(extensions_group.conf用)　削除コールピックアップ用
            List<IncomingGroupInfo> incomingGroupInfoListExtGroupForDel = new ArrayList<IncomingGroupInfo>();
            List<List<String>> childExtensionNumberListExtGroupForDel = new ArrayList<List<String>>();
            
            // #1348 start 
            // extensions_group.confのためのフラグ宣言
            boolean incomingGroupInfoListPickupExtGroupForAdd = false;
            boolean incomingGroupInfoListPickupExtGroupForMod = false;
            boolean incomingGroupInfoListPickupExtGroupForDel = false;
            // #1348 end
            
            try {
                
                /** CSV取込で追加、グループ種別が順次着信の数だけ実行する */
                for (IncomingGroupDataForAddSlideCsv tmpAddSlideCsvData : incomingGroupAddSlideList){
                    
                    if (tmpAddSlideCsvData == null) {
                        throw new IllegalArgumentException("操作種別:追加、グループ種別:順次着信の情報が不正です。");
                    }
                    
                    log.info("操作種別 = 追加、グループ種別 = 順次着信の着信グループ情報一括設定ファイルが取り込まれました。");
                    log.debug("着信グループ情報ID = " + tmpAddSlideCsvData.getIncomingGroupInfoIdForAddSlideCsv());

                    // 着信グループ情報IDから必要なデータをセット
                    setExtensionsExtensionNumberInConfDataByIncomingGroupInfoId(
                            dbConnection,
                            tmpAddSlideCsvData.getIncomingGroupInfoIdForAddSlideCsv(),
                            extensionNumberInfoListExtIn,
                            absenceBehaviorInfoList,
                            outsideCallInfoList,
                            childExtensionNumberInfoList,
                            groupCallTypeList);
                    
                    // 転送先ディレクトリ情報取得メソッド呼出
                    ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberInConf(
                            localFileList,
                            dstFileList,
                            dstFileBackUpList,
                            dstFileTmpList,
                            nNumberInfo,
                            extensionNumberInfoListExtIn);
    
                    // ファイル生成メソッド呼出
                    ConfigCreatorFile.getInstance().createExtensionsExtensionNumberInConf(extensionNumberInfoListExtIn,
                            absenceBehaviorInfoList,
                            outsideCallInfoList,
                            groupCallTypeList,
                            childExtensionNumberInfoList,
                            nNumberInfo);
                }
                
                /** CSV取込で追加、グループ種別が一斉着信の数だけ実行する */
                for (IncomingGroupDataForAddVolleyCsv tmpAddVolleyCsvData : incomingGroupAddVolleyList) {
                    
                    if (tmpAddVolleyCsvData == null) {
                        throw new IllegalArgumentException("操作種別:追加、グループ種別:一斉着信の情報が不正です。");
                    }
                    
                    log.info("操作種別 = 追加、グループ種別 = 一斉着信の着信グループ情報一括設定ファイルが取り込まれました。");
                    log.debug("着信グループ情報ID = " + tmpAddVolleyCsvData.getIncomingGroupInfoIdForAddVolleyCsv());
                    
                    // 着信グループ情報IDから必要なデータをセット
                    setExtensionsExtensionNumberInConfDataByIncomingGroupInfoId(
                            dbConnection,
                            tmpAddVolleyCsvData.getIncomingGroupInfoIdForAddVolleyCsv(),
                            extensionNumberInfoListExtIn,
                            absenceBehaviorInfoList,
                            outsideCallInfoList,
                            childExtensionNumberInfoList,
                            groupCallTypeList);
    
                    // 転送先ディレクトリ情報取得メソッド呼出
                    ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberInConf(
                            localFileList,
                            dstFileList,
                            dstFileBackUpList,
                            dstFileTmpList,
                            nNumberInfo,
                            extensionNumberInfoListExtIn);
    
                    // ファイル生成メソッド呼出
                    ConfigCreatorFile.getInstance().createExtensionsExtensionNumberInConf(extensionNumberInfoListExtIn,
                            absenceBehaviorInfoList,
                            outsideCallInfoList,
                            groupCallTypeList,
                            childExtensionNumberInfoList,
                            nNumberInfo);  

                }
                
                /** CSV取込で追加、グループ種別がコールピックアップの数だけ実行する */
                for (IncomingGroupDataForAddPickupCsv tmpAddPickupCsvData : incomingGroupAddPickupList){
                    
                    if (tmpAddPickupCsvData == null) {
                        throw new IllegalArgumentException("操作種別:追加、グループ種別:コールピックアップの情報が不正です。");
                    }
                    
                    log.info("操作種別 = 追加、グループ種別 = コールピックアップの着信グループ情報一括設定ファイルが取り込まれました。");
                    log.debug("着信グループ情報ID = " + tmpAddPickupCsvData.getIncomingGroupInfoIdForAddPickupCsv());
                    
                    if (tmpAddPickupCsvData.getAddExtensionNumberInfoIdArray() == null) {
                        throw new IllegalArgumentException("追加対象の着信グループに含まれている子番号の「内線番号情報.内線番号情報ID」のリストの情報がありません。");
                    }

                    for (Long extensionNumberInfoId : tmpAddPickupCsvData.getAddExtensionNumberInfoIdArray()){
                        
                        if (extensionNumberInfoId == null) {
                            throw new IllegalArgumentException("追加対象の着信グループに含まれている子番号の「内線番号情報.内線番号情報ID」のリストの要素がnullです。");
                        }
                        
                        // 内線番号情報IDからsip_内線番号.confファイル生成に必要なデータをセット
                        setSipExtensionNumberConfDataByExtensionNumberInfoId(
                                dbConnection,
                                extensionNumberInfoId,
                                extNumberInfoListSipIn,
                                pickUpGroupList);
                    }
                    
                    // 転送先ディレクトリ情報取得メソッド呼出
                    ConfigCreatorFile.getInstance().setPathListOfSipExtensionNumberConf(
                            localFileList,
                            dstFileList,
                            dstFileBackUpList,
                            dstFileTmpList,
                            nNumberInfo,
                            extNumberInfoListSipIn);
                    
                    
                    //ファイル生成メソッド呼出
                    ConfigCreatorFile.getInstance().createSipExtensionNumberConf(extNumberInfoListSipIn,
                            pickUpGroupList,
                            nNumberInfo);    
                    
                    // #1348 start
                    if (!incomingGroupInfoListPickupExtGroupForAdd) {
                        // #768 START  (extensions_group.conf)
                        // N番情報IDからextensions_group.confファイル生成に必要なデータをセット
                        setExtensionsGroupConfDataByNNumberInfoId(
                                dbConnection,
                                nNumberInfoId,
                                incomingGroupInfoListExtGroupForAdd,
                                childExtensionNumberListExtGroupForAdd);
                        // #768 END    (extensions_group.conf)
                        
                        
                        // #768 START  (extensions_group.conf)
                        ConfigCreatorFile.getInstance().setPathListOfExtensionsGroupConf(
                                localFileList,
                                dstFileList,
                                dstFileBackUpList,
                                dstFileTmpList,
                                nNumberInfo);
                        // #768 END    (extensions_group.conf)
                        
                        // #768 START  (extensions_group.conf)
                        // extensions_group.conf
                        ConfigCreatorFile.getInstance().createExtensionsGroupConf(
                                incomingGroupInfoListExtGroupForAdd, 
                                childExtensionNumberListExtGroupForAdd, 
                                nNumberInfo );
                        // #768 END    (extensions_group.conf)
                        
                        incomingGroupInfoListPickupExtGroupForAdd = true;
                    }
                    // #1348 end
                }
                
                /** CSV取込で変更、グループ種別が順次着信の数だけ実行する */
                for (IncomingGroupDataForModSlideCsv tmpModSlideCsvData : incomingGroupModSlideList) {
                    
                    if (tmpModSlideCsvData == null) {
                        throw new IllegalArgumentException("操作種別:変更、グループ種別:順次着信の情報が不正です。");
                    }
                    
                    log.info("操作種別 = 変更、グループ種別 = 順次着信の着信グループ情報一括設定ファイルが取り込まれました。");
                    log.debug("着信グループ情報ID = " + tmpModSlideCsvData.getNewIncomingGroupInfoIdForModSlideCsv());
                    
                    // 変更後の着信グループ情報IDからファイル生成に必要なデータをセット
                    setExtensionsExtensionNumberInConfDataByIncomingGroupInfoId(
                            dbConnection,
                            tmpModSlideCsvData.getNewIncomingGroupInfoIdForModSlideCsv(),
                            extensionNumberInfoListExtIn,
                            absenceBehaviorInfoList,
                            outsideCallInfoList,
                            childExtensionNumberInfoList,
                            groupCallTypeList);
                    
                    // 代表番号が変更されたか関係なく、変更前の内線番号情報IDからファイル生成に必要なデータをセット　
                    setExtensionsExtensionNumberInConfDataByExtensionNumberInfoId(
                            dbConnection,
                            tmpModSlideCsvData.getOldExtensionNumberInfoIdForModSlideCsv(), 
                            extensionNumberInfoListExtIn, 
                            absenceBehaviorInfoList, 
                            outsideCallInfoList, 
                            childExtensionNumberInfoList, 
                            groupCallTypeList);         
                    
                    // 転送先ディレクトリ情報取得メソッド呼出
                    ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberInConf(
                            localFileList,
                            dstFileList,
                            dstFileBackUpList,
                            dstFileTmpList,
                            nNumberInfo,
                            extensionNumberInfoListExtIn);

                    //ファイル生成メソッド呼出
                    ConfigCreatorFile.getInstance().createExtensionsExtensionNumberInConf(extensionNumberInfoListExtIn,
                            absenceBehaviorInfoList,
                            outsideCallInfoList,
                            groupCallTypeList,
                            childExtensionNumberInfoList,
                            nNumberInfo);        
                    
                }
                
                /** CSV取込で変更、グループ種別が一斉着信の数だけ実行する */
                for (IncomingGroupDataForModVolleyCsv tmpModVolleyCsvData : incomingGroupModVolleyList) {
                    
                    if (tmpModVolleyCsvData == null) {
                        throw new IllegalArgumentException("操作種別:変更、グループ種別:一斉着信の情報が不正です。");
                    }
                    
                    log.info("操作種別 = 変更、グループ種別 = 一斉着信の着信グループ情報一括設定ファイルが取り込まれました。");
                    log.debug("着信グループ情報ID = " + tmpModVolleyCsvData.getNewIncomingGroupInfoIdForModVolleyCsv());
                    
                    // 変更後の着信グループ情報IDからファイル生成に必要なデータをセット
                    setExtensionsExtensionNumberInConfDataByIncomingGroupInfoId(
                            dbConnection,
                            tmpModVolleyCsvData.getNewIncomingGroupInfoIdForModVolleyCsv(),
                            extensionNumberInfoListExtIn,
                            absenceBehaviorInfoList,
                            outsideCallInfoList,
                            childExtensionNumberInfoList,
                            groupCallTypeList);
                    
                    // 代表番号が変更されたか関係なく、変更前の内線番号情報IDからファイル生成に必要なデータをセット
                    setExtensionsExtensionNumberInConfDataByExtensionNumberInfoId(
                            dbConnection,
                            tmpModVolleyCsvData.getoldExtensionNumberInfoIdForModVolleyCsv(), 
                            extensionNumberInfoListExtIn, 
                            absenceBehaviorInfoList, 
                            outsideCallInfoList, 
                            childExtensionNumberInfoList, 
                            groupCallTypeList);
                    
                    // 転送先ディレクトリ情報取得メソッド呼出
                    ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberInConf(
                            localFileList,
                            dstFileList,
                            dstFileBackUpList,
                            dstFileTmpList,
                            nNumberInfo,
                            extensionNumberInfoListExtIn);

                    //ファイル生成メソッド呼出
                    ConfigCreatorFile.getInstance().createExtensionsExtensionNumberInConf(extensionNumberInfoListExtIn,
                            absenceBehaviorInfoList,
                            outsideCallInfoList,
                            groupCallTypeList,
                            childExtensionNumberInfoList,
                            nNumberInfo);
                }
                
                /** CSV取込で変更、グループ種別がコールピックアップの数だけ実行する */
                for (IncomingGroupDataForModPickupCsv tmpModPickupCsvData : incomingGroupModPickupList) {
                    
                    if (tmpModPickupCsvData == null) {
                        throw new IllegalArgumentException("操作種別:変更、グループ種別:コールピックアップの情報が不正です。");
                    }
                    
                    log.info("操作種別 = 変更、グループ種別 = コールピックアップの着信グループ情報一括設定ファイルが取り込まれました。");
                    log.debug("着信グループ情報ID = " + tmpModPickupCsvData.getIncomingGroupInfoIdForModPickupCsv());
                    
                    // 追加される内線番号情報IDのリストからデータをセット
                    for (Long addExtensionNumberInfoId : tmpModPickupCsvData.getAddExtensionNumberInfoIdArray()) {
                        
                        // Listのnullは許容するが、要素のnullは許容しない
                        if (addExtensionNumberInfoId == null) {
                            throw new IllegalArgumentException("変更対象の着信グループに含まれている追加子番号の「内線番号情報.内線番号情報ID」のリストの要素がnullです。");
                        }
                        
                        // 内線番号情報IDからsip_内線番号.confファイル生成に必要なデータをセット
                        setSipExtensionNumberConfDataByExtensionNumberInfoId(
                                dbConnection,
                                addExtensionNumberInfoId,
                                extNumberInfoListSipIn,
                                pickUpGroupList);
                    }
                    
                    // 削除される内線番号情報IDのリストからデータをセット
                    for (Long delExtensionNumberInfoId : tmpModPickupCsvData.getDelExtensionNumberInfoIdArray()) {
                        
                        // Listのnullは許容するが、要素のnullは許容しない
                        if (delExtensionNumberInfoId == null) {
                            throw new IllegalArgumentException("変更対象の着信グループに含まれていた削除子番号の「内線番号情報.内線番号情報ID」のリストの要素がnullです。");
                        }
                        
                        // 内線番号情報IDからsip_内線番号.confファイル生成に必要なデータをセット
                        setSipExtensionNumberConfDataByExtensionNumberInfoId(
                                dbConnection,
                                delExtensionNumberInfoId,
                                extNumberInfoListSipIn,
                                pickUpGroupList);
                    }
                    

                    // 転送先ディレクトリ情報取得メソッド呼出
                    ConfigCreatorFile.getInstance().setPathListOfSipExtensionNumberConf(
                            localFileList,
                            dstFileList,
                            dstFileBackUpList,
                            dstFileTmpList,
                            nNumberInfo,
                            extNumberInfoListSipIn);
                    

                    // ファイル生成メソッド呼出
                    ConfigCreatorFile.getInstance().createSipExtensionNumberConf(extNumberInfoListSipIn,
                            pickUpGroupList,
                            nNumberInfo);   
                    
                    // #1348 start
                    if (!incomingGroupInfoListPickupExtGroupForMod) {
                        // #768 START  (extensions_group.conf)
                        // N番情報IDからextensions_group.confファイル生成に必要なデータをセット
                        setExtensionsGroupConfDataByNNumberInfoId(
                                dbConnection,
                                nNumberInfoId,
                                incomingGroupInfoListExtGroupForMod,
                                childExtensionNumberListExtGroupForMod);
                        // #768 END    (extensions_group.conf)
                        
                        // #768 START  (extensions_group.conf)
                        ConfigCreatorFile.getInstance().setPathListOfExtensionsGroupConf(
                                localFileList,
                                dstFileList,
                                dstFileBackUpList,
                                dstFileTmpList,
                                nNumberInfo);
                        // #768 END    (extensions_group.conf)
                        
                        // #768 START  (extensions_group.conf)
                        // extensions_group.conf
                        ConfigCreatorFile.getInstance().createExtensionsGroupConf(
                                incomingGroupInfoListExtGroupForMod,
                                childExtensionNumberListExtGroupForMod,
                                nNumberInfo );
                        // #768 END    (extensions_group.conf)
                        
                        incomingGroupInfoListPickupExtGroupForMod = true;
                    }
                    // #1348 end
                }
                
                /** CSV取込で削除、グループ種別が順次着信の数だけ実行する */
                for (IncomingGroupDataForDelSlideCsv tmpDelSlideCsvData : incomingGroupDelSlideList) {
                    
                    if (tmpDelSlideCsvData == null) {
                        throw new IllegalArgumentException("操作種別:削除、グループ種別:順次着信の情報が不正です。");
                    }
                    
                        log.info("操作種別 = 削除、グループ種別 = 順次着信の着信グループ情報一括設定ファイルが取り込まれました。");
                        
                        // 削除前の内線番号情報IDからファイル生成に必要なデータをセット
                        setExtensionsExtensionNumberInConfDataByExtensionNumberInfoId(
                                dbConnection,
                                tmpDelSlideCsvData.getExtensionNumberInfoIdForDelSlideCsv(), 
                                extensionNumberInfoListExtIn, 
                                absenceBehaviorInfoList, 
                                outsideCallInfoList, 
                                childExtensionNumberInfoList, 
                                groupCallTypeList);
                        
                        // 転送先ディレクトリ情報取得メソッド呼出
                        ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberInConf(
                                localFileList,
                                dstFileList,
                                dstFileBackUpList,
                                dstFileTmpList,
                                nNumberInfo,
                                extensionNumberInfoListExtIn);
    
                        //ファイル生成メソッド呼出
                        ConfigCreatorFile.getInstance().createExtensionsExtensionNumberInConf(extensionNumberInfoListExtIn,
                                absenceBehaviorInfoList,
                                outsideCallInfoList,
                                groupCallTypeList,
                                childExtensionNumberInfoList,
                                nNumberInfo);
                    
                }
                
                /** CSV取込で削除、グループ種別が一斉着信の数だけ実行する */
                for (IncomingGroupDataForDelVolleyCsv tmpDelVolleyCsvData : incomingGroupDelVolleyList) {
                    
                    if (tmpDelVolleyCsvData == null) {
                        throw new IllegalArgumentException("操作種別:削除、グループ種別:一斉着信の情報が不正です。");
                    }
                    
                    log.info("操作種別 = 削除、グループ種別 = 一斉着信の着信グループ情報一括設定ファイルが取り込まれました。");
                    
                    // 削除前の内線番号情報IDからファイル生成に必要なデータをセット
                    setExtensionsExtensionNumberInConfDataByExtensionNumberInfoId(
                            dbConnection,
                            tmpDelVolleyCsvData.getExtensionNumberInfoIdForDelVolleyCsv(),
                            extensionNumberInfoListExtIn,
                            absenceBehaviorInfoList,
                            outsideCallInfoList,
                            childExtensionNumberInfoList,
                            groupCallTypeList);
                    
                    // 転送先ディレクトリ情報取得メソッド呼出
                    ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberInConf(
                            localFileList,
                            dstFileList,
                            dstFileBackUpList,
                            dstFileTmpList,
                            nNumberInfo,
                            extensionNumberInfoListExtIn);

                    //ファイル生成メソッド呼出
                    ConfigCreatorFile.getInstance().createExtensionsExtensionNumberInConf(extensionNumberInfoListExtIn,
                            absenceBehaviorInfoList,
                            outsideCallInfoList,
                            groupCallTypeList,
                            childExtensionNumberInfoList,
                            nNumberInfo);
                }
                
                /** CSV取込で削除、グループ種別がコールピックアップの数だけ実行する */
                for (IncomingGroupDataForDelPickupCsv tmpDelPickupCsvData : incomingGroupDelPickupList) {
                    
                    if (tmpDelPickupCsvData == null) {
                        throw new IllegalArgumentException("操作種別:削除、グループ種別:コールピックアップの情報が不正です。");
                    }
                    
                    log.info("操作種別 = 削除、グループ種別 = コールピックアップの着信グループ情報一括設定ファイルが取り込まれました。");
                    
                    if (tmpDelPickupCsvData.getDelExtensionNumberInfoIdArray() == null) {
                        throw new IllegalArgumentException("削除対象の着信グループに含まれていた子番号の「内線番号情報.内線番号情報ID」のリストがありません。");
                    }
                    
                    // 削除される内線番号情報IDのリストからデータをセット
                    for (Long extensionNumberInfoId : tmpDelPickupCsvData.getDelExtensionNumberInfoIdArray()) {
                        
                        if (extensionNumberInfoId == null) {
                            throw new IllegalArgumentException("削除対象の着信グループに含まれていた削除子番号の「内線番号情報.内線番号情報ID」のリストの要素がnullです。");
                        }
                        
                        // 内線番号情報IDからsip_内線番号.confファイル生成に必要なデータをセット
                        setSipExtensionNumberConfDataByExtensionNumberInfoId(
                                dbConnection,
                                extensionNumberInfoId,
                                extNumberInfoListSipIn,
                                pickUpGroupList);
                    }
                    
                    // 転送先ディレクトリ情報取得メソッド呼出
                    ConfigCreatorFile.getInstance().setPathListOfSipExtensionNumberConf(
                            localFileList,
                            dstFileList,
                            dstFileBackUpList,
                            dstFileTmpList,
                            nNumberInfo,
                            extNumberInfoListSipIn);
                    
                    //ファイル生成メソッド呼出
                    ConfigCreatorFile.getInstance().createSipExtensionNumberConf(extNumberInfoListSipIn,
                            pickUpGroupList,
                            nNumberInfo);
                    
                    // #1348 start
                    if (!incomingGroupInfoListPickupExtGroupForDel) {
                        // #768 START  (extensions_group.conf)
                        // N番情報IDからextensions_group.confファイル生成に必要なデータをセット
                        setExtensionsGroupConfDataByNNumberInfoId(
                                dbConnection,
                                nNumberInfoId,
                                incomingGroupInfoListExtGroupForDel,
                                childExtensionNumberListExtGroupForDel);
                        // #768 END    (extensions_group.conf)
                        
                        // #768 START  (extensions_group.conf)
                        ConfigCreatorFile.getInstance().setPathListOfExtensionsGroupConf(
                                localFileList,
                                dstFileList,
                                dstFileBackUpList,
                                dstFileTmpList,
                                nNumberInfo);
                        // #768 END    (extensions_group.conf)
                        
                        // #768 START  (extensions_group.conf)
                        // extensions_group.conf
                        ConfigCreatorFile.getInstance().createExtensionsGroupConf(
                                incomingGroupInfoListExtGroupForDel, 
                                childExtensionNumberListExtGroupForDel, 
                                nNumberInfo );
                        // #768 END    (extensions_group.conf)
                        
                        incomingGroupInfoListPickupExtGroupForDel = true;
                    }
                    // #1348 end
                }
                
                // DBに内線サーバ設定ファイルバックアップ情報を登録
                executeBackUp(loginId,
                        dbConnection, 
                        nNumberInfoId,
                        localFileList,
                        dstFileList,
                        null);
                
            } catch(Exception e) {
                // ファイル生成 失敗時のログ出力
                String msg = "[着信グループ一括設定機能]内線サーバ設定ファイル作成処理でエラーが発生しました。"
                        + " ログインID = " + loginId
                        + " N番情報ID = " + nNumberInfoId
                        + " エラー内容 = " + e.toString();
                log.error(msg);
                
                // ファイル生成 失敗時の監視ログ出力
                log.error(Util.message(Const.ERROR_CODE.E020802, String.format(Const.MESSAGE_CODE.E020802,null,null,null,null,loginId)));
                throw e;
            }
            
            // ファイルアップロード処理実行
            // (ファイルバックアップ、SIPリロード含む)
            executeFileUpload(loginId,
                    dbConnection, 
                    nNumberInfoId,
                    localFileList,
                    dstFileList,
                    dstFileBackUpList,
                    dstFileTmpList,
                    null,null); 
            
        } catch (Exception e) {
            log.error("method error : importGroupCsv() = " + e.toString());

            throw e;
        }
        
        log.debug("method end : importGroupCsv() ");
    }
    // step1.6 end
    
    /**
     * G0602_着信グループ設定画面(追加)の結果をAsteriskへ反映する<br>
     * <br>
     * G0602_着信グループ設定画面(追加)で追加ボタン押下時に<br>
     * グループ種別=順次着信の場合に実行する。<br>
     * 
     * @param loginId　ログインID
     * @param dbConnection DBコネクション
     * @param nNumberInfoId　「N番情報ID.N番情報ID」
     * @param incomingGroupInfoId　追加された「着信グループ情報.着信グループ情報ID」
     * @throws Exception 例外
     */
    public void addGroupOfSlide(String loginId, Connection dbConnection, long nNumberInfoId,
            long incomingGroupInfoId) 
                    throws Exception {   
        try{
            log.debug("method start :addGroupOfSlide");

            // 更新対象の内線サーバ設定ファイルバックアップ情報をロック(同じN番を全ロック)
            executeLock(dbConnection, 
                    nNumberInfoId);

            // 変数を宣言
            NNumberInfo nNumberInfo = getNNumberInfo(dbConnection, nNumberInfoId);
            List<ExtensionNumberInfo> extensionNumberInfoList = new ArrayList<ExtensionNumberInfo>();
            List<AbsenceBehaviorInfo> absenceBehaviorInfoList = new ArrayList<AbsenceBehaviorInfo>();
            List<OutsideCallInfo> outsideCallInfoList = new ArrayList<OutsideCallInfo>();
            List<List<String>> childExtensionNumberInfoList = new ArrayList<List<String>>();
            List<Integer> groupCallTypeList = new ArrayList<Integer>();
            List<String> localFileList = new ArrayList<String>();
            List<String> dstFileList = new ArrayList<String>();
            List<String> dstFileBackUpList = new ArrayList<String>();
            List<String> dstFileTmpList = new ArrayList<String>();


            //     #748 START
            try{
                // #748 END
                
                // 着信グループ情報IDから必要なデータをセット
                setExtensionsExtensionNumberInConfDataByIncomingGroupInfoId(
                        dbConnection,
                        incomingGroupInfoId,
                        extensionNumberInfoList,
                        absenceBehaviorInfoList,
                        outsideCallInfoList,
                        childExtensionNumberInfoList,
                        groupCallTypeList);
                // #834 START
                //(VoIP-GWの考慮漏れ。extensions_内線番号_in.confファイル生成に必要な同一拠点番号の他のVoIP-GWのデータを取得する)
                //VoIP-GWは着信グループの対象外
                //setExtensionsExtensionNumberInConfDataByIncomingGroupInfoIdに対応するメソッドは実装しないため、
                //サービス仕様変更時に実装すること
                // #834 END

                // 転送先ディレクトリ情報取得メソッド呼出
                ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberInConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        extensionNumberInfoList);

                // ファイル生成メソッド呼出
                ConfigCreatorFile.getInstance().createExtensionsExtensionNumberInConf(extensionNumberInfoList,
                        absenceBehaviorInfoList,
                        outsideCallInfoList,
                        groupCallTypeList,
                        childExtensionNumberInfoList,
                        nNumberInfo);  

                // DBに内線サーバ設定ファイルバックアップ情報を登録
                executeBackUp(loginId,
                        dbConnection, 
                        nNumberInfoId,
                        localFileList,
                        dstFileList,
                        null);    

                // #748 START
                // ファイル生成 成功時のログ出力
                log.info(Util.message(Const.ERROR_CODE.I020801, String.format(Const.MESSAGE_CODE.I020801,null,null,null,null,loginId)));
                // #748 END

                // #748 START
            }catch(Exception e){
                // ファイル生成 失敗時のログ出力
                log.error(Util.message(Const.ERROR_CODE.E020802, String.format(Const.MESSAGE_CODE.E020802,null,null,null,null,loginId)));
                throw e;
            }
            //     #748 END

            // ファイルアップロード処理実行
            // (ファイルバックアップ、SIPリロード含む)
            executeFileUpload(loginId,
                    dbConnection, 
                    nNumberInfoId,
                    localFileList,
                    dstFileList,
                    dstFileBackUpList,
                    dstFileTmpList,
                    null,
                    null);

        }catch (Exception e){
            log.error("method error :addGroupOfSlide = " + e.toString());

            throw e;
        }
        
        log.debug("method end :addGroupOfSlide");
    }

    /**
     * G0602_着信グループ設定画面(追加)の結果をAsteriskへ反映する<br>
     * 
     * <br>
     * G0602_着信グループ設定画面(追加)で追加ボタン押下時に<br>
     * グループ種別=一斉着信の場合に実行する。<br>
     * 
     * @param loginId　ログインID
     * @param dbConnection DBコネクション
     * @param nNumberInfoId　「N番情報ID.N番情報ID」
     * @param incomingGroupInfoId　追加された「着信グループ情報.着信グループ情報ID」
     * @throws Exception 例外
     */
    public void addGroupOfVolley(String loginId, Connection dbConnection, long nNumberInfoId, 
            long incomingGroupInfoId) 
                    throws Exception {
        try{
            log.debug("method start :addGroupOfVolley");

            // 更新対象の内線サーバ設定ファイルバックアップ情報をロック(同じN番を全ロック)
            executeLock(dbConnection, 
                    nNumberInfoId);

            // 変数を宣言
            NNumberInfo nNumberInfo = getNNumberInfo(dbConnection, nNumberInfoId);
            List<ExtensionNumberInfo> extensionNumberInfoList = new ArrayList<ExtensionNumberInfo>();
            List<AbsenceBehaviorInfo> absenceBehaviorInfoList = new ArrayList<AbsenceBehaviorInfo>();
            List<OutsideCallInfo> outsideCallInfoList = new ArrayList<OutsideCallInfo>();
            List<List<String>> childExtensionNumberInfoList = new ArrayList<List<String>>();
            List<Integer> groupCallTypeList = new ArrayList<Integer>();
            List<String> localFileList = new ArrayList<String>();
            List<String> dstFileList = new ArrayList<String>();
            List<String> dstFileBackUpList = new ArrayList<String>();
            List<String> dstFileTmpList = new ArrayList<String>();

            //     #748 START
            try{
                // #748 END


                // 着信グループ情報IDから必要なデータをセット
                setExtensionsExtensionNumberInConfDataByIncomingGroupInfoId(
                        dbConnection,
                        incomingGroupInfoId,
                        extensionNumberInfoList,
                        absenceBehaviorInfoList,
                        outsideCallInfoList,
                        childExtensionNumberInfoList,
                        groupCallTypeList);
                // #834 START
                //(VoIP-GWの考慮漏れ。extensions_内線番号_in.confファイル生成に必要な同一拠点番号の他のVoIP-GWのデータを取得する)
                //VoIP-GWは着信グループの対象外
                //setExtensionsExtensionNumberInConfDataByIncomingGroupInfoIdに対応するメソッドは実装しないため、
                //サービス仕様変更時に実装すること
                // #834 END

                // 転送先ディレクトリ情報取得メソッド呼出
                ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberInConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        extensionNumberInfoList);

                // ファイル生成メソッド呼出
                ConfigCreatorFile.getInstance().createExtensionsExtensionNumberInConf(extensionNumberInfoList,
                        absenceBehaviorInfoList,
                        outsideCallInfoList,
                        groupCallTypeList,
                        childExtensionNumberInfoList,
                        nNumberInfo);  

                // DBに内線サーバ設定ファイルバックアップ情報を登録
                executeBackUp(loginId,
                        dbConnection, 
                        nNumberInfoId,
                        localFileList,
                        dstFileList,
                        null);   

                // #748 START
                // ファイル生成 成功時のログ出力
                log.info(Util.message(Const.ERROR_CODE.I020801, String.format(Const.MESSAGE_CODE.I020801,null,null,null,null,loginId)));
                // #748 END

                // #748 START
            }catch(Exception e){
                // ファイル生成 失敗時のログ出力
                log.error(Util.message(Const.ERROR_CODE.E020802, String.format(Const.MESSAGE_CODE.E020802,null,null,null,null,loginId)));
                throw e;
            }
            //     #748 END
            
            // ファイルアップロード処理実行
            // (ファイルバックアップ、SIPリロード含む)
            executeFileUpload(loginId,
                    dbConnection, 
                    nNumberInfoId,
                    localFileList,
                    dstFileList,
                    dstFileBackUpList,
                    dstFileTmpList,
                    null,null); 

        }catch (Exception e){
            log.debug("method error :addGroupOfVolley = " + e.toString());

            throw e;
        }
        
        log.debug("method end :addGroupOfVolley");
    }

    /**
     * G0602_着信グループ設定画面(追加)の結果をAsteriskへ反映する<br>
     * <br>
     * G0602_着信グループ設定画面(追加)で追加ボタン押下時に<br>
     * グループ種別=コールピックアップの場合に実行する。<br>
     * 
     * @param loginId　ログインID
     * @param dbConnection DBコネクション
     * @param nNumberInfoId　「N番情報ID.N番情報ID」
     * @param incomingGroupInfoId　追加対象の「着信グループ情報.着信グループ情報ID」
     * @param addExtensionNumberInfoIdArray　追加対象の着信グループに追加された子番号の「内線番号情報.内線番号情報ID」のリスト
     * @throws Exception 例外
     */
    public void addGroupOfPickup(String loginId, Connection dbConnection, long nNumberInfoId, 
            long incomingGroupInfoId,
            ArrayList<Long> addExtensionNumberInfoIdArray) 
                    throws Exception {
        try{
            log.debug("method start :addGroupOfPickup");

            // 更新対象の内線サーバ設定ファイルバックアップ情報をロック(同じN番を全ロック)
            executeLock(dbConnection, 
                    nNumberInfoId);

            // 変数を宣言
            NNumberInfo nNumberInfo = getNNumberInfo(dbConnection, nNumberInfoId);
            List<ExtensionNumberInfo> extensionNumberInfoList = new ArrayList<ExtensionNumberInfo>();
            List<String> pickUpGroupList = new ArrayList<String>();
            List<String> localFileList = new ArrayList<String>();
            List<String> dstFileList = new ArrayList<String>();
            List<String> dstFileBackUpList = new ArrayList<String>();
            List<String> dstFileTmpList = new ArrayList<String>();
            // #768 START  (extensions_group.conf)
            // 変数を宣言(extensions_group.conf用)
            List<IncomingGroupInfo> incomingGroupInfoListOfCallPickUp = new ArrayList<IncomingGroupInfo>();
            List<List<String>> childExtensionNumberListExtGroup = new ArrayList<List<String>>();
            // #768 END    (extensions_group.conf)

            //     #748 START
            try{
                // #748 END

                // 追加される内線番号情報IDのリストからデータをセット       
                Iterator<Long> itrAddExtensionNumberInfoId = addExtensionNumberInfoIdArray.iterator();
                while(itrAddExtensionNumberInfoId.hasNext()){     

                    // 内線番号情報IDのリストから内線番号情報IDを取得
                    long extensionNumberInfoId = itrAddExtensionNumberInfoId.next();        

                    // 内線番号情報IDからsip_内線番号.confファイル生成に必要なデータをセット
                    setSipExtensionNumberConfDataByExtensionNumberInfoId(
                            dbConnection,
                            extensionNumberInfoId,
                            extensionNumberInfoList,
                            pickUpGroupList);
                    // #834 START
                    //(VoIP-GWの考慮漏れ。sip_内線番号.confファイル生成に必要な同一拠点番号の他のVoIP-GWのデータを取得する)
                    //VoIP-GWは着信グループの対象外のため、実装するがコメントアウト
                    //                setSipExtensionNumberConfDataByExtensionNumberInfoIdForOtherVoipGw(
                    //                        dbConnection,
                    //                        extensionNumberInfoId,
                    //                        extensionNumberInfoList,
                    //                        pickUpGroupList);
                    // #834 END
                }
                
                // #768 START  (extensions_group.conf)
                // N番情報IDからextensions_group.confファイル生成に必要なデータをセット
                setExtensionsGroupConfDataByNNumberInfoId(
                        dbConnection,
                        nNumberInfoId,
                        incomingGroupInfoListOfCallPickUp,
                        childExtensionNumberListExtGroup);
                // #768 END    (extensions_group.conf)
             

                // 転送先ディレクトリ情報取得メソッド呼出
                ConfigCreatorFile.getInstance().setPathListOfSipExtensionNumberConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        extensionNumberInfoList);
                
                // #768 START  (extensions_group.conf)
                ConfigCreatorFile.getInstance().setPathListOfExtensionsGroupConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo);
                // #768 END    (extensions_group.conf)

                //ファイル生成メソッド呼出
                ConfigCreatorFile.getInstance().createSipExtensionNumberConf(extensionNumberInfoList,
                        pickUpGroupList,
                        nNumberInfo);        
                
                // #768 START  (extensions_group.conf)
                // extensions_group.conf
                ConfigCreatorFile.getInstance().createExtensionsGroupConf(
                        incomingGroupInfoListOfCallPickUp, 
                        childExtensionNumberListExtGroup, 
                        nNumberInfo );
                // #768 END    (extensions_group.conf)

                // DBに内線サーバ設定ファイルバックアップ情報を登録
                executeBackUp(loginId,
                        dbConnection, 
                        nNumberInfoId,
                        localFileList,
                        dstFileList,
                        null);   

                // #748 START
                // ファイル生成 成功時のログ出力
                log.info(Util.message(Const.ERROR_CODE.I020801, String.format(Const.MESSAGE_CODE.I020801,null,null,null,null,loginId)));
                // #748 END

                // #748 START
            }catch(Exception e){
                // ファイル生成 失敗時のログ出力
                log.error(Util.message(Const.ERROR_CODE.E020802, String.format(Const.MESSAGE_CODE.E020802,null,null,null,null,loginId)));
                throw e;
            }
            //     #748 END

            // ファイルアップロード処理実行
            // (ファイルバックアップ、SIPリロード含む)
            executeFileUpload(loginId,
                    dbConnection, 
                    nNumberInfoId,
                    localFileList,
                    dstFileList,
                    dstFileBackUpList,
                    dstFileTmpList,
                    null,null); 

        }catch (Exception e){
            log.error("method error :addGroupOfPickup = " + e.toString());

            throw e;
        }
        
        log.debug("method end :addGroupOfPickup");
    } 

    /**
     * G0604_着信グループ設定画面(変更)の結果をAsteriskへ反映する<br>
     * <br>
     * G0604_着信グループ設定画面(変更)で変更ボタン押下時に<br>
     * グループ種別=順次着信の場合に実行する。<br>
     * 
     * @param loginId　ログインID
     * @param dbConnection DBコネクション
     * @param nNumberInfoId　「N番情報ID.N番情報ID」
     * @param extensionNumberInfoId　変更前の内線代表番号の「内線番号情報.内線番号情報ID」
     * @param incomingGroupInfoId　変更された「着信グループ情報.着信グループ情報ID」
     * @throws Exception 例外
     */
    public void modGroupOfSlide(String loginId, Connection dbConnection, long nNumberInfoId, 
            long extensionNumberInfoId,
            long incomingGroupInfoId) 
                    throws Exception { 
        try{
            log.debug("method start :modGroupOfSlide");


            // 更新対象の内線サーバ設定ファイルバックアップ情報をロック(同じN番を全ロック)
            executeLock(dbConnection, 
                    nNumberInfoId);

            // 変数を宣言
            NNumberInfo nNumberInfo = getNNumberInfo(dbConnection, nNumberInfoId);
            List<ExtensionNumberInfo> extensionNumberInfoList = new ArrayList<ExtensionNumberInfo>();
            List<AbsenceBehaviorInfo> absenceBehaviorInfoList = new ArrayList<AbsenceBehaviorInfo>();
            List<OutsideCallInfo> outsideCallInfoList = new ArrayList<OutsideCallInfo>();
            List<List<String>> childExtensionNumberInfoList = new ArrayList<List<String>>();
            List<Integer> groupCallTypeList = new ArrayList<Integer>();
            List<String> localFileList = new ArrayList<String>();
            List<String> dstFileList = new ArrayList<String>();
            List<String> dstFileBackUpList = new ArrayList<String>();
            List<String> dstFileTmpList = new ArrayList<String>();

            //     #748 START
            try{
                // #748 END

                // 変更後の着信グループ情報IDからファイル生成に必要なデータをセット
                setExtensionsExtensionNumberInConfDataByIncomingGroupInfoId(
                        dbConnection,
                        incomingGroupInfoId,
                        extensionNumberInfoList,
                        absenceBehaviorInfoList,
                        outsideCallInfoList,
                        childExtensionNumberInfoList,
                        groupCallTypeList);
                // #834 START
                //(VoIP-GWの考慮漏れ。extensions_内線番号_in.confファイル生成に必要な同一拠点番号の他のVoIP-GWのデータを取得する)
                //VoIP-GWは着信グループの対象外
                //setExtensionsExtensionNumberInConfDataByIncomingGroupInfoIdに対応するメソッドは実装しないため、
                //サービス仕様変更時に実装すること
                // #834 END

                // 代表番号が変更されたか確認して、変更されていた場合は
                // 変更前の内線番号情報IDからファイル生成に必要なデータをセット
                if(extensionNumberInfoList.get(0).getExtensionNumberInfoId() != extensionNumberInfoId){
                    setExtensionsExtensionNumberInConfDataByExtensionNumberInfoId(
                            dbConnection,
                            extensionNumberInfoId, 
                            extensionNumberInfoList, 
                            absenceBehaviorInfoList, 
                            outsideCallInfoList, 
                            childExtensionNumberInfoList, 
                            groupCallTypeList);          
                    // #834 START
                    //(VoIP-GWの考慮漏れ。extensions_内線番号_in.confファイル生成に必要な同一拠点番号の他のVoIP-GWのデータを取得する)
                    //VoIP-GWは着信グループの対象外のため、実装するがコメントアウト
                    //                setExtensionsExtensionNumberInConfDataByExtensionNumberInfoIdForOtherVoipGw(
                    //                        dbConnection,
                    //                        extensionNumberInfoId, 
                    //                        extensionNumberInfoList, 
                    //                        absenceBehaviorInfoList, 
                    //                        outsideCallInfoList, 
                    //                        childExtensionNumberInfoList, 
                    //                        groupCallTypeList);          
                    // #834 END
                }

                // 転送先ディレクトリ情報取得メソッド呼出
                ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberInConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        extensionNumberInfoList);

                //ファイル生成メソッド呼出
                ConfigCreatorFile.getInstance().createExtensionsExtensionNumberInConf(extensionNumberInfoList,
                        absenceBehaviorInfoList,
                        outsideCallInfoList,
                        groupCallTypeList,
                        childExtensionNumberInfoList,
                        nNumberInfo);        

                // DBに内線サーバ設定ファイルバックアップ情報を登録
                executeBackUp(loginId,
                        dbConnection, 
                        nNumberInfoId,
                        localFileList,
                        dstFileList,
                        null);   

                // #748 START
                // ファイル生成 成功時のログ出力
                log.info(Util.message(Const.ERROR_CODE.I020801, String.format(Const.MESSAGE_CODE.I020801,null,null,null,null,loginId)));
                // #748 END

                // #748 START
            }catch(Exception e){
                // ファイル生成 失敗時のログ出力
                log.error(Util.message(Const.ERROR_CODE.E020802, String.format(Const.MESSAGE_CODE.E020802,null,null,null,null,loginId)));
                throw e;
            }
            //     #748 END

            // ファイルアップロード処理実行
            // (ファイルバックアップ、SIPリロード含む)
            executeFileUpload(loginId,
                    dbConnection, 
                    nNumberInfoId,
                    localFileList,
                    dstFileList,
                    dstFileBackUpList,
                    dstFileTmpList,
                    null,null); 

        }catch (Exception e){
            log.error("method error :modGroupOfSlide = " + e.toString());

            throw e;
        }
        
        log.debug("method end :modGroupOfSlide");
    } 

    /**
     * G0604_着信グループ設定画面(変更)の結果をAsteriskへ反映する<br>
     * <br>
     * G0604_着信グループ設定画面(変更)で変更ボタン押下時に<br>
     * グループ種別=一斉着信の場合に実行する。<br>
     * 
     * @param loginId　ログインID
     * @param dbConnection DBコネクション
     * @param nNumberInfoId　「N番情報ID.N番情報ID」
     * @param extensionNumberInfoId　変更前の内線代表番号の「内線番号情報.内線番号情報ID」
     * @param incomingGroupInfoId　変更された「着信グループ情報.着信グループ情報ID」
     * @throws Exception 例外
     */
    public void modGroupOfVolley(String loginId, Connection dbConnection, long nNumberInfoId, 
            long extensionNumberInfoId,
            long incomingGroupInfoId) 
                    throws Exception {
        try{
            log.debug("method start :modGroupOfVolley");


            // 更新対象の内線サーバ設定ファイルバックアップ情報をロック(同じN番を全ロック)
            executeLock(dbConnection, 
                    nNumberInfoId);

            // 変数を宣言
            NNumberInfo nNumberInfo = getNNumberInfo(dbConnection, nNumberInfoId);
            List<ExtensionNumberInfo> extensionNumberInfoList = new ArrayList<ExtensionNumberInfo>();
            List<AbsenceBehaviorInfo> absenceBehaviorInfoList = new ArrayList<AbsenceBehaviorInfo>();
            List<OutsideCallInfo> outsideCallInfoList = new ArrayList<OutsideCallInfo>();
            List<List<String>> childExtensionNumberInfoList = new ArrayList<List<String>>();
            List<Integer> groupCallTypeList = new ArrayList<Integer>();
            List<String> localFileList = new ArrayList<String>();
            List<String> dstFileList = new ArrayList<String>();
            List<String> dstFileBackUpList = new ArrayList<String>();
            List<String> dstFileTmpList = new ArrayList<String>();

            //     #748 START
            try{
                // #748 END

                // 変更後の着信グループ情報IDからファイル生成に必要なデータをセット
                setExtensionsExtensionNumberInConfDataByIncomingGroupInfoId(
                        dbConnection,
                        incomingGroupInfoId,
                        extensionNumberInfoList,
                        absenceBehaviorInfoList,
                        outsideCallInfoList,
                        childExtensionNumberInfoList,
                        groupCallTypeList);
                // #834 START
                //(VoIP-GWの考慮漏れ。extensions_内線番号_in.confファイル生成に必要な同一拠点番号の他のVoIP-GWのデータを取得する)
                //VoIP-GWは着信グループの対象外
                //setExtensionsExtensionNumberInConfDataByIncomingGroupInfoIdに対応するメソッドは実装しないため、
                //サービス仕様変更時に実装すること
                // #834 END

                // 代表番号が変更されたか確認して、変更されていた場合は
                // 変更前の内線番号情報IDからファイル生成に必要なデータをセット
                if(extensionNumberInfoList.get(0).getExtensionNumberInfoId() != extensionNumberInfoId){
                    setExtensionsExtensionNumberInConfDataByExtensionNumberInfoId(
                            dbConnection,
                            extensionNumberInfoId, 
                            extensionNumberInfoList, 
                            absenceBehaviorInfoList, 
                            outsideCallInfoList, 
                            childExtensionNumberInfoList, 
                            groupCallTypeList);          
                }
                // #834 START
                //(VoIP-GWの考慮漏れ。sip_内線番号.confファイル生成に必要な同一拠点番号の他のVoIP-GWのデータを取得する)
                //VoIP-GWは着信グループの対象外のため、実装するがコメントアウト
                //            setExtensionsExtensionNumberInConfDataByExtensionNumberInfoIdForOtherVoipGw(
                //                    dbConnection,
                //                    extensionNumberInfoId, 
                //                    extensionNumberInfoList, 
                //                    absenceBehaviorInfoList, 
                //                    outsideCallInfoList, 
                //                    childExtensionNumberInfoList, 
                //                    groupCallTypeList);          
                // #834 END

                // 転送先ディレクトリ情報取得メソッド呼出
                ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberInConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        extensionNumberInfoList);

                //ファイル生成メソッド呼出
                ConfigCreatorFile.getInstance().createExtensionsExtensionNumberInConf(extensionNumberInfoList,
                        absenceBehaviorInfoList,
                        outsideCallInfoList,
                        groupCallTypeList,
                        childExtensionNumberInfoList,
                        nNumberInfo);        

                // DBに内線サーバ設定ファイルバックアップ情報を登録
                executeBackUp(loginId,
                        dbConnection, 
                        nNumberInfoId,
                        localFileList,
                        dstFileList,
                        null);   

                // #748 START
                // ファイル生成 成功時のログ出力
                log.info(Util.message(Const.ERROR_CODE.I020801, String.format(Const.MESSAGE_CODE.I020801,null,null,null,null,loginId)));
                // #748 END

                // #748 START
            }catch(Exception e){
                // ファイル生成 失敗時のログ出力
                log.error(Util.message(Const.ERROR_CODE.E020802, String.format(Const.MESSAGE_CODE.E020802,null,null,null,null,loginId)));
                throw e;
            }
            //     #748 END

            // ファイルアップロード処理実行
            // (ファイルバックアップ、SIPリロード含む)
            executeFileUpload(loginId,
                    dbConnection, 
                    nNumberInfoId,
                    localFileList,
                    dstFileList,
                    dstFileBackUpList,
                    dstFileTmpList,
                    null,null); 

        }catch (Exception e){
            log.error("method error :modGroupOfVolley = " + e.toString());

            throw e;
        }
        
        log.debug("method end :modGroupOfVolley");
    } 

    /**
     * G0604_着信グループ設定画面(変更)の結果をAsteriskへ反映する<br>
     * <br>
     * G0604_着信グループ設定画面(変更)で変更ボタン押下時に<br>
     * グループ種別=コールピックアップの場合に実行する。<br>
     * 
     * @param loginId　ログインID
     * @param dbConnection DBコネクション
     * @param nNumberInfoId　「N番情報ID.N番情報ID」
     * @param incomingGroupInfoId　変更対象の「着信グループ情報.着信グループ情報ID」
     * @param addExtensionNumberInfoIdArray　変更対象のグループに追加された子番号の「内線番号情報.内線番号情報ID」のリスト
     * @param delExtensionNumberInfoIdArray　変更対象のグループから削除された子番号の「内線番号情報.内線番号情報ID」のリスト
     * @throws Exception 例外
     */
    public void modGroupOfPickup(String loginId, Connection dbConnection, long nNumberInfoId, 
            long incomingGroupInfoId,
            ArrayList<Long> addExtensionNumberInfoIdArray,
            ArrayList<Long> delExtensionNumberInfoIdArray) 
                    throws Exception {
        try{
            log.debug("method start :modGroupOfPickup");

            // 更新対象の内線サーバ設定ファイルバックアップ情報をロック(同じN番を全ロック)
            executeLock(dbConnection, 
                    nNumberInfoId);

            // 変数を宣言
            NNumberInfo nNumberInfo = getNNumberInfo(dbConnection, nNumberInfoId);
            List<ExtensionNumberInfo> extensionNumberInfoList = new ArrayList<ExtensionNumberInfo>();
            List<String> pickUpGroupList = new ArrayList<String>();
            List<String> localFileList = new ArrayList<String>();
            List<String> dstFileList = new ArrayList<String>();
            List<String> dstFileBackUpList = new ArrayList<String>();
            List<String> dstFileTmpList = new ArrayList<String>();
            // #768 START  (extensions_group.conf)
            // 変数を宣言(extensions_group.conf用)
            List<IncomingGroupInfo> incomingGroupInfoListOfCallPickUp = new ArrayList<IncomingGroupInfo>();
            List<List<String>> childExtensionNumberListExtGroup = new ArrayList<List<String>>();
            // #768 END    (extensions_group.conf)

            //     #748 START
            try{
                // #748 END

                // 追加される内線番号情報IDのリストからデータをセット       
                Iterator<Long> itrAddExtensionNumberInfoId = addExtensionNumberInfoIdArray.iterator();
                while(itrAddExtensionNumberInfoId.hasNext()){     
                    // 内線番号情報IDのリストから内線番号情報IDを取得
                    long extensionNumberInfoId = itrAddExtensionNumberInfoId.next();        

                    // 内線番号情報IDからsip_内線番号.confファイル生成に必要なデータをセット
                    setSipExtensionNumberConfDataByExtensionNumberInfoId(
                            dbConnection,
                            extensionNumberInfoId,
                            extensionNumberInfoList,
                            pickUpGroupList);
                }

                // 削除される内線番号情報IDのリストからデータをセット       
                Iterator<Long> itrDelExtensionNumberInfoId = delExtensionNumberInfoIdArray.iterator();
                while(itrDelExtensionNumberInfoId.hasNext()){     
                    // 内線番号情報IDのリストから内線番号情報IDを取得
                    long extensionNumberInfoId = itrDelExtensionNumberInfoId.next();        

                    // 内線番号情報IDからsip_内線番号.confファイル生成に必要なデータをセット
                    setSipExtensionNumberConfDataByExtensionNumberInfoId(
                            dbConnection,
                            extensionNumberInfoId,
                            extensionNumberInfoList,
                            pickUpGroupList);
                    // #834 START
                    //(VoIP-GWの考慮漏れ。sip_内線番号.confファイル生成に必要な同一拠点番号の他のVoIP-GWのデータを取得する)
                    //VoIP-GWは着信グループの対象外のため、実装するがコメントアウト
                    //                setSipExtensionNumberConfDataByExtensionNumberInfoIdForOtherVoipGw(
                    //                        dbConnection,
                    //                        extensionNumberInfoId,
                    //                        extensionNumberInfoList,
                    //                        pickUpGroupList);
                    // #834 END
                }
                
                // #768 START  (extensions_group.conf)
                // N番情報IDからextensions_group.confファイル生成に必要なデータをセット
                setExtensionsGroupConfDataByNNumberInfoId(
                        dbConnection,
                        nNumberInfoId,
                        incomingGroupInfoListOfCallPickUp,
                        childExtensionNumberListExtGroup);
                // #768 END    (extensions_group.conf)

                // 転送先ディレクトリ情報取得メソッド呼出
                ConfigCreatorFile.getInstance().setPathListOfSipExtensionNumberConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        extensionNumberInfoList);
                
                // #768 START  (extensions_group.conf)
                ConfigCreatorFile.getInstance().setPathListOfExtensionsGroupConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo);
                // #768 END    (extensions_group.conf)

                // ファイル生成メソッド呼出
                ConfigCreatorFile.getInstance().createSipExtensionNumberConf(extensionNumberInfoList,
                        pickUpGroupList,
                        nNumberInfo);        
                
                // #768 START  (extensions_group.conf)
                // extensions_group.conf
                ConfigCreatorFile.getInstance().createExtensionsGroupConf(
                        incomingGroupInfoListOfCallPickUp, 
                        childExtensionNumberListExtGroup, 
                        nNumberInfo );
                // #768 END    (extensions_group.conf)


                // DBに内線サーバ設定ファイルバックアップ情報を登録
                executeBackUp(loginId,
                        dbConnection, 
                        nNumberInfoId,
                        localFileList,
                        dstFileList,
                        null);   

                // #748 START
                // ファイル生成 成功時のログ出力
                log.info(Util.message(Const.ERROR_CODE.I020801, String.format(Const.MESSAGE_CODE.I020801,null,null,null,null,loginId)));
                // #748 END

                // #748 START
            }catch(Exception e){
                // ファイル生成 失敗時のログ出力
                log.error(Util.message(Const.ERROR_CODE.E020802, String.format(Const.MESSAGE_CODE.E020802,null,null,null,null,loginId)));
                throw e;
            }
            //     #748 END
            
            // ファイルアップロード処理実行
            // (ファイルバックアップ、SIPリロード含む)
            executeFileUpload(loginId,
                    dbConnection, 
                    nNumberInfoId,
                    localFileList,
                    dstFileList,
                    dstFileBackUpList,
                    dstFileTmpList,
                    null,null); 

        }catch (Exception e){
            log.error("method error :modGroupOfPickup = " + e.toString());

            throw e;
        }
        
        log.debug("method end :modGroupOfPickup");
    } 

    /**
     * G0606_着信グループ設定画面(削除)の結果をAsteriskへ反映する<br>
     * <br>
     * G0606_着信グループ設定画面(削除)で削除ボタン押下時に<br>
     * グループ種別=順次着信の場合に実行する。<br>
     * 
     * @param loginId　ログインID
     * @param dbConnection DBコネクション
     * @param nNumberInfoId　「N番情報ID.N番情報ID」
     * @param extensionNumberInfoId　 削除前の内線代表番号の「内線番号情報.内線番号情報ID」
     * @throws Exception 例外
     */
    public void delGroupOfSlide(String loginId, Connection dbConnection, long nNumberInfoId, 
            long extensionNumberInfoId) 
                    throws Exception {
        try{
            log.debug("method start :delGroupOfSlide");

            // 更新対象の内線サーバ設定ファイルバックアップ情報をロック(同じN番を全ロック)
            executeLock(dbConnection, 
                    nNumberInfoId);

            // 変数を宣言
            NNumberInfo nNumberInfo = getNNumberInfo(dbConnection, nNumberInfoId);
            List<ExtensionNumberInfo> extensionNumberInfoList = new ArrayList<ExtensionNumberInfo>();
            List<AbsenceBehaviorInfo> absenceBehaviorInfoList = new ArrayList<AbsenceBehaviorInfo>();
            List<OutsideCallInfo> outsideCallInfoList = new ArrayList<OutsideCallInfo>();
            List<List<String>> childExtensionNumberInfoList = new ArrayList<List<String>>();
            List<Integer> groupCallTypeList = new ArrayList<Integer>();
            List<String> localFileList = new ArrayList<String>();
            List<String> dstFileList = new ArrayList<String>();
            List<String> dstFileBackUpList = new ArrayList<String>();
            List<String> dstFileTmpList = new ArrayList<String>();

            //     #748 START
            try{
                // #748 END

                // 削除前の内線番号情報IDからファイル生成に必要なデータをセット
                setExtensionsExtensionNumberInConfDataByExtensionNumberInfoId(
                        dbConnection,
                        extensionNumberInfoId, 
                        extensionNumberInfoList, 
                        absenceBehaviorInfoList, 
                        outsideCallInfoList, 
                        childExtensionNumberInfoList, 
                        groupCallTypeList);            
                // #834 START
                //(VoIP-GWの考慮漏れ。sip_内線番号.confファイル生成に必要な同一拠点番号の他のVoIP-GWのデータを取得する)
                //VoIP-GWは着信グループの対象外のため、実装するがコメントアウト
                //            setExtensionsExtensionNumberInConfDataByExtensionNumberInfoIdForOtherVoipGw(
                //                    dbConnection,
                //                    extensionNumberInfoId, 
                //                    extensionNumberInfoList, 
                //                    absenceBehaviorInfoList, 
                //                    outsideCallInfoList, 
                //                    childExtensionNumberInfoList, 
                //                    groupCallTypeList);          
                // #834 END

                // 転送先ディレクトリ情報取得メソッド呼出
                ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberInConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        extensionNumberInfoList);

                //ファイル生成メソッド呼出
                ConfigCreatorFile.getInstance().createExtensionsExtensionNumberInConf(extensionNumberInfoList,
                        absenceBehaviorInfoList,
                        outsideCallInfoList,
                        groupCallTypeList,
                        childExtensionNumberInfoList,
                        nNumberInfo);        

                // DBに内線サーバ設定ファイルバックアップ情報を登録
                executeBackUp(loginId,
                        dbConnection, 
                        nNumberInfoId,
                        localFileList,
                        dstFileList,
                        null);   

                // #748 START
                // ファイル生成 成功時のログ出力
                log.info(Util.message(Const.ERROR_CODE.I020801, String.format(Const.MESSAGE_CODE.I020801,null,null,null,null,loginId)));
                // #748 END

                // #748 START
            }catch(Exception e){
                // ファイル生成 失敗時のログ出力
                log.error(Util.message(Const.ERROR_CODE.E020802, String.format(Const.MESSAGE_CODE.E020802,null,null,null,null,loginId)));
                throw e;
            }
            //     #748 END
            
            // ファイルアップロード処理実行
            // (ファイルバックアップ、SIPリロード含む)
            executeFileUpload(loginId,
                    dbConnection, 
                    nNumberInfoId,
                    localFileList,
                    dstFileList,
                    dstFileBackUpList,
                    dstFileTmpList,
                    null,null); 

        }catch (Exception e){
            log.error("method error :delGroupOfSlide = " + e.toString());

            throw e;
        }
        
        log.debug("method end :delGroupOfSlide");
    }

    /**
     * G0606_着信グループ設定画面(削除)の結果をAsteriskへ反映する<br>
     * <br>
     * G0606_着信グループ設定画面(削除)で削除ボタン押下時に<br>
     * グループ種別=一斉着信の場合に実行する。<br>
     * 
     * @param loginId　ログインID
     * @param dbConnection DBコネクション
     * @param nNumberInfoId　「N番情報ID.N番情報ID」
     * @param extensionNumberInfoId　 削除前の内線代表番号の「内線番号情報.内線番号情報ID」
     * @throws Exception 例外
     */
    public void delGroupOfVolley(String loginId, Connection dbConnection, long nNumberInfoId, 
            long extensionNumberInfoId) 
                    throws Exception {
        try{
            log.debug("method start :delGroupOfVolley");

            // 更新対象の内線サーバ設定ファイルバックアップ情報をロック(同じN番を全ロック)
            executeLock(dbConnection, 
                    nNumberInfoId);

            // 変数を宣言
            NNumberInfo nNumberInfo = getNNumberInfo(dbConnection, nNumberInfoId);
            List<ExtensionNumberInfo> extensionNumberInfoList = new ArrayList<ExtensionNumberInfo>();
            List<AbsenceBehaviorInfo> absenceBehaviorInfoList = new ArrayList<AbsenceBehaviorInfo>();
            List<OutsideCallInfo> outsideCallInfoList = new ArrayList<OutsideCallInfo>();
            List<List<String>> childExtensionNumberInfoList = new ArrayList<List<String>>();
            List<Integer> groupCallTypeList = new ArrayList<Integer>();
            List<String> localFileList = new ArrayList<String>();
            List<String> dstFileList = new ArrayList<String>();
            List<String> dstFileBackUpList = new ArrayList<String>();
            List<String> dstFileTmpList = new ArrayList<String>();

            //     #748 START
            try{
                // #748 END

                // 削除前の内線番号情報IDからファイル生成に必要なデータをセット
                setExtensionsExtensionNumberInConfDataByExtensionNumberInfoId(
                        dbConnection,
                        extensionNumberInfoId, 
                        extensionNumberInfoList, 
                        absenceBehaviorInfoList, 
                        outsideCallInfoList, 
                        childExtensionNumberInfoList, 
                        groupCallTypeList);            
                // #834 START
                //(VoIP-GWの考慮漏れ。sip_内線番号.confファイル生成に必要な同一拠点番号の他のVoIP-GWのデータを取得する)
                //VoIP-GWは着信グループの対象外のため、実装するがコメントアウト
                //            setExtensionsExtensionNumberInConfDataByExtensionNumberInfoIdForOtherVoipGw(
                //                    dbConnection,
                //                    extensionNumberInfoId, 
                //                    extensionNumberInfoList, 
                //                    absenceBehaviorInfoList, 
                //                    outsideCallInfoList, 
                //                    childExtensionNumberInfoList, 
                //                    groupCallTypeList);          
                // #834 END

                // 転送先ディレクトリ情報取得メソッド呼出
                ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberInConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        extensionNumberInfoList);

                //ファイル生成メソッド呼出
                ConfigCreatorFile.getInstance().createExtensionsExtensionNumberInConf(extensionNumberInfoList,
                        absenceBehaviorInfoList,
                        outsideCallInfoList,
                        groupCallTypeList,
                        childExtensionNumberInfoList,
                        nNumberInfo);        

                // DBに内線サーバ設定ファイルバックアップ情報を登録
                executeBackUp(loginId,
                        dbConnection, 
                        nNumberInfoId,
                        localFileList,
                        dstFileList,
                        null);   

                // #748 START
                // ファイル生成 成功時のログ出力
                log.info(Util.message(Const.ERROR_CODE.I020801, String.format(Const.MESSAGE_CODE.I020801,null,null,null,null,loginId)));
                // #748 END

                // #748 START
            }catch(Exception e){
                // ファイル生成 失敗時のログ出力
                log.error(Util.message(Const.ERROR_CODE.E020802, String.format(Const.MESSAGE_CODE.E020802,null,null,null,null,loginId)));
                throw e;
            }
            //     #748 END
            
            // ファイルアップロード処理実行
            // (ファイルバックアップ、SIPリロード含む)
            executeFileUpload(loginId,
                    dbConnection, 
                    nNumberInfoId,
                    localFileList,
                    dstFileList,
                    dstFileBackUpList,
                    dstFileTmpList,
                    null,null); 

        }catch (Exception e){
            log.error("method error :delGroupOfVolley = " + e.toString());

            throw e;
        }
        
        log.debug("method end :delGroupOfVolley");
    }


    /**
     * G0606_着信グループ設定画面(削除)の結果をAsteriskへ反映する<br>
     * <br>
     * G0606_着信グループ設定画面(削除)で削除ボタン押下時に<br>
     * グループ種別=コールピックアップの場合に実行する。<br>
     * 
     * @param loginId　ログインID
     * @param dbConnection DBコネクション
     * @param nNumberInfoId　「N番情報ID.N番情報ID」
     * @param incomingGroupInfoId　削除対象の「着信グループ情報.着信グループ情報ID」
     * @param delExtensionNumberInfoIdArray　削除対象の着信グループに含まれていた子番号の「内線番号情報.内線番号情報ID」のリスト
     * @throws Exception 例外
     */
    public void delGroupOfPickup(String loginId, Connection dbConnection, long nNumberInfoId, 
            long incomingGroupInfoId,
            ArrayList<Long> delExtensionNumberInfoIdArray) 
                    throws Exception {
        try{
            log.debug("method start :delGroupOfPickup");

            // 更新対象の内線サーバ設定ファイルバックアップ情報をロック(同じN番を全ロック)
            executeLock(dbConnection, 
                    nNumberInfoId);

            // 変数を宣言
            NNumberInfo nNumberInfo = getNNumberInfo(dbConnection, nNumberInfoId);
            List<ExtensionNumberInfo> extensionNumberInfoList = new ArrayList<ExtensionNumberInfo>();
            List<String> pickUpGroupList = new ArrayList<String>();
            List<String> localFileList = new ArrayList<String>();
            List<String> dstFileList = new ArrayList<String>();
            List<String> dstFileBackUpList = new ArrayList<String>();
            List<String> dstFileTmpList = new ArrayList<String>();
            // #768 START  (extensions_group.conf)
            // 変数を宣言(extensions_group.conf用)
            List<IncomingGroupInfo> incomingGroupInfoListOfCallPickUp = new ArrayList<IncomingGroupInfo>();
            List<List<String>> childExtensionNumberListExtGroup = new ArrayList<List<String>>();
            // #768 END    (extensions_group.conf)

            //     #748 START
            try{
                // #748 END

                // 削除される内線番号情報IDのリストからデータをセット       
                Iterator<Long> itrDelExtensionNumberInfoId = delExtensionNumberInfoIdArray.iterator();
                while(itrDelExtensionNumberInfoId.hasNext()){     
                    // 内線番号情報IDのリストから内線番号情報IDを取得
                    long extensionNumberInfoId = itrDelExtensionNumberInfoId.next();        

                    // 内線番号情報IDからsip_内線番号.confファイル生成に必要なデータをセット
                    setSipExtensionNumberConfDataByExtensionNumberInfoId(
                            dbConnection,
                            extensionNumberInfoId,
                            extensionNumberInfoList,
                            pickUpGroupList);
                    // #834 START
                    //(VoIP-GWの考慮漏れ。sip_内線番号.confファイル生成に必要な同一拠点番号の他のVoIP-GWのデータを取得する)
                    //VoIP-GWは着信グループの対象外のため、実装するがコメントアウト
                    //                setSipExtensionNumberConfDataByExtensionNumberInfoIdForOtherVoipGw(
                    //                        dbConnection,
                    //                        extensionNumberInfoId,
                    //                        extensionNumberInfoList,
                    //                        pickUpGroupList);
                    // #834 END
                }
                
                // #768 START  (extensions_group.conf)
                // N番情報IDからextensions_group.confファイル生成に必要なデータをセット
                setExtensionsGroupConfDataByNNumberInfoId(
                        dbConnection,
                        nNumberInfoId,
                        incomingGroupInfoListOfCallPickUp,
                        childExtensionNumberListExtGroup);
                // #768 END    (extensions_group.conf)

                // 転送先ディレクトリ情報取得メソッド呼出
                ConfigCreatorFile.getInstance().setPathListOfSipExtensionNumberConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        extensionNumberInfoList);
                
                // #768 START  (extensions_group.conf)
                ConfigCreatorFile.getInstance().setPathListOfExtensionsGroupConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo);
                // #768 END    (extensions_group.conf)

                //ファイル生成メソッド呼出
                ConfigCreatorFile.getInstance().createSipExtensionNumberConf(extensionNumberInfoList,
                        pickUpGroupList,
                        nNumberInfo);        
                
                // #768 START  (extensions_group.conf)
                // extensions_group.conf
                ConfigCreatorFile.getInstance().createExtensionsGroupConf(
                        incomingGroupInfoListOfCallPickUp, 
                        childExtensionNumberListExtGroup, 
                        nNumberInfo );
                // #768 END    (extensions_group.conf)

                
                // DBに内線サーバ設定ファイルバックアップ情報を登録
                executeBackUp(loginId,
                        dbConnection, 
                        nNumberInfoId,
                        localFileList,
                        dstFileList,
                        null);   

                // #748 START
                // ファイル生成 成功時のログ出力
                log.info(Util.message(Const.ERROR_CODE.I020801, String.format(Const.MESSAGE_CODE.I020801,null,null,null,null,loginId)));
                // #748 END

                // #748 START
            }catch(Exception e){
                // ファイル生成 失敗時のログ出力
                log.error(Util.message(Const.ERROR_CODE.E020802, String.format(Const.MESSAGE_CODE.E020802,null,null,null,null,loginId)));
                throw e;
            }
            //     #748 END
            
            // ファイルアップロード処理実行
            // (ファイルバックアップ、SIPリロード含む)
            executeFileUpload(loginId,
                    dbConnection, 
                    nNumberInfoId,
                    localFileList,
                    dstFileList,
                    dstFileBackUpList,
                    dstFileTmpList,
                    null,null); 

        }catch (Exception e){
            log.error("method error :delGroupOfPickup = " + e.toString());

            throw e;
        }
        
        log.debug("method end :delGroupOfPickup");
    }

    /**
     * G0701_外線着信設定画面(一覧表示)の結果をAsteriskへ反映する<br>
     * <br>
     * G0701_外線着信設定画面(一覧表示)でCSV取込ボタン押下時に実行する。<br>
     * 
     * @param loginId　ログインID
     * @param dbConnection DBコネクション
     * @param nNumberInfoId　「N番情報ID.N番情報ID」
     * @param addOutsideCallInfoIdArray　CSV取込で追加対象の「外線情報.外線情報ID」のリスト
     * @param modOutsideCallInfoIdArray　CSV取込で変更対象の「外線情報.外線情報ID」のリスト
     * @param delOutsideCallInfoIdArray　CSV取込で削除対象の「外線情報.外線情報ID」のリスト
     * @throws Exception 例外
     */
    public void importOutsideCall(String loginId, Connection dbConnection, long nNumberInfoId, 
            ArrayList<Long> addOutsideCallInfoIdArray,
            ArrayList<Long> modOutsideCallInfoIdArray,
            ArrayList<Long> delOutsideCallInfoIdArray) 
                    throws Exception {
        try{
            log.debug("method start :importOutsideCall");   

            // 更新対象の内線サーバ設定ファイルバックアップ情報をロック(同じN番を全ロック)
            executeLock(dbConnection, 
                    nNumberInfoId);

            // 変数を宣言(共通)        
            NNumberInfo nNumberInfo = getNNumberInfo(dbConnection, nNumberInfoId);
            List<String> localFileList = new ArrayList<String>();
            List<String> dstFileList = new ArrayList<String>();
            List<String> dstFileBackUpList = new ArrayList<String>();
            List<String> dstFileTmpList = new ArrayList<String>();
            List<String> delFileList = new ArrayList<String>();
            List<String> delFileBackUpList = new ArrayList<String>();   

            // 変数を宣言(Sip.conf用)
            List<VmInfo> vmInfoSipList = new ArrayList<VmInfo>();
            List<OutsideCallInfo> outsideCallInfoListSip = new ArrayList<OutsideCallInfo>();

            // 変数を宣言(sip_外線番号.conf用)
            List<OutsideCallInfo> outsideCallInfoListSipOut = new ArrayList<OutsideCallInfo>();

            // 変数を宣言(sip_reg外線番号.conf用)
            List<OutsideCallInfo> outsideCallInfoListSipReg = new ArrayList<OutsideCallInfo>();

            // 変数を宣言(extensions_外線番号_in.conf用)
            List<OutsideCallInfo> outsideCallInfoListExtIn = new ArrayList<OutsideCallInfo>();
            List<OutsideCallIncomingInfo> outsideCallIncomingInfoListExtIn = new ArrayList<OutsideCallIncomingInfo>();
            List<ExtensionNumberInfo> extensionNumberInfoListExtIn = new ArrayList<ExtensionNumberInfo>();

            // 変数を宣言(extensions_out.conf用)
            List<OutsideCallInfo> outsideCallInfoListExtOut = new ArrayList<OutsideCallInfo>();

            // 変数を宣言(削除対象の一覧用)
            List<String> delSipOutgoingNumberList = new ArrayList<String>();
            List<String> delSipRegOutgoingNumberList = new ArrayList<String>();       
            List<String> delExtensionOutgoingNumberList = new ArrayList<String>();

            //     #748 START
            try{
                // #748 END
                    
                // N番情報IDからsip.confファイル生成に必要なデータをセット
                setSipConfDataByNNumberInfoId(
                        dbConnection,
                        nNumberInfoId,
                        vmInfoSipList,
                        outsideCallInfoListSip);

                // N番情報IDからextensions_out.confファイル生成に必要なデータをセット
                // (着信内線番号の設定/未設定の切替がDB上からわからないため、一律生成し直す)
                setExtensionsOutConfByNNumberInfoId(
                        dbConnection,
                        nNumberInfoId,
                        outsideCallInfoListExtOut);

                // 追加の外線情報IDリストから、ファイル生成に必要なデータをセット
                Iterator<Long> itrAddOutsideCallInfoId = addOutsideCallInfoIdArray.iterator();
                while(itrAddOutsideCallInfoId.hasNext()){ 
                    // 外線情報IDを取得
                    long outsideCallInfoId = itrAddOutsideCallInfoId.next();

                    // #1176 START (着信先未設定でも内線番号_inを生成する)
//                    // 着信内線番号が設定済みかを取得
//                    boolean incomingExtensionNumberExistflg = incomingExtensionNumberExist(
//                            dbConnection,
//                            outsideCallInfoId);
                    // #1176 END

                    // 外線情報IDから、sip_外線番号.confファイル生成に必要なデータをセット
                    setSipOutgoingNumberConfDataByOutsideCallInfoId(
                            dbConnection,
                            outsideCallInfoId,
                            outsideCallInfoListSipOut);

                    // 外線情報IDから、sip_reg外線番号.confファイル生成に必要なデータをセット
                    setSipRegOutgoingNumberConfDataByOutsideCallInfoId(
                            dbConnection,
                            outsideCallInfoId,
                            outsideCallInfoListSipReg);

                    // #1176 START (着信先未設定でも内線番号_inを生成する。if文の撤去)
                    //if(incomingExtensionNumberExistflg){
                    // 外線情報IDから、extensions_外線番号_in.conファイル生成に必要なデータをセット
                    setExtensionsOutgoingNumberInConfDataByOutsideCallInfoId(
                            dbConnection,
                            outsideCallInfoId,
                            outsideCallInfoListExtIn,
                            outsideCallIncomingInfoListExtIn,
                            extensionNumberInfoListExtIn);
                    //}
                    // #1176 END
                }

                // 変更の外線情報IDリストから、ファイル生成に必要なデータと
                // extensions_外線番号_in.confの削除対象とする外線番号をセット
                Iterator<Long> itrModOutsideCallInfoId = modOutsideCallInfoIdArray.iterator();
                while(itrModOutsideCallInfoId.hasNext()){ 
                    // 外線情報IDを取得
                    long outsideCallInfoId = itrModOutsideCallInfoId.next();

                    // #1176 START (着信先未設定でも内線番号_inを生成する)
//                    // 着信内線番号が設定済みかを取得
//                    boolean incomingExtensionNumberExistflg = incomingExtensionNumberExist(
//                            dbConnection,
//                            outsideCallInfoId);
                    // #1176 END

                    // 外線情報IDから、sip_外線番号.confファイル生成に必要なデータをセット
                    setSipOutgoingNumberConfDataByOutsideCallInfoId(
                            dbConnection,
                            outsideCallInfoId,
                            outsideCallInfoListSipOut);

                    // 外線情報IDから、sip_reg外線番号.confファイル生成に必要なデータをセット
                    setSipRegOutgoingNumberConfDataByOutsideCallInfoId(
                            dbConnection,
                            outsideCallInfoId,
                            outsideCallInfoListSipReg);

                    // #1176 START (着信先未設定でも内線番号_inを生成する。if文の撤去)
                    //if(incomingExtensionNumberExistflg){
                        // 外線情報IDから、extensions_外線番号_in.confファイル生成に必要なデータをセット
                    setExtensionsOutgoingNumberInConfDataByOutsideCallInfoId(
                            dbConnection,
                            outsideCallInfoId,
                            outsideCallInfoListExtIn,
                            outsideCallIncomingInfoListExtIn,
                            extensionNumberInfoListExtIn);
                    //}
                    //else{
                        //extensions_外線番号_in.confファイル削除のため、外線番号をリストにセット
                        //delExtensionOutgoingNumberList.add(getOutsideCallNumber(dbConnection, outsideCallInfoId));
                    //}
                    // #1176 END
                }

                // 削除の外線情報IDのリストから、sip_外線番号.confとip_reg外線番号.confファイル
                // extensions_外線番号_in.confの削除対象とする外線番号をセット
                Iterator<Long> itrDelOutsideCallInfoId = delOutsideCallInfoIdArray.iterator();
                while(itrDelOutsideCallInfoId.hasNext()){ 
                    // 外線情報IDを取得
                    long outsideCallInfoId = itrDelOutsideCallInfoId.next();

                    // 外線番号を取得
                    String outsideCallNumber = getOutsideCallNumber(dbConnection, outsideCallInfoId);

                    // #1176 START (着信先未設定でも内線番号_inを生成する)
                    // 着信内線番号が設定済みかを取得
//                    boolean incomingExtensionNumberExistflg = incomingExtensionNumberExist(
//                            dbConnection, 
//                            outsideCallInfoId);
                    // #1176 END

                    // sip_外線番号.confとip_reg外線番号.confファイル削除のため、外線番号をリストにセット
                    delSipOutgoingNumberList.add(outsideCallNumber);
                    delSipRegOutgoingNumberList.add(outsideCallNumber);

                    // #1176 START (着信先未設定でも内線番号_inを生成する。if文の撤去)
                    //if(incomingExtensionNumberExistflg){
                    // extensions_外線番号_in.confファイル削除のため、外線番号をリストにセット
                    delExtensionOutgoingNumberList.add(outsideCallNumber);
                    //}
                    // #1176 END
                }

                // 転送先ディレクトリ情報取得メソッド呼出
                // sip.conf
                ConfigCreatorFile.getInstance().setPathListOfSipConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo);
                // sip_外線番号.conf
                ConfigCreatorFile.getInstance().setPathListOfSipOutgoingNumberConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        outsideCallInfoListSipOut);
                // sip_reg外線番号.conf
                ConfigCreatorFile.getInstance().setPathListOfSipRegOutgoingNumberConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        outsideCallInfoListSipReg);
                // extensions_out.conf
                ConfigCreatorFile.getInstance().setPathListOfExtensionsOutConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo);
                //  extensions_外線番号_in.conf
                ConfigCreatorFile.getInstance().setPathListOfExtensionsOutgoingNumberInConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        outsideCallInfoListExtIn);

                // 削除先ディレクトリ情報取得メソッド呼出
                // sip_外線番号.conf
                ConfigCreatorFile.getInstance().setPathListOfSipOutgoingNumberConfByOutgoingNumber(
                        new ArrayList<String>(),
                        delFileList,
                        delFileBackUpList,
                        new ArrayList<String>(),
                        nNumberInfo,
                        delSipOutgoingNumberList);
                // sip_reg外線番号.conf
                ConfigCreatorFile.getInstance().setPathListOfSipRegOutgoingNumberConfByOutgoingNumber(
                        new ArrayList<String>(),
                        delFileList,
                        delFileBackUpList,
                        new ArrayList<String>(),
                        nNumberInfo,
                        delSipRegOutgoingNumberList);
                //  extensions_外線番号_in.conf
                ConfigCreatorFile.getInstance().setPathListOfExtensionsOutgoingNumberInConfByOutgoingNumber(
                        new ArrayList<String>(),
                        delFileList,
                        delFileBackUpList,
                        new ArrayList<String>(),
                        nNumberInfo,
                        delExtensionOutgoingNumberList);

                // ファイル生成メソッド呼出
                // sip.conf
                ConfigCreatorFile.getInstance().createSipConf(vmInfoSipList.get(0),
                        outsideCallInfoListSip,
                        nNumberInfo);
                // sip_外線番号.conf
                ConfigCreatorFile.getInstance().createSipOutgoingNumberConf(outsideCallInfoListSipOut,
                        nNumberInfo);
                // sip_reg外線番号.conf
                ConfigCreatorFile.getInstance().createSipRegOutgoingNumberConf(outsideCallInfoListSipReg,
                        nNumberInfo);
                // extensions_out.conf
                ConfigCreatorFile.getInstance().createExtensionsOutConf(outsideCallInfoListExtOut,
                        nNumberInfo);
                //  extensions_外線番号_in.conf
                ConfigCreatorFile.getInstance().createExtensionsOutgoingNumberInConf(outsideCallInfoListExtIn,
                        outsideCallIncomingInfoListExtIn,
                        extensionNumberInfoListExtIn,
                        nNumberInfo);

                // DBに内線サーバ設定ファイルバックアップ情報を登録
                executeBackUp(loginId,
                        dbConnection, 
                        nNumberInfoId,
                        localFileList,
                        dstFileList,
                        delFileList);   

                // #748 START
                // ファイル生成 成功時のログ出力
                log.info(Util.message(Const.ERROR_CODE.I020801, String.format(Const.MESSAGE_CODE.I020801,null,null,null,null,loginId)));
                // #748 END

                // #748 START
            }catch(Exception e){
                // ファイル生成 失敗時のログ出力
                log.error(Util.message(Const.ERROR_CODE.E020802, String.format(Const.MESSAGE_CODE.E020802,null,null,null,null,loginId)));
                throw e;
            }
            //     #748 END

            // ファイルアップロード処理実行
            // (ファイルバックアップ、SIPリロード含む)
            executeFileUpload(loginId,
                    dbConnection, 
                    nNumberInfoId,
                    localFileList,
                    dstFileList,
                    dstFileBackUpList,
                    dstFileTmpList,
                    delFileList,
                    delFileBackUpList); 

        }catch (Exception e){
            log.error("method error :importOutsideCall = " + e.toString());

            throw e;
        }
        
        log.debug("method end :importOutsideCall");
    }

    /**
     * G0702_外線着信設定画面(追加)の結果をAsteriskへ反映する<br>
     * <br>
     * G0702_外線着信設定画面(追加)で外線番号追加ボタン押下時に実行する。<br>
     * 
     * @param loginId　ログインID
     * @param dbConnection DBコネクション
     * @param nNumberInfoId　「N番情報ID.N番情報ID」
     * @param outsideCallInfoId　追加された「外線情報.外線情報ID」
     * @throws Exception 例外
     */
    public void addOutsideCall(String loginId, Connection dbConnection, long nNumberInfoId, 
            long outsideCallInfoId) 
                    throws Exception {
        try{
            log.debug("method start :addOutsideCall");  

            // 更新対象の内線サーバ設定ファイルバックアップ情報をロック(同じN番を全ロック)
            executeLock(dbConnection, 
                    nNumberInfoId);

            // 変数を宣言(共通)        
            NNumberInfo nNumberInfo = getNNumberInfo(dbConnection, nNumberInfoId);
            List<String> localFileList = new ArrayList<String>();
            List<String> dstFileList = new ArrayList<String>();
            List<String> dstFileBackUpList = new ArrayList<String>();
            List<String> dstFileTmpList = new ArrayList<String>();
            // #1176 START (着信先未設定でも内線番号_inを生成する)
//            boolean incomingExtensionNumberExistflg = incomingExtensionNumberExist(
//                    dbConnection,
//                    outsideCallInfoId);
            // #1176 END

            // 変数を宣言(Sip.conf用)
            List<VmInfo> vmInfoSipList = new ArrayList<VmInfo>();
            List<OutsideCallInfo> outsideCallInfoListSip = new ArrayList<OutsideCallInfo>();

            // 変数を宣言(sip_外線番号.conf用)
            List<OutsideCallInfo> outsideCallInfoListSipOut = new ArrayList<OutsideCallInfo>();

            // 変数を宣言(sip_reg外線番号.conf用)
            List<OutsideCallInfo> outsideCallInfoListSipReg = new ArrayList<OutsideCallInfo>();

            // 変数を宣言(extensions_外線番号_in.conf用)
            List<OutsideCallInfo> outsideCallInfoListExtIn = new ArrayList<OutsideCallInfo>();
            List<OutsideCallIncomingInfo> outsideCallIncomingInfoListExtIn = new ArrayList<OutsideCallIncomingInfo>();
            List<ExtensionNumberInfo> extensionNumberInfoListExtIn = new ArrayList<ExtensionNumberInfo>();

            // 変数を宣言(extensions_out.conf用)
            List<OutsideCallInfo> outsideCallInfoListExtOut = new ArrayList<OutsideCallInfo>();

            //     #748 START
            try{
                // #748 END

                // N番情報IDからsip.confファイル生成に必要なデータをセット
                setSipConfDataByNNumberInfoId(
                        dbConnection,
                        nNumberInfoId,
                        vmInfoSipList,
                        outsideCallInfoListSip);

                // 外線情報IDから、sip_外線番号.confファイル生成に必要なデータをセット
                setSipOutgoingNumberConfDataByOutsideCallInfoId(
                        dbConnection,
                        outsideCallInfoId,
                        outsideCallInfoListSipOut);

                // 外線情報IDから、sip_reg外線番号.confファイル生成に必要なデータをセット
                setSipRegOutgoingNumberConfDataByOutsideCallInfoId(
                        dbConnection,
                        outsideCallInfoId,
                        outsideCallInfoListSipReg);

                // #1176 START (着信先未設定でも内線番号_inを生成する。if文の撤去)
                //if(incomingExtensionNumberExistflg){
                // 外線情報IDから、extensions_外線番号_in.conファイル生成に必要なデータをセット
                setExtensionsOutgoingNumberInConfDataByOutsideCallInfoId(
                        dbConnection,
                        outsideCallInfoId,
                        outsideCallInfoListExtIn,
                        outsideCallIncomingInfoListExtIn,
                        extensionNumberInfoListExtIn);

                //N番情報IDからextensions_out.confファイル生成に必要なデータをセット
                setExtensionsOutConfByNNumberInfoId(
                        dbConnection,
                        nNumberInfoId,
                        outsideCallInfoListExtOut);
                //}
                // #1176 END

                // 転送先ディレクトリ情報取得メソッド呼出
                // sip.conf
                ConfigCreatorFile.getInstance().setPathListOfSipConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo);
                // sip_外線番号.conf
                ConfigCreatorFile.getInstance().setPathListOfSipOutgoingNumberConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        outsideCallInfoListSipOut);
                // sip_reg外線番号.conf
                ConfigCreatorFile.getInstance().setPathListOfSipRegOutgoingNumberConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        outsideCallInfoListSipReg);
                // #1176 START (着信先未設定でも内線番号_inを生成する。if文の撤去)
                //if(incomingExtensionNumberExistflg){
                // extensions_out.conf
                ConfigCreatorFile.getInstance().setPathListOfExtensionsOutConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo);
                //  extensions_外線番号_in.conf
                ConfigCreatorFile.getInstance().setPathListOfExtensionsOutgoingNumberInConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        outsideCallInfoListExtIn);
                // #1176 END
                //}

                // ファイル生成メソッド呼出
                // sip.conf
                ConfigCreatorFile.getInstance().createSipConf(vmInfoSipList.get(0),
                        outsideCallInfoListSip,
                        nNumberInfo);
                // sip_外線番号.conf
                ConfigCreatorFile.getInstance().createSipOutgoingNumberConf(outsideCallInfoListSipOut,
                        nNumberInfo);
                // sip_reg外線番号.conf
                ConfigCreatorFile.getInstance().createSipRegOutgoingNumberConf(outsideCallInfoListSipReg,
                        nNumberInfo);
                // #1176 START (着信先未設定でも内線番号_inを生成する。if文の撤去)
                // 着信内線番号が設定時
                //if(incomingExtensionNumberExistflg){
                // extensions_out.conf
                ConfigCreatorFile.getInstance().createExtensionsOutConf(outsideCallInfoListExtOut,
                        nNumberInfo);
                //  extensions_外線番号_in.conf
                ConfigCreatorFile.getInstance().createExtensionsOutgoingNumberInConf(outsideCallInfoListExtIn,
                        outsideCallIncomingInfoListExtIn,
                        extensionNumberInfoListExtIn,
                        nNumberInfo);
                //}
                // #1176 END

                // DBに内線サーバ設定ファイルバックアップ情報を登録
                executeBackUp(loginId,
                        dbConnection, 
                        nNumberInfoId,
                        localFileList,
                        dstFileList,
                        null);   

                // #748 START
                // ファイル生成 成功時のログ出力
                log.info(Util.message(Const.ERROR_CODE.I020801, String.format(Const.MESSAGE_CODE.I020801,null,null,null,null,loginId)));
                // #748 END

                // #748 START
            }catch(Exception e){
                // ファイル生成 失敗時のログ出力
                log.error(Util.message(Const.ERROR_CODE.E020802, String.format(Const.MESSAGE_CODE.E020802,null,null,null,null,loginId)));
                throw e;
            }
            //     #748 END
            
            // ファイルアップロード処理実行
            // (ファイルバックアップ、SIPリロード含む)
            executeFileUpload(loginId,
                    dbConnection,
                    nNumberInfoId,
                    localFileList,
                    dstFileList,
                    dstFileBackUpList,
                    dstFileTmpList,
                    null,null); 

        }catch (Exception e){
            log.error("method error :addOutsideCall = " + e.toString());

            throw e;
        }
        
        log.debug("method end :addOutsideCall");
    }

    /**
     * G0704_外線着信設定画面(変更)の結果をAsteriskへ反映する<br>
     * <br>
     * G0704_外線着信設定画面(変更)で設定ボタン押下時に実行する。<br>
     * 
     * @param loginId　ログインID08
     * @param dbConnection DBコネクション
     * @param nNumberInfoId　「N番情報ID.N番情報ID」
     * @param outsideCallInfoId　設定が変更された「外線情報.外線情報ID」
     * @throws Exception 例外
     */
    public void modOutsideCall(String loginId, Connection dbConnection, long nNumberInfoId, 
            long outsideCallInfoId) 
                    throws Exception {
        try{
            log.debug("method start :modOutsideCall");

            // 更新対象の内線サーバ設定ファイルバックアップ情報をロック(同じN番を全ロック)
            executeLock(dbConnection, 
                    nNumberInfoId);

            // 変数を宣言(共通)        
            NNumberInfo nNumberInfo = getNNumberInfo(dbConnection, nNumberInfoId);
            List<String> localFileList = new ArrayList<String>();
            List<String> dstFileList = new ArrayList<String>();
            List<String> dstFileBackUpList = new ArrayList<String>();
            List<String> dstFileTmpList = new ArrayList<String>();
            List<String> delFileList = new ArrayList<String>();
            List<String> delFileBackUpList = new ArrayList<String>();
            // #1176 START (着信先未設定でも内線番号_inを生成する)
//            boolean incomingExtensionNumberExistflg = incomingExtensionNumberExist(
//                    dbConnection,
//                    outsideCallInfoId);
            // #1176 END

            // 変数を宣言(extensions_外線番号_in.conf用)
            List<OutsideCallInfo> outsideCallInfoListExtIn = new ArrayList<OutsideCallInfo>();
            List<OutsideCallIncomingInfo> outsideCallIncomingInfoListExtIn = new ArrayList<OutsideCallIncomingInfo>();
            List<ExtensionNumberInfo> extensionNumberInfoListExtIn = new ArrayList<ExtensionNumberInfo>();

            // 変数を宣言(extensions_out.conf用)
            List<OutsideCallInfo> outsideCallInfoListExtOut = new ArrayList<OutsideCallInfo>();

            //     #748 START
            try{
                // #748 END

                // 外線情報IDから、extensions_外線番号_in.confファイル生成に必要なデータをセット
                setExtensionsOutgoingNumberInConfDataByOutsideCallInfoId(
                        dbConnection,
                        outsideCallInfoId,
                        outsideCallInfoListExtIn,
                        outsideCallIncomingInfoListExtIn,
                        extensionNumberInfoListExtIn);

                // N番情報IDからextensions_out.confファイル生成に必要なデータをセット
                // (着信内線番号の設定/未設定の切替がDB上からわからないため、一律生成し直す)
                setExtensionsOutConfByNNumberInfoId(
                        dbConnection,
                        nNumberInfoId,
                        outsideCallInfoListExtOut);

                // 転送先ディレクトリ情報取得メソッド呼出
                // extensions_out.conf
                ConfigCreatorFile.getInstance().setPathListOfExtensionsOutConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo);

             // #1176 START (着信先未設定でも内線番号_inを生成する。if文の撤去)
              // extensions_外線番号_in.confを生成
              ConfigCreatorFile.getInstance().setPathListOfExtensionsOutgoingNumberInConf(
                      localFileList,
                      dstFileList,
                      dstFileBackUpList,
                      dstFileTmpList,
                      nNumberInfo,
                      outsideCallInfoListExtIn);
                
//                // 着信内線番号の設定/未設定によって、処理を分岐
//                if(incomingExtensionNumberExistflg){
//                    // extensions_外線番号_in.confをアップロード
//                    ConfigCreatorFile.getInstance().setPathListOfExtensionsOutgoingNumberInConf(
//                            localFileList,
//                            dstFileList,
//                            dstFileBackUpList,
//                            dstFileTmpList,
//                            nNumberInfo,
//                            outsideCallInfoListExtIn);
//                }else{
//                    // extensions_外線番号_in.confを削除
//                    ConfigCreatorFile.getInstance().setPathListOfExtensionsOutgoingNumberInConf(
//                            new ArrayList<String>(),
//                            delFileList,
//                            delFileBackUpList,
//                            new ArrayList<String>(),
//                            nNumberInfo,
//                            outsideCallInfoListExtIn);
//                }
              // #1176 END

                // ファイル生成メソッド呼出
                // extensions_out.conf(着信内線番号の設定/未設定の切替がDB上からわからないため、一律生成し直す)
                ConfigCreatorFile.getInstance().createExtensionsOutConf(outsideCallInfoListExtOut,
                        nNumberInfo);

                // #1176 START (着信先未設定でも内線番号_inを生成する。if文の撤去)
                // 着信内線番号が設定時
                //if(incomingExtensionNumberExistflg){
                //  extensions_外線番号_in.conf
                ConfigCreatorFile.getInstance().createExtensionsOutgoingNumberInConf(outsideCallInfoListExtIn,
                        outsideCallIncomingInfoListExtIn,
                        extensionNumberInfoListExtIn,
                        nNumberInfo);
                //}
                // #1176 END

                // DBに内線サーバ設定ファイルバックアップ情報を登録
                executeBackUp(loginId,
                        dbConnection, 
                        nNumberInfoId,
                        localFileList,
                        dstFileList,
                        delFileList);  

                // #748 START
                // ファイル生成 成功時のログ出力
                log.info(Util.message(Const.ERROR_CODE.I020801, String.format(Const.MESSAGE_CODE.I020801,null,null,null,null,loginId)));
                // #748 END

                // #748 START
            }catch(Exception e){
                // ファイル生成 失敗時のログ出力
                log.error(Util.message(Const.ERROR_CODE.E020802, String.format(Const.MESSAGE_CODE.E020802,null,null,null,null,loginId)));
                throw e;
            }
            //     #748 END

            // ファイルアップロード処理実行
            // (ファイルバックアップ、SIPリロード含む)
            executeFileUpload(loginId,
                    dbConnection, 
                    nNumberInfoId,
                    localFileList,
                    dstFileList,
                    dstFileBackUpList,
                    dstFileTmpList,
                    delFileList,
                    delFileBackUpList); 

        }catch (Exception e){
            log.error("method error :modOutsideCall = " + e.toString());

            throw e;
        }
        
        log.debug("method end :modOutsideCall");
    }

    /**
     * G0706_外線着信設定画面(削除)の結果をAsteriskへ反映する<br>
     * <br>
     * G0706_外線着信設定画面(削除)で削除ボタン押下時に実行する。<br>
     * 
     * @param loginId　ログインID
     * @param dbConnection DBコネクション
     * @param nNumberInfoId　「N番情報ID.N番情報ID」
     * @param outsideCallInfoId　削除対象の「外線情報.外線情報ID」
     * @throws Exception 例外
     */
    public void delOutsideCall(String loginId, Connection dbConnection, long nNumberInfoId, 
            long outsideCallInfoId) 
                    throws Exception {
        try{
            log.debug("method start :delOutsideCall");  

            // 更新対象の内線サーバ設定ファイルバックアップ情報をロック(同じN番を全ロック)
            executeLock(dbConnection, 
                    nNumberInfoId);

            // 変数を宣言(共通)        
            NNumberInfo nNumberInfo = getNNumberInfo(dbConnection, nNumberInfoId);
            List<String> localFileList = new ArrayList<String>();
            List<String> dstFileList = new ArrayList<String>();
            List<String> dstFileBackUpList = new ArrayList<String>();
            List<String> dstFileTmpList = new ArrayList<String>();
            List<String> delFileList = new ArrayList<String>();
            List<String> delFileBackUpList = new ArrayList<String>();
            // #1176 START (着信先未設定でも内線番号_inを生成する)
//            boolean incomingExtensionNumberExistflg = incomingExtensionNumberExist(
//                    dbConnection,
//                    outsideCallInfoId);
            // #1176 END

            // 変数を宣言(Sip.conf用)
            List<VmInfo> vmInfoSipList = new ArrayList<VmInfo>();
            List<OutsideCallInfo> outsideCallInfoListSip = new ArrayList<OutsideCallInfo>();

            // 変数を宣言(extensions_out.conf用)
            List<OutsideCallInfo> outsideCallInfoListExt = new ArrayList<OutsideCallInfo>();

            // 変数を宣言(削除対象の一覧用)
            List<String> delSipOutgoingNumberList = new ArrayList<String>();
            List<String> delSipRegOutgoingNumberList = new ArrayList<String>();       
            List<String> delExtensionOutgoingNumberList = new ArrayList<String>();

            // #748 START
            try{
                // #748 END

                // N番情報IDからsip.confファイル生成に必要なデータをセット
                setSipConfDataByNNumberInfoId(
                        dbConnection,
                        nNumberInfoId,
                        vmInfoSipList,
                        outsideCallInfoListSip);

                // #1176 START (着信先未設定でも内線番号_inを生成する。if文の撤去)
                // N番情報IDからextensions_out.confファイル生成に必要なデータをセット
                //if(incomingExtensionNumberExistflg){
                setExtensionsOutConfByNNumberInfoId(
                        dbConnection,
                        nNumberInfoId,
                        outsideCallInfoListExt);
                //}
                // #1176 END

                // 外線番号を取得
                String outsideCallNumber = getOutsideCallNumber(dbConnection, outsideCallInfoId);

                // sip_外線番号.confとip_reg外線番号.confファイル削除のため、外線番号をリストにセット
                delSipOutgoingNumberList.add(outsideCallNumber);
                delSipRegOutgoingNumberList.add(outsideCallNumber);

                // #1176 START (着信先未設定でも内線番号_inを生成する。if文の撤去)
                //if(incomingExtensionNumberExistflg){
                // extensions_外線番号_in.confファイル削除のため、外線番号をリストにセット
                delExtensionOutgoingNumberList.add(outsideCallNumber);
                //}
                // #1176 END

                // 転送先ディレクトリ情報取得メソッド呼出
                // sip.conf
                ConfigCreatorFile.getInstance().setPathListOfSipConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo);

                // sip_外線番号.conf(削除)
                ConfigCreatorFile.getInstance().setPathListOfSipOutgoingNumberConfByOutgoingNumber(
                        new ArrayList<String>(),
                        delFileList,
                        delFileBackUpList,
                        new ArrayList<String>(),
                        nNumberInfo,
                        delSipOutgoingNumberList);
                // sip_reg外線番号.conf(削除)
                ConfigCreatorFile.getInstance().setPathListOfSipRegOutgoingNumberConfByOutgoingNumber(
                        new ArrayList<String>(),
                        delFileList,
                        delFileBackUpList,
                        new ArrayList<String>(),
                        nNumberInfo,
                        delSipRegOutgoingNumberList);

                // #1176 START (着信先未設定でも内線番号_inを生成する。if文の撤去)
                //if(incomingExtensionNumberExistflg){
                // extensions_外線番号_in.conf(削除)
                ConfigCreatorFile.getInstance().setPathListOfExtensionsOutgoingNumberInConfByOutgoingNumber(
                        new ArrayList<String>(),
                        delFileList,
                        delFileBackUpList,
                        new ArrayList<String>(),
                        nNumberInfo,
                        delExtensionOutgoingNumberList);        
                // extensions_out.conf
                ConfigCreatorFile.getInstance().setPathListOfExtensionsOutConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo);
                //}
                // #1176 END
                
                // ファイル生成メソッド呼出
                // sip.conf
                ConfigCreatorFile.getInstance().createSipConf(vmInfoSipList.get(0),
                        outsideCallInfoListSip,
                        nNumberInfo);
                // #1176 START (着信先未設定でも内線番号_inを生成する。if文の撤去)
                //if(incomingExtensionNumberExistflg){
                // extensions_out.conf
                ConfigCreatorFile.getInstance().createExtensionsOutConf(outsideCallInfoListExt,
                        nNumberInfo);
                //}
                // #1176 END

                // DBに内線サーバ設定ファイルバックアップ情報を登録
                executeBackUp(loginId,
                        dbConnection, 
                        nNumberInfoId,
                        localFileList,
                        dstFileList,
                        delFileList);  

                // #748 START
                // ファイル生成 成功時のログ出力
                log.info(Util.message(Const.ERROR_CODE.I020801, String.format(Const.MESSAGE_CODE.I020801,null,null,null,null,loginId)));
                // #748 END

                // #748 START
            }catch(Exception e){
                // ファイル生成 失敗時のログ出力
                log.error(Util.message(Const.ERROR_CODE.E020802, String.format(Const.MESSAGE_CODE.E020802,null,null,null,null,loginId)));
                throw e;
            }
            //     #748 END
            
            // ファイルアップロード処理実行
            // (ファイルバックアップ、SIPリロード含む)
            executeFileUpload(loginId,
                    dbConnection, 
                    nNumberInfoId,
                    localFileList,
                    dstFileList,
                    dstFileBackUpList,
                    dstFileTmpList,
                    delFileList,
                    delFileBackUpList); 

        }catch (Exception e){
            log.error("method error :delOutsideCall = " + e.toString());

            throw e;
        }
        
        log.debug("method end :delOutsideCall");
    }

    /**
     * G0801_外線発信設定画面(一覧表示)<br>
     * G1001_発信規制先設定画面(設定)の結果をAsteriskへ反映する<br>
     * <br>
     * G0801_外線発信設定画面(一覧表示)で外線Prefix設定ボタン押下時<br>
     * G1001_発信規制先設定画面(設定)で変更ボタン押下時に実行する。<br>
     * 
     * @param loginId　ログインID
     * @param dbConnection DBコネクション
     * @param nNumberInfoId　設定を変更した内線サーバの「N番情報.N番情報ID」
     * @throws Exception 例外
     */
    public void setGlobalSetting(String loginId, Connection dbConnection, long nNumberInfoId) 
            throws Exception {
        try{
            log.debug("method start :setGlobalSetting");        

            // 更新対象の内線サーバ設定ファイルバックアップ情報をロック(同じN番を全ロック)
            executeLock(dbConnection, 
                    nNumberInfoId);

            // 変数を宣言     
            NNumberInfo nNumberInfo = getNNumberInfo(dbConnection, nNumberInfoId);
            List<String> localFileList = new ArrayList<String>();
            List<String> dstFileList = new ArrayList<String>();
            List<String> dstFileBackUpList = new ArrayList<String>();
            List<String> dstFileTmpList = new ArrayList<String>();
            List<CallRegulationInfo> callRegulationInfoList = new ArrayList<CallRegulationInfo>();
            
            
            // #748 START
            try{
                // #748 END

                // N番情報IDから必要なデータをセット
                setExtensionsGlobalsConfDataByNNumberInfoId(
                        dbConnection,
                        nNumberInfoId,
                        callRegulationInfoList);

                // 転送先ディレクトリ情報取得メソッド呼出
                ConfigCreatorFile.getInstance().setPathListOfExtensionsGlobalConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo);

                // ファイル生成メソッド呼出
                ConfigCreatorFile.getInstance().createExtensionsGlobalConf(callRegulationInfoList.get(0),
                        nNumberInfo);

                // DBに内線サーバ設定ファイルバックアップ情報を登録
                executeBackUp(loginId,
                        dbConnection, 
                        nNumberInfoId,
                        localFileList,
                        dstFileList,
                        null);  

                // #748 START
                // ファイル生成 成功時のログ出力
                log.info(Util.message(Const.ERROR_CODE.I020801, String.format(Const.MESSAGE_CODE.I020801,null,null,null,null,loginId)));
                // #748 END

                // #748 START
            }catch(Exception e){
                // ファイル生成 失敗時のログ出力
                log.error(Util.message(Const.ERROR_CODE.E020802, String.format(Const.MESSAGE_CODE.E020802,null,null,null,null,loginId)));
                throw e;
            }
            //     #748 END

            // ファイルアップロード処理実行
            // (ファイルバックアップ、SIPリロード含む)
            executeFileUpload(loginId,
                    dbConnection, 
                    nNumberInfoId,
                    localFileList,
                    dstFileList,
                    dstFileBackUpList,
                    dstFileTmpList,
                    null,null); 

        }catch (Exception e){
            log.error("method error :setGlobalSetting = " + e.toString());

            throw e;
        }
        
        log.debug("method end :setGlobalSetting");
    }

    /**
     * G0801_外線発信設定画面(一覧表示)の結果をAsteriskへ反映する<br>
     * <br>
     * G0801_外線発信設定画面(一覧表示)でCSV取込ボタン押下時に実行する。<br>
     * 
     * @param loginId　ログインID
     * @param dbConnection DBコネクション
     * @param nNumberInfoId　「N番情報ID.N番情報ID」
     * @param outsideCallSendingInfoIdArray　CSV取込で設定した外線番号の「外線発信情報.外線発信情報ID」のリスト
     * @throws Exception 例外
     */
    public void importUseOutsideForExten(String loginId, Connection dbConnection, long nNumberInfoId, 
            ArrayList<Long> outsideCallSendingInfoIdArray) 
                    throws Exception {
        try{
            log.debug("method start :importUseOutsideForExten");        

            // 更新対象の内線サーバ設定ファイルバックアップ情報をロック(同じN番を全ロック)
            executeLock(dbConnection, 
                    nNumberInfoId);

            // 変数を宣言
            NNumberInfo nNumberInfo = getNNumberInfo(dbConnection, nNumberInfoId);
            List<String> localFileList = new ArrayList<String>();
            List<String> dstFileList = new ArrayList<String>();
            List<String> dstFileBackUpList = new ArrayList<String>();
            List<String> dstFileTmpList = new ArrayList<String>();
            List<ExtensionNumberInfo> extensionNumberInfoList = new ArrayList<ExtensionNumberInfo>();
            List<OutsideCallInfo> outsideCallInfoList = new ArrayList<OutsideCallInfo>();
            // #997 START 
            List<OutsideCallInfo> outsideCallInfoListForDialIn = new ArrayList<OutsideCallInfo>();
            // #997 END
            
            // #1290 START (extensions_内線番号_in.confの生成)
            // 変数を宣言(extensions_内線番号_in.conf用)
            List<ExtensionNumberInfo> extensionNumberInfoListExtIn = new ArrayList<ExtensionNumberInfo>();
            List<AbsenceBehaviorInfo> absenceBehaviorInfoListExtIn = new ArrayList<AbsenceBehaviorInfo>();
            List<OutsideCallInfo> outsideCallInfoListExtIn = new ArrayList<OutsideCallInfo>();
            List<List<String>> childExtensionNumberInfoListExtIn = new ArrayList<List<String>>();
            List<Integer> groupCallTypeListExtIn = new ArrayList<Integer>();
            // #1290 END
            
            // #748 START
            try{
                // #748 END

                // 外線発信情報IDのリストから必要なデータをセット       
                Iterator<Long> itrOutsideCallSendingInfoId = outsideCallSendingInfoIdArray.iterator();
                while(itrOutsideCallSendingInfoId.hasNext()){            
                    
                    // #1290 START (extensions_内線番号_in.confの生成)
                    long tmpOutsideCallSendingInfoId = itrOutsideCallSendingInfoId.next();
                    // #1290 END
                    
                    // 外線発信情報IDから必要なデータをセット
                    // #997 START (引数の追加)
                    setExtensionsExtensionNumberOutConfDataByOutsideCallSendingInfoId(
                            dbConnection,
                            tmpOutsideCallSendingInfoId,
                            extensionNumberInfoList,
                            outsideCallInfoList,
                            outsideCallInfoListForDialIn);
                    // #997 END
                    
                    // #834 START
                    //(VoIP-GWの考慮漏れ。extensions_内線番号_out.confファイル生成に必要な同一拠点番号の他のVoIP-GWのデータを取得する)
                    //本ルートは画面処理側で考慮されている。
                    //外線発信情報に設定される内線番号情報は、VoIP-GWの場合、複数台利用が1となるようになっている。
                    //なお、setExtensionsExtensionNumberOutConfDataByOutsideCallSendingInfoIdに対応するメソッドは実装しないため、
                    //サービス仕様変更時に実装すること
                    // #834 END
                    
                    // #1290 START (extensions_内線番号_in.confの生成)
                    // %SIP_PEER_OUTBAND_KIND%のため、外線発信設定を変更したら、extensions_内線番号_in.confを生成する
                    setExtensionsExtensionNumberInConfDataByOutsideCallSendingInfoId(
                            dbConnection,
                            tmpOutsideCallSendingInfoId,
                            extensionNumberInfoListExtIn,
                            absenceBehaviorInfoListExtIn, 
                            outsideCallInfoListExtIn,
                            childExtensionNumberInfoListExtIn,
                            groupCallTypeListExtIn);
                    
                    // (#834の対処はsetExtensionsExtensionNumberInConfDataByOutsideCallSendingInfoIdの呼び出し先で対処)
                    // #1290 END
                    
                }

                // 転送先ディレクトリ情報取得メソッド呼出
                ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberOutConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        extensionNumberInfoList);
                
                // #1290 START (extensions_内線番号_in.confの生成)
                // %SIP_PEER_OUTBAND_KIND%のため、外線発信設定を変更したら、extensions_内線番号_in.confを生成する
                ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberInConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        extensionNumberInfoListExtIn );
                // #1290 END

                
                // ファイル生成メソッド呼出
                // #997 START (引数の追加)
                ConfigCreatorFile.getInstance().createExtensionsExtensionNumberOutConf(
                        extensionNumberInfoList,
                        outsideCallInfoList,
                        outsideCallInfoListForDialIn,
                        nNumberInfo);        
                // #997 END (引数の追加)

                // #1290 START (extensions_内線番号_in.confの生成)
                // %SIP_PEER_OUTBAND_KIND%のため、外線発信設定を変更したら、extensions_内線番号_in.confを生成する
                ConfigCreatorFile.getInstance().createExtensionsExtensionNumberInConf(
                        extensionNumberInfoListExtIn,
                        absenceBehaviorInfoListExtIn,
                        outsideCallInfoListExtIn,
                        groupCallTypeListExtIn,
                        childExtensionNumberInfoListExtIn,
                        nNumberInfo );
                // #1290 END
                
                
                // DBに内線サーバ設定ファイルバックアップ情報を登録
                executeBackUp(loginId,
                        dbConnection, 
                        nNumberInfoId,
                        localFileList,
                        dstFileList,
                        null);  

                // #748 START
                // ファイル生成 成功時のログ出力
                log.info(Util.message(Const.ERROR_CODE.I020801, String.format(Const.MESSAGE_CODE.I020801,null,null,null,null,loginId)));
                // #748 END

                // #748 START
            }catch(Exception e){
                // ファイル生成 失敗時のログ出力
                log.error(Util.message(Const.ERROR_CODE.E020802, String.format(Const.MESSAGE_CODE.E020802,null,null,null,null,loginId)));
                throw e;
            }
            //     #748 END
            
            // ファイルアップロード処理実行
            // (ファイルバックアップ、SIPリロード含む)
            executeFileUpload(loginId,
                    dbConnection, 
                    nNumberInfoId,
                    localFileList,
                    dstFileList,
                    dstFileBackUpList,
                    dstFileTmpList,
                    null,null); 

        }catch (Exception e){
            log.error("method error :importUseOutsideForExten = " + e.toString());

            throw e;
        }
        
        log.debug("method end :importUseOutsideForExten");
    }   

    /**
     * G0802_外線発信設定画面(設定)の結果をAsteriskへ反映する<br>
     * <br>
     * G0802_外線発信設定画面(設定)で設定ボタン押下時に実行する。<br>
     * 
     * @param loginId　ログインID
     * @param dbConnection DBコネクション
     * @param nNumberInfoId　「N番情報ID.N番情報ID」
     * @param outsideCallSendingInfoId　発信情報を設定した外線番号の「外線発信情報.外線発信情報ID」
     * @throws Exception 例外
     */
    public void setUseOutsideForExten(String loginId, Connection dbConnection, long nNumberInfoId, 
            long outsideCallSendingInfoId) 
                    throws Exception {
        try{
            log.debug("method start :setUseOutsideForExten");   

            // 更新対象の内線サーバ設定ファイルバックアップ情報をロック(同じN番を全ロック)
            executeLock(dbConnection, 
                    nNumberInfoId);

            // 変数を宣言
            NNumberInfo nNumberInfo = getNNumberInfo(dbConnection, nNumberInfoId);
            List<String> localFileList = new ArrayList<String>();
            List<String> dstFileList = new ArrayList<String>();
            List<String> dstFileBackUpList = new ArrayList<String>();
            List<String> dstFileTmpList = new ArrayList<String>();
            List<ExtensionNumberInfo> extensionNumberInfoList = new ArrayList<ExtensionNumberInfo>();
            List<OutsideCallInfo> outsideCallInfoList = new ArrayList<OutsideCallInfo>();
            // #997 START 
            List<OutsideCallInfo> outsideCallInfoListForDialIn = new ArrayList<OutsideCallInfo>();
            // #997 END
            
            // #1290 START (extensions_内線番号_in.confの生成)
            // 変数を宣言(extensions_内線番号_in.conf用)
            List<ExtensionNumberInfo> extensionNumberInfoListExtIn = new ArrayList<ExtensionNumberInfo>();
            List<AbsenceBehaviorInfo> absenceBehaviorInfoListExtIn = new ArrayList<AbsenceBehaviorInfo>();
            List<OutsideCallInfo> outsideCallInfoListExtIn = new ArrayList<OutsideCallInfo>();
            List<List<String>> childExtensionNumberInfoListExtIn = new ArrayList<List<String>>();
            List<Integer> groupCallTypeListExtIn = new ArrayList<Integer>();
            // #1290 END
            
            // #748 START
            try{
                // #748 END

                // 外線発信情報IDから必要なデータをセット
                // #997 START
                setExtensionsExtensionNumberOutConfDataByOutsideCallSendingInfoId(
                        dbConnection,
                        outsideCallSendingInfoId,
                        extensionNumberInfoList,
                        outsideCallInfoList,
                        outsideCallInfoListForDialIn);
                // #997 END
                
                // #834 START
                //(VoIP-GWの考慮漏れ。extensions_内線番号_out.confファイル生成に必要な同一拠点番号の他のVoIP-GWのデータを取得する)
                //本ルートは画面処理側で考慮されている。
                //外線発信情報に設定される内線番号情報は、VoIP-GWの場合、複数台利用が1となるようになっている。
                //なお、setExtensionsExtensionNumberOutConfDataByOutsideCallSendingInfoIdに対応するメソッドは実装しないため、
                //サービス仕様変更時に実装すること
                // #834 END
                
                // #1290 START (extensions_内線番号_in.confの生成)
                // %SIP_PEER_OUTBAND_KIND%のため、外線発信設定を変更したら、extensions_内線番号_in.confを生成する
                setExtensionsExtensionNumberInConfDataByOutsideCallSendingInfoId(
                        dbConnection,
                        outsideCallSendingInfoId,
                        extensionNumberInfoListExtIn,
                        absenceBehaviorInfoListExtIn, 
                        outsideCallInfoListExtIn,
                        childExtensionNumberInfoListExtIn,
                        groupCallTypeListExtIn);
                
                // (#834の対処はsetExtensionsExtensionNumberInConfDataByOutsideCallSendingInfoIdの呼び出し先で対処)
                // #1290 END
                
                
                // 転送先ディレクトリ情報取得メソッド呼出
                ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberOutConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        extensionNumberInfoList);
                
                // #1290 START (extensions_内線番号_in.confの生成)
                // %SIP_PEER_OUTBAND_KIND%のため、外線発信設定を変更したら、extensions_内線番号_in.confを生成する
                ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberInConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        extensionNumberInfoListExtIn );
                // #1290 END

                
                // ファイル生成メソッド呼出
                // #997 START (引数の追加)
                ConfigCreatorFile.getInstance().createExtensionsExtensionNumberOutConf(
                        extensionNumberInfoList,
                        outsideCallInfoList,
                        outsideCallInfoListForDialIn,
                        nNumberInfo);        
                // #997 END (引数の追加)
                
                // #1290 START (extensions_内線番号_in.confの生成)
                // %SIP_PEER_OUTBAND_KIND%のため、外線発信設定を変更したら、extensions_内線番号_in.confを生成する
                ConfigCreatorFile.getInstance().createExtensionsExtensionNumberInConf(
                        extensionNumberInfoListExtIn,
                        absenceBehaviorInfoListExtIn,
                        outsideCallInfoListExtIn,
                        groupCallTypeListExtIn,
                        childExtensionNumberInfoListExtIn,
                        nNumberInfo );
                // #1290 END

                
                // DBに内線サーバ設定ファイルバックアップ情報を登録
                executeBackUp(loginId,
                        dbConnection, 
                        nNumberInfoId,
                        localFileList,
                        dstFileList,
                        null);  

                // #748 START
                // ファイル生成 成功時のログ出力
                log.info(Util.message(Const.ERROR_CODE.I020801, String.format(Const.MESSAGE_CODE.I020801,null,null,null,null,loginId)));
                // #748 END

                // #748 START
            }catch(Exception e){
                // ファイル生成 失敗時のログ出力
                log.error(Util.message(Const.ERROR_CODE.E020802, String.format(Const.MESSAGE_CODE.E020802,null,null,null,null,loginId)));
                throw e;
            }
            //     #748 END
        
            
            // ファイルアップロード処理実行
            // (ファイルバックアップ、SIPリロード含む)
            executeFileUpload(loginId,
                    dbConnection, 
                    nNumberInfoId,
                    localFileList,
                    dstFileList,
                    dstFileBackUpList,
                    dstFileTmpList,
                    null,null); 

        }catch (Exception e){
            log.error("method error :setUseOutsideForExten = " + e.toString());

            throw e;
        }
        
        log.debug("method end :setUseOutsideForExten");
    }   

    /**
     * G0901_内線設定画面(一覧表示)の結果をAsteriskへ反映する<br>
     * <br>
     * G0901_内線設定画面(一覧表示)でCSV取込ボタン押下時に実行する。<br>
     * 
     * @param loginId　ログインID
     * @param dbConnection DBコネクション
     * @param nNumberInfoId　「N番情報ID.N番情報ID」
     * @param extensionNumberInfoIdArray　 CSV取込で設定を変更した内線番号の「内線番号情報.内線番号情報ID」のリスト
     * @throws Exception 例外
     */
    public void importExtension(String loginId, Connection dbConnection, long nNumberInfoId, 
            ArrayList<Long> extensionNumberInfoIdArray) 
                    throws Exception {
        try{
            log.debug("method start :importExtension");     

            // 更新対象の内線サーバ設定ファイルバックアップ情報をロック(同じN番を全ロック)
            executeLock(dbConnection, 
                    nNumberInfoId);

            // #834 START
            // 以下のファイル生成用のデータ取得を行った内線番号を管理
            // sip_内線番号.conf,extensions_内線番号_in.conf,extensions_内線番号_out.conf
            // MAP<Key,Value>=<内線番号,ListのINDEX>
            HashMap<String,Integer> mapExtensionNumber = null;
            String tmpExtensionNumber = null;
            int indexOfList = 0;
            // #834 END

            // 変数を宣言(共通)        
            NNumberInfo nNumberInfo = getNNumberInfo(dbConnection, nNumberInfoId);
            List<String> localFileList = new ArrayList<String>();
            List<String> dstFileList = new ArrayList<String>();
            List<String> dstFileBackUpList = new ArrayList<String>();
            List<String> dstFileTmpList = new ArrayList<String>();

            // 変数を宣言(sip_内線番号.conf用)
            List<ExtensionNumberInfo> extensionNumberInfoListSip = new ArrayList<ExtensionNumberInfo>();
            List<String> pickUpGroupListSip = new ArrayList<String>();

            // 変数を宣言(extensions_内線番号_in.conf用)
            List<ExtensionNumberInfo> extensionNumberInfoListExtIn = new ArrayList<ExtensionNumberInfo>();
            List<AbsenceBehaviorInfo> absenceBehaviorInfoListExtIn = new ArrayList<AbsenceBehaviorInfo>();
            List<OutsideCallInfo> outsideCallInfoListExtIn = new ArrayList<OutsideCallInfo>();
            List<List<String>> childExtensionNumberInfoListExtIn = new ArrayList<List<String>>();
            List<Integer> groupCallTypeListExtIn = new ArrayList<Integer>();

            // 変数を宣言(extensions_内線番号_out.conf用)
            List<ExtensionNumberInfo> extensionNumberInfoListExtOut = new ArrayList<ExtensionNumberInfo>();
            List<OutsideCallInfo> outsideCallInfoListExtOut = new ArrayList<OutsideCallInfo>();
            // #997 START 
            List<OutsideCallInfo> outsideCallInfoListExtExtNumOutForDialIn = new ArrayList<OutsideCallInfo>();
            // #997 END

            // #748 START
            try{
                // #748 END

                // 内線番号情報IDのリストから必要なデータをセット       
                Iterator<Long> itrExtensionNumberInfoId = extensionNumberInfoIdArray.iterator();
                while(itrExtensionNumberInfoId.hasNext()){     
                    // 内線番号情報IDのリストから内線番号情報IDを取得
                    long extensionNumberInfoId = itrExtensionNumberInfoId.next();           

                    // #834 START
                    //HashMapの初期化
                    mapExtensionNumber = new HashMap<String,Integer>();
                    tmpExtensionNumber = null;
                    indexOfList = extensionNumberInfoListSip.size();
                    // #834 END

                    // 内線番号情報IDからsip_内線番号.confファイル生成に必要なデータをセット
                    setSipExtensionNumberConfDataByExtensionNumberInfoId(
                            dbConnection,
                            extensionNumberInfoId,
                            extensionNumberInfoListSip,
                            pickUpGroupListSip);
                    // #834 START
                    //(VoIP-GWの考慮漏れ。sip_内線番号.confファイル生成に必要な同一拠点番号の他のVoIP-GWのデータを取得する)
                    // 既に同じ内線番号で実行されていたら対象外。
                    tmpExtensionNumber = extensionNumberInfoListSip.get(indexOfList).getExtensionNumber();
                    if( !mapExtensionNumber.containsKey(tmpExtensionNumber) ){
                        //存在しない
                        mapExtensionNumber.put(tmpExtensionNumber,indexOfList );

                        setSipExtensionNumberConfDataByExtensionNumberInfoIdForOtherVoipGw(
                                dbConnection,
                                extensionNumberInfoId,
                                extensionNumberInfoListSip,
                                pickUpGroupListSip);
                    }
                    // #834 END

                    // #834 START
                    //HashMapの初期化
                    mapExtensionNumber = new HashMap<String,Integer>();
                    tmpExtensionNumber = null;
                    indexOfList = extensionNumberInfoListExtIn.size();
                    // #834 END

                    // 内線番号情報IDからextensions_内線番号_in.confファイル生成に必要なデータをセット
                    setExtensionsExtensionNumberInConfDataByExtensionNumberInfoId(
                            dbConnection,
                            extensionNumberInfoId,
                            extensionNumberInfoListExtIn,
                            absenceBehaviorInfoListExtIn,
                            outsideCallInfoListExtIn,
                            childExtensionNumberInfoListExtIn,
                            groupCallTypeListExtIn);
                    // #834 START
                    //(VoIP-GWの考慮漏れ。extensions_内線番号_in.confファイル生成に必要な同一拠点番号の他のVoIP-GWのデータを取得する)
                    // 既に同じ内線番号で実行されていたら対象外。
                    tmpExtensionNumber = extensionNumberInfoListExtIn.get(indexOfList).getExtensionNumber();
                    if( !mapExtensionNumber.containsKey(tmpExtensionNumber) ){
                        //存在しない
                        mapExtensionNumber.put(tmpExtensionNumber,indexOfList );

                        setExtensionsExtensionNumberInConfDataByExtensionNumberInfoIdForOtherVoipGw(
                                dbConnection,
                                extensionNumberInfoId,
                                extensionNumberInfoListExtIn,
                                absenceBehaviorInfoListExtIn,
                                outsideCallInfoListExtIn,
                                childExtensionNumberInfoListExtIn,
                                groupCallTypeListExtIn);
                    }
                    // #834 END

                    // #834 START
                    //HashMapの初期化
                    mapExtensionNumber = new HashMap<String,Integer>();
                    tmpExtensionNumber = null;
                    indexOfList = extensionNumberInfoListExtOut.size();
                    // #834 END

                    // 内線番号情報IDからextensions_内線番号_out.confファイル生成に必要なデータをセット
                    // #997 START (引数の追加)
                    setExtensionsExtensionNumberOutConfDataByExtensionNumberInfoId(
                            dbConnection,
                            extensionNumberInfoId,
                            extensionNumberInfoListExtOut,
                            outsideCallInfoListExtOut,
                            outsideCallInfoListExtExtNumOutForDialIn);
                 // #997 END
                    
                    // #834 START
                    //(VoIP-GWの考慮漏れ。extensions_内線番号_out.confファイル生成に必要な同一拠点番号の他のVoIP-GWのデータを取得する)
                    // 既に同じ内線番号で実行されていたら対象外。
                    tmpExtensionNumber = extensionNumberInfoListExtOut.get(indexOfList).getExtensionNumber();
                    if( !mapExtensionNumber.containsKey(tmpExtensionNumber) ){
                        //存在しない
                        mapExtensionNumber.put(tmpExtensionNumber,indexOfList );

                        // #997 START (引数の追加)
                        setExtensionsExtensionNumberOutConfDataByExtensionNumberInfoIdForOtherVoipGw(
                                dbConnection,
                                extensionNumberInfoId,
                                extensionNumberInfoListExtOut,
                                outsideCallInfoListExtOut,
                                outsideCallInfoListExtExtNumOutForDialIn);
                        // #997 END
                    }
                    // #834 END


                }//while

                // 転送先ディレクトリ情報取得メソッド呼出
                // sip_内線番号
                ConfigCreatorFile.getInstance().setPathListOfSipExtensionNumberConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        extensionNumberInfoListSip);
                // extensions_内線番号_in
                ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberInConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        extensionNumberInfoListExtIn);
                // extensions_内線番号_out
                ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberOutConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        extensionNumberInfoListExtOut);

                // ファイル生成メソッド呼出
                // sip_内線番号
                ConfigCreatorFile.getInstance().createSipExtensionNumberConf(extensionNumberInfoListSip,
                        pickUpGroupListSip,
                        nNumberInfo);

                // extensions_内線番号_in
                ConfigCreatorFile.getInstance().createExtensionsExtensionNumberInConf(extensionNumberInfoListExtIn,
                        absenceBehaviorInfoListExtIn,
                        outsideCallInfoListExtIn,
                        groupCallTypeListExtIn,
                        childExtensionNumberInfoListExtIn,
                        nNumberInfo);       

                // extensions_内線番号_out
                // #997 START (引数の追加)
                ConfigCreatorFile.getInstance().createExtensionsExtensionNumberOutConf(extensionNumberInfoListExtOut,
                        outsideCallInfoListExtOut,
                        outsideCallInfoListExtExtNumOutForDialIn,
                        nNumberInfo);    
                // #997 END (引数の追加)

                // DBに内線サーバ設定ファイルバックアップ情報を登録
                executeBackUp(loginId,
                        dbConnection, 
                        nNumberInfoId,
                        localFileList,
                        dstFileList,
                        null);  

                // #748 START
                // ファイル生成 成功時のログ出力
                log.info(Util.message(Const.ERROR_CODE.I020801, String.format(Const.MESSAGE_CODE.I020801,null,null,null,null,loginId)));
                // #748 END

                // #748 START
            }catch(Exception e){
                // ファイル生成 失敗時のログ出力
                log.error(Util.message(Const.ERROR_CODE.E020802, String.format(Const.MESSAGE_CODE.E020802,null,null,null,null,loginId)));
                throw e;
            }
            //     #748 END
            
            // ファイルアップロード処理実行
            // (ファイルバックアップ、SIPリロード含む)
            executeFileUpload(loginId,
                    dbConnection, 
                    nNumberInfoId,
                    localFileList,
                    dstFileList,
                    dstFileBackUpList,
                    dstFileTmpList,
                    null,null); 

        }catch (Exception e){
            log.error("method error :importExtension = " + e.toString());

            throw e;
        }
        
        log.debug("method end :importExtension");
    }

    /**
     * G0902_内線設定画面(変更)の結果をAsteriskへ反映する<br>
     * <br>
     * 内線設定画面(変更)で変更ボタン押下時に実行する。<br>
     * 
     * @param loginId　ログインID
     * @param dbConnection DBコネクション
     * @param nNumberInfoId　「N番情報ID.N番情報ID」
     * @param extensionNumberInfoId 設定を変更した内線番号の「内線番号情報.内線番号情報ID」
     * @param oldExtensionNumber　変更前の内線番号情報の「内線番号情報.内線番号」
     * @param newExtensionNumber 変更後の内線番号情報の「内線番号情報.内線番号」
     * @throws Exception 例外
     */
    public void modExtension(String loginId, Connection dbConnection, long nNumberInfoId, 
            long extensionNumberInfoId,
            String oldExtensionNumber,
            String newExtensionNumber) 
                    throws Exception {
        try{
            log.debug("method start :modExtension");

            // 更新対象の内線サーバ設定ファイルバックアップ情報をロック(同じN番を全ロック)
            executeLock(dbConnection, 
                    nNumberInfoId);

            // 変数を宣言(共通)        
            NNumberInfo nNumberInfo = getNNumberInfo(dbConnection, nNumberInfoId);
            List<String> localFileList = new ArrayList<String>();
            List<String> dstFileList = new ArrayList<String>();
            List<String> dstFileBackUpList = new ArrayList<String>();
            List<String> dstFileTmpList = new ArrayList<String>();
            List<String> delFileList = new ArrayList<String>();
            List<String> delFileBackUpList = new ArrayList<String>();

            // 変数を宣言(sip_内線番号.conf用)
            List<ExtensionNumberInfo> extensionNumberInfoListSip = new ArrayList<ExtensionNumberInfo>();
            List<String> pickUpGroupListSip = new ArrayList<String>();

            // 変数を宣言(extensions_内線番号_in.conf用)
            List<ExtensionNumberInfo> extensionNumberInfoListExtIn = new ArrayList<ExtensionNumberInfo>();
            List<AbsenceBehaviorInfo> absenceBehaviorInfoListExtIn = new ArrayList<AbsenceBehaviorInfo>();
            List<OutsideCallInfo> outsideCallInfoListExtIn = new ArrayList<OutsideCallInfo>();
            List<List<String>> childExtensionNumberInfoListExtIn = new ArrayList<List<String>>();
            List<Integer> groupCallTypeListExtIn = new ArrayList<Integer>();

            // 変数を宣言(extensions_内線番号_out.conf用)
            List<ExtensionNumberInfo> extensionNumberInfoListExtOut = new ArrayList<ExtensionNumberInfo>();
            List<OutsideCallInfo> outsideCallInfoListExtOut = new ArrayList<OutsideCallInfo>();
            // #997 START 
            List<OutsideCallInfo> outsideCallInfoListExtExtNumOutForDialIn = new ArrayList<OutsideCallInfo>();
            // #997 END

            // 変数を宣言(sip_user.conf用)
            List<ExtensionNumberInfo> extensionNumberInfoListSipUser = new ArrayList<ExtensionNumberInfo>();

            // 変数を宣言(extensions_user.conf用)
            List<ExtensionNumberInfo> extensionNumberInfoListExtUser = new ArrayList<ExtensionNumberInfo>();

            // 変数を宣言(extensions_外線番号_in用)
            List<OutsideCallInfo> outsideCallInfoListExt = new ArrayList<OutsideCallInfo>();
            List<OutsideCallIncomingInfo> outsideCallIncomingInfoListExt = new ArrayList<OutsideCallIncomingInfo>();
            List<ExtensionNumberInfo> extensionNumberInfoListExt = new ArrayList<ExtensionNumberInfo>();

            // 変数を宣言(voicemail.conf用)
            List<ExtensionNumberInfo> extensionNumberInfoListVoice = new ArrayList<ExtensionNumberInfo>();
            List<AbsenceBehaviorInfo> absenceBehaviorInfoListVoice = new ArrayList<AbsenceBehaviorInfo>();
            
            // #768 START  (extensions_group.conf)
            // 変数を宣言(extensions_group.conf用)
            List<IncomingGroupInfo> incomingGroupInfoListOfCallPickUp = new ArrayList<IncomingGroupInfo>();
            List<List<String>> childExtensionNumberListExtGroup = new ArrayList<List<String>>();
            // #768 END    (extensions_group.conf)


            // #748 START
            try{
                // #748 END

                // 内線番号情報IDからsip_内線番号.confファイル生成に必要なデータをセット
                setSipExtensionNumberConfDataByExtensionNumberInfoId(
                        dbConnection,
                        extensionNumberInfoId,
                        extensionNumberInfoListSip,
                        pickUpGroupListSip);
                // #834 START
                //(VoIP-GWの考慮漏れ。sip_内線番号.confファイル生成に必要な同一拠点番号の他のVoIP-GWのデータを取得する)
                setSipExtensionNumberConfDataByExtensionNumberInfoIdForOtherVoipGw(
                        dbConnection,
                        extensionNumberInfoId,
                        extensionNumberInfoListSip,
                        pickUpGroupListSip);
                // #834 END

                // 内線番号情報IDからextensions_内線番号_in.confファイル生成に必要なデータをセット
                setExtensionsExtensionNumberInConfDataByExtensionNumberInfoId(
                        dbConnection,
                        extensionNumberInfoId,
                        extensionNumberInfoListExtIn,
                        absenceBehaviorInfoListExtIn,
                        outsideCallInfoListExtIn,
                        childExtensionNumberInfoListExtIn,
                        groupCallTypeListExtIn);
                // #834 START
                //(VoIP-GWの考慮漏れ。extensions_内線番号_in.confファイル生成に必要な同一拠点番号の他のVoIP-GWのデータを取得する)
                setExtensionsExtensionNumberInConfDataByExtensionNumberInfoIdForOtherVoipGw(
                        dbConnection,
                        extensionNumberInfoId,
                        extensionNumberInfoListExtIn,
                        absenceBehaviorInfoListExtIn,
                        outsideCallInfoListExtIn,
                        childExtensionNumberInfoListExtIn,
                        groupCallTypeListExtIn);
                // #834 END

                // #997 START (引数の追加)
                // 内線番号情報IDからextensions_内線番号_out.confファイル生成に必要なデータをセット
                setExtensionsExtensionNumberOutConfDataByExtensionNumberInfoId(
                        dbConnection,
                        extensionNumberInfoId,
                        extensionNumberInfoListExtOut,
                        outsideCallInfoListExtOut,
                        outsideCallInfoListExtExtNumOutForDialIn);                
                // #997 END
                
                // #834 START
                //(VoIP-GWの考慮漏れ。extensions_内線番号_out.confファイル生成に必要な同一拠点番号の他のVoIP-GWのデータを取得する)
                // #997 START (引数の追加)
                setExtensionsExtensionNumberOutConfDataByExtensionNumberInfoIdForOtherVoipGw(
                        dbConnection,
                        extensionNumberInfoId,
                        extensionNumberInfoListExtOut,
                        outsideCallInfoListExtOut,
                        outsideCallInfoListExtExtNumOutForDialIn);                
                // #997 END
                // #834 END

                // 内線番号変更時
                if(! oldExtensionNumber.equals(newExtensionNumber)){
                    log.debug("extension_number is changed !! ");
                    
                    // N番情報IDから、sip_user.confの作成に必要なデータをセット
                    setSipUserConfDataByNNumberInfoId(
                            dbConnection,
                            nNumberInfoId,
                            extensionNumberInfoListSipUser);

                    // N番情報IDから、extensions_user.confの作成に必要なデータをセット
                    setExtensionUserConfDataByNNumberInfoId(
                            dbConnection,
                            nNumberInfoId,
                            extensionNumberInfoListExtUser);

                    // 内線番号情報IDから、extensions_外線番号_in.confの作成に必要なデータをセット
                    setExtensionsOutGoingNumberInConfDataByExtensionNumberInfoId(
                            dbConnection,
                            extensionNumberInfoId,
                            outsideCallInfoListExt,
                            outsideCallIncomingInfoListExt,
                            extensionNumberInfoListExt);

                    // N番情報IDから、voicemail.confの作成に必要なデータをセット
                    setVoiceMailConfDataByNNumberInfoId(
                            dbConnection,
                            nNumberInfoId,
                            extensionNumberInfoListVoice,
                            absenceBehaviorInfoListVoice);
                    
                    // #768 START  (extensions_group.conf)
                    // N番情報IDからextensions_group.confファイル生成に必要なデータをセット
                    setExtensionsGroupConfDataByNNumberInfoId(
                            dbConnection,
                            nNumberInfoId,
                            incomingGroupInfoListOfCallPickUp,
                            childExtensionNumberListExtGroup);
                    // #768 END    (extensions_group.conf)
                    
                    // #1287 START
                    // 内線番号を変更した端末が、順次着信、一斉着信の子番号の場合、
                    // 親番号のextensions_内線番号_inを生成する
                    setExtensionsExtensionNumberInConfDataByExtensionNumberInfoIdForIfChildNumber(
                            dbConnection,
                            extensionNumberInfoId,
                            extensionNumberInfoListExtIn,
                            absenceBehaviorInfoListExtIn,
                            outsideCallInfoListExtIn,
                            childExtensionNumberInfoListExtIn,
                            groupCallTypeListExtIn);
                    // #1287 END
                    
                }

                // 転送先ディレクトリ情報取得メソッド呼出
                // sip_内線番号
                ConfigCreatorFile.getInstance().setPathListOfSipExtensionNumberConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        extensionNumberInfoListSip);
                // extensions_内線番号_in
                ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberInConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        extensionNumberInfoListExtIn);
                // extensions_内線番号_out
                ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberOutConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        extensionNumberInfoListExtOut);

                // 内線番号変更時
                if(! oldExtensionNumber.equals(newExtensionNumber)){
                    // 削除用の内線番号
                    List<String> extensionNumberList = new ArrayList<String>();
                    extensionNumberList.add(oldExtensionNumber);

                    // sip_user
                    ConfigCreatorFile.getInstance().setPathListOfSipUserConf(
                            localFileList,
                            dstFileList,
                            dstFileBackUpList,
                            dstFileTmpList,
                            nNumberInfo);
                    // extensions_user
                    ConfigCreatorFile.getInstance().setPathListOfExtensionsUserConf(
                            localFileList,
                            dstFileList,
                            dstFileBackUpList,
                            dstFileTmpList,
                            nNumberInfo);
                    // extensions_外線番号_in
                    ConfigCreatorFile.getInstance().setPathListOfExtensionsOutgoingNumberInConf(
                            localFileList,
                            dstFileList,
                            dstFileBackUpList,
                            dstFileTmpList,
                            nNumberInfo,
                            outsideCallInfoListExt);
                    // voicemail.conf
                    ConfigCreatorFile.getInstance().setPathListOfVoicemailConf(
                            localFileList,
                            dstFileList,
                            dstFileBackUpList,
                            dstFileTmpList,
                            nNumberInfo);
                    // #768 START  (extensions_group.conf)
                    ConfigCreatorFile.getInstance().setPathListOfExtensionsGroupConf(
                            localFileList,
                            dstFileList,
                            dstFileBackUpList,
                            dstFileTmpList,
                            nNumberInfo);
                    // #768 END    (extensions_group.conf)

                    // sip_内線番号(削除用)
                    ConfigCreatorFile.getInstance().setPathListOfSipExtensionNumberConfByExtensionNumber(
                            new ArrayList<String>(),
                            delFileList,
                            delFileBackUpList,
                            new ArrayList<String>(),
                            nNumberInfo,
                            extensionNumberList);
                    // extensions_内線番号_in(削除用)
                    ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberInConfByExtensionNumber(
                            new ArrayList<String>(),
                            delFileList,
                            delFileBackUpList,
                            new ArrayList<String>(),
                            nNumberInfo,
                            extensionNumberList);
                    // extensions_内線番号_out(削除用)
                    ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberOutConfByExtensionNumber(
                            new ArrayList<String>(),
                            delFileList,
                            delFileBackUpList,
                            new ArrayList<String>(),
                            nNumberInfo,
                            extensionNumberList);
                    
                }

                // ファイル生成メソッド呼出
                // sip_内線番号
                ConfigCreatorFile.getInstance().createSipExtensionNumberConf(extensionNumberInfoListSip,
                        pickUpGroupListSip,
                        nNumberInfo);

                // extensions_内線番号_in
                ConfigCreatorFile.getInstance().createExtensionsExtensionNumberInConf(extensionNumberInfoListExtIn,
                        absenceBehaviorInfoListExtIn,
                        outsideCallInfoListExtIn,
                        groupCallTypeListExtIn,
                        childExtensionNumberInfoListExtIn,
                        nNumberInfo);       

                // extensions_内線番号_out
                // #997 START (引数の追加)
                ConfigCreatorFile.getInstance().createExtensionsExtensionNumberOutConf(extensionNumberInfoListExtOut,
                        outsideCallInfoListExtOut,
                        outsideCallInfoListExtExtNumOutForDialIn,
                        nNumberInfo);    
                // #997 END (引数の追加)

                // 内線番号変更時
                if(! oldExtensionNumber.equals(newExtensionNumber)){
                    // sip_user
                    ConfigCreatorFile.getInstance().createSipUserConf(extensionNumberInfoListSipUser,
                            nNumberInfo);

                    // extensions_user
                    ConfigCreatorFile.getInstance().createExtensionsUserConf(extensionNumberInfoListExtUser,
                            nNumberInfo);

                    // extensions_外線番号_in
                    ConfigCreatorFile.getInstance().createExtensionsOutgoingNumberInConf(outsideCallInfoListExt,
                            outsideCallIncomingInfoListExt,
                            extensionNumberInfoListExt,
                            nNumberInfo);

                    // voicemail.conf
                    ConfigCreatorFile.getInstance().createVoicemailConf(extensionNumberInfoListVoice,
                            absenceBehaviorInfoListVoice,
                            nNumberInfo);
                    
                    // #768 START  (extensions_group.conf)
                    // extensions_group.conf
                    ConfigCreatorFile.getInstance().createExtensionsGroupConf(
                            incomingGroupInfoListOfCallPickUp, 
                            childExtensionNumberListExtGroup, 
                            nNumberInfo );
                    // #768 END    (extensions_group.conf)

                }

                // DBに内線サーバ設定ファイルバックアップ情報を登録
                executeBackUp(loginId,
                        dbConnection, 
                        nNumberInfoId,
                        localFileList,
                        dstFileList,
                        delFileList);  

                // #748 START
                // ファイル生成 成功時のログ出力
                log.info(Util.message(Const.ERROR_CODE.I020801, String.format(Const.MESSAGE_CODE.I020801,null,null,null,null,loginId)));
                // #748 END

                // #748 START
            }catch(Exception e){
                // ファイル生成 失敗時のログ出力
                log.error(Util.message(Const.ERROR_CODE.E020802, String.format(Const.MESSAGE_CODE.E020802,null,null,null,null,loginId)));
                throw e;
            }
            //     #748 END
            
            // ファイルアップロード処理実行
            // (ファイルバックアップ、SIPリロード含む)
            executeFileUpload(loginId,
                    dbConnection, 
                    nNumberInfoId,
                    localFileList,
                    dstFileList,
                    dstFileBackUpList,
                    dstFileTmpList,
                    delFileList,
                    delFileBackUpList); 

        }catch (Exception e){
            log.error("method error :modExtension = " + e.toString());

            throw e;
        }
        
        log.debug("method end :modExtension");
    }

    /**
     * G1401_不在時動作設定画面(設定)の結果をAsteriskへ反映する<br>
     * <br>
     * G1401_不在時動作設定画面(設定)で設定ボタン押下時に実行する。<br>
     * 
     * @param loginId　ログインID
     * @param dbConnection DBコネクション
     * @param nNumberInfoId　「N番情報ID.N番情報ID」
     * @param extensionNumberInfoId　設定を変更した内線番号の「内線番号情報.内線番号情報ID」
     * @throws Exception 例外
     */
    public void modAbsenceBehavior(String loginId, Connection dbConnection, long nNumberInfoId, 
            long extensionNumberInfoId) 
                    throws Exception {
        try{
            log.debug("method start :modAbsenceBehavior");      

            // 更新対象の内線サーバ設定ファイルバックアップ情報をロック(同じN番を全ロック)
            executeLock(dbConnection, 
                    nNumberInfoId);

            // 変数を宣言
            NNumberInfo nNumberInfo = getNNumberInfo(dbConnection, nNumberInfoId);
            List<String> localFileList = new ArrayList<String>();
            List<String> dstFileList = new ArrayList<String>();
            List<String> dstFileBackUpList = new ArrayList<String>();
            List<String> dstFileTmpList = new ArrayList<String>();
            List<ExtensionNumberInfo> extensionNumberInfoList = new ArrayList<ExtensionNumberInfo>();
            List<AbsenceBehaviorInfo> absenceBehaviorInfoList = new ArrayList<AbsenceBehaviorInfo>();
            List<OutsideCallInfo> outsideCallInfoList = new ArrayList<OutsideCallInfo>();
            List<List<String>> childExtensionNumberInfoList = new ArrayList<List<String>>();
            List<Integer> groupCallTypeList = new ArrayList<Integer>();
            
            // #748 START
            try{
                // #748 END

                // 内線番号情報IDからextensions_内線番号_in.confファイル生成に必要なデータをセット
                setExtensionsExtensionNumberInConfDataByExtensionNumberInfoId(
                        dbConnection,
                        extensionNumberInfoId,
                        extensionNumberInfoList,
                        absenceBehaviorInfoList,
                        outsideCallInfoList,
                        childExtensionNumberInfoList,
                        groupCallTypeList);
                // #834 START
                //(VoIP-GWの考慮漏れ。extensions_内線番号_in.confファイル生成に必要な同一拠点番号の他のVoIP-GWのデータを取得する)
                setExtensionsExtensionNumberInConfDataByExtensionNumberInfoIdForOtherVoipGw(
                        dbConnection,
                        extensionNumberInfoId,
                        extensionNumberInfoList,
                        absenceBehaviorInfoList,
                        outsideCallInfoList,
                        childExtensionNumberInfoList,
                        groupCallTypeList);
                // #834 END

                // 転送先ディレクトリ情報取得メソッド呼出
                ConfigCreatorFile.getInstance().setPathListOfExtensionsExtensionNumberInConf(
                        localFileList,
                        dstFileList,
                        dstFileBackUpList,
                        dstFileTmpList,
                        nNumberInfo,
                        extensionNumberInfoList);

                // ファイル生成メソッド呼出
                ConfigCreatorFile.getInstance().createExtensionsExtensionNumberInConf(extensionNumberInfoList,
                        absenceBehaviorInfoList,
                        outsideCallInfoList,
                        groupCallTypeList,
                        childExtensionNumberInfoList,
                        nNumberInfo);      

                // DBに内線サーバ設定ファイルバックアップ情報を登録
                executeBackUp(loginId,
                        dbConnection, 
                        nNumberInfoId,
                        localFileList,
                        dstFileList,
                        null);  

                // #748 START
                // ファイル生成 成功時のログ出力
                log.info(Util.message(Const.ERROR_CODE.I020801, String.format(Const.MESSAGE_CODE.I020801,null,null,null,null,loginId)));
                // #748 END

                // #748 START
            }catch(Exception e){
                // ファイル生成 失敗時のログ出力
                log.error(Util.message(Const.ERROR_CODE.E020802, String.format(Const.MESSAGE_CODE.E020802,null,null,null,null,loginId)));
                throw e;
            }
            //     #748 END
            
            // ファイルアップロード処理実行
            // (ファイルバックアップ、SIPリロード含む)
            executeFileUpload(loginId,
                    dbConnection, 
                    nNumberInfoId,
                    localFileList,
                    dstFileList,
                    dstFileBackUpList,
                    dstFileTmpList,
                    null,null); 

        }catch (Exception e){
            log.error("method error :modAbsenceBehavior = " + e.toString());

            throw e;
        }
        
        log.debug("method end :modAbsenceBehavior");
    }



    /**
     * N番情報IDとアップロードファイルのリストから内線サーバ設定ファイルバックアップ情報のロック処理を実施。
     * @throws Exception 
     * 
     */

    private void executeLock(Connection dbConnection, 
            long nNumberInfoId) throws Exception{
        log.trace("method start :executeLock");

        // DBロック
        Result<Boolean> rs = 
                ConfigCreatorDAO.getInstance().lockPbxFileBackupInfo(nNumberInfoId, dbConnection);
        if(rs.getRetCode() != Const.ReturnCode.OK){
            String msg = "N番情報IDに紐づく内線サーバ設定ファイルバックアップ情報のロック取得に失敗しました。"
                    + "  N番情報ID=" + nNumberInfoId ;
            log.error(msg);
            throw new Exception(msg);
        }

        log.trace("method end :executeLock");
    }

    /**
     * N番情報IDとアップロードファイルのリストから内線サーバ設定ファイルバックアップ情報の更新を実施。
     * @throws Exception 
     * 
     */
    private void executeBackUp(String loginId,
            Connection dbConnection, 
            long nNumberInfoId,
            List<String> localFileList,
            List<String> dstFileList,
            List<String> delFileList
            ) throws Exception{
        log.trace("method start :executeBackUp");

        // 変数宣言
        VmInfo vmInfo = new VmInfo();
        long accountInfoId = 0;

        // N番IDからVM情報を取得
        Result<VmInfo> rsVmInfo = 
                ConfigCreatorDAO.getInstance().getVmInfoByNNumberInfoId(nNumberInfoId, dbConnection);
        if(rsVmInfo.getRetCode() == Const.ReturnCode.OK){
            vmInfo = rsVmInfo.getData();
        }else{
            String msg = "N番情報IDに紐づくVM情報の取得に失敗しました。"
                    + "  N番情報ID=" + nNumberInfoId ;
            log.error(msg);
            throw new Exception(msg);
        }

        // ログインIDからアカウント情報IDを取得
        Result<Long> rsAccountInfoId = 
                ConfigCreatorDAO.getInstance().getAccountInfoIdByLoginId(loginId, dbConnection);
        if(rsAccountInfoId.getRetCode() == Const.ReturnCode.OK){
            accountInfoId = rsAccountInfoId.getData();
        }else{
            String msg = "ログインIDに紐づくアカウント情報の取得に失敗しました。"
                    + "  ログインID=" + loginId ;
            log.error(msg);
            throw new Exception(msg);
        }

        // バックアップファイルのDB更新処理
        if(localFileList != null){
            Iterator<String> itrlocalFile = localFileList.iterator();
            Iterator<String> itrdstFile = dstFileList.iterator();
            while(itrlocalFile.hasNext()){   
                // PbxFileBackupInfoのインスタンス生成
                PbxFileBackupInfo pbxFileBackupInfo = new PbxFileBackupInfo();

                // 設定値を取得
                pbxFileBackupInfo.setFkVmInfoId(vmInfo.getVmInfoId());
                pbxFileBackupInfo.setPbxFilePath(itrdstFile.next());
                pbxFileBackupInfo.setPbxFileData(
                        ConfigCreatorFile.getInstance().readFileReturnStirngWithEscape(itrlocalFile.next()));
                pbxFileBackupInfo.setLastUpdateAccountInfoId(accountInfoId);

                // DB更新
                Result<Boolean> rs = 
                        ConfigCreatorDAO.getInstance().upsertPbxFileBackupInfo(pbxFileBackupInfo, dbConnection);
                if(rs.getRetCode() != Const.ReturnCode.OK){
                    String msg = "内線サーバ設定ファイルバックアップ情報の更新に失敗しました。";
                    log.error(msg);
                    throw new Exception(msg);
                }

            }
        }
        // バックアップファイルのDB削除処理
        if(delFileList != null){
            Iterator<String> itrdelFile = delFileList.iterator();
            while(itrdelFile.hasNext()){            
                // PbxFileBackupInfoのインスタンス生成
                PbxFileBackupInfo pbxFileBackupInfo = new PbxFileBackupInfo();              

                // 設定値を取得
                pbxFileBackupInfo.setFkVmInfoId(vmInfo.getVmInfoId());
                pbxFileBackupInfo.setPbxFilePath(itrdelFile.next());

                // DB削除
                Result<Boolean> rs = 
                        ConfigCreatorDAO.getInstance().deletePbxFileBackupInfo(pbxFileBackupInfo, dbConnection);
                if(rs.getRetCode() != Const.ReturnCode.OK){
                    String msg = "内線サーバ設定ファイルバックアップ情報の削除に失敗しました。";
                    log.error(msg);
                    throw new Exception(msg);
                }
            }
        }

        log.trace("method end :executeBackUp");
    }

    /**
     * N番情報IDとアップロードファイルのリストからSFTP転送とSIPリロードを実施。
     * 処理の流れは以下となる
     * 1. 転送先のTMP面にローカルファイルをSFTP転送
     * 2. 転送先の本番面のファイルをバックアップ面に移動
     * 3. 転送先のTMP面のファイルを本番面に移動
     * 4. SIPリロード
     * 5. 転送先のTMP面とバックアップ面のファイルを削除
     * (2. 3. 4. の処理で失敗した場合、バックアップ面からファイルを復帰する)
     * 
     * @throws Exception 
     * 
     */
    private void executeFileUpload(String loginId,
            Connection dbConnection, 
            long nNumberInfoId,
            List<String> localFileList,
            List<String> dstFileList,
            List<String> dstFileBackUpList,
            List<String> dstFileTmpList,
            List<String> delFileList,
            List<String> delFileBackUpList
            ) throws Exception{
        log.trace("method start :executeFileUpload");

        // 変数宣言
        VmInfo vmInfo = new VmInfo();

        // N番IDからVM情報を取得
        Result<VmInfo> rsVmInfo = 
                ConfigCreatorDAO.getInstance().getVmInfoByNNumberInfoId(nNumberInfoId, dbConnection);
        if(rsVmInfo.getRetCode() == Const.ReturnCode.OK){
            vmInfo = rsVmInfo.getData();
        }else{
            String msg = "N番情報IDに紐づくVM情報の取得に失敗しました。"
                    + "  N番情報ID=" + nNumberInfoId ;
            log.error(msg);
            throw new Exception(msg);
        }

        // SFTPのインスタンス生成
        // START #684
        Sftp sftpInstance = new Sftp(vmInfo.getVmPrivateIpB().inetAddress().getHostAddress(), vmInfo.getOsLoginId(), vmInfo.getOsPassword(),ConfigCreatorProperties.PropertiesValue.ASTERISK_SSH_PORT );
        // END   #684
        // Amiのインスタンス生成     
        Ami amiInstance = new Ami();

        // SFTPのコネクション接続
        try{
            sftpInstance.connect(
                    ConfigCreatorProperties.PropertiesValue.RETRY_MAX_COUNT,
                    ConfigCreatorProperties.PropertiesValue.RETRY_INTERVAL);
        }
        catch (Exception e){
            //  SFTPアップロード失敗ログ
            // #955 START
            log.error(Util.message(Const.ERROR_CODE.E020804, String.format(Const.MESSAGE_CODE.E020804,null,null,null,null,null,loginId,vmInfo.getVmPrivateIpB().inetAddress().getHostAddress())));
            // #955 END
            throw e;
        }

        // 1. 転送先のTMP面にローカルファイルをSFTP転送
        try {
            // ファイルのアップロード処理
            if(dstFileList != null){
                Iterator<String> itrlocalFile = localFileList.iterator();
                Iterator<String> itrdstTmpFile = dstFileTmpList.iterator();
                while(itrlocalFile.hasNext()){            
                    // ファイルアップロード
                    sftpInstance.put(
                            ConfigCreatorProperties.PropertiesValue.RETRY_MAX_COUNT,
                            ConfigCreatorProperties.PropertiesValue.RETRY_INTERVAL,     
                            itrlocalFile.next(), itrdstTmpFile.next());                     
                }
            }  
        }
        catch (Exception e){
            //  SFTPアップロード失敗ログ
            // #955 START
            log.error(Util.message(Const.ERROR_CODE.E020804, String.format(Const.MESSAGE_CODE.E020804,null,null,null,null,null,loginId,vmInfo.getVmPrivateIpB().inetAddress().getHostAddress())));
            // #955 END
         
            // SFTPのコネクション切断
            sftpInstance.disconnect();
            throw e;
        }

        // 2. 転送先の本番面のファイルをバックアップ面に移動
        try {
            // 更新ファイルのバックアップ処理
            if(dstFileList != null){
                Iterator<String> itrdstFile = dstFileList.iterator();
                Iterator<String> itrdstFileBackUp = dstFileBackUpList.iterator();
                while(itrdstFile.hasNext()){
                    // ファイルリネーム
                    sftpInstance.rename(
                            ConfigCreatorProperties.PropertiesValue.RETRY_MAX_COUNT,
                            ConfigCreatorProperties.PropertiesValue.RETRY_INTERVAL,     
                            itrdstFile.next(), itrdstFileBackUp.next());                    
                }
            }
            // 削除ファイルのバックアップ処理
            if(delFileList != null){
                Iterator<String> itrdelFile = delFileList.iterator();
                Iterator<String> itrdelFileBackUp = delFileBackUpList.iterator();
                while(itrdelFile.hasNext()){
                    // ファイルリネーム
                    sftpInstance.rename(
                            ConfigCreatorProperties.PropertiesValue.RETRY_MAX_COUNT,
                            ConfigCreatorProperties.PropertiesValue.RETRY_INTERVAL,     
                            itrdelFile.next(), itrdelFileBackUp.next());                    
                }
            }           

        } catch (Exception e){
            //  SFTPアップロード失敗ログ
            // #955 START
            log.error(Util.message(Const.ERROR_CODE.E020804, String.format(Const.MESSAGE_CODE.E020804,null,null,null,null,null,loginId,vmInfo.getVmPrivateIpB().inetAddress().getHostAddress())));
            // #955 END

            // 更新ファイルのバックアップからファイル復旧
            if(dstFileList != null){
                Iterator<String> itrdstFile = dstFileList.iterator();
                Iterator<String> itrdstFileBackUp = dstFileBackUpList.iterator();
                while(itrdstFile.hasNext()){
                    // ファイルリネーム
                    sftpInstance.rename(
                            ConfigCreatorProperties.PropertiesValue.RETRY_MAX_COUNT,
                            ConfigCreatorProperties.PropertiesValue.RETRY_INTERVAL,     
                            itrdstFileBackUp.next(), itrdstFile.next());                    
                }
            }
            // 削除ファイルのバックアップからファイル復旧
            if(delFileList != null){
                Iterator<String> itrdelFile = delFileList.iterator();
                Iterator<String> itrdelFileBackUp = delFileBackUpList.iterator();
                while(itrdelFile.hasNext()){
                    // ファイルリネーム
                    sftpInstance.rename(
                            ConfigCreatorProperties.PropertiesValue.RETRY_MAX_COUNT,
                            ConfigCreatorProperties.PropertiesValue.RETRY_INTERVAL,     
                            itrdelFileBackUp.next(), itrdelFile.next());                    
                }
            }   

            // SFTPのコネクション切断
            sftpInstance.disconnect();
            throw e;
        }

        // 3. 転送先のTMP面のファイルを本番面に移動
        try {
            // TMP面のファイルを本番面に移動
            if(dstFileList != null){
                Iterator<String> itrdstFile = dstFileList.iterator();
                Iterator<String> itrdstFileTmp = dstFileTmpList.iterator();
                while(itrdstFile.hasNext()){
                    // ファイルリネーム
                    sftpInstance.rename(
                            ConfigCreatorProperties.PropertiesValue.RETRY_MAX_COUNT,
                            ConfigCreatorProperties.PropertiesValue.RETRY_INTERVAL,     
                            itrdstFileTmp.next(), itrdstFile.next());                       
                }
            }

            // ファイルの削除処理
            if(delFileList != null){
                Iterator<String> itrdelFile = delFileList.iterator();
                while(itrdelFile.hasNext()){            
                    // ファイル削除
                    sftpInstance.del(
                            ConfigCreatorProperties.PropertiesValue.RETRY_MAX_COUNT,
                            ConfigCreatorProperties.PropertiesValue.RETRY_INTERVAL, 
                            itrdelFile.next());                     
                }
            }

            // SFTPアップロード成功ログ
            // #955 START
            log.info(Util.message(Const.ERROR_CODE.I020803, String.format(Const.MESSAGE_CODE.I020803,null,null,null,null,null,loginId,vmInfo.getVmPrivateIpB().inetAddress().getHostAddress())));         
            // #955 END
        } catch (Exception e){
            // FTPアップロード失敗ログ
            // #955 START  
            log.error(Util.message(Const.ERROR_CODE.E020804, String.format(Const.MESSAGE_CODE.E020804,null,null,null,null,null,loginId,vmInfo.getVmPrivateIpB().inetAddress().getHostAddress())));
            // #955 END
            
            
            // 追加したファイルを削除
            if(dstFileList != null){
                Iterator<String> itrdstFile = dstFileList.iterator();
                while(itrdstFile.hasNext()){            
                    // ファイル削除
                    sftpInstance.del(
                            ConfigCreatorProperties.PropertiesValue.RETRY_MAX_COUNT,
                            ConfigCreatorProperties.PropertiesValue.RETRY_INTERVAL, 
                            itrdstFile.next());                     
                }
            }

            // 更新ファイルのバックアップからファイル復旧
            if(dstFileList != null){
                Iterator<String> itrdstFile = dstFileList.iterator();
                Iterator<String> itrdstFileBackUp = dstFileBackUpList.iterator();
                while(itrdstFile.hasNext()){
                    // ファイルリネーム
                    sftpInstance.rename(
                            ConfigCreatorProperties.PropertiesValue.RETRY_MAX_COUNT,
                            ConfigCreatorProperties.PropertiesValue.RETRY_INTERVAL,     
                            itrdstFileBackUp.next(), itrdstFile.next());                    
                }
            }
            // 削除ファイルのバックアップからファイル復旧
            if(delFileList != null){
                Iterator<String> itrdelFile = delFileList.iterator();
                Iterator<String> itrdelFileBackUp = delFileBackUpList.iterator();
                while(itrdelFile.hasNext()){
                    // ファイルリネーム
                    sftpInstance.rename(
                            ConfigCreatorProperties.PropertiesValue.RETRY_MAX_COUNT,
                            ConfigCreatorProperties.PropertiesValue.RETRY_INTERVAL,     
                            itrdelFileBackUp.next(), itrdelFile.next());                    
                }
            }   

            // コネクション切断
            sftpInstance.disconnect();
            throw e;
        }

        // 4. SIPリロード
        // リトライカウント
        int retryCount = 0;
        // 成功フラグ
        boolean sipReloadFlag = false;

        // retryMaxCountまでSIPリロードを実行
        while(! sipReloadFlag){
            try{
                // SIPリロード
                amiInstance.executeSipReload(
                        ConfigCreatorProperties.PropertiesValue.ASTERISK_AMI_USERNAME,
                        ConfigCreatorProperties.PropertiesValue.ASTERISK_AMI_PASSWORD,
                        vmInfo.getVmPrivateIpB().inetAddress().getHostAddress(),
                        ConfigCreatorProperties.PropertiesValue.ASTERISK_AMI_PORT);

                sipReloadFlag = true;

                // AMIコマンド成功ログ
                log.info(Util.message(Const.ERROR_CODE.I020805, String.format(Const.MESSAGE_CODE.I020805,null,null,null,null,null,loginId)));  

            }catch (Exception e){
                if(retryCount >= ConfigCreatorProperties.PropertiesValue.RETRY_MAX_COUNT){
                    // AMIコマンド失敗ログ
                    log.error(Util.message(Const.ERROR_CODE.E020806, String.format(Const.MESSAGE_CODE.E020806,null,null,null,null,null,loginId)));

                    // 追加したファイルを削除
                    if(dstFileList != null){
                        Iterator<String> itrdstFile = dstFileList.iterator();
                        while(itrdstFile.hasNext()){            
                            // ファイル削除
                            sftpInstance.del(
                                    ConfigCreatorProperties.PropertiesValue.RETRY_MAX_COUNT,
                                    ConfigCreatorProperties.PropertiesValue.RETRY_INTERVAL, 
                                    itrdstFile.next());                     
                        }
                    }
                    // 更新ファイルのバックアップからファイル復旧
                    if(dstFileList != null){
                        Iterator<String> itrdstFile = dstFileList.iterator();
                        Iterator<String> itrdstFileBackUp = dstFileBackUpList.iterator();
                        while(itrdstFile.hasNext()){
                            // ファイルリネーム
                            sftpInstance.rename(
                                    ConfigCreatorProperties.PropertiesValue.RETRY_MAX_COUNT,
                                    ConfigCreatorProperties.PropertiesValue.RETRY_INTERVAL,     
                                    itrdstFileBackUp.next(), itrdstFile.next());                    
                        }
                    }
                    // 削除ファイルのバックアップからファイル復旧
                    if(delFileList != null){
                        Iterator<String> itrdelFile = delFileList.iterator();
                        Iterator<String> itrdelFileBackUp = delFileBackUpList.iterator();
                        while(itrdelFile.hasNext()){
                            // ファイルリネーム
                            sftpInstance.rename(
                                    ConfigCreatorProperties.PropertiesValue.RETRY_MAX_COUNT,
                                    ConfigCreatorProperties.PropertiesValue.RETRY_INTERVAL,     
                                    itrdelFileBackUp.next(), itrdelFile.next());                    
                        }
                    }   

                    // コネクション切断
                    sftpInstance.disconnect();
                    throw e;        
                }
                else{
                    retryCount++;
                    Thread.sleep(ConfigCreatorProperties.PropertiesValue.RETRY_INTERVAL * 1000);
                }
            }
        }

        // 5. 転送先のバックアップ面のファイルを削除 
        try {
            // 更新ファイルのバックアップの削除処理
            if(dstFileBackUpList != null){
                Iterator<String> itrdstFileBackUp = dstFileBackUpList.iterator();
                while(itrdstFileBackUp.hasNext()){            
                    // ファイル削除
                    sftpInstance.del(
                            ConfigCreatorProperties.PropertiesValue.RETRY_MAX_COUNT,
                            ConfigCreatorProperties.PropertiesValue.RETRY_INTERVAL, 
                            itrdstFileBackUp.next());                       
                }
            }

            // 削除ファイルのバックアップの削除処理
            if(delFileBackUpList != null){
                Iterator<String> itrdelFileBackUp = delFileBackUpList.iterator();
                while(itrdelFileBackUp.hasNext()){            
                    // ファイル削除
                    sftpInstance.del(
                            ConfigCreatorProperties.PropertiesValue.RETRY_MAX_COUNT,
                            ConfigCreatorProperties.PropertiesValue.RETRY_INTERVAL, 
                            itrdelFileBackUp.next());                       
                }
            }

            // SFTPのコネクション切断
            sftpInstance.disconnect();
        }
        catch (Exception e){
            log.error("転送先のバックアップ面のファイルを削除 処理失敗 = " + e.toString());

            // SFTPのコネクション切断
            sftpInstance.disconnect();
            throw e;
        }

        log.trace("method end :executeFileUpload");
    }



    /**
     * 着信グループ情報IDから、extensions_内線番号_in.confの作成に必要なデータをセットする。
     * @throws Exception 
     * 
     */
    private void setExtensionsExtensionNumberInConfDataByIncomingGroupInfoId(
            Connection dbConnection,
            long incomingGroupInfoId,
            List<ExtensionNumberInfo> extensionNumberInfoList,
            List<AbsenceBehaviorInfo> absenceBehaviorInfoList,
            List<OutsideCallInfo> outsideCallInfoList,
            List<List<String>> childExtensionNumberInfoList,
            List<Integer> groupCallTypeList
            ) throws Exception{
        log.trace("method start :setExtensionsExtensionNumberInConfDataByIncomingGroupInfoId");
        log.trace("method param :incomingGroupInfoId = " + incomingGroupInfoId);

        // 変数宣言
        IncomingGroupInfo imcomingGroupInfo = new IncomingGroupInfo();

        // 着信グループ情報を取得
        Result<IncomingGroupInfo> rsIncomingGroupInfo = 
                ConfigCreatorDAO.getInstance().getIncomingGroupInfoById(incomingGroupInfoId, dbConnection);
        if(rsIncomingGroupInfo.getRetCode() == Const.ReturnCode.OK){
            imcomingGroupInfo = rsIncomingGroupInfo.getData();
        }else{
            String msg = "着信グループ情報の取得に失敗しました。"
                    + "  着信グループ情報ID=" + incomingGroupInfoId ;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        
        // #1334 start
        if (rsIncomingGroupInfo.getData() == null) {
            String msg = "着信グループ情報IDから着信グループ情報の取得に失敗しました。 " 
                    + "着信グループ情報ID = " + incomingGroupInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        // #1334 end

        // 着信グループ情報の内線番号情報IDからデータをセット
        setExtensionsExtensionNumberInConfDataByExtensionNumberInfoId(
                dbConnection,
                imcomingGroupInfo.getFkExtensionNumberInfoId(),
                extensionNumberInfoList,
                absenceBehaviorInfoList,
                outsideCallInfoList,
                childExtensionNumberInfoList,
                groupCallTypeList);    

        log.trace("method end :setExtensionsExtensionNumberInConfDataByIncomingGroupInfoId");       
    }

    /**
     * 内線番号情報IDから、sip_内線番号.confの作成に必要なデータをセットする。
     * @throws Exception 
     * 
     */
    private void setSipExtensionNumberConfDataByExtensionNumberInfoId(
            Connection dbConnection,
            long extensionNumberInfoId,
            List<ExtensionNumberInfo> extensionNumberInfoList,
            List<String> pickUpGroupList
            ) throws Exception{
        log.trace("method start :setSipExtensionNumberConfDataByExtensionNumberInfoId");
        log.trace("method param :extensionNumberInfoId = " + extensionNumberInfoId);

        // 変数宣言
        ExtensionNumberInfo extensionNumberInfo = new ExtensionNumberInfo();
        String pickUpGroup = new String();

        // 内線番号情報IDから内線番号情報を取得
        Result<ExtensionNumberInfo> rsExtensionNumberInfo = 
                ConfigCreatorDAO.getInstance().getExtensionNumberInfoById(extensionNumberInfoId, dbConnection);
        if(rsExtensionNumberInfo.getRetCode() == Const.ReturnCode.OK){
            extensionNumberInfo = rsExtensionNumberInfo.getData();
        }else{
            String msg = "内線番号情報の取得に失敗しました。"
                    + "  内線番号情報ID=" + extensionNumberInfoId ;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        
        // #1334 start
        if (rsExtensionNumberInfo.getData() == null) {
            String msg = "内線番号情報IDから内線番号情報の取得に失敗しました。 " 
                    + "内線番号情報ID = " + extensionNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        // #1334 end

        // 内線番号情報IDからコールピック番号を取得
        Result<IncomingGroupInfo> rsIncomingGroupInfo = 
                ConfigCreatorDAO.getInstance().getPickUpGroup(extensionNumberInfoId, dbConnection);
        if(rsIncomingGroupInfo.getRetCode() == Const.ReturnCode.OK){
            if( rsIncomingGroupInfo.getData() != null){
                pickUpGroup = rsIncomingGroupInfo.getData().getIncomingGroupName();
            }
            else{
                pickUpGroup = null;
            }
        }else{
            String msg = "着信グループ情報の取得に失敗しました。"
                    + "  内線番号情報ID=" + extensionNumberInfoId ;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        // 引数にデータをセット
        extensionNumberInfoList.add(extensionNumberInfo);
        pickUpGroupList.add(pickUpGroup);     

        log.trace("method end :setSipExtensionNumberConfDataByExtensionNumberInfoId");
    }

    // #834 START
    /**
     * 指定された内線番号情報IDに紐づく端末種別がVoIP-GWの場合、
     * 同じ内線番号かつ、異なる複数台利用のデータを取得し、
     * sip_内線番号.confの作成に必要なデータとして追加ででセットする。
     * @throws Exception 
     * 
     */
    private void setSipExtensionNumberConfDataByExtensionNumberInfoIdForOtherVoipGw(
            Connection dbConnection,
            long extensionNumberInfoId,
            List<ExtensionNumberInfo> extensionNumberInfoList,
            List<String> pickUpGroupList
            ) throws Exception{
        log.trace("method start :setSipExtensionNumberConfDataByExtensionNumberInfoIdForOtherVoipGw");
        log.trace("method param :extensionNumberInfoId = " + extensionNumberInfoId);

        ExtensionNumberInfo targetExtensionNumberInfo = new ExtensionNumberInfo();

        // 変数宣言
        List<ExtensionNumberInfo> extensionNumberInfoListVoipGw = new ArrayList<ExtensionNumberInfo>();
        String pickUpGroup = new String();


        // 内線番号情報IDから内線番号情報を取得
        Result<ExtensionNumberInfo> rsExtensionNumberInfo = 
                ConfigCreatorDAO.getInstance().getExtensionNumberInfoById(extensionNumberInfoId, dbConnection);
        if(rsExtensionNumberInfo.getRetCode() == Const.ReturnCode.OK){
            targetExtensionNumberInfo = rsExtensionNumberInfo.getData();
        }else{
            String msg = "内線番号情報の取得に失敗しました。"
                    + "  内線番号情報ID=" + extensionNumberInfoId ;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        
        // #1334 start
        if (rsExtensionNumberInfo.getData() == null) {
            String msg = "内線番号情報IDから内線番号情報の取得に失敗しました。 " 
                    + "内線番号情報ID = " + extensionNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        // #1334 end

        // 端末種別がVoIP-GW以外の場合は終了。
        // VoIP-GWの場合は、自分以外他の複数台利用のデータを取得
        Integer tmpTerminalType = targetExtensionNumberInfo.getTerminalType();
        if( null == tmpTerminalType || Const.TERMINAL_TYPE.VOIP_GW_RT != tmpTerminalType ){
            log.trace("terminal-Type is not VoIP-GW");
            return;
        }else{
            log.trace("terminal-Type is VoIP-GW. get other location multiuse.");

            Result<List<ExtensionNumberInfo>> rsExtensionNumberInfoList = null;
            rsExtensionNumberInfoList =  ConfigCreatorDAO.getInstance().getExtensionNumberInfoForOtherVoipGwRtAri(
                    targetExtensionNumberInfo.getExtensionNumber(),
                    targetExtensionNumberInfo.getLocationNumMultiUse(),
                    targetExtensionNumberInfo.getFkNNumberInfoId(),
                    dbConnection);

            if(rsExtensionNumberInfoList.getRetCode() == Const.ReturnCode.OK){
                log.trace("get List of ExtensionNumberInfo");
                extensionNumberInfoListVoipGw = rsExtensionNumberInfoList.getData();
            }else{
                String msg = "同一拠点番号内かつ自端末以外のVoIP-GWの情報の取得に失敗しました。"
                        + "  内線番号" + targetExtensionNumberInfo.getExtensionNumber()
                        + ", 複数台利用数" + targetExtensionNumberInfo.getLocationNumMultiUse()
                        + ", N番情報ID" + targetExtensionNumberInfo.getFkNNumberInfoId();
                log.error(msg);
                throw new IllegalArgumentException(msg);
            }

            // VoIP-GWなのでpickUpGroup(コールピック番号)はnull固定
            pickUpGroup = null;

            for( ExtensionNumberInfo extensionNumberInfo : extensionNumberInfoListVoipGw ){
                if( null==extensionNumberInfo ){ continue; }
                // 引数にデータをセット
                extensionNumberInfoList.add(extensionNumberInfo);
                pickUpGroupList.add(pickUpGroup);    
            }//for

        }//else

        log.trace("method end :setSipExtensionNumberConfDataByExtensionNumberInfoIdForOtherVoipGw");
    }
    // #834 END

    /**
     * 内線番号情報IDから、extensions_内線番号_in.confの作成に必要なデータをセットする。
     * @throws Exception 
     * 
     */
    private void setExtensionsExtensionNumberInConfDataByExtensionNumberInfoId(
            Connection dbConnection,
            long extensionNumberInfoId,
            List<ExtensionNumberInfo> extensionNumberInfoList,
            List<AbsenceBehaviorInfo> absenceBehaviorInfoList,
            List<OutsideCallInfo> outsideCallInfoList,
            List<List<String>> childExtensionNumberInfoList,
            List<Integer> groupCallTypeList) throws Exception{
        log.trace("method start :setExtensionsExtensionNumberInConfDataByExtensionNumberInfoId");
        log.trace("method param :extensionNumberInfoId = " + extensionNumberInfoId);

        // 変数宣言
        ExtensionNumberInfo extensionNumberInfo = new ExtensionNumberInfo();
        AbsenceBehaviorInfo absenceBehaviorInfo = new AbsenceBehaviorInfo();
        OutsideCallInfo outsideCallInfo = new OutsideCallInfo();
        IncomingGroupInfo imcomingGroupInfo = new IncomingGroupInfo();

        int groupCallType = 0;
        List<String> groupExtensionNumberList = new ArrayList<String>();

        
        // 内線番号情報IDから内線番号情報を取得
        Result<ExtensionNumberInfo> rsExtensionNumberInfo = 
                ConfigCreatorDAO.getInstance().getExtensionNumberInfoById(extensionNumberInfoId, dbConnection);
        
        if(rsExtensionNumberInfo.getRetCode() == Const.ReturnCode.OK){
            extensionNumberInfo = rsExtensionNumberInfo.getData();
        }else{
            String msg = "内線番号情報の取得に失敗しました。"
                    + "  内線番号情報ID=" + extensionNumberInfoId ;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        
        // #1334 start
        if (rsExtensionNumberInfo.getData() == null) {
            String msg = "内線番号情報IDから内線番号情報の取得に失敗しました。" 
                    + "  内線番号情報ID = " + extensionNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        // #1334 end

        
        // 内線番号情報IDから不在時動作情報を取得
        Result<AbsenceBehaviorInfo> rsAbsenceBehaviorInfo = 
                ConfigCreatorDAO.getInstance().getAbsenceBehaviorInfoById(extensionNumberInfo.getExtensionNumberInfoId(), dbConnection);
        if(rsAbsenceBehaviorInfo.getRetCode() == Const.ReturnCode.OK){
            absenceBehaviorInfo = rsAbsenceBehaviorInfo.getData();
        }else{
            String msg = "内線番号情報IDから不在時動作情報の取得に失敗しました。" 
                    + "  内線番号情報ID = " + extensionNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        // 外線発信情報の内線番号情報IDから外線情報を取得
        Result<OutsideCallInfo> rsOutsideCallInfo = 
                ConfigCreatorDAO.getInstance().getOutsideCallInfoByExtensionNumberInfoId(extensionNumberInfoId, dbConnection);
        if(rsOutsideCallInfo.getRetCode() == Const.ReturnCode.OK){
            outsideCallInfo = rsOutsideCallInfo.getData();
        }else{
            String msg = "内線番号情報IDから外線情報（外線発信情報）の取得に失敗しました。" 
                    + "  内線番号情報ID = " + extensionNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        // 内線番号情報IDから着信グループ情報を取得
        Result<IncomingGroupInfo> rsIncomingGroupInfo = 
                ConfigCreatorDAO.getInstance().getIncomingGroupInfoByExtensionNumberInfoId(extensionNumberInfoId, dbConnection);
        if(rsIncomingGroupInfo.getRetCode() == Const.ReturnCode.OK){
            imcomingGroupInfo = rsIncomingGroupInfo.getData();
        }else{
            String msg = "内線番号情報IDから着信グループ情報の取得に失敗しました。" 
                    + "  内線番号情報ID = " + extensionNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        // 内線番号が代表番号として設定されている場合、グループ種別とグループ子番号を取得
        if(imcomingGroupInfo != null){
            // グループ種別を代入
            groupCallType = imcomingGroupInfo.getGroupCallType();

            // 着信グループ情報に紐づく、グループ子番号の内線番号情報を取得
            Result<List<String>> rsExtensionNumberInfoList = 
                    ConfigCreatorDAO.getInstance().getGroupChildNumber(imcomingGroupInfo.getIncomingGroupInfoId(), dbConnection);
            if(rsExtensionNumberInfoList.getRetCode() == Const.ReturnCode.OK){
                groupExtensionNumberList = rsExtensionNumberInfoList.getData();
            }else{
                String msg = "着信グループ情報IDからグループ子番号の内線番号情報の取得に失敗しました。" 
                        + "  内線番号情報ID = " + extensionNumberInfoId
                        + "  着信グループ情報ID = " + imcomingGroupInfo.getIncomingGroupInfoId();
                log.error(msg);
                throw new IllegalArgumentException(msg);
            }            
        }

        // 引数にデータをセット
        extensionNumberInfoList.add(extensionNumberInfo);
        absenceBehaviorInfoList.add(absenceBehaviorInfo);
        outsideCallInfoList.add(outsideCallInfo);
        childExtensionNumberInfoList.add(groupExtensionNumberList);
        groupCallTypeList.add(groupCallType);     

        log.trace("method end :setExtensionsExtensionNumberInConfDataByExtensionNumberInfoId");
    }

    // #834 START
    /**
     * 指定された内線番号情報IDに紐づく端末種別がVoIP-GWの場合、
     * 同じ内線番号かつ、異なる複数台利用のデータを取得し、
     * extensions_内線番号_in.confの作成に必要なデータとして追加ででセットする。
     * @throws Exception 
     * 
     */
    private void setExtensionsExtensionNumberInConfDataByExtensionNumberInfoIdForOtherVoipGw(
            Connection dbConnection,
            long extensionNumberInfoId,
            List<ExtensionNumberInfo> extensionNumberInfoList,
            List<AbsenceBehaviorInfo> absenceBehaviorInfoList,
            List<OutsideCallInfo> outsideCallInfoList,
            List<List<String>> childExtensionNumberInfoList,
            List<Integer> groupCallTypeList) throws Exception{
        log.trace("method start :setExtensionsExtensionNumberInConfDataByExtensionNumberInfoIdForOtherVoipGw");
        log.trace("method param :extensionNumberInfoId = " + extensionNumberInfoId);

        ExtensionNumberInfo targetExtensionNumberInfo = new ExtensionNumberInfo();
        
        // 変数宣言
        List<ExtensionNumberInfo> extensionNumberInfoListVoipGw = new ArrayList<ExtensionNumberInfo>();
        AbsenceBehaviorInfo absenceBehaviorInfo = new AbsenceBehaviorInfo();
        OutsideCallInfo outsideCallInfo = new OutsideCallInfo();
        IncomingGroupInfo imcomingGroupInfo = new IncomingGroupInfo();

        int groupCallType = 0;
        List<String> groupExtensionNumberList = new ArrayList<String>();

        // 内線番号情報IDから内線番号情報を取得
        Result<ExtensionNumberInfo> rsExtensionNumberInfo = 
                ConfigCreatorDAO.getInstance().getExtensionNumberInfoById(extensionNumberInfoId, dbConnection);
        if(rsExtensionNumberInfo.getRetCode() == Const.ReturnCode.OK){
            targetExtensionNumberInfo = rsExtensionNumberInfo.getData();
        }else{
            String msg = "内線番号情報の取得に失敗しました。"
                    + "  内線番号情報ID=" + extensionNumberInfoId ;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        
        // #1334 start
        if (rsExtensionNumberInfo.getData() == null) {
            String msg = "内線番号情報IDから内線番号情報の取得に失敗しました。 " 
                    + "内線番号情報ID = " + extensionNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        // #1334 end
        
        // 端末種別がVoIP-GW以外の場合は終了。
        // VoIP-GWの場合は、自分以外他の複数台利用のデータを取得
        Integer tmpTerminalType = targetExtensionNumberInfo.getTerminalType();
        if( null == tmpTerminalType || Const.TERMINAL_TYPE.VOIP_GW_RT != tmpTerminalType ){
            log.trace("terminal-Type is not VoIP-GW");
            return;
        }else{
            log.trace("terminal-Type is VoIP-GW. get other location multiuse.");

            Result<List<ExtensionNumberInfo>> rsExtensionNumberInfoList = null;
            rsExtensionNumberInfoList =  ConfigCreatorDAO.getInstance().getExtensionNumberInfoForOtherVoipGwRtAri(
                    targetExtensionNumberInfo.getExtensionNumber(),
                    targetExtensionNumberInfo.getLocationNumMultiUse(),
                    targetExtensionNumberInfo.getFkNNumberInfoId(),
                    dbConnection);

            if(rsExtensionNumberInfoList.getRetCode() == Const.ReturnCode.OK){
                log.trace("get List of ExtensionNumberInfo");
                extensionNumberInfoListVoipGw = rsExtensionNumberInfoList.getData();
            }else{
                String msg = "同一拠点番号内かつ自端末以外のVoIP-GWの情報の取得に失敗しました。"
                        + "  内線番号" + targetExtensionNumberInfo.getExtensionNumber()
                        + ", 複数台利用数" + targetExtensionNumberInfo.getLocationNumMultiUse()
                        + ", N番情報ID" + targetExtensionNumberInfo.getFkNNumberInfoId();
                log.error(msg);
            }
            
            for( ExtensionNumberInfo extensionNumberInfo : extensionNumberInfoListVoipGw ){
                if( null==extensionNumberInfo ){ continue; }
                
                // 内線番号情報IDから不在時動作情報を取得
                Result<AbsenceBehaviorInfo> rsAbsenceBehaviorInfo = 
                        ConfigCreatorDAO.getInstance().getAbsenceBehaviorInfoById(extensionNumberInfo.getExtensionNumberInfoId(), dbConnection);
                if(rsAbsenceBehaviorInfo.getRetCode() == Const.ReturnCode.OK){
                    absenceBehaviorInfo = rsAbsenceBehaviorInfo.getData();
                }else{
                    String msg = "内線番号情報IDから不在時動作情報の取得に失敗しました。" 
                            + "  内線番号情報ID = " + extensionNumberInfoId;
                    log.error(msg);
                    throw new IllegalArgumentException(msg);
                }

                // 外線発信情報の内線番号情報IDから外線情報を取得
                Result<OutsideCallInfo> rsOutsideCallInfo = 
                        ConfigCreatorDAO.getInstance().getOutsideCallInfoByExtensionNumberInfoId(extensionNumberInfoId, dbConnection);
                if(rsOutsideCallInfo.getRetCode() == Const.ReturnCode.OK){
                    outsideCallInfo = rsOutsideCallInfo.getData();
                }else{
                    String msg = "内線番号情報IDから外線情報（外線発信情報）の取得に失敗しました。" 
                            + "  内線番号情報ID = " + extensionNumberInfoId;
                    log.error(msg);
                    throw new IllegalArgumentException(msg);
                }

                // 内線番号情報IDから着信グループ情報を取得
                Result<IncomingGroupInfo> rsIncomingGroupInfo = 
                        ConfigCreatorDAO.getInstance().getIncomingGroupInfoByExtensionNumberInfoId(extensionNumberInfoId, dbConnection);
                if(rsIncomingGroupInfo.getRetCode() == Const.ReturnCode.OK){
                    imcomingGroupInfo = rsIncomingGroupInfo.getData();
                }else{
                    String msg = "内線番号情報IDから着信グループ情報の取得に失敗しました。" 
                            + "  内線番号情報ID = " + extensionNumberInfoId;
                    log.error(msg);
                    throw new IllegalArgumentException(msg);
                }

                // 内線番号が代表番号として設定されている場合、グループ種別とグループ子番号を取得
                if(imcomingGroupInfo != null){
                    // グループ種別を代入
                    groupCallType = imcomingGroupInfo.getGroupCallType();

                    // 着信グループ情報に紐づく、グループ子番号の内線番号情報を取得
                    Result<List<String>> rsExtensionNumberListOfChild = 
                            ConfigCreatorDAO.getInstance().getGroupChildNumber(imcomingGroupInfo.getIncomingGroupInfoId(), dbConnection);
                    if(rsExtensionNumberListOfChild.getRetCode() == Const.ReturnCode.OK){
                        groupExtensionNumberList = rsExtensionNumberListOfChild.getData();
                    }else{
                        String msg = "着信グループ情報IDからグループ子番号の内線番号情報の取得に失敗しました。" 
                                + "  内線番号情報ID = " + extensionNumberInfoId
                                + "  着信グループ情報ID = " + imcomingGroupInfo.getIncomingGroupInfoId();
                        log.error(msg);
                        throw new IllegalArgumentException(msg);
                    }            
                }
                
                // 引数にデータをセット
                extensionNumberInfoList.add(extensionNumberInfo);
                absenceBehaviorInfoList.add(absenceBehaviorInfo);
                outsideCallInfoList.add(outsideCallInfo);
                childExtensionNumberInfoList.add(groupExtensionNumberList);
                groupCallTypeList.add(groupCallType);     
            }//for

        }//else

        log.trace("method end :setExtensionsExtensionNumberInConfDataByExtensionNumberInfoIdForOtherVoipGw");
    }
    // #834 END
    
    
    // #1287 START
    /**
     * 指定された内線番号情報IDが順次着信もしくは一斉着信の子番号の場合に、
     * 親番号の内線情報IDを取得し、その内線番号情報IDを用いて
     * extensions_内線番号_in.confの作成に必要なデータとして追加でセットする。
     * （子番号でない場合は追加でセットしない。）
     * @throws Exception 
     * 
     */
    private void setExtensionsExtensionNumberInConfDataByExtensionNumberInfoIdForIfChildNumber(
            Connection dbConnection,
            long extensionNumberInfoId,
            List<ExtensionNumberInfo> extensionNumberInfoList,
            List<AbsenceBehaviorInfo> absenceBehaviorInfoList,
            List<OutsideCallInfo> outsideCallInfoList,
            List<List<String>> childExtensionNumberInfoList,
            List<Integer> groupCallTypeList) throws Exception{
        log.trace("method start :setExtensionsExtensionNumberInConfDataByExtensionNumberInfoIdForIfChildNumber");
        log.trace("method param :extensionNumberInfoId = " + extensionNumberInfoId);
        
        // DB検索結果の着信グループ情報を保持するList
        List<IncomingGroupInfo> incomingGroupInfoList = null;
        
        // 引数の内線番号情報IDが子番号である着信グループ情報の取得
        Result<List<IncomingGroupInfo>> rsIncomingGroupList = 
                ConfigCreatorDAO.getInstance().getIncomingGroupInfoListByChildByExtensionNumberInfoId(
                extensionNumberInfoId,
                dbConnection );
        if(rsIncomingGroupList.getRetCode() == Const.ReturnCode.OK){
            incomingGroupInfoList = rsIncomingGroupList.getData();
        }else{
            throw new IllegalArgumentException("着信グループの取得に失敗しました。  内線番号情報ID = " + extensionNumberInfoId);
        }
        
        //以降は取得した着信グループごとにループ
        for( IncomingGroupInfo incomingGroupInfo : incomingGroupInfoList ){
            
            if( incomingGroupInfo == null){
                continue;
            }
            // その着信グループ情報が順次着信と一斉着信以外の場合追加不要
            if( incomingGroupInfo.getGroupCallType() != 1 && incomingGroupInfo.getGroupCallType() != 2 ){
                continue;
            }
            
            // 着信グループの代表番号である内線番号情報IDで、extensions_内線番号_in.confの作成に必要なデータを取得
            log.trace("need to create extension_内線番号_in.conf which is owned by pilot number.");
            setExtensionsExtensionNumberInConfDataByExtensionNumberInfoId(
                    dbConnection, 
                    incomingGroupInfo.getFkExtensionNumberInfoId(),  // ここが親番号の内線番号情報ID 
                    extensionNumberInfoList, 
                    absenceBehaviorInfoList, 
                    outsideCallInfoList, 
                    childExtensionNumberInfoList,
                    groupCallTypeList );
        } //for
        
            
        log.trace("method end :setExtensionsExtensionNumberInConfDataByExtensionNumberInfoIdForIfChildNumber");
    }
    // #1287 END
    
    
    // #1290 START
    /**
     * 外線発信情報IDから、extensions_内線番号_in.confの作成に必要なデータをセットする。
     * (setExtensionsExtensionNumberInConfDataByExtensionNumberInfoIdForOtherVoipGwを呼んでVoIP-GWの考慮をしている)
     * @throws Exception 
     * 
     */
    private void setExtensionsExtensionNumberInConfDataByOutsideCallSendingInfoId(
            Connection dbConnection,
            long outsideCallSendingInfoId,
            List<ExtensionNumberInfo> extensionNumberInfoList,
            List<AbsenceBehaviorInfo> absenceBehaviorInfoList,
            List<OutsideCallInfo> outsideCallInfoList,
            List<List<String>> childExtensionNumberInfoList,
            List<Integer> groupCallTypeList) throws Exception{
        log.trace("method start :setExtensionsExtensionNumberInConfDataByOutsideCallSendingInfoId");
        log.trace("method param :outsideCallSendingInfoId = " + outsideCallSendingInfoId);

        // 変数宣言
        ExtensionNumberInfo extensionNumberInfo = new ExtensionNumberInfo();
        AbsenceBehaviorInfo absenceBehaviorInfo = new AbsenceBehaviorInfo();
        OutsideCallInfo outsideCallInfo = new OutsideCallInfo();
        IncomingGroupInfo imcomingGroupInfo = new IncomingGroupInfo();

        int groupCallType = 0;
        List<String> groupExtensionNumberList = new ArrayList<String>();

        // 外線発信情報IDから内線番号情報を取得
        Result<ExtensionNumberInfo> rsExtensionNumberInfo = 
                ConfigCreatorDAO.getInstance().getExtensionNumberInfoByOutsideCallSendingInfoId(
                        outsideCallSendingInfoId,
                        dbConnection );
        if(rsExtensionNumberInfo.getRetCode() == Const.ReturnCode.OK){
            extensionNumberInfo = rsExtensionNumberInfo.getData();
        }else{
            String msg = "外線発信情報IDから内線番号情報の取得に失敗しました。"
                    + "  外線発信情報ID=" + outsideCallSendingInfoId ;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        // 内線番号情報IDから不在時動作情報を取得
        Result<AbsenceBehaviorInfo> rsAbsenceBehaviorInfo = 
                ConfigCreatorDAO.getInstance().getAbsenceBehaviorInfoById(
                        extensionNumberInfo.getExtensionNumberInfoId(),
                        dbConnection );
        if(rsAbsenceBehaviorInfo.getRetCode() == Const.ReturnCode.OK){
            absenceBehaviorInfo = rsAbsenceBehaviorInfo.getData();
        }else{
            String msg = "内線番号情報IDから不在時動作情報の取得に失敗しました。" 
                    + "  内線番号情報ID = " + extensionNumberInfo.getExtensionNumberInfoId();
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        // 外線発信情報の内線番号情報IDから外線情報を取得
        Result<OutsideCallInfo> rsOutsideCallInfo = 
                ConfigCreatorDAO.getInstance().getOutsideCallInfoByExtensionNumberInfoId(
                        extensionNumberInfo.getExtensionNumberInfoId(),
                        dbConnection );
        if(rsOutsideCallInfo.getRetCode() == Const.ReturnCode.OK){
            outsideCallInfo = rsOutsideCallInfo.getData();
        }else{
            String msg = "内線番号情報IDから外線情報（外線発信情報）の取得に失敗しました。" 
                    + "  内線番号情報ID = " + extensionNumberInfo.getExtensionNumberInfoId();
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        // 内線番号情報IDから着信グループ情報を取得
        Result<IncomingGroupInfo> rsIncomingGroupInfo = 
                ConfigCreatorDAO.getInstance().getIncomingGroupInfoByExtensionNumberInfoId(
                        extensionNumberInfo.getExtensionNumberInfoId(),
                        dbConnection );
        if(rsIncomingGroupInfo.getRetCode() == Const.ReturnCode.OK){
            imcomingGroupInfo = rsIncomingGroupInfo.getData();
        }else{
            String msg = "内線番号情報IDから着信グループ情報の取得に失敗しました。" 
                    + "  内線番号情報ID = " + extensionNumberInfo.getExtensionNumberInfoId();
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        // 内線番号が代表番号として設定されている場合、グループ種別とグループ子番号を取得
        if(imcomingGroupInfo != null){
            // グループ種別を代入
            groupCallType = imcomingGroupInfo.getGroupCallType();

            // 着信グループ情報に紐づく、グループ子番号の内線番号情報を取得
            Result<List<String>> rsExtensionNumberInfoList = 
                    ConfigCreatorDAO.getInstance().getGroupChildNumber(imcomingGroupInfo.getIncomingGroupInfoId(), dbConnection);
            if(rsExtensionNumberInfoList.getRetCode() == Const.ReturnCode.OK){
                groupExtensionNumberList = rsExtensionNumberInfoList.getData();
            }else{
                String msg = "着信グループ情報IDからグループ子番号の内線番号情報の取得に失敗しました。" 
                        + "  内線番号情報ID = " + extensionNumberInfo.getExtensionNumberInfoId()
                        + "  着信グループ情報ID = " + imcomingGroupInfo.getIncomingGroupInfoId();
                log.error(msg);
                throw new IllegalArgumentException(msg);
            }            
        }

        // 引数にデータをセット
        extensionNumberInfoList.add(extensionNumberInfo);
        absenceBehaviorInfoList.add(absenceBehaviorInfo);
        outsideCallInfoList.add(outsideCallInfo);
        childExtensionNumberInfoList.add(groupExtensionNumberList);
        groupCallTypeList.add(groupCallType);     
        
        //(#834の対処を入れる)
        setExtensionsExtensionNumberInConfDataByExtensionNumberInfoIdForOtherVoipGw(
                dbConnection,
                extensionNumberInfo.getExtensionNumberInfoId(),
                extensionNumberInfoList,
                absenceBehaviorInfoList,
                outsideCallInfoList,
                childExtensionNumberInfoList,
                groupCallTypeList );

        log.trace("method end :setExtensionsExtensionNumberInConfDataByOutsideCallSendingInfoId");
    }
    // #1290 END

    /**
     * 内線番号情報IDから、extensions_内線番号_out.confの作成に必要なデータをセットする。
     * @throws Exception 
     * 
     */
    //  #997 START (引数の追加)
    private void setExtensionsExtensionNumberOutConfDataByExtensionNumberInfoId(
            Connection dbConnection,
            long extensionNumberInfoId,
            List<ExtensionNumberInfo> extensionNumberInfoList,
            List<OutsideCallInfo> outsideCallInfoList,
            List<OutsideCallInfo> outsideCallInfoForDialInList
            ) throws Exception{
        // #997 END
        log.trace("method start :setExtensionsExtensionNumberOutConfDataByExtensionNumberInfoId");
        log.trace("method param :extensionNumberInfoId = " + extensionNumberInfoId);

        // 変数宣言
        ExtensionNumberInfo extensionNumberInfo = new ExtensionNumberInfo();
        OutsideCallInfo outsideCallInfo = new OutsideCallInfo();        
        // #997 START
        OutsideCallInfo outsideCallInfoForDialIn = new OutsideCallInfo();
        // #997 END

        // 内線番号情報IDから内線番号情報を取得
        Result<ExtensionNumberInfo> rsExtensionNumberInfo = 
                ConfigCreatorDAO.getInstance().getExtensionNumberInfoById(extensionNumberInfoId, dbConnection);
        if(rsExtensionNumberInfo.getRetCode() == Const.ReturnCode.OK){
            extensionNumberInfo = rsExtensionNumberInfo.getData();
        }else{
            String msg = "内線番号情報の取得に失敗しました。"
                    + "  内線番号情報ID=" + extensionNumberInfoId ;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        // #1334 start
        if (rsExtensionNumberInfo.getData() == null) {
            String msg = "内線番号情報IDから内線番号情報の取得に失敗しました。 " 
                    + "内線番号情報ID = " + extensionNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        // #1334 end
        
        // 外線発信情報の内線番号情報IDから外線情報を取得
        Result<OutsideCallInfo> rsOutsideCallInfo = 
                ConfigCreatorDAO.getInstance().getOutsideCallInfoByExtensionNumberInfoId(extensionNumberInfoId, dbConnection);
        if(rsOutsideCallInfo.getRetCode() == Const.ReturnCode.OK){
            outsideCallInfo = rsOutsideCallInfo.getData();
            // #997 START
            outsideCallInfoForDialIn = rsOutsideCallInfo.getData();
            // #997 END

            // #770 START
            // 取得した外線番号情報がIP-Voiceかつ、追加番号の場合
            // IP-Voiceかつ同じアクセス回線種別の基本番号の外線番号情報を取得する
            if( null != outsideCallInfo
                    && outsideCallInfo.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX
                    && true == outsideCallInfo.getAddFlag() ){
                log.trace("try to get Bace Number Of IP-Voice.");
                rsOutsideCallInfo = ConfigCreatorDAO.getInstance().getOutsideCallInfoForBaceNumberOfIpVoice(
                        outsideCallInfo.getFkNNumberInfoId(),
                        outsideCallInfo.getOutsideCallLineType(),
                        dbConnection);
                if(rsOutsideCallInfo.getRetCode() == Const.ReturnCode.OK){
                    outsideCallInfo = rsOutsideCallInfo.getData();
                }else{
                    throw new Exception("IP-Voice外線の基本番号の取得に失敗しました。");
                }
            }
            // #770 END

        }else{
            String msg = "内線番号情報IDから外線情報（外線発信情報）の取得に失敗しました。" 
                    + "  内線番号情報ID = " + extensionNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        // 引数にデータをセット
        extensionNumberInfoList.add(extensionNumberInfo);
        outsideCallInfoList.add(outsideCallInfo);    
        // #997 START
        outsideCallInfoForDialInList.add(outsideCallInfoForDialIn);
        // #997 END

        log.trace("method end :setExtensionsExtensionNumberOutConfDataByExtensionNumberInfoId");
    }

    // #834 START
    /**
     * 指定された内線番号情報IDに紐づく端末種別がVoIP-GWの場合、
     * 同じ内線番号かつ、異なる複数台利用のデータを取得し、
     * extensions_内線番号_out.confの作成に必要なデータとして追加ででセットする。
     * @throws Exception 
     * 
     */
    //  #997 START (引数の追加)
    private void setExtensionsExtensionNumberOutConfDataByExtensionNumberInfoIdForOtherVoipGw(
            Connection dbConnection,
            long extensionNumberInfoId,
            List<ExtensionNumberInfo> extensionNumberInfoList,
            List<OutsideCallInfo> outsideCallInfoList,
            List<OutsideCallInfo> outsideCallInfoForDialInList
            ) throws Exception{
        // #997 END
        log.trace("method start :setExtensionsExtensionNumberOutConfDataByExtensionNumberInfoIdForOtherVoipGw");
        log.trace("method param :extensionNumberInfoId = " + extensionNumberInfoId);
        
        ExtensionNumberInfo targetExtensionNumberInfo = new ExtensionNumberInfo();

        // 変数宣言
        List<ExtensionNumberInfo> extensionNumberInfoListVoipGw = new ArrayList<ExtensionNumberInfo>();
        OutsideCallInfo outsideCallInfo = new OutsideCallInfo();        
        // #997 START
        OutsideCallInfo outsideCallInfoForDialIn = new OutsideCallInfo();
        // #997 END

        // 内線番号情報IDから内線番号情報を取得
        Result<ExtensionNumberInfo> rsExtensionNumberInfo = 
                ConfigCreatorDAO.getInstance().getExtensionNumberInfoById(extensionNumberInfoId, dbConnection);
        if(rsExtensionNumberInfo.getRetCode() == Const.ReturnCode.OK){
            targetExtensionNumberInfo = rsExtensionNumberInfo.getData();
        }else{
            String msg = "内線番号情報の取得に失敗しました。"
                    + "  内線番号情報ID=" + extensionNumberInfoId ;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        
        // #1334 start
        if (rsExtensionNumberInfo.getData() == null) {
            String msg = "内線番号情報IDから内線番号情報の取得に失敗しました。 " 
                    + "内線番号情報ID = " + extensionNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        // #1334 end
        
        // 端末種別がVoIP-GW以外の場合は終了。
        // VoIP-GWの場合は、自分以外他の複数台利用のデータを取得
        Integer tmpTerminalType = targetExtensionNumberInfo.getTerminalType();
        if( null == tmpTerminalType || Const.TERMINAL_TYPE.VOIP_GW_RT != tmpTerminalType ){
            log.trace("terminal-Type is not VoIP-GW");
            return;
        }else{
            log.trace("terminal-Type is VoIP-GW. get other location multiuse.");

            Result<List<ExtensionNumberInfo>> rsExtensionNumberInfoList = null;
            rsExtensionNumberInfoList =  ConfigCreatorDAO.getInstance().getExtensionNumberInfoForOtherVoipGwRtAri(
                    targetExtensionNumberInfo.getExtensionNumber(),
                    targetExtensionNumberInfo.getLocationNumMultiUse(),
                    targetExtensionNumberInfo.getFkNNumberInfoId(),
                    dbConnection);

            if(rsExtensionNumberInfoList.getRetCode() == Const.ReturnCode.OK){
                log.trace("get List of ExtensionNumberInfo");
                extensionNumberInfoListVoipGw = rsExtensionNumberInfoList.getData();
            }else{
                String msg = "同一拠点番号内かつ自端末以外のVoIP-GWの情報の取得に失敗しました。"
                        + "  内線番号" + targetExtensionNumberInfo.getExtensionNumber()
                        + ", 複数台利用数" + targetExtensionNumberInfo.getLocationNumMultiUse()
                        + ", N番情報ID" + targetExtensionNumberInfo.getFkNNumberInfoId();
                log.error(msg);
            }
            
            for( ExtensionNumberInfo extensionNumberInfo : extensionNumberInfoListVoipGw ){
                if( null==extensionNumberInfo ){ continue; }
                
                // #1037 START (内線番号情報IDの変更)
                long tmpExtensionNumberInfoIdForOhrtVoipGw = extensionNumberInfo.getExtensionNumberInfoId();
                // #1037 END
                
                // 外線発信情報の内線番号情報IDから外線情報を取得
                // #1037 START (内線番号情報IDの変更)
                Result<OutsideCallInfo> rsOutsideCallInfo = 
                        ConfigCreatorDAO.getInstance().getOutsideCallInfoByExtensionNumberInfoId(
                                tmpExtensionNumberInfoIdForOhrtVoipGw,
                                dbConnection );
                // #1037 END
                if(rsOutsideCallInfo.getRetCode() == Const.ReturnCode.OK){
                    outsideCallInfo = rsOutsideCallInfo.getData();
                    // #997 START
                    outsideCallInfoForDialIn = rsOutsideCallInfo.getData();
                    // #997 END
                    
                    // #770 START
                    // 取得した外線番号情報がIP-Voiceかつ、追加番号の場合
                    // IP-Voiceかつ同じアクセス回線種別の基本番号の外線番号情報を取得する
                    if( null != outsideCallInfo
                            && outsideCallInfo.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX
                            && true == outsideCallInfo.getAddFlag() ){
                        log.trace("try to get Bace Number Of IP-Voice.");
                        rsOutsideCallInfo = ConfigCreatorDAO.getInstance().getOutsideCallInfoForBaceNumberOfIpVoice(
                                outsideCallInfo.getFkNNumberInfoId(),
                                outsideCallInfo.getOutsideCallLineType(),
                                dbConnection);
                        if(rsOutsideCallInfo.getRetCode() == Const.ReturnCode.OK){
                            outsideCallInfo = rsOutsideCallInfo.getData();
                        }else{
                            throw new Exception("IP-Voice外線の基本番号の取得に失敗しました。");
                        }
                    }
                    // #770 END
                }else{
                    String msg = "内線番号情報IDから外線情報（外線発信情報）の取得に失敗しました。" 
                            + "  内線番号情報ID = " + extensionNumberInfoId;
                    log.error(msg);
                    throw new IllegalArgumentException(msg);
                }
                
                // 引数にデータをセット
                extensionNumberInfoList.add(extensionNumberInfo);
                outsideCallInfoList.add(outsideCallInfo);    
                // #997 START
                outsideCallInfoForDialInList.add(outsideCallInfoForDialIn);
                // #997 END
            }//for

        }//else
        
        log.trace("method end :setExtensionsExtensionNumberOutConfDataByExtensionNumberInfoIdForOtherVoipGw");
    }
    // #834 END
    
    /**
     * 内線番号情報IDから、extensions_外線番号_in.confの作成に必要なデータをセットする。
     * @throws Exception 
     * 
     */
    private void setExtensionsOutGoingNumberInConfDataByExtensionNumberInfoId(
            Connection dbConnection,
            long extensionNumberInfoId,
            List<OutsideCallInfo> outsideCallInfoList,
            List<OutsideCallIncomingInfo> outsideCallIncomingInfoList,
            List<ExtensionNumberInfo> extensionNumberInfoList) throws Exception{
        log.trace("method start :setExtensionsOutGoingNumberInConfDataByExtensionNumberInfoId");
        log.trace("method param :extensionNumberInfoId = " + extensionNumberInfoId);

        // 変数宣言
        List<Long> outsideCallInfoIdList = new ArrayList<Long>();

        // 内線番号情報IDから外線着信情報に紐づく外線情報IDのリストを取得
        Result<List<Long>> rsOutsideCallInfoIdList = 
                ConfigCreatorDAO.getInstance().getOutsideCallInfoIdListByExtensionNumberInfoId(extensionNumberInfoId, dbConnection);
        if(rsOutsideCallInfoIdList.getRetCode() == Const.ReturnCode.OK){
            outsideCallInfoIdList = rsOutsideCallInfoIdList.getData();
        }else{
            String msg = "内線番号情報IDから外線情報（外線着信情報）の取得に失敗しました。" 
                    + "  内線番号情報ID = " + extensionNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        // 取得した外線情報IDごとに、引数にデータをセット
        Iterator<Long> itrOutsideCallInfoId = outsideCallInfoIdList.iterator();
        while(itrOutsideCallInfoId.hasNext()){            
            // 外線情報IDから、extensions_外線番号_in.confファイル生成に必要なデータをセット
            setExtensionsOutgoingNumberInConfDataByOutsideCallInfoId(
                    dbConnection,
                    itrOutsideCallInfoId.next(),
                    outsideCallInfoList,
                    outsideCallIncomingInfoList,
                    extensionNumberInfoList);
        }

        log.trace("method end :setExtensionsOutGoingNumberInConfDataByExtensionNumberInfoId");
    }

    /**
     * N番情報IDから、sip_user.confの作成に必要なデータをセットする。
     * @throws Exception 
     * 
     */
    private void setSipUserConfDataByNNumberInfoId(
            Connection dbConnection,
            long NNumberInfoId,
            List<ExtensionNumberInfo> extensionNumberInfoList) throws Exception{
        log.trace("method start :setSipUserConfDataByNNumberInfoId");
        log.trace("method param :NNumberInfoId = " + NNumberInfoId);

        // 変数宣言
        List<ExtensionNumberInfo> extensionNumberInfoListLocal = new ArrayList<ExtensionNumberInfo>();

        // N番情報IDから内線番号情報を取得
        Result<List<ExtensionNumberInfo>> rsExtensionNumberInfoList = 
                ConfigCreatorDAO.getInstance().getExtensionNumberInfoListByNNumberInfoId(NNumberInfoId, dbConnection);
        
        if(rsExtensionNumberInfoList.getRetCode() == Const.ReturnCode.OK){
            extensionNumberInfoListLocal = rsExtensionNumberInfoList.getData();
        }else{
            String msg = "N番情報IDから内線番号情報の取得に失敗しました。 " 
                    + "N番情報ID = " + NNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        
        // #1334 start
        if (rsExtensionNumberInfoList.getData() == null) {
            String msg = "N番情報IDから内線番号情報の取得に失敗しました。 " 
                    + "N番情報ID = " + NNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        // #1334 end

        Iterator<ExtensionNumberInfo> itrExtensionNumberInfo = extensionNumberInfoListLocal.iterator();
        while(itrExtensionNumberInfo.hasNext()){            
            // 変数宣言
            ExtensionNumberInfo extensionNumberInfo = itrExtensionNumberInfo.next();

            // 引数にデータをセット
            extensionNumberInfoList.add(extensionNumberInfo);
        }

        log.trace("method end :setSipUserConfDataByNNumberInfoId");
    }

    /**
     * N番情報IDから、extensions_user.confの作成に必要なデータをセットする。
     * @throws Exception 
     * 
     */
    private void setExtensionUserConfDataByNNumberInfoId(
            Connection dbConnection,
            long NNumberInfoId,
            List<ExtensionNumberInfo> extensionNumberInfoList) throws Exception{
        log.trace("method start :setExtensionUserConfDataByNNumberInfoId");
        log.trace("method param :NNumberInfoId = " + NNumberInfoId);

        // 変数宣言
        List<ExtensionNumberInfo> extensionNumberInfoListLocal = new ArrayList<ExtensionNumberInfo>();

        // N番情報IDから内線番号情報を取得
        Result<List<ExtensionNumberInfo>> rsExtensionNumberInfoList = 
                ConfigCreatorDAO.getInstance().getExtensionNumberInfoListByNNumberInfoId(NNumberInfoId, dbConnection);
        
        if(rsExtensionNumberInfoList.getRetCode() == Const.ReturnCode.OK){
            extensionNumberInfoListLocal = rsExtensionNumberInfoList.getData();
        }else{
            String msg = "N番情報IDから内線番号情報の取得に失敗しました。 " 
                    + "N番情報ID = " + NNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        
        // #1334 start
        if (rsExtensionNumberInfoList.getData() == null) {
            String msg = "N番情報IDから内線番号情報の取得に失敗しました。 " 
                    + "N番情報ID = " + NNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        // #1334 end

        Iterator<ExtensionNumberInfo> itrExtensionNumberInfo = extensionNumberInfoListLocal.iterator();
        while(itrExtensionNumberInfo.hasNext()){            
            // 変数宣言
            ExtensionNumberInfo extensionNumberInfo = itrExtensionNumberInfo.next();

            // 引数にデータをセット
            extensionNumberInfoList.add(extensionNumberInfo);
        }

        log.trace("method end :setExtensionUserConfDataByNNumberInfoId");
    }


    /**
     * N番情報IDから、voicemail.confの作成に必要なデータをセットする。
     * @throws Exception      
     * 
     */
    private void setVoiceMailConfDataByNNumberInfoId(
            Connection dbConnection,
            long nNumberInfoId,
            List<ExtensionNumberInfo> extensionNumberInfoList,
            List<AbsenceBehaviorInfo> absenceBehaviorInfoList) throws Exception{
        log.trace("method start :setVoiceMailConfDataByNNumberInfoId");
        log.trace("method param :nNumberInfoId = " + nNumberInfoId);

        // 変数宣言
        List<ExtensionNumberInfo> extensionNumberInfoListLocal = new ArrayList<ExtensionNumberInfo>();

        // N番情報IDから（VoIP-GW以外の）内線番号情報を取得
        Result<List<ExtensionNumberInfo>> rsExtensionNumberInfoList = 
                ConfigCreatorDAO.getInstance().getExtensionNumberInfoWithoutRtAriByNNumberInfoId(nNumberInfoId, dbConnection);
                
        if(rsExtensionNumberInfoList.getRetCode() == Const.ReturnCode.OK){
            extensionNumberInfoListLocal = rsExtensionNumberInfoList.getData();
        }else{
            String msg = "N番情報IDから内線番号情報の取得に失敗しました。 " 
                    + "N番情報ID = " + nNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }        

        // 取得した内線番号情報ごとに、不在時動作情報にデータをセット
        Iterator<ExtensionNumberInfo> itrExtensionNumberInfo = extensionNumberInfoListLocal.iterator();
        while(itrExtensionNumberInfo.hasNext()){            
            // 変数宣言
            AbsenceBehaviorInfo absenceBehaviorInfo = new AbsenceBehaviorInfo();        
            ExtensionNumberInfo extensionNumberInfo = itrExtensionNumberInfo.next();

            // 内線情報IDから不在時動作情報を取得
            Result<AbsenceBehaviorInfo> rsAbsenceBehaviorInfo = 
                    ConfigCreatorDAO.getInstance().getAbsenceBehaviorInfoById(extensionNumberInfo.getExtensionNumberInfoId(), dbConnection);
            if(rsAbsenceBehaviorInfo.getRetCode() == Const.ReturnCode.OK){
                absenceBehaviorInfo = rsAbsenceBehaviorInfo.getData();
            }else{
                String msg = "内線番号情報IDから不在時動作情報の取得に失敗しました。" 
                        + "  内線番号情報ID = " + extensionNumberInfo.getExtensionNumberInfoId();
                log.error(msg);
                throw new IllegalArgumentException(msg);
            }

            // 引数にデータをセット
            extensionNumberInfoList.add(extensionNumberInfo);
            absenceBehaviorInfoList.add(absenceBehaviorInfo);
        }

        log.trace("method end :setVoiceMailConfDataByNNumberInfoId");
    }

    /**
     * N番情報IDからデータを取得し、extensions_globals.confの作成に必要なデータをセットする。
     * @throws Exception 
     * 
     */
    private void setExtensionsGlobalsConfDataByNNumberInfoId(
            Connection dbConnection,
            long nNumberInfoId,
            List<CallRegulationInfo> callRegulationInfoList) throws Exception{
        log.trace("method start :setExtensionsGlobalsConfDataByNNumberInfoId");
        log.trace("method param :nNumberInfoId = " + nNumberInfoId);

        // N番情報IDから発信規制情報を取得
        Result<CallRegulationInfo> rsCallRegulationInfo = 
                ConfigCreatorDAO.getInstance().getCallRegulationInfoByNNumberInfoId(nNumberInfoId, dbConnection);
        if(rsCallRegulationInfo.getRetCode() == Const.ReturnCode.OK){
            callRegulationInfoList.add(rsCallRegulationInfo.getData()); 
        }else{
            String msg = "N番情報IDから発信規制情報の取得に失敗しました。 " 
                    + "N番情報ID = " + nNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        log.trace("method end :setExtensionsGlobalsConfDataByNNumberInfoId");
    }
    
    // #768 START  (extensions_group.conf)
    /**
     * N番情報IDからデータを取得し、extensions_group.confの作成に必要なデータをセットする。
     * @throws Exception 
     * 
     */
    private void setExtensionsGroupConfDataByNNumberInfoId(
            Connection dbConnection,
            long nNumberInfoId,
            List<IncomingGroupInfo> incomingGroupInfoListOfCallPickUp,
            List<List<String>> childExtensionNumberListExtGroup ) throws Exception{
        log.trace("method start :setExtensionsGroupConfDataByNNumberInfoId");
        log.trace("method param :nNumberInfoId = " + nNumberInfoId);
        
        // 変数を宣言
        List<IncomingGroupInfo> incomingGroupInfoListOfLocal = new ArrayList<IncomingGroupInfo>();
        
        
        // N番情報IDから着信グループ情報(コールピック)のリストを取得
        Result<List<IncomingGroupInfo>> rsIncGroupList = 
                ConfigCreatorDAO.getInstance().getPickUpGroupListByNNumberInfoId(
                        nNumberInfoId,
                        dbConnection );
        
        if(rsIncGroupList.getRetCode() == Const.ReturnCode.OK){
            incomingGroupInfoListOfLocal = rsIncGroupList.getData();
        }else{
            throw new Exception("N番情報IDから着信グループ情報に失敗しました。N番情報ID=" + nNumberInfoId );
        }
        
        // 取得したグループ情報のリストから子番号のリストを取得
        Iterator<IncomingGroupInfo> itrIncomingGroupInfo = incomingGroupInfoListOfLocal.iterator();
        while( itrIncomingGroupInfo.hasNext() ){
            
            // 取得したグループ着信情報の取得
            IncomingGroupInfo incomingGroupInfo = itrIncomingGroupInfo.next();
            
            // グループ番号配下の子番号の取得
            List<String> childExtensionNumberList = new ArrayList<String>();
            if(incomingGroupInfo != null){

                // 着信グループ情報に紐づく、グループ子番号の内線番号情報を取得
                Result<List<String>> rsExtensionNumberInfoList = 
                        ConfigCreatorDAO.getInstance().getGroupChildNumber(
                                incomingGroupInfo.getIncomingGroupInfoId(),
                                dbConnection );
                
                if(rsExtensionNumberInfoList.getRetCode() == Const.ReturnCode.OK){
                    childExtensionNumberList = rsExtensionNumberInfoList.getData();
                }else{
                    throw new Exception("着信グループ情報IDから着信グループ配下の子番号の取得に失敗しました。"
                            + "着信グループ情報ID=" + incomingGroupInfo.getIncomingGroupInfoId() );
                }            
            }
            
            // 引数にデータをセット
            incomingGroupInfoListOfCallPickUp.add(incomingGroupInfo);
            childExtensionNumberListExtGroup.add(childExtensionNumberList);
            
        } //while

        log.trace("method end :setExtensionsGroupConfDataByNNumberInfoId");
    }
    // #768 END    (extensions_group.conf)

    /**
     * N番情報IDからsip.confファイル生成に必要なデータをセットする。
     * @throws Exception 
     * 
     */
    private void setSipConfDataByNNumberInfoId(
            Connection dbConnection,
            long nNumberInfoId,
            List<VmInfo> vmInfoList,
            List<OutsideCallInfo> outsideCallInfoList) throws Exception{
        log.trace("method start :setSipConfDataByNNumberInfoId");
        log.trace("method param :nNumberInfoId = " + nNumberInfoId);

        // 変数宣言
        List<OutsideCallInfo> OutsideCallInfoListLocal = new ArrayList<OutsideCallInfo>();

        // N番IDからVM情報を取得
        Result<VmInfo> rsVmInfo = 
                ConfigCreatorDAO.getInstance().getVmInfoByNNumberInfoId(nNumberInfoId, dbConnection);
        if(rsVmInfo.getRetCode() == Const.ReturnCode.OK){
            // 引数にデータをセット
            vmInfoList.add(rsVmInfo.getData());
        }else{
            String msg = "N番情報IDからVM情報の取得に失敗しました。 " 
                    + "N番情報ID = " + nNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        // 外線情報を取得(N番IDから一覧を)
        Result<List<OutsideCallInfo>> rsOutsideCallInfoList = 
                ConfigCreatorDAO.getInstance().getOutsideCallInfoListByNNumberInfoId(nNumberInfoId, dbConnection);
        if(rsOutsideCallInfoList.getRetCode() == Const.ReturnCode.OK){
            OutsideCallInfoListLocal = rsOutsideCallInfoList.getData();
        }else{
            String msg = "N番情報IDから外線情報の取得に失敗しました。 " 
                    + "N番情報ID = " + nNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        Iterator<OutsideCallInfo> itrOutsideCallInfo = OutsideCallInfoListLocal.iterator();
        while(itrOutsideCallInfo.hasNext()){            
            // 変数宣言
            OutsideCallInfo outsideCallInfo = itrOutsideCallInfo.next();

            // #770 START
            // 取得した外線番号情報がIP-Voiceかつ、追加番号の場合
            // IP-Voiceかつ同じアクセス回線種別の基本番号の回線を使用するため、生成対象としない
            if( outsideCallInfo.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX
                    && true == outsideCallInfo.getAddFlag() ){
                log.trace(" skip add list, becuase this outside-number is not Bace Number Of IP-Voice.");
                continue;
            }
            // #770 END

            // 引数にデータをセット
            outsideCallInfoList.add(outsideCallInfo);
        }

        log.trace("method end :setSipConfDataByNNumberInfoId");
    }

    /**
     * N番情報IDからextensions_out.confファイル生成に必要なデータをセットする。
     * @throws Exception
     * 
     */
    private void setExtensionsOutConfByNNumberInfoId(
            Connection dbConnection,
            long nNumberInfoId,
            List<OutsideCallInfo> outsideCallInfoList) throws Exception{ 
        log.trace("method start :setExtensionsOutConfByNNumberInfoId");
        log.trace("method param :nNumberInfoId = " + nNumberInfoId);

        // 変数宣言
        List<OutsideCallInfo> OutsideCallInfoListLocal = new ArrayList<OutsideCallInfo>();

        // 外線情報を取得(N番IDに紐づき、着信内線番号設定がされている一覧を取得する)
        Result<List<OutsideCallInfo>> rsOutsideCallInfoList = 
                ConfigCreatorDAO.getInstance().getOutsideCallInfoListWithExtByNNumberInfoId(nNumberInfoId, dbConnection);
        if(rsOutsideCallInfoList.getRetCode() == Const.ReturnCode.OK){
            OutsideCallInfoListLocal = rsOutsideCallInfoList.getData();
        }else{
            String msg = "N番情報IDから外線情報の取得に失敗しました。 " 
                    + "N番情報ID = " + nNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        Iterator<OutsideCallInfo> itrOutsideCallInfo = OutsideCallInfoListLocal.iterator();
        while(itrOutsideCallInfo.hasNext()){            
            // 変数宣言
            OutsideCallInfo outsideCallInfo = itrOutsideCallInfo.next();

            // 引数にデータをセット
            outsideCallInfoList.add(outsideCallInfo);
        }

        log.trace("method end :setExtensionsOutConfByNNumberInfoId");
    }

    /**
     * 外線情報IDから、sip_外線番号.confファイル生成に必要なデータをセットする。
     * @throws Exception
     * 
     */
    private void setSipOutgoingNumberConfDataByOutsideCallInfoId(
            Connection dbConnection,
            long outsideCallInfoId,
            List<OutsideCallInfo> outsideCallInfoList) throws Exception{
        log.trace("method start :setSipOutgoingNumberConfDataByOutsideCallInfoId");
        log.trace("method param :outsideCallInfoId = " + outsideCallInfoId);

        // 変数宣言
        OutsideCallInfo outsideCallInfo = new OutsideCallInfo();

        // 外線情報IDから外線情報を取得
        Result<OutsideCallInfo> rsOutsideCallInfo = 
                ConfigCreatorDAO.getInstance().getOutsideCallInfoById(outsideCallInfoId, dbConnection);
        if(rsOutsideCallInfo.getRetCode() == Const.ReturnCode.OK){
            
            // #1334 start
            if (rsOutsideCallInfo.getData() == null) {
                String msg = "外線情報IDから外線情報の取得に失敗しました。 " 
                        + "外線情報ID = " + outsideCallInfoId;
                log.error(msg);
                throw new IllegalArgumentException(msg);
            }
            // #1334 end
            
            outsideCallInfo = rsOutsideCallInfo.getData();

            // #770 START
            // 取得した外線番号情報がIP-Voiceかつ、追加番号の場合
            // IP-Voiceかつ同じアクセス回線種別の基本番号の回線を使用するため、ファイル生成対象としない
            if( outsideCallInfo.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX
                    && true == outsideCallInfo.getAddFlag() ){
                log.trace(" skip add list, becuase this outside-number is not Bace Number Of IP-Voice.");
                log.trace("method end :setSipOutgoingNumberConfDataByOutsideCallInfoId");
                return;
            }
            // #770 END

        }else{
            String msg = "外線情報IDから外線情報の取得に失敗しました。 " 
                    + "外線情報ID = " + outsideCallInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        // 引数にデータをセット
        outsideCallInfoList.add(outsideCallInfo);     

        log.trace("method end :setSipOutgoingNumberConfDataByOutsideCallInfoId");
    }

    /**
     * 外線情報IDから、sip_reg外線番号.confファイル生成に必要なデータをセットする。
     * @throws Exception
     * 
     */
    private void setSipRegOutgoingNumberConfDataByOutsideCallInfoId(
            Connection dbConnection,
            long outsideCallInfoId,
            List<OutsideCallInfo> outsideCallInfoList) throws Exception{
        log.trace("method start :setSipRegOutgoingNumberConfDataByOutsideCallInfoId");
        log.trace("method param :outsideCallInfoId = " + outsideCallInfoId);

        // 変数宣言
        OutsideCallInfo outsideCallInfo = new OutsideCallInfo();

        // 外線情報IDから外線情報を取得
        Result<OutsideCallInfo> rsOutsideCallInfo = 
                ConfigCreatorDAO.getInstance().getOutsideCallInfoById(outsideCallInfoId, dbConnection);
        if(rsOutsideCallInfo.getRetCode() == Const.ReturnCode.OK){
            
            // #1334 start
            if (rsOutsideCallInfo.getData() == null) {
                String msg = "外線情報IDから外線情報の取得に失敗しました。 " 
                        + "外線情報ID = " + outsideCallInfoId;
                log.error(msg);
                throw new IllegalArgumentException(msg);
            }
            // #1334 end
            
            outsideCallInfo = rsOutsideCallInfo.getData();

            // #770 START
            // 取得した外線番号情報がIP-Voiceかつ、追加番号の場合
            // IP-Voiceかつ同じアクセス回線種別の基本番号の回線を使用するため、ファイル生成対象としない
            if( outsideCallInfo.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX
                    && true == outsideCallInfo.getAddFlag() ){
                log.trace(" skip add list, becuase this outside-number is not Bace Number Of IP-Voice.");
                log.trace("method end :setSipRegOutgoingNumberConfDataByOutsideCallInfoId");
                return;
            }
            // #770 END

        }else{
            String msg = "外線情報IDから外線情報の取得に失敗しました。 " 
                    + "外線情報ID = " + outsideCallInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        // 引数にデータをセット
        outsideCallInfoList.add(outsideCallInfo);  

        log.trace("method end :setSipRegOutgoingNumberConfDataByOutsideCallInfoId");
    }

    /**
     * 外線情報IDから、extensions_外線番号_in.confファイル生成に必要なデータをセットする。
     * @throws Exception
     * 
     */
    private void setExtensionsOutgoingNumberInConfDataByOutsideCallInfoId(
            Connection dbConnection,
            long outsideCallInfoId,
            List<OutsideCallInfo> outsideCallInfoList,
            List<OutsideCallIncomingInfo> outsideCallIncomingInfoList,
            List<ExtensionNumberInfo> extensionNumberInfoList) throws Exception{
        log.trace("method start :setExtensionsOutgoingNumberInConfDataByOutsideCallInfoId");
        log.trace("method param :outsideCallInfoId = " + outsideCallInfoId);

        // 変数宣言
        ExtensionNumberInfo extensionNumberInfo = new ExtensionNumberInfo();
        OutsideCallInfo outsideCallInfo = new OutsideCallInfo();
        OutsideCallIncomingInfo outsideCallIncomingInfo = new OutsideCallIncomingInfo();

        // 外線情報IDから外線情報を取得
        Result<OutsideCallInfo> rsOutsideCallInfo = 
                ConfigCreatorDAO.getInstance().getOutsideCallInfoById(outsideCallInfoId, dbConnection);
        if(rsOutsideCallInfo.getRetCode() == Const.ReturnCode.OK){
            outsideCallInfo = rsOutsideCallInfo.getData();
        }else{
            String msg = "外線情報IDから外線情報の取得に失敗しました。 " 
                    + "外線情報ID = " + outsideCallInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        
        // #1334 start
        if (rsOutsideCallInfo.getData() == null) {
            String msg = "外線情報IDから外線情報の取得に失敗しました。 " 
                    + "外線情報ID = " + outsideCallInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        // #1334 end

        // 外線情報IDから外線着信情報を取得
        Result<OutsideCallIncomingInfo> rsOutsideCallIncomingInfo = 
                ConfigCreatorDAO.getInstance().getOutsideCallIncomingInfoByOutsideCallInfoId(outsideCallInfoId, dbConnection);
        if(rsOutsideCallIncomingInfo.getRetCode() == Const.ReturnCode.OK){
            outsideCallIncomingInfo = rsOutsideCallIncomingInfo.getData();
        }else{
            String msg = "外線情報IDから外線着信情報の取得に失敗しました。 " 
                    + "外線情報ID = " + outsideCallInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        // 外線情報IDから外線着信情報に紐づく内線番号情報を取得
        // (なければNULLが取得される)
        Result<ExtensionNumberInfo> rsExtensionNumberInfo = 
                ConfigCreatorDAO.getInstance().getExtensionNumberInfoWithExtByOutsideCallInfoId(outsideCallInfoId, dbConnection);
        if(rsExtensionNumberInfo.getRetCode() == Const.ReturnCode.OK){
            extensionNumberInfo = rsExtensionNumberInfo.getData();
        }else{
            String msg = "外線情報IDから外線着信情報に紐づく内線番号情報の取得に失敗しました。 " 
                    + "外線情報ID = " + outsideCallInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        // 引数にデータをセット
        outsideCallInfoList.add(outsideCallInfo);       
        outsideCallIncomingInfoList.add(outsideCallIncomingInfo);
        extensionNumberInfoList.add(extensionNumberInfo);

        log.trace("method end :setExtensionsOutgoingNumberInConfDataByOutsideCallInfoId");
    }


    /**
     * 外線発信情報IDからデータを取得し、extensions_内線番号_out.confの作成に必要なデータをセットする。
     * @throws Exception
     * 
     */
    //  #997 START (引数の追加)
    private void setExtensionsExtensionNumberOutConfDataByOutsideCallSendingInfoId(
            Connection dbConnection,
            long outsideCallSendingInfoId,
            List<ExtensionNumberInfo> extensionNumberInfoList,
            List<OutsideCallInfo> outsideCallInfoList,
            List<OutsideCallInfo> outsideCallInfoForDialInList
            ) throws Exception{
        // #997 END
        log.trace("method start :setExtensionsExtensionNumberOutConfDataByOutsideCallSendingInfoId");
        log.trace("method param :outsideCallSendingInfoId = " + outsideCallSendingInfoId);

        // 変数宣言
//        ExtensionNumberInfo extensionNumberInfo = new ExtensionNumberInfo();
//        OutsideCallInfo outsideCallInfo = new OutsideCallInfo();
//        // #997 START
//        OutsideCallInfo outsideCallInfoForDialIn = new OutsideCallInfo();
        // #997 END

        // #1340 start
        ExtensionNumberInfo extensionNumberInfo = null;
        OutsideCallInfo outsideCallInfo = null;
        OutsideCallInfo outsideCallInfoForDialIn = null;
        // #1340 end
        
        // 外線発信情報IDから外線情報を取得
        Result<OutsideCallInfo> rsOutsideCallInfo = 
                ConfigCreatorDAO.getInstance().getOutsideCallInfoByOutsideCallSendingInfoId(outsideCallSendingInfoId, dbConnection);
        
        if(rsOutsideCallInfo.getRetCode() == Const.ReturnCode.OK){
            outsideCallInfo = rsOutsideCallInfo.getData();
            // #997 START
            outsideCallInfoForDialIn = rsOutsideCallInfo.getData();
            // #997 END

            // #1340 start
            if (outsideCallInfo != null) {
                // #770 START
                // 取得した外線番号情報がIP-Voiceかつ、追加番号の場合
                // IP-Voiceかつ同じアクセス回線種別の基本番号の外線番号情報を取得する
                if( outsideCallInfo.getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.IP_VOICE_FOR_SMARTPBX
                        && true == outsideCallInfo.getAddFlag() ){
                    log.debug("try to get Bace Number Of IP-Voice.");
                    rsOutsideCallInfo = ConfigCreatorDAO.getInstance().getOutsideCallInfoForBaceNumberOfIpVoice(
                            outsideCallInfo.getFkNNumberInfoId(),
                            outsideCallInfo.getOutsideCallLineType(),
                            dbConnection);
                    if(rsOutsideCallInfo.getRetCode() == Const.ReturnCode.OK){
                        outsideCallInfo = rsOutsideCallInfo.getData();
                    }else{
                        throw new Exception("IP-Voice外線の基本番号の取得に失敗しました。");
                    }
                }
                // #770 END
            }
            // #1340 start
            
        }else{
            String msg = "外線発信情報IDから外線情報の取得に失敗しました。 " 
                    + "外線発信情報ID = " + outsideCallSendingInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        // 外線発信情報IDから内線番号情報を取得
        Result<ExtensionNumberInfo> rsExtensionNumberInfo = 
                ConfigCreatorDAO.getInstance().getExtensionNumberInfoByOutsideCallSendingInfoId(outsideCallSendingInfoId, dbConnection);
        if(rsExtensionNumberInfo.getRetCode() == Const.ReturnCode.OK){
            extensionNumberInfo = rsExtensionNumberInfo.getData();
        }else{
            String msg = "外線発信情報IDから内線情報の取得に失敗しました。 " 
                    + "外線発信情報ID = " + outsideCallSendingInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        // 引数にデータをセット
        extensionNumberInfoList.add(extensionNumberInfo);
        outsideCallInfoList.add(outsideCallInfo);  
        // #997 START
        outsideCallInfoForDialInList.add(outsideCallInfoForDialIn);
        // #997 END

        log.trace("method end :setExtensionsExtensionNumberOutConfDataByOutsideCallSendingInfoId");
    }


    /**
     * 共通：N番情報IDからN番情報を返す。
     * @throws Exception
     * 
     */
    private NNumberInfo getNNumberInfo(Connection dbConnection, long nNumberInfoId) throws Exception{
        log.trace("method start :getNNumberInfo");
        log.trace("method param :nNumberInfoId = " + nNumberInfoId);

        // N番情報IDからN番情報を取得
        Result<NNumberInfo> rsNNumberInfo =
                ConfigCreatorDAO.getInstance().getNNumberInfoById(nNumberInfoId, dbConnection);
        
        // #1334 start
        if (rsNNumberInfo.getData() == null) {
            String msg = "N番情報IDからN番情報の取得に失敗しました。 "
                    + "N番情報ID = " + nNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        // #1334 end
        
        if(rsNNumberInfo.getRetCode() == Const.ReturnCode.OK){
            return rsNNumberInfo.getData();
        }else{
            String msg = "N番情報IDからN番情報の取得に失敗しました。 "
                    + "N番情報ID = " + nNumberInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 共通：外線番号IDから外線番号を返す。
     * @throws Exception
     * 
     */
    private String getOutsideCallNumber(Connection dbConnection, long outsideCallInfoId) throws Exception{
        log.trace("method start :getOutsideCallNumber");
        log.trace("method param :outsideCallInfoId = " + outsideCallInfoId);

        // 外線情報IDから外線情報を取得
        Result<OutsideCallInfo> rsOutsideCallInfo = 
                ConfigCreatorDAO.getInstance().getOutsideCallInfoById(outsideCallInfoId, dbConnection);
        
        // #1334 start
        if (rsOutsideCallInfo.getData() == null) {
            String msg = "外線情報IDから外線情報の取得に失敗しました。 " 
                    + "外線情報ID = " + outsideCallInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        // #1334 end
        
        if(rsOutsideCallInfo.getRetCode() == Const.ReturnCode.OK){
            // 取得できれば、外線番号を返す。
            return rsOutsideCallInfo.getData().getOutsideCallNumber();
        }else{
            String msg = "外線情報IDから外線情報の取得に失敗しました。 " 
                    + "外線情報ID = " + outsideCallInfoId;
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        
    }

//    商用バグの対処により未使用化
//    /**
//     * 共通：外線情報IDから着信内線番号が設定されているかを返す。
//     * @throws Exception
//     * 
//     */
//    private boolean incomingExtensionNumberExist(Connection dbConnection, long outsideCallInfoId) throws Exception{
//        log.trace("method start :incomingExtensionNumberExist");
//        log.trace("method param :outsideCallInfoId = " + outsideCallInfoId);
//
//        // 変数宣言
//        ExtensionNumberInfo extensionNumberInfo = new ExtensionNumberInfo();
//
//        // 外線情報IDから外線着信情報に紐づく内線番号情報を取得
//        // (なければNULLが取得される)
//        Result<ExtensionNumberInfo> rsExtensionNumberInfo = 
//                ConfigCreatorDAO.getInstance().getExtensionNumberInfoWithExtByOutsideCallInfoId(outsideCallInfoId, dbConnection);
//        if(rsExtensionNumberInfo.getRetCode() == Const.ReturnCode.OK){
//            extensionNumberInfo = rsExtensionNumberInfo.getData();
//        }else{
//            throw new Exception("外線情報IDから外線着信情報に紐づく内線番号情報の取得に失敗しました。");
//        }
//
//        // 内線番号情報がNULLであれば"False" NotNULLであれば"True"を返す
//        if(extensionNumberInfo == null){            
//            return false;
//        }
//        else{
//            return true;
//        }
//    }
    
}




//(C) NTT Communications  2013  All Rights Reserved
