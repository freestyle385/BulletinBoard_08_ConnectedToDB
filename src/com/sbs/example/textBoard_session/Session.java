package com.sbs.example.textBoard_session;

import com.sbs.example.textBoard_dto.Member;

public class Session {
	public int loginedMemberId;
	
	public Member loginedMember = null;
	
	public Session() {
		loginedMemberId = -1;
	}
}
