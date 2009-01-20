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

/**
 * 本類別描述的是組成文字的"部件"
 * @author mgdesigner 
 * 
 * 12/6　增加末級與非末級的區別 
 * 12/13　不另存異體字代碼，改放在查詢的鑰匙
 * 12/27　改良只有一筆畫的部件組不出來的問題（因為寬或是高=0所至），yap的方法是用強制+1避開，我原來的作法是用分支跳躍來改
 * 12/28　
 * 12/29　把寫字指令傳的參數由倍數改成尺寸
 * 2006
 * 1/3作簡單的最佳化、去掉get筆形()，外面直接呼叫寫()、增加get筆畫數()功能
 * 1/5發現動態組字時，有單取橫向或縱向最大框的需要，固把is遞迴變數擴充為兩個
 * 10/9修正debug模式時，外框的描繪錯誤
 */
package dgg;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.GeneralPath;
import java.util.Hashtable;
import java.util.Vector;
import java.lang.Math;

import dgg.Utils;

public class CharComponent {
	public final boolean DBG_MBF = false;

	private int[] 筆畫 = null;

	private String[] 子組件 = null;

	private int[][] 子組件框 = null;

	private boolean 末級;// 是否為末級部件

	private Hashtable 字碼表 = null;

	// 記載最小框架（MBF）用
	MBF myMBF = null;

	public CharComponent(ComponentFrameGroup 甲組件框, Hashtable 字碼表) {
		/*
		 * 複合部件的建構子 由於會有查詢其他子部件代碼的需要，所以要從外面傳入字碼表的參考
		 */
		末級 = false;
		this.字碼表 = 字碼表;
		this.子組件 = 甲組件框.子組件;
		this.子組件框 = 甲組件框.框;
		myMBF = new MBF(this);
	}

	public CharComponent(int[] 筆畫, String name) {
		// 末級部件的建構子
		this.筆畫 = 筆畫;
		this.字碼表 = 字碼表;
		末級 = true;
		myMBF = new MBF(this);
		子組件 = new String[1];
		子組件框 = new int[1][4];
		子組件[0] = name;
		子組件框[0][0] = 0;
		子組件框[0][1] = 0;
		子組件框[0][2] = 255;
		子組件框[0][3] = 255;
		/**
		 * 子組件框[0][0]=myMBF.getX(); 子組件框[0][1]=myMBF.getY();
		 * 子組件框[0][2]=(int)myMBF.getWidth(); 子組件框[0][3]=(int)myMBF.getHeight();
		 */
		// 末級部件只有單一很大很大的框架
	}

