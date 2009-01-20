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

package demo;

import javax.swing.JComponent;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Iterator;
import java.util.Hashtable;
import java.awt.RenderingHints;
import ids.FontDB;
import ids.CharComponent;
import ids.Utils;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;


/**
 * @author mgdesigner
 * 
 */
public class FontDisplayer extends JComponent {
    private final int 文字區大小 = 512;// 256的兩倍
    public Hashtable 字碼表;
    private final int 九宮左上 = 30;
    private String tmp字="⿰女⿱萌灬";
    private Color 字色=Color.BLACK;
    private byte 目前字體=FontDB.圓體;
    public FontDB fdb = null;

    public FontDisplayer() {
	fdb = new FontDB();	
	字碼表=fdb.get字碼表();
    }

    public void paint(Graphics g) {
	Graphics2D g2=(Graphics2D)g;
	draw佈景(g);
	//fdb.drawComponent(g2,tmp字,512, 九宮左上, 九宮左上,Color.ORANGE,FontDB.圓體,true);//過年用
	fdb.drawComponent(g2,tmp字,512, 九宮左上, 九宮左上,字色,目前字體,true);		
    }

    public BufferedImage getFontImage(int size)    {
	
	BufferedImage img = new BufferedImage(512, 512,BufferedImage.TYPE_INT_ARGB);
	Graphics2D g2=(Graphics2D)img.getGraphics();
	fdb.drawComponent(g2,tmp字,512, 0, 0,字色,目前字體,true);
	g2.dispose();
	return img;

    }

 
    
    private void draw佈景(Graphics g) {

	g.setColor(Color.BLACK);
	g.fillRect(0, 0, this.getWidth(), this.getHeight());
	g.setColor(Color.white);
	g.drawString(" Canvas width=" + this.getWidth() + ",height="
		+ this.getHeight() + "文字區大小為" + 文字區大小 + "X" + 文字區大小, 20, 596);// debug大小
	draw九宮格(g);
	

    }
    
    private void draw九宮格(Graphics g)
    {
//	 畫九宮
	g.setColor(Color.white);
	g.fillRect(29, 29, 513, 513);
	g.setColor(Color.red);
	//g.drawString(String.valueOf(fdb.test), 20, 20);
	// 長寬540
	g.drawRect(九宮左上, 九宮左上, 512,512);// 長寬各為512，為256的兩倍
	// 二直線
	g.drawLine(210, 30, 210, 542);
	g.drawLine(390, 30, 390, 542);
	// 二橫線
	g.drawLine(30, 210, 542, 210);
	g.drawLine(30, 390, 542, 390);
    }
    
public void showKanji(String IDS) {
    tmp字=IDS;
    this.repaint();
    }

public void setFont(byte ftnum){
    目前字體=ftnum;
    this.repaint();    
}

public void setFontColor(Color color){
    字色=color;
    this.repaint();    
}
    




    

}