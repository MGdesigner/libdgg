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

import java.util.Vector;

/**
 * @author mgdesigner
 * 
 * 11/1single.fnt風、月等字要校定其長度，不然難以分辨3、4點與7、8的差別 金偏旁的上面點要集中不然會判斷錯誤 龜的筆順似乎有誤
 * 豐的第1筆錯寫成兩劃
 */
public class Stroke {
	private Vector 筆鋒序;

	public int 起點x;

	public int 起點y;

	private byte stroketype = 0;// 如果一直到顯示時，都是0，表示判斷不出來

	public Stroke(int 起點x, int 起點y) {
		this.起點x = 起點x;
		this.起點y = 起點y;
		筆鋒序 = new Vector(6);// 最多是順彎的五畫，事先分配好，減少記憶體的增量處理
	}

	public void add筆鋒(byte 方向, int 長度) {
		筆鋒序.add(new SSegment(方向, 長度));

	}

	public void add筆鋒(byte 方向) {
		筆鋒序.add(new SSegment(方向));
	}

	public Vector get筆鋒序() {
		return 筆鋒序;
	}

	public void ananysisStrokeType_8() {
		/**
		 * 這是八筆分析
		 *  1   2   3  　　 4        5              6               7   8
		 *  |     - 平點　縱點　順彎　逆彎　　/     \
		 *  
		 * */
		// 特別注意，single.fnt裏面有末級部件沒有筆畫的
		// System.out.println(筆鋒序.size());
		// if (筆鋒序.size()>0)

		SSegment 筆鋒0;
		SSegment 筆鋒1;
		/**
		 * 以下是筆鋒方向的參考
		 *    6   7    8 
		 *    5 筆鋒1
		 *    4   3    2 
		 */ 
		
		/* 有些末級部件不知是有問題還是如何只好這樣捕捉 */
		try {
			筆鋒0 = (SSegment) 筆鋒序.get(0);

			// System.out.println("筆鋒0.dir="+筆鋒0.dir);
			switch (筆鋒0.dir) {
			case 1:// 向右

				if (筆鋒序.size() == 1)
					stroketype = 1;// 筆畫只有一筆鋒

				else if (筆鋒序.size() >= 4) {
					筆鋒1 = (SSegment) 筆鋒序.get(1);
					SSegment 筆鋒2 = (SSegment) 筆鋒序.get(2);
					SSegment 筆鋒3 = (SSegment) 筆鋒序.get(2);
					if (筆鋒3.dir == 4)
						stroketype = 6;// （乙形）形的逆彎
					else if (筆鋒3.dir == 1)
						stroketype = 5;// 「辶」的耳朵形
					else
						stroketype = 5;
				} else {
					stroketype = 5;// 一定有折，而且漢字沒有折1,2,6,7,8方向的
				}
				break;

			case 2:// 向右下
				stroketype = 8;

				if (this.is_point())
					stroketype = 3;// 短的話是點
				else if (筆鋒序.size() != 1) {
					筆鋒1 = (SSegment) 筆鋒序.get(筆鋒序.size() - 1);// 跟最後一筆鋒比較
					if (筆鋒1.dir == 8)
						stroketype = 3;// 這是特例，但也是點（3點水最後一筆）
					else if (筆鋒1.dir == 6 || 筆鋒1.dir == 7 || 筆鋒1.dir == 8) {
						stroketype = 6;// 是逆彎

						for (int i = 0; i < 筆鋒序.size(); i++) {
							筆鋒0 = (SSegment) 筆鋒序.get(i);// 逐一檢查有無「轉彎點」
							if (筆鋒0.dir == 4) {
								stroketype = 5;// 是順彎
								break;
							}
						}
					}

				} else
					stroketype = 8;// 如果長的話...

				break;
			case 3:// 向下
				stroketype = 1;// 通則是1，若不是後面再修正
				if (筆鋒序.size() > 1) {
					筆鋒1 = (SSegment) 筆鋒序.get(1);
					if (筆鋒1.dir == 1 || 筆鋒1.dir == 2 || 筆鋒1.dir == 8)
						stroketype = 6;
				}
				break;
			case 4:// 向左下
				stroketype = 7;// 通則是向左下長線，若不是後面再修正
				if (筆鋒序.size() > 1) {
					筆鋒1 = (SSegment) 筆鋒序.get(1);
					if (筆鋒1.dir == 4)
						stroketype = 7;
					else if (筆鋒1.dir == 8 || 筆鋒1.dir == 1 || 筆鋒1.dir == 2)
						stroketype = 6;// 「女」形逆彎
				} else if (this.is_point())
					stroketype = 3;// 短的話是點

				break;

			case 8:// 向右上
				stroketype = 2;
				// if(this.is_point())stroketype=3;//太短是點

				break;

			default:
				break;
			}
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			// TODO: handle exception
		}

	}
	
