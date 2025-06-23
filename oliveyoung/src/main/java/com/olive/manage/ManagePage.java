package com.olive.manage;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.olive.common.config.Config;
import com.olive.common.model.Bound;
import com.olive.common.model.Member;
import com.olive.common.view.Page;
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;
import com.olive.manage.approval.ApprovalDetailPanel;
import com.olive.manage.approval.ApprovalListPanel;
import com.olive.manage.user.UserDetailPanel;
import com.olive.manage.user.UserListPanel;

//슬라이드 기능 (접었다 폈다)
public class ManagePage extends Page{
	
	JPanel p_side;
	JLabel la_user;
	JButton bt_user_list;
	JLabel la_approval;
	JButton bt_approval_list;
	JPanel p_content;
	Panel[] panels;
	
	CardLayout cardLayout;
	String previousKey;  //이전 페이지 기억하기 위한 키(cardlayout의 key)
	String currentKey = ManageConfig.USER_LIST_KEY;
	
	UserListPanel userListPanel;
	UserDetailPanel userDetailPanel;
	ApprovalListPanel approvalListPanel;
	ApprovalDetailPanel approvalDetailPanel;
	
	
	public ManagePage(MainLayout mainLayout) {
		super(mainLayout);
		setLayout(new BorderLayout());
		
		userListPanel = new UserListPanel(mainLayout, ManageConfig.USER_LIST_TITLE, this);
		userDetailPanel = new UserDetailPanel(mainLayout, ManageConfig.USER_DETAIL_TITLE, this);
		approvalListPanel = new ApprovalListPanel(mainLayout, ManageConfig.APPROVAL_LIST_TITLE, this); 
		approvalDetailPanel = new ApprovalDetailPanel(mainLayout, ManageConfig.APPROVAL_DETAIL_TITLE, this);
		
		// create
		p_side = new JPanel();
		la_user = new JLabel("사용자 관리");
		bt_user_list = new JButton("  사용자 목록");
		la_approval = new JLabel("결재 관리");
		bt_approval_list = new JButton("  결재 목록");
		
		cardLayout = new CardLayout();
		p_content = new JPanel(cardLayout);
		
		//style
		Font topFont = new Font("Noto Sans KR", Font.BOLD, 18);
		Font subFont = new Font("Noto Sans KR", Font.BOLD, 14);
		
		p_side.setBackground(Config.LIGHT_GRAY);
		p_side.setLayout(new BoxLayout(p_side, BoxLayout.Y_AXIS));
		p_side.setBorder(BorderFactory.createEmptyBorder(0,27,0,0));
		p_side.setPreferredSize(new Dimension(Config.SIDE_W, Config.SIDE_H));
		
		la_user.setFont(topFont);
		bt_user_list.setFont(subFont);
		bt_user_list.setBackground(Config.LIGHT_GRAY);
		bt_user_list.setFocusPainted(false);
		bt_user_list.setBorder(null);
		
		la_approval.setFont(topFont);
		bt_approval_list.setFont(subFont);
		bt_approval_list.setBackground(Config.LIGHT_GRAY);
		bt_approval_list.setFocusPainted(false);
		bt_approval_list.setBorder(null);

		p_content.setBackground(Config.WHITE);
		p_content.setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));
		
		p_side.add(Box.createVerticalStrut(25));
		p_side.add(la_user);	
		p_side.add(Box.createVerticalStrut(15));
		p_side.add(bt_user_list);	
		
		p_side.add(Box.createVerticalStrut(25));
		p_side.add(la_approval);	
		p_side.add(Box.createVerticalStrut(15));
		p_side.add(bt_approval_list);
		
		add(p_side, BorderLayout.WEST);
		add(p_content, BorderLayout.CENTER);
	
		// listener
		for (JButton btn : new JButton[] { bt_user_list, bt_approval_list} ) {
			btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {btn.setForeground(Config.DARK_GREEN);}
				public void mouseExited(MouseEvent e) {btn.setForeground(Color.BLACK);}
				public void mouseClicked(MouseEvent e) {
				      JButton source = (JButton) e.getSource();
				      
				      if (source == bt_user_list) {
				    	  showPanel(ManageConfig.USER_LIST_KEY);
				    	  currentKey = ManageConfig.USER_LIST_KEY;
				      }
				      else if (source == bt_approval_list) {
				    	  showPanel(ManageConfig.APPROVAL_LIST_KEY);
				    	  currentKey = ManageConfig.APPROVAL_LIST_KEY;
				      }
				}
			});
		}
	
		createPanel();
	}
	
	// 카드레이아웃인 p_content에 패널 담아두기 
	public void createPanel() {
		p_content.add(userListPanel, ManageConfig.USER_LIST_KEY);
		p_content.add(userDetailPanel, ManageConfig.USER_DETAIL_KEY);
		p_content.add(approvalListPanel, ManageConfig.APPROVAL_LIST_KEY);
		p_content.add(approvalDetailPanel, ManageConfig.APPROVAL_DETAIL_KEY);
	}
	
	public void showPanel(String key) {
		previousKey = currentKey;
		currentKey = key;
		cardLayout.show(p_content, key);
		p_content.revalidate();  // 레이아웃 다시 계산
		p_content.repaint();  
	}
	
	// 보여줄 사용자로 userDetailPanel 세팅
	public void showUserDetailPanel(Member member) {
		userDetailPanel.setMember(member); 
		showPanel(ManageConfig.USER_DETAIL_KEY);
	}
	
	public void showApprovalDetailPanel(Bound bound) {
		approvalDetailPanel.setBound(bound);
		showPanel(ManageConfig.APPROVAL_DETAIL_KEY);
	}
	
	 public void back() {
        if (previousKey != null) {
	        showPanel(previousKey);
        }
	 }
	 
	 

}
