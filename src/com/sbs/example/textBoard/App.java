package com.sbs.example.textBoard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

		if (command.equals("article write")) {
			System.out.println("---게시글 생성---");
			System.out.print("제목 : ");
			String title = sc.nextLine();
			System.out.print("내용 : ");
			String body = sc.nextLine();

			PreparedStatement pstat = null;

			try {

				String sql = "INSERT INTO article";
				sql += " SET regDate = NOW()";
				sql += ", updateDate = NOW()";
				sql += ", title = \'" + title + "\'";
				sql += ", `body` = \'" + body + "\';";

				pstat = conn.prepareStatement(sql);
				int affectedRows = pstat.executeUpdate();

			} catch (SQLException e) {
				System.out.println("에러: " + e);

			} finally {
				try {
					if (pstat != null && !pstat.isClosed()) {
						pstat.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			System.out.println("게시물이 생성되었습니다.");

		} else if (command.equals("article list")) {
			System.out.println("---게시글 리스트---");
			List<Article> articles = new ArrayList<>();

			PreparedStatement pstat = null;
			ResultSet rs = null;

			try {

				String sql = "SELECT * FROM article ORDER BY id DESC;";

				pstat = conn.prepareStatement(sql);
				rs = pstat.executeQuery();

				while (rs.next()) {
					int id = rs.getInt("id");
					String regDate = rs.getString("regDate");
					String updateDate = rs.getString("updateDate");
					String title = rs.getString("title");
					String body = rs.getString("body");

					Article article = new Article(id, regDate, updateDate, title, body);
					articles.add(article);
				}

			} catch (SQLException e) {
				System.out.println("에러: " + e);

			} finally {
				try {
					if (rs != null && !rs.isClosed()) {
						rs.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (pstat != null && !pstat.isClosed()) {
						pstat.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (articles.size() == 0) {
				System.out.println("게시물이 존재하지 않습니다.");
				return 0;
			}

			System.out.println("번호  /  제목");

			for (Article article : articles) {
				System.out.printf("%d    /   %s\n", article.id, article.title);
			}

		} else if (command.startsWith("article modify ")) {
			int id = Integer.parseInt(command.split(" ")[2]);
			System.out.printf("---%d번 게시글 수정---\n", id);

			int foundIndexId = -1;
			PreparedStatement pstat = null;
			ResultSet rs = null;

			try {
				String sql = "SELECT * FROM article WHERE id = " + id + ";";

				pstat = conn.prepareStatement(sql);
				rs = pstat.executeQuery();

				while (rs.next()) {
					id = rs.getInt("id");

					foundIndexId = id;
				}

				if (foundIndexId == -1) {
					System.out.println("게시물이 존재하지 않습니다.");
					return 0;

				} else {

					System.out.print("새 제목 : ");
					String title = sc.nextLine();
					System.out.print("새 내용 : ");
					String body = sc.nextLine();

					sql = "UPDATE article";
					sql += " SET updateDate = NOW()";
					sql += ", title = \'" + title + "\'";
					sql += ", `body` = \'" + body + "\'";
					sql += " WHERE id = " + id + ";";

					pstat = conn.prepareStatement(sql);
					int affectedRows = pstat.executeUpdate();
				}

			} catch (SQLException e) {
				System.out.println("에러: " + e);

			} finally {

				try {
					if (pstat != null && !pstat.isClosed()) {
						pstat.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			System.out.printf("%d번 게시글 수정이 완료되었습니다.\n", id);

		} else if (command.startsWith("article detail ")) {
			int id = Integer.parseInt(command.split(" ")[2]);
			System.out.printf("---%d번 게시글 상세정보---\n", id);
			Article foundArticle = null;

			PreparedStatement pstat = null;
			ResultSet rs = null;

			try {

				String sql = "SELECT * FROM article WHERE id = " + id + ";";

				pstat = conn.prepareStatement(sql);
				rs = pstat.executeQuery();

				while (rs.next()) {
					String regDate = rs.getString("regDate");
					String updateDate = rs.getString("updateDate");
					String title = rs.getString("title");
					String body = rs.getString("body");

					foundArticle = new Article(id, regDate, updateDate, title, body);
				}

				if (foundArticle == null) {
					System.out.println("게시물이 존재하지 않습니다.");
					return 0;
				}

				System.out.printf("번호 : %d\n", foundArticle.id);
				System.out.printf("생성일 : %s\n", foundArticle.regDate);
				System.out.printf("수정일 : %s\n", foundArticle.updateDate);
				System.out.printf("제목 : %s\n", foundArticle.title);
				System.out.printf("내용 : %s\n", foundArticle.body);

			} catch (SQLException e) {
				System.out.println("에러: " + e);

			} finally {
				try {
					if (rs != null && !rs.isClosed()) {
						rs.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (pstat != null && !pstat.isClosed()) {
						pstat.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else if (command.startsWith("article delete ")) {
			int id = Integer.parseInt(command.split(" ")[2]);
			System.out.printf("---%d번 게시글 삭제---\n", id);

			int foundIndexId = -1;
			PreparedStatement pstat = null;
			ResultSet rs = null;

			try {
				String sql = "SELECT * FROM article WHERE id = " + id + ";";

				pstat = conn.prepareStatement(sql);
				rs = pstat.executeQuery();

				while (rs.next()) {
					id = rs.getInt("id");

					foundIndexId = id;
				}

				if (foundIndexId == -1) {
					System.out.println("게시물이 존재하지 않습니다.");
					return 0;

				} else {

					sql = "DELETE FROM article WHERE id = " + id + ";";

					pstat = conn.prepareStatement(sql);
					int affectedRows = pstat.executeUpdate();

				}
			} catch (SQLException e) {
				System.out.println("에러: " + e);

			} finally {
				try {
					if (pstat != null && !pstat.isClosed()) {
						pstat.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

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