/**
 * 家系図作成ソフト「あのこだれのこ」
 * メニューとタイトル等の表示クラス
 * （メニューは選択番号をreturn）
 */
package family_tree;

/**
 * @author 大野康世 OHNO Yasuyo
 * @version 2023/01/16
 *
 */
public class Menu {
	// ■タイトル～初期メニュー
	/**
	 * 家系図ソフトのタイトル表示
	 */
	public static void showTitle() {
		System.out.println();
		Util.showTxt("title.txt", "タイトル");
		return;
	}
	/**
	 * 初期メニュー（1.説明を表示　2.新規登録開始　3.保存データ読込　7.罫線の乱れを修正　9.終了）
	 * @return 選択されたメニュー番号
	 */
	public static int menuFirst() {
		System.out.println();
		System.out.println(Util.rulePatch("┌────────┐"));
		System.out.println(Util.rulePatch("│　初期メニュー　│　メニュー番号を入力してください（半角数字）"));
		System.out.println(Util.rulePatch("├────────┴────────────────────────┐"));
		System.out.println(Util.rulePatch("│1.説明を表示　2.新規登録開始　3.保存データ読込　　　　　　　　　　│"));
		System.out.println(Util.rulePatch("│7.罫線の乱れを修正　　　　　　　　　　　　　　　　　　　　9.終了　│"));
		System.out.println(Util.rulePatch("└─────────────────────────────────┘"));
		
		int num = Util.numInput();  // メニュー選択番号の入力受付
		return num;
	}
	/**
	 * 説明書の表示
	 */
	public static void showUsage() {
		System.out.println();
		
		Util.showTxt("usage1.txt", "説明書1");
		Util.waitEnter();	// Enterキーが入力されるまで待つ
		Util.showTxt("usage2.txt", "説明書2");
		
		return;
	}
	/**
	 * 説明書表示後のメニュー（2.新規登録開始　0.初期メニューに戻る）
	 * @return 選択されたメニュー番号
	 */
	public static int afterUsage() {
		System.out.println();
		System.out.println("　メニュー番号を入力してください（半角数字）");
		System.out.println(Util.rulePatch("┌─────────────────────────────────┐"));
		System.out.println(Util.rulePatch("│2.新規登録開始　0.初期メニューに戻る　　　　　　　　　　　　　　　│"));
		System.out.println(Util.rulePatch("└─────────────────────────────────┘"));
		
		int num = Util.numInput();  // メニュー選択番号の入力受付
		return num;
	}
	/**
	 * 新規登録の説明文の表示
	 */
	public static void showInfoNew() {
		System.out.println();
		
		String fileName = "infoNew.txt";
		Util.showTxt(fileName, "新規登録の説明");
		
		return;
	}
	/**
	 * 家系図タイトル入力
	 * @return 家系図の名前
	 */
	public static String inputTitle() {
		System.out.println();
		System.out.println("■家系図に名前をつけてください。　例：「鈴木家の家系図」");
		String title = Util.strInput();  // 文字列の入力受付
		
		if(title == "") {		// 未入力だったらダミーを入れる
			title = "○○家の家系図";
			System.out.println("家系図の名前が入力されなかったので「" + title + "」で登録します。");
		}
		
		return title;
	}
	/**
	 * 家系図タイトル変更
	 * @return 家系図の新しい名前
	 */
	public static String inputNewTitle() {
		System.out.println();
		System.out.println("■家系図の名前を変更します。　現在の名前：「" + Generation.getStrTitle() + "」");
		System.out.println("　新しい名前をつけてください。　例：「" + Generation.getStrTitle() + "２」");
		String title = Util.strInput();  // 文字列の入力受付
		
		if(title == "") {		// 未入力だったらダミーを入れる
			title = "○○家の家系図";
			System.out.println("家系図の名前が入力されなかったので「" + title + "」で登録します。");
		}
		
		return title;
	}
	/**
	 * 初代の名前入力
	 * @return １代目の名前
	 */
	public static String inputFounder() {
		System.out.println();
		System.out.println("■１代目の名前を入力してください。（全角文字）　例：「鈴木一郎」");
		String founder = Util.strInput();  // 文字列の入力受付
		
		if(founder == "") {		// 未入力だったらダミーを入れる
			founder = "○○○○";
			System.out.println("名前が入力されなかったので「" + founder + "」さんで登録します。");
		}
		
		return founder;
	}
	/**
	 * 新規登録確認（1.ＯＫ　2.家系図の名前を変更　3.１代目の名前を変更　0.中止して初期メニューに戻る）
	 * @param title 家系図のタイトル
	 * @param founder 1代目の名前
	 * @return 選択されたメニュー番号
	 */
	public static int confirmNew(String title, String founder) {
		System.out.println();
		System.out.println("【確認】家系図の名前：" + title + "　　１代目の名前：" + founder);
		System.out.println("　　　　この設定で家系図を作成します。");
		System.out.println("　　　　よろしいですか？");
		System.out.println();
		System.out.println("　番号を入力してください（半角数字）");
		System.out.println(Util.rulePatch("┌─────────────────────────────────┐"));
		System.out.println(Util.rulePatch("│1.ＯＫ　2.家系図の名前を変更する　3.１代目の名前を変更する　　　　│"));
		System.out.println(Util.rulePatch("│0.家系図作成を中止して初期メニューに戻る　　　　　　　　　　　　　│"));
		System.out.println(Util.rulePatch("└─────────────────────────────────┘"));
		int num = Util.numInput();  // メニュー選択番号の入力受付
		return num;
	}
	/**
	 * 新規登録中止の確認（0.ＯＫ　3.中止をキャンセル）
	 * @return 選択されたメニュー番号
	 * @param 1代目の名前
	 * @return 選択されたメニュー番号
	 */
	public static int confirmNewCancel(String title, String founder) {
		System.out.println();
		System.out.println("【確認】作成を中止すると、これまでに登録されたデータは消えます。");
		System.out.println("　　　　　家系図の名前：" + title + "　　１代目の名前：" + founder);
		System.out.println("　　　　よろしいですか？");
		System.out.println();
		System.out.println("　番号を入力してください（半角数字）");
		System.out.println(Util.rulePatch("┌─────────────────────────────────┐"));
		System.out.println(Util.rulePatch("│0.ＯＫ（データを破棄して初期メニューに戻る）　　　　　　　　　　　│"));
		System.out.println(Util.rulePatch("│3.家系図作成の確認に戻る（作成中止のキャンセル）　　　　　　　　　│"));
		System.out.println(Util.rulePatch("└─────────────────────────────────┘"));
		int num = Util.numInput();  // メニュー選択番号の入力受付
		return num;
	}
	/**
	 * 読込データ名の入力（拡張子「.csv」は不要）
	 * @return データファイル名
	 */
	public static String menuLoadData() {
		System.out.println();
		System.out.println("保存された家系図のcsvデータを読み込みます。");
		System.out.println("■読み込むファイル名を入力してください。（拡張子「.csv」は不要）");
		System.out.println("　ヒント：「鈴木家の家系図」と入力するとサンプルファイルを読み込みます。");
		
		String fileName = Util.strInput();  // 文字列の入力受付
		return fileName;
	}
	/**
	 * データ読込の確認（3.ＯＫ　0.中止して初期メニューに戻る）
	 * @param strFilePath データファイルのパス
	 * @return 選択されたメニュー番号
	 */
	public static int confirmLoadData(String strFilePath) {
		System.out.println();
		System.out.println("【確認】「" + strFilePath + "」を読み込みます。");
		System.out.println("　　　　よろしいですか？");
		System.out.println();
		System.out.println("　番号を入力してください（半角数字）");
		System.out.println(Util.rulePatch("┌─────────────────────────────────┐"));
		System.out.println(Util.rulePatch("│3.ＯＫ（データ読込）　　　　　　　　0.中止して初期メニューに戻る　│"));
		System.out.println(Util.rulePatch("└─────────────────────────────────┘"));
		
		int num = Util.numInput();  // メニュー選択番号の入力受付
		return num;
	}
	/**
	 * 罫線表示の乱れを修正するパッチの指定（7.Windows7コンソール用に変更　0.現状のまま）
	 * @param strFilePath データファイルのパス
	 * @return 選択されたメニュー番号
	 */
	public static int selectPatchSwt() {
		System.out.println();
		System.out.println("罫線表示の乱れを修正します。");
		System.out.println("どちらの罫線がきれいな「田」に見えますか？");
		System.out.println();
		System.out.println("　7.Windows7コンソール用 ↓");
		System.out.println(Util.rulePatch7("┌─┬─┐"));
		System.out.println(Util.rulePatch7("│　│　│"));
		System.out.println(Util.rulePatch7("├─┼─┤"));
		System.out.println(Util.rulePatch7("│　│　│"));
		System.out.println(Util.rulePatch7("└─┴─┘"));
		System.out.println();
		System.out.println("　0.その他用 ↓");
		System.out.println("┌─┬─┐");
		System.out.println("│　│　│");
		System.out.println("├─┼─┤");
		System.out.println("│　│　│");
		System.out.println("└─┴─┘");
		System.out.println();
		System.out.println("　番号を入力してください（半角数字）");
		System.out.println(Util.rulePatch("┌─────────────────────────────────┐"));
		System.out.println(Util.rulePatch("│7.Windows7コンソール用　0.その他用 　　　　　　 　　　　　　　　　│"));
		System.out.println(Util.rulePatch("└─────────────────────────────────┘"));
		
		int num = Util.numInput();  // メニュー選択番号の入力受付
		return num;
	}
	/**
	 * 初期メニューからの終了確認（3.データ保存　0.初期メニューに戻る　9.ほんとうに終了する）
	 * @return 選択されたメニュー番号
	 */
	public static int confirmQuit1() {	// 
		System.out.println();
		System.out.println("【確認】データを保存していないと、これまでに登録されたデータは消えます。");
		System.out.println("　　　　ほんとうに終了しますか？");
		System.out.println();
		System.out.println("　番号を入力してください（半角数字）");
		System.out.println(Util.rulePatch("┌─────────────────────────────────┐"));
		System.out.println(Util.rulePatch("│3.データ保存　0.初期メニューに戻る　　　　　　　　　　　　　　　　│"));
		System.out.println(Util.rulePatch("│　　　　　　　　　　　　　　　　　　　　　　9.ほんとうに終了する　│"));
		System.out.println(Util.rulePatch("└─────────────────────────────────┘"));
		
		int num = Util.numInput();  // メニュー選択番号の入力受付
		return num;
	}
	/**
	 * 新規登録完了の表示
	 * @param 家系図のタイトル
	 */
	public static void rgstrdNew(String title) {
		System.out.println();
		System.out.println(Util.rulePatch("┌─────────────────────────────────┐"));
		System.out.println(Util.rulePatch("│　　　　　　　　　 新しい家系図を作成しました！ 　　　　　　　　　│"));
		System.out.println(Util.rulePatch("└─────────────────────────────────┘"));
		System.out.println("\t■家系図の名前：" + title);
		System.out.println();
		System.out.println("　次のステップ：家族を追加していきましょう！");
		return;
	}
	
