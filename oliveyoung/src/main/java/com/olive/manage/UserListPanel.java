package com.olive.manage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.olive.common.model.Branch;
import com.olive.common.model.Role;
import com.olive.common.model.User;
import com.olive.common.repository.BranchDAO;
import com.olive.common.repository.MemberDAO;
import com.olive.common.repository.RoleDAO;
import com.olive.common.util.ImageUtil;
import com.olive.mainlayout.MainLayout;

public class UserListPanel extends ManagePanel{
	
	MemberDAO memberDAO;
	BranchDAO branchDAO;
	RoleDAO roleDAO;
	MemberFilterDTO filter;
	
	JPanel p_north;
	
	JPanel p_start;
	JLabel lb_start;
	JButton bt_start;
	
	JPanel p_end;
	JLabel lb_end;
	JButton bt_end;
	
	
	
	JComboBox<Branch> cb_branch;   //지점 콤보박스
	JComboBox<Role> cb_role;
	JTextField t_name;
	JButton bt_search;
	
	
	JTable table;
	JScrollPane scroll;
	MemberModel memberModel;
	
	ImageUtil imgUtil = new ImageUtil();
	
	public User user = new User();  //임시 로그인 유저 
	
	public UserListPanel(MainLayout mainLayout) {
		super(mainLayout);
		this.setLayout(new BorderLayout());
		memberDAO = new MemberDAO();
		branchDAO = new BranchDAO();
		roleDAO = new RoleDAO();
		filter = new MemberFilterDTO();
		user.setUser_id(1);
		
		//생성
		p_north = new JPanel();
		
		p_start = new JPanel(new BorderLayout());
		lb_start = new JLabel("yyyy.mm.dd");
		Image img = imgUtil.getImage("images/calendar_icon.png", 20, 20);
		bt_start = new JButton(new ImageIcon(img));
		bt_start.setBorderPainted(false);       // 테두리 없애기
		bt_start.setContentAreaFilled(false);   // 배경 채우기 제거
		//bt_cal.setFocusPainted(false);        // 포커스 테두리 제거
		bt_start.setOpaque(false);              // 불투명 해제 (배경 투명화)
		
		p_end = new JPanel(new BorderLayout());
		LocalDate ld = LocalDate.now();
		
		
		String formattedMonth = String.format("%02d", ld.getMonthValue());  //0붙여서 나오기   
		String formattedDay = String.format("%02d", ld.getDayOfMonth());  
		
		String today = ld.getYear() + "." + formattedMonth + "." + formattedDay;
		lb_end = new JLabel(today);  // 오늘 날짜 바로 나오게 
		
		Image img2 = imgUtil.getImage("images/calendar_icon.png", 20, 20);
		bt_end = new JButton(new ImageIcon(img2));
		bt_end.setBorderPainted(false);       // 테두리 없애기
		bt_end.setContentAreaFilled(false);   // 배경 채우기 제거
		//bt_cal.setFocusPainted(false);        // 포커스 테두리 제거
		bt_end.setOpaque(false); 
		
		
		cb_branch = new JComboBox<>();
		cb_role = new JComboBox<>();
		t_name = new JTextField("이름");
		bt_search = new JButton("검색");
		
		
		table = new JTable(memberModel = new MemberModel(user));
		scroll = new JScrollPane(table);
		
		
		//스타일
		p_start.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		p_end.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		cb_branch.setPreferredSize(new Dimension(100, 30));
		cb_role.setPreferredSize(new Dimension(100, 30));
		t_name.setPreferredSize(new Dimension(100, 30));
		bt_search.setPreferredSize(new Dimension(60, 30));
		
		lb_start.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // top, left, bottom, right
		lb_end.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // top, left, bottom, right
		//bt_cal.setPreferredSize(new Dimension(30, 30));
		bt_start.setHorizontalAlignment(SwingConstants.RIGHT);
		//bt_cal.setIconTextGap(5); // 텍스트와 아이콘 사이 간격
		
		// 조립 
		p_north.add(p_start);
		p_start.add(lb_start, BorderLayout.WEST);
		p_start.add(bt_start, BorderLayout.EAST);
		
		p_north.add(p_end);
		p_end.add(lb_end, BorderLayout.WEST);
		p_end.add(bt_end, BorderLayout.EAST);
		
		
		p_north.add(cb_branch);
		p_north.add(cb_role);
		p_north.add(t_name);
		p_north.add(bt_search);
		add(p_north, BorderLayout.NORTH);
		add(scroll);
		
		getBranch();
		getRole();
		
		
		// 이벤트 연결 
		bt_start.addActionListener(e -> {
			new DatePicker(lb_start);
		});
		
		bt_end.addActionListener(e -> {
			new DatePicker(lb_end);
		});
		
		bt_search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				filter.setBr_id(((Branch)cb_branch.getSelectedItem()).getBr_id());
				filter.setRole_id(((Role)cb_role.getSelectedItem()).getRole_id());
				filter.setUser_id(1);  //수정 필요
				filter.setUser_name(t_name.getText());
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
				if(!lb_start.getText().equals("yyyy.mm.dd")) {
					filter.setStart_date(Date.valueOf(LocalDate.parse(lb_start.getText(), formatter))); 
				}
				
				if(!lb_end.getText().equals("yyyy.mm.dd")) {
					filter.setEnd_date(Date.valueOf(LocalDate.parse(lb_end.getText(), formatter))); 
				}
				
				memberModel.list = memberDAO.select(filter);
				memberModel.fireTableDataChanged();
			}
		});
		
		//이름 textfield의 placeholder 제거 이벤트 
		t_name.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (t_name.getText().isEmpty()) {
					t_name.setText("이름");
		        }
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if (t_name.getText().equals("이름")) {
		            t_name.setText("");
		        }
			}
		});
		
		
	}
	
	// 지점 목록을 콤보박스에 넣기 
	public void getBranch() {
		System.out.println("UserListPanel.getBranch()");
		List<Branch> br_list = branchDAO.getBranchList(1);   //수정 필요
		Branch dummy = new Branch();
		dummy.setBr_id(0);
		dummy.setBr_name("지점");
		//---- dummy에도 전체 값 세팅해야하는지 
		
		cb_branch.addItem(dummy);
		for(Branch br : br_list) {
			cb_branch.addItem(br);
		}
	}
	
	public void getRole() {
		List<Role> role_list = roleDAO.selectAll();
		
		Role dummy = new Role();
		dummy.setRole_id(0);
		dummy.setRole_name("직급");
		//--전체 값 세팅?
		
		cb_role.addItem(dummy);
		for(Role role : role_list) {
			cb_role.addItem(role);
		}
	}
	
	
}
