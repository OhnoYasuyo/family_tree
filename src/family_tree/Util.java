/**
 * 家系図作成ソフト「あのこだれのこ」
 * ユーティリティクラス：
 * ・罫線表示の補正メソッド rulePatch(String str)
 * ・テキスト入力受付のメソッド
 * ・一時停止メソッド waitEnter()
 * ・メニューヘッダの表示メソッド
 * ・編集用家系図メニューの表示メソッド
 */
package family_tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 大野康世 OHNO Yasuyo
 * @version 2023/01/16
 *
 */
public class Util {
	private static final String TXT_CODE = "UTF-8";	// 文字コード "UTF-8" or "Shift_JIS"
	private static int patchSwt;	// 罫線表示パッチのスイッチ 7:Windows7コンソール用 0:default
	
	// アクセサ
	/**
	 * @return txtCode 文字コード
	 */
	public static String getTxtCode() {
		return TXT_CODE;
	}
	/**
	 * @return patchSwt 罫線表示パッチのスイッチ
	 */
	public static int getPatchSwt() {
		return patchSwt;
	}
	/**
	 * @param patchSwt 罫線表示パッチのスイッチをセットする
	 */
	public static void setPatchSwt(int numPatch) {
		patchSwt = numPatch;
		return;
	}
	
	// ■メソッド
	/**
	 * 文字列の長さを取得（半角換算） 全角は 2、半角は 1
	 * @param 文字列
	 * @return 半角換算の文字列長さ
	 */
	public static int getStrLength(String str) {
		int length = 0;	// 初期化
		//全角半角を判定して文字数を数える
		char[] c = str.toCharArray();
		for(int i = 0; i < c.length; i++) {
			if(String.valueOf(c[i]).getBytes().length <= 1){
				length += 1; //半角文字なら＋１
			}else{
				length += 2; //全角文字なら＋２
			}
		}
		return length;
	}
	/**
	 * 罫線表示のずれを補正するパッチ（swtPatchで切り替え）
	 * @param str 元の文字列
	 * @return 補正後の文字列
	 */
	public static String rulePatch(String str) {
		switch (patchSwt) {
		case 7:
			return rulePatch7(str);
		default:
			return str;
		}
	}
	/**
	 * 罫線表示のずれを補正するパッチ（Windows7コンソール用）
	 * @param str 元の文字列
	 * @return 補正後の文字列
	 */
	public static String rulePatch7(String str) {
		// 罫線文字の後に半角スペースを入れる
		str = str.replaceAll("[─-┷]", "$0 ");
		return str;
	}
	/**
	 * テキストファイルを読み込んで表示
	 * @param fileName テキストファイルのファイル名（拡張子あり・実行ディレクトリ内のtxtフォルダに置く）
	 * @param title テキストの内容（エラー表示用）
	 */
	public static void showTxt(String fileName, String title) {
		File file = new File(Data.getTxtFilePath(fileName).toString());
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				line = rulePatch(line);		// 罫線表示の補正パッチ
				System.out.println(line);
			}
		} catch (Exception e) {
			System.out.println(title + "の読み込みに失敗しました");
			e.printStackTrace();
		}
		return;
	}
	/**
	 * 半角数字（メニュー番号）の入力受付
	 * @return 入力された数字（未入力は 999、入力エラーは 0）
	 */
	public static int numInput() {
		System.out.println();
		System.out.print("　⇒ ");	// プロンプタ表示
		
		int num = 0;	// 初期化
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			String str = br.readLine();
			if(str == "") {		// 未入力のときはエラー回避のため"999"とする
				str = "999";
			}
			num = Integer.parseInt(str);
		} catch (NumberFormatException e) {	// 数字以外（文字列等）を入力したときの例外処理
			System.out.println("無効な値が入力されました");
			System.out.println("メニュー番号を半角数字で入力してください");
			return 0;
		} catch(Exception e) {		// その他の例外処理
			System.out.println("入力エラーです");
			e.printStackTrace();
			return 0;
		}
		System.out.println();  // １行アケ
		return num;
	}
	/**
	 * 文字列の入力受付
	 * @return 入力された文字列
	 */
	public static String strInput() {
		System.out.println();
		System.out.print("　⇒ ");  // プロンプタ表示
		
		String string = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			string = br.readLine();
		} catch(Exception e) {		// 例外処理
			System.out.println("入力エラーです");
			e.printStackTrace();
			return "";
		}
		System.out.println();  // １行アケ
		return string;
	}
	/**
	 * 一時停止（Enterキーが押されるまで待つ）
	 */
	public static void waitEnter() {
		// 何か文字を入力しても無視する
		System.out.print("Enterキーを押すと続行します");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			String str = br.readLine();
			if (str != "") {		// エラー回避
				str = "";
			}
			return;
		} catch(Exception e) {		// 例外処理
			System.out.println("入力エラーです");
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * メニューヘッダの表示（見出しとコメント指定）
	 * @param heading 見出し文字列
	 * @param comment コメント文字列
	 * @return ヘッダ各行のList（家系図書き出し用）
	 */
	public static List<String> showMenuHeader(String heading, String comment) {
		List<String> listHeader = new ArrayList<>();
		
		// 1行は全角45文字（文字数はすべて全角換算）
		int headingLength = Util.getStrLength(heading);	// タイトル文字列の長さ（半角）
		// バイト数が奇数だったら後ろに半角スペースを入れて偶数に
		if ((headingLength % 2) == 1) {
			heading = heading + " ";
			headingLength += 1;
		}
		headingLength /= 2;	// タイトル文字列の長さ（全角）
		
		// ヘッダ1行目：左罫線素 + 横線*(heading文字数+2) + 右罫線素
		StringBuilder sb1 = new StringBuilder();
		sb1.append("┌");
		for(int i=0; i < (headingLength + 2); i++) {
			sb1.append("─");
		}
		sb1.append("┐");
		System.out.println(rulePatch(sb1.toString()));
		listHeader.add(sb1.toString());		// sb1をlistHeaderに入れる
		
		// ヘッダ2行目：左罫線素 + 全角スペース + heading + 全角スペース + 右罫線素 + コメント
		String str2 = "│　" + heading + "　│　" + comment;
		System.out.println(rulePatch(str2));	// str2を表示
		listHeader.add(str2.toString());		// str2をlistHeaderに入れる
		
		// ヘッダ3行目：左罫線素 + 横線*(heading文字数+2) + 右罫線素 + 横線*(45-(heading文字数+2+2))
		StringBuilder sb3 = new StringBuilder();
		sb3.append("┴");
		for(int i=0; i < (headingLength + 2); i++) {
			sb3.append("─");
		}
		sb3.append("┴");
		for(int i=0; i < (41 - headingLength); i++) {
			sb3.append("─");
		}
		System.out.println(rulePatch(sb3.toString()));
		listHeader.add(sb3.toString());		// sb3をlistHeaderに入れる
		
		return listHeader;
	}
	/**
	 * 罫線の表示
	 * @return 罫線の文字列（家系図書き出し用）
	 */
	public static String showLine() {
		String strLine = "─────────────────────────────────────────────";
		System.out.println(rulePatch(strLine));
		return strLine;
	}
	/**
	 * 編集用家系図メニューの表示（見出しとコメント指定）
	 * @param heading 見出し文字列
	 * @param comment コメント文字列
	 */
	public static void showMenuTree(String heading, String comment) {
		// ヘッダ表示
		showMenuHeader(heading, comment);
		
		// 編集用家系図
		Tree.showTree(true);
		
		return;
	}
	/**
	 * 編集用家系図メニューの表示（見出しのみ、コメントはdefault） オーバーロード
	 * @param heading 見出し文字列
	 */
	public static void showMenuTree(String heading) {
		showMenuTree(heading, "名前の前についているのは個人番号です。");
		return;
	}
}