	// ■メインメニュー
	/**
	 * メインメニュー（1.家系図を表示・保存　2.家族を追加・編集　3.データ保存　9.終了）
	 * @return 選択されたメニュー番号
	 */
	public static int menuMain() {
		System.out.println();
		System.out.println(Util.rulePatch("┌─────────┐"));
		System.out.println(Util.rulePatch("│　メインメニュー　│　メニュー番号を入力してください（半角数字）"));
		System.out.println(Util.rulePatch("├─────────┴───────────────────────┐"));
		System.out.println(Util.rulePatch("│1.家系図を表示・保存　2.家族を追加・編集　3.データ保存　　9.終了　│"));
		System.out.println(Util.rulePatch("└─────────────────────────────────┘"));
		
		int num = Util.numInput();  // メニュー選択番号の入力受付
		return num;
	}
	/**
	 * テキスト保存の確認（1.保存する　0.保存しないでメニューに戻る）
	 * @return 選択されたメニュー番号
	 */
	public static int menuSaveText() {
		System.out.println();
		System.out.println(Util.rulePatch("┌───────────────────┐"));
		System.out.println(Util.rulePatch("│　この家系図をテキスト保存しますか？　│　番号を入力（半角数字）"));
		System.out.println(Util.rulePatch("├───────────────────┴─────────────┐"));
		System.out.println(Util.rulePatch("│1.保存する　　　　　　　　　　　　　0.保存しないでメニューに戻る　│"));
		System.out.println(Util.rulePatch("└─────────────────────────────────┘"));
		
		int num = Util.numInput();  // メニュー選択番号の入力受付
		return num;
	}
	/**
	 * 家系図を表示して個人番号を入力させる
	 * @return 選択された個人番号
	 */
	public static int selectID() {
		System.out.println();
		Util.showMenuTree("家族を追加・編集");;
		System.out.println("表示されている人の「配偶者または子供を追加」「名前の変更」「削除」ができます。");
		System.out.println("■編集したい人の個人番号を入力してください。（メニューに戻るには 0 を入力）");
		
		int id = Util.numInput();  // 個人番号の入力受付
		return id;
	}
	
