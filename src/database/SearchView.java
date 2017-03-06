package database;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import database.SiteView.EditListener;
import database.SiteView.printListener;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SearchView {
	public JFrame f = new JFrame("检索");
	private String[] str = {"月份","星期"};
	private String[] str2 = {"发生指数","伤亡数","灾害等级"};
	private JComboBox<String> date = new JComboBox<String>(str);
	private JComboBox<String> fires = new JComboBox<String>(str2);
	private JTextField low = new JTextField(7);
	private JTextField high = new JTextField(7);
	private JTextField keyword = new JTextField(15);
	private JLabel label = new JLabel("~");
	private JButton btn = new JButton("检索");
	private JButton report = new JButton("报表");

	private JPanel p = new JPanel();
	private JPanel p2 = new JPanel();
	private JPanel p3 = new JPanel();
	private JPanel p4 = new JPanel();
	private JScrollPane scroll = new JScrollPane();
	private JTable table = new JTable();
//    private DefaultTableModel _dtm  = new DefaultTableModel(new Object[20][8],head);
    private List<Forestfire> list = new ArrayList<Forestfire>();
    private static String[] head = {"序号","月份","星期","火灾面积","经济损失","伤亡人员","灾害等级","发生指数"};

	public void init() {
		p.add(date);
		p.add(keyword);
		p2.add(fires);
		p2.add(low);
		p2.add(label);
		p2.add(high);
		p2.add(btn);
		report.addActionListener(new printListener());

		btn.addActionListener(new SearchListener());
		p3.add(report);		

		p3.add(p,BorderLayout.WEST);
		p3.add(p2);
	    //宽度自适应
	    table.setAutoResizeMode(table.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
	    //设置table大小
	    table.setModel(new DefaultTableModel(new Object[20][8],head));
	    table.setPreferredScrollableViewportSize(new Dimension(600, 400));
		scroll.setViewportView(table);

	    f.add(p3,BorderLayout.NORTH);
	    f.add(scroll,BorderLayout.CENTER);	    		
	    
	    //窗口显示在正中央
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();  
	    f.setLocation((dim.width/2 - f.getWidth()) / 2, (dim.height/2 - f.getHeight()) / 4);  
	    f.pack();
	    f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	class SearchListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String key = keyword.getText();
			String sql = null;
			int index = date.getSelectedIndex();
			int index2 = fires.getSelectedIndex();

			if(!key.equals("")) {
				double l = 0;
				double h = 99999999;
				key = "'%"+key+"%'";
				String str = null;
				if(!low.getText().equals("")) {
					l = Double.parseDouble(low.getText());
				}
				if(!high.getText().equals("")) {
					h = Double.parseDouble(high.getText());
				}
				if(index2 == 0) {
					str = "f.possibility";
				} else if(index2 == 1) {
					str = "l.wd";
				} else if(index2 == 2) {
					str = "f.level";
				}
				if(index == 0) {
					sql = "select f.id,d.month,d.day,f.area,f.level,f.possibility,l.wd,l.money from date_factor d,forest_fires f,loss l where d.id=f.did and l.id=f.lid and d.month like "+key+" and "+str+">="+l+" and "+str+"<="+h+";"; 
				} else if(index == 1) {
					sql = "select f.id,d.month,d.day,f.area,f.level,f.possibility,l.wd,l.money from date_factor d,forest_fires f,loss l where d.id=f.did and l.id=f.lid and d.day like "+key+" and "+str+">="+l+" and "+str+"<="+h+";";
				}
			} else if(key.equals("") && !low.equals("") && !high.equals("")) {
				double l = Double.parseDouble(low.getText());
				double h = Double.parseDouble(high.getText());
				if(index2 == 0) {
					sql = "select f.id,d.month,d.day,f.area,f.level,f.possibility,l.wd,l.money from date_factor d,forest_fires f,loss l where d.id=f.did and l.id=f.lid and f.possibility >="+l+" and f.possibility<="+h+" order by f.possibility asc;";
				} else if(index2 == 1) {
					sql = "select f.id,d.month,d.day,f.area,f.level,f.possibility,l.wd,l.money from date_factor d,forest_fires f,loss l where d.id=f.did and l.id=f.lid and l.wd >="+l+" and l.wd<="+h+" order by l.wd asc;";					
				} else if(index2 == 2) {
					sql = "select f.id,d.month,d.day,f.area,f.level,f.possibility,l.wd,l.money from date_factor d,forest_fires f,loss l where d.id=f.did and l.id=f.lid and f.level >="+l+" and f.level<="+h+" order by f.level asc;";					
				}
			}  else if(key.equals("") && low.equals("") && !high.equals("")) {
				double h = Double.parseDouble(high.getText());
				if(index2 == 0) {
					sql = "select f.id,d.month,d.day,f.area,f.level,f.possibility,l.wd,l.money from date_factor d,forest_fires f,loss l where d.id=f.did and l.id=f.lid and f.possibility<="+h+" order by f.possibility asc;";
				} else if(index2 == 1) {
					sql = "select f.id,d.month,d.day,f.area,f.level,f.possibility,l.wd,l.money from date_factor d,forest_fires f,loss l where d.id=f.did and l.id=f.lid and l.wd<="+h+" order by l.wd asc;";					
				} else if(index2 == 2) {
					sql = "select f.id,d.month,d.day,f.area,f.level,f.possibility,l.wd,l.money from date_factor d,forest_fires f,loss l where d.id=f.did and l.id=f.lid and f.level<="+h+" order by f.level asc;";					
				}
			}  else if(key.equals("") && !low.equals("") && high.equals("")) {
				double l = Double.parseDouble(low.getText());
				if(index2 == 0) {
					sql = "select f.id,d.month,d.day,f.area,f.level,f.possibility,l.wd,l.money from date_factor d,forest_fires f,loss l where d.id=f.did and l.id=f.lid and f.possibility >="+l+" order by f.possibility asc;";
				} else if(index2 == 1) {
					sql = "select f.id,d.month,d.day,f.area,f.level,f.possibility,l.wd,l.money from date_factor d,forest_fires f,loss l where d.id=f.did and l.id=f.lid and l.wd >="+l+" order by l.wd asc;";					
				} else if(index2 == 2) {
					sql = "select f.id,d.month,d.day,f.area,f.level,f.possibility,l.wd,l.money from date_factor d,forest_fires f,loss l where d.id=f.did and l.id=f.lid and f.level >="+l+" order by f.level asc;";		
				} 
			} 
//			System.out.println(sql);
			try {
				Connection conn = Connect.getConn();
				Statement st = conn.createStatement();
				ResultSet res = st.executeQuery(sql);
				list.clear();
				DefaultTableModel dtm = new DefaultTableModel(null,head);
				table.setModel(dtm);
				while(res.next()) {
					Forestfire ff;
					ff = new Forestfire(res.getInt(1),res.getString(2), res.getString(3), res.getDouble(4), res.getDouble(5),res.getDouble(6),res.getInt(7),res.getDouble(8));
					list.add(ff);
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
					dtm = new DefaultTableModel(objects,head);
					dtm.addTableModelListener(new EditListener(list));
					table.setModel(dtm);
				}		
				
			} catch (Exception e1) {
//				e1.printStackTrace();
				System.out.println(e1.getMessage());
			}
			
		}
	
	}
	
	//修改表格的同事修改数据库中的数据
		class EditListener implements TableModelListener {
			List<Forestfire> list = new ArrayList<Forestfire>();
			
			EditListener(List<Forestfire> l) {
				this.list = l;
			}

			public void tableChanged(TableModelEvent arg0) {
				String sql = null;
				Object value = table.getValueAt(arg0.getFirstRow(), arg0.getColumn());
				//forest_fires的id
				int id = list.get(arg0.getFirstRow()).id;
				try {
					Connection conn = Connect.getConn();
					Statement st = conn.createStatement();
					String search = "select id,cid,did,fid,sid,lid from forest_fires where id="+id;
					ResultSet res = st.executeQuery(search);
					res.next();
					switch(arg0.getColumn()) {
						case 0 : break;
						case 1 : sql = "update date_factor set month='"+value+"' where id="+res.getInt("did")+";";break;
						case 2 : sql = "update date_factor set day='"+value+"' where id="+res.getInt("did")+";";break;
						case 3 : sql = "update forest_fires set area="+value+" where id="+res.getInt("id")+";";break;
						case 4 : sql = "update loss set money="+value+" where id="+res.getInt("lid")+";";break;
						case 5 : sql = "update loss set wd="+value+" where id="+res.getInt("lid")+";";break;
						case 7 : sql = "update forest_fires set possibility="+value+" where id="+res.getInt("id")+";";break;
					}				
					int rs = st.executeUpdate(sql);
				} catch (SQLException e) {
					if(arg0.getColumn() == 0) JOptionPane.showMessageDialog(f.getContentPane(), "不能修改id！");
					else if(arg0.getColumn() == 6) JOptionPane.showMessageDialog(f.getContentPane(), "不能修改灾害等级！");
					else if(arg0.getColumn() == 7) JOptionPane.showMessageDialog(f.getContentPane(), "不能修改发生指数！");
					else JOptionPane.showMessageDialog(f.getContentPane(), "非法操作！");
				}
			}
			
		}
		
	    class printListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				new PrintView(new JFrame("打印"),"打印报表",true,table,"表头","表尾");
			}
		}
}
