package com.olive.manage;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DatePicker extends JFrame{
	
	Container contentPane;
	JPanel p_north;   //   | <  year  month  > |
	JButton bt_next;
	JButton bt_prev;
	JComboBox<Integer> cb_year;
	JComboBox<Integer> cb_month;
	
	//더블클릭 이벤트 혹은 선택완료 버튼 만들어서 날짜 지정 
	
	JPanel p_day;
	JLabel[] lb_days;  
	String[] str = {"월", "화", "수", "목", "금", "토", "일"};
	
	JPanel p_date;
	JLabel[] lb_dates;
	
	LocalDate date = LocalDate.now();
	JLabel lb_selected = null;  // 클릭한 date 기억하기 위한 용도
	
	public DatePicker(JLabel lb) {
		contentPane = this.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		p_north = new JPanel();
		bt_prev = new JButton("<<");
		bt_next = new JButton(">>");
		cb_year = new JComboBox<>();
		cb_month = new JComboBox<>();
		
		p_day = new JPanel(new GridLayout(1, 7));
		lb_days = new JLabel[7];
		for(int i = 0; i < lb_days.length; i++) {
			lb_days[i] = new JLabel(str[i]);
			lb_days[i].setHorizontalAlignment(SwingConstants.CENTER);  //수평 중앙정렬
		}
		
		p_date = new JPanel(new GridLayout(6, 7));
		lb_dates = new JLabel[42];
		for(int i = 0; i < lb_dates.length; i++) {
			lb_dates[i] = new JLabel(Integer.toString(i));
			lb_dates[i].setOpaque(true);
			lb_dates[i].setBorder(BorderFactory.createLineBorder(Color.black));
			lb_dates[i].setHorizontalAlignment(SwingConstants.CENTER);   
		}
		
		
		// 스타일 
		Dimension d = new Dimension(500, 50);
		p_north.setPreferredSize(d);
		p_north.setMaximumSize(d);
		//p_north.setBackground(Color.pink);
		
		Dimension d2 = new Dimension(450, 50);
		p_day.setPreferredSize(d2);
		p_day.setMaximumSize(d2);
		p_day.setBackground(Color.orange);
		
		p_date.setPreferredSize(new Dimension(500, 300));
		p_date.setMaximumSize(new Dimension(450, 300));
		
		
		//조립
		contentPane.add(p_north);
		p_north.add(bt_prev);
		p_north.add(cb_year);
		p_north.add(cb_month);
		p_north.add(bt_next);
		
		contentPane.add(p_day);
		for(int i = 0; i < lb_days.length; i++) {
			p_day.add(lb_days[i]);
		}
		
		contentPane.add(p_date);
		for(int i = 0; i < lb_dates.length; i++) {
			p_date.add(lb_dates[i]);
		}
		
		//콤보박스 채우기
		getYearList();
		getMonthList();
		printNum();
		
		
		// 이벤트 연결
		bt_next.addActionListener(e -> {
			// 알림으로 띄우기 
			if(date.getYear() == LocalDate.now().getYear() && date.getMonthValue() == LocalDate.now().getMonthValue()) {
				JOptionPane.showMessageDialog(this, "현재 월까지만 조회할 수 있습니다.");
				return;
			}
			
			date = date.plusMonths(1);
			cb_month.setSelectedItem(date.getMonthValue());
			cb_year.setSelectedItem(date.getYear());
			printNum();
			if(lb_selected != null) {
				lb_selected.setBackground(Color.white);
			}
		});
		
		bt_prev.addActionListener(e -> {
			date = date.minusMonths(1);
			cb_month.setSelectedItem(date.getMonthValue());
			cb_year.setSelectedItem(date.getYear());
			printNum();
			if(lb_selected != null) {
				lb_selected.setBackground(Color.white);
			}		});
		
		cb_year.addItemListener(e -> {
			// 선택된 연도로  date 변경 
			date = LocalDate.of((int)cb_year.getSelectedItem(), date.getMonth(), date.getDayOfMonth());
			printNum();
			if(lb_selected != null) {
				lb_selected.setBackground(Color.white);
			}
		
		});
		cb_month.addItemListener(e -> {
			// 선택된 연도로  date 변경 
			date = LocalDate.of(date.getYear(), (int)cb_month.getSelectedItem(), date.getDayOfMonth());
			printNum();
			if(lb_selected != null) {
				lb_selected.setBackground(Color.white);
			}
			
		});
		
		
		for(int i = 0; i < lb_dates.length; i++) {
			final int idx = i;
			
			lb_dates[idx].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(lb_dates[idx].getText().length() == 0) {  // 날짜 없는 곳 선택시 종료
						return;
					}
					
					if(lb_selected != null) {
						lb_selected.setBackground(Color.white);
					}
					
					lb_dates[idx].setBackground(Color.blue);
					lb_selected = lb_dates[idx];
					
					if(e.getClickCount() == 2) {
						
						int year = date.getYear();
						int month = date.getMonth().getValue();
						int dateNum = Integer.parseInt(lb_dates[idx].getText());
						
						date = LocalDate.of(year, month, dateNum);
						
						String formattedMonth = String.format("%02d", month);  //0붙여서 나오기   
						String formattedDay = String.format("%02d", dateNum);  
						lb.setText(year + "." + formattedMonth + "." + formattedDay);
						dispose();
					}
					
					
				}
			});
		}

		setVisible(true);
		setBounds(500, 300, 500, 400);
		
	}
	
	// 콤보박스 채우기
	public void getYearList() {
		for(int i = date.getYear(); i >= 1980; i--) {
			cb_year.addItem(i);
		}
	}
	
	public void getMonthList() {
		for(int i = 1; i <= 12; i++) {
			cb_month.addItem(i);
		}
		cb_month.setSelectedItem(date.getMonthValue());
	}

	// 달력 출력
	public int getStartDay() {
		LocalDate d = LocalDate.of(date.getYear(), date.getMonth(), 1);
		return d.getDayOfWeek().getValue();   // Mon:1, Sun:7
	}
	
	public int getLastDate() {
		return date.lengthOfMonth();
	}
	
	public void printNum() {
		int count = 1;
		for(int i = 1; i <= lb_dates.length; i++) {
			// 1일이 찍히는게 해당 월의 시작 요일부터 
			
			if(i >= getStartDay() && count <= getLastDate()) {
				lb_dates[i-1].setText(Integer.toString(count++));
			}else {
				lb_dates[i-1].setText("");
			}
		}
	}

	
	public static void main(String[] args) {
		DatePicker d = new DatePicker(new JLabel());
		//LocalDate d = LocalDate.of(2025, 6, 1);
		d.printNum();
		
	}
	
}
