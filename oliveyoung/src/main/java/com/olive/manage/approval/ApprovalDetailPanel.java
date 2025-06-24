package com.olive.manage.approval;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Date;
import java.time.LocalDate;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.olive.common.config.Config;
import com.olive.common.model.Bound;
import com.olive.common.model.BoundProduct;
import com.olive.common.model.BoundState;
import com.olive.common.model.Stock;
import com.olive.common.model.User;
import com.olive.common.repository.BoundDAO;
import com.olive.common.repository.BoundStateDAO;
import com.olive.common.repository.StockDAO;
import com.olive.common.util.style.ButtonUtil;
import com.olive.common.util.style.TableUtil;
import com.olive.mainlayout.MainLayout;
import com.olive.manage.BasePanel;
import com.olive.manage.ManageConfig;
import com.olive.manage.ManagePage;

public class ApprovalDetailPanel extends BasePanel {
	JPanel p_content; // boxlayout

	JLabel lb_boundId;
	JLabel lb_requester;
	JLabel lb_requestDate;
	JLabel lb_status; 
	
	ApprovalDetailModel model;
	JTable table;
	JScrollPane scroll;
	
	JTextArea ta_memo;
	JPanel p_button;
	JButton bt_approve;
	JButton bt_reject;

	JPanel p_confirmed;
	JButton bt_print;
	JLabel lb_confirm;
	JLabel lb_approver;
	JLabel lb_approveDate;

	BoundDAO boundDAO = new BoundDAO();
	BoundStateDAO boundStateDAO = new BoundStateDAO();
	StockDAO stockDAO = new StockDAO();
	Bound bound;

	public ApprovalDetailPanel(MainLayout mainLayout, String title, ManagePage managePage) {
		super(mainLayout, title, managePage);

	}

	@Override
	public JPanel createContent() {
		p_content = new JPanel();
		p_content.setLayout(new BoxLayout(p_content, BoxLayout.Y_AXIS));

		// 기본 정보
		JPanel p_north = new JPanel(new FlowLayout(FlowLayout.LEFT));

		p_north.setBorder(new EmptyBorder(20, 20, 0, 0)); // top, left, bottom, right
		p_north.setPreferredSize(new Dimension(800, 100));
		p_north.setBackground(Color.WHITE);
		p_content.add(p_north);

		Dimension d = new Dimension(800, 30);
		Font f = new Font("Noto Sans KR", Font.BOLD, 14);
		lb_boundId = new JLabel("요청서 번호:   ");
		lb_boundId.setFont(f);
		lb_boundId.setPreferredSize(d);
		p_north.add(lb_boundId);

		lb_requester = new JLabel("요청자:        ");
		lb_requester.setFont(f);
		lb_requester.setPreferredSize(d);
		p_north.add(lb_requester);

		lb_requestDate = new JLabel("요청일:    ");
		lb_requestDate.setFont(f);
		lb_requestDate.setPreferredSize(d);
		p_north.add(lb_requestDate);

		lb_status = new JLabel("요청 상태:   ");
		lb_status.setFont(f);
		lb_status.setPreferredSize(d);
		p_north.add(lb_status);

		// 품목 라벨 + 테이블
		JPanel p_center = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p_center.setBorder(new EmptyBorder(0, 20, 0, 0)); // top, left, bottom, right
		p_center.setPreferredSize(new Dimension(1000, 230));
		p_center.setBackground(Color.white);
		p_content.add(p_center);

		JLabel lb_items = new JLabel("품목");
		lb_items.setFont(ManageConfig.BOLD_FONT);
		lb_items.setPreferredSize(d);
		p_center.add(lb_items);
		
		model = new ApprovalDetailModel(1);  // 임시 bound

		table = new JTable(model);
		
		TableUtil.applyStyle(table);

		scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(1000, 250));
		scroll.getViewport().setBackground(Color.WHITE); // 뷰포트 배경
		scroll.setBackground(Color.white);

		p_center.add(scroll);

