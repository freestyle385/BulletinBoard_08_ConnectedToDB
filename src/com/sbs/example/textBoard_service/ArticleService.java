package com.sbs.example.textBoard_service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.sbs.example.textBoard_dao.ArticleDao;

public class ArticleService {
	
	private ArticleDao articleDao;
	
	public ArticleService(Connection conn) {
		articleDao = new ArticleDao(conn);
	}
	
	public int dowrite(String title, String body) {
		return articleDao.dowrite(title, body);
	}
	
	public boolean articleExists(int id) {
		return articleDao.articleExists(id);
	}

	public int doModify(String title, String body, int id) {
		return articleDao.doModify(title, body, id);
	}

	public List<Map<String, Object>> showList() {
		return articleDao.showList();
	}

	public Map<String, Object> showDetail(int id) {
		return articleDao.showDetail(id);
	}

	public int doDelete(int id) {
		return articleDao.doDelete(id);
	}
}
