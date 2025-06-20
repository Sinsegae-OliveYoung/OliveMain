package com.olive.store;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.olive.common.config.Config;
import com.olive.common.model.Branch;
import com.olive.common.model.User;
import com.olive.common.repository.BranchDAO;
import com.olive.common.view.Page;
import com.olive.common.view.Panel;
import com.olive.mainlayout.MainLayout;
import com.olive.store.report.view.ReportProductMenu;
import com.olive.store.report.view.ReportStoreMenu;
import com.olive.store.report.view.ReportTotalMenu;
import com.olive.store.storeconfig.view.StoreConfigMenu;
import com.olive.store.stores.view.StoresMenu;

public class StorePage extends Page {

	JPanel p_side; // 사이드 바

	JButton mn_store_config; // 지점 관리

	JLabel mn_report; // 보고서
	JButton mn_report_total; // 기간별 총 매출
	JButton mn_report_product; // 상품별 매출
	JButton mn_report_store; // 지점별 매출

	JPanel p_content;

	private List<JButton> storeBranchButtons = new ArrayList<>(); // 메뉴들을 담을 배열

	Panel[] panels; // 하위 메뉴 패널들을 담을 배열
	int index;			// 선택된
	MainLayout mainLayout;
	
	List<String> brList = new ArrayList();
	int roleId;
	String brName;
	BranchDAO branchDAO = new BranchDAO();
	
	public StorePage(MainLayout mainLayout) {
		super(mainLayout);
		this.mainLayout = mainLayout;
		this.roleId = mainLayout.user.getRole().getRole_id();
		this.brName = (branchDAO.getBranchList(mainLayout.user.getUser_id())).get(0).getBr_name();
		setLayout(new BorderLayout());
		setBackground(Config.LIGHT_GRAY);
		
		// create
		p_side = new JPanel();

		mn_store_config = new JButton("지점 관리");			// 1 허용

		mn_report = new JLabel("보고서");							
		mn_report_total = new JButton("  기간별 총 매출");		// 1 - 모든 지점 / 2 - 본인 지점
		mn_report_product = new JButton("  상품별 매출");		// 1,2 허용
		mn_report_store = new JButton("  지점별 매출");		// 1 허용

		p_content = new JPanel();

		branchDAO = new BranchDAO();
		
		// style
		p_side.setBackground(Config.LIGHT_GRAY);
		p_side.setLayout(new BoxLayout(p_side, BoxLayout.Y_AXIS));
		p_side.setBorder(BorderFactory.createEmptyBorder(0, 27, 0, 0));
		p_side.setPreferredSize(new Dimension(Config.SIDE_W, Config.SIDE_H));

		mn_report.setFont(new Font("Noto Sans KR", Font.BOLD, 18));

		p_content.setBackground(Config.WHITE);
		p_content.setPreferredSize(new Dimension(Config.CONTENT_W, Config.CONTENT_H));

		storeBranchButtons.add(mn_store_config);
		storeBranchButtons.add(mn_report_total);
		storeBranchButtons.add(mn_report_product);
		storeBranchButtons.add(mn_report_store);

		// 고정된 메뉴들 listener, style
		for (JButton btn : storeBranchButtons) {
			setButtonStyle(btn);		// 스타일 적용
			mn_store_config.setFont(new Font("Noto Sans KR", Font.BOLD, 18));	// 크기가 달라 따로 스타일 지정
			btn.addActionListener(e->{
			      JButton source = (JButton) e.getSource();
			      if (source == mn_store_config && roleId == 1) showPanel(0);
			      else if (source == mn_report_total) showPanel(index);
			      else if (source == mn_report_product) showPanel(index+1);
			      else if (source == mn_report_store && roleId == 1) showPanel(index+2);
			      else JOptionPane.showMessageDialog(StorePage.this, "권한이 없습니다");
			      
			      
			});
		}

		// 메뉴 생성하기
		createMenus();

		setPreferredSize(new Dimension(Config.LAYOUT_W, Config.CONTENT_H));
	}

	// 메뉴 생성하는 메서드
	public void createMenus() {
		List<Branch> branches = new BranchDAO().selectBranch(); // DB에서 지점 목록 가져오기

		p_side.removeAll(); // 메뉴 패널 내 자식 요소들 삭제
		storeBranchButtons.clear(); // 모든 버튼 리스트에서 제거

		// 보고서 메뉴 assemble
		p_side.add(Box.createVerticalStrut(25));
		p_side.add(mn_store_config);

		// 지점 메뉴들 (재)생성 및 추가
		index = 1; // panels[0]은 보고서라 1부터 시작
		for (Branch branch : branches) {
			JButton branchBtn = new JButton("  " + branch.getBr_name());
			setButtonStyle(branchBtn);	// 스타일
			
			final int panelIndex = index++; // panel 배열의 인덱스 고정
			branchBtn.addActionListener(e -> {
				if (roleId == 1 && brName.equals(brList.get(panelIndex-1)) || (roleId == 2 && brName.equals(brList.get(panelIndex-1))))
					showPanel(panelIndex);
			      else JOptionPane.showMessageDialog(StorePage.this, "권한이 없습니다");
			});
			storeBranchButtons.add(branchBtn); // 리스트에 추가
			
			p_side.add(Box.createVerticalStrut(10));
			p_side.add(branchBtn);
		}

		p_side.add(Box.createVerticalStrut(25));
		p_side.add(mn_report);
		p_side.add(Box.createVerticalStrut(15));
		p_side.add(mn_report_total);
		p_side.add(Box.createVerticalStrut(10));
		p_side.add(mn_report_product);
		p_side.add(Box.createVerticalStrut(10));
		p_side.add(mn_report_store);
		add(p_side, BorderLayout.WEST);

		add(p_content, BorderLayout.CENTER);

		createPanels(branches);
		
		// 테이블 재생성
		revalidate();
		repaint();
	}

	// 버튼 스타일
	private void setButtonStyle(JButton btn) {
		btn.setFont(new Font("Noto Sans KR", Font.BOLD, 14));
		btn.setBackground(Config.LIGHT_GRAY);
		btn.setFocusPainted(false);
		btn.setBorder(null);

		btn.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) { btn.setForeground(Config.DARK_GREEN); }
			public void mouseExited(MouseEvent e) { btn.setForeground(Color.BLACK); }
		});
	}

	// 메뉴에 해당되는 각 패널 생성
	public void createPanels(List<Branch> branches) {
		panels = new Panel[1 + branches.size() + 3]; // 지점 관리 + 지점 수 + 3개 보고서 메뉴

		panels[0] = new StoreConfigMenu(mainLayout, this); // 지점 관리
		
		int n = 1;
		for (Branch branch : branches) {
			panels[n++] = new StoresMenu(mainLayout, branch.getBr_name());
			brList.add(branch.getBr_name());
		}
		
		// 각 보고서 메뉴
		panels[n++] = new ReportTotalMenu(mainLayout);
		panels[n++] = new ReportProductMenu(mainLayout);
		panels[n++] = new ReportStoreMenu(mainLayout);

		p_content.removeAll();		// p_content 자식요소 제거
		for (Panel panel : panels)
			p_content.add(panel);
	}

	public void showPanel(int target) {
		for (int i = 0; i < panels.length; i++)
			panels[i].setVisible((i == target) ? true : false);
	}

}