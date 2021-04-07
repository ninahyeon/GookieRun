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
		} // 스트림 받음
	}

	@Override
	public void run() {
		infiniteStream();
	}

	// 스트림을 무한히 받아오는 메서드
	private void infiniteStream() {
		try {
			while (true) {
				byte[] reBuffer = new byte[100];
				reMsg.read(reBuffer);
				String attempt = new String(reBuffer);
				attempt = attempt.trim(); // 배열 빈공간 제거
				// 받은 정보가 어떤 정보인지 구분
				whatInfo(attempt);
			}
		} catch (SocketException e) {
			// 로그아웃하지 않고 GUI창 껐을 때 나는 오류(소켓 연결이 끊어졌으므로 해당 사용자가 로그아웃한 것으로 간주)
			myServer.exitWL(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 스트림 내용을 판단하는 메서드
	private void whatInfo(String attempt) {
		if (attempt.startsWith("login") || attempt.startsWith("signup")) {
			// 로그인/회원가입일 때
			memberchk(attempt);
		} else if (attempt.startsWith("chat")) {
			// 채팅일 때
			myServer.chat(attempt, userId, this);
		} else if (attempt.startsWith("logout")) {
			System.out.println("앗 고객이 로그아웃했다");
			userId = null;
			logout(attempt);
		} else if (attempt.startsWith("gg/")) {
			gameOver(attempt);
		} else if (attempt.startsWith("levelUp/")) {
			lvUp(attempt);
		} else if (attempt.startsWith("rank")) {
			rank();
		} else if (attempt.startsWith("접속자목록 주세요")) {
			myServer.updateOnUser(this);
		} else if (attempt.startsWith("play2")) {
			// 2인플레이 하고싶다고? 대기실로 가!
			myServer.waitingRoom(this);
		}
	}

	// 2p 두 명 모였다!
	public void p2Go(ServiceToClient stc) {
		this.oppo = stc; // 상대편 stc
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
		String rank = "순위\tID\t레벨\t점수\n";
		if (rList.size() < 0) {
			rank = rank + "아직 기록이 없습니다.";
		} else {
			for (int i = 0; i < rList.size(); i++) {
				rank = rank + (i + 1) + "위\t" + rList.get(i).prt() + "\n";
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
			// 랭킹정보 먼저 받고 처리
			RankDTO r = new RankDTO();
			r.setId(ar[1]);
			r.setLevel(Integer.parseInt(ar[2]));
			r.setPoint(Integer.parseInt(ar[3]));
			rDAO.insert(r);

			// 멤버 코인 정보 처리
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
			System.out.println("아 고객이 로그아웃 했다니까!");
			sendMsg.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 멤버DAO 접근해 로그인/회원가입 가능한지 알려주는 메서드
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
		// 클라이언트에게 결과 전송
		try {
			sendMsg.write(num.getBytes());

			sendMsg.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 로그인한 회원 객체 DB에서 찾아 전송
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