	public GeneralPath write(GeneralPath path, float width, float height,
			float 基點x, float 基點y, boolean 取框) {
		// 「寫字」
		float 始x = -1, 始y = -1, 末x = -1, 末y = -1;
		float 反曲x = -1, 反曲y = -1;
		boolean is曲線 = false;
		int i = 0;// 第幾筆觸
		float x倍 = width / 256f;// 整個字的橫向放大
		float y倍 = height / 256f;// 整個字的縱向放大
		int 框x = 0;
		int 框y = 0;
		float 框w = 256f;
		float 框h = 256f;
		float zFX = 0;// 框的橫向縮放
		float zFY = 0;// 框的縱向縮放
		// float 父w=256f;
		// float 父h=256f;
		// int 父x=0;
		// int 父y=0;

		if (取框) { // IF始
			// 如果是遞迴處理，要去除框架，且放大之
			框x = myMBF.getX();
			框w = myMBF.getWidth();
		}
		if (取框) { // IF始
			框y = myMBF.getY();
			框h = myMBF.getHeight();
		} // IF末

		if (末級) { // IF始
			zFX = 256f / 框w;
			zFY = 256f / 框h;
			while (i < 筆畫.length) {// WHILE // 一次處理讀4筆

				switch (筆畫[i + 1]) {// SWITCH
				case 0:// moveto
					始x = (筆畫[i + 2] - 框x) * zFX;
					始y = (筆畫[i + 3] - 框y) * zFY;

					path.moveTo(基點x + 始x * x倍, 基點y + 始y * y倍);
					break;
				case 1:// lineto
					末x = (筆畫[i + 2] - 框x) * zFX;
					末y = (筆畫[i + 3] - 框y) * zFY;

					if (!is曲線)
						path.lineTo(基點x + 末x * x倍, 基點y + 末y * y倍);
					else {
						path.curveTo(基點x + 始x * x倍, 基點y + 始y * y倍, 基點x + 反曲x
								* x倍, 基點y + 反曲y * y倍, 基點x + 末x * x倍, 基點y + 末y
								* y倍);
						is曲線 = false;// 曲線畫完，歸零
					} // 畫完以後，這次的末點，變成下次的始點
					始x = 末x;
					始y = 末y;
					break;
				case 2:// curve
					is曲線 = true;
					反曲x = (筆畫[i + 2] - 框x) * zFX;
					反曲y = (筆畫[i + 3] - 框y) * zFY;
					break;
				default:
					break;
				}// SWITCH
				i += 4;
			}// WHILE
		} // IF末

		else { // ELSE
			// 不是末級的話，要找出子部件與其框然後描繪之
			zFX = width / 框w;// 框的橫向縮放
			zFY = height / 框h;// 框的縱向縮放
			CharComponent 部件;
			for (int j = 0; j < 子組件.length; j++) { // FOR始

				float x = 基點x + (子組件框[j][0] - 框x) * zFX;// 左上角 x座標
				float y = 基點y + (子組件框[j][1] - 框y) * zFY;// 左上角 y座標
				float 子組件w = (float) 子組件框[j][2] * zFX;// 寬
				float 子組件h = (float) 子組件框[j][3] * zFY;// 高

				部件 = (CharComponent) 字碼表.get(子組件[j]);// 選字

				if (DBG_MBF) { // IF始
					// 描繪子組件外框
					path.moveTo(x, y);
					path.lineTo(x + x倍 * (float) 子組件框[j][2], y);
					path.lineTo(x + x倍 * (float) 子組件框[j][2], y + y倍
							* (float) 子組件框[j][3]);
					path.lineTo(x, y + y倍 * (float) 子組件框[j][3]);
					path.lineTo(x, y);
				} // IF末

				// System.out.println(子組件[j]);
				// System.out.println("框架基點=("+x+","+y+"),子組件w="+子組件w+",子組件h="+子組件h);
				部件.write(path, 子組件w, 子組件h, x, y, true);
				/*
				 * 子組件框[j][0]是框架基點x 子組件框[j][1]是框架基點y 子組件框[j][2]是框架寬
				 * 子組件框[j][3]是框架高
				 */
			} // FOR末

		} // ELSE end
		return path;
	}

	public boolean is末級() {
		return 末級;
	}

	public int[] get筆畫() {
		return 筆畫;
	}

	public int get筆畫數() {
		// 遞迴的得出筆畫數
		int 筆畫數 = 0;
		CharComponent 部件;
		if (!末級) { // IF始
			for (int j = 0; j < 子組件.length; j++) { // FOR始
				部件 = (CharComponent) 字碼表.get(子組件[j]);
				if (部件 != null)
					筆畫數 = 筆畫數 + 部件.get筆畫數();// single.fnt有嚴重錯誤:末級部件沒有筆畫
			} // FOR末
		} // IF末
		else { // ELSE
			int i = 0;
			while (i < 筆畫.length) {
				switch (筆畫[i + 1]) {
				case 0:
					筆畫數++;
					break;
				default:
					break;
				}
				i += 4;
			}
		} // ELSE end
		return 筆畫數;
	}

	public String[] get子組件() {
		if (子組件 == null) { // IF始
			System.out.println("子組件為空");
		} // IF末
		return 子組件;
	}

