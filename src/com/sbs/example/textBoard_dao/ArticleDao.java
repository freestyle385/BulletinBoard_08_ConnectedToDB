package com.sbs.example.textBoard_dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.sbs.example.textBoard.util.DBUtil;
import com.sbs.example.textBoard.util.SecSql;

public class ArticleDao {
	
	private Connection conn;
	
	public ArticleDao(Connection conn) {
		this.conn = conn;
	}

	public int dowrite(String title, String body) {
		
		SecSql sql = new SecSql();

		sql.append("INSERT INTO article");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", title = ?", title);
		sql.append(", `body` = ?", body);

		int id = DBUtil.insert(conn, sql); // DB에 새로 생성된 데이터의 id를 조회해 가져옴
		
		return id;
	}

	public boolean articleExists(int id) {
		SecSql sql = new SecSql(); 
		
		sql.append("SELECT COUNT(*) > 0");
		sql.append("FROM article");
		sql.append("WHERE id = ?", id);
		
		return DBUtil.selectRowBooleanValue(conn, sql);
		// 입력된 id의 게시물이 있다면 'SELECT COUNT(*) > 0' 식이 참이므로 1(true), 없다면 0(false)
	}

	public int doModify(String title, String body, int id) {
		SecSql sql = new SecSql();

		sql.append("UPDATE article");
		sql.append("SET updateDate = NOW()");
		sql.append(", title= ?", title);
		sql.append(", `body` = ?", body);
		sql.append("WHERE id = ?", id);
		
		return DBUtil.update(conn, sql);
	}

	public List<Map<String, Object>> showList() {
		SecSql sql = new SecSql();

		sql.append("SELECT *");
		sql.append("FROM article");
		sql.append("ORDER BY id DESC");
		
		return DBUtil.selectRows(conn, sql);
	}

	public Map<String, Object> showDetail(int id) {
		SecSql sql = new SecSql();

		sql.append("SELECT *");
		sql.append("FROM article");
		sql.append("WHERE id = ?", id);
		
		return DBUtil.selectRow(conn, sql);
	}

	public int doDelete(int id) {
		SecSql sql = new SecSql();
		
		sql.append("DELETE FROM article");
		sql.append("WHERE id = ?", id);
		
		return DBUtil.delete(conn, sql);
	}
	
	

}
