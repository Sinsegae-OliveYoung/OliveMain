package com.olive.chat;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class Client extends JFrame{
	
	JTextField tf = new JTextField(15);

	ChatClientThread clientThread;
	
	//클라이언트는 접속하자마자 채팅스레드를 만들면 된다. 
	public Client() {
		
		add(tf);
		
		try {
			Socket socket = new Socket("192.168.60.42", 9999);   //이 클라이언트를 서버에 접속시킴 (서버 ip 주소)
			clientThread = new ChatClientThread(socket);
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
				}
				
			}
		});
		
		setSize(200, 200);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		Client client = new Client();
		System.out.println(client.toString() + " 클라이언트 실행 ");
	}

}

/**
 * 클라이언트는 접속을 해야됨 
 */
