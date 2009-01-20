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
 * 
 * 這個元件的工作，是自動做出一個新的字形（可以想成single.fnt上的一行）
 * 1/18/2007調整辶的內外組時，被包含字的框架大小
 * 目前的組合法還很ugly，以後再改
 * *更正「周」外圍的那個部件unicode碼
 */
package ids;
import java.awt.geom.GeneralPath;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Hashtable;
/**
 * @author mgdesigner
 *
 */
public class IDSparser {
    Hashtable 字碼表;
    Stack stack;
    public IDSparser(Hashtable 字碼表)
    {
	this.字碼表=字碼表;//只有傳參考喔
	stack=new Stack();
    }
    
    public CharComponent parse(String IDS){
	String[] str=null;
    	int[][] 框=null;
//	先看看字串開頭是不是一個IDC，以此判斷是不是一組IDS
	stack.clear();
	CharComponent char1;
	CharComponent char2;
	CharComponent char3;
	String[] char1子組件;
   String[] char2子組件;
   String[] char3子組件;
   int[][] char1框;
   int[][] char2框;
   int[][] char3框;
   int 比例[];
	int count=0;//組的次數;
	int 子組件數目=0;

	if (Character.UnicodeBlock.of(IDS.charAt(0)).equals(Character.UnicodeBlock.IDEOGRAPHIC_DESCRIPTION_CHARACTERS)) { //IF始
	    //是IDS得解析了	    
	    char[] IDSarray=IDS.toCharArray();
	   //倒過來作
	    for (int i = IDSarray.length-1; i >=0; i--) { //FOR始
		System.out.println(""+IDSarray[i]);
			if(Character.UnicodeBlock.of(IDSarray[i]).equals(Character.UnicodeBlock.IDEOGRAPHIC_DESCRIPTION_CHARACTERS))
			{
			    switch (IDSarray[i]) { //SW始
				case '\u2ff0'://左右組
				    
				    char1=(CharComponent)stack.pop();//日
				    char2=(CharComponent)stack.pop();//月
				    char1子組件= char1.get子組件();
				    char2子組件= char2.get子組件();
				    char1框=char1.get子組件框();
				    char2框=char2.get子組件框();
				    子組件數目=char1子組件.length+char2子組件.length;				    	  
				    str=new String[子組件數目];
				    	   框=new int[子組件數目][4];
			    	   
				    	   //左邊日				    
				    比例=判_縮小比例(char1,char2);			    	
					for (int j = 0; j < char1.get子組件().length; j++) { //FOR始
					str[j]=new String(char1子組件[j]);						
					框[j][0]=char1框[j][0]*比例[0]/10;//x
					框[j][1]=char1框[j][1];//y
					框[j][2]=char1框[j][2]*比例[0]/10;
					框[j][3]=char1框[j][3];
					} //FOR末
				    	
				    //右邊月			    	
				    for (int j = 0; j < char2.get子組件().length; j++) { //FOR始
						str[j+char1.get子組件().length]=new String(char2子組件[j]);
						框[j+char1子組件.length][0]=char2框[j][0]*比例[1]/10+256*比例[0]/10;
						框[j+char1子組件.length][1]=char2框[j][1];		
						框[j+char1子組件.length][2]=char2框[j][2]*比例[1]/10;
						框[j+char1子組件.length][3]=char2框[j][3];						
						
											
				    } //FOR末				    
				 
				    	
				    
			    break;
				case '\u2ff1'://上下組		
				    char1=(CharComponent)stack.pop();//例如艹
				    char2=(CharComponent)stack.pop();//例如明	    
				    		    	
				    	
				    char1子組件= char1.get子組件();
				    char2子組件= char2.get子組件();
				    char1框=char1.get子組件框();
				    char2框=char2.get子組件框();
				    子組件數目=char1子組件.length+char2子組件.length;
				    str=new String[子組件數目];
				    框=new int[子組件數目][4];
				    比例=判_縮小比例(char1,char2);
				    
				    	
				    	for (int j = 0; j < char1.get子組件().length; j++) { //FOR始
							str[j]=new String(char1子組件[j]);
							框[j][0]=char1框[j][0];//x
							框[j][1]=char1框[j][1]*比例[0]/10;//y
							框[j][2]=char1框[j][2];//寬
							框[j][3]=char1框[j][3]*比例[0]/10;//高						
							} //FOR末
						    	
						    //右邊月			    	
						    for (int j = 0; j < char2.get子組件().length; j++) { //FOR始
								str[j+char1.get子組件().length]=new String(char2子組件[j]);
								框[j+char1子組件.length][0]=char2框[j][0];
								框[j+char1子組件.length][1]=char2框[j][1]*比例[1]/10+256*比例[0]/10;		
								框[j+char1子組件.length][2]=char2框[j][2];
								框[j+char1子組件.length][3]=char2框[j][3]*比例[1]/10;						
								
													
						    } //FOR末		    		    
				    	
				    
			    break;
				case '\u2ff4'://包含組
					char1=(CharComponent)stack.pop();//囗
				    char2=(CharComponent)stack.pop();//或
				    char1子組件= char1.get子組件();
				    char2子組件= char2.get子組件();
				    char1框=char1.get子組件框();
				    char2框=char2.get子組件框();
				    子組件數目=char1子組件.length+char2子組件.length;
				    str=new String[子組件數目];
				    框=new int[子組件數目][4];
				    for (int j = 0; j < char1.get子組件().length; j++) { //FOR始
						str[j]=new String(char1子組件[j]);
						框[j][0]=char1框[j][0];//x
						框[j][1]=char1框[j][1];//y
						框[j][2]=char1框[j][2];//寬
						框[j][3]=char1框[j][3];//高						
					} //FOR末
				    if (char1.get子組件()[0].equals(""+'門'+'0')) { //IF		
				    	
				    	//with result do begin Left :=70; top:=112; Right:=180; bottom:=220;  end; exit;	
						for (int j = 0; j < char2.get子組件().length; j++) { //FOR始
							str[j+char1.get子組件().length]=new String(char2子組件[j]);
							框[j+char1子組件.length][0]=70+char2框[j][0]*(180-70)/256;
							框[j+char1子組件.length][1]=112+char2框[j][1]*(220-112)/256;		
							框[j+char1子組件.length][2]=char2框[j][2]*(180-70)/256;
							框[j+char1子組件.length][3]=char2框[j][3]*(220-112)/256;						
						} //FOR末
				    	
					} //IF
				    if (char1.get子組件()[0].equals(""+'凵'+'0')) { //IF 凵
				    	
				    	//with result do begin Left :=55; top:=40; Right:=185; bottom:=170;  end; exit;
				    	for (int j = 0; j < char2.get子組件().length; j++) { //FOR始
							str[j+char1.get子組件().length]=new String(char2子組件[j]);
							框[j+char1子組件.length][0]=35+char2框[j][0]*(205-35)/256;
							框[j+char1子組件.length][1]=20+char2框[j][1]*(200-20)/256;		
							框[j+char1子組件.length][2]=char2框[j][2]*(205-35)/256;
							框[j+char1子組件.length][3]=char2框[j][3]*(200-20)/256;						
						} //FOR末
					} //IF
				    if (char1.get子組件()[0].equals(""+'\u56d7'+'0')) { //IF	囗		
					//with result do begin Left :=30; top:=20; Right:=230; bottom:=210;  end; exit;
				    	for (int j = 0; j < char2.get子組件().length; j++) { //FOR始
							str[j+char1.get子組件().length]=new String(char2子組件[j]);
							框[j+char1子組件.length][0]=20+char2框[j][0]*(240-20)/256;
							框[j+char1子組件.length][1]=15+char2框[j][1]*(200-15)/256;		
							框[j+char1子組件.length][2]=char2框[j][2]*(240-20)/256;
							框[j+char1子組件.length][3]=char2框[j][3]*(200-15)/256;						
						} //FOR末
					} //IF
				    if (char1.get子組件()[0].equals(""+''+'0')) { //IF	冂		
					//with result do begin Left :=30; top:=20; Right:=230; bottom:=210;  end; exit;
				    	for (int j = 0; j < char2.get子組件().length; j++) { //FOR始
							str[j+char1.get子組件().length]=new String(char2子組件[j]);
							框[j+char1子組件.length][0]=50+char2框[j][0]*(230-50)/256;
							框[j+char1子組件.length][1]=40+char2框[j][1]*(210-40)/256;		
							框[j+char1子組件.length][2]=char2框[j][2]*(230-50)/256;
							框[j+char1子組件.length][3]=char2框[j][3]*(210-40)/256;						
						} //FOR末
					} //IF
				    
				    
				    
				    if (char1.get子組件()[0].equals(""+'\u531a'+'0')) { //IF 匚
					//with result do begin Left :=50; top:=50; Right:=205; bottom:=200;  end; exit;
				    	for (int j = 0; j < char2.get子組件().length; j++) { //FOR始
							str[j+char1.get子組件().length]=new String(char2子組件[j]);
							框[j+char2子組件.length][0]=30+char2框[j][0]*(225-30)/256;
							框[j+char1子組件.length][1]=30+char2框[j][1]*(220-30)/256;		
							框[j+char1子組件.length][2]=char2框[j][2]*(225-30)/256;
							框[j+char1子組件.length][3]=char2框[j][3]*(220-30)/256;						
						} //FOR末
					} //IF
				    if (char1.get子組件()[0].equals(""+'\u7592'+'0')) { //IF	疒
					//with result do begin Left :=108; top:=89; Right:=222; bottom:=222;  end; exit;
				    	for (int j = 0; j < char2.get子組件().length; j++) { //FOR始
							str[j+char1.get子組件().length]=new String(char2子組件[j]);
							框[j+char1子組件.length][0]=68+char2框[j][0]*(255-68)/256;
							框[j+char1子組件.length][1]=49+char2框[j][1]*(255-49)/256;		
							框[j+char1子組件.length][2]=char2框[j][2]*(255-68)/256;
							框[j+char1子組件.length][3]=char2框[j][3]*(255-49)/256;						
						} //FOR末
					} //IF
				    if (char1.get子組件()[0].equals(""+'\u5E7F'+'0')) { //IF	广	
					//with result do begin Left :=83; top:=87; Right:=222; bottom:=222;  end; exit;
				    	for (int j = 0; j < char2.get子組件().length; j++) { //FOR始
							str[j+char1.get子組件().length]=new String(char2子組件[j]);
							框[j+char1子組件.length][0]=43+char2框[j][0]*(255-43)/256;
							框[j+char1子組件.length][1]=47+char2框[j][1]*(255-47)/256;		
							框[j+char1子組件.length][2]=char2框[j][2]*(255-43)/256;
							框[j+char1子組件.length][3]=char2框[j][3]*(255-47)/256;						
						} //FOR末
					} //IF
				    if (char1.get子組件()[0].equals(""+'\u8FB6'+'0')) { //IF 辶				    	
					//with result do begin Left :=70; top:=20; Right:=242; bottom:=230;  end; exit;
				    	//略改為框架為 Left :=80; top:=20; Right:=252; bottom:=230;
				    	for (int j = 0; j < char2.get子組件().length; j++) { //FOR始
							str[j+char1.get子組件().length]=new String(char2子組件[j]);
							框[j+char1子組件.length][0]=80+char2框[j][0]*(252-80)/256;
							框[j+char1子組件.length][1]=0+char2框[j][1]*(220-0)/256;		
							框[j+char1子組件.length][2]=char2框[j][2]*(252-80)/256;
							框[j+char1子組件.length][3]=char2框[j][3]*(220-0)/256;						
						} //FOR末
					} //IF				    
				    
				    
			    break;

				default:
			    break;
				} //SW末
			    stack.push(new CharComponent  (   new ComponentFrameGroup(str,框) ,字碼表   )  );
			    count++;
			}		    
			
			else		
			{ //ELSE
			    System.out.println("push"+IDSarray[i]);
			    //普通的字元			     
			    
			    stack.push((CharComponent)字碼表.get(""+IDSarray[i]+'0') );
			} //ELSE end
	    } //FOR末
	    //⿱艹⿰日月
	    

	    CharComponent 新字=(CharComponent)stack.pop(); 

	    //由於位置不對暫時去掉
	    //原作法行不通，僅是

	    for (int i = 0; i < 新字.get子組件框().length; i++) { //FOR
	    	新字.get子組件框()[i][0]+=5;
	    	新字.get子組件框()[i][1]+=5;
	    	新字.get子組件框()[i][2]=新字.get子組件框()[i][2] * 210/256;
	    	新字.get子組件框()[i][3]=新字.get子組件框()[i][3] * 210/256;
		} //FOR

	    return 新字;
	} //IF末
	else 	{ //ELSE
	    //平常的字
	    return (CharComponent)字碼表.get(IDS+0);//選字

	} //ELSE end
    }
    