		// 메모 + 승인/반려 버튼
		JPanel p_south = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p_south.setBorder(new EmptyBorder(0, 20, 0, 0)); // top, left, bottom, right
		p_south.setBackground(Color.white);
		p_content.add(p_south);

		JLabel lb_memo = new JLabel("비고");
		lb_memo.setFont(ManageConfig.BOLD_FONT);
		lb_memo.setPreferredSize(d);
		p_south.add(lb_memo);

		ta_memo = new JTextArea(3, 90);
		ta_memo.setBackground(Config.LIGHT_GREEN);
		ta_memo.setMargin(new Insets(5, 5, 5, 5)); // top, left, bottom, right
		ta_memo.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 글꼴, 스타일, 크기
		ta_memo.setEditable(false);
		p_south.add(ta_memo);

		p_button = new JPanel();
		p_button.setBackground(Color.white);
		bt_approve = ButtonUtil.greenButtonUtil("승인");
		p_button.add(bt_approve);
		bt_reject = ButtonUtil.pinkButtonUtil("반려");
		p_button.add(bt_reject);
		p_content.add(p_button);
		p_button.setVisible(false);

		Dimension d2 = new Dimension(200, 30);
		//p_confirmed.setBackground(Color.white);
		p_confirmed = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p_confirmed.setBorder(new EmptyBorder(0, 0, 0, 50)); // top, left, bottom, right

		bt_print = ButtonUtil.greenButtonUtil("출력하기");
		p_confirmed.add(bt_print);
		p_confirmed.add(Box.createHorizontalStrut(500));

		lb_approver = new JLabel("결재자: ");
		lb_approver.setFont(f);
		lb_approver.setPreferredSize(d2);
		p_confirmed.add(lb_approver);

		lb_approveDate = new JLabel("결재일: ");
		p_confirmed.add(lb_approveDate);
		lb_approveDate.setFont(f);
		lb_approveDate.setPreferredSize(d2);

		lb_confirm = new JLabel("ㅇㅇ됨") {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();

				// 배경 색
				g2.setColor(Color.yellow);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

				// 텍스트 포함 기본 동작 유지
				super.paintComponent(g);
				g2.dispose();
			}
		};
