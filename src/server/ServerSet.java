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
	private ServiceToClient[] wr = new ServiceToClient[1]; // ����...

	ServerSet() {
		setServer();
	}

	private void setServer() {
		try {
			server = new ServerSocket();
			server.bind(new InetSocketAddress("localhost", 6666));
			while (true) {
				System.out.println("���� ���� �з�! Ŭ���̾�Ʈ ���� ���...");
				socket = server.accept(); // Ŭ���̾�Ʈ ���� ���

				InetAddress ia = socket.getInetAddress();
				System.out.println("���� ip : " + ia.getHostAddress());
				System.out.println(socket + "���� �湮�ϼ̽��ϴ�.");
				System.out.println(socket.hashCode()); // ��ü�� �ִ� �ּ�

				// ���ο� Ŭ���̾�Ʈ ���� �� ����� ���� ��ü����
				newC = new ServiceToClient(socket, this);
				newC.start();
				cList.add(newC);
				System.out.println(cList.size() + "���� ���� ��");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void chat(String attempt, String userId, ServiceToClient stc) {
		attempt = attempt.replace("chat/", "");
		if (attempt.startsWith("@")) {
			// �ӼӸ�
			int startIdx = 1;
			int endIdx = attempt.indexOf(" ");
			// ������ ���̵�
			String toWho = attempt.substring(startIdx, endIdx);
			String msg = attempt.substring(endIdx + 1);
			sendTo(userId, toWho, msg, stc);
		} else {
			// ��ü�޽���
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

	// �ӼӸ� �޼���
	private void sendTo(String userId, String toWho, String msg, ServiceToClient stc) {
		String realMsg = "[" + userId + "] " + toWho + " �Կ��� �ӼӸ� : " + msg;
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
				realMsg = "[������] " + msg;
				flag = true;
			}
			if (!flag) { // ���� ������ ȸ�� �� �ش� �����ڰ� �������� ������
				realMsg = "**����ȸ���� �ƴմϴ�.**";
			}
			stc.getSendMsg().write(realMsg.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ȸ����� ������Ʈ�� Ŭ���̾�Ʈ�鿡 ����
	public void updateOnUser(ServiceToClient stc) {
		String onList = "<����ȸ�� ���>\n";
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

	// �α׾ƿ� ���� �ʰ� GUIâ�� �������� ��
	public void exitWL(ServiceToClient stc) {
		for (int i = 0; i < cList.size(); i++) {
			if (cList.get(i).equals(stc)) {
				cList.remove(i);
				stc = null;
			}
		}
	}

	public void waitingRoom(ServiceToClient stc) {
		if (wr[0] == null) { // ù 2p ����ڱ���! ���ǿ��� ���...
			wr[0] = stc;
		} else { // �ι�° 2p ����ڱ���! ������ �����غ��ô�!
			stc.p2Go(wr[0]);
			wr[0].p2Go(stc);
			wr[0] = null; // ���� ����...
		}
	}

}
