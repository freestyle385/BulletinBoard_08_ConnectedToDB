package com.sbs.example.textBoard.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sbs.example.textBoard.Article;

public class JDBCSelectTest {
	public static void main(String[] args) {
		Connection conn = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		List<Article> articles = new ArrayList<>();

		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/text_board?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			conn = DriverManager.getConnection(url, "root", "");
			System.out.println("연결 성공!");

			String sql = "SELECT * FROM article ORDER BY id DESC;";

			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();
			
			
			while (rs.next()) {
				int id = rs.getInt(1);
				String regDate = rs.getString(2);
				String updateDate = rs.getString(3);
				String title = rs.getString(4);
				String body = rs.getString(5);
				
//				Article article = new Article(id, regDate, updateDate, title, body);
//				articles.add(article);
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
		System.out.println("결과 : " + articles);
	}
}