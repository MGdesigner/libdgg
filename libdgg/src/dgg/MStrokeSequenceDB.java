/*
* * 本軟體係應用中華民國發明專利號碼第I254863號「可攜式造字引擎」專利
* 本程式引用專利license:可攜式造字引擎專利公眾授權條款KSAFAEA
* 該條款可在anouncement.zhongwen.tw取得

* Copyright (c) 2007 著作權由張正一所有。著作權人保留一切權利。
*
* 這份授權條款，在使用者符合以下三條件的情形下，授予使用者使用及再散播本
* 軟體包裝原始碼及二進位可執行形式的權利，無論此包裝是否經改作皆然：
* 
* * 對於本軟體原始碼的再散播，必須保留上述的版權宣告、此三條件表列，以
*   及下述的免責聲明。
* * 對於本套件二進位可執行形式的再散播，必須連帶以文件以及／或者其他附
*   於散播包裝中的媒介方式，重製上述之版權宣告、此三條件表列，以及下述
*   的免責聲明。
* * 未獲事前取得書面許可，不得使用張正一或本軟體貢獻者之名稱，
*   來為本軟體之衍生物做任何表示支持、認可或推廣、促銷之行為。
* 
* 免責聲明：本軟體是由張正一及本軟體之貢獻者以現狀（\as is\）提供，
* 本軟體包裝不負任何明示或默示之擔保責任，包括但不限於就適售性以及特定目
* 的的適用性為默示性擔保。張正一及本軟體之貢獻者，無論任何條件、
* 無論成因或任何責任主義、無論此責任為因合約關係、無過失責任主義或因非違
* 約之侵權（包括過失或其他原因等）而起，對於任何因使用本軟體包裝所產生的
* 任何直接性、間接性、偶發性、特殊性、懲罰性或任何結果的損害（包括但不限
* 於替代商品或勞務之購用、使用損失、資料損失、利益損失、業務中斷等等），
* 不負任何責任，即在該種使用已獲事前告知可能會造成此類損害的情形下亦然。
 */
package dgg;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author mgdesigner 這是indexing過的機器筆序碼DB，跟mICROsOFT公司可沒關係喔
 */
public class MStrokeSequenceDB {
	
	Hashtable[] index;// 八種筆畫序查表index

	Hashtable sum;// 查詢結果=null

	public MStrokeSequenceDB() {
		index = new Hashtable[7];
		index[0] = null;// 有錯誤的筆畫，暫時不用，或是有問題的
		index[1] = new Hashtable(10000);
		index[2] = new Hashtable(10000);
		index[3] = new Hashtable(10000);
		index[4] = new Hashtable(10000);
		index[5] = new Hashtable(10000);
		index[6] = new Hashtable(10000);		
	}

	public void addElement(byte 筆形, IndexedStrokeElement 單元, String 部件名) {
		index[筆形].put(部件名, 單元);
	}

	public void rmElement(byte 筆形, String 部件名) {
		index[筆形].remove(部件名);
	}

	public Hashtable search(String str) {
		// str是被比對字串
		sum = new Hashtable();

		for (int i = 0; i < str.length(); i++) {
			// 從頭掃描字串的每一字元
			//System.out.println("search" + i + "位的" + str.charAt(i));
			search_indexeddb(i, str.charAt(i));
		}

		return sum;
	}

	private void search_indexeddb(int i, char ch) {
		int type = Character.getNumericValue(ch);
		// System.out.println("index[type].size()"+index[type].size());
		if (i == 0) {// 第0次取得一比較基準
			// System.out.println("type="+type);
			sum = (Hashtable) (index[type].clone());

			// System.out.println("sum.size()="+sum.size());
		} else {// 非第一次

			// System.out.println("取出src陣列裡合「在i位置要有type型態的筆觸」的元素到sum");
			Enumeration sum_keys = sum.keys();
			
			while (sum_keys.hasMoreElements()) {
				String 部件名 = (String) sum_keys.nextElement();
				// 目前部件=(CharComponent)字碼表.get( 部件名 );
				IndexedStrokeElement 比對者 = (IndexedStrokeElement) sum.get(部件名);
				IndexedStrokeElement 被比對者 = (IndexedStrokeElement) index[type]
						.get(部件名);

				if (被比對者 == null) {// 該字根本沒那筆畫，例如「三」沒有stroke5
					// 刪除法
					sum.remove(部件名);
				} else {

					boolean matched = false;
					for (int j = 0; j < 被比對者.出現位置.length; j++) {// 這個陣列都不大
						// 找沒有i的
						if (i == 被比對者.出現位置[j])
							matched = true;
					}
					if (!matched)
						sum.remove(部件名);
				}

			}
	
			//System.out.println("sumsize=" + sum.size());
		}

	}

}
