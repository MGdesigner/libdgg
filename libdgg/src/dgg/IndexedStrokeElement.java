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

//import java.util.Hashtable;
import java.util.ArrayList;
/**
 * @author mgdesigner
 *
 */
public class IndexedStrokeElement {
	String 部件名=null;
	byte[] 出現位置;
	int 筆畫數;
	
/*	一部件出現某筆畫的位置，例如日的橫出現在是2,3（0為首位）*/
	
	public IndexedStrokeElement(byte 要index的筆畫type,CharComponent 部件,String 部件名)
	{
		this.部件名=部件名;
		
		筆畫數=部件.get筆畫數();
		
		char tmp=(""+要index的筆畫type).charAt(0);
		indexStroke(部件.get筆序(), tmp);
		
	}
	
	private void indexStroke(String 筆序,char type){		
		
		//System.out.println("index處理"+部件名+"的筆序="+筆序);
		int count=0;
		//先計算陣列需要多大
		for (int i = 0; i < 筆序.length(); i++) {
			
			if (筆序.charAt(i)==type)count++;
		}
		//出現位置改成byte array
		
		ArrayList al出現位置=new ArrayList(10);
		for (byte i = 0; i < 筆序.length(); i++) {
			//System.out.println("筆序.charAt(i)="+筆序.charAt(i)+",type="+type);
			if (筆序.charAt(i)==type)al出現位置.add(new Byte(i));
		}
		//Byte.byteValue() ;
		//為了加快速度，儲存改用靜態陣列
		//System.out.println("al出現位置.size()="+al出現位置.size());
		Byte[] tmp=(Byte[])al出現位置.toArray(new Byte[al出現位置.size()]);		
		
		//String[] stringArray = (String[])list.toArray(new String[list.size()]);
		出現位置=new byte[al出現位置.size()];
		for (int i = 0; i < 出現位置.length; i++) {
			出現位置[i]=tmp[i].byteValue();
		}
		//System.out.println("出現位置.length="+出現位置.length);
	}
	
	/**
	 * 查詢是否有該筆形存在
	 */
	public static boolean isStroke_exist(byte type,String 筆序){		
		if(筆序.indexOf(""+type)>=0)return true;//找到第1筆就回報
		else return false;
	}
}
