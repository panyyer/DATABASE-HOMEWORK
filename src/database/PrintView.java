package database;


import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import database.SiteView.EditListener;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrintView extends JDialog implements ActionListener {
	List<Forestfire> list = new ArrayList<Forestfire>(); 
	JTable jtable;
	JButton jb1, jb2,jb3;
	JPanel jp,jp2;
	boolean flag = false;
	JLabel label = new JLabel("温馨提示：当前窗口看不到页码，打印之后会自动添加页码等排版！");

	public PrintView(Frame owner, String title, boolean model, JTable jtable,String header,String footer) {
		super(owner, title, model);
		this.jtable = jtable;
		this.setLayout(new BorderLayout());
		
		JScrollPane scrollPane = new JScrollPane(jtable);
		this.add(scrollPane, BorderLayout.NORTH);
		jp = new JPanel();
		jp2 = new JPanel();
		
		jb1 = new JButton("打印当前数据");
		jb2 = new JButton("打印全部");
		jb3 = new JButton("取消");
		
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		jb3.addActionListener(this);
		
		jp.add(jb1);
		jp.add(jb2);
		jp.add(jb3);
		jp2.add(label,BorderLayout.CENTER);
		
		this.add(jp, BorderLayout.SOUTH);
		this.add(jp2, BorderLayout.CENTER);

		this.setTitle("打印预览");
		this.setSize(640, 550);
	
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation(width / 2 - this.getWidth() / 2,
		height / 2 - this.getHeight() / 2);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		//响应确定打印按钮
		if (e.getSource() == jb1) {
			try {
				MessageFormat header = new MessageFormat("- {0} -"); //页脚加页码
				MessageFormat footer = new MessageFormat("打印时间: " + new Date()); //页眉加时间
				MessageFormat headerFormat = header;
				MessageFormat footerFormat = footer;
				// 按比例将输出缩小的打印模式
				jtable.print(JTable.PrintMode.FIT_WIDTH, headerFormat,footerFormat);
				flag = true;
			} catch (Exception pe) {
				flag = false;
				System.err.println("Error printing: " + pe.getMessage());
			}
			this.dispose();
		}
		if (e.getSource() == jb3) {
			flag = false;
			this.dispose();
		}
		if (e.getSource() == jb2) {
		
			try {
				Connection conn = new Connect().getConn();	
				Statement st = conn.createStatement();
				String sql = "select f.id,d.month,d.day,f.area,f.level,f.possibility,l.wd,l.money from date_factor d,forest_fires f,loss l where d.id=f.did and l.id=f.lid";
				ResultSet res = st.executeQuery(sql);
				while(res.next()) {
					Forestfire f;
					f = new Forestfire(res.getInt(1),res.getString(2), res.getString(3), res.getDouble(4), res.getDouble(5),res.getDouble(6),res.getInt(7),res.getDouble(8));
					this.list.add(f);
				}
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			 Object[][] objects = new Object[(list.size())][];  
			 for (int i = 0; i < objects.length; i++) {  
			     objects[i] = new Object[8];   
			     objects[i][0] = list.get(i).id;  
			     objects[i][1] = list.get(i).month;  
			     objects[i][2] = list.get(i).day;  
			     objects[i][3] = list.get(i).area;  
			     objects[i][4] = list.get(i).money;  
			     objects[i][5] = list.get(i).wd;  
			     objects[i][6] = list.get(i).level;  
			     objects[i][7] = list.get(i).possibility;  
			 }  
			 DefaultTableModel dtm = new DefaultTableModel(objects,SiteView.head);
			 jtable.setModel(dtm);  
				try {
					MessageFormat header = new MessageFormat("- {0} -"); //页脚加页码
					MessageFormat footer = new MessageFormat("打印时间: " + new Date()); //页眉加时间
					MessageFormat headerFormat = header;
					MessageFormat footerFormat = footer;
					// 按比例将输出缩小的打印模式
					jtable.print(JTable.PrintMode.FIT_WIDTH, headerFormat,footerFormat);
					flag = true;
				} catch (Exception pe) {
					flag = false;
					System.err.println("Error printing: " + pe.getMessage());
				}
				this.dispose();
		}
	}
	public boolean getStatus() {
		return flag;
	}

}