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
 * @author mgdesigner 
 * 12/12增加判讀註解（#）功能
 *  12/13 字碼表改以String當鑰匙，末級部件鑰匙的第2碼為液體字代碼
 * 12/28改良化字的指令，改為一班習慣以字的尺寸來衡量
 * 1/5/2006 開始動態組的實作，試作無recursize的作法
 */
package dgg;

import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.lang.*;
import java.io.LineNumberReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;
import java.awt.Font;
import java.util.*;
import java.io.UnsupportedEncodingException;

public class FontDB {
	final int 輸入法顯示區限制數量 = 60;

	public final boolean DEBUG = false;

	public boolean need_index = false;

	private final String FILENAME = "single.fnt";

	public final boolean use系統字 = false;

	public char test;

	private InputStreamReader filerd;

	public Hashtable 字碼表;

	public static final byte 細線體 = 0;

	public static final byte 圓體 = 1;

	public static final byte 黑體 = 2;
	
	public static final byte 萌體 = 3;

	private GeneralPath glyph;

	private Font testfont;

	private IDSparser iDSparser;

	private MStrokeSequenceDB 筆序DB = null;

	public FontDB() {

		try { // TRY始
			// 開檔
			System.out.println("讀："
					+ new File("fonts" + File.separator + FILENAME));
			// filerd = new FileReader(new
			// File(System.getProperty("user.dir"),"fonts" + File.separator +
			// FILENAME));
			// FileInputStream fis = new FileInputStream(new
			// File(System.getProperty("user.dir"); try/
			try {
				filerd = new InputStreamReader(new FileInputStream(new File(
						System.getProperty("user.dir"), "fonts"
								+ File.separator + FILENAME)), "UTF8");
			} catch (UnsupportedEncodingException e) {
				System.err
						.println("ur system encoding did'nt support UTF8,this program need it");
			}

			System.out.println("file encode=" + filerd.getEncoding());
			this.parse_file();

		} // TRY末
		catch (FileNotFoundException e) { // CACH始
			System.err.println("找無字形檔");
			// TODO: handle exception
		} // CACH末
		finally {
			try { // TRY始
				filerd.close();// 關檔
				System.out.println("檔案讀完了");
				if (need_index) index();

			} // TRY末
			catch (IOException e) { // CACH始
				// TODO: handle exception
			} // CACH末

		}
		glyph = new GeneralPath(GeneralPath.WIND_NON_ZERO);// 產生畫字的GeneralPath
		iDSparser = new IDSparser(字碼表);

	}

	private void parse_file() {
		LineNumberReader lnreader = new LineNumberReader(filerd);
		字碼表 = new Hashtable(71820);// 目前總共有71814個（本來有83202個，但後來single.fnt抽掉一萬多個）
		筆序DB = new MStrokeSequenceDB();// indexed筆序資料，供查詢用
		CharComponent 目前部件;
		ComponentFrameGroup 子部件組 = new ComponentFrameGroup();
		int counter = 0;
		try { // TRY始
			StringBuffer buffer = null;
			String nline = null;
			while (true) { // while始
				nline = lnreader.readLine();
				if (nline != null) { // IF
					buffer = new StringBuffer(nline);
					counter++;
					// 鑰匙放字碼名
					// String u碼 = buffer.charAt();//
					// 該字字碼，String是UTF8，涵蓋的範圍比utf16的char廣
					String u碼 = buffer.substring(buffer.indexOf("|") + 1,
							buffer.indexOf("|") + 2);// 該字字碼，String是UTF8
					byte 異No = (byte) Character.getNumericValue(buffer
							.charAt(buffer.indexOf("|") + 2));
					StringBuffer data;
					if (buffer.charAt(0) == '#' && DEBUG) {
						System.out.println(buffer);
					}// 註解，不做任何處置，不過目前會印到console

					else if (buffer.charAt(0) == '!') { // ELIF始
						// 第1字元是!，為末級部件
						data = new StringBuffer(buffer.substring(buffer
								.indexOf("=") + 1));
						int[] 筆畫 = new int[data.length() / 2];// 有幾個bytes
						// System.out.println("筆畫.length="+筆畫.length);
						for (int i = 0; i < 筆畫.length; i++) { // FOR始
							try { // TRY始
								筆畫[i] = (int) Integer.parseInt(data.substring(
										(i << 1), (i << 1) + 2), 16);
							} // TRY末
							catch (NumberFormatException e) { // CACH始
								// TODO: handle exception
							} // CACH末
						} // FOR末
						字碼表.put("" + u碼 + 異No, new CharComponent(筆畫, "" + u碼
								+ 異No));// 異體字要能查詢到
					} // ELIF末

					else { // ELSE始
						// 所以最後這個是非末級部件了
						data = new StringBuffer(buffer.substring(buffer
								.indexOf("=") + 1));
						int 子組件數 = data.length() / 10;
						子部件組.子組件 = new String[子組件數];
						子部件組.框 = new int[子組件數][4];
						// String 框str = data.substring(2);//從2到最後共8碼
						for (int i = 0; i < 子組件數; i++) { // FOR始
							子部件組.子組件[i] = data.substring(i * 10, i * 10 + 2);// 0~1
							// data=data.delete(0, 2);//去除掉已處理過的兩碼

							/**
							 * 子組件框[i][0]左上角x座標 子組件框[i][1]左上角y座標 子組件框[i][2]寬
							 * 子組件框[i][3]高
							 */
							for (int j = 0; j < 4; j++) { // FOR始
								try { // TRY始
									// System.out.println("j="+j);
									子部件組.框[i][j] = Integer
											.parseInt(
													data
															.substring(
																	(10 * i + (j + 1) * 2),
																	(10 * i + (j + 1) * 2) + 2),
													16);
								} // TRY末
								catch (NumberFormatException e) { // CACH始
									// TODO: handle exception
								} // CACH末

							} // FOR末

						} // FOR末
						目前部件 = new CharComponent(子部件組, 字碼表);
						字碼表.put("" + u碼 + 異No, 目前部件);
						目前部件 = null;

					} // ELSE末

				} // IF end
				else
					break;
			} // while末

		} // TRY末
		catch (IOException e) { // CACH始

		} // CACH末

	}

