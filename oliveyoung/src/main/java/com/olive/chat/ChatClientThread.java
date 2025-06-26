package com.olive.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.text.BadLocationException;

import com.google.gson.Gson;

public class ChatClientThread extends Thread{
	Client client;
	Socket socket;
	BufferedReader br;
	BufferedWriter bw;
	
	Gson gson;
	Sender sender;
	
	public ChatClientThread(Client client, Socket socket, Sender sender) {
		this.socket = socket;
		this.client = client;
		this.sender = sender;
	
		gson = new Gson();
		
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			// 초기 방 배정을 위해 서버에 로그인 유저 정보를 송신 
			send("connect", null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		listen();
	}
	
	//서버에서 오는 json 메시지 수신 대기
	public void listen() {
		while(true) {
			try {
				String jsonStr = br.readLine();  //json문자열을 서버로부터 수신한다.
				Payload p = gson.fromJson(jsonStr, Payload.class); //json 문자열을 다시 Payload로 변환 
				String msg = p.getSender().getUser_name() + ": " + p.getData() + "\n";
				
				System.out.println(this + " 클라이언트 메시지 수신: " + msg);
				
				try {
					client.doc.setParagraphAttributes(client.doc.getLength(), 1, client.leftAlign, false);
					client.doc.insertString(client.doc.getLength(), msg, client.leftAlign);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//서버에 메시지 송신
	public void send(String requestType, String msg) {
		try {
			Payload p = createPayload(requestType, msg); 
			String data = gson.toJson(p);
			System.out.println(this + " 서버로 메시지 전송:  " + msg);
			
			bw.write(data + "\n");
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public Payload createPayload(String requestType, String msg) {  //요청 상태 
		Payload p = new Payload();
		p.setRequestType(requestType);
		p.setSender(sender);
		p.setData(msg);
		
		return p;
	}
}
