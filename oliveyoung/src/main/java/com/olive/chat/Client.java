package com.olive.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.olive.common.config.Config;
import com.olive.common.config.SecurityConfig;
import com.olive.common.repository.BranchDAO;
import com.olive.mainlayout.MainLayout;

public class Client extends JFrame{
	// 채팅 메시지 표시 영역
	JTextPane tp = new JTextPane();
	StyledDocument doc = tp.getStyledDocument();
	SimpleAttributeSet leftAlign = new SimpleAttributeSet();
	SimpleAttributeSet rightAlign = new SimpleAttributeSet();
	SimpleAttributeSet centerAlign = new SimpleAttributeSet();
	JScrollPane scroll = new JScrollPane(tp);
	
	// 전송 영역
	JTextField tf = new JTextField(15);
	
	String ip = SecurityConfig.IP;
    

	Sender sender;
	MainLayout mainLayout;
	public ChatClientThread clientThread;
	
	BranchDAO branchDAO = new BranchDAO();

    String now;
    
	public Client(MainLayout mainLayout) {
		this.mainLayout = mainLayout;
		now = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
		
		tp.setBackground(Config.WHITE);
		tp.setEditable(false);     // 키보드 입력 막기
		tp.setFocusable(false);    // 포커스도 못 가게
		
		//남이 보낸 메시지: 왼쪽 정렬
		StyleConstants.setFontSize(leftAlign, 14);
		StyleConstants.setLineSpacing(leftAlign, 0.2f);
        StyleConstants.setForeground(leftAlign, Color.BLACK);
        StyleConstants.setFontFamily(leftAlign, "Noto Sans KR");
        StyleConstants.setBackground(leftAlign, Config.LIGHT_GREEN);
        StyleConstants.setAlignment(leftAlign, StyleConstants.ALIGN_LEFT);

        // 내가 보낸 메시지: 오른쪽 정렬
        StyleConstants.setFontSize(rightAlign, 14);
        StyleConstants.setLineSpacing(rightAlign, 0.2f);
        StyleConstants.setForeground(rightAlign, Color.BLACK);
        StyleConstants.setFontFamily(rightAlign, "Noto Sans KR");
        StyleConstants.setBackground(rightAlign, Config.LIGHT_GRAY);
        StyleConstants.setAlignment(rightAlign, StyleConstants.ALIGN_RIGHT);
        
        // 내가 보낸 메시지: 가운데 정렬
        StyleConstants.setFontSize(centerAlign, 14);
        StyleConstants.setLineSpacing(centerAlign, 0.2f);
        StyleConstants.setForeground(centerAlign, Config.DARK_GREEN);
        StyleConstants.setFontFamily(centerAlign, "Noto Sans KR");
        StyleConstants.setAlignment(centerAlign, StyleConstants.ALIGN_CENTER);
		
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scroll);
		
		tf.setPreferredSize(new Dimension(getWidth(), 30));
		tf.setBackground(Color.WHITE);
		
		add(tf, BorderLayout.SOUTH);

		tf.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER && !tf.getText().isBlank()) {	// 텍스트가 비어있지 않고 엔터키를 누르면 발생	
					try {
				        doc.setParagraphAttributes(doc.getLength(), 1, rightAlign, false);
						doc.insertString(doc.getLength(), "  " + tf.getText() + " [" + now + "] \n", rightAlign);	// 내가 보낸 텍스트 창
						clientThread.send("message", tf.getText() + "  ");
						tp.setCaretPosition(tp.getDocument().getLength());			// 자동 스크롤
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
					
					tf.setText("");
				}
			}
		});
		
		setTitle(branchDAO.getBranchList(mainLayout.user.getUser_id()).get(0).getBr_name());
		setBounds(1300, 300, 400, 600);
		setVisible(false);
	}
	
	public void connect() {
		try {
			Socket socket = new Socket(ip, 9999);   //이 클라이언트를 서버에 접속시킴 (서버 ip 주소)
			sender = new Sender();
			
			sender.setUser_id(mainLayout.user.getUser_id());
			sender.setUser_name(mainLayout.user.getUser_name());
			sender.setBranch_id(branchDAO.getBranchList(mainLayout.user.getUser_id()).get(0).getBr_id());
			
			// 애초에 클라이언트 정보를 만들어서 넘겨주고 통신은 클라이언트스레드가 하니까 클라이언트 스레드가 payload를 만드는게 낫겠다. 
			clientThread = new ChatClientThread(this, socket, sender);
			clientThread.start();
			System.out.println(" 클라이언트 쓰레드 생성");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}


