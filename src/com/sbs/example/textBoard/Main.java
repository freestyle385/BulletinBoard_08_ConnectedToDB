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
		List<Article> articles = new ArrayList<>();

		int lastArticleId = 0;

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
				int id = lastArticleId + 1;
				lastArticleId = id;
				System.out.print("제목 : ");
				String title = sc.nextLine();
				System.out.print("내용 : ");
				String body = sc.nextLine();

//				System.out.printf("제목 : %s, 내용 : %s\n", title, body);

				Article article = new Article(id, title, body);
				articles.add(article);

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

//					System.out.println(sql);

					pstat = conn.prepareStatement(sql);

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

				System.out.printf("%d번 글이 생성되었습니다.\n", id);

			} else if (command.equals("article list")) {
				System.out.println("---게시글 리스트---");

				if (articles.size() == 0) {
					System.out.println("게시물이 존재하지 않습니다.");
					continue;
				}

				Connection conn = null;
				PreparedStatement pstat = null;
				ResultSet rs = null;

				try {
					Class.forName("com.mysql.jdbc.Driver");
					String url = "jdbc:mysql://127.0.0.1:3306/text_board?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

					conn = DriverManager.getConnection(url, "root", "");
					System.out.println("연결 성공!");

					String sql = "SELECT * FROM article;";

					pstat = conn.prepareStatement(sql);
					rs = pstat.executeQuery();

					while (rs.next()) {
						int id = rs.getInt(1);
						String regDate = rs.getString(2);
						String updateDate = rs.getString(3);
						String title = rs.getString(4);
						String body = rs.getString(5);

						System.out.println("id: " + id + " || regDate : " + regDate + " || updateDate : " + updateDate
								+ " || title : " + title + " || body : " + body);
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

			}
//			else if (command.equals("article detail")) {
//				System.out.println("article detail");
//				
//				
//			} else if (command.equals("article modify")) {
//				System.out.println("article modify");
//				
//				
//			} else if (command.equals("article delete")) {
//				System.out.println("article delete");
//				
//				
//			}

			else {
				System.out.println("잘못된 명령어입니다.");
			}
		}

		sc.close();
		System.out.println("---프로그램 종료---");
	}

}
