package com.sbs.example.textBoard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import com.sbs.example.textBoard_controller.ArticleController;
import com.sbs.example.textBoard_controller.MemberController;

public class App {
	public void run() {
		System.out.println("---프로그램 시작---");
		Container.sc = new Scanner(System.in);
		
		Container.init();

		while (true) {
			System.out.print("입력하실 명령어 )) ");
			String command = Container.sc.nextLine().trim();

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
				Container.conn = conn;
				
				int actionResult = action(command);

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

		Container.sc.close();

	}

	private int action(String command) {

		MemberController memberController = Container.memberController;
		
		ArticleController articleController = Container.articleController;
		
		if (command.equals("member whoami")) {
			memberController.whoami(command);
		} else if (command.equals("member join")) {
			memberController.doJoin(command);

		} else if (command.equals("member login")) {
			memberController.doLogin(command);

		} else if (command.equals("article write")) {
			articleController.doWrite(command);
			
		} else if (command.startsWith("article modify ")) {
			articleController.doModify(command);

		} else if (command.equals("article list")) {
			articleController.showList(command);

		} else if (command.startsWith("article detail ")) {
			articleController.showDetail(command);

		} else if (command.startsWith("article delete ")) {
			articleController.doDelete(command);

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