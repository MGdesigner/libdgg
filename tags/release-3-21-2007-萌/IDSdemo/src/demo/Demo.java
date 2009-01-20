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
import java.awt.event.KeyEvent;
import javax.swing.text.BadLocationException;
import java.awt.event.KeyListener;
import java.awt.Event;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.KeyStroke;
import javax.swing.JPanel;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.JTextArea;

/**
 * @author mgdesigner
 *12/13把initialize、main建構子放到最後面
 *12/27完成從輸入區輸入的功能（目前只能打一字）
 *2007/1/13昨天把UI大躍進了，今天把拆字的方法細緻化
 */
public class Demo extends JFrame implements ActionListener,MouseListener,KeyListener{
    private final String version="萌";
    private final String 授權書=
	"\n* * 本軟體係應用中華民國發明專利號碼第I254863號「可攜式造字引擎」專利"
	+"\n* 本程式引用專利license:可攜式造字引擎專利公眾授權條款KSAFAEA，"
	+"\n* 該條款可在anouncement.zhongwen.tw取得"
	+"\n*\n* "
	 +"* Copyright (c) 2007 著作權由張正一所有。著作權人保留一切權利。"
    +"\n*" 
    +"\n* 這份授權條款，在使用者符合以下三條件的情形下，授予使用者使用及再散播本"
    +"\n* 軟體包裝原始碼及二進位可執行形式的權利，無論此包裝是否經改作皆然："
    +"\n* "
    +"\n* * 對於本軟體原始碼的再散播，必須保留上述的版權宣告、此三條件表列，以"
    +"\n*   及下述的免責聲明。"
    +"\n* * 對於本套件二進位可執行形式的再散播，必須連帶以文件以及／或者其他附"
    +"\n*   於散播包裝中的媒介方式，重製上述之版權宣告、此三條件表列，以及下述"
    +"\n*   的免責聲明。"
    +"\n* * 未獲事前取得書面許可，不得使用張正一或本軟體貢獻者之名稱，"
    +"\n*   來為本軟體之衍生物做任何表示支持、認可或推廣、促銷之行為。"
    +"\n* 免責聲明：本軟體是由張正一及本軟體之貢獻者以現狀（\"as is\"）提供，"
    +"\n* 本軟體包裝不負任何明示或默示之擔保責任，包括但不限於就適售性以及特定目"
    +"\n* 的的適用性為默示性擔保。張正一及本軟體之貢獻者，無論任何條件、"
    +"\n* 無論成因或任何責任主義、無論此責任為因合約關係、無過失責任主義或因非違"
    +"\n* 約之侵權（包括過失或其他原因等）而起，對於任何因使用本軟體包裝所產生的"
    +"\n* 任何直接性、間接性、偶發性、特殊性、懲罰性或任何結果的損害（包括但不限"
    +"\n* 於替代商品或勞務之購用、使用損失、資料損失、利益損失、業務中斷等等），"
    +"\n* 不負任何責任，即在該種使用已獲事前告知可能會造成此類損害的情形下亦然。";
    private JPanel jContentPane = null;

    private JMenuBar jJMenuBar = null;

    private JMenu fileMenu = null;

    private JMenu editMenu = null;

    private JMenu helpMenu = null;

    private JMenuItem exitMenuItem = null;

    private JMenuItem aboutMenuItem = null;

    private JMenuItem cutMenuItem = null;

    private JMenuItem copyMenuItem = null;

    private JMenuItem pasteMenuItem = null;

    private JMenuItem saveMenuItem = null;

    private JTextField jTextField = null;
    private FontDisplayer fd=null;

	private JToolBar 組字鈕列 = null;  //  @jve:decl-index=0:visual-constraint="737,282"

	private JButton 左右組btn = null;

	private JButton 上下組btn = null;

	private JButton 包含組 = null;

