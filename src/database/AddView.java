package database;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

//import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.border.*;

public class AddView {
	private JFrame f = new JFrame("添加火灾数据");
	private JButton ok = new JButton("增加");
	private JButton cancel = new JButton("取消");
	private int size = 15;
	private JTextField month = new JTextField(size);
	private JTextField day = new JTextField(size);
	private JTextField area = new JTextField(size);
	private JTextField money = new JTextField(size);
	private JTextField wounded = new JTextField(size);
	private JTextField deadth = new JTextField(size);
	private JTextField FFMC = new JTextField(size);
	private JTextField DMC = new JTextField(size);
	private JTextField DC = new JTextField(size);
	private JTextField ISI = new JTextField(size);
	private JTextField temp = new JTextField(size);
	private JTextField RH = new JTextField(size);
	private JTextField wind = new JTextField(size);
	private JTextField rain = new JTextField(size);
	private JTextField X = new JTextField(size);
	private JTextField Y = new JTextField(size);
	
	private JLabel Jmonth = new JLabel("月份");
	private JLabel Jday = new JLabel("星期");
	private JLabel Jarea = new JLabel("火灾面积");
	private JLabel Jmoney = new JLabel("经济损失");
	private JLabel Jwounded = new JLabel("受伤人数");
	private JLabel Jdeadth = new JLabel("死亡人数");
	private JLabel JFFMC = new JLabel("细小可燃物湿度");
	private JLabel JDMC = new JLabel("腐殖质湿度码");
	private JLabel JDC = new JLabel("干旱码");
	private JLabel JISI = new JLabel("初始蔓延指数");
	private JLabel Jtemp = new JLabel("温度");
	private JLabel JRH = new JLabel("相对湿度");
	private JLabel Jwind = new JLabel("风力");
	private JLabel Jrain = new JLabel("降雨量");
	private JLabel JX = new JLabel("X坐标");
	private JLabel JY = new JLabel("Y坐标");
	
	private JTextField[] field = {month,day,area,money,wounded,deadth,FFMC,DMC,DC,ISI,temp,RH,wind,rain,X,Y};
	private JLabel[] label = {Jmonth,Jday,Jarea,Jmoney,Jwounded,Jdeadth,JFFMC,JDMC,JDC,JISI,Jtemp,JRH,Jwind,Jrain,JX,JY};
	
	public void init() {
		JPanel p = new JPanel();
		p.setBorder(new TitledBorder(new EtchedBorder(),"New Data",TitledBorder.LEFT,TitledBorder.TOP));
		JPanel[] p2 = new JPanel[16];
		p.setLayout(null);
		for(int i=0;i<field.length-1;i+=2) {
			p.add(label[i]);
			p.add(field[i]);
			p.add(label[i+1]);
			p.add(field[i+1]);
			label[i].setBounds(30,30*i+10,100,50);
			field[i].setBounds(30+100,30*i+20,200,30);
			label[i+1].setBounds(30+360,30*i+10,100,50);
			field[i+1].setBounds(30+450,30*i+20,200,30);
		}
		p.add(ok);
		p.add(cancel);
		cancel.addActionListener(new CancelListener());
		ok.addActionListener(new AddListener());
		ok.setBounds(270,500,80,30);
		cancel.setBounds(380,500,80,30);
		f.add(p);
		f.pack();
		f.setBounds(280, 100, 800, 600);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setVisible(true);
	}
	
