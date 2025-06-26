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

public class Client extends JFrame {
	// 채팅 메시지 표시 영역
	JTextPane tp = new JTextPane();
	StyledDocument doc = tp.getStyledDocument();
	SimpleAttributeSet leftAlign = new SimpleAttributeSet();
	SimpleAttributeSet rightAlign = new SimpleAttributeSet();
	JScrollPane scroll = new JScrollPane(tp);

	// 전송 영역
	JTextField tf = new JTextField(15);
	
	String ip = "192.168.50.2";
	
	Sender sender;
	MainLayout mainLayout;
	public ChatClientThread clientThread;

	BranchDAO branchDAO = new BranchDAO();

	public Client(MainLayout mainLayout) {
		this.mainLayout = mainLayout;

		try {
			Socket socket = new Socket(ip, 9999); // 이 클라이언트를 서버에 접속시킴 (서버 ip 주소)
			sender = new Sender();

			sender.setUser_name(mainLayout.user.getUser_name());
			sender.setBranch_id(branchDAO.getBranchList(mainLayout.user.getUser_id()).get(0).getBr_id());

			// 애초에 클라이언트 정보를 만들어서 넘겨주고 통신은 클라이언트스레드가 하니까 클라이언트 스레드가 payload를 만드는게 낫겠다.
			clientThread = new ChatClientThread(this, socket, sender);
			clientThread.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		tp.setBackground(Config.LIGHT_GREEN);
		tp.setEditable(false); // 키보드 입력 막기
		tp.setFocusable(false); // 포커스도 못 가게

		// 남이 보낸 메시지: 왼쪽 정렬
		StyleConstants.setAlignment(leftAlign, StyleConstants.ALIGN_LEFT);
		StyleConstants.setForeground(leftAlign, Color.BLUE);
		StyleConstants.setFontSize(leftAlign, 14);

		// 내가 보낸 메시지: 오른쪽 정렬
		StyleConstants.setAlignment(rightAlign, StyleConstants.ALIGN_RIGHT);
		StyleConstants.setForeground(rightAlign, Color.GRAY);
		StyleConstants.setFontSize(rightAlign, 14);

		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scroll);

		add(tf, BorderLayout.SOUTH);

		tf.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
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

		setTitle(branchDAO.getBranchList(mainLayout.user.getUser_id()).get(0).getBr_name());
		setBounds(1300, 300, 300, 600);
		setVisible(false);
	}

}
