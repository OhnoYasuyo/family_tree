/**
 * 家系図作成ソフト「あのこだれのこ」
 * ファイル入出力クラス
 */
package family_tree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author 大野康世 OHNO Yasuyo
 * @version 2023/01/16
 *
 */
public class Data {
	// ■パスの取得
	/**
	 * 実行ディレクトリの絶対パスの取得
	 * @return Path アプリを実行しているディレクトリの絶対パス
	 */
	public static Path getCrntPath() {
		return Paths.get("").toAbsolutePath();
	}
	/**
	 * txtフォルダ内にあるファイルの絶対パスの取得
	 * @param fileName ファイル名（拡張子あり）
	 * @return Path txtフォルダ内にあるファイルの絶対パス
	 */
	public static Path getTxtFilePath(String fileName) {
		Path txtFilePath =  Paths.get(getCrntPath().toString() + "/src/txt/" + fileName);
		return txtFilePath.toAbsolutePath();
	}
	/**
	 * ユーザデータフォルダの絶対パスの取得
	 * （実行ディレクトリの2階層上）
	 * @return Path ユーザデータフォルダの絶対パス
	 */
	public static Path getDataPath() {
		Path dataPath = Paths.get("");
		return dataPath.toAbsolutePath();
	}
	/**
	 * ユーザデータフォルダ内にあるファイルの絶対パスの取得
	 * @param fileName ファイル名（拡張子あり）
	 * @return Path user_dataフォルダ内にあるファイルの絶対パス
	 */
	public static Path getDataFilePath(String fileName) {
		return Paths.get(getDataPath().toString() + "/" + fileName);
	}
	/**
	 * user_dataフォルダ内にあるファイルの絶対パスの取得
	 * @param fileName ファイル名（拡張子なし）
	 * @param ext 拡張子
	 * @return Path user_dataフォルダ内にあるファイルの絶対パス
	 */
	public static Path getDataFilePath(String fileName, String ext) {
		return getDataFilePath(fileName + "." + ext);
	}
	
	// ■データ読込
	/**
	 * csvデータ読込（csvファイルの絶対パスを指定）
	 * @param strFilePath csvファイルのフルパス（絶対パス）
	 */
	public static void loadData(String strFilePath) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(strFilePath), Charset.forName(Util.getTxtCode())))) {
			
			String line;
			int i = 0;		// ループカウンタ
			while ((line = br.readLine()) != null) {
				if(i == 0) {								// 1行目：タイトル
					String[] data = line.split(",");
					if (data.length == 2) {
						// 読み込んだCSVファイルの内容を登録
						Generation.setStrTitle(data[1]);
					}
				} else if (i > 0) {							// 2行目以降：個人データ
					String[] data = line.split(",");
					if (data.length == 2) {
						// 読み込んだCSVファイルの内容を登録
						Generation.addOne(Integer.parseInt(data[0]), data[1]);
					}
				}
				i++;
			}
			System.out.println();
			System.out.print("　「" + Generation.getStrTitle() + "」　");
			System.out.println((i - 1) + "人のデータを読み込みました");
			System.out.println();
		} catch (IOException e) {
			System.out.println("データを読み込めませんでした");
			e.printStackTrace();
		}
		return;
	}
	// ■データ保存
	/**
	 * csvデータ保存
	 * @param csvName ファイル名（拡張子なし）
	 */
	public static void saveData(String csvName) {
		try {
			// 出力ファイルの作成
			FileWriter fw = new FileWriter(getDataFilePath(csvName, "csv").toString(), false);
			// PrintWriterクラスのオブジェクトを生成
			PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
			
			// データを書き込む
			pw.println("家系図名：," + Generation.getStrTitle());	// 1行目：タイトル
			for(int i=0; i < Generation.getMaxGen(); i++){			// 2行目以降：個人データ
				Generation gen = Generation.getGenFromNum(i+1);
				for(int j=0; j < gen.getGenCount(); j++) {
					int id = gen.getGenIDs()[j];
					pw.println(id + "," + Generation.getName(id));
				}
			}
			pw.close();
			
			// 保存完了メッセージ
			System.out.println();
			System.out.println("データを保存しました");
			System.out.println("保存フォルダ： " + getDataPath().toString());
			System.out.println("ファイル名： " + csvName + ".csv");
			
		} catch (IOException e) {
			System.out.println("データ保存ができませんでした");
			e.printStackTrace();
		}
		return;
	}
	// ■テキスト保存
	/**
	 *  家系図をテキスト形式で保存
	 */
	public static void saveText(String txtName) {
		try {
			// 出力ファイルの作成
			FileWriter fw = new FileWriter(getDataFilePath(txtName, "txt").toString(), false);
			// PrintWriterクラスのオブジェクトを生成
			PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
			
			// ヘッダを書き込む
			for(String str : Tree.getListTreeHeader()){		// TreeのListTreeHeaderを１行ずつ呼び出して書き込み
				pw.println(str);
			}
			// １行アケ
			pw.println("");
			
			// 家系図を書き込む
			for(String str : Tree.getListTree()){		// TreeのListTreeを１行ずつ呼び出して書き込み
				pw.println(str);
			}
			
			pw.close();
			
			// 保存完了メッセージ
			System.out.println();
			System.out.println("データを保存しました");
			System.out.println("ファイル名： " + txtName + ".txt");
			System.out.println("保存フォルダ： " + getDataPath().toString());
			
		} catch (IOException e) {
			System.out.println("データ保存ができませんでした");
			e.printStackTrace();
		}
		return;
	}
}
