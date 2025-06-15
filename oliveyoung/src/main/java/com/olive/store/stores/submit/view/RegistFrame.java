package com.olive.store.stores.submit.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

import com.olive.common.config.Config;
import com.olive.common.view.SubmitFrame;

public class RegistFrame extends SubmitFrame {
	JPanel p_write;
	JLabel lb_name;
	JTextField t_name;
	JLabel lb_address;
	JTextArea t_address;
	JLabel lb_tel;
	JTextField t_tel;
	JLabel lb_userNo;
	JComboBox cb_userNo;
	
	JPanel p_bt;
	JButton bt_regist;
	
	static String title;
	static int width, height;
	
	public RegistFrame() {
		super("지점 등록하기", 350, 350);
		
		// create
		p_write = new JPanel();
		lb_name = new JLabel("지점명    ");
		t_name = new JTextField();
		lb_address = new JLabel("매장 주소    ");
		t_address = new JTextArea();
		lb_tel = new JLabel("매장 번호    ");
		t_tel = new JTextField();
		lb_userNo = new JLabel("담당자 사원번호    ");
		cb_userNo = new JComboBox<>();
		
		p_bt = new JPanel();
		bt_regist = new JButton("등록    ");
		
		// style
		Dimension d1 = new Dimension(100, 30);
		Dimension d2 = new Dimension(180, 30);

		p_write.setBackground(Config.WHITE);
		p_write.setPreferredSize(new Dimension(330, 280));

		lb_name.setPreferredSize(d1);
		lb_name.setHorizontalAlignment(JLabel.RIGHT);
		
		t_name.setPreferredSize(d2);

		lb_address.setPreferredSize(d1);
		lb_address.setHorizontalAlignment(JLabel.RIGHT);

		t_address.setPreferredSize(new Dimension(180, 60));
		t_address.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		lb_tel.setPreferredSize(d1);
		lb_tel.setHorizontalAlignment(JLabel.RIGHT);
		
		t_tel.setPreferredSize(d2);

		lb_userNo.setPreferredSize(d1);
		lb_userNo.setHorizontalAlignment(JLabel.RIGHT);

		cb_userNo.setPreferredSize(d2);
		cb_userNo.setUI(new CustomComboBoxUI());
		cb_userNo.setBackground(Color.WHITE);
		cb_userNo.setBorder(new LineBorder(Color.GRAY, 1, true));
		
		p_bt.setBackground(Config.WHITE);
		
		bt_regist.setPreferredSize(d1);
		bt_regist.setBackground(Config.LIGHT_GREEN);
		
		
		
		
		////////////////// 띄우기 ///////////
		
		
		// assemble
		p_write.add(lb_name);
		p_write.add(t_name);
		p_write.add(Box.createVerticalStrut(30));
		p_write.add(lb_address);
		p_write.add(t_address);
		p_write.add(Box.createVerticalStrut(30));
		p_write.add(lb_tel);
		p_write.add(t_tel);
		p_write.add(Box.createVerticalStrut(30));
		p_write.add(lb_userNo);
		p_write.add(cb_userNo);
		p_write.add(Box.createVerticalStrut(30));
		add(p_write, BorderLayout.NORTH);
		
		p_bt.add(bt_regist);
		add(p_bt);
		
		setVisible(true);
	}

class CustomComboBoxUI extends BasicComboBoxUI {
    @Override
    protected JButton createArrowButton() {
        JButton button = new JButton("▼");
        button.setBackground(Config.LIGHT_GREEN);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createEmptyBorder());
        return button;
    }
    
}
	public static void main(String[] args) {
		new RegistFrame();
	}
}
