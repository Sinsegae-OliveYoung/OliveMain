package com.olive.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JFrame;

public class Server extends JFrame{
	
	
	int index = 1;  // 서버스레드 번호
	ServerSocket server;
	Vector<ChatServerThread> vec;
	
	public Server() {
		// 서버 열기 
		try {
			server = new ServerSocket(9999);
			vec = new Vector<>();
			
			while(true) {
				Socket socket = server.accept();
				System.out.println("접속 감지");
				
				ChatServerThread serverThread = new ChatServerThread(this, socket, index++);
				vec.add(serverThread);
				
				serverThread.start();
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		new Server();
	}

}
