package login;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Client.Client;

import javax.swing.JPasswordField;

public class Login {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	Connection connection;
	JTextField textField;
	private JPasswordField passwordField;
	
	
	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
		connection = SqlConnection.dataBaseConnector();
		
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 554, 387);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel usernamebtn = new JLabel("Username");
		usernamebtn.setBounds(79, 126, 108, 26);
		frame.getContentPane().add(usernamebtn);
		
		JLabel passwordbtn = new JLabel("Password");
		passwordbtn.setBounds(79, 168, 108, 34);
		frame.getContentPane().add(passwordbtn);
		
		textField = new JTextField();
		textField.setBounds(159, 120, 271, 39);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton loginbtn = new JButton("Login");
		loginbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Connection connection1 = null;
				ResultSet rs = null;
				PreparedStatement pst = null;
				try {
					String query;
					query = "select * from Login where username=? and password=?";
					// pass the query to the prepared statement
					pst = connection.prepareStatement(query);
					pst.setString(1, textField.getText()); // 1 is username value
					pst.setString(2, passwordField.getText()); // 2 is password value It will mask password field can't get text
					
					
					// once query is passed it will store result in rs and the result for rs will increase. if we get one result it will match the username and password and will be correc 
					rs = pst.executeQuery(); // when the query is excuted the result will be transfered to the rs object
					int count = 0;
					while(rs.next()) { 
						count++; 
			
					}
					if (count == 1 ) { // its 2 because username is 1 and password is 2
						JOptionPane.showMessageDialog(null, "Username and Password is correct");
						frame.dispose();
						//Client client = new Client();
						Client.main(null); // run the main from the client class
						
					} else if (count > 2 ) {
						JOptionPane.showMessageDialog(null, "Duplicate Username and Password");
					} else {
						JOptionPane.showMessageDialog(null, "Username and Passwprd is not correct... Please try Again");
					} 
					
					rs.close(); // close the connection with db
					pst.close(); 
					
				} catch(Exception ex) {
					ex.printStackTrace();
					
				}
			}
		});
		loginbtn.setBounds(194, 292, 138, 49);
		frame.getContentPane().add(loginbtn);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(159, 166, 271, 39);
		frame.getContentPane().add(passwordField);
		
		JButton exitbtn = new JButton("Exit");
		exitbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		exitbtn.setBounds(351, 292, 138, 49);
		frame.getContentPane().add(exitbtn);
		
		JLabel lblChatApplicationCreated = new JLabel("Chat Application created by ");
		lblChatApplicationCreated.setBounds(21, 6, 195, 16);
		frame.getContentPane().add(lblChatApplicationCreated);
		
		JLabel lblNewLabel = new JLabel("Mickey - 1438760");
		lblNewLabel.setBounds(20, 29, 167, 16);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblMac = new JLabel("Maciej - 142262");
		lblMac.setBounds(21, 42, 166, 16);
		frame.getContentPane().add(lblMac);
		
		JLabel lblTito = new JLabel("Tito - 1439680");
		lblTito.setBounds(21, 57, 195, 16);
		frame.getContentPane().add(lblTito);
		
		JLabel lblAunie = new JLabel("Aunie - 1234567");
		lblAunie.setBounds(20, 70, 167, 16);
		frame.getContentPane().add(lblAunie);
	}
}