	private JButton 拆字btn = null;
	private JDialog 關於Diag;
	private StringBuffer 組字鈕列buf=null;//組字鈕列幕後處理用

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {  
    	System.out.println(e.getActionCommand());
    	jTextField.setCaretColor(Color.CYAN);
    	
    	int 游標位置=jTextField.getCaretPosition();
    	System.out.println("游標位置="+游標位置);
		組字鈕列buf.append(jTextField.getText());
		
    	if (e.getActionCommand().equals("左右組")) { //IF
    		char 游標所在的字=組字鈕列buf.charAt(游標位置);
    		jTextField.setText(組字鈕列buf.replace(游標位置,游標位置,""+'\u2ff0').toString());
    		
		} //IF
    	if (e.getActionCommand().equals("上下組")) { //IF
    		char 游標所在的字=組字鈕列buf.charAt(游標位置);
    		jTextField.setText(組字鈕列buf.replace(游標位置,游標位置,""+'\u2ff1').toString());
    		
    		//jTextField.setText('\u2ff1'+jTextField.getText());
		} //IF
    	if (e.getActionCommand().equals("包含組")) { //IF
    		char 游標所在的字=組字鈕列buf.charAt(游標位置);
    		jTextField.setText(組字鈕列buf.replace(游標位置,游標位置,""+'\u2ff4').toString());
    		//jTextField.setText('\u2ff4'+jTextField.getText());
		} //IF
    	if (e.getActionCommand().equals("拆字")) { //IF
    		char 要拆的字=組字鈕列buf.charAt(游標位置-1);//目前游標前一字，這樣比較符合中文的直覺    		    		
    		jTextField.setText(
    				組字鈕列buf.replace(游標位置-1,游標位置,fd.fdb.查部件組成(要拆的字)).toString()//目前游標前一字，這樣比較符合中文的直覺		
    		);
    		
		} //IF
    	組字鈕列buf.delete(0,組字鈕列buf.capacity());//清空
    	jTextField.setCaretColor(Color.BLACK);
 	


    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent arg0) {
	// TODO 自動產生方法 Stub

    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)j
     */
    public void mouseEntered(MouseEvent arg0) {
	// TODO 自動產生方法 Stub

    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent arg0) {
	// TODO 自動產生方法 Stub

    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent arg0) {
	// TODO 自動產生方法 Stub

    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent arg0) {
	// TODO 自動產生方法 Stub

    }

    /**
     * This method initializes jTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextField() {
        if (jTextField == null) {
    	jTextField = new JTextField();
        }
        return jTextField;
    }

    
    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
	if (jContentPane == null) {
	    jContentPane = new JPanel();
	    jContentPane.setLayout(new GridBagLayout());
	    GridBagConstraints Constraint1=new GridBagConstraints();
	    Constraint1.weighty=1;
	    Constraint1.weightx=512;
	    Constraint1.gridx = 0;
	    Constraint1.gridy = 0;
	    Constraint1.fill=Constraint1.HORIZONTAL;
	    jContentPane.add(getJTextField(), Constraint1);
	    GridBagConstraints Constraint2=new GridBagConstraints();
	    Constraint2.weighty=10;
	    Constraint2.weightx=512;
	    Constraint2.gridx = 0;
	    Constraint2.gridy = 1;
	    Constraint2.fill=Constraint2.BOTH;
	    jContentPane.add(get組字鈕列(), Constraint2);
	    GridBagConstraints Constraint3=new GridBagConstraints();
	    Constraint3.weighty=512;
	    Constraint3.weightx=512;
	    Constraint3.gridx = 0;
	    Constraint3.gridy = 2;
	    Constraint3.fill=Constraint3.BOTH;
	    jContentPane.add(fd, Constraint3);
	}
	return jContentPane;
    }

    /**
     * This method initializes jJMenuBar	
     * 	
     * @return javax.swing.JMenuBar	
     */
    private JMenuBar getJJMenuBar() {
	if (jJMenuBar == null) {
	    jJMenuBar = new JMenuBar();
	    jJMenuBar.add(getFileMenu());
	    //jJMenuBar.add(getEditMenu());
	    jJMenuBar.add(getHelpMenu());
	}
	return jJMenuBar;
    }

