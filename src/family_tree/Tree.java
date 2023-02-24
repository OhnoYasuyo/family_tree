/**
 * 家系図作成ソフト「あのこだれのこ」
 * 家系図の描画・表示クラス
 * ・インスタンスは血縁者ごとのペア（血縁者＋配偶者）
 * ・各インスタンスのListに血縁者ごとのペアの表示テキストを入れる
 */
package family_tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 大野康世 OHNO Yasuyo
 * @version 2023/01/16
 *
 */
public class Tree {
	// インスタンス 血縁者ごとのペア（血縁者＋配偶者）
	private int bloodID;			// 血縁者ID
	private int intGenNum;			// 世代番号
	private int intGenNameLength;	// 世代ごとの名前の最長文字数（半角換算）
	private int crntChildID;		// 書き出し中の子のID（血縁者）
	private List<String> listBranch;// インスタンスの表示テキスト
	private int linePos;			// インスタンス内の処理中の行番号
	private boolean isEnd;			// 書き出し終了
	
	// static
	private static Map<Integer, Tree> mapBranch = new HashMap<>();	// 血縁者IDとインスタンスのMap
	private static List<String> listTree = new ArrayList<>();	// 家系図各行のList
	private static List<String> listTreeHeader = new ArrayList<>();	// 家系図のヘッダ各行のList
	private static boolean addID;	// 家系図に表示する名前にIDをつけるかどうかのスイッチ
	
	// アクセサ インスタンス
	/**
	 * @return bloodID 血縁者ID
	 */
	public int getBloodID() {
		return bloodID;
	}
	/**
	 * @return intGenNum 世代番号
	 */
	public int getGenNum() {
		return intGenNum;
	}
	/**
	 * @return intGenNameLength 世代ごとの名前の最長文字数（半角換算）
	 */
	public int getGenNameLength() {
		return intGenNameLength;
	}
	/**
	 * 世代ごとの名前の最長文字数（半角換算） Generationから取得して設定
	 */
	public void setGenNameLength() {
		this.intGenNameLength = Generation.getGenFromID(bloodID).getGenNameMaxLength();
		return;
	}
	/**
	 * @return crntChildID
	 */
	public int getCrntChildID() {
		return crntChildID;
	}
	/**
	 * 処理中に呼び出すべき子のID（血縁者IDから）
	 * @param bloodID 血縁者ID
	 * @return 子のID
	 */
	public static int getCrntChildID(int bloodID) {
		return getBranch(bloodID).getCrntChildID();
	}
	/**
	 * @param crntChildID 書き出し中の子のID（血縁者）
	 */
	public void setCrntChildID(int crntChildID) {
		this.crntChildID = crntChildID;
		return;
	}
	/**
	 * @return listBranch インスタンスの表示テキスト
	 */
	public List<String> getListBranch() {
		return listBranch;
	}
	/**
	 * @return linePos インスタンス内の処理中の行番号
	 */
	public int getLinePos() {
		return linePos;
	}
	/**
	 * @param linePos インスタンス内の処理中の行番号
	 */
	public void setLinePos(int crntLine) {
		this.linePos = crntLine;
		return;
	}
	/**
	 * @return isEnd 書き出し終了
	 */
	public boolean getIsEnd() {
		return isEnd;
	}
	/**
	 * @param isEnd 書き出し終了
	 */
	public void setIsEnd(boolean isEnd) {
		this.isEnd = isEnd;
		return;
	}
	
	// アクセサ static
	/**
	 * @return mapBranch 血縁者IDとインスタンスのMap
	 */
	public static Map<Integer, Tree> getMapBranch() {
		return mapBranch;
	}
	/**
	 * @return listTree 家系図各行のList
	 */
	public static List<String> getListTree() {
		return listTree;
	}
	/**
	 * @return listTreeHeader 家系図のヘッダ各行のList
	 */
	public static List<String> getListTreeHeader() {
		return listTreeHeader;
	}
	/**
	 * @param listTreeHeader 家系図のヘッダ各行のList
	 */
	public static void setListTreeHeader(List<String> listTreeHeader) {
		Tree.listTreeHeader = listTreeHeader;
		return;
	}
	/**
	 * @return addID 家系図に表示する名前にIDをつけるかどうか
	 */
	public static boolean getAddID() {
		return addID;
	}
	/**
	 * @param addID 家系図に表示する名前にIDをつけるかどうか
	 */
	public static void setAddID(boolean addID) {
		Tree.addID = addID;
	}
	