//		lb_confirm.setPreferredSize(new Dimension(70, 70));
//		lb_confirm.setHorizontalAlignment(SwingConstants.CENTER);
//
//		lb_confirm.setOpaque(true);
//		lb_confirm.setBackground(Color.yellow);
		p_confirmed.add(lb_confirm);
		p_content.add(p_confirmed);

		// 이벤트 연결
		bt_approve.addActionListener(e -> {
			int result = JOptionPane.showConfirmDialog(this, "승인하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				JOptionPane.showMessageDialog(this, "승인이 완료되었습니다.");
				// 승인 업데이트 -> 수량에 바로 반영
				BoundState bs;
				if (bound.getBound_flag().equals("in")) {
					bs = boundStateDAO.select(2);
				} else {
					bs = boundStateDAO.select(3);
				}
				confirm(bs);
				setBound(bound);
				boundDAO.update(bound);
				
				
				// 재고 테이블에 반영 
				for(int i = 0; i < model.list.size(); i++) {
					// 품목 하나하나 재고 테이블에 반영 
					BoundProduct bp = model.list.get(i);
					// 해당 품목의 option_id, 기존 재고 가져오기,  st_quantity 
					// bound state id == in 이면 더하기, out이면 빼기
					
					Stock stock = stockDAO.select(bp.getProductOption().getOption_id(), bound.getBranch().getBr_id());
					
					if(bound.getBound_flag().equals("in")) {
						stockDAO.updateProductQuantity(stock.getSt_id(), stock.getSt_quantity() + bp.getB_count());
					} else {
						stockDAO.updateProductQuantity(stock.getSt_id(), stock.getSt_quantity() - bp.getB_count());
					}
					
				}
			}
		});

		bt_reject.addActionListener(e -> {

			int result = JOptionPane.showConfirmDialog(this, "요청을 반려 처리 하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				String reason = JOptionPane.showInputDialog(this, "사유를 입력하세요.");
				if (reason != null) {
					JOptionPane.showMessageDialog(this, "처리가 완료되었습니다.");
					// 반려 처리 db 업데이트
					BoundState bs = boundStateDAO.select(4);
					ta_memo.append("\n반려 사유: " + reason);
					confirm(bs);
					setBound(bound);
					boundDAO.update(bound);
				}
			}
		});

		// PDF 출력 버튼 리스너
		bt_print.addActionListener(e -> {
			long currentTime = System.currentTimeMillis();
			Date now = new Date(currentTime);
			
			// 인쇄 작업 시 인쇄 목록에 표시될 제목
			PrinterJob print = PrinterJob.getPrinterJob();
			print.setJobName("결제-상세-정보_" + now);

			// 페이지 포맷 설정 (세로)
			PageFormat pageFormat = print.defaultPage();
			pageFormat.setOrientation(PageFormat.PORTRAIT);
			
			print.setPrintable(new Printable() {
				public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
					if (pageIndex > 0)		// 0번째 페이지만 출력함 (한 페이지)
						return Printable.NO_SUCH_PAGE;

					Graphics2D graphic = (Graphics2D) graphics;
					graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					graphic.translate(pageFormat.getImageableX(), pageFormat.getImageableY());		// 이미지 출력 가능 영역으로 이동

					// 패널 크기 - 출력 영역 크기 계산
		            // 패널 크기와 출력 영역 크기 계산
		            double panelW = ApprovalDetailPanel.this.getWidth();
		            double panelH = ApprovalDetailPanel.this.getHeight();

		            double pageW = pageFormat.getImageableWidth();
		            double pageH = pageFormat.getImageableHeight();

		            // 너비, 높이 비율 계산하여 비율 중 더 작은 값을 사용 (전체가 잘리지 않도록)
		            double scale = Math.min(pageW / panelW, pageH / panelH);

		            // 스케일 적용
		            graphic.scale(scale, scale);

		            // 전체 컴포넌트 출력
					ApprovalDetailPanel.this.print(graphic);

					return Printable.PAGE_EXISTS;
				}
			});
			
			if(print.printDialog()) {
				try {
					print.print();
				} catch (PrinterException ex) {
					throw new RuntimeException(ex);
				}
			}
		});

		return p_content;
	}

	public void confirm(BoundState bs) {
		bound.setBoundState(bs);
		bound.setApprove_date(Date.valueOf(LocalDate.now()));
		User approver = new User();
		approver.setUser_id(mainLayout.user.getUser_id());
		bound.setApprover(approver);
		bound.setComment(ta_memo.getText());
	}

	public void setBound(Bound bound) {
		this.bound = bound;

		// 라벨 텍스트 업데이트
		lb_boundId.setText("요청서 번호          " + bound.getBound_id());
		lb_requester.setText("요청자                " + bound.getUser().getUser_name());
		lb_requestDate.setText("요청일                " + bound.getRequest_date().toString());
		lb_status.setText("요청 상태            " + bound.getBoundState().getBo_state_name());
		// 테이블 모델 갱신
		model = new ApprovalDetailModel(bound.getBound_id());
		table.setModel(model); // 모델만 교체

		if (bound.getBoundState().getBo_state_id() == 1) {
			p_confirmed.setVisible(false);
			p_button.setVisible(true);
		} else {
			p_confirmed.setVisible(true);
			p_button.setVisible(false);
		}

		lb_approveDate.setText("결재일: " + LocalDate.now().toString());
		lb_approver.setText("결재자: " + mainLayout.user.getUser_name());
		ta_memo.setText(bound.getComment());
		if (bound.getBoundState().getBo_state_id() == 4) {
			lb_confirm.setText("반려됨");
		} else {
			lb_confirm.setText("승인됨");
		}
	}

}
