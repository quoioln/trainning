=================================================
  DDLについて
=================================================
作成物とブランチするファイルを以下に示す。


■作成物

ALTER_TABLE_StepX.X.sql
  →今のStepで変更があるもの"差分"を記載
    DB構成に変更がない場合は作成不要。
    今Stepのermファイルから、今Stepの変更分を抜き出す。
    （WinMergeで前StepのCREATE_TABLEと今StepのCREATE_TABLEをdiffで確認すると良い。）
    ＜ブランチ時＞
    DB変更時に新規作成するファイルなのでブランチしない
    （前のStepからコピーは不要）

CREATE_TABLE_StepX.[X-1].sql
  →前StepのDDL (ユニークインデックスを除く)
    ※ErMasterからエクスポートしたもの
    ＜ブランチ時＞
    前のStepのermファイルから出力したものを配置
    （前のStepからコピーは不要）

CREATE_UNIQUE_INDEX_StepX.[X-1].sql
  →ユニークインデックスのALTER TABLE文を記載したもの。
    ※ErMasterのエクスポート時はユニークインデックスの構文が意図通り出力されないため、このファイルに別途管理している。
    ＜ブランチ時＞
    前Stepの「CREATE_UNIQUE_INDEX」からブランチ。(コピー)
    ユニークインデックスに変更がないかぎりはブランチしたままで良い。
    変更がある場合は手作業で修正する。

=================================================

■今StepのDBを1から作る場合(データ0件)
以下の順番でDDLを全て実施。
  CREATE_TABLE_StepX.[X-1].sql
  CREATE_UNIQUE_INDEX_StepX.[X-1].sql
  ALTER_TABLE_StepX.X.sql

■前のStepのDBを使い回す場合
前のStepのDBに対し、ALTER_TABLE_StepX.X.sqlを実施。
（2つ以上前のStepの場合、間のStepのALTER_TABLEの実施も必要）

=================================================

