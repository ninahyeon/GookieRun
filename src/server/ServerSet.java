package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerSet {
	private ServiceToClient newC = null;
	private ServerSocket server = null;
	private Socket socket = null;
	private ArrayList<ServiceToClient> cList = new ArrayList<>();
	private ServiceToClient[] wr = new ServiceToClient[1]; // 대기실...

	ServerSet() {
		setServer();
	}

	private void setServer() {
		try {
			server = new ServerSocket();
			server.bind(new InetSocketAddress("localhost", 6666));
			while (true) {
				System.out.println("서버 설정 왈료! 클라이언트 접속 대기...");
				socket = server.accept(); // 클라이언트 접속 대기

				InetAddress ia = socket.getInetAddress();
				System.out.println("접속 ip : " + ia.getHostAddress());
				System.out.println(socket + "님이 방문하셨습니다.");
				System.out.println(socket.hashCode()); // 객체가 있는 주소

				// 새로운 클라이언트 접속 시 통신을 위한 객체생성
				newC = new ServiceToClient(socket, this);
				newC.start();
				cList.add(newC);
				System.out.println(cList.size() + "명이 접속 중");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void chat(String attempt, String userId, ServiceToClient stc) {
		attempt = attempt.replace("chat/", "");
		if (attempt.startsWith("@")) {
			// 귓속말
			int startIdx = 1;
			int endIdx = attempt.indexOf(" ");
			// 수신자 아이디
			String toWho = attempt.substring(startIdx, endIdx);
			String msg = attempt.substring(endIdx + 1);
			sendTo(userId, toWho, msg, stc);
		} else {
			// 전체메시지
			sendAll(userId, attempt);
		}
	}

	private void sendAll(String userId, String attempt) {
		String realMsg = "[" + userId + "] " + attempt;
		for (int i = 0; i < cList.size(); i++) {
			try {
				cList.get(i).getSendMsg().write(realMsg.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 귓속말 메서드
	private void sendTo(String userId, String toWho, String msg, ServiceToClient stc) {
		String realMsg = "[" + userId + "] " + toWho + " 님에게 귓속말 : " + msg;
		boolean flag = false;
		try {
			if (!userId.equals(toWho)) {
				for (int i = 0; i < cList.size(); i++) {
					if (cList.get(i).getUserId().equals(toWho)) {
						cList.get(i).getSendMsg().write(realMsg.getBytes());
						flag = true;
						break;
					}
				}
			} else {
				realMsg = "[나에게] " + msg;
				flag = true;
			}
			if (!flag) { // 만약 접속한 회원 중 해당 수신자가 존재하지 않으면
				realMsg = "**접속회원이 아닙니다.**";
			}
			stc.getSendMsg().write(realMsg.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 회원목록 업데이트해 클라이언트들에 전송
	public void updateOnUser(ServiceToClient stc) {
		String onList = "<접속회원 목록>\n";
		if (cList.size() > 0) {
			for (int i = 0; i < cList.size(); i++) {
				if (cList.get(i).getUserId() != null) {
					onList = onList + cList.get(i).getUserId() + "\n";
				}
			}
		}
		try {
			stc.getSendMsg().write(onList.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 로그아웃 하지 않고 GUI창을 종료했을 때
	public void exitWL(ServiceToClient stc) {
		for (int i = 0; i < cList.size(); i++) {
			if (cList.get(i).equals(stc)) {
				cList.remove(i);
				stc = null;
			}
		}
	}

	public void waitingRoom(ServiceToClient stc) {
		if (wr[0] == null) { // 첫 2p 희망자군요! 대기실에서 대기...
			wr[0] = stc;
		} else { // 두번째 2p 희망자군요! 게임을 시작해봅시다!
			stc.p2Go(wr[0]);
			wr[0].p2Go(stc);
			wr[0] = null; // 대기실 비우기...
		}
	}

}
