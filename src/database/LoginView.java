package database;

//import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class LoginView {
	private JFrame f = new JFrame("森林降雨数据管理系统");
	private JButton open = new JButton("登录"); 
	private JButton close = new JButton("退出"); 
	private JLabel uLabel = new JLabel("账号：");
	private JLabel pLabel = new JLabel("密码：");
	private JPasswordField pass = new JPasswordField();
	private JTextField username = new JTextField();
	
	public void init() {
		JPanel p = new JPanel();
		p.setLayout(null); //空布局
		
		p.add(pLabel);
		p.add(uLabel);
		p.add(username);
		p.add(pass);
		p.add(open);
		p.add(close);
		
		//设置组件位置
		uLabel.setBounds(160,100,60,30);
		username.setBounds(200,100,250,30);
		pLabel.setBounds(160,150,60,30);
		pass.setBounds(200,150,250,30);
		open.setBounds(240,200,60,30);
		close.setBounds(340,200,60,30);
		
		close.addActionListener(new CloseListener());
		open.addActionListener(new OpenListener());
		p.setBorder(new TitledBorder(new EtchedBorder(),"Login",TitledBorder.LEFT,TitledBorder.TOP));

		f.add(p);
		f.pack();
		f.setBounds(200, 200, 650, 400);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	
    class CloseListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			System.exit(0);  //退出程序
		}
	}
    
    class OpenListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try {
				Login log = new Login(username.getText(),pass.getPassword());
				if(log.userLogin()) {
					f.dispose();
					new SiteView().init();
				} else {
					JOptionPane.showMessageDialog(f.getContentPane(), "账号密码有误");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
		}
	}
}


