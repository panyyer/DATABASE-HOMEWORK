package database;

import java.security.MessageDigest;
import java.sql.*;

//import javax.naming.spi.DirStateFactory.Result;

public class Connect {
	public Connection conn;
	public Statement st;

	Connect() throws Exception{
		Class.forName("com.mysql.jdbc.Driver");

	}
	
	public static Connection getConn() throws SQLException {
		//change this for your own database connection
		Connection conn = DriverManager.getConnection("your connection");
		return conn;
	}
	
	public boolean checkLogin(String username, String password) throws Exception {
	    //change this for your own database connection
	    Connection conn = DriverManager.getConnection("your connection");
	    Statement st = conn.createStatement();
	    ResultSet res = st.executeQuery("select * from user");

    	
	    while(res.next()) {
	    	if(username.equals(res.getString("username")) && password.equals(res.getString("password"))) {
		    	return true;
	    	}
	    }
	    return false;
	}

}
