package com.olive.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServerThread extends Thread{
	
	int index;
	Server server;
	Socket socket;
	BufferedReader br;
	BufferedWriter bw;
	
	public ChatServerThread(Server server, Socket socket, int index) {
		this.server = server;
		this.socket = socket;
		this.index = index;  // 서버의 숫자 
		
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		listen();
	}
	
	
	//클라이언트로부터 메시지 수신 대기 
	// 수신을 하면 이걸 다른 클라이언트에게 전송해야함. 
	public void listen() {
		while(true) {
			try {
				
				String msg = br.readLine();
				System.out.println("서버스레드" + index + " 메시지 수신: " + msg);
				
				// 여기서 다른 서버스레드의 send를 호출해서 다른 클라이언트에도 메시지를 전송  
				for(int i = 0; i < server.vec.size(); i++) {
					ChatServerThread serverThread = server.vec.get(i);
					serverThread.send(msg);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
	// 한 클라이언트에만 전송하는것이다. 지금은 
	public void send(String msg) {
		try {
			bw.write(msg);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
