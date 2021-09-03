package com.sbs.example.textBoard_service;

import java.sql.Connection;

import com.sbs.example.textBoard.Member;
import com.sbs.example.textBoard_dao.MemberDao;

public class MemberService {

	private MemberDao memberDao;

	public MemberService(Connection conn) {
		memberDao = new MemberDao(conn);
	}

	public boolean isLoginIdDup(String loginId) {
		return memberDao.isLoginIdDup(loginId);
	}

	public void accountJoin(String loginId, String loginPw, String name) {
		memberDao.accountJoin(loginId, loginPw, name);
	}

	public Member getMemberByLoginId(String loginId) {
		return memberDao.getMemberByLoginId(loginId);
	}

}
