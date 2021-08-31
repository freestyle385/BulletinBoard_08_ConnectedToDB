package com.sbs.example.textBoard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.sbs.example.textBoard.util.DBUtil;
import com.sbs.example.textBoard.util.SecSql;

public class App {
	public void run() {
		System.out.println("---프로그램 시작---");
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.print("입력하실 명령어 )) ");
			String command = sc.nextLine().trim();

			if (command.length() == 0) {
				System.out.println("다시 입력해주세요.");
				continue;
			}

			// DB 연결 시작
			Connection conn = null;

			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e1) {
				System.err.println("예외 : MySQL 드라이버 클래스가 없습니다.");
				System.out.println("프로그램을 종료합니다.");
				break;
			}
			String url = "jdbc:mysql://127.0.0.1:3306/text_board?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";
			try {
				conn = DriverManager.getConnection(url, "root", "");

				int actionResult = doAction(conn, sc, command);

				if (actionResult == -1) {
					break;
				}

			} catch (SQLException e1) {
				System.err.println("예외 : DB에 연결할 수 없습니다.");
				System.out.println("프로그램을 종료합니다.");
				break;
			} finally {
				try {
					if (conn != null && !conn.isClosed()) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		// DB 연결 끝

		sc.close();

	}

	private int doAction(Connection conn, Scanner sc, String command) {

		if (command.equals("member join")) {

			System.out.println("--- 회원가입 ---");
			String loginId;
			String loginPw;
			String loginPwConfirm;
			String name;

			// 로그인 아이디 입력
			while (true) {
				System.out.printf("아이디 : ");
				loginId = sc.nextLine().trim();

				if (loginId.length() == 0) {
					System.out.println("생성할 아이디를 입력해주세요.");
					continue;
				}

				SecSql sql = new SecSql();
				sql.append("SELECT COUNT(*) > 0"); 
				sql.append("FROM `member`");
				sql.append("WHERE loginId = ?", loginId);
				// loginId와 동일한 아이디가 있다면, 'SELECT COUNT(*) > 0' 식이 참이므로 1, 없다면 0

				boolean isLoginIdDup = DBUtil.selectRowBooleanValue(conn, sql);

				if (isLoginIdDup) {
					System.out.printf("%s(은)는 이미 사용중인 로그인 아이디입니다.\n", loginId);
					continue;
				}
				break;

			}
			// 로그인 비밀번호 입력
			while (true) {
				System.out.printf("로그인 비밀번호 : ");
				loginPw = sc.nextLine().trim();

				if (loginPw.length() == 0) {
					System.out.println("로그인 비밀번호를 입력해주세요.");
				}
				// 로그인 비밀번호 확인 입력
				boolean loginPwConfirmIsSame = true;

				while (true) {
					System.out.printf("로그인 비밀번호 확인 : ");
					loginPwConfirm = sc.nextLine().trim();

					if (loginPwConfirm.length() == 0) {
						System.out.println("로그인 비밀번호 확인을 입력해주세요.");
						continue;
					}
					if (loginPw.equals(loginPwConfirm) == false) {
						System.out.println("로그인 비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
						loginPwConfirmIsSame = false;
						break;
					}

					break;
				}
				// 로그인 비밀번호와 로그인 비밀번호 확인이 일치한다면 입력 완료.
				if (loginPwConfirmIsSame) {
					break;
				}
			}
			// 이름 입력
			while (true) {
				System.out.printf("이름 : ");
				name = sc.nextLine().trim();

				if (name.length() == 0) {
					System.out.println("이름을 입력해주세요.");
					continue;
				}
				break;

			}

			SecSql sql = new SecSql();

			sql.append("INSERT INTO `member`");
			sql.append("SET regDate = NOW()");
			sql.append(", updateDate = NOW()");
			sql.append(", loginId = ?", loginId);
			sql.append(", loginPw = ?", loginPw);
			sql.append(", `name` = ?", name);

			int id = DBUtil.insert(conn, sql);

			System.out.printf("%s님 환영합니다.\n", name);

		} else if (command.equals("article write")) {
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

		} else if (command.startsWith("article modify ")) {
			int id = Integer.parseInt(command.split(" ")[2]);
			System.out.printf("---%d번 게시글 수정---\n", id);

			SecSql sql = new SecSql(); // 입력된 id로 삭제할 데이터가 존재하는지 여부 조회 => 0 또는 1개의 데이터 조회
			sql.append("SELECT COUNT(*) AS cnt");
			sql.append("FROM article");
			sql.append("WHERE id = ?", id);
			int articlesCount = DBUtil.selectRowIntValue(conn, sql);

			if (articlesCount == 0) {
				System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
				return 0;
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

		} else if (command.equals("article list")) {
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
				return 0;
			}

			System.out.println("번호  /  제목");

			for (Article article : articles) {
				System.out.printf("%d    /   %s\n", article.id, article.title);
			}

		} else if (command.startsWith("article detail ")) {
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
				return 0;
			}

			System.out.printf("번호 : %d\n", foundArticle.id);
			System.out.printf("생성일 : %s\n", foundArticle.regDate);
			System.out.printf("수정일 : %s\n", foundArticle.updateDate);
			System.out.printf("제목 : %s\n", foundArticle.title);
			System.out.printf("내용 : %s\n", foundArticle.body);

		} else if (command.startsWith("article delete ")) {
			int id = Integer.parseInt(command.split(" ")[2]);
			System.out.printf("---%d번 게시글 삭제---\n", id);

			SecSql sql = new SecSql(); // 입력된 id로 삭제할 데이터가 존재하는지 여부 조회 => 0 또는 1개의 데이터 조회
			sql.append("SELECT COUNT(*) AS cnt");
			sql.append("FROM article");
			sql.append("WHERE id = ?", id);
			int articlesCount = DBUtil.selectRowIntValue(conn, sql);

			if (articlesCount == 0) {
				System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
				return 0;
			}

			sql = new SecSql();
			sql.append("DELETE FROM article");
			sql.append("WHERE id = ?", id);

			DBUtil.delete(conn, sql);
			System.out.printf("%d번 게시물이 삭제되었습니다.\n", id);

		} else if (command.equals("system exit")) {
			System.out.println("== 프로그램 종료 ==");
			return -1;
		} else {
			System.out.println("존재하지 않는 명령어입니다.");
			return 0;
		}
		return 0;
	}
}