	// コンストラクタ
	/**
	 * @param bloodID 血縁者ID
	 */
	public Tree(int bloodID) {
		this.bloodID = bloodID;
		this.intGenNum = Generation.getGenNum(bloodID);
		this.intGenNameLength = Generation.getGenFromID(bloodID).getGenNameMaxLength();
		this.crntChildID = 0;
		this.listBranch = new ArrayList<>();
		mapBranch.put(bloodID, this);
		this.linePos = 0;		// インスタンス内の処理中の行番号を初期化
		this.isEnd = false;		// 初期化
	}

	// ■■ メソッド
	// ■ インスタンス処理
	/**
	 *  インスタンスの更新（インスタンスが存在しなければ作成）
	 *  ・名前の変更や配偶者が追加・削除されたときに対応
	 *  ・変更がなかったときにも適応
	 * @param bloodID 血縁者ID
	 */
	public static void updateBranch(int bloodID) {
		
		if (getMapBranch().isEmpty() || getMapBranch().get(bloodID) == null) {
			new Tree(bloodID) ;
		} else {
			Tree thisBranch = getBranch(bloodID);
			
			thisBranch.setGenNameLength();
			thisBranch.setLinePos(0);		// インスタンス内の処理中の行番号を初期化
			thisBranch.setIsEnd(false);		// 書き出し終了を初期化
		}
	}
	
	// ■ IDからのアクセサ（static）
	/**
	 * 血縁者IDからTreeインスタンスを取得
	 * @param bloodID 血縁者ID
	 * @return Treeインスタンス
	 */
	public static Tree getBranch(int bloodID) {
		return mapBranch.get(bloodID);
	}
	/**
	 * 血縁者IDから親のTreeインスタンスを取得
	 * @param bloodID 血縁者ID
	 * @return 親のTreeインスタンス
	 */
	public static Tree getParentBranch(int bloodID) {
		return mapBranch.get(Generation.getParentID(bloodID));
	}
	/**
	 * 配偶者がいるかどうか
	 * @param bloodID 血縁者ID
	 * @return 配偶者がいるかどうか
	 */
	public static boolean hasPartner(int bloodID) {
		return Generation.hasPartner(bloodID);
	}
	/**
	 * ひとり親かどうか
	 * @param bloodID 血縁者ID
	 * @return ひとり親かどうか
	 */
	public static boolean isSingleParent(int bloodID) {
		return Generation.isSingleParent(bloodID);
	}
	/**
	 * addID（名前にIDをつけるかどうか）に応じた表示用の名前を取得
	 * @param id 個人ID
	 * @return 表示用の名前（名前のみ or ID＋名前）
	 */
	public static String getShowName(int id) {
		if (addID) {		// IDあり
			return Generation.getIdName(id);
		} else {			// IDなし
			return Generation.getName(id);
		}
	}
	
