package login;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JOptionPane;
public class SqlConnection {
	Connection conn = null;
	
	public static Connection dataBaseConnector() {
		try {
			Class.forName("org.sqlite.JDBC");//Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/mickeyenglish/Desktop/LoginForm2.sqlite");// path of database file
			Connection conn = DriverManager.getConnection("jdbc:sqlite:LoginForm2.sqlite");// root path of chat app 
			return conn;
			
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Connection Unccessful");
			System.out.println("Connection to the DB Unccessfull");
			return null;
		}
		
	}

}

