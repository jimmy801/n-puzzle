import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.lang.*;

public class Form2 extends JFrame{
	puzzle ref;
	JButton check = new JButton("OK!");
	JPanel p0 = new JPanel();
	JPanel p1 = new JPanel();
	JPanel p2 = new JPanel();
	JLabel d_h = new JLabel();
	JLabel d_w = new JLabel();
	JLabel h_name = new JLabel("高:");
	JLabel w_name = new JLabel("寬:");
	JTextField Height = new JTextField(5);
	JTextField Width = new JTextField(5);
	JDialog D = new JDialog(this, "輸入初始格數", true);

	private void controlBtn(String s1,String s2,String s3,String s4){
		if(s1 != "" || s2 != "" || s3.length() < 1 || Integer.parseInt(s3) < 2
		|| s4.length() < 1 || Integer.parseInt(s4) < 2)check.setEnabled(false);
		else check.setEnabled(true);
	}
	
	private boolean isInt(String str){
		try { 
		Integer.parseInt(str);  
		return true;  
		}
		catch (NumberFormatException e) { 
		return false;  
		}  	
	}
	
	private void changeType(String str, JLabel lab){
		if(str.length() < 1)lab.setText("");
		else{
			if(!isInt(str))lab.setText("輸入數字不合法!");
			else if(Integer.parseInt(str) < 3)lab.setText("輸入數字必須大於2!");
			else lab.setText("");
		}
		D.pack();
	}
	
	public Form2(puzzle in){
		ref = in;
		
		D.setLayout(new FlowLayout(FlowLayout.LEFT));

		check.setEnabled(false);
		
		if(Height.getText() != "")Height.setText("");
		if(Width.getText() != "")Width.setText("");
		
		Height.getDocument().addDocumentListener(new DocumentListener(){  
		public void insertUpdate(DocumentEvent e) {
			changeType(Height.getText(),d_h);
			controlBtn(d_h.getText(),d_w.getText(),Height.getText(),Width.getText());
		}
		public void changedUpdate(DocumentEvent e) {
			changeType(Height.getText(),d_h);
			controlBtn(d_h.getText(),d_w.getText(),Height.getText(),Width.getText());
		}
		public void removeUpdate(DocumentEvent documentEvent) {
			changeType(Height.getText(),d_h);
			controlBtn(d_h.getText(),d_w.getText(),Height.getText(),Width.getText());
		}
		});
		
		Width.getDocument().addDocumentListener(new DocumentListener(){  
		public void insertUpdate(DocumentEvent e) {
			changeType(Width.getText(),d_w);
			controlBtn(d_h.getText(),d_w.getText(),Height.getText(),Width.getText());
		}
		public void changedUpdate(DocumentEvent e) {
			changeType(Width.getText(),d_w);
			controlBtn(d_h.getText(),d_w.getText(),Height.getText(),Width.getText());
		}
		public void removeUpdate(DocumentEvent documentEvent) {
			changeType(Width.getText(),d_w);
			controlBtn(d_h.getText(),d_w.getText(),Height.getText(),Width.getText());	
		}
		});
		
		Height.addKeyListener(new KeyMoniter());
		Width.addKeyListener(new KeyMoniter());
		
		check.addKeyListener(new KeyMoniter());
		check.addActionListener(new ActionListener() { // 按下btn觸發
				public void actionPerformed(ActionEvent e) {
				ref.Height = Integer.parseInt(Height.getText());
				ref.Width = Integer.parseInt(Width.getText());
				D.dispose();
			}
		});
		
		p0.setLayout(new GridLayout(2,1));
		p1.setLayout(new GridLayout(2,2));
		p0.add(h_name);
		p0.add(w_name);
		p1.add(Height);
		p1.add(d_h);
		p1.add(Width);
		p1.add(d_w);
		Height.enableInputMethods(false);
		Width.enableInputMethods(false);
		p2.setLayout(new FlowLayout(FlowLayout.CENTER));
		p2.add(check);
		D.add(p0);
		D.add(p1);
		D.add(p2);	
		D.pack();
		D.setResizable(false);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent event) {
				Form2.this.dispose();
			}});
		D.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
		D.setVisible(true);	
	}
	
	public class KeyMoniter extends KeyAdapter{
		public void keyPressed(KeyEvent e){
			int keyCode = e.getKeyCode();
			switch(keyCode){
				case KeyEvent.VK_ENTER :
				if(check.isEnabled())check.doClick();
				break;     
			}
		}
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
	}
}