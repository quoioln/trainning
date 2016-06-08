=================================================
  ■ERMaster用のJavaプロジェクトについて
=================================================

■はじめに
このフォルダは、SmartPBXカスコン開発における
ERMasterのファイル(DB設計書)を管理するためのEclipse向けプロジェクトです。


■.ermファイルを編集する方法
.ermファイルを編集するには、Eclipse上で.ermファイルを開く必要があります。
EclipseにはERMAsterのプラグインが導入されている必要があります。
（※導入方法は、ERMasterについてを参照）


編集する場合は、このフォルダごとEclipseにインポートして下さい。

なお、本フォルダがSVNのリポジトリ上にある場合、以下のいずれかの方法で行って下さい。
・このフォルダを指定して、Ecliseでチェックアウトする。
  （EclipseにSVNを扱うプラグインを導入すること。）
・このフォルダをSVNからエクスポートして、Eclipseにインポートする。


■.xlsファイルを出力する方法
.ermを開いた画面（ビュー）上で右クリックし、[エクスポート]→[Excel]を選択して下さい。

テンプレートファイルは本プロジェクト内にある以下を利用して下さい。
  template\SmartPBX_DB設計書テンプレート.xls

出力先ファイルは、本プロジェクト内と指定います。
SVN上の設計書にマージを行って下さい。
（※SVN上の.xlsファイルを上書いてコミットは行わないこと。）



■参考：ERMasterについて（About "ERMaster"）
※インストール方法、使用方法が丁寧に解説されています。

（English）
http://ermaster.sourceforge.net/
（日本語）
http://ermaster.sourceforge.net/index_ja.html


■DDL作成方法

FD終了後、できればMまでに速やかに実施する。
作成するのは以下。
・[CREATE_TABLE_Step(N-1).sql]
    前のStepのDDL(既存のカラムのCREATE TABLEのみ)
・[ALTER_TABLE_StepN.sql]
    開発中のStepのDDL(追加したテーブル分のCREATE TABLE、追加したカラムのALTER TABLE)
    F更手順で使用する。

以下の手順を前回のStepと今回のStepで実施する。

0.【前提】
・EclipseにSVN関連のプラグインをインストール済み
・EclipseにER Masterがインストール済み
・Eclipseに以下のプロジェクトをチェックアウト済み(前のStep＆今回のStep)

1. 「機能設計書 [別紙３]データベース設計書.erm」をEclipse上で開く

2. ermファイルの編集画面（ER図が出ている）で、右クリック→エクスポート→DDL を選択

   ＜入力値＞
   出力ファイル：機能設計書 [別紙３]データベース設計書.sql 
                 ※とりあえず
   ファイルエンコード：UTF-8
   改行コード：LF   ★★デフォルトから変更
   DROP：全てチェック
   CREATE：全てチェック
   ファイル保存後に開く：チェックを外しておいたほうが良い
   
   上記を設定し[OK]ボタンを押下

   プロジェクトフォルダ直下に以下が出力されている。
      {プロジェクトフォルダ}\機能設計書 [別紙３]データベース設計書.sql

3. 出力されたファイルをコピーしファイルを作成する

[CREATE_TABLE_Step(N-1).sql]の場合
前回のStepで出力したものであればそのままで良い。
（今回のStepで出力した場合、今回の開発分のテーブルとカラムを削除する必要がある。）
※推奨は前のStepで出力したものを使用すること。


[ALTER_TABLE_StepN.sql]の場合
上記ファイルを編集する。編集内容は以下の通り
  ・今回のStep(開発時のStep)で追加したテーブルのCREATE TABLE文のみにする。
  ・今回のStepでALTER TABLE文はCREATEテーブル分を参考に作成する。

作成したファイルを以下にコピーしコミットする。
(/svn/smartpbx-cuscon/)配下の /trunk/{StepN}/30_DD
      StepN・・・例 Step2.9

ローカルに出力された以下は削除する。
・機能設計書 [別紙３]データベース設計書.sql 
・[CREATE_TABLE_Step(N-1).sql]
・[ALTER_TABLE_StepN.sql]

以上