    private int[] 判_縮小比例(CharComponent 甲,CharComponent 乙)  {
//    	以十個刻度來算
    	int 比例[]=new int[2]; 
    	int 筆劃差=甲.get筆畫數()-乙.get筆畫數();
    	if (Math.abs(筆劃差)<4) { //IF始 
    	    	//筆畫差在伯仲之間，平分
    	    比例[0]=5;
	} //IF末
    	else if (Math.abs(筆劃差)>=4 && Math.abs(筆劃差)<8) { //IF始 
    	    	//筆畫差大了一點，64分
	    if (筆劃差>0) { //IF始  甲>乙
			比例[0]=6;
	    } //IF末
	    else 比例[0]=4;
	} //IF末
    	else if (Math.abs(筆劃差)>=8 && Math.abs(筆劃差)<20) { //IF始
    	    	//筆畫差又更大，73分
    	if (筆劃差>0) { //IF始  甲>乙 
			比例[0]=7;
    	} //IF末
    	else 比例[0]=3;
	} //IF末
    	else	{ //ELSE 
    	    //超級合文，孔孟好學、招財進寶等用
    	if (筆劃差>0) { //IF始  甲>乙
			比例[0]=8;
    	} //IF末
    	else 比例[0]=2;
	} //ELSE end
    	
    	比例[1]=10-比例[0];  	
    	
    	return 比例;
    }

}