    /**
     * This method initializes jMenu	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenu getFileMenu() {
	if (fileMenu == null) {
	    fileMenu = new JMenu();
	    fileMenu.setText("檔");
	    //fileMenu.add(getSaveMenuItem());
	    fileMenu.add(getExitMenuItem());
	}
	return fileMenu;
    }

    /**
     * This method initializes jMenu	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenu getEditMenu() {
	if (editMenu == null) {
	    editMenu = new JMenu();
	    editMenu.setText("編");
	    editMenu.add(getCutMenuItem());
	    editMenu.add(getCopyMenuItem());
	    editMenu.add(getPasteMenuItem());
	}
	return editMenu;
    }

    /**
     * This method initializes jMenu	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenu getHelpMenu() {
	if (helpMenu == null) {
	    helpMenu = new JMenu();
	    helpMenu.setText("助");
	    helpMenu.add(getAboutMenuItem());
	}
	return helpMenu;
    }

    /**
     * This method initializes jMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getExitMenuItem() {
	if (exitMenuItem == null) {
	    exitMenuItem = new JMenuItem();
	    exitMenuItem.setText("離");
	    exitMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    System.exit(0);
		}
	    });
	}
	return exitMenuItem;
    }

    /**
     * This method initializes jMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getAboutMenuItem() {
	if (aboutMenuItem == null) {
	    aboutMenuItem = new JMenuItem();
	    aboutMenuItem.setText("關於");
	    aboutMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    關於Diag=new JDialog(Demo.this, "關於", true);
		    關於Diag.setSize(600, 600);
		 JTextArea 版權聲名=new JTextArea(授權書);
		 版權聲名.setEditable(false);
		    關於Diag.add(版權聲名);
		    關於Diag.setVisible(true);
		    
		}
	    });
	}
	return aboutMenuItem;
    }

    /**
     * This method initializes jMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getCutMenuItem() {
	if (cutMenuItem == null) {
	    cutMenuItem = new JMenuItem();
	    cutMenuItem.setText("剪");
	    cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
		    Event.CTRL_MASK, true));
	}
	return cutMenuItem;
    }

    /**
     * This method initializes jMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getCopyMenuItem() {
	if (copyMenuItem == null) {
	    copyMenuItem = new JMenuItem();
	    copyMenuItem.setText("複製");
	    copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
		    Event.CTRL_MASK, true));
	}
	return copyMenuItem;
    }

    /**
     * This method initializes jMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getPasteMenuItem() {
	if (pasteMenuItem == null) {
	    pasteMenuItem = new JMenuItem();
	    pasteMenuItem.setText("貼");
	    pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
		    Event.CTRL_MASK, true));
	}
	return pasteMenuItem;
    }

    /**
     * This method initializes jMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getSaveMenuItem() {
	if (saveMenuItem == null) {
	    saveMenuItem = new JMenuItem();
	    saveMenuItem.setText("存");
	    saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		    Event.CTRL_MASK, true));
	}
	return saveMenuItem;
    }
    
    /* (non-Javadoc)
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent e) {
	// TODO 自動產生方法 Stub
	if (e.getKeyCode()==KeyEvent.VK_ENTER) { //IF始
	    System.out.println("enter");
	    fd.showKanji(jTextField.getText());
	    
	} //IF末
	
    }

    /* (non-Javadoc)
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    public void keyReleased(KeyEvent e) {
	// TODO 自動產生方法 Stub
	
    }

    /* (non-Javadoc)
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(KeyEvent e) {
	// TODO 自動產生方法 Stub
	
    }

    
    /**
	 * This method initializes 組字鈕列	
	 * 	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar get組字鈕列() {
		if (組字鈕列 == null) {
			組字鈕列 = new JToolBar();
			組字鈕列.add(get左右組btn());
			組字鈕列.add(get上下組btn());
			組字鈕列.add(get包含組btn());
			組字鈕列.add(get拆字btn());
		}
		組字鈕列.setFloatable(false);
		get左右組btn().setActionCommand("左右組");
		get上下組btn().setActionCommand("上下組");
		get包含組btn().setActionCommand("包含組");
		get拆字btn().setActionCommand("拆字");
				
		get左右組btn().addActionListener(this);
		get上下組btn().addActionListener(this);
		get包含組btn().addActionListener(this);
		get拆字btn().addActionListener(this);
		return 組字鈕列;
	}

	/**
	 * This method initializes 左右組btn	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton get左右組btn() {
		if (左右組btn == null) {
			左右組btn = new JButton("左右組");
		}
		return 左右組btn;
	}

	/**
	 * This method initializes 上下組btn	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton get上下組btn() {
		if (上下組btn == null) {
			上下組btn = new JButton("上下組");
		}
		return 上下組btn;
	}

	/**
	 * This method initializes 包含組	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton get包含組btn() {
		if (包含組 == null) {
			包含組 = new JButton("包含組");
		}
		return 包含組;
	}

	/**
	 * This method initializes 拆字btn	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton get拆字btn() {
		if (拆字btn == null) {
			拆字btn = new JButton("拆字");
		}
		return 拆字btn;
	}

	/**
     * @param args
     */
   public static void main(String[] args) {
		Demo application = new Demo();
		application.setVisible(true);
    }

    /**
     * This is the default constructor
     */
    public Demo() {    
    initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
		fd = new FontDisplayer(); //建新FontDisplayer類之物
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setJMenuBar(getJJMenuBar());
    this.setSize(622, 711);
    this.setContentPane(getJContentPane());
    this.setTitle("漢字組字示範程式   "+version+"版");
    //this.setResizable(false);
    jTextField.addKeyListener(this);    
    	組字鈕列buf=new StringBuffer();    
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"
