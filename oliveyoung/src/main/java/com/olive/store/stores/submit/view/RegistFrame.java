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
import com.olive.store.StorePage;
import com.olive.store.StorePage;
import com.olive.store.storeconfig.view.StoreConfigMenu;

public class RegistFrame extends JFrame {
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

	StorePage storePage;
	StoreConfigMenu storeConfigMenu;

	public RegistFrame(StorePage storePage, StoreConfigMenu storeConfigMenu) {
		this.storePage = storePage;
		this.storeConfigMenu = storeConfigMenu;

		// create
		p_write = new JPanel();
		lb_name = new JLabel("지점명  ");
		t_name = new JTextField();
		lb_address = new JLabel("매장 주소  ");
		t_address = new JTextArea();
		lb_tel = new JLabel("매장 번호  ");
		t_tel = new JTextField();
		lb_userNo = new JLabel("담당자  ");
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
		t_tel.setToolTipText("ex. 010-1234-5678");
		t_tel.setBackground(Config.LIGHT_GRAY);

		lb_userNo.setPreferredSize(d1);
		lb_userNo.setHorizontalAlignment(JLabel.RIGHT);
		lb_userNo.setFont(new Font("Noto Sans KR", Font.BOLD, 14));

		cb_userNo.setPreferredSize(d2);
		cb_userNo.setToolTipText("사원 번호 - 담당자 이름");
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

		bt_regist.addActionListener(e -> {
			regist();
		});

		setBounds(600, 200, 400, 420);
		setTitle("지점 등록하기");
		setVisible(true);
	}

	public void getUserNo() {
		List<User> userList = userDAO.selectAll();

		User dummy = new User();
		cb_userNo.addItem("사원 번호 - 담당자명");

		for (User user : userList)
			cb_userNo.addItem(user);
	}

	public void insert() {
		Connection con = dbManager.getConnection();
		try {
			con.setAutoCommit(false);

			User user = (User) cb_userNo.getSelectedItem();
			int userId = user.getUser_id();

			Branch branch = new Branch();
			branch.setBr_name(t_name.getText());
			branch.setBr_address(t_address.getText());
			branch.setBr_tel(t_tel.getText());
			branch.setUser(user);

			branchDAO.insert(branch);

			con.commit();
			JOptionPane.showMessageDialog(this, "지점이 등록되었습니다");
			storeConfigMenu.loadData();
			 ((StorePage) storePage).createMenus(); // 사이드 메뉴 재생성
			 storePage.showPanel(0);
			 dispose();
		} catch (BranchException | UserException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
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
		else {
			for (int i = 0; i < storeConfigMenu.storeConfigModel.getRowCount(); i++)
				if (t_name.getText().equals(storeConfigMenu.storeConfigModel.getValueAt(i, 1).toString())) {
					JOptionPane.showMessageDialog(this, "이미 해당 지점이 존재합니다");
					return;
				}
			insert();
		}
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
}
