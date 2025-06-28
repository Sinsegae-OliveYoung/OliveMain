package com.olive.manage.approval;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.olive.common.config.Config;
import com.olive.common.model.Bound;
import com.olive.common.model.BoundState;
import com.olive.common.model.Branch;
import com.olive.common.repository.BoundDAO;
import com.olive.common.util.DateUtil;
import com.olive.common.util.style.ButtonUtil;
import com.olive.common.util.style.ComboBoxUtil;
import com.olive.common.util.style.TableUtil;
import com.olive.mainlayout.MainLayout;
import com.olive.manage.BasePanel;
import com.olive.manage.DatePickerPanel;
import com.olive.manage.ManagePage;
import com.olive.manage.user.MemberModel;

public class ApprovalListPanel extends BasePanel{
	
	// 타이틀 아래 영역 
	JPanel p_content;
	
	// 필터: 요청자, 요청일, 상태 
	JPanel p_filter;
	JTextField t_submitter;
	DatePickerPanel p_startdate;
	DatePickerPanel p_enddate;
	JComboBox<BoundState> cb_status;
	JComboBox<Branch> cb_branch;
	JButton bt_search;
	
	// 센터 : 테이블 
	JPanel p_center;
	JScrollPane scroll;
	JTableHeader header;
	JTable table;
	
	JPanel p_south;
	
	public ApprovalModel model;
	
	BoundFilterDTO filter;
	BoundDAO boundDAO = new BoundDAO();
	Bound selectedBound;
	
	public ApprovalListPanel(MainLayout mainLayout, String title, ManagePage managePage) {
		super(mainLayout, title, managePage);
		super.setButtonVisible(false);
	}
	
	@Override
	public JPanel createContent() {
		p_content = new JPanel(new BorderLayout());
		
		// 필터 패널 (north)
		p_filter = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));  //수평, 수직간격
		p_filter.setBackground(Color.white);
		p_filter.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // 상좌하우 마진
		p_content.add(p_filter, BorderLayout.NORTH);
		
		p_filter.add(new JLabel("필터"));    //필터 라벨 추가(단순 텍스트라 멤버변수 X)
		
		p_startdate = new DatePickerPanel("yyyy.mm.dd");
		p_filter.add(p_startdate);
		
		LocalDate ld = LocalDate.now();
		String formattedMonth = String.format("%02d", ld.getMonthValue());  //0붙여서 나오기   
		String formattedDay = String.format("%02d", ld.getDayOfMonth());  
		String today = ld.getYear() + "." + formattedMonth + "." + formattedDay;
		p_enddate = new DatePickerPanel(today);   
		p_filter.add(p_enddate);
		
		cb_branch = ComboBoxUtil.createBranchComboBox(mainLayout.user.getUser_id());
		cb_branch.setPreferredSize(new Dimension(100, 30));
		p_filter.add(cb_branch);
		cb_status = ComboBoxUtil.createBoundStateComboBox();
		cb_status.setPreferredSize(new Dimension(100, 30));
		p_filter.add(cb_status);
		
		
		t_submitter = new JTextField("이름");
		t_submitter.setPreferredSize(new Dimension(100, 30));
		p_filter.add(t_submitter);
		
		bt_search = ButtonUtil.greenButtonUtil("검색");
		bt_search.setPreferredSize(new Dimension(200, 30));
		
		p_filter.add(bt_search);
		
		

		
		//센터 패널 (center)
		p_center = new JPanel();    
		p_center.setBackground(Config.WHITE);
		p_content.add(p_center, BorderLayout.CENTER);
		
		//테이블 
		filter = new BoundFilterDTO();
		filter.setUser_id(mainLayout.user.getUser_id());
		model = new ApprovalModel(filter);
		table = new JTable(model);
		TableUtil.applyStyle(table);
		scroll = new JScrollPane(table);
		scroll.getViewport().setBackground(Color.white);
		scroll.setPreferredSize(new Dimension(1000, 550));
		p_center.add(scroll);
		
		// 페이징 패널 (south)  구현할지 말지.?
		
		// 테이블 헤더 컬럼 정렬
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);
		
		header = table.getTableHeader();
		header.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int columnIndex = header.columnAtPoint(e.getPoint());
		        String columnName = table.getColumnName(columnIndex);
		        System.out.println("헤더 클릭됨: " + columnName + " (인덱스: " + columnIndex + ")");
		    }

		    private SortOrder getSortOrder(TableRowSorter<?> sorter, int columnIndex) {
		        List<? extends RowSorter.SortKey> sortKeys = sorter.getSortKeys();
		        for (RowSorter.SortKey key : sortKeys) {
		            if (key.getColumn() == columnIndex) {
		                return key.getSortOrder();
		            }
		        }
		        return SortOrder.UNSORTED;
		    }
		});
		
		//요청자 필드 포커스 이벤트
		t_submitter.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (t_submitter.getText().isEmpty()) {
					t_submitter.setText("이름");
		        }
			}
			@Override
			public void focusGained(FocusEvent e) {
				if (t_submitter.getText().equals("이름")) {
					t_submitter.setText("");
		        }
			}
		});
		
		bt_search.addActionListener(e -> {
			setFilter();
			model.list = boundDAO.select(filter);
			model.fireTableDataChanged();
		});
		
		table.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int row = table.getSelectedRow();  // 클릭된 row index
		        
		        if (row >= 0) {
					int modelRow = table.convertRowIndexToModel(row);  // 실제 모델 인덱스

		            // 모델에서 정확한 데이터 가져오기
					model = (ApprovalModel) table.getModel();

		            // 모델에서 사용자 정보 추출
					selectedBound = model.list.get(modelRow);  
					managePage.showApprovalDetailPanel(selectedBound);
				}

//		        // 모델에서 사용자 정보 추출
//		        selectedBound = model.list.get(row);
//		        managePage.showApprovalDetailPanel(selectedBound);
		    }
		});
		return p_content;
	}	
	
	public void setFilter() {
		filter.setBoundstate_id(((BoundState)cb_status.getSelectedItem()).getBo_state_id());
		if(!p_startdate.lb_date.getText().equals("yyyy.mm.dd")) {
			filter.setStart_date(DateUtil.stringToDate(p_startdate.lb_date.getText()));
		}
		filter.setEnd_date(DateUtil.stringToDate(p_enddate.lb_date.getText()));
		filter.setBr_id(0);  //수정 필요 
		filter.setSubmitter_name(t_submitter.getText());
	}
	
	public void clearFilter() {
		t_submitter.setText("이름");
		p_startdate.lb_date.setText("yyyy.mm.dd");
		
		LocalDate ld = LocalDate.now();
		String formattedMonth = String.format("%02d", ld.getMonthValue());  //0붙여서 나오기   
		String formattedDay = String.format("%02d", ld.getDayOfMonth());  
		String today = ld.getYear() + "." + formattedMonth + "." + formattedDay;
		p_enddate.lb_date.setText(today);
		cb_status.setSelectedIndex(0);		
		cb_branch.setSelectedIndex(0);		
	}
	
	public void refresh() {
		clearFilter();
		model.list = boundDAO.select(filter);
		model.fireTableDataChanged();
	}
}
