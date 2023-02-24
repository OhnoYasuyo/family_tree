/**
 * 家系図作成ソフト「あのこだれのこ」実行クラス
 * 
 * ・引数にcsvファイルを指定するとデータを読み込んで開始
 * ・読込csvファイルの文字コードはUtilクラスの TXT_CODE で指定
 * ・表示テキストおよびユーザ保存／読込ファイルのディレクトリはDataクラス内で指定
 */
package family_tree;

/**
 * @author 大野康世 OHNO Yasuyo
 * @version 2023/01/16
 */
public class FamilyTreeExe {

	/**
	 * 実行メソッド
	 * @param args 実行時読込csvファイル
	 */
	public static void main(String[] args) {
		Util.setPatchSwt(0);	// 罫線パッチを設定  7:Windows7コンソール用 0:default
		Menu.showTitle();	// タイトル表示
		
		boolean isFirstTime = true;		// 初回処理（新規登録未完了）
		boolean goLoadData = false;		// データ読込セクションへ
		boolean goSaveData = false;		// データ保存セクションへ
		boolean goQuit = false;			// 終了セクションへ
		boolean goEdit = true;			// 個人編集セクションへ
		
		// ■起動時の引数にファイル名があったとき
		if(args.length > 0) {
			goLoadData = true;		// データ読込セクションへ
		}
		
		// ■■初期メニュー
		boolean escapeFirstMenu = false;  // 初期メニューから抜けるか
		
		while(!escapeFirstMenu && !goSaveData) {
			if (!goLoadData) {
				switch(Menu.menuFirst()) {	// 【初期メニュー】
				case 1:  // 1.説明書を表示
					Menu.showUsage();
					boolean escapeAfterUsage = false;		// afterUsageを抜けるか
					boolean goBack = false;		// 初期メニューのループへ戻るか
					while(!escapeAfterUsage) {
						switch(Menu.afterUsage()) {	// 【説明書表示後のメニュー】
						case 2:	// 2.新規登録開始
							escapeAfterUsage = true;	// 初期メニュー 2.新規登録へ
							break;
						case 0:	// 0.初期メニューに戻る
							escapeAfterUsage = true;
							goBack = true;
							break;
						default:
							System.out.println("有効なメニュー番号ではありません");
						}
					}
					if(goBack) {
						break;
					}		// goBackがfalseなら、case 2へ
				case 2:  // 2.新規登録
					Menu.showInfoNew();		// 新規登録メニュー
					String title = Menu.inputTitle();	// 家系図タイトル入力
					String founder = Menu.inputFounder();	// 1代目の名前入力
					boolean escapeNew = false;	// 新規登録メニューを抜けるか
					goBack = false;			// 初期メニューのループへ戻るか
					do {
						switch(Menu.confirmNew(title, founder)) {	// 【新規登録確認】
						case 1:		// 1.ＯＫ
							Generation.setStrTitle(title);			// 家系図タイトル設定
							Generation.addFounder(founder);	// 第1世代のmapに1代目を登録
							Menu.rgstrdNew(title);		// 新規登録完了の表示
							escapeNew = true;
							escapeFirstMenu = true;
							break;
						case 2:		// 2.家系図の名前を変更する
							title = Menu.inputNewTitle();	// 家系図タイトル入力
							Generation.setStrTitle(title);
							break;
						case 3:		// 3.１代目の名前を変更する
							founder = Menu.inputFounder();	// 1代目の名前入力
							Generation.setName(1, founder);
							break;
						case 0:		// 0.家系図作成を中止して初期メニューに戻る
							boolean escapeCancel = false;
							do {
								switch(Menu.confirmNewCancel(title, founder)) {	// 新規登録中止確認
								case 0:		// 0.ＯＫ（データを破棄して初期メニューに戻る）
									escapeCancel = true;
									escapeNew = true;
									goBack = true;
									break;
								case 3:		// 3.家系図作成の確認に戻る（作成中止のキャンセル）
									escapeCancel = true;
									goBack = true;
									break;
								default:
									System.out.println("有効なメニュー番号ではありません");
									break;
								}
							} while(!escapeCancel);
							break;
						default:
							System.out.println("有効なメニュー番号ではありません");
							break;
						}
					} while(!escapeNew);
					if(goBack) {
						break;
					}
					break;
				case 3:  // 3.保存データ読込
					goLoadData = true;		// データ読込セクションへ
					break;
				case 7:  // 7.罫線表示の乱れを修正
					Util.setPatchSwt(Menu.selectPatchSwt());	// パッチスイッチの変更メニュー
					break;
				case 9:  // 9.終了
					boolean escapeQuit1 = false;		// 終了セクションを抜けるか
					while(!escapeQuit1) {
						switch(Menu.confirmQuit1()) {	// 終了確認
						case 3:		// 3.データ保存
							goSaveData = true;
							escapeQuit1 = true;
							break;
						case 0:		// 0.初期メニューに戻る
							escapeQuit1 = true;
							break;
						case 9:		// 9.ほんとうに終了する
							Menu.showEndRoll();
							return;		// 実行メソッド終了
						default:
							System.out.println("その番号はありません。正しいメニュー番号を入力してください。");
							break;
						}
					}
					break;
				default:  // 初期メニュー番号以外が入力されたとき
					System.out.println("その番号はありません。正しいメニュー番号を入力してください。");
					break;
				}
			}
			// ■データ読込セクション
			if(goLoadData) {
				String strFilePath = "";	// 初期化
				if(args.length > 0) {		// ■起動時の引数にファイル名があったとき
					strFilePath = args[0];
					System.out.println();
					System.out.println("起動時の引数にデータファイルが指定されました。");
				} else {					// ■初期メニューでデータ読込を選択したとき
					String fileName = Menu.menuLoadData() + ".csv";	// 読込データ名取得（拡張子csvをつける）
					strFilePath = Data.getDataFilePath(fileName).toString();
				}
				boolean escapeLoad = false;
				while (!escapeLoad) {
					switch(Menu.confirmLoadData(strFilePath)) {		// データ読込確認
					case 3:	// 3.はい（データ読込）
						Data.loadData(strFilePath);		// データ読込
						Util.waitEnter();				// 一時停止
						Tree.showTreeHeader();	// 家系図のヘッダを表示
						Tree.showTree(false);		// 家系図を表示
						escapeFirstMenu = true;
						escapeLoad = true;
						isFirstTime = false;
						goEdit = true;
						break;
					case 0: // 0.中止して初期メニューに戻る
						escapeLoad = true;
						break;
					default:
						System.out.println("その番号はありません。正しいメニュー番号を入力してください。");
						break;
					}
				}
				goLoadData = false;
			}
		}
		
		// ■■メインメニュー
		boolean isEnd = false;		// ほんとうに終了するか
		
		while(!isEnd) {
			if(goQuit) {
				// ■終了セクション
				boolean escapeQuit = false;		// 終了セクションを抜けるか
				while(!escapeQuit) {
					switch(Menu.confirmQuit()) {	// 終了確認
								// 3.データ保存　0.メインメニューに戻る　9.ほんとうに終了する
					case 3:		// 3.データ保存
						goSaveData = true;
						escapeQuit = true;
						break;
					case 0:		// 0.メインメニューに戻る
						escapeQuit = true;
						break;
					case 9:		// 9.ほんとうに終了する
						Menu.showEndRoll();
						escapeQuit = true;
						isEnd = true;
						return;		// 実行メソッド終了
					default:
						System.out.println("その番号はありません。正しいメニュー番号を入力してください。");
						break;
					}
				}
			}
			
			// ■メインメニュー本体
			if(!goSaveData) {
				if(!isFirstTime) {
					switch(Menu.menuMain()) {	// メインメニュー
					case 1:		// 1.家系図を表示・保存
						Tree.showTreeHeader();
						Tree.showTree(false);
						switch(Menu.menuSaveText()) {	// 家系図をテキスト保存するか
						case 1:	// 1.保存する
							
							// 確認して保存
							boolean escapeSave = false;
							while (!escapeSave) {
								// ファイル名を家系図名で設定
								String fileName = Generation.getStrTitle();
								
								switch(Menu.confirmSave(fileName, "txt")) {	// 保存の確認
								case 3:		// 3.保存する
									Data.saveText(fileName);
									escapeSave = true;
									goEdit = false;
									break;
								case 5:		// 5.家系図の名前を変更する
									fileName = Menu.inputNewTitle();
									Generation.setStrTitle(fileName);
									break;
								case 9:		// 9.やっぱりやめる
									System.out.println();
									System.out.println("保存を中止します。");
									escapeSave = true;
									break;
								default:	// メニュー番号以外が入力されたとき
									System.out.println("その番号はありません。正しいメニュー番号を入力してください。");
									break;
								}
							}
							break;
						case 0:	// 0.保存しないでメニューに戻る
							goEdit = false;
							break;
						default:
							System.out.println("その番号はありません。正しいメニュー番号を入力してください。");
							break;
						}
						break;
					case 2:		// 2.家族を追加・編集
						goEdit = true;		// 個人編集セクションへ
						break;
					case 3:		// 3.データ保存
						goSaveData = true;		// データ保存セクションへ
						goEdit = false;
						break;
					case 9:		// 9.終了
						goEdit = false;
						goQuit = true;
						break;
					default:  // メインメニュー番号以外が入力されたとき
						System.out.println("その番号はありません。正しいメニュー番号を入力してください。");
						break;
					}
				}
			}
			
			// ■個人編集セクション
			if(goEdit) {
				boolean escapeEdit = false;		// 個人編集セクションを抜けるか
				while(!escapeEdit) {
					int selectID = 1;		// 編集する個人番号を宣言して初期化
					if(!isFirstTime) {
						// IDの存在確認
						boolean escapeSelectID = false;
						while(!escapeSelectID) {
							selectID = Menu.selectID();		// 個人番号セレクタ
							if (selectID == 0) {	// 0 が入力されたらこのwhileループを抜ける
								break;
							}
							boolean isExist = Generation.isExistID(selectID);
							if(isExist) {
								escapeSelectID = true;
								break;
							} else {
								System.out.println();
								System.out.println("【エラー】個人番号" + selectID + "に該当する人はいません。");
							}
						}
					}
					isFirstTime = false;
					// 個人編集メニュー
					boolean escapeEditID = false;		// 別の人の編集へ行くか
					if (selectID == 0) {	// 0 だったらメインメニューに戻る
						escapeEditID = true;
						escapeEdit = true;
						goEdit = false;
						break;
					}
					while(!escapeEditID) {
						
						switch(Menu.menuEdit(selectID)) {	// 個人編集メニュー
						case 1:		// 1.配偶者を追加
							Menu.addPartner(selectID);
							break;
						case 2:		// 2.子供を追加
							Menu.addChild(selectID);
							break;
						case 3:		// 3.名前を編集
							Menu.renName(selectID);
							break;
						case 4:		// 4.この人を削除
							boolean isOK = false;
							while(!isOK) {
								switch(Menu.confirmRemove(selectID)) {		// 削除の確認
								case 1:		// 1.やっぱりやめる
									isOK = true;
									break;
								case 4:		// 4.ほんとうに削除する
									Generation.removeID(selectID);
									isOK = true;
									escapeEditID = true;
									break;
								default:  // メニュー番号以外が入力されたとき
									System.out.println("その番号はありません。"
												+ "正しいメニュー番号を入力してください。");
									break;
								}
							}
							break;
						case 9:		// 9.別の人を選択
							escapeEditID = true;
							break;
						case 0:		// 0.メニューに戻る
							escapeEditID = true;
							escapeEdit = true;
							goEdit = false;
							break;
						default:	// メニュー番号以外が入力されたとき
							System.out.println("その番号はありません。正しいメニュー番号を入力してください。");
							break;
						}
					}
				}
			}
			// ■データ保存セクション
			if(goSaveData) {
				// ファイル名を家系図名で設定
				String fileName = "";	// 初期化
				
				// 確認して保存
				boolean escapeSave = false;
				while(!escapeSave) {
					fileName = Generation.getStrTitle();
					switch(Menu.confirmSave(fileName, "csv")) {	// 保存の確認
					case 3:		// 3.保存する
						Data.saveData(fileName);
						escapeSave = true;
						break;
					case 5:		// 5.家系図の名前を変更する
						fileName = Menu.inputNewTitle();
						Generation.setStrTitle(fileName);
						break;
					case 9:		// 9.やっぱりやめる
						System.out.println();
						System.out.println("保存を中止します。");
						escapeSave = true;
						break;
					default:	// メニュー番号以外が入力されたとき
						System.out.println("その番号はありません。正しいメニュー番号を入力してください。");
						break;
					}
				}
				goSaveData = false;
			}
		}

	}

}
