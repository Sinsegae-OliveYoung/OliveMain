package com.olive.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.olive.common.config.Config;
import com.olive.common.model.Role;
import com.olive.common.model.User;
import com.olive.common.repository.BranchDAO;
import com.olive.mainlayout.MainLayout;

public class Client extends JFrame{
	
	
	JTextPane tp = new JTextPane();
	JScrollPane scroll = new JScrollPane(tp);
	StyledDocument doc;
	SimpleAttributeSet leftAlign, rightAlign;
	
	JTextArea ta = new JTextArea();
	JTextField tf = new JTextField(15);

	ChatClientThread clientThread;
	MainLayout mainLayout;
	
	BranchDAO branchDAO = new BranchDAO();
	String ip = "192.168.10.100";

	//클라이언트는 접속하자마자 채팅스레드를 만들면 된다. 
	public Client(MainLayout mainLayout) {
		this.mainLayout = mainLayout;
		
		
		
		tf.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {		
					try {
				        doc.setParagraphAttributes(doc.getLength(), 1, rightAlign, false);
						doc.insertString(doc.getLength(), tf.getText() + "\n", rightAlign);
						clientThread.send("message", tf.getText());
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
					
					tf.setText("");
				}
				
			}
		});

		// 디자인 
		ta.setBackground(Config.LIGHT_GREEN);
		ta.setEditable(false);     // 키보드 입력 막기
		ta.setFocusable(false);    // 포커스도 못 가게
		//add(ta);
		tp.setBackground(Config.LIGHT_GREEN);
		tp.setEditable(false);     // 키보드 입력 막기
		tp.setFocusable(false);    // 포커스도 못 가게
		doc = tp.getStyledDocument();
		
		leftAlign = new SimpleAttributeSet();
        StyleConstants.setAlignment(leftAlign, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(leftAlign, Color.BLUE);
        StyleConstants.setFontSize(leftAlign, 14);

        
     // 남이 보낸 메시지 (오른쪽)
        rightAlign = new SimpleAttributeSet();
        StyleConstants.setAlignment(rightAlign, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setForeground(rightAlign, Color.GRAY);
        StyleConstants.setFontSize(rightAlign, 14);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
       // scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		add(scroll);
		
		
		add(tf, BorderLayout.SOUTH);
		
		
		
		try {
			Socket socket = new Socket(ip, 9999);   //이 클라이언트를 서버에 접속시킴 (서버 ip 주소)
			Sender sender = new Sender();
			
			sender.setUser_name(mainLayout.user.getUser_name());
			sender.setBranch_id(branchDAO.getBranchList(mainLayout.user.getUser_id()).get(0).getBr_id());
			
			// 애초에 클라이언트 정보를 만들어서 넘겨주자 
			// 통신은 클라이언트스레드가 하니까 클라이언트 스레드가 payload를 만드는게 낫겠다. 
			clientThread = new ChatClientThread(this, socket, sender);
			clientThread.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
		setTitle("클라이언트");
		setBounds(1300, 300, 300, 600);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		User user = new User();
		user.setUser_id(1);
		user.setRole(new Role());
		Client client = new Client(new MainLayout(user));
		System.out.println(client.toString() + " 클라이언트 실행 ");
	}

}
