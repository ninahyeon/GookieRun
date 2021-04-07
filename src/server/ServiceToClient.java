package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import jdbc.MemberDAO;
import jdbc.MemberDTO;
import jdbc.RankDAO;
import jdbc.RankDTO;

public class ServiceToClient extends Thread {
	private OutputStream sendMsg = null;
	private InputStream reMsg = null;

	private Socket socket = null;
	private ServerSet myServer = null;
	private String userId = null;

	// DAO
	private Member member = new Member();
	private RankDAO rDAO = RankDAO.getInstance();
	private MemberDAO mDAO = MemberDAO.getInstance();
	private MemberDTO m = null;
	private ArrayList<RankDTO> rList = null;

	// 2p
	private ServiceToClient oppo;

	public ServiceToClient(Socket socket, ServerSet serverSet) {
		this.socket = socket;
		this.myServer = serverSet;
		try {
			reMsg = socket.getInputStream();
			sendMsg = socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // ��Ʈ�� ����
	}

	@Override
	public void run() {
		infiniteStream();
	}

	// ��Ʈ���� ������ �޾ƿ��� �޼���
	private void infiniteStream() {
		try {
			while (true) {
				byte[] reBuffer = new byte[100];
				reMsg.read(reBuffer);
				String attempt = new String(reBuffer);
				attempt = attempt.trim(); // �迭 ����� ����
				// ���� ������ � �������� ����
				whatInfo(attempt);
			}
		} catch (SocketException e) {
			// �α׾ƿ����� �ʰ� GUIâ ���� �� ���� ����(���� ������ ���������Ƿ� �ش� ����ڰ� �α׾ƿ��� ������ ����)
			myServer.exitWL(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ��Ʈ�� ������ �Ǵ��ϴ� �޼���
	private void whatInfo(String attempt) {
		if (attempt.startsWith("login") || attempt.startsWith("signup")) {
			// �α���/ȸ�������� ��
			memberchk(attempt);
		} else if (attempt.startsWith("chat")) {
			// ä���� ��
			myServer.chat(attempt, userId, this);
		} else if (attempt.startsWith("logout")) {
			System.out.println("�� ���� �α׾ƿ��ߴ�");
			userId = null;
			logout(attempt);
		} else if (attempt.startsWith("gg/")) {
			gameOver(attempt);
		} else if (attempt.startsWith("levelUp/")) {
			lvUp(attempt);
		} else if (attempt.startsWith("rank")) {
			rank();
		} else if (attempt.startsWith("�����ڸ�� �ּ���")) {
			myServer.updateOnUser(this);
		} else if (attempt.startsWith("play2")) {
			// 2���÷��� �ϰ�ʹٰ�? ���Ƿ� ��!
			myServer.waitingRoom(this);
		}
	}

	// 2p �� �� �𿴴�!
	public void p2Go(ServiceToClient stc) {
		this.oppo = stc; // ����� stc
		System.out.println(oppo);
		String opID = "opID/" + oppo.m.getId();

		try {
			sendMsg.write(opID.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void rank() {
		rList = rDAO.rank();
		String rank = "����\tID\t����\t����\n";
		if (rList.size() < 0) {
			rank = rank + "���� ����� �����ϴ�.";
		} else {
			for (int i = 0; i < rList.size(); i++) {
				rank = rank + (i + 1) + "��\t" + rList.get(i).prt() + "\n";
			}
		}
		try {
			sendMsg.write(rank.getBytes());

			sendMsg.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void lvUp(String attempt) {
		String[] msg = attempt.split("/");
		m = new MemberDTO();
		m.setId(msg[1]);
		m.setCoin(Integer.parseInt(msg[2]));
		m.setLevel(Integer.parseInt(msg[3]));
		mDAO.levelUp(m);
	}

	private void gameOver(String attempt) {
		try {
			String[] ar = attempt.split("/");
			// ��ŷ���� ���� �ް� ó��
			RankDTO r = new RankDTO();
			r.setId(ar[1]);
			r.setLevel(Integer.parseInt(ar[2]));
			r.setPoint(Integer.parseInt(ar[3]));
			rDAO.insert(r);

			// ��� ���� ���� ó��
			m = new MemberDTO();
			m.setId(ar[1]);
			m.setCoin(Integer.parseInt(ar[4]));
			mDAO.coinUpdate(m);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void logout(String att) {
		try {
			sendMsg.write(att.getBytes());
			System.out.println("�� ���� �α׾ƿ� �ߴٴϱ�!");
			sendMsg.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ���DAO ������ �α���/ȸ������ �������� �˷��ִ� �޼���
	private void memberchk(String attempt) {
		String[] info = attempt.split("/");
		String num = "";
		if (info[0].equals("login")) {
			num = "" + member.login(info[1], info[2]);
		} else if (info[0].equals("signup")) {
			if (!member.iddup(info[1])) {
				num = "1";
			} else {
				member.signup(info[1], info[2]);
				num = "2";
			}
		}
		if (num.equals("3")) {
			userId = info[1];
			m = mDAO.selectOne(userId);
			num = "3/" + m.getId() + "/" + m.getLevel() + "/" + m.getCoin();
			System.out.println(num);
		}
		// Ŭ���̾�Ʈ���� ��� ����
		try {
			sendMsg.write(num.getBytes());

			sendMsg.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// �α����� ȸ�� ��ü DB���� ã�� ����
//	private void login() {
//		m = mDAO.selectOne(userId);
//		String user = "3/" + m.getId() + "/" + m.getLevel() + "/" + m.getCoin();
//		System.out.println(user);
//		try {
//			sendMsg.write(user.getBytes());
//			sendMsg.flush();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public OutputStream getSendMsg() {
		return sendMsg;
	}
}