	public int[][] get子組件框() {
		return 子組件框;
	}

	public void set子組件(String[] 子組件) {
		this.子組件 = 子組件.clone();
	}

	public void set子組件框(int[][] 子組件框) {
		this.子組件框 = 子組件框;
	}

	public MBF getMBF() {
		return myMBF;
	}

	public String get筆序() {
		StringBuffer hash = new StringBuffer();
		if (末級) { // IF始
			// 得出部件序hash
			int i = 0;
			boolean is曲線 = false;
			Vector 筆畫序列 = new Vector(20);
			SSegment 當前筆鋒 = null;
			Stroke 當前筆畫 = null;
			int 始x = 0;
			int 中x = 0;
			int 末x = 0;
			int 始y = 0;
			int 中y = 0;
			int 末y = 0;
			// System.out.println("本部件筆畫-----------------");
			while (i < 筆畫.length) {// WHILE // 一次處理讀4筆

				switch (筆畫[i + 1]) {// SWITCH
				case 0:// moveto
					if (當前筆畫 != null) {
						// 讀到新的一個筆畫，表示之前的筆畫已經結束
						筆畫序列.add(當前筆畫);
					}
					始x = 筆畫[i + 2];
					始y = 筆畫[i + 3];
					當前筆畫 = new Stroke(始x, 始y);
					// System.out.println("--筆畫----");
					break;

				case 1:
					// 0001、0002都當成折線看待
					末x = 筆畫[i + 2];
					末y = 筆畫[i + 3];
					// //System.out.println("(末x,末y) (始x,始y)比對，得出筆鋒走勢");
					/**
					 * y=末y-始y,>0是往右，<0是往左 x=末x-始x,>0是往下，<0是往上
					 */
					int x,
					y;
					x = 末x - 始x;
					y = 末y - 始y;

					// System.out.println("x="+x+",y="+y+"x^2+y^2="+(Math.abs(x^2)+Math.abs(y^2)));
					/**
					 * 這裡很怪，平方居然可以是負值，可能是JVM的bug，有空得來回報s
					 */
					int len = (int) Math.sqrt((double) (Math.abs(x ^ 2) + Math
							.abs(y ^ 2)));

					// System.out.println("len="+len);
					// System.out.println("筆鋒長度平方="+len平方);
					// 判斷序以順時鐘從向右開始
					boolean isLong = false;

					if (y == 0) {
						當前筆畫.add筆鋒((byte) 1);
					}// System.out.println("筆鋒：向右");}//向右
					else if (x > 0 && y > 0) {
						當前筆畫.add筆鋒((byte) 2, len);
					}// System.out.println("筆鋒：右下");}//右下
					else if (x == 0 && y > 0) {
						當前筆畫.add筆鋒((byte) 3);
					}// System.out.println("筆鋒：向下");}//向下
					else if (x < 0 && y > 0) {
						當前筆畫.add筆鋒((byte) 4, len);
					}// System.out.println("筆鋒：左下");}//左下
					else if (x < 0 && y < 0) {
						當前筆畫.add筆鋒((byte) 6, len);
					}// System.out.println("筆鋒：左上");}//左上
					else if (x == 0 && y > 0) {
						當前筆畫.add筆鋒((byte) 7);
					}// System.out.println("筆鋒：向上");}//向上
					else if (x > 0 && y < 0) {
						當前筆畫.add筆鋒((byte) 8, len);
					}// System.out.println("筆鋒：右上");}//右上
					始x = 末x;
					始y = 末y;
					break;
				default:
					// 0002不處理，對stroke判斷無幫助
					break;
				}// SWITCH
				i += 4;
			}// WHILE
			// 最後一筆由於不會有下一筆0000得知其結束，所以要在這邊做
			筆畫序列.add(當前筆畫);
			// System.out.println("筆序代碼付與、校正");
			for (int j = 0; j < 筆畫序列.size(); j++) {

				Stroke stroke = (Stroke) 筆畫序列.get(j);
				Stroke prv_stroke;
				stroke.ananysisStrokeType_6();// 不用8筆改用6筆
				// 判斷橫向點
				// System.out.println("判斷橫向點");
				if ((stroke.getStrokeType() == 3 || stroke.getStrokeType() == 2)
						&& j > 0)// 是點，且還有上一筆
				{
					prv_stroke = (Stroke) 筆畫序列.get(j - 1);
					if (prv_stroke.getStrokeType() == 4)
						stroke.setStrokeType((byte) 4);// 前一筆四，那這一筆也一定是
					else if (prv_stroke.getStrokeType() == 3) {
						int x = stroke.起點x - prv_stroke.起點x;// 水平向起點距離差
						int y = stroke.起點y - prv_stroke.起點y;// 垂直像起點距離差
						if (x > y) {
							stroke.setStrokeType((byte) 4);
							prv_stroke.setStrokeType((byte) 4);
							// System.out.println("nxt_stroke="+nxt_stroke.getStrokeType());
							筆畫序列.setElementAt(prv_stroke, j - 1);
							筆畫序列.setElementAt(stroke, j);
						}
					}
				}
			}
			// System.out.println("輸出筆序代碼");
			for (int j = 0; j < 筆畫序列.size(); j++) {
				Stroke stroke = (Stroke) 筆畫序列.get(j);
				hash.append(stroke.getStrokeType());
			}

			// System.out.println("end-筆序代碼產生");
			hash.append("");// 把最後一筆畫加上去
		} // IF末
		else { // ELSE
			// 找子組件的hash
			CharComponent 部件;
			for (int j = 0; j < 子組件.length; j++) { // FOR始
				部件 = (CharComponent) 字碼表.get(子組件[j]);// 選字

				hash.append(部件.get筆序());
			}// FOR末
		} // ELSE end
		return hash.toString();
	}

