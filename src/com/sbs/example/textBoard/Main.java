package com.sbs.example.textBoard;

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

				System.out.printf("%d번 글이 생성되었습니다.\n", id);

			} else if (command.equals("article list")) {
				System.out.println("---게시글 리스트---");

				if (articles.size() == 0) {
					System.out.println("게시물이 존재하지 않습니다.");
					continue;
				}

				System.out.println("번호  /  제목");

				for (Article article : articles) {
					System.out.printf("  %d  /  %s  \n", article.id, article.title);
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
