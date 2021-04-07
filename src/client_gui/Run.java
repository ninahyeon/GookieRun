package client_gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.ClientSet;
import dto.GookieDTO;
import dto.GroundSuper;
import dto.ItemSuper;
import game.Game;
import game.Main;
import game.PowerBar;

public class Run extends JPanel implements ActionListener {
	public ClientSet clientSet = null;
	private PowerBar pb = null;

	private String userID;
	private int coin;
	private int level;

	private int power = 300;
	private GUIMain gm = null;
	private Image bimg;
	private Graphics buffg;
	private Game game = null;
	private GookieDTO goo = null;
	public ArrayList<GroundSuper> lList = new ArrayList<>(); // 지형리스트(일반 땅, 유령, 키 큰 유령, 거미)
	public ArrayList<ItemSuper> iList = null;

	// 게임 종료창
	private JLabel fp;
	private JInternalFrame itf;
	private JButton again;
	private JButton goBack;

	/**
	 * Create the panel.
	 * 
	 * @param m
	 * 
	 * @param gm
	 */
	public Run() {
		setLayout(null);
	}

	public void updatePan(GUIMain gm) {
		this.game = new Game(this);
		this.gm = gm;
		this.clientSet = ClientSet.getInstance();
		this.coin = clientSet.getCoin();
		this.level = clientSet.getLevel();
		this.userID = clientSet.getUserID();
		gm.addKeyListener(new key());
		gm.setFocusable(true);
		this.lList = game.lList;
		this.iList = game.iList;
		this.pb = game.pb;
		goo = game.goo;

		// 0.05초에 한 번씩 화면 업데이트
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(50);
						power = (int) (300 * pb.getP_per() / 100);
						repaint();
						if (pb.getNow_power() <= 0) {
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				gameOver();
			}
		}).start();
	}

	private void gameOver() {
		power = -1;

		clientSet.setCoin(clientSet.getCoin() + game.getCoin());

		String gg = "gg/" + userID + "/" + clientSet.getLevel() + "/" + game.getPoint() + "/" + clientSet.getCoin();
		// 서버로 랭킹 및 코인 업데이트 객체 정보 전송
		clientSet.send(gg);

		// 죽은 국희로 이미지 변경
		goo.setGookie(new ImageIcon(Main.class.getResource("../img/goo_dead.png")).getImage());
		repaint();
		addButton();
	}

	private void addButton() {
		// 인터널 프레임 생성
		itf = new JInternalFrame("GameOver", false, false, false, false); // 크기조절 및 닫기 버튼 모두 사용 안 함
		itf.setBounds(200, 200, 350, 200);
		itf.setLayout(null);

		// 최종 점수 표시
		fp = new JLabel("최종점수 : " + game.getPoint() + "점");
		fp.setFont(new Font("굴림", Font.BOLD, 20));
		fp.setBounds(100, 50, 200, 25);
		itf.add(fp);

		// 홈으로 돌아가기 버튼 생성
		goBack = new JButton("홈으로");
		goBack.setFont(new Font("굴림", Font.BOLD, 15));
		goBack.setBounds(110, 90, 100, 30);
		itf.add(goBack);

		// 다시하기 버튼 생성
		again = new JButton("한 판 더");
		again.setFont(new Font("굴림", Font.BOLD, 15));
		again.setBounds(110, 130, 100, 30);
		itf.add(again);

		itf.setVisible(true);
		add(itf);
		gm.setVisible(true);
		addLis();
	}

	private void addLis() {
		goBack.addActionListener(this);
		again.addActionListener(this);
	}

	@Override
	public void paint(Graphics g) {
		if (buffg == null) {
			bimg = createImage(800, 600);
			if (bimg == null)
				System.out.println("더블버퍼링 실패");
			else
				buffg = bimg.getGraphics();
		}
		update(g);
	}

	@Override
	public void update(Graphics g) {
		buffg.clearRect(0, 0, 800, 600);
		Image bg = new ImageIcon(Main.class.getResource("../img/background.png")).getImage();
		buffg.drawImage(bg, 0, 0, null);
		buffg.setColor(Color.black);
		if (game != null) {
			if (game.cdwn != null) {
				Image cnt = game.cdwn.getCnt();
				if (cnt != null) {
					buffg.drawImage(cnt, 350, 200, null);
				} else {
					pb.start();
					game.cdwn = null;
				}
			}
			if (lList.size() > 0) {
				for (int i = 0; i < lList.size(); i++) {
					GroundSuper ls = lList.get(i);
					if (ls.getX() > ls.getLand().getWidth(null) * -1)
						buffg.drawImage(ls.getLand(), ls.getX(), ls.getY(), null);
				}
			}
			if (iList.size() > 0) {
				for (int i = 0; i < iList.size(); i++) {
					ItemSuper it = iList.get(i);
					if (it.getX() > it.getItem().getWidth(null) * -1)
						buffg.drawImage(it.getItem(), it.getX(), it.getY(), null);
				}
			}
		}
		buffg.setFont(new Font("굴림", Font.BOLD, 20));
		buffg.drawString(game.getPoint() + "점", 700, 25);
		buffg.drawRect(20, 20, 310, 30);
		buffg.setColor(Color.red);
		buffg.fillRect(25, 25, power, 20);
		if (goo != null) {
			buffg.drawImage(goo.getGookie(), goo.getX(), goo.getY(), null);
		}
		g.drawImage(bimg, 0, 0, this);
	}

	private class key implements KeyListener {
		int doubleJump = 0;

		@Override
		public void keyPressed(KeyEvent e) {
			// 바닥에 있을 때 ↓방향키 누르면 납작해짐
			if (goo.getY() == 447 && e.getKeyCode() == 40 && !goo.isHit()) { // 점프하고 있을 때/장애물에 부딪혀 깜빡일 때는 납작해지지 않도록 함
				goo.setGookie(new ImageIcon(Main.class.getResource("../img/goo_slide.png")).getImage());
				goo.setY(540 - goo.getGookie().getHeight(null));
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// 중력점프와 더블점프
			if (e.getKeyCode() == 32 && doubleJump < 2) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							doubleJump++;
							for (int i = -6; i <= 6; i++) {
								if (goo.getY() + (i * 8) <= 447) {
									goo.setY(goo.getY() + (i * 8));
									Thread.sleep(110);
									if (doubleJump == 2) {
										douJ();
										break;
									}
								}
							}
							goo.setY(447);
							if (goo.getY() == 447) {
								doubleJump = 0;
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					private void douJ() {
						try {
							for (int j = -4; j <= 8; j++) {
								if (goo.getY() + (j * 8) <= 447) {
									goo.setY(goo.getY() + (j * 8));
									Thread.sleep(110);
								} else {
									break;
								}
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();
			} else if (e.getKeyCode() == 40) {
				// ↓방향키 떼면 원상복구
				goo.setGookie(new ImageIcon(Main.class.getResource("../img/gookie.png")).getImage());
				goo.setY(447);
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(goBack)) {
			gm.gameHome();
		} else if (e.getSource().equals(again)) {
			gm.gameGo();
		}
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

}
