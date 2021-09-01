package com.sbs.example.textBoard_controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sbs.example.textBoard.Article;
import com.sbs.example.textBoard.util.DBUtil;
import com.sbs.example.textBoard.util.SecSql;

public class ArticleController extends Controller {

	public void doWrite(String command) {
		System.out.println("---게시글 생성---");
		System.out.print("제목 : ");
		String title = sc.nextLine();
		System.out.print("내용 : ");
		String body = sc.nextLine();

		SecSql sql = new SecSql();

		sql.append("INSERT INTO article");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", title = ?", title);
		sql.append(", `body` = ?", body);

		int id = DBUtil.insert(conn, sql); // DB에 새로 생성된 데이터의 id를 조회해 가져옴

		System.out.printf("%d번 게시물이 생성되었습니다.\n", id);
	}

	public void doModify(String command) {
		int id = Integer.parseInt(command.split(" ")[2]);
		System.out.printf("---%d번 게시글 수정---\n", id);

		SecSql sql = new SecSql(); // 입력된 id로 삭제할 데이터가 존재하는지 여부 조회 => 0 또는 1개의 데이터 조회
		sql.append("SELECT COUNT(*) AS cnt");
		sql.append("FROM article");
		sql.append("WHERE id = ?", id);
		int articlesCount = DBUtil.selectRowIntValue(conn, sql);

		if (articlesCount == 0) {
			System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
			return;
		}

		System.out.print("새 제목 : ");
		String title = sc.nextLine();
		System.out.print("새 내용 : ");
		String body = sc.nextLine();

		sql = new SecSql();

		sql.append("UPDATE article");
		sql.append("SET updateDate = NOW()");
		sql.append(", title= ?", title);
		sql.append(", `body` = ?", body);
		sql.append("WHERE id = ?", id);

		DBUtil.update(conn, sql);

		System.out.printf("%d번 게시글 수정이 완료되었습니다.\n", id);
	}

	public void showList(String command) {
		System.out.println("---게시글 리스트---");
		List<Article> articles = new ArrayList<>();

		SecSql sql = new SecSql();

		sql.append("SELECT *");
		sql.append("FROM article");
		sql.append("ORDER BY id DESC");

		List<Map<String, Object>> articlesListMap = DBUtil.selectRows(conn, sql);
		// DB에서 조회해 불러온 데이터의 구조를 자바에서는 배열로 바로 인식할 수 없음 => 우선 맵핑(key,value) 리스트로 불러오기

		for (Map<String, Object> articleMap : articlesListMap) {
			articles.add(new Article(articleMap));
		}
		// 맵핑 리스트의 데이터 articleMap을 for문을 통해 articles 배열에 하나씩 넣어줌

		if (articles.size() == 0) {
			System.out.println("게시물이 존재하지 않습니다.");
			return;
		}

		System.out.println("번호  /  제목");

		for (Article article : articles) {
			System.out.printf("%d    /   %s\n", article.id, article.title);
		}
	}

	public void showDetail(String command) {
		int id = Integer.parseInt(command.split(" ")[2]);
		System.out.printf("---%d번 게시글 상세정보---\n", id);

		Article foundArticle = null;

		SecSql sql = new SecSql();

		sql.append("SELECT *");
		sql.append("FROM article");
		sql.append("WHERE id = ?", id);

		Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);
		foundArticle = new Article(articleMap);

		if (articleMap.isEmpty()) {
			System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
			return;
		}

		System.out.printf("번호 : %d\n", foundArticle.id);
		System.out.printf("생성일 : %s\n", foundArticle.regDate);
		System.out.printf("수정일 : %s\n", foundArticle.updateDate);
		System.out.printf("제목 : %s\n", foundArticle.title);
		System.out.printf("내용 : %s\n", foundArticle.body);
	}

	public void doDelete(String command) {
		int id = Integer.parseInt(command.split(" ")[2]);
		System.out.printf("---%d번 게시글 삭제---\n", id);

		SecSql sql = new SecSql(); // 입력된 id로 삭제할 데이터가 존재하는지 여부 조회 => 0 또는 1개의 데이터 조회
		sql.append("SELECT COUNT(*) AS cnt");
		sql.append("FROM article");
		sql.append("WHERE id = ?", id);
		int articlesCount = DBUtil.selectRowIntValue(conn, sql);

		if (articlesCount == 0) {
			System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
			return;
		}

		sql = new SecSql();
		sql.append("DELETE FROM article");
		sql.append("WHERE id = ?", id);

		DBUtil.delete(conn, sql);
		System.out.printf("%d번 게시물이 삭제되었습니다.\n", id);
	}

}
