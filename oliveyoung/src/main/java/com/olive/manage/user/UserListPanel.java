package com.olive.manage.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import com.olive.common.util.DateUtil;
import com.olive.common.util.ImageUtil;
import com.olive.common.util.style.ComboBoxUtil;
import com.olive.common.util.style.TableUtil;
import com.olive.mainlayout.MainLayout;
import com.olive.manage.BasePanel;
import com.olive.manage.DatePickerPanel;
import com.olive.manage.ManageConfig;
import com.olive.manage.ManagePage;

public class UserListPanel extends BasePanel{
	
	ImageUtil imgUtil = new ImageUtil();
	
	MemberDAO memberDAO = new MemberDAO();
	BranchDAO branchDAO = new BranchDAO();
	RoleDAO roleDAO = new RoleDAO();
	MemberFilterDTO filter;
	Member selectedMember; 
	
	//타이틀 아래 영역 
	JPanel p_content;
	
	//필터: 입사일, 지점, 직급, 이름
	JPanel p_filter;
	JLabel lb_filter;
	DatePickerPanel p_startdate;
	DatePickerPanel p_enddate;
	JComboBox<Branch> cb_branch;  
	JComboBox<Role> cb_role;
	JTextField t_name;
	JButton bt_search;
	
	//센터 : 테이블
	JPanel p_center; 
	JTable table;
	JScrollPane scroll;
	MemberModel memberModel;
	
	
	// 하단 페이지 번호 영역 
	JPanel p_south;   //페이징 
	JButton bt_prev;  //이전 페이지 
	JButton bt_next;   //다음 페이지 
	
	List<JButton> bt_list = new ArrayList<>();
	int currentPage = 1;  
	int pageSize = 3;
	int totalPageSize = 0;

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

	@Override
	public JPanel createContent() {
		p_content = new JPanel(new BorderLayout());
		
		//필터 패널 (north)
		p_filter = new JPanel();
		p_content.add(p_filter, BorderLayout.NORTH);
		
		lb_filter = new JLabel("필터");
		p_filter.add(lb_filter);    
		
		p_startdate = new DatePickerPanel("yyyy.mm.dd");
		p_filter.add(p_startdate);
		
		LocalDate ld = LocalDate.now();
		String formattedMonth = String.format("%02d", ld.getMonthValue());  //0붙여서 나오기   
		String formattedDay = String.format("%02d", ld.getDayOfMonth());  
		String today = ld.getYear() + "." + formattedMonth + "." + formattedDay;
		//수정 필요 : 오늘 날짜인데 숫자가 10 이하이면 0붙이기, yyyy.mm.dd 형식
		p_enddate = new DatePickerPanel(today);  // 오늘날짜로 지정 
		p_filter.add(p_enddate);
		
		cb_branch= ComboBoxUtil.createBranchComboBox();
		cb_branch.setPreferredSize(new Dimension(100, 30));
		p_filter.add(cb_branch);
		
		cb_role = ComboBoxUtil.createRoleComboBox();
		cb_role.setPreferredSize(new Dimension(100, 30));
		p_filter.add(cb_role);
		
		t_name = new JTextField("이름");
		t_name.setPreferredSize(new Dimension(100, 30));
		p_filter.add(t_name);
		
		bt_search = new JButton("검색");
		bt_search.setPreferredSize(new Dimension(60, 30));
		p_filter.add(bt_search);
		
		
		//테이블 패널 (center)
		p_center = new JPanel(new BorderLayout());
		p_content.add(p_center, BorderLayout.CENTER);
		
		//테이블 
		filter = new MemberFilterDTO();
		filter.setUser_id(mainLayout.user.getUser_id());
		memberModel = new MemberModel(filter, currentPage, pageSize);
		table = new JTable(memberModel);
		TableUtil.applyStyle(table);
		
		scroll = new JScrollPane(table);
		scroll.getViewport().setBackground(Color.WHITE);
		scroll.setPreferredSize(new Dimension(1000, 500));
		p_center.add(scroll);
		
		
		// 이벤트 연결 
		bt_search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setFilter();
				memberModel.list = memberDAO.select(filter, currentPage, pageSize);
				table.updateUI();
			}
		});
		
//		bt_next.addActionListener(e -> {
//			currentPage++;
//			bt_prev.setEnabled(true);
//			if(currentPage == totalPageSize) {
//				bt_next.setEnabled(false);
//			}
//			
//			if(currentPage % pageSize == 1) {
//				setPageButton();
//			}
//		});
//		
//		bt_prev.addActionListener(e -> {
//			currentPage--;
//			bt_next.setEnabled(true);
//			if(currentPage == 1) {
//				bt_prev.setEnabled(false);
//			}
//			
//			if(currentPage % pageSize == 0) {
//				setPageButton();
//			}
//		});
//		
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
		        selectedMember = memberModel.list.get(row);  
		        managePage.showUserDetailPanel(selectedMember);
		    }
		});
		
		
		return p_content;
	}
	
	public UserListPanel(MainLayout mainLayout, String title, ManagePage managePage) {
		super(mainLayout, ManageConfig.USER_LIST_TITLE, managePage);
		setButtonVisible(false);
		
		//페이징 
//		p_south = new JPanel();
//		bt_prev = new JButton("<");
//		bt_prev.setEnabled(false);
//		bt_next = new JButton(">");
//		
//		//스타일
//		
//		cb_role.setPreferredSize(new Dimension(100, 30));
//	     
//		p_south.setPreferredSize(new Dimension(800, 100));
//		p_south.setBackground(Color.red);
//		
//		// 조립 
//		p_south.add(bt_prev);
//		createPageButton();  // 5개 페이지 버튼에 대응되는 버튼 생성
//		setPageButton();
//		p_south.add(bt_next);
//		add(p_south, BorderLayout.SOUTH);
//		
	}
	
	public void setFilter() {
		filter.setBr_id(((Branch)cb_branch.getSelectedItem()).getBr_id());
		filter.setRole_id(((Role)cb_role.getSelectedItem()).getRole_id());
		filter.setUser_id(mainLayout.user.getUser_id());  
		filter.setUser_name(t_name.getText());
		
		if(!p_startdate.lb_date.getText().equals("yyyy.mm.dd")) {
			filter.setStart_date(DateUtil.stringToDate(p_startdate.lb_date.getText()));
		}
		filter.setEnd_date(DateUtil.stringToDate(p_enddate.lb_date.getText()));
	}
	
}