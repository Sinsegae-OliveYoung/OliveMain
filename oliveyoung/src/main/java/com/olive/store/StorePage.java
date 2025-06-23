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
import com.olive.common.repository.BranchDAO;
import com.olive.common.repository.UserDAO;
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
	int index; // 선택된 메뉴를 가르킬 변수
	MainLayout mainLayout;
	int roleId; // 로그인 한 유저의 아이디
	List<Branch> branches; // 모든 지점 리스트
	List<Branch> userBranches = new ArrayList(); // 로그인 한 유저의 지점 리스트
	BranchDAO branchDAO = new BranchDAO();

	public StorePage(MainLayout mainLayout) {
		super(mainLayout);
		this.mainLayout = mainLayout;
		this.roleId = mainLayout.user.getRole().getRole_id();

		// create
		p_side = new JPanel();

		mn_store_config = new JButton("지점 관리"); // 1 허용

		mn_report = new JLabel("보고서");
		mn_report_total = new JButton("  기간별 총 매출"); // 1 - 모든 지점 / 2 - 본인 지점
		mn_report_product = new JButton("  상품별 매출"); // 1,2 허용
		mn_report_store = new JButton("  지점별 매출"); // 1 허용

		p_content = new JPanel();

		branchDAO = new BranchDAO();

		// style
		setLayout(new BorderLayout());
		setBackground(Config.LIGHT_GRAY);
		setPreferredSize(new Dimension(Config.LAYOUT_W, Config.CONTENT_H));

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
			setButtonStyle(btn); // 스타일 적용
			mn_store_config.setFont(new Font("Noto Sans KR", Font.BOLD, 18)); // 크기가 달라 따로 스타일 지정
			btn.addActionListener(e -> {
				JButton source = (JButton) e.getSource(); // 클릭된 버튼 변수 선언
				if (source == mn_store_config && roleId == 1)
					showPanel(0);
				else if (source == mn_report_total)
					showPanel(index);
				else if (source == mn_report_product)
					showPanel(index + 1);
				else if (source == mn_report_store && roleId == 1)
					showPanel(index + 2);
				else
					JOptionPane.showMessageDialog(StorePage.this, "권한이 없습니다");
			});
		}

		// 메뉴 (재)생성하기
		createMenus();
	}

	// 메뉴(버튼)를 생성/재생성 하는 메서드
	public void createMenus() {
		branches = new BranchDAO().selectBranch(); // DB에서 모든 지점 목록 가져오기
		userBranches = branchDAO.getBranchList(mainLayout.user.getUser_id()); // 유저의 지점 목록 가져오기
		
		// 이전 메뉴들 초기화
		p_side.removeAll(); // 패널에서 요소 삭제
		storeBranchButtons.clear(); // 리스트에서 삭제

		// 새롭게 메뉴 붙이기
		// 패널에 첫번째 메뉴 추가
		p_side.add(Box.createVerticalStrut(25));
		p_side.add(mn_store_config);

		index = 1; 	// panels[0]은 보고서라 1부터 시작하여 지점 메뉴들 담기
		for (Branch allBranch : branches) { // 모든 지점 리스트만큼 반복
			JButton branchBtn = new JButton("  " + allBranch.getBr_name()); // 메뉴 버튼 생성
			setButtonStyle(branchBtn); // 메뉴 버튼 스타일

			final int panelIndex = index++;	// 버튼 하나당 소유할 패널값 선언
			branchBtn.addActionListener(e -> {
				if (userBranches.contains(allBranch)) // 유저의 지점 중 선택된 지점이 포함된다면
					showPanel(panelIndex); // 해당 패널로 이동
				else
					JOptionPane.showMessageDialog(StorePage.this, "권한이 없습니다"); // 아닐 시 거부
			});
			storeBranchButtons.add(branchBtn); // 지점 버튼 리스트에 추가

			// 패널에 지점 메뉴 추가
			p_side.add(Box.createVerticalStrut(10));
			p_side.add(branchBtn);
		}

		// 패널에 나머지 메뉴 추가
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

	// 버튼 스타일 설정하는 메서드
	private void setButtonStyle(JButton btn) {
		btn.setFont(new Font("Noto Sans KR", Font.BOLD, 14));
		btn.setBackground(Config.LIGHT_GRAY);
		btn.setFocusPainted(false);
		btn.setBorder(null);

		btn.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				btn.setForeground(Config.DARK_GREEN);
			}

			public void mouseExited(MouseEvent e) {
				btn.setForeground(Color.BLACK);
			}
		});
	}

	// 메뉴에 해당되는 각 패널(화면) 생성
	public void createPanels(List<Branch> branches) {
		panels = new Panel[1 + branches.size() + 3]; // 지점 관리 + (변동되는) 지점 수 + 3개 보고서 패널 = 총 크기

		panels[0] = new StoreConfigMenu(mainLayout, this); // 지점 관리 패널 부착

		int n = 1; // 이미 등록된 메뉴 이후 인덱스부터 등록
		for (Branch branch : branches) {
			panels[n++] = new StoresMenu(mainLayout, branch.getBr_name());
		}

		// 각 보고서 메뉴 부착
		panels[n++] = new ReportTotalMenu(mainLayout);
		panels[n++] = new ReportProductMenu(mainLayout);
		panels[n++] = new ReportStoreMenu(mainLayout);

		p_content.removeAll(); // 이전에 부착된 패널들 초기화
		for (Panel panel : panels)
			p_content.add(panel); // 새롭게 생성한 패널들 부착
	}

	public void showPanel(int target) {
		for (int i = 0; i < panels.length; i++)
			panels[i].setVisible((i == target) ? true : false);
	}

}
