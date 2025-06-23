package com.olive.common.model;

public class Branch {

	private int br_id;
	private String br_name;
	private String br_address;
	private String br_tel;
	private User user;
	
	public int getBr_id() {
		return br_id;
	}
	public void setBr_id(int br_id) {
		this.br_id = br_id;
	}
	public String getBr_name() {
		return br_name;
	}
	public void setBr_name(String br_name) {
		this.br_name = br_name;
	}
	public String getBr_address() {
		return br_address;
	}
	public void setBr_address(String br_address) {
		this.br_address = br_address;
	}
	public String getBr_tel() {
		return br_tel;
	}
	public void setBr_tel(String br_tel) {
		this.br_tel = br_tel;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String toString() {
		return this.br_name;
	}
	
	// branch 객체 간 비교 후 같은 값이 있는지 확인하기 위해 오버라이딩
	public boolean equals(Object obj) {
		if (this == obj) return true;		// 현재 객체와 매개변수 객체가 같은 주소를 가지면 true
		if (!(obj instanceof Branch)) return false; // 매개변수 객체가 Branch 타입이 아니면 false
		Branch branch = (Branch) obj;	// 매개변수 객체가 Branch 타입에 속하면 캐스팅해서
		return br_id == branch.br_id;	// 같은 타입이 된 객체끼리 비교하여 결과값 반환
	}
}

