package com.skplanet.nlp.client;

import java.io.*;
import java.net.*;

public class Client {

	public static void main(String[] args) {
		try {
			int port = Integer.parseInt(args[0]);
			new Client().startClient(port);
		} catch (Exception e) {
			System.out.println("Something falied: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void startClient(int port) throws IOException {

		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		InetAddress host = null;
		BufferedReader stdIn = null;

		try {
			host = InetAddress.getLocalHost();
			socket = new Socket(host.getHostName(), port);

			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			stdIn = new BufferedReader(new InputStreamReader(System.in));
			String fromServer;
			String fromUser;
			while((fromUser = stdIn.readLine())!=null) {
				if(fromUser.trim().length() == 0) {
					continue;
				} else {
					System.out.println("fromUser : " + fromUser);
					break;
				}
			}
			out.println(fromUser);

			//Read from socket and write back the response to server. 
			while ((fromServer = in.readLine()) != null) {
				System.out.println("Server - " + fromServer);
				if (fromServer.equals("exit"))
					break;

				fromUser = stdIn.readLine();
				if (fromUser != null) {
					System.out.println("Client - " + fromUser);
					out.println(fromUser);
				}
			}
		} catch (UnknownHostException e) {
			System.err.println("Cannot find the host: " + host.getHostName());
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't read/write from the connection: " + e.getMessage());
			System.exit(1);
		} finally { //Make sure we always clean up
			out.close();
			in.close();
			stdIn.close();
			socket.close();
		}
	}
}
