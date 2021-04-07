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
			System.out.println("클라이언트 정보 : " + clientSocket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/****************************************************************************/

	// 로그인이나 회원가입 했을 때 서버로 아이디 비번 정보 보내는 메서드
	public String memberchk(String attempt) {
		try {
			num = "";
			sendMsg.write(attempt.getBytes());
			System.out.println("서버로 보낸당");

			// 서버로부터 메시지 받기
			byte[] reBuffer = new byte[100];
			reMsg.read(reBuffer);
			num = new String(reBuffer);
			num = num.trim();

			// 로그인 성공
			System.out.println(num);
			if (num.startsWith("3/")) {
				// 해당 회원 정보 받아오기
				String[] ua = num.split("/");
				userID = ua[1];
				level = Integer.parseInt(ua[2]);
				coin = Integer.parseInt(ua[3]);
				num = "3";
				// 무한 수신 시작
				receive();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}

	/****************************************************************************/
	// 무한 메시지 받기
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
						} else if (tempMsg.startsWith("순위\tID\t레벨\t점수\n")) {
							myGUI.rank.viewRank(tempMsg);
						} else if (tempMsg.startsWith("<접속회원 목록>\n")) {
							whosIn = tempMsg.replace("<접속회원 목록>\n", "");
						} else {
							chat = chat + tempMsg + "\n";
							myGUI.hp.addChat(chat);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println("더이상 안 받음!");
			}
		}).start();
	}

	/****************************************************************************/

	// 서버로 메시지 보내는 메서드!
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
