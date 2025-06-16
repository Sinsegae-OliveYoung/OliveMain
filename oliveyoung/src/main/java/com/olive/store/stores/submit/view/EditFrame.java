package com.olive.store.stores.submit.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

import com.olive.common.config.Config;
import com.olive.common.exception.BranchException;
import com.olive.common.exception.UserException;
import com.olive.common.model.Branch;
import com.olive.common.model.User;
import com.olive.common.repository.BranchDAO;
import com.olive.common.repository.UserDAO;
import com.olive.common.util.DBManager;
import com.olive.store.storeconfig.view.StoreConfigMenu;

public class EditFrame extends JFrame {
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

	DBManager dbManager = DBManager.getInstance();
	BranchDAO branchDAO;
	UserDAO userDAO;

	private Branch branch;
	private StoreConfigMenu storeConfigMenu;
	int br_id;
	
	public EditFrame(StoreConfigMenu storeConfigMenu, Branch branch) {
		this.storeConfigMenu = storeConfigMenu;
		this.branch = branch;

		// create
		p_write = new JPanel();
		lb_name = new JLabel("지점명  ");
		t_name = new JTextField();
		lb_address = new JLabel("매장 주소  ");
		t_address = new JTextArea();
		lb_tel = new JLabel("매장 번호  ");
		t_tel = new JTextField();
		lb_userNo = new JLabel("담당자 사원번호  ");
		cb_userNo = new JComboBox<>();

		p_bt = new JPanel();
		bt_regist = new JButton("등록");

		branchDAO = new BranchDAO();
		userDAO = new UserDAO();
		
		// style
		Dimension d1 = new Dimension(140, 30);
		Dimension d2 = new Dimension(180, 30);

		p_write.setBackground(Config.WHITE);
		p_write.setPreferredSize(new Dimension(500, 290));
		p_write.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 30));

		lb_name.setPreferredSize(d1);
		lb_name.setHorizontalAlignment(JLabel.RIGHT);
		lb_name.setFont(new Font("Noto Sans KR", Font.BOLD, 14));

		t_name.setPreferredSize(d2);
		t_name.setBackground(Config.LIGHT_GRAY);

		lb_address.setPreferredSize(d1);
		lb_address.setHorizontalAlignment(JLabel.RIGHT);
		lb_address.setFont(new Font("Noto Sans KR", Font.BOLD, 14));

		t_address.setPreferredSize(new Dimension(180, 60));
		t_address.setBackground(Config.LIGHT_GRAY);
		t_address.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		lb_tel.setPreferredSize(d1);
		lb_tel.setHorizontalAlignment(JLabel.RIGHT);
		lb_tel.setFont(new Font("Noto Sans KR", Font.BOLD, 14));

		t_tel.setPreferredSize(d2);
		t_tel.setBackground(Config.LIGHT_GRAY);

		lb_userNo.setPreferredSize(d1);
		lb_userNo.setHorizontalAlignment(JLabel.RIGHT);
		lb_userNo.setFont(new Font("Noto Sans KR", Font.BOLD, 14));

		cb_userNo.setPreferredSize(d2);
		cb_userNo.setUI(new CustomComboBoxUI());
		cb_userNo.setBackground(Config.LIGHT_GRAY);
		cb_userNo.setBorder(new LineBorder(Color.GRAY, 1, true));

		p_bt.setBackground(Config.WHITE);
		p_bt.setPreferredSize(new Dimension(330, 80));

		bt_regist.setPreferredSize(new Dimension(80, 30));
		bt_regist.setBackground(Config.LIGHT_GREEN);
		bt_regist.setFont(new Font("Noto Sans KR", Font.BOLD, 15));

		// assemble
		p_write.add(lb_name);
		p_write.add(t_name);
		p_write.add(Box.createVerticalStrut(60));
		p_write.add(lb_address);
		p_write.add(t_address);
		p_write.add(Box.createVerticalStrut(60));
		p_write.add(lb_tel);
		p_write.add(t_tel);
		p_write.add(Box.createVerticalStrut(60));
		p_write.add(lb_userNo);
		p_write.add(cb_userNo);
		add(p_write, BorderLayout.NORTH);

		p_bt.add(Box.createVerticalStrut(60));
		p_bt.add(bt_regist);
		add(p_bt);

		getUserNo();
		load();
		
		bt_regist.addActionListener(e -> {
			regist();
		});
		
		setBounds(600, 200, 400, 420);
		setTitle("지점 수정하기");
		setVisible(true);
	}

	public void getUserNo() {
		List<User> userList = userDAO.selectAll();

		User dummy = new User();
		cb_userNo.addItem("사원 번호 - 담당자명");
		
		for (User user : userList)
			cb_userNo.addItem(user);
	}
	
	// 테이블에서 누른 값 받아오기
	public void load() {
			br_id = branch.getBr_id();
			t_name.setText(branch.getBr_name());
			t_address.setText(branch.getBr_address());
			t_tel.setText(branch.getBr_tel());
			cb_userNo.setSelectedItem(branch.getUser().getUser_no() + " - " + branch.getUser().getUser_name());
	}

	public void update() {
		Connection con = dbManager.getConnection();
		try {
			con.setAutoCommit(false);
			
			User user = (User) cb_userNo.getSelectedItem();
			
			Branch branch = new Branch();
			branch.setBr_name(t_name.getText());
			branch.setBr_address(t_address.getText());
			branch.setBr_tel(t_tel.getText());
			branch.setBr_id(br_id);
			branch.setUser(user);
			
			branchDAO.update(branch);
					
			con.commit();
			JOptionPane.showMessageDialog(this, "지점이 수정되었습니다");
			storeConfigMenu.loadData();
			dispose();
		} catch (BranchException | UserException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();														
			JOptionPane.showMessageDialog(this, e.getMessage());	
		}	catch (SQLException e) {
			e.printStackTrace();
		}	finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void regist() {
			if (t_name.getText().length() < 1) 
				JOptionPane.showMessageDialog(this, "지점명을 입력하세요");
			else if (t_address.getText().length() < 1) 
				JOptionPane.showMessageDialog(this, "매장 주소를 입력하세요");
			else if (t_tel.getText().length() < 1)
				JOptionPane.showMessageDialog(this, "매장 번호를 입력하세요");
			else if (cb_userNo.getSelectedIndex() < 1)
				JOptionPane.showMessageDialog(this, "담당자를 선택하세요");
			else
				update();
		}
	
	// ComboBox ui
	class CustomComboBoxUI extends BasicComboBoxUI {
		protected JButton createArrowButton() {
			JButton button = new JButton("▼");
			button.setBackground(Config.LIGHT_GREEN);
			button.setForeground(Color.BLACK);
			button.setBorder(BorderFactory.createEmptyBorder());
			return button;
		}
	}
}
