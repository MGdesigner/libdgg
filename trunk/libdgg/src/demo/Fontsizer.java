package demo;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JDialog;

public class Fontsizer extends JDialog implements ActionListener {
	JButton btn=null;
	Demo owner;
	boolean sizesetted=false;
	JTextField tf=null;	
	public Fontsizer(Demo owner)
	{
		super(owner);
		this.owner=owner;
		btn=new JButton("設定");
		tf=new JTextField();
		this.setLayout(new BorderLayout());
		this.add(tf,BorderLayout.CENTER);
		this.add(btn,BorderLayout.SOUTH);
		tf.setSize(200, 20);
		
		btn.setSize(20, 100);
		btn.setActionCommand("btn");
		btn.addActionListener(this);
		this.setSize(200, 100);
				
		
		
	}
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

if (e.getActionCommand().equals("btn")) {//i
	
	if (tf.getText()!=null) {//i
		owner.opglyphsize=Integer.parseInt( tf.getText());	
	}//i
	else owner.opglyphsize=512;
	
	this.setVisible(false);
	
		
}//i
	}
	public void setsize()	{
		tf.setText(""+owner.opglyphsize);
		this.setVisible(true);		
	}
	
	

}
