package client_gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import client.ClientSet;

public class HomePan extends JPanel implements ActionListener {
	private String userID;
	private int coin;
	private int level;
	private ClientSet clientSet = null;
	private GUIMain gm = null;
	private JButton play = null;
	private JButton lvup = null;
	private JButton rank = null;
	private JLabel lvCoin = null;
	private JLabel coinView = null;
	private JLabel lvView = null;
	private JLabel gookieIMG = null;
	private JButton logout = null;

	// ä��
	private JScrollPane chatScroll;
	private JScrollPane onScroll;
	private JTextArea showChat;
	private JTextField writeChat;
	private JButton sendChat;
	private JTextArea inList;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel2;

	// 2�ο����
	private JButton play2;

	private boolean out = false;

	/**
	 * Create the panel.
	 * 
	 * @param id
	 */
	public HomePan(GUIMain gm) {
		this.clientSet = ClientSet.getInstance();
		setBackground(new Color(255, 222, 173));
		this.gm = gm;
		this.userID = clientSet.getUserID();
		this.coin = clientSet.getCoin();
		this.level = clientSet.getLevel();
		setPan();
		addLis();
		inUpdate();
	}

	private void inUpdate() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					out = false;
					while (!out) {
						// ������ ��� 1�ʿ� �� ���� ������Ʈ
						clientSet.send("�����ڸ�� �ּ���");
						inList.setText(clientSet.getWhosIn());
						Thread.sleep(1000);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void setPan() {
		setLayout(null);

		sendChat = new JButton("\u2192");
		sendChat.setBounds(501, 552, 52, 26);
		add(sendChat);

		writeChat = new JTextField();
		writeChat.setBounds(30, 553, 472, 25);
		add(writeChat);
		writeChat.setColumns(10);

		lvView = new JLabel("Lv. " + level);
		lvView.setFont(new Font("����", Font.BOLD, 15));
		lvView.setBounds(291, 116, 218, 22);
		add(lvView);

		JLabel id = new JLabel("ID " + userID);
		id.setFont(new Font("����", Font.BOLD, 15));
		id.setBounds(291, 76, 218, 20);
		add(id);

		coinView = new JLabel("�������� : " + coin);
		coinView.setFont(new Font("����", Font.BOLD, 15));
		coinView.setBounds(291, 95, 218, 22);
		add(coinView);

		play = new JButton("");
		play.setBackground(new Color(255, 222, 173));
		play.setBorderPainted(false);
		play.setIcon(new ImageIcon(HomePan.class.getResource("/img/runBtn.png")));
		play.setBounds(30, 76, 225, 145);
		add(play);

		lvup = new JButton("");
		lvup.setBackground(new Color(255, 222, 173));
		lvup.setBorderPainted(false);
		lvup.setIcon(new ImageIcon(HomePan.class.getResource("/img/lvupBtn.png")));
		lvup.setBounds(290, 319, 220, 60);
		add(lvup);

		rank = new JButton("");
		rank.setBackground(new Color(255, 222, 173));
		rank.setBorderPainted(false);
		rank.setIcon(new ImageIcon(HomePan.class.getResource("/img/rankBtn.png")));
		rank.setBounds(546, 76, 225, 312);
		add(rank);

		gookieIMG = new JLabel("");
		gookieIMG.setHorizontalAlignment(SwingConstants.CENTER);
		gookieIMG.setIcon(new ImageIcon(HomePan.class.getResource("/img/gookieBig.png")));
		gookieIMG.setBounds(260, 65, 280, 240);
		add(gookieIMG);

		lvCoin = new JLabel(level * 10 + " ���� �ҿ�");
		lvCoin.setHorizontalAlignment(SwingConstants.CENTER);
		lvCoin.setBounds(291, 378, 218, 31);
		add(lvCoin);

		logout = new JButton("");
		logout.setBorderPainted(false);
		logout.setForeground(new Color(255, 218, 185));
		logout.setBackground(new Color(255, 218, 185));
		logout.setIcon(new ImageIcon(HomePan.class.getResource("/img/logoutBtn.png")));
		logout.setFont(new Font("����", Font.PLAIN, 20));
		logout.setBounds(30, 19, 111, 26);
		add(logout);

		showChat = new JTextArea();
		chatScroll = new JScrollPane(showChat);
		chatScroll.setBounds(30, 419, 523, 135);
		add(chatScroll);

		inList = new JTextArea();
		onScroll = new JScrollPane(inList);
		onScroll.setBounds(564, 419, 207, 159);
		add(onScroll);

		lblNewLabel = new JLabel("[\uCC44\uD305\uCC3D]");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setFont(new Font("����", Font.BOLD, 15));
		lblNewLabel.setBounds(30, 398, 87, 22);
		add(lblNewLabel);

		lblNewLabel2 = new JLabel("[\uC811\uC18D\uC790 \uBAA9\uB85D]");
		lblNewLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel2.setFont(new Font("����", Font.BOLD, 15));
		lblNewLabel2.setBounds(563, 398, 207, 22);
		add(lblNewLabel2);

		play2 = new JButton("");
		play2.setIcon(new ImageIcon(HomePan.class.getResource("/img/play2.png")));
		play2.setBackground(new Color(255, 222, 173));
		play2.setBorderPainted(false);
		play2.setBounds(30, 234, 225, 145);
		add(play2);
		addChat(clientSet.getChat());
	}

	public void addChat(String chatList) {
		showChat.setText(chatList);
		// ä�� �����Ǹ� ��ũ�� �� �Ʒ��� ������
		showChat.setCaretPosition(showChat.getDocument().getLength());
	}

	private void addLis() {
		play.addActionListener(this);
		lvup.addActionListener(this);
		rank.addActionListener(this);
		logout.addActionListener(this);
		writeChat.addActionListener(this);
		sendChat.addActionListener(this);
		play2.addActionListener(this);
	}

	public boolean lvup() {
		if (coin >= (level * 10)) {
			coin = coin - (level * 10);
			level++;
			return true;
		}
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(play)) {
			out = true;
			gm.gameGo();
		} else if (e.getSource().equals(lvup)) {
			if (lvup()) {
				// ������ ��ü ���� �����صα� ���� �޽���
				String lvup = "levelUp/" + userID + "/" + coin + "/" + level;
				clientSet.send(lvup);

				gookieIMG.setIcon(new ImageIcon(HomePan.class.getResource("/img/lvup.png")));
				gm.setVisible(true);
				changeIMG();
				coinView.setText("�������� : " + coin);
				lvView.setText("Lv. " + level);
				lvCoin.setText("-" + level * 10 + " ����");
			} else {
				lvCoin.setForeground(Color.red);
				lvCoin.setText("������ �����մϴ�!");
			}
		} else if (e.getSource().equals(rank)) {
			out = true;
			gm.ranking();
		} else if (e.getSource().equals(logout)) {
			out = true;
			clientSet.setChat("");
			String msg = "logout/";
			clientSet.send(msg);
			clientSet.setUserID(null);
			System.out.println("�α׾ƿ� " + out);
			gm.home();
		} else if (e.getSource().equals(writeChat) || e.getSource().equals(sendChat)) {
			String msg = "chat/" + writeChat.getText();
			clientSet.send(msg);
			writeChat.setText("");
		}
	}

	private void changeIMG() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					gookieIMG.setIcon(new ImageIcon(HomePan.class.getResource("/img/gookieBig.png")));
					gm.setVisible(true);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	public JTextArea getShowChat() {
		return showChat;
	}

	public void setShowChat(JTextArea showChat) {
		this.showChat = showChat;
	}

	public JTextArea getInList() {
		return inList;
	}

	public void setInList(String inList) {
		this.inList.setText(inList);
	}
}
