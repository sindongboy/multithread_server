package com.skplanet.nlp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientLocal {

	public static void main(String[] args) {
		try {
			int port = Integer.parseInt(args[0]);
			new ClientLocal().startClient(port);
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
			while ((fromUser = stdIn.readLine()) != null) {
				if (fromUser.trim().length() == 0) {
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
		} catch (IOException e) {
			System.err.println("Couldn't read/write from the connection: " + e.getMessage());
		} finally { //Make sure we always clean up
			out.close();
			in.close();
			stdIn.close();
			socket.close();
		}
	}
}
