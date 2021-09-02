package com.sbs.example.textBoard_dao;

import java.sql.Connection;

import com.sbs.example.textBoard.util.DBUtil;
import com.sbs.example.textBoard.util.SecSql;

public class MemberDao {
	
	private Connection conn;
	
	public MemberDao(Connection conn) {
		this.conn = conn;
	}

	public boolean isLoginIdDup(String loginId) {
		SecSql sql = new SecSql();
		
		sql.append("SELECT COUNT(*) > 0");
		sql.append("FROM `member`");
		sql.append("WHERE loginId = ?", loginId);
		// loginId와 동일한 아이디가 있다면, 'SELECT COUNT(*) > 0' 식이 참이므로 1(true), 없다면 0(false)
		
		return DBUtil.selectRowBooleanValue(conn, sql);
	}

	public void doJoin(String loginId, String loginPw, String name) {
		SecSql sql = new SecSql();

		sql.append("INSERT INTO `member`");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", loginId = ?", loginId);
		sql.append(", loginPw = ?", loginPw);
		sql.append(", `name` = ?", name);
		
		DBUtil.insert(conn, sql);
	}

}
