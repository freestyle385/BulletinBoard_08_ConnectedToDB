package com.sbs.example.textBoard_service;

import java.util.List;

import com.sbs.example.textBoard.Container;
import com.sbs.example.textBoard_dao.ArticleDao;
import com.sbs.example.textBoard_dto.Article;

public class ArticleService {

	private ArticleDao articleDao;

	public ArticleService() {
		articleDao = Container.articleDao;
	}

	public int write(int memberId, String title, String body) {
		return articleDao.write(memberId, title, body);
	}

	public boolean articleExists(int id) {
		return articleDao.articleExists(id);
	}

	public void update(String title, String body, int id) {
		articleDao.update(title, body, id);
	}

	public List<Article> getArticles() {
		return articleDao.getArticles();
	}

	public Article getArticleById(int id) {
		return articleDao.getArticleById(id);
	}

	public void delete(int id) {
		articleDao.delete(id);
	}

	public void increaseHit(int id) {
		articleDao.increaseHit(id);
	}

}