	class CancelListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			f.dispose();
		}
	}
	
	class AddListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try {
				String _month = month.getText();
				String _day = day.getText();
				int _X = Integer.parseInt(X.getText());
				int _Y = Integer.parseInt(Y.getText());
				int _deadth = Integer.parseInt(deadth.getText());
				int _wounded = Integer.parseInt(wounded.getText());
				int _RH = Integer.parseInt(RH.getText());
				int _wd = _wounded + _deadth;
				double _DC = Double.parseDouble(DC.getText());
				double _FFMC = Double.parseDouble(FFMC.getText());
				double _DMC = Double.parseDouble(DMC.getText());
				double _ISI = Double.parseDouble(ISI.getText());
				double _rain = Double.parseDouble(rain.getText());
				double _wind = Double.parseDouble(wind.getText());
				double _temp = Double.parseDouble(temp.getText());
				double _area = Double.parseDouble(area.getText());
				double _money = Double.parseDouble(money.getText());
				
				Connection conn = Connect.getConn();
				Statement st = conn.createStatement();
				String sql = "insert into date_factor(month,day) values("+"'"+_month+"'"+","+"'"+_day+"'"+");";
				String sql2 = "insert into climate_factor(temp,RH,wind,rain) values("+_temp+","+_RH+","+_wind+","+_rain+");";
				String sql3 = "insert into forest_factor(FFMC,DMC,DC,ISI) values("+_FFMC+","+_DMC+","+_DC+","+_ISI+");";
				String sql4 = "insert into loss(money,wd) values("+_money+","+_wd+");";
				String sql5 = "insert into space_factor(X,Y) values("+_X+","+_Y+");";
				int res = st.executeUpdate(sql);
				int res2 = st.executeUpdate(sql2);
				int res3 = st.executeUpdate(sql3);
				int res4 = st.executeUpdate(sql4);	
				int res5 = st.executeUpdate(sql5);
				
				String id = "select id from date_factor where month="+"'"+_month+"'"+" and day="+"'"+_day+"'"+" order by id desc;";
				String id2 = "select id from climate_factor where temp="+_temp+" and RH="+_RH+" and wind="+_wind+" and rain="+_rain+" order by id desc;";
				String id3 = "select id from forest_factor where FFMC="+_FFMC+" and DMC="+_DMC+" and DC="+_DC+" and ISI="+_ISI+" order by id desc;";
				String id4 = "select id from loss where money="+_money+" and wd="+(_wounded+_deadth)+" order by id desc;";
				String id5 = "select id from space_factor where X="+_X+" and Y="+_Y+" order by id desc;";
				
				ResultSet rs = st.executeQuery(id);
				rs.next();
				int did = rs.getInt(1);
				rs = st.executeQuery(id2);
				rs.next();
				int cid = rs.getInt(1);
				rs = st.executeQuery(id3);
				rs.next();
				int fid = rs.getInt(1);
				rs = st.executeQuery(id4);
				rs.next();
				int lid = rs.getInt(1);
				rs = st.executeQuery(id5);
				rs.next();
				int sid = rs.getInt(1);
                double level = 0;
	             if(_area > 0) {
	                 level = _area*0.03+_deadth+_wounded;
	             }
	             int[] len = {0,0,0,0,0,0,0};
	             double[] arr = {_area,_FFMC,_DMC,_temp,_DC,_RH,_rain};
	             for(int i=0;i<7;i++) {
	                 while(arr[i] >= 1) {
	                     len[i]++;
	                     arr[i]/=10;
	                 }
	             }
	             double possibility = _area*Math.pow(10,5-len[0])*0.2 + _FFMC*Math.pow(10,5-len[1])*0.3 + _DMC*Math.pow(10,5-len[2])*0.05 + _temp*Math.pow(10,5-len[3])*0.2 + _DC*Math.pow(10,5-len[4])*0.05 + _RH*Math.pow(10,5-len[5])*0.1 + _rain*Math.pow(10,5-len[6])*0.1;
	             possibility/=Math.pow(10,5);

	             while(!(possibility < 1)) {
	                 possibility/=10.0;
	             }
				String sql6 = "insert into forest_fires(cid,did,fid,sid,lid,area,possibility,level) values("+cid+","+did+","+fid+","+sid+","+lid+","+_area+","+possibility+","+level+");";
				int res6 = st.executeUpdate(sql6);

				if(res + res2 + res3 + res4 + res5 + res6 == 6) {
					JOptionPane.showMessageDialog(f.getContentPane(), "操作成功！");
				} else {
					JOptionPane.showMessageDialog(f.getContentPane(), "操作失败！");
				}
			} catch(Exception ee) {
//				System.out.println(cid);
//				System.out.println(ee.getMessage());
				ee.printStackTrace();
				JOptionPane.showMessageDialog(f.getContentPane(), "数据不能为空！");
			}
		}
	}
}


