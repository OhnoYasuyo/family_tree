/**
 * 家系図作成ソフト「あのこだれのこ」
 * 世代クラス：
 * ・家系図名をstaticに格納
 * ・個人名とIDをMapで格納
 * ・世代と個人の管理
 * ・関係者の取得メソッド
 */
package family_tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 大野康世 OHNO Yasuyo
 * @version 2023/01/16
 *
 */
public class Generation {
	// static
	private static String strTitle;	// 家系図の名前
	private static final int INT_MAX_GEN_LIMIT = 5;	// 世代数の最大値（変更可能）
	// 世代数の最大値を変更したら、txt/usage1.txtとtxt/infoNew.txtを書き換えること
	private static List<Generation> listGens = new ArrayList<>();	// 世代インスタンスのリスト
	private static int INT_MIN_LENGTH = 4;			// 「名前」の規定最小文字数（全角、樹形図の整形用）
	
	// インスタンス
	private int intGenNum;					// 世代番号（*代目）
	private Map<Integer, String> mapNames;	// 個人のIDと名前を格納するマップ
	
	/**
	 * コンストラクタ
	 * @param intGenNum 世代番号（＊代目, 1～世代数の最大値）
	 * 世代番号は、リストには 0～(世代数の最大値 - 1) で格納
	 * ★インスタンスには変数名をつけず、リストlistGensから呼び出す
	 */
	public Generation(int intGenNum) {
		if (intGenNum > INT_MAX_GEN_LIMIT) {
			System.out.println("登録できる世代数を超えたため、登録できません");
		} else {
			this.intGenNum = intGenNum;
			this.mapNames = new HashMap<>();
			listGens.add((intGenNum - 1), this);	// 世代インスタンスをリストに追加
		}
		return;
	}
	
	// アクセサ static
	/**
	 * @return strTitle 家系図の名前を取得
	 */
	public static String getStrTitle() {
		return strTitle;
	}
	/**
	 * @param strTitle 家系図の名前をセットする
	 */
	public static void setStrTitle(String strTitle) {
		Generation.strTitle = strTitle;
		return;
	}
	/**
	 * @return listGens 世代インスタンスのリストを取得
	 */
	public static List<Generation> getListGens() {
		return listGens;
	}
	/**
	 * @return intMaxGen 世代数の最大値を取得
	 */
	public static int getMAX_GEN_LIMIT() {
		return INT_MAX_GEN_LIMIT;
	}
	
	// アクセサ 世代インスタンスから
	/**
	 * @return intGenNum
	 */
	public int getGenNum() {
		return intGenNum;
	}
	
	// ■■■世代インスタンスから実行するメソッド■■■
	// ■世代の調査
	/**
	 * @return 世代に含まれる人数 配偶者を含む
	 */
	public int getGenCount() {
		if (this.mapNames.isEmpty()) {
			return 0;
		} else {
			return this.mapNames.size();
		}
	}
	/**
	 * @return 世代に含まれる血縁者の人数（世代全員の人数－配偶者）
	 */
	public int getGenBloodCount() {
		if (this.mapNames.isEmpty()) {
			return 0;
		}
		int genBloodCount = 0;	// リセット
		for (int id : getGenIDs()) {
			if (!isPartner(id)) {
				genBloodCount++;
			}
		}
		return genBloodCount;
	}
	/**
	 * @return 世代全員のIDの配列（昇順ソート） 配偶者を含む
	 */
	public int[] getGenIDs() {
		Set<Integer> genIDsSet = new HashSet<>();
		genIDsSet = this.mapNames.keySet();
		int[] genIDs = new int[this.getGenCount()];
		int i = 0;		// ループカウンタ
		for (int id: genIDsSet) {	// 世代全員のIDを配列に入れる
			genIDs[i] = id;
			i++;
		}
		Arrays.sort(genIDs);		// 配列を昇順にソート
		return genIDs;
	}
	/**
	 * @return 世代に含まれる「名前」の最大文字数（半角、偶数）
	 */
	public int getGenNameMaxLength() {
		int intMaxLength = INT_MIN_LENGTH;	// 規定最小文字数で初期化
		for (int id: this.mapNames.keySet()) {
			if (intMaxLength < getNameLength(id)) {
				intMaxLength = getNameLength(id);
			}
		}
		return (intMaxLength + (intMaxLength % 2));		// 奇数だったら +1 で偶数にする
	}
	
	// ■■■staticメソッド■■■
	/**
	 * 個人IDから世代番号を取得 アクセサのオーバーロード 存在しないIDもOK
	 * @param id 個人ID
	 * @return その個人が含まれる世代番号(1～）
	 */
	public static int getGenNum(int id) {
		return Integer.toString(id).length();	// IDの桁数を取得
	}
	
