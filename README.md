# family_tree


家系図作成ソフト「あのこ だれのこ」
----------------------------------
★職業訓練校の基礎制作（卒業制作）として作成したものを、修了後にブラッシュアップ

【概要】
父系または母系の5代までの親戚一覧を家系図にして表示する。
追加機能：データ保存、テキスト出力


【目標】
1人または1組の親から子・孫・曾孫・玄孫までの代の人名を入力させて、樹形図を出力するプログラム。
●入力の手間が多いので、中断・再開ができるようにデータ保存を可能にする。
●最終出力の樹形図は、テキスト保存を可能にする。
●「5代まで」の制限は、final変数で規定して変更可能とする。（5代までとしたのは、出力テキスト１行の長さを見栄えよくするため）


【備考】
・父系と母系を両方表示しようとすると複雑になりすぎるので、樹形の大本（第1世代）は1人または配偶者との2人のみとする。
・配偶者は1人につき1人とする（前妻・前夫は登録できない）
・一人親に対応
・性別不問（夫婦を並べる際に男性が上ではなく、血族が上とする）
・樹形図表示に使用する罫線がWindows10のコンソールでは表示崩れを起こすためパッチを当てる（ユーザがメニューから選択）。
・コーディング規約は、エスワイITカレッジの規定による。


【目標】
・データの読み書き、Collectionの利用、メソッドの再帰処理 等を行うプログラムの作成を試みた。


【開発環境】
●OS:		Windows10
●言語:		Java Version 8
●Editor:		サクラエディタ（一部Eclipseで下書き）
●文字コード:	Shift-JIS

【想定実行環境】Windows10 のコマンドプロンプト

--------------------------------------------------
【開発の動機】
近年、自分の従弟たちに次々と子供が生まれて、誰が誰だか分からなくなって
きたので、整理する必要があったためにこの題材を選びました！
