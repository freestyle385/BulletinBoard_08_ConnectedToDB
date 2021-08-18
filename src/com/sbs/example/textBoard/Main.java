package com.sbs.example.textBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		List<Article> articles = new ArrayList<>();

		System.out.println("---프로그램 시작---");

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
				System.out.println("article write");
				
				
			} else if (command.equals("article list")) {
				System.out.println("article list");
				
				
			} else if (command.equals("article detail")) {
				System.out.println("article detail");
				
				
			} else if (command.equals("article modify")) {
				System.out.println("article modify");
				
				
			} else if (command.equals("article delete")) {
				System.out.println("article delete");
				
				
			}

			else {
				System.out.println("잘못된 명령어입니다.");
			}
		}

		sc.close();
		System.out.println("---프로그램 종료---");
	}

}
