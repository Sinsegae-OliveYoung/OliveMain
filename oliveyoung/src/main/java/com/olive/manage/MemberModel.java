package com.olive.manage;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.olive.common.model.Member;
import com.olive.common.model.User;
import com.olive.common.repository.MemberDAO;

public class MemberModel extends AbstractTableModel{
	
	User user;  //로그인 유저 정보
	List<Member> list;
	MemberDAO memberDAO;
	String[] column = { 
			"사원번호", "사원명", "소속지점", "직급", "연락처", "이메일", "입사일"
	};
	
	
	public MemberModel(User user) {
		this.user = user;
		memberDAO = new MemberDAO();
		MemberFilterDTO filter = new MemberFilterDTO();
		filter.setUser_id(1);
		list = memberDAO.select(filter);
		
	}

	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public int getColumnCount() {
		return column.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		Member member = list.get(row);
		
		switch(col) {
			case 0: return member.getUser().getUser_no();
			case 1: return member.getUser().getUser_name();
			case 2: return member.getBranch().getBr_name();
			case 3: return member.getUser().getRole().getRole_name();
			case 4: return member.getUser().getTel();
			case 5: return member.getUser().getEmail();
			case 6: return member.getUser().getHiredate();				
			default: return null;
		}
	}
}
