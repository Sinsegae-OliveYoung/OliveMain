package com.olive.manage.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import javax.swing.table.DefaultTableCellRenderer;

import com.olive.common.model.Branch;
import com.olive.common.model.Member;
import com.olive.common.model.Role;
import com.olive.common.repository.BranchDAO;
import com.olive.common.repository.MemberDAO;
import com.olive.common.repository.RoleDAO;
import com.olive.common.util.ImageUtil;
import com.olive.common.util.style.TableUtil;
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;
import com.olive.manage.DatePicker;
import com.olive.manage.ManagePage;

public class UserListPanel extends Panel{
	
	MemberDAO memberDAO;
	BranchDAO branchDAO;
	RoleDAO roleDAO;
	MemberFilterDTO filter;
	Member selectedMember; 
	
	
	JPanel p_north1;
	JLabel lb_menu;
	
	JPanel p_north;
	JLabel lb_filter;
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
	
	JPanel p_table; 
	JTable table;
	JScrollPane scroll;
	MemberModel memberModel;
	
	JPanel p_south;   //페이징 
	JButton bt_prev;  //이전 페이지 
	JButton bt_next;   //다음 페이지 
	
	List<JButton> bt_list = new ArrayList<>();
	int currentPage = 1;  
	int pageSize = 3;
	int totalPageSize = 0;

	ImageUtil imgUtil = new ImageUtil();
	ManagePage managePage;	
	
	public void createPageButton() {
		for(int i = 0; i < pageSize; i++) {
			bt_list.add(new JButton());
			p_south.add(bt_list.get(i));
		}
	}
	
	public void setTotalPageSize() {
		
		int count = memberDAO.countSelect(filter);
		totalPageSize = count / pageSize;
		if(count % pageSize != 0) totalPageSize++;
	}
	
	
	public void setPageRange() {
		 
	}
	
	public void setPageButton() {
		
		int start = ((currentPage - 1) / pageSize) * pageSize + 1;
		int end = Math.min(totalPageSize, (start + pageSize -1));
		System.out.println("start: " + start);
		System.out.println("end: " + end);
		
		for(int i = 0; i < bt_list.size(); i++) {
			JButton btn = bt_list.get(i);
			
			if(start + i <= end) {
				 btn.setText(String.valueOf(start + i));
				  btn.setVisible(true);
			} else {
				  btn.setVisible(false);
			}
		}
		
	}
	
	public UserListPanel(MainLayout mainLayout, ManagePage managePage) {
		super(mainLayout);
		this.managePage = managePage;
		
		memberDAO = new MemberDAO();
		branchDAO = new BranchDAO();
		roleDAO = new RoleDAO();
		filter = new MemberFilterDTO();
		
		filter.setUser_id(mainLayout.user.getUser_id());  
		setTotalPageSize();
		setStyle();
		
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
				setFilter();
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
				if(!lb_start.getText().equals("yyyy.mm.dd")) {
					filter.setStart_date(Date.valueOf(LocalDate.parse(lb_start.getText(), formatter))); 
				}
				
				if(!lb_end.getText().equals("yyyy.mm.dd")) {
					filter.setEnd_date(Date.valueOf(LocalDate.parse(lb_end.getText(), formatter))); 
				}
				
				memberModel.list = memberDAO.select(filter, currentPage, pageSize);
				table.updateUI();
			}
		});
		
		bt_next.addActionListener(e -> {
			currentPage++;
			bt_prev.setEnabled(true);
			if(currentPage == totalPageSize) {
				bt_next.setEnabled(false);
			}
			
			if(currentPage % pageSize == 1) {
				setPageButton();
			}
		});
		
		bt_prev.addActionListener(e -> {
			currentPage--;
			bt_next.setEnabled(true);
			if(currentPage == 1) {
				bt_prev.setEnabled(false);
			}
			
			if(currentPage % pageSize == 0) {
				setPageButton();
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
		
		table.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int row = table.getSelectedRow();  // 클릭된 row index

		        // 모델에서 사용자 정보 추출
		        selectedMember = memberModel.list.get(row);  // ← 너가 만든 MemberModel의 list 사용
		        managePage.showUserDetailPanel(selectedMember);
		    }
		});
		
	}
	
	public void setFilter() {
		filter.setBr_id(((Branch)cb_branch.getSelectedItem()).getBr_id());
		filter.setRole_id(((Role)cb_role.getSelectedItem()).getRole_id());
		filter.setUser_id(mainLayout.user.getUser_id());  
		filter.setUser_name(t_name.getText());
	}
	
	public void setStyle() {
		setLayout(new BorderLayout());
		
//		p_north1 = new JPanel();
//		p_north1.setPreferredSize(new Dimension(100, 100));
//		add(p_north1, BorderLayout.NORTH);
//		lb_menu = new JLabel("사용자 목록");
//	    lb_menu.setFont(new Font("SansSerif", Font.BOLD, 22));
//	    lb_menu.setHorizontalAlignment(SwingConstants.LEFT);
//	    p_north1.add(lb_menu);
	    
		p_north = new JPanel();
		lb_filter = new JLabel("필터");
		p_north.add(lb_filter);
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
		
		p_table = new JPanel();
	
		table = new JTable(memberModel = new MemberModel(filter, currentPage, pageSize));
		TableUtil.applyStyle(table);
	
	
		//셀 내용 가운데 정렬 
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        scroll = new JScrollPane(table);
		scroll.getViewport().setBackground(Color.WHITE);
		scroll.setPreferredSize(new Dimension(1000, 500));
		
		p_south = new JPanel();
		bt_prev = new JButton("<");
		bt_prev.setEnabled(false);
		bt_next = new JButton(">");
		
		
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
		
		
		p_south.setPreferredSize(new Dimension(800, 100));
		p_south.setBackground(Color.red);
		
		
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
		
		p_table.add(scroll);
		add(p_table);
		
		p_south.add(bt_prev);
		createPageButton();  // 5개 페이지 버튼에 대응되는 버튼 생성
		setPageButton();
		p_south.add(bt_next);
		add(p_south, BorderLayout.SOUTH);
		
	}
	
	
	// 지점, 직급 콤보박스 채우기 
	public void getBranch() {
		System.out.println("UserListPanel.getBranch()");
		List<Branch> br_list = branchDAO.getBranchList(mainLayout.user.getUser_id());  
		Branch dummy = new Branch();
		dummy.setBr_id(0);
		dummy.setBr_name("지점");
		
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
		
		cb_role.addItem(dummy);
		for(Role role : role_list) {
			cb_role.addItem(role);
		}
	}
	
	
	
	
	
}