	/**
	 * 這是產生空間配置評價的評比參數 以筆畫分析為基準 第一步取得筆畫數
	 * 筆畫數為2、4、6的，由於一筆畫就可以佔有比較大的面積（1、3只能決定直線），所以2、4、6獲得加權
	 * 又由於1,3要最少兩筆畫才能佔到二維面積，所以設定2 4 6的評價值為為2。
	 */
	public int getLayoutRank() {
		int rank = 0;
		rank += get筆畫數();
		String 筆序碼 = this.get筆序();
		// 因為等評價的筆畫數已經統計過了，這邊只要額外增加就可
		for (int i = 0; i < 筆序碼.length(); i++) {// f
			if (筆序碼.charAt(i) == '2' || 筆序碼.charAt(i) == '4')
				rank++;
			else if (筆序碼.charAt(i) == '5' || 筆序碼.charAt(i) == '6')
				rank += 2;

		}// f

		return rank;
	}

	public int getLayoutRank(boolean 組合方向) {
		int rank = 0;
		rank += get筆畫數();
		String 筆序碼 = this.get筆序();
		// 因為等評價的筆畫數已經統計過了，這邊只要額外增加就可
		if (組合方向) {// i
			// 垂直向
			for (int i = 0; i < 筆序碼.length(); i++) {// f
				if (筆序碼.charAt(i) == '2' || 筆序碼.charAt(i) == '4')
					rank++;
				else if (筆序碼.charAt(i) == '3' ||筆序碼.charAt(i) == '5' ||筆序碼.charAt(i) == '6')
					rank += 2;
			}// f
			
		}// i
		//水平向
		else {// e
			for (int i = 0; i < 筆序碼.length(); i++) {// f
				if (筆序碼.charAt(i) == '2' || 筆序碼.charAt(i) == '4')
					rank++;
				else if (筆序碼.charAt(i) == '1' || 筆序碼.charAt(i) == '6')
					rank += 2;
			}// f
		}// e

		return rank;
	}

}
