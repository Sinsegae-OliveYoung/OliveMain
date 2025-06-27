package com.olive.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JLabel;
import javax.swing.text.BadLocationException;

import com.google.gson.Gson;
import com.olive.mainlayout.MainLayout;

public class ChatClientThread extends Thread {
	Client client;
	Socket socket;
	BufferedReader br;
	BufferedWriter bw;

	Payload p;
	Gson gson;
	String jsonStr;
	Sender sender;

	String now;

	public ChatClientThread(Client client, Socket socket, Sender sender) {
		this.socket = socket;
		this.client = client;
		this.sender = sender;
		now = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

		gson = new Gson();

		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			// 초기 방 배정을 위해 서버에 로그인 유저 정보를 송신 
			send("connect", sender.getUser_name() + "님이 입장하셨습니다.");
		} catch (IOException e) {
			e.printStackTrace();
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

	@Override
	public void run() {
		listen();
	}

	// 서버에서 오는 json 메시지 수신 대기
	public void listen() {
		while (true) {
			try {
				jsonStr = br.readLine(); // json문자열을 서버로부터 수신
				p = gson.fromJson(jsonStr, Payload.class); // json -> Payload
				String msg;
				String now = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")); 

				if (p.getRequestType().equals("connect")) {
				    System.out.println("[클라이언트] connect 받음: " + p.getData());
				    client.doc.setParagraphAttributes(client.doc.getLength(), 1, client.centerAlign, false);
				    client.doc.insertString(client.doc.getLength(), p.getData() + "\n", client.centerAlign);
				}else if (p.getRequestType().equals("message")) {
					msg = " [" + now + "] " + p.getSender().getUser_name() + ": " + p.getData() + " \n";
					System.out.println(this + " 클라이언트 메시지 수신: " + msg);
					client.doc.setParagraphAttributes(client.doc.getLength(), 1, client.leftAlign, false);
					client.doc.insertString(client.doc.getLength(), msg, client.leftAlign);
				} else if (p.getRequestType().equals("disconnect")) {
				    System.out.println("[클라이언트] connect 끊김: " + p.getData());
				    client.doc.setParagraphAttributes(client.doc.getLength(), 1, client.centerAlign, false);
				    client.doc.insertString(client.doc.getLength(), p.getData() + "\n", client.centerAlign);
				}
			} catch (IOException | BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

	public Payload createPayload(String requestType, String msg) { // 요청 상태
		Payload p = new Payload();
		p.setRequestType(requestType);
		p.setSender(sender);
		p.setData(msg);

		return p;
	}
}