	// ■ 文字処理
	/**
	 * 文字列の繰り返し（全角スペースや罫線の描画に使用）
	 * @param str 繰り返す文字列
	 * @param num 繰り返しの数
	 * @return 文字列
	 */
	public static String repeatStr(String str, int num) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < num; i++) {
			sb.append(str);
		}
		return sb.toString();
	}
	/**
	 * ID分の半角文字数
	 * @param id 個人ID
	 * @return ID分の半角文字数（ID非表示のときは 0）
	 */
	public static int getIdLength(int id) {
		if (addID) {	// IDあり
			return (Integer.valueOf(id).toString().length() + 1);
		} else {		// IDなし
			return 0;
		}
	}
	/**
	 * 行末の空白文字を削除
	 * @param str 家系図の文字列
	 * @return 行末の空白を削除した文字列
	 */
	public static String delLastBlank(String str) {
		str = str.replaceFirst("[\\h]+$", "");
		return str;
	}
	
	// ■ 家系図の部品
	/**
	 * 血縁者IDからペア（血縁者＋配偶者）の表示テキストListを取得（オーバーロード）
	 * ・配偶者がいなければ2行、いれば4行
	 * @param bloodID 血縁者ID
	 * @return listBranch 血縁者ごとのペア（血縁者＋配偶者）の表示テキスト
	 */
	public static List<String> getListBranch(int bloodID) {
		return getBranch(bloodID).getListBranch();
	}
	/**
	 * 家系図１ペア分の表示文字列（複数行）作成
	 * ・IDをつけるかどうかはstaticのaddIDで指定 ← 呼び出し側のshowTree() で設定
	 * ・配偶者がいなければ2行、いれば4行 + 親がひとり親でなければ頭に空行
	 * @param bloodID 血縁者ID
	 */
	public static void setBranchOne(int bloodID) {
		Tree crntBranch = getBranch(bloodID);
		getListBranch(bloodID).clear();		// ペアの表示文字列リセット
		int length;		// 文字列の長さ計算用
		
		// ■０行目	第２世代以降で親がひとり親でなく、第一子ならば頭に空行を入れる
		if(bloodID > 1 && !isSingleParent(Generation.getParentID(bloodID)) && Generation.isEldest(bloodID)) {
			getListBranch(bloodID).add("");
		}
		
		// ■１行目sb1
		StringBuilder sb1 = new StringBuilder();
		// 1,2文字目
		if(crntBranch.getGenNum() > 1) {	// 第1世代は1,2文字目なし
			if(Generation.isOnlyChild(bloodID)) {
				// 一人っ子なら
				sb1.append("──");
			} else if(!Generation.isEldest(bloodID) && !Generation.isYoungest(bloodID)) {
				// 第一子でも末子でもないなら
				sb1.append("├─");
			} else if(Generation.isYoungest(bloodID)) {
				// 末子なら
				sb1.append("└─");
			} else {
				// 第一子なら
				sb1.append("┬─");
			}
		}
		// 名前（IDをつけるかどうかはidNameSwtで指定）
		sb1.append(getShowName(bloodID));
		// 自身がひとり親なら、そのあとを"─"で埋める（血縁者はひとり親でなければこの行は終了）
		if(isSingleParent(bloodID)) {
			length = crntBranch.getGenNameLength() - Generation.getNameLength(bloodID);	// 半角での残り文字数
			sb1.append(repeatStr(" ", length % 2));	// 名前が全角に収まらなければ、末尾に半角スペースを入れる
			sb1.append(repeatStr("─", ((length / 2) + 1)));
		}
		// １行目をlistBranchに入れる
		getListBranch(bloodID).add(sb1.toString());
		
		// ■２行目sb2
		StringBuilder sb2 = new StringBuilder();
		
		// 1～2文字目
		if (crntBranch.getGenNum() > 1) {	// 第2世代以降のきょうだいの枝 第1世代はなし
			if(!Generation.isYoungest(bloodID)) {	// 自身の後にきょうだいがいたら（末子でない）
				sb2.append("│　");
			} else {		// 末子なら
				sb2.append("　　");
			}
		}
		// ID分のスペース
		sb2.append(repeatStr(" ", getIdLength(bloodID)));
		
		// 配偶者への枝以降
		length = (crntBranch.getGenNameLength() / 2) + 1;		// 全角での世代文字数 + 1
		if(hasPartner(bloodID)) {
			if(Generation.hasChild(bloodID)) {	// 配偶者あり子あり
				sb2.append("├");
				sb2.append(repeatStr("─", (length - 1)));
			} else {							// 配偶者あり子なし
				sb2.append("│");
				sb2.append(repeatStr("　", (length - 1)));
			}
		} else {								// 配偶者なし
			sb2.append(repeatStr("　", length));
		}
		
		// ２行目をlistBranchに入れる
		getListBranch(bloodID).add(sb2.toString());
		
		// ３行目以降は配偶者があるときのみ
		if(hasPartner(bloodID)) {		// 配偶者があるとき
			
			// ■３行目sb3
			StringBuilder sb3 = new StringBuilder();
			// 1,2文字目
			if(crntBranch.getGenNum() > 1) {	// 第1世代は1,2文字目なし
				if(Generation.isYoungest(bloodID)) {		// 末子なら（一人っ子を含む）
					sb3.append("　　");
				} else {									// 末子でなければ
					sb3.append("│　");
				}
			}
			// 配偶者の名前（IDをつけるかどうかはidNameSwtで指定）
			sb3.append(getShowName(Generation.getPartnerID(bloodID)));
			// 残りを全角スペースで埋める
			length = crntBranch.getGenNameLength() - 
					Generation.getNameLength(Generation.getPartnerID(bloodID));		// 半角での残り文字数
			sb3.append(repeatStr(" ", length % 2));	// 名前が全角に収まらなければ、末尾に半角スペースを入れる
			sb3.append(repeatStr("　", (length / 2) + 1));
			
			// ３行目をlistBranchに入れる
			getListBranch(bloodID).add(sb3.toString());
			
			// ■4行目sb4
			StringBuilder sb4 = new StringBuilder();
			// 1,2文字目
			if(crntBranch.getGenNum() > 1) {	// 第1世代は1,2文字目なし
				if(Generation.isYoungest(bloodID)) {		// 末子なら（一人っ子を含む）
					sb4.append("　　");
				} else {									// 末子でなければ
					sb4.append("│　");
				}
			}
			// ID分のスペース
			sb4.append(repeatStr(" ", getIdLength(bloodID)));
			// 残りを全角スペースで埋める
			length = (crntBranch.getGenNameLength() / 2) + 1;	// 全角での世代文字長さ + 1
			sb4.append(repeatStr("　", length));
			
			// ４行目をlistBranchに入れる
			getListBranch(bloodID).add(sb4.toString());
		}
		return;
	}
	/**
	 * 自身と子孫の isEnd がすべて true かどうかを取得（再帰処理あり）
	 * @param bloodID 血縁者ID
	 * @return 自身と子孫の isEnd がすべて true かどうか
	 */
	public static boolean isBranchEnd(int bloodID) {
		boolean isBranchEnd = false;		// 初期化
		if (!Generation.hasChild(bloodID)) {
			isBranchEnd = getBranch(bloodID).getIsEnd();			// 子がいなければ自身の isEnd を返す
		} else {
			int lastChildID = Generation.getLastChildID(bloodID);	// 子がいたら末子について調べる
			
			// 自身がisEndのとき、末子について調べる（再帰処理）
			if (getBranch(bloodID).getIsEnd() == true) {
				isBranchEnd = isBranchEnd(lastChildID);			// 自身も末子もisEndなら true それ以外は false
				// false が帰ってきたら再帰処理ループは止まる
			}
		}
		return isBranchEnd;
	}
	/**
	 * 最初に呼び出すべき子のIDをcrntChildIDに代入（該当がなければ 0）
	 * @param bloodID 血縁者ID
	 */
	public static void setFirstChildID(int bloodID) {
		if (Generation.hasChild(bloodID)) {
			getBranch(bloodID).setCrntChildID(Generation.getChildIDs(bloodID)[0]);	// childID配列の 0 番目を代入
		} else {
			getBranch(bloodID).setCrntChildID(0);		// 子がなければ 0 を代入
		}
		return;
	}
	/**
	 * 次に呼び出すべき子のIDをcrntChildIDに代入（該当がなければ 0）
	 * @param bloodID 血縁者ID
	 * @return 子のID
	 */
	public static void setNextChildID(int bloodID) {
		// 現在呼び出している子が 存在しない または 末っ子 のとき
		if (getCrntChildID(bloodID) == 0 || Generation.isYoungest(getCrntChildID(bloodID))) {
			getBranch(bloodID).setCrntChildID(0);		// 0 を crntChildID に代入
			return;
		} else {		// 現在呼び出している子が末っ子でないとき
			boolean isFound = false;	// 見つけたかどうか 初期化
			// crntChildIDの次の子を見つける
			for (int id : Generation.getChildIDs(bloodID)) {
				if (isFound) {			// 現在呼び出している子のIDが見つかった後の次の周回（i = 次の子のID）
					getBranch(bloodID).setCrntChildID(id);		// id = 次の子のID を crntChildID に代入
					return;
				}
				if (id == getCrntChildID(bloodID)) {		// id = 現在呼び出している子のID
					isFound = true;
				}
			}
			getBranch(bloodID).setCrntChildID(0);		// 見つからなければ 0 を crntChildID に代入
			return;
		}
	}
	/**
	 * 1ペアごとの指定行文字列を取得してlinePosを更新 （最終行のあとは、最終行を返す）
	 * @param ペアの個人番号
	 * @param 指定行
	 * @return 1ペアごとの指定行文字列（0～）
	 */
	public static String getBranchLine(int bloodID, int num) {
		String strBranchLine = "blank";		// 初期化
		Tree crntBranch = getBranch(bloodID);
		if (getListBranch(bloodID).size() - 1 >= num) {	// 自身の行の書き出しが終っていないとき
			strBranchLine = getListBranch(bloodID).get(num);	// 処理中の行番号の文字列を取得して代入
			crntBranch.setLinePos(crntBranch.getLinePos() + 1);	// 処理中の行番号を +1
			
			// 最終行なら isEnd を true にする
			if (getListBranch(bloodID).size() - 1 == num) {
				crntBranch.setIsEnd(true);
			}
		} else {		// 自身の行の書き出しが終っていたら、
			if (!isBranchEnd(bloodID)) {		// すべての子孫が書き出し終っていなければ最終行を返す
				strBranchLine = getListBranch(bloodID).get(getListBranch(bloodID).size() - 1);
			}	// 自身も子孫も書き出し終っていたら "blank" のまま
		}
		return strBranchLine;
	}
	/**
	 * 家系図の１行を作成（再帰処理あり）
	 * ・インスタンスが持っているlinePosの行を書き出し、後ろに子のインスタンスを追加
	 * @param bloodID 血縁者ID
	 * @return １行の文字列
	 */
	public static String getTreeLineOne(int bloodID) {
		String line = "";	// 初期化
		
		if (!isBranchEnd(bloodID)) {		// 自身と子孫がisEndでないときのみ
			line = getBranchLine(bloodID, getBranch(bloodID).getLinePos());		// 自分の行を書き出し
			// 自身の行が""（親がひとり親のときの0行目）でないとき 現在の子がいるなら 子を呼ぶ
			if (line != "" && getCrntChildID(bloodID) != 0) {
				
				// 子の子孫がすべてisEndのときは、次の子に
				if (isBranchEnd(getCrntChildID(bloodID))) {
					setNextChildID(bloodID);	// 自身のcrntChildIDを次の子に
				}
				
				if (getCrntChildID(bloodID) != 0) {		// 次の子が見つかったとき
					// 子の行を取得（このメソッドに再帰処理）して自分に追加
					line = line + getTreeLineOne(getCrntChildID(bloodID));
				}
			}	// 自身が "" または 子が見つからない -> line = 自分の行のみ
		}	// 自身と子孫がisEnd -> line = ""
		return line;
	}
	/**
	 * 家系図表示（IDをつけるかどうかを引数で指定）とsetList
	 * @param addID 名前にIDをつけるときtrue
	 */
	public static void showTree(boolean addID) {
		setAddID(addID);		// 名前にIDをつけるかどうか
		getListTree().clear();		// 家系図を初期化
		
		// すべての血縁者のインスタンス更新と、ペアの表示テキストListを作成
		for (int bloodID : Generation.getBloodIDs()) {
			updateBranch(bloodID);	// インスタンスを更新
			setBranchOne(bloodID);	// ペアの表示テキストListを作成
			if (Generation.hasChild(bloodID)) {
				setFirstChildID(bloodID);	// crntChildIDに最初の子を入れる
			}
		}
		
		// 文字列を１行ずつ取得して、listTreeに入れて、画面表示
		while (!isBranchEnd(1)) {		// 初代の子がすべてisEndになるまで繰り返す
			// 初代でgetTreeLineOneを呼ぶ
			String line = getTreeLineOne(1);
			line = delLastBlank(line);			// 行末の全半角スペースを消去
			if (line != "") {			// lineが""のときは書き出さない
				getListTree().add(line);				// この行をListに入れる
				System.out.println(Util.rulePatch(line));	// 画面に表示
			}
		}
		// 罫線をlistTreeに入れて、画面表示
		getListTree().add(Util.showLine());
		return;
	}
	/**
	 * 家系図ヘッダ表示（人数等も表示）とsetList
	 */
	public static void showTreeHeader() {
		getListTreeHeader().clear();	// 初期化
		// ヘッダのコメントを作成
		int genCount = Generation.getMaxGen();
		int familyCount = Generation.getFamilyCount();
		String str1 = "■" + genCount + "世代 " + familyCount + "人";
		// ヘッダを表示してListに入れる
		setListTreeHeader(Util.showMenuHeader(Generation.getStrTitle(), str1));
		// 世代の人数を計算
		String str2 = "";
		for (int i = 1; i <= genCount; i++) {
			str2 += "　・第" + i + "世代：" + Generation.getGenFromNum(i).getGenCount() + "人";
		}
		// 世代の人数を表示してListに入れる
		System.out.println(str2);
		getListTreeHeader().add(str2);
		// 罫線を表示してListに入れる
		getListTreeHeader().add(Util.showLine());
		return;
	}
}
