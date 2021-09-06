package com.sbs.example.textBoard_service;

import com.sbs.example.textBoard.Container;
import com.sbs.example.textBoard_dao.MemberDao;
import com.sbs.example.textBoard_dto.Member;

public class MemberService {

	private MemberDao memberDao;

	public MemberService() {
		memberDao = Container.memberDao;
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
