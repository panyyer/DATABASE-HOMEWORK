package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DetailView {
	
	private JFrame f = new JFrame("��������");
	private JButton ok = new JButton("ȷ��");
	private JPanel p = new JPanel();
	private Object id;
	
	DetailView(Object id) {
		this.id = id;
	}
	
	public void init() {
		try {
			Connection conn = Connect.getConn();
			Statement st = conn.createStatement();
			String search = "select id,cid,did,fid,sid,lid from forest_fires where id="+id;
			ResultSet res = st.executeQuery(search);
			res.next();
			String sql = null;
			int rs = st.executeUpdate(sql);
		} catch (SQLException e) {
			
		}	
	}
}
