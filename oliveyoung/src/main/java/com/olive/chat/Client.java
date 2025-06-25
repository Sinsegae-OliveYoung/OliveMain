package com.olive.chat;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.olive.common.config.Config;

public class Client extends JFrame{
	JTextArea ta = new JTextArea();
	JTextField tf = new JTextField(15);

	ChatClientThread clientThread;
	
	//클라이언트는 접속하자마자 채팅스레드를 만들면 된다. 
	public Client() {
		
		try {
			Socket socket = new Socket("192.168.60.42", 9999);   //이 클라이언트를 서버에 접속시킴 (서버 ip 주소)
			clientThread = new ChatClientThread(this, socket);
			//clientThread.send("BRANCH:" + 1);
			clientThread.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		tf.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					clientThread.send(tf.getText());
					tf.setText("");
				}
				
			}
		});

		// 디자인 
		ta.setBackground(Config.LIGHT_GREEN);
		ta.setEditable(false);     // 키보드 입력 막기
		ta.setFocusable(false);    // 포커스도 못 가게
		add(ta);
		
		add(tf, BorderLayout.SOUTH);
		
		
		setTitle("클라이언트");
		setBounds(1300, 300, 300, 600);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		Client client = new Client();
		System.out.println(client.toString() + " 클라이언트 실행 ");
	}

}
