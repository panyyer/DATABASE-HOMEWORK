package database;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

//import com.mysql.sql.*;

public class SiteView {
//	private JTable table = new JTable(20,5);
	private JTable table;
	private JScrollPane scrollPane = new JScrollPane();
    private DefaultTableModel tmd;
    public static String[] head = {"���","�·�","����","�������","������ʧ","������Ա","�ֺ��ȼ�","����ָ��"};
	private JFrame f = new JFrame("������");
	private JPanel p = new JPanel();
	private JPanel p2 = new JPanel();
	private JPanel p3 = new JPanel();
	private JButton add = new JButton("����");
	private JButton delete = new JButton("ɾ��");
	private JButton report = new JButton("����");
	private JButton search = new JButton("��ѯ");
	private JButton flash = new JButton("ˢ��");
	private JButton detail = new JButton("��ϸ��Ϣ");

	private JButton frist = new JButton("��ҳ");
	private JButton last = new JButton("βҳ");
	private JButton previous = new JButton("��һҳ");
	private JButton next = new JButton("��һҳ");

	private JMenuBar mb = new JMenuBar();
	private JMenu menu = new JMenu("����");
	private JMenuItem Nimbus = new JMenuItem("Nimbus ���");
	private JMenuItem Windows = new JMenuItem("Windows ���");
	private JMenuItem WindowsClass = new JMenuItem("Windows ������");
	private JMenuItem Metal = new JMenuItem("Metal ���");
	private JMenuItem Motif = new JMenuItem("Motif ��� ");
	
	private List<Forestfire> bigList = new ArrayList<Forestfire>(); //�󼯺ϣ�������ȡ 
	private List<Forestfire> smallList = new ArrayList<Forestfire>(); //С���ϣ����ظ�����������
	
	public void init() {
		delete.addActionListener(new DeleteListener());
		
		p.setLayout(new FlowLayout(FlowLayout.CENTER,15,10));
		p.add(add);
		p.add(search);
		p.add(delete);
		p.add(report);		
		p.add(flash);		
//		p.add(detail);		

		p2.setLayout(new FlowLayout(FlowLayout.RIGHT,10,5));
		add.addActionListener(new AddListener());
		search.addActionListener(new SearchListener());
		delete.addActionListener(new DeleteListener());
//		detail.addActionListener(new detailListener());
		report.addActionListener(new printListener());
		flash.addActionListener(new flashListener());

		previous.addActionListener(new previousListener());
		frist.addActionListener(new firstListener());
		last.addActionListener(new lastListener());
		next.addActionListener(new nextListener());
		p2.add(frist);
		p2.add(previous);
		p2.add(next);
		p2.add(last);

		menu.add(Metal);
		menu.add(Nimbus);
		menu.add(Windows);
		menu.add(WindowsClass);
		menu.add(Motif);

		Metal.addActionListener(new FlavorListener());
		Nimbus.addActionListener(new FlavorListener());
		Windows.addActionListener(new FlavorListener());
		WindowsClass.addActionListener(new FlavorListener());
		Motif.addActionListener(new FlavorListener());
		mb.add(menu);
		
	    table = new JTable();
		List<Forestfire> newlist = new PageController().previousPage();  
		viewAll(newlist);
	    //�������Ӧ
	    table.setAutoResizeMode(table.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
	    //����table��С
	    table.setPreferredScrollableViewportSize(new Dimension(600, 400));
	    scrollPane.setViewportView(table);
	    f.setJMenuBar(mb);
	    f.add(p,BorderLayout.NORTH);
	    f.add(p2,BorderLayout.SOUTH);
	    f.add(scrollPane);	    		
	    //������ʾ��������
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();  
	    f.setLocation((dim.width/2 - f.getWidth()) / 2, (dim.height/2 - f.getHeight()) / 4);  
	    f.pack();
	    f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
    class previousListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			 List<Forestfire> newlist = new PageController().previousPage();  
			 viewAll(newlist);
		}
	}
  
