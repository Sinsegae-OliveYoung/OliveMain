package com.olive.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

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
			Payload p = createPayload("connect");
			String msg = gson.toJson(p);
			send(msg);
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
				client.ta.append(msg + "\n");
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

	
	// connect/disconnect payload
	public Payload createPayload(String requestType) {  //요청 상태 
		Payload p = new Payload();
		p.setRequestType(requestType);
		p.setSender(sender);
		
		return p;
	}
	
	// message 전송 payload
	public Payload createPayload(String requestType, String msg) {  //요청 상태 
		Payload p = createPayload(requestType);
		p.setData(msg);
		
		return p;
	}

}
