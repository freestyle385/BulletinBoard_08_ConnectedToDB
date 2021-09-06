package com.sbs.example.textBoard_controller;

import com.sbs.example.textBoard.Container;
import com.sbs.example.textBoard_dto.Member;
import com.sbs.example.textBoard_service.MemberService;

public class MemberController extends Controller {

	private MemberService memberService;

	public MemberController() {
		memberService = Container.memberService;
	}

	public void doJoin(String command) {
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

			boolean isLoginIdDup = memberService.isLoginIdDup(loginId);

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
		memberService.accountJoin(loginId, loginPw, name);

		System.out.printf("%s님 환영합니다.\n", name);

	}

	public void doLogin(String command) {
		System.out.println("--- 회원 로그인 ---");
		String loginId = null;
		String loginPw;
		
		int loginIdTryMaxCount = 3;
		int loginIdTryCount = 0;

		// 로그인 아이디 입력
		while (true) {
			if (loginIdTryCount >= loginIdTryMaxCount) {
				System.out.println("로그인 시도 횟수를 초과했습니다. 로그인 아이디 확인 후 다음에 다시 시도해주세요.");
				return;
			}
			
			System.out.printf("아이디 : ");
			loginId = sc.nextLine().trim();

			if (loginId.length() == 0) {
				loginIdTryCount++;
				System.out.println("로그인할 아이디를 입력해주세요.");
				continue;
			}

			boolean isLoginIdDup = memberService.isLoginIdDup(loginId);

			if (isLoginIdDup == false) {
				System.out.printf("%s(은)는 존재하지 않는 회원입니다.\n", loginId);
				continue;
			}
			break;

		}

		Member member = memberService.getMemberByLoginId(loginId);

		int loginPwTryMaxCount = 3;
		int loginPwTryCount = 0;

		// 로그인 비밀번호 입력
		while (true) {
			if (loginPwTryCount >= loginPwTryMaxCount) {
				System.out.println("로그인 시도 횟수를 초과했습니다.");
				break;
			}
			System.out.printf("로그인 비밀번호 : ");
			loginPw = sc.nextLine().trim();

			if (loginPw.length() == 0) {
				System.out.println("비밀번호를 입력해주세요.");
			}

			if (member.loginPw.equals(loginPw) == false) {
				loginPwTryCount++;
				System.out.println("비밀번호가 일치하지 않습니다");
				continue;
			}

			System.out.printf("%s님 환영합니다.\n", member.name);

			Container.session.loginedMemberId = member.id;
			Container.session.loginedMember = member;
			break;
		}

	}

	public void whoami(String command) {
		if (Container.session.loginedMemberId == -1) {
			System.out.println("로그인 상태가 아닙니다.");
		} else {
			System.out.printf("%s님이 로그인 중입니다.\n", Container.session.loginedMember.loginId);
		}

	}
}