	// ■世代インスタンスの取得
	/**
	 * 世代番号（1～）から世代インスタンスを取得
	 * @param num 世代番号（1～）
	 * @return 世代インスタンス
	 */
	public static Generation getGenFromNum(int num) {
		return listGens.get(num - 1);
	}
	/**
	 * 個人IDからその人を含む世代インスタンスを取得
	 * @param id 個人ID
	 * @return その個人が含まれる世代インスタンス
	 */
	public static Generation getGenFromID(int id) {
		return getGenFromNum(getGenNum(id));
	}
	/**
	 * 編集中の個人IDからその子の世代インスタンスを取得
	 * @param baseID 個人ID
	 * @return 子の世代インスタンス
	 */
	public static Generation getChildGenFromID(int baseID) {
		return listGens.get(getGenNum(baseID));	// getGenNum(id) - 1 + 1
	}
	
	// ■家系全体の調査
	/**
	 * @return 家系全体の人数
	 */
	public static int getFamilyCount() {
		int familyCount = 0;	// リセット
		for (int i = 1; i <= getMaxGen(); i++) {
			familyCount += getGenFromNum(i).getGenCount();
		}
		return familyCount;
	}
	/**
	 * @return 家系全体の血縁者の人数（家系全体 - 配偶者）
	 */
	public static int getBloodCount() {
		int bloodCount = 0;		// リセット
		for (int i = 0; i < getMaxGen(); i++) {
			for (int id : getGenFromNum(i + 1).getGenIDs()) {
				if (!isPartner(id)) {
					bloodCount++;
				}
			}
		}
		return bloodCount;
	}
	/**
	 * @return 家系全体の血縁者のIDの配列（昇順ソート）
	 */
	public static int[] getBloodIDs() {
		int[] bloodIDs = new int[getBloodCount()];
		int count = 0;		// リセット
		for (int i = 0; i < getMaxGen(); i++) {
			for (int id : getGenFromNum(i + 1).getGenIDs()) {
				if (!isPartner(id)) {
					bloodIDs[count] = id;
					count++;
				}
			}
		}
		Arrays.sort(bloodIDs);		// 配列を昇順にソート
		return bloodIDs;
	}
	/**
	 * @return 最大の世代番号（1～）
	 */
	public static int getMaxGen() {
		return getListGens().size();
	}
	
	// ■■個人の操作
	// ■個人の追加と名前の変更
	/**
	 * 名前を指定して初代を追加（世代番号とIDは 1 で固定）
	 * @param name 初代の名前
	 */
	public static void addFounder(String name) {
		new Generation(1);
		getGenFromNum(1).mapNames.put(1, name);
		return;
	}
	/**
	 * IDと名前を指定して初代以外を追加
	 * @param id 個人番号
	 * @param name 名前
	 */
	public static void addOne(int id, String name) {
		if (getGenNum(id) > getMaxGen()) {	// 世代が存在しなければインスタンスを作成
			new Generation(getGenNum(id));
		}
		getGenFromID(id).mapNames.put(id, name);
		return;
	}
	/**
	 * 指定IDの名前を変更する
	 * @param id 個人ID
	 * @param name 変更後の名前
	 */
	public static void setName(int id, String name) {
		getGenFromID(id).mapNames.put(id, name);
		return;
	}
	
