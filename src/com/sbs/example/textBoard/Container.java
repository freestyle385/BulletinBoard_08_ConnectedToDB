package com.sbs.example.textBoard;

import java.sql.Connection;
import java.util.Scanner;

import com.sbs.example.textBoard_controller.ArticleController;
import com.sbs.example.textBoard_controller.MemberController;
import com.sbs.example.textBoard_dao.ArticleDao;
import com.sbs.example.textBoard_dao.MemberDao;
import com.sbs.example.textBoard_service.ArticleService;
import com.sbs.example.textBoard_service.MemberService;
import com.sbs.example.textBoard_session.Session;

public class Container {
	public static ArticleController articleController;
	public static MemberController memberController;
	
	public static ArticleService articleService;
	public static MemberService memberService;
	
	public static ArticleDao articleDao;
	public static MemberDao memberDao;
	
	public static Session session;
	
	public static Scanner sc;
	public static Connection conn;
	
	public static void init() {
		session = new Session();
		
		articleDao = new ArticleDao();
		memberDao = new MemberDao();
		
		articleService = new ArticleService();
		memberService = new MemberService();
		
		articleController = new ArticleController();
		memberController = new MemberController();
	}
}
