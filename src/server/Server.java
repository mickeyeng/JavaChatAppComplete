package server;

import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;

import Client.Client;

import java.io.*;

public class Server {

	public final static int port_p = 4000;
	public static int clientNo = 0;
	String result = null;
	static ArrayList<Socket> clientConnections;
	static ArrayList<String> userList = new ArrayList<String>();
	static ArrayList<Client> clients = new ArrayList<Client>(); 
	String message;
	ServerSocket serverSocket;
	Socket clientConnection;

	public static void main(String[] args) throws IOException {
		new Server().runServer();		
	} // end of main

	public void runServer() throws IOException {
		ServerSocket serverSocket = null;
		Socket clientConnection = null;
		clientConnections = new ArrayList<Socket>();

		try {

			serverSocket = new ServerSocket(port_p);
			System.out.println("Waiting for connection....." + "\n");

			while (true) {
				
				clientConnection = serverSocket.accept();
				clientConnections.add(clientConnection);
				clientNo++; // increments the client number by 1
				
				
				
				
				
				Thread t = new Thread(new ServerThreads(clientConnection, userList));
				t.start();
				

				userList.add(clientConnection.getInetAddress().toString());
				System.out.println("New connection..");
				System.out.println("\n");
				System.out.println("Welcome to Mickey's server...." + InetAddress.getLocalHost() + " " + "Client" + " "
						+ clientNo);
				System.out.println("Size of UserList: " + userList.size());
				System.out.println("\n");
				
			

			}
			
			
		
		
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException ioe) {
				//System.out.println("Connection in use!");
				ioe.printStackTrace();
				System.exit(1);
			}
		}

	}
	
	
	
	
	
	
	// save all client output streams and iterate over each connection in the
	// array and write message to out socket
	public void broadcastAll(String username, String message, Socket senderConnection) throws IOException {
		for (Socket connection : clientConnections) {
			if (!connection.equals(senderConnection)) {
				System.out.println("sending message to all clients" + "\n");
				PrintWriter out = new PrintWriter(connection.getOutputStream());
				out.println(username + ": " + message);
				out.flush();
				//System.out.println("list of client connected" + clientConnections);
			} 
		}
		
	} // close broadcast
	
	// Send message from server to client
		public void sendFromServer() throws IOException {
			PrintWriter out = new PrintWriter(clientConnection.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String line = in.readLine();
			while (line != null && !line.equals(".")) {
				out.println(line);
				line = in.readLine();
				
		}
	}

	
} // end of class