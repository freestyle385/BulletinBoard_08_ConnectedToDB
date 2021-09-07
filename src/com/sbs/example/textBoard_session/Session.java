package com.sbs.example.textBoard_session;

import com.sbs.example.textBoard_dto.Member;

public class Session {
	public int loginedMemberId;
	
	public Member loginedMember = null;
	
	public Session() {
		loginedMemberId = -1;
	}

	public boolean isLogined() {
		
		return loginedMemberId != -1;
	}
	
	
	public void logout() {
		loginedMemberId = -1;
		loginedMember = null;
	}
	
	public void login(Member member) {
		loginedMemberId = member.id;
		loginedMember = member;
	}
}