	// ■個人編集メニュー
	/**
	 * 個人編集メニュー（1.配偶者を追加　2.子供を追加　3.名前を変更　4.この人を削除　9.別の人を選択　0.メニューに戻る）
	 * @param id 個人ID
	 * @return 選択されたメニュー番号
	 */
	public static int menuEdit(int id) {
		System.out.println();
		Util.showMenuTree("追加・変更・削除");;
		System.out.println("【" + Generation.getIdName(id) + "】　　　メニュー番号を入力してください（半角数字）");
		System.out.println(Util.rulePatch("┌─────────────────────────────────┐"));
		System.out.println(Util.rulePatch("│1.配偶者を追加　2.子供を追加　3.名前を変更　4.この人を削除　　　　│"));
		System.out.println(Util.rulePatch("│9.別の人を選択　　　　　　　　　　　　　　　　　0.メニューに戻る　│"));
		System.out.println(Util.rulePatch("└─────────────────────────────────┘"));
		
		int num = Util.numInput();  // メニュー選択番号の入力受付
		return num;
	}
	/**
	 * 配偶者の名前を入力して追加
	 * @param baseID 編集中の個人ID
	 */
	public static void addPartner(int baseID) {
		if (Generation.chkAddPartner(baseID)) {		// 配偶者を追加できるか判定
			int id = Generation.getPartnerID(baseID);	// 配偶者IDの決定
			
			System.out.println();
			Util.showMenuTree("配偶者を追加");;
			System.out.println("■" + baseID + " " + Generation.getName(baseID) + "さんの配偶者の名前を入力してください。");
			
			String name = Util.strInput();  // 文字列の入力受付
			
			if(name == "") {		// 未入力だったらダミーを入れる
				name = "○○○○";
				System.out.println("名前が入力されなかったので「" + name + "」さんで登録します。");
				System.out.println("「9.別の人を選択」で個人番号 " + id + " を選択すると変更できます。");
			}
			
			Generation.addOne(id, name);	// 配偶者を追加
			System.out.println("　" + id + " " + name + "さんを追加しました");
		}
		return;
	}
	/**
	 * 子の名前を入力して追加
	 * @param baseID 編集中の個人ID
	 */
	public static void addChild(int baseID) {
		int childID = Generation.getNextChildID(baseID);	 // 追加不可のとき 0 でメッセージを表示
		if (childID != 0) {		// 追加不可でないときのみ実行
			System.out.println();
			Util.showMenuTree("子供を追加");
			System.out.println("　" + baseID + " " + Generation.getName(baseID) + "さんに子供を追加します。");
			System.out.println();
			System.out.println("■" + baseID + " " + Generation.getName(baseID) + "さんの子供の名前を入力してください。");
			
			String name = Util.strInput();  // 文字列の入力受付
			
			if(name == "") {		// 未入力だったらダミーを入れる
				name = "○○○○";
				System.out.println("名前が入力されなかったので「" + name + "」さんで登録します。");
				System.out.println("「9.別の人を選択」で個人番号 " + childID + " を選択すると変更できます。");
			}
			
			Generation.addOne(childID, name);	// 子を追加
			System.out.println("　" + childID + " " + name + "さんを追加しました");
		}
		return;
	}
	/**
	 * 名前を変更
	 * @param id 個人ID
	 */
	public static void renName(int id) {
		System.out.println();
		Util.showMenuTree("名前を変更");
		System.out.println("■" + id + " " + Generation.getName(id) + "さんの名前を変更します。");
		System.out.println("　新しい名前を入力してください。");
		
		String name = Util.strInput();  // 文字列の入力受付
		
		if(name == "") {		// 未入力だったらダミーを入れる
			name = "○○○○";
			System.out.println("名前が入力されなかったので「" + name + "」さんで登録します。");
		}
		
		Generation.setName(id, name);
		return;
	}
	/**
	 * 削除の確認（1.やっぱりやめる　4.ほんとうに削除する）
	 * @param 個人番号
	 * @return 選択されたメニュー番号
	 */
	public static int confirmRemove(int id) {
		if(id == 1) {
			System.out.println("【エラー】" + 
					Generation.getName(1) + "さんは１代目筆頭のため、削除することができません。");
			return 1;		// メニュー番号「1.やっぱりやめる」で返す
		}
		
		System.out.println();
		Util.showMenuTree("削除");
		System.out.println("■" + id + " " + Generation.getName(id) + "さんを削除します。");
		Generation.chkRemoveID(id);	// そのまま削除してよいか判定してコメント表示
		System.out.println();
		System.out.println("　ほんとうに削除してよろしいですか？");
		System.out.println();
		System.out.println("　番号を入力してください（半角数字）");
		System.out.println(Util.rulePatch("┌─────────────────────────────────┐"));
		System.out.println(Util.rulePatch("│1.やっぱりやめる　　　　　　　　　　　　　　4.ほんとうに削除する　│"));
		System.out.println(Util.rulePatch("└─────────────────────────────────┘"));
		
		int num = Util.numInput();  // メニュー選択番号の入力受付
		return num;
	}
	/**
	 * データ保存の確認（3.保存する　9.やっぱりやめる） データと家系図の保存で使用
	 * @param ファイル名
	 * @param 拡張子
	 * @return 選択されたメニュー番号
	 */
	public static int confirmSave(String fileName, String ext) {
		System.out.println();
		System.out.println("■家系図の名前をファイル名として、データを「" + ext + "」形式で保存します。");
		System.out.println("　保存フォルダ： " + Data.getDataPath().toString());
		System.out.println("　ファイル名： " + fileName + "." + ext);
		System.out.println();
		System.out.println("★同じ名前のファイルがあれば、上書きします。");
		System.out.println("　ヒント：家系図の名前を変更すると、その名前で保存します。");
		System.out.println();
		System.out.println("　よろしいですか？");
		System.out.println();
		System.out.println("　番号を入力してください（半角数字）");
		System.out.println(Util.rulePatch("┌─────────────────────────────────┐"));
		System.out.println(Util.rulePatch("│3.保存する　5.家系図の名前を変更する　　　　　　9.やっぱりやめる　│"));
		System.out.println(Util.rulePatch("└─────────────────────────────────┘"));
		
		int num = Util.numInput();  // メニュー選択番号の入力受付
		return num;
	}
	
	// ■終了
	/**
	 * 終了確認（3.データ保存　0.メインメニューに戻る　9.ほんとうに終了する）
	 * @return 選択されたメニュー番号
	 */
	public static int confirmQuit() {	// 
		System.out.println();
		System.out.println("【確認】データを保存していないと、これまでに登録されたデータは消えます。");
		System.out.println("　　　　ほんとうに終了しますか？");
		System.out.println();
		System.out.println("　番号を入力してください（半角数字）");
		System.out.println(Util.rulePatch("┌─────────────────────────────────┐"));
		System.out.println(Util.rulePatch("│3.データ保存　0.メインメニューに戻る　　　　　　　　　　　　　　　│"));
		System.out.println(Util.rulePatch("│　　　　　　　　　　　　　　　　　　　　　　9.ほんとうに終了する　│"));
		System.out.println(Util.rulePatch("└─────────────────────────────────┘"));
		
		int num = Util.numInput();  // メニュー選択番号の入力受付
		return num;
	}
	/**
	 * 終了時のエンドロールの表示
	 */
	public static void showEndRoll() {
		System.out.println();
		
		String fileName = "endRoll.txt";
		Util.showTxt(fileName, "終了メッセージ");
		
		return;
	}
}