	// ■個人の削除
	/**
	 * そのまま削除できるかどうかの判定 （できないときはメッセージを表示）
	 * @param id 個人ID
	 * @return そのまま削除できるかどうか
	 */
	public static boolean chkRemoveID(int id) {
		if (hasPartner(id) || hasChild(id)) {
			System.out.print(getName(id) + "さんを削除すると、");
			if(hasPartner(id)) {
				System.out.print("配偶者");
			}
			if(hasChild(id)) {
				if(hasPartner(id)) {
					System.out.print("、");
				}
				System.out.print("子孫とその配偶者");
			}
				System.out.println("も削除されます");
			return false;
		} else {
			return true;
		}
		
	}
	/**
	 * 個人を削除する（配偶者と子孫も問答無用ですべて削除）
	 * @param id 個人ID
	 * ★実行前に必ずchkRemoveID()を実行すること
	 */
	public static void removeID(int id) {
		if (hasPartner(id)) {		// 配偶者がいたら先に削除する
			getGenFromID(id).mapNames.remove(getPartnerID(id));
		}
		if (hasChild(id)) {		// 子がいたら先にすべて削除する
			removeChildID(id);	// 入れ子なので、子孫全員が削除される
		}
		getGenFromID(id).mapNames.remove(id);
		return;
	}
	/**
	 * 指定IDの子をすべて削除 （removeID()の入れ子）
	 * @param baseID 編集中の個人ID
	 */
	public static void removeChildID(int baseID) {
		if(getChildCount(baseID) > 0) {
			for (int childID: getChildIDs(baseID)) {
				removeID(childID);
			}
		}
		return;
	}
	
	
	// ■個人の調査
	/**
	 * IDから名前を取得
	 * @param id 個人ID
	 * @return 名前
	 */
	public static String getName(int id) {
		return getGenFromID(id).mapNames.get(id);
	}
	/**
	 * IDから表示用の「名前」の長さを取得（半角）
	 * @param id
	 * @return 表示用の「名前」の長さ
	 */
	public static int getNameLength(int id) {
		return Util.getStrLength(getName(id));
	}
	/**
	 * IDから表示用の文字列「ID 名前」を取得（表示崩れ防止の調整あり）
	 * @param id
	 * @return 表示用の文字列「ID 名前」
	 */
	public static String getIdName(int id) {
		String idName = id + " " + getName(id);
		return idName;
	}
	/**
	 * IDが存在するかどうかの判定
	 * @param id 個人ID
	 * @return IDが存在するかどうか
	 */
	public static boolean isExistID(int id) {
		return getGenFromID(id).mapNames.containsKey(id);
	}
	/**
	 * 配偶者かどうか（=血縁者でない）の判定
	 * @param id 個人ID
	 * @return 配偶者かどうか falseなら血縁者
	 */
	public static boolean isPartner(int id) {
		return String.valueOf(id).startsWith("2");
	}
	/**
	 * 一人っ子かどうかの判定（第1世代はtrue、配偶者はfalse）
	 * @param id 個人ID
	 * @return 一人っ子かどうか
	 */
	public static boolean isOnlyChild(int id) {
		// 第1世代はtrue、配偶者はfalse、それ以外は親の子の数で判定
		if (getGenNum(id) == 1) {
			return true;
		} else if (isPartner(id)) {
			return false;
		} else if (getChildCount(getParentID(id)) == 1) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 第一子かどうかの判定（第1世代はtrue、配偶者はfalse）
	 * @param id 個人ID
	 * @return 第一子かどうか
	 */
	public static boolean isEldest(int id) {
		// 親の第一子のIDと一致するかどうかで判定
		if (getGenNum(id) == 1 || id == getFirstChildID(getParentID(id))) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 末子かどうかの判定（第1世代はtrue、配偶者はfalse）
	 * @param id 個人ID
	 * @return 末子かどうか
	 */
	public static boolean isYoungest(int id) {
		// 親の第一子のIDと一致するかどうかで判定
		if (getGenNum(id) == 1 || id == getLastChildID(getParentID(id))) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * ひとり親かどうかの判定
	 * @param id 個人ID
	 * @return ひとり親かどうか
	 */
	public static boolean isSingleParent(int id) {
		if (hasChild(id) && !hasPartner(id)) {
			return true;
		} else {
			return false;
		}
	}
	
	// ■配偶者の調査
	/**
	 * 指定IDから配偶者のIDを取得（本人が配偶者のとき 0、存在しないときも動作）
	 * @param baseID 編集中の個人ID
	 * @return 配偶者のID
	 */
 	public static int getPartnerID(int baseID) {
		if (isPartner(baseID)) {
			return 0;
		}
		// baseIDの頭の 1 を 2 に書き換える = 配偶者のID
		int partnerID = Integer.parseInt(String.valueOf(baseID).replaceAll("^1", "2"));
		return partnerID;
	}
	/**
	 * 配偶者が存在するかどうか（本人が配偶者のとき false）
	 * @param baseID 編集中の個人ID
	 * @return 配偶者が存在するかどうか
	 */
	public static boolean hasPartner(int baseID) {
		if (isPartner(baseID)) {
			return false;
		}
		return isExistID(getPartnerID(baseID));
	}
	/**
	 * 配偶者を追加できるかどうか（できないときはメッセージを表示）
	 * @param baseID 編集中の個人ID
	 * @return 配偶者を追加できるかどうか
	 */
	public static boolean chkAddPartner(int baseID) {
		if (isPartner(baseID)) {
			System.out.println(getName(baseID) + "さんは血縁ではなく配偶者です。");
			System.out.println("配偶者に配偶者を追加することはできません。");
			return false;
		} else if (hasPartner(baseID)) {
			System.out.println(getName(baseID) + "さんには、配偶者として" +
						getName(getPartnerID(baseID)) + "さんが登録されています。");
			System.out.println("配偶者を2人以上登録することはできません。");
			return false;
		} else {
			return true;
		}
		
	}
	
	// ■親の調査
	/**
	 * 指定IDから親のIDを取得（第1世代は 0）
	 * @param baseID 編集中の個人ID
	 * @return 親のID
	 */
	public static int getParentID(int baseID) {
		if (isPartner(baseID) || getGenNum(baseID) == 1) {
			return 0;
		} else {
			// baseIDの末尾の1文字を削除 = 親のID
			int parentID = Integer.parseInt(String.valueOf(baseID).replaceAll(".$", ""));
			return parentID;
		}
	}
	
	// ■子の調査
	/**
	 * 子の数を取得（配偶者は 0、子の配偶者を含む）
	 * @param baseID 編集中の個人ID
	 * @return 子の数
	 */
	public static int getChildCount(int baseID) {
		int childCount = 0;		// リセット
		if (isPartner(baseID) || getGenNum(baseID) == getMaxGen()) {
			return 0;
		} else {
			for (int id : getChildGenFromID(baseID).getGenIDs()) {
				if (baseID == getParentID(id)) {
					childCount++;
				}
			}
		}
		return childCount;
	}
	/**
	 * 子が存在するかどうかの判定
	 * @param baseID 編集中の個人ID
	 * @return 子が存在するかどうか
	 */
	public static boolean hasChild(int baseID) {
		return (getChildCount(baseID) > 0);
	}
	/**
	 * 実子の数を取得（配偶者は 0、子の配偶者は含まない）
	 * @param baseID 編集中の個人ID
	 * @return 実子の数
	 */
	public static int getBloodChildCount(int baseID) {
		int childCount = 0;		// リセット
		if (isPartner(baseID) || getGenNum(baseID) == getMaxGen()) {
			return 0;
		} else {
			for (int id : getChildGenFromID(baseID).getGenIDs()) {
				if (baseID == getParentID(id) && !isPartner(id)) {
					childCount++;
				}
			}
		}
		return childCount;
	}
	/**
	 * 実子のIDを配列で取得（配偶者からは 0、子の配偶者は含まない）
	 * @param baseID 編集中の個人ID
	 * @return 実子のIDの配列（昇順ソート）
	 */
	public static int[] getChildIDs(int baseID) {
		int[] childIDs = new int[getChildCount(baseID)];
		if (isPartner(baseID) || !hasChild(baseID)) {
			childIDs[0] = 0;
			return childIDs;
		}
		Generation genChild = getChildGenFromID(baseID);
		int i = 0;	// ループカウンタ
		for (int id : genChild.getGenIDs()) {
			if (getParentID(id) == baseID) {
				childIDs[i] = id;
				i++;
			}
		}
		Arrays.sort(childIDs);		// 配列を昇順にソート
		return childIDs;
	}
	/**
	 * 第一子のID（一人っ子も該当、子がいなければ 0）
	 * @param baseID 編集中の個人ID
	 * @return 第一子のID
	 */
	public static int getFirstChildID(int baseID) {
		if (!hasChild(baseID)) {
			return 0;
		}
		return getChildIDs(baseID)[0];
	}
	/**
	 * 末子のID（一人っ子も該当、子がいなければ 0）
	 * @param baseID 編集中の個人ID
	 * @return 末子のID
	 */
	public static int getLastChildID(int baseID) {
		if (!hasChild(baseID)) {
			return 0;
		}
		return getChildIDs(baseID)[getChildCount(baseID) - 1];
	}
	/**
	 * 次に追加する子のID（追加不可なら 0、メッセージも表示）
	 * @param baseID 編集中の個人ID
	 * @return 次に追加する子のID
	 */
	public static int getNextChildID(int baseID) {
		// baseIDがINT_MAX_GEN_LIMITだったら 0（追加不可）
		if (getGenNum(baseID) == getMAX_GEN_LIMIT()) {
			System.out.print("第");
			System.out.print(getMAX_GEN_LIMIT());
			System.out.print("世代の子供（第");
			System.out.print((getMAX_GEN_LIMIT() + 1));
			System.out.println("世代）を登録することはできません");
			return 0;
		}
		if (isPartner(baseID)) {		// baseIDが配偶者なら血縁者のIDで継続
			String strBloodID = Integer.valueOf(baseID).toString().replaceFirst("2", "1");
			baseID = Integer.parseInt(strBloodID);
		}
		if (!hasChild(baseID)) {		// 子がまだ存在しないとき
			return (baseID * 10) + 1;
		} else {					// 子が存在したら、末子のID + 1
			return getLastChildID(baseID) + 1;
		}
	}
}
