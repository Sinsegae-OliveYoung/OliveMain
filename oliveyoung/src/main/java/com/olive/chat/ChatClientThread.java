package com.olive.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ChatClientThread extends Thread{
	
	Socket socket;
	BufferedReader br;
	BufferedWriter bw;
	
	public ChatClientThread(Socket socket) {
		this.socket = socket;
		
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
	
	//서버에서 오는 메시지 수신 대기
	public void listen() {
		while(true) {
			try {
				String msg = br.readLine();
				System.out.println(this + " 클라이언트 메시지 수신: " + msg);
				//화면에 메시지 표시
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//서버에 메시지 송신
	public void send(String msg) {
		try {
			System.out.println(this + " 서버로 메시지 전송:  " + msg);
			bw.write(msg + "\n");
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
