package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import client_gui.GUIMain;

public class ClientSet {
	private OutputStream sendMsg = null;
	private InputStream reMsg = null;

	private Socket clientSocket = null;
	private GUIMain myGUI = null;

	private String tempMsg = null;
	private String num = null;
	private String out = "";

	private String whosIn = "";
	private String chat = "";

	private String userID;
	private int coin;
	private int level;

	/****************************************************************************/
	private ClientSet() {
		setClient();
		try {
			reMsg = clientSocket.getInputStream();
			sendMsg = clientSocket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (clientSocket != null) {
			myGUI = new GUIMain(this);
		}
	}

	public static ClientSet getInstance() {
		return LazyHolder.IT;
	}

	private static class LazyHolder {
		private static final ClientSet IT = new ClientSet();
	}

	public void setClient() {
		try {
			clientSocket = new Socket("localhost", 6666);
			System.out.println("Ŭ���̾�Ʈ ���� : " + clientSocket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/****************************************************************************/

	// �α����̳� ȸ������ ���� �� ������ ���̵� ��� ���� ������ �޼���
	public String memberchk(String attempt) {
		try {
			num = "";
			sendMsg.write(attempt.getBytes());
			System.out.println("������ ������");

			// �����κ��� �޽��� �ޱ�
			byte[] reBuffer = new byte[100];
			reMsg.read(reBuffer);
			num = new String(reBuffer);
			num = num.trim();

			// �α��� ����
			System.out.println(num);
			if (num.startsWith("3/")) {
				// �ش� ȸ�� ���� �޾ƿ���
				String[] ua = num.split("/");
				userID = ua[1];
				level = Integer.parseInt(ua[2]);
				coin = Integer.parseInt(ua[3]);
				num = "3";
				// ���� ���� ����
				receive();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}

	/****************************************************************************/
	// ���� �޽��� �ޱ�
	private void receive() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (userID != null) {
					try {
						byte[] reBuffer = new byte[10000];
						reMsg.read(reBuffer);
						tempMsg = new String(reBuffer);
						tempMsg = tempMsg.trim();
						if (tempMsg.equals("logout/")) {
							break;
						} else if (tempMsg.startsWith("����\tID\t����\t����\n")) {
							myGUI.rank.viewRank(tempMsg);
						} else if (tempMsg.startsWith("<����ȸ�� ���>\n")) {
							whosIn = tempMsg.replace("<����ȸ�� ���>\n", "");
						} else {
							chat = chat + tempMsg + "\n";
							myGUI.hp.addChat(chat);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println("���̻� �� ����!");
			}
		}).start();
	}

	/****************************************************************************/

	// ������ �޽��� ������ �޼���!
	public void send(String msg) {
		try {
			sendMsg.write(msg.getBytes());

			sendMsg.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/****************************************************************************/

	public String getOut() {
		return out;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setOut(String out) {
		this.out = out;
	}

	public String getWhosIn() {
		return whosIn;
	}

	public void setWhosIn(String whosIn) {
		this.whosIn = whosIn;
	}

	public String getChat() {
		return chat;
	}

	public void setChat(String chat) {
		this.chat = chat;
	}
}
