package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerThreads extends Thread {
	Server server = new Server();
	Socket clientConnection = null;
	BufferedReader in;
	PrintWriter out;
	int clientNo;
	ArrayList<String> userList;
	String username;

	// Main
	public ServerThreads(Socket clientConnection, ArrayList<String> userList) {
		this.clientConnection = clientConnection;
		this.userList = userList;
		this.username = "anonymous";

	}

	// Run Server
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
			out = new PrintWriter(clientConnection.getOutputStream(), true);
			while (true) {
				
				
				
				String inputLine = in.readLine();
				System.out.println("Incoming client message");

				while (inputLine != null) {
					System.out.println(inputLine);
					// ^ START OF THE string. SET USER IS THE STRING IT SHOULD
					// MATCH. (. = ANY CHAR .+ 1 OR MORE) AND $ END OF STRING
					Pattern pattern = Pattern.compile("^SET USER (.+)$");
					// comparing the pattern with the Inputline string
					Matcher matcher = pattern.matcher(inputLine);

					if (matcher.matches()) {
						this.username = matcher.group(1);
						System.out.println(this.username);
					} else {
						// message read from client input stream adds a new line
						// after input line
						server.broadcastAll(username, inputLine, clientConnection);
						if (inputLine.equalsIgnoreCase("exit")) {
							break;
						}
						
						

					}
					inputLine = in.readLine();

				} // end of while

				closeConnections();
			}

		} catch (IOException e) {
			System.out.println("Client closed session" + "\n");
		}

	}
	// End of Run Server
	


	// Close streams
	public void closeConnections() {
		System.out.println("Closing Connections ......");
		try {
			System.out.println("closing out");
			out.close();
			System.out.println("closing in");
			in.close();
			clientConnection.close();

		} catch (IOException ioException) {
			System.out.print("..........");

		}

	} // END of close streams
	
	
	public String[] userList() {
		String[] names = new String[userList.size()];
		for (int i = 0; i < userList.size(); ++i) {
			names[i] = userList.get(i);
		}
		return names;
	}

}
