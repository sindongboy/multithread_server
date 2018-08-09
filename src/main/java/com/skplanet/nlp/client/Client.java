package com.skplanet.nlp.client;

import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

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

		String hostIp="10.48.19.193";
		String user="shindonghun01";
		String password="a00794405";
		int pport=22;

		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");

		String remoteHost="192.168.11.21";

		int remotePort=8893;

		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		InetAddress host = null;
		BufferedReader stdIn = null;

		try {

			JSch jsch=new JSch();
			Session session=jsch.getSession(user, hostIp, pport);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			Channel channel=session.openChannel("direct-tcpip");

			((ChannelDirectTCPIP)channel).setHost(remoteHost);
			((ChannelDirectTCPIP)channel).setPort(remotePort);

			channel.connect(10000);

			/*
			host = InetAddress.getLocalHost();
			socket = new Socket(host.getHostName(), port);

			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			*/
			out = new PrintWriter(channel.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(channel.getInputStream()));


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
			channel.disconnect();
		} catch (UnknownHostException e) {
			System.err.println("Cannot find the host: " + host.getHostName());
		} catch (IOException e) {
			System.err.println("Couldn't read/write from the connection: " + e.getMessage());
		} catch (JSchException e) {
			e.printStackTrace();
		} finally { //Make sure we always clean up
			out.close();
			in.close();
			stdIn.close();
			socket.close();
		}
	}
}