	public void ananysisStrokeType_6() {
		/**這是六筆分析
		 * 1   2   3  4  5            6
		 *  - /    - \  順彎　逆彎
		 * */
		// 特別注意，single.fnt裏面有末級部件沒有筆畫的
		// System.out.println(筆鋒序.size());
		// if (筆鋒序.size()>0)

		SSegment 筆鋒0;
		SSegment 筆鋒1;
		/**
		 * 以下是筆鋒方向的參考
		 *    6   7    8 
		 *    5 筆鋒1
		 *    4   3    2 
		 */ 
		
		/* 有些末級部件不知是有問題還是如何只好這樣捕捉 */
		try {
			筆鋒0 = (SSegment) 筆鋒序.get(0);

			// System.out.println("筆鋒0.dir="+筆鋒0.dir);
			switch (筆鋒0.dir) {
			case 1:// 向右

				if (筆鋒序.size() == 1)
					stroketype = 3;// 筆畫只有一筆鋒

				else if (筆鋒序.size() >= 4) {
					筆鋒1 = (SSegment) 筆鋒序.get(1);
					SSegment 筆鋒2 = (SSegment) 筆鋒序.get(2);
					SSegment 筆鋒3 = (SSegment) 筆鋒序.get(2);
					if (筆鋒3.dir == 4)
						stroketype = 6;// （乙形）形的逆彎
					else if (筆鋒3.dir == 1)
						stroketype = 5;// 「辶」的耳朵形
					else
						stroketype = 5;
				} else {
					stroketype = 5;// 一定有折，而且漢字沒有折1,2,6,7,8方向的
				}
				break;

			case 2:// 向右下
				stroketype = 4;
				if (筆鋒序.size() != 1) {
					筆鋒1 = (SSegment) 筆鋒序.get(筆鋒序.size() - 1);// 跟最後一筆鋒比較					
					if (筆鋒1.dir == 6 || 筆鋒1.dir == 7 ) {
						stroketype = 6;// 是逆彎

						for (int i = 0; i < 筆鋒序.size(); i++) {
							筆鋒0 = (SSegment) 筆鋒序.get(i);// 逐一檢查有無「轉彎點」
							if (筆鋒0.dir == 4) {
								stroketype = 5;// 是順彎
								break;
							}
						}
					}
					else stroketype=5;//順彎

				} 

				break;
			case 3:// 向下
				stroketype = 1;// 通則是1，若不是後面再修正
				if (筆鋒序.size() > 1) {
					筆鋒1 = (SSegment) 筆鋒序.get(1);
					if (筆鋒1.dir == 1 || 筆鋒1.dir == 2 || 筆鋒1.dir == 8)
						stroketype = 6;//逆彎
				}
				break;
			case 4:// 向左下
				stroketype = 2;// 通則是向左下長線，若不是後面再修正
				if (筆鋒序.size() > 1) {
					筆鋒1 = (SSegment) 筆鋒序.get(1);					
					if (筆鋒1.dir == 8 || 筆鋒1.dir == 1 || 筆鋒1.dir == 2)
						stroketype = 6;// 「女」形逆彎
				} 
				break;

			case 8:// 向右上
				stroketype = 3;


				break;

			default:
				break;
			}
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			// TODO: handle exception
		}

	}

	public void setStrokeType(byte type) {
		this.stroketype = type;
	}

	public byte getStrokeType() {
		return stroketype;
	}

	private boolean is_point() {
		int len = 0;
		SSegment sseg;
		for (int i = 0; i < 筆鋒序.size(); i++) {
			sseg = (SSegment) 筆鋒序.get(i);
			len += sseg.length;
		}
		if (len <= 12)
			return true;// 12是參考值，太小是點，不過這個值必須要求字形檔的製作
		else
			return false;
	}
}
