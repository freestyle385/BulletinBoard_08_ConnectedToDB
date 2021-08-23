package com.sbs.example.textBoard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		System.out.println("---프로그램 시작---");
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.print("입력하실 명령어 )) ");
			String command = sc.nextLine().trim();

			if (command.length() == 0) {
				System.out.println("다시 입력해주세요.");
				continue;
			}

			if (command.equals("system exit")) {
				break;
			}
			if (command.equals("article write")) {
				System.out.println("---게시글 생성---");
				System.out.print("제목 : ");
				String title = sc.nextLine();
				System.out.print("내용 : ");
				String body = sc.nextLine();

				Connection conn = null;
				PreparedStatement pstat = null;

				try {
					Class.forName("com.mysql.jdbc.Driver");
					String url = "jdbc:mysql://127.0.0.1:3306/text_board?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

					conn = DriverManager.getConnection(url, "root", "");
					System.out.println("연결 성공!");

					String sql = "INSERT INTO article";
					sql += " SET regDate = NOW()";
					sql += ", updateDate = NOW()";
					sql += ", title = \'" + title + "\'";
					sql += ", `body` = \'" + body + "\';";

					pstat = conn.prepareStatement(sql);
					int affectedRows = pstat.executeUpdate();

				} catch (ClassNotFoundException e) {
					System.out.println("드라이버 로딩 실패");
				} catch (SQLException e) {
					System.out.println("에러: " + e);
				} finally {
					try {
						if (conn != null && !conn.isClosed()) {
							conn.close();
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
				System.out.println("게시물이 생성되었습니다.");

			} else if (command.equals("article list")) {
				System.out.println("---게시글 리스트---");
				List<Article> articles = new ArrayList<>();

				Connection conn = null;
				PreparedStatement pstat = null;
				ResultSet rs = null;

				try {
					Class.forName("com.mysql.jdbc.Driver");
					String url = "jdbc:mysql://127.0.0.1:3306/text_board?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

					conn = DriverManager.getConnection(url, "root", "");
					System.out.println("연결 성공!");

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

				} catch (ClassNotFoundException e) {
					System.out.println("드라이버 로딩 실패");
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
					try {
						if (conn != null && !conn.isClosed()) {
							conn.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

				if (articles.size() == 0) {
					System.out.println("게시물이 존재하지 않습니다.");
					continue;
				}

				System.out.println("번호  /  제목");

				for (Article article : articles) {
					System.out.printf("%d    /   %s\n", article.id, article.title);
				}

			} else if (command.startsWith("article modify ")) {
				int id = Integer.parseInt(command.split(" ")[2]);
				System.out.printf("---%d번 게시글 수정---\n", id);
				System.out.print("새 제목 : ");
				String title = sc.nextLine();
				System.out.print("새 내용 : ");
				String body = sc.nextLine();

				Connection conn = null;
				PreparedStatement pstat = null;

				try {
					Class.forName("com.mysql.jdbc.Driver");
					String url = "jdbc:mysql://127.0.0.1:3306/text_board?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

					conn = DriverManager.getConnection(url, "root", "");
					System.out.println("연결 성공!");

					String sql = "UPDATE article";
					sql += " SET updateDate = NOW()";
					sql += ", title = \'" + title + "\'";
					sql += ", `body` = \'" + body + "\'";
					sql += " WHERE id = " + id + ";";

					pstat = conn.prepareStatement(sql);
					int affectedRows = pstat.executeUpdate();

				} catch (ClassNotFoundException e) {
					System.out.println("드라이버 로딩 실패");
				} catch (SQLException e) {
					System.out.println("에러: " + e);
				} finally {
					try {
						if (conn != null && !conn.isClosed()) {
							conn.close();
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
				System.out.printf("%d번 게시글 수정이 완료되었습니다.\n", id);

			} else if (command.startsWith("article detail ")) {
				int id = Integer.parseInt(command.split(" ")[2]);
				System.out.printf("---%d번 게시글 상세정보---\n", id);
				Article foundArticle = null;

				Connection conn = null;
				PreparedStatement pstat = null;
				ResultSet rs = null;

				try {
					Class.forName("com.mysql.jdbc.Driver");
					String url = "jdbc:mysql://127.0.0.1:3306/text_board?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

					conn = DriverManager.getConnection(url, "root", "");
					System.out.println("연결 성공!");

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
				} catch (ClassNotFoundException e) {
					System.out.println("드라이버 로딩 실패");
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
					try {
						if (conn != null && !conn.isClosed()) {
							conn.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

				if (foundArticle == null) {
					System.out.println("게시물이 존재하지 않습니다.");
					continue;
				}

				System.out.printf("번호 : %d\n", foundArticle.id);
				System.out.printf("생성일 : %s\n", foundArticle.regDate);
				System.out.printf("수정일 : %s\n", foundArticle.updateDate);
				System.out.printf("제목 : %s\n", foundArticle.title);
				System.out.printf("내용 : %s\n", foundArticle.body);

			} else if (command.startsWith("article delete ")) {
				int id = Integer.parseInt(command.split(" ")[2]);
				System.out.printf("---%d번 게시글 삭제---\n", id);

				Connection conn = null;
				PreparedStatement pstat = null;

				try {
					Class.forName("com.mysql.jdbc.Driver");
					String url = "jdbc:mysql://127.0.0.1:3306/text_board?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

					conn = DriverManager.getConnection(url, "root", "");
					System.out.println("연결 성공!");

					String sql = "DELETE FROM article WHERE id = " + id + ";";

					pstat = conn.prepareStatement(sql);
					int affectedRows = pstat.executeUpdate();

				} catch (ClassNotFoundException e) {
					System.out.println("드라이버 로딩 실패");
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
					try {
						if (conn != null && !conn.isClosed()) {
							conn.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				System.out.printf("%d번 게시물이 삭제되었습니다.\n", id);

			} else {
				System.out.println("잘못된 명령어입니다.");
			}
		}

		sc.close();
		System.out.println("---프로그램 종료---");
	}

}
