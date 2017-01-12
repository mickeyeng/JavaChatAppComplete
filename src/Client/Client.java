package Client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JEditorPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Client extends JFrame {

	private JPanel contentPane;
	private static JTextField writeMessageBox;
	private static JTextArea showMessagesBox;

	public final static int port_p = 4000;
	String name, address;
	int port;

	String host = "localhost";
	static Socket socket = null;
	private static String message = "";
	static BufferedReader in;
	static PrintWriter out;

	/**
	 * Launch the application.
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 *
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					Client client = new Client();
					client.startRunning();
					InputThread();
					Client frame = new Client();
					
					
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * Create the frame.
	 */

	public Client() {
		super("Chat Clinet");
		setBackground(Color.WHITE);
		setResizable(false);
		setSize(800, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 200, 800, 600); // set location of GUI and size of GUI
										// window
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		
		JButton send = new JButton("Send");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					out.println(writeMessageBox.getText());
					message = writeMessageBox.getText();
					out.flush();
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				writeMessageBox.setText(""); // once message has sent from text
												// area will set box to null
				showMessagesBox.append("\n" + message);

			}
		});
		send.setBounds(562, 528, 117, 29);
		contentPane.add(send);

		JButton btnExit = new JButton("Exit");
		btnExit.setBackground(Color.BLUE);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});
		btnExit.setBounds(677, 528, 117, 29);
		contentPane.add(btnExit);

		JLabel lblListOfUsers = new JLabel("Help Commands");
		lblListOfUsers.setBounds(588, 42, 130, 16);
		contentPane.add(lblListOfUsers);
		

		writeMessageBox = new JTextField();
		writeMessageBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
	            {
					try {
						out.println(writeMessageBox.getText());
						message = writeMessageBox.getText();
						out.flush();
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					writeMessageBox.setText(""); // once message has sent from text
													// area will set box to null
					showMessagesBox.append("\n" + message);
	            }
			}
		});
		writeMessageBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		writeMessageBox.setBounds(0, 511, 558, 61);
		contentPane.add(writeMessageBox);
		writeMessageBox.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 42, 547, 457);
		contentPane.add(scrollPane);

		showMessagesBox = new JTextArea();
		scrollPane.setViewportView(showMessagesBox);
		showMessagesBox.setEditable(false);
		showMessagesBox.setBackground(Color.WHITE);

		
		
		JEditorPane dtrpnHelpCommands = new JEditorPane();
		dtrpnHelpCommands.setToolTipText("");
		dtrpnHelpCommands.setText("Click exit to end chat\n" + "\n" + "Enter SET USER Followed by\n\nA name of your choice to set a\nusername\n\nSend to send a message");
		dtrpnHelpCommands.setBounds(573, 70, 204, 183);
		contentPane.add(dtrpnHelpCommands);
		
		

	}

	public void connectToServer() throws IOException {
		showMessage("Connection Established!" + "\n" + "Connected to: " + socket.getInetAddress().getHostName() + "\n");
		showMessage("Welcome to the chat client please enter some text\n");

	}

	public void startRunning() throws IOException {
		try {
			socket = new Socket(host, port_p);
			setUpStreams();
			connectToServer();

		} catch (UnknownHostException e) {
			System.out.println(e);
		} 

	}

	public void setUpStreams() throws IOException {

		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		// showMessage("streams setup");
	}

	public static void closeConnection() throws IOException {
		socket.close();
		in.close();
		out.close();
		System.exit(1);
	}

	private static void showMessage(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				showMessagesBox.append(message);
				//cmdsBox.append(message);
			}
		});
	}
	




	public static void InputThread() {
		Thread inputThread = new Thread() {
			public void run() {
				try {
					BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
					String line = userIn.readLine();
					while (line != null && !line.equals(".")) {
						out.println(line);
						System.out.println("\n");
						line = userIn.readLine();
						if (line.equalsIgnoreCase("exit")) {
							break;
						} 

					} // end of while
					out.close();
					in.close();
				} catch (SocketException e) {

				} catch (IOException e) {
					System.out.println("Exception reading user input");
					// e.printStackTrace();
				}
			}
		};
		inputThread.start();

		// read messsages from server and write to console. problem it was
		// writing to server socket
		Thread serverMessageInputThread = new Thread() {
			public void run() {
				try {
					BufferedReader serverMessageReader = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));
					String line = serverMessageReader.readLine();
					while (line != null) {
						showMessage("\n" + line);
						line = serverMessageReader.readLine();
					} // end of while

					out.close();
					serverMessageReader.close();
				} catch (SocketException e) { // socket exception will just exit
												// back to the cmd line once the
												// socket is closed and not
												// print any errors to the
												// console
				} catch (IOException e) {
					System.out.println("Exception reading server input");
					e.printStackTrace();
				}
			}
		};
		serverMessageInputThread.start();

	} // end of inputThred
}