package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import com.mysql.jdbc.Statement;

public class Login {
	private String username;
	private String password;

	Login(String username, char[] password) throws Exception {
		this.username = username;
		this.password = encode(password);

	}
	
	public boolean userLogin() throws Exception{
		Connect cn = new Connect();
		return cn.checkLogin(this.username,this.password);
	}
	
	private String encode(char[] str) {
		for(int i=0;i<str.length;i++) {
			str[i] += i;
		}
		return new String(str);
	}
}
