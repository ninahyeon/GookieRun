package server;

import java.util.ArrayList;

import jdbc.MemberDAO;
import jdbc.MemberDTO;

public class Member {
	// ����Ʈ ���⼭ ���� ���� ���� DAO select���� �����ؼ� ��������
	private ArrayList<MemberDTO> mList = null;
	private MemberDAO mDAO = null;
	private MemberDTO m = null;

	public Member() {
		mDAO = MemberDAO.getInstance();
	}

	public int login(String id, String pw) {
		if (iddup(id)) {
			return 1;
		} else if (!m.getPw().equals(pw)) {
			return 2;
		} else {
			return 3;
		}
	}

	public void signup(String id, String pw) {
		MemberDTO m = new MemberDTO();
		m.setId(id);
		m.setPw(pw);
		if (mDAO.insert(m) == 1) {
			System.out.println("ȸ�����Լ���!");
		}
	}

	// id �ߺ� üũ
	public boolean iddup(String id) {
		mList = mDAO.selectAll();
		if (mList.size() < 0) {
			return true;
		} else {
			for (int i = 0; i < mList.size(); i++) {
				if (mList.get(i).getId().equals(id)) {
					this.m = mList.get(i);
					return false;
				}
			}
			return true;
		}
	}

}