	public void drawComponent(Graphics2D g2, String IDSeqS, int size, float x,
			float y, Color color, int 字形, boolean antialising) {
		g2.setColor(color);
		int 筆畫數 = 0;

		char[] IDS = IDSeqS.toCharArray();

		// -草|日月
		long 畫前 = System.currentTimeMillis();

		Character tmp;
		CharComponent 部件;
		// 以外的字符壓入堆疊

		// idsstack.push(new Character(IDS[i]));
		部件 = iDSparser.gen(IDSeqS);
		筆畫數 = 部件.getLayoutRank();
		int stroksize=0;
		
		/**
		 * 權衡size、文字複雜度調整stroke厚度
		 * */
		if(部件.getLayoutRank()<60) stroksize=size/10*6/10;
			else if (部件.getLayoutRank()>60 && 部件.getLayoutRank()<100)stroksize=size/10/2-7;
				//else if (部件.getLayoutRank()>40 && 部件.getLayoutRank()<60)stroksize=size/10/6;
					//else if (部件.getLayoutRank()>60 && 部件.getLayoutRank()<80)stroksize=size/10/8;
			else stroksize=size/10/4;
		if (stroksize<1)stroksize=1;
		//----------------------------------------------
		
		if (antialising)
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		// 日後得能因應字的大小調整比畫粗細
		switch (字形) { // SW始
		case 1:// 圓體
			
			g2.setStroke(new BasicStroke(stroksize ,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));			
			break;
		case 2:// 黑體
			g2.setStroke(new BasicStroke( stroksize,BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));// 設定筆觸風格
			break;
		case 3:// 萌體
			g2.setStroke(new BasicStroke( stroksize*3/2,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));// 設定筆觸風格
			break;

		default:// 細線體
			break;
		} // SW末
		if (use系統字) { // IF始
			g2.setFont(g2.getFont().deriveFont((float) size));
			g2.drawString(IDSeqS, x, y + size);
		} // IF末
		else { // ELSE
			// g2.translate(x, y);
			// g2.scale(size>>8, size>>8);

			g2.draw(部件.write(glyph, size, size, x, y, false));
		} // ELSE end

		long 畫後 = System.currentTimeMillis();
		System.out.println("描繪時間=" + (畫後 - 畫前));
		System.out.println("本字的筆畫數為" + 筆畫數);
		glyph.reset(); // 清除
	}

	public Hashtable get字碼表() {
		return 字碼表;
	}

	public String 查部件組成(char 字) {
		CharComponent 字部件 = (CharComponent) 字碼表.get("" + 字 + '0');
		if (!字部件.is末級()) { // IF
			StringBuffer buffer = new StringBuffer();

			for (int i = 0; i < 字部件.get子組件().length; i++) { // FOR
				buffer.append(字部件.get子組件()[i].charAt(0));// 不傳回異體字碼
			} // FOR
			return buffer.toString();
		} // IF
		else
			return "" + 字;// 是末級部件的話就拆不了了

	}

	public String get_charhash(String IDSeqS) {
		CharComponent 字;
		字 = iDSparser.gen(IDSeqS);
		return 字.get筆序();
	}

	private void index() {
		System.out.println("indexing筆序全文檢索資料庫   開始");
		CharComponent 目前部件;
		Enumeration keys = 字碼表.keys();
		while (keys.hasMoreElements()) {
			String 部件名 = (String) keys.nextElement();
			// System.out.println("indexing"+部件名);

			目前部件 = (CharComponent) 字碼表.get(部件名);
			for (byte i = 1; i <= 6; i++) {
				try {
					if (IndexedStrokeElement.isStroke_exist(i, 目前部件.get筆序())) {

						筆序DB.addElement(i, new IndexedStrokeElement(i, 目前部件,
								部件名), 部件名);
						// System.out.println("indexing over"+部件名);
					}
				} catch (NullPointerException e) {
					// System.err.println("原檔案的"+部件名+"字有錯誤");
					筆序DB.rmElement(i, 部件名);
					// TODO: handle exception
				}
			}
		}
		System.out.println("indexing筆序全文檢索資料庫  結束");

	}

	public String 筆序碼查字(String txt) {
		if (need_index) {
			Hashtable tmp = 筆序DB.search(txt);
			StringBuffer buf = new StringBuffer();
			Enumeration tmp_keys = tmp.keys();
			int amount = 0;
			while (tmp_keys.hasMoreElements() && amount < 輸入法顯示區限制數量) {
				String 部件名 = (String) tmp_keys.nextElement();
				IndexedStrokeElement e = (IndexedStrokeElement) tmp.get(部件名);
				buf.append(e.部件名);
				amount++;
			}
			return buf.toString();
		}
		else return "無";
	}
	
	public int getLayoutRank(String IDS) {
		
		
		return this.iDSparser.gen(IDS).getLayoutRank();
		
	}

}