    class lastListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			 List<Forestfire> newlist = new PageController().lastPage();  
			 viewAll(newlist);
		}
	}
    
    class AddListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			 new AddView().init();
		}
	}
    
    class SearchListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			 new SearchView().init();
		}
	}
    class firstListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			 List<Forestfire> newlist = new PageController(1).setCurentPageIndex();  
			 viewAll(newlist);
		}
	}
    
    class nextListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			 List<Forestfire> newlist = new PageController().nextPage();  
			 viewAll(newlist);
		}
	}
	
    class printListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			new PrintView(new JFrame("��ӡ"),"��ӡ����",true,table,"��ͷ","��β");
		}
	}
    
    class flashListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int nowPage = new PageController().nowPage();
			List<Forestfire> newlist = new PageController(nowPage).setCurentPageIndex();  
			viewAll(newlist);
		    scrollPane.setViewportView(table);
		}
	}
    
    class detailListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			//��ȡid
			Object id = table.getValueAt(table.getSelectedRow(), 0);	    
			new DetailView(id).init();
		}
	}
    
    class FlavorListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try {
				switch(e.getActionCommand()) {
				case "Metal ���" : 
					UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
					break;
				case "Nimbus ���" : 
					UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
					break;
				case "Windows ���" : 
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					break;
				case "Windows ������" : 
					System.out.println("aaa");
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
					break;
				default : 
					System.out.println("aaa");
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
					break;
				}

				SwingUtilities.updateComponentTreeUI(f.getContentPane());
				SwingUtilities.updateComponentTreeUI(mb);
			} catch (Exception ee) {
				
			}
		}
	}
    
	 //��ʾList�е��û�  
	public void viewAll(List<Forestfire> list) {  
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
	 DefaultTableModel dtm = new DefaultTableModel(objects,head);
	 dtm.addTableModelListener(new EditListener(list));
	 table.setModel(dtm);  
	}  
	
	//�޸ı���ͬ���޸����ݿ��е�����
	class EditListener implements TableModelListener {
		List<Forestfire> list = new ArrayList<Forestfire>();
		
		EditListener(List<Forestfire> l) {
			this.list = l;
		}

		public void tableChanged(TableModelEvent arg0) {
			String sql = null;
			Object value = table.getValueAt(arg0.getFirstRow(), arg0.getColumn());
			//forest_fires��id
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
				JOptionPane.showMessageDialog(f.getContentPane(), "�޸ĳɹ���");
			} catch (SQLException e) {
				if(arg0.getColumn() == 0) JOptionPane.showMessageDialog(f.getContentPane(), "�����޸�id��");
				else if(arg0.getColumn() == 6) JOptionPane.showMessageDialog(f.getContentPane(), "�����޸��ֺ��ȼ���");
				else if(arg0.getColumn() == 7) JOptionPane.showMessageDialog(f.getContentPane(), "�����޸ķ���ָ����");
				else JOptionPane.showMessageDialog(f.getContentPane(), "�Ƿ�������");
			}
		}
		
	}
	
	class DeleteListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			//��ȡid
			Object id = table.getValueAt(table.getSelectedRow(), 0);
			int did=0,cid=0,fid=0,sid=0,lid=0;
			try {
				Connection conn;
				conn = Connect.getConn();
				Statement st = conn.createStatement();
				String search = "select id,cid,did,fid,sid,lid from forest_fires where id="+id+";";
				ResultSet res = st.executeQuery(search);
				while(res.next()) {
					did = res.getInt("did");
					cid = res.getInt("cid");
					fid = res.getInt("fid");
					sid = res.getInt("sid");
					lid = res.getInt("lid");
				}
				String del = "delete from date_factor where id="+did+";";
				String del2 = "delete from climate_factor where id="+cid+";";
				String del3 = "delete from forest_factor where id="+fid+";";
				String del4 = "delete from space_factor where id="+sid+";";
				String del5 = "delete from loss where id="+lid+";";
				String del6 = "delete from forest_fires where id="+id+";";
				
				int res1 = st.executeUpdate(del);
				int res2 = st.executeUpdate(del2);
				int res3 = st.executeUpdate(del3);
				int res4 = st.executeUpdate(del4);
				int res5 = st.executeUpdate(del5);
				int res6 = st.executeUpdate(del6);
				if(res1 + res2 + res3 + res4 +res5 + res6 == 6) {
					JOptionPane.showMessageDialog(f.getContentPane(), "ɾ���ɹ���");
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(f.getContentPane(), "ɾ��ʧ�ܣ�");
				e.printStackTrace();
			}
		}
	}
    
    
}

