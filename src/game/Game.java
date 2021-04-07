package game;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

import client.ClientSet;
import client_gui.Run;
import dto.CoinDTO;
import dto.CookieDTO;
import dto.GhostDTO;
import dto.GookieDTO;
import dto.GroundSuper;
import dto.ItemSuper;
import dto.LandDTO;
import dto.PotionDTO;
import dto.SpiderDTO;
import dto.TallGho;

public class Game {
	public GroundAdd ga = null;
	public ItemAdd ia = null;
	public ArrayList<GroundSuper> lList = new ArrayList<>(); // 지형리스트(일반 땅, 유령, 키 큰 유령, 거미)
	public ArrayList<ItemSuper> iList = new ArrayList<>(); // 아이템리스트
	public Countdwn cdwn = null;
	public GookieDTO goo = null;
	public Run run = null;
	public PowerBar pb = null;
	public ClientSet clientSet;

	// 게임중 획득 아이템 변수
	private int coin = 0;
	private int point = 0;

	// 지형 컨트롤 변수
	private Random r = new Random();
	private int ran = -1;
	private int land = 0;

	// 아이템으로 포물선 그리기위한 변수
	private int gr = -50;
	private int savegr = 0;
	private int tr = -90;
	private int savetr = 0;

	public Game(Run run) {
		this.run = run;
		this.clientSet = ClientSet.getInstance();
		init();
		gameGo();
	}

	private void gameGo() {
		ga.start();
//		ia.start();
		cdwn.start();
	}

	private void init() {
		this.pb = new PowerBar(run);
		ga = new GroundAdd(this);
		cdwn = new Countdwn();
		goo = new GookieDTO();
	}

	// 지나간 객체들은 지우기
	private void delOb() {
		// 새로운 땅 객체 생성마다 for문 돌리기엔 너무 무거울 것 같아서
		// 리스트의 첫번째 객체만 반복적으로 삭제.
		if (lList.get(0).getX() <= lList.get(0).getLand().getWidth(null) * -1) {
			lList.remove(0);
		}
		if (iList.size() > 0) {
			if (iList.get(0).getX() <= -40) {
				iList.remove(0);
			}
		}
	}

	// 땅 객체 & 유령 객체 생성
	public void groundAdd() {
		GroundSuper l = null;
		int k = r.nextInt(4);
		if (land == 2) {
			// 지형 생성마다 아이템 생성 스레드 돌리기
			ia = new ItemAdd(this);
			// 지형마다 아이템 나오는 갯수 다르게 조정
			if (k == 1) {
				ia.setAmount(9);
			} else if (k == 2) {
				ia.setAmount(10);
			} else if (k == 3) {
				ia.setAmount(5);
			}
			landinit();
			ia.start();
			ran = k;
		}
		if (land <= 1 || ran <= 0) {
			// 일반 땅
			l = new LandDTO(this);
			// 맨 처음 시작할 때
			if (land <= 1) {
				l.setLand(new ImageIcon(Main.class.getResource("../img/first.png")).getImage());
				l.setX(0);
			}
		} else if (ran == 1) {
			// 유령
			l = new GhostDTO(this);
		} else if (ran == 2) {
			// 키 큰 유령
			l = new TallGho(this);
		} else if (ran == 3) {
			// 거미
			l = new SpiderDTO(this);
		}
		land++;
		if (land == 3) {
			land = 2;
		}
		l.start();
		lList.add(l);
		delOb();
	}

	public void itemadd() {
		ItemSuper it = null;
		int k = r.nextInt(100);
		if (k == 0) {
			it = new PotionDTO(this);
		} else if (k >= 1 && k <= 50) {
			it = new CoinDTO(this);
		} else if (k > 50) {
			it = new CookieDTO(this);
		}
		itemDisplay(it);
		it.start();
		iList.add(it);
	}

	// 생성된 아이템의 y좌표를 지형에 따라 설정하는 메서드
	private void itemDisplay(ItemSuper it) {
		for (int i = 0; i < lList.size(); i++) {
			GroundSuper l = lList.get(i);
			if (l.getWhat().equals("gho")) {
				if (it.getX() >= l.getX() + 120 && it.getX() <= l.getX() + 240) {
					// 범위에 유령이 있으면
					ghoRound(it);
					break;
				}
			} else if (l.getWhat().equals("spi")) {
				if (it.getX() >= l.getX() && it.getX() <= l.getX() + 360) {
					// 범위에 거미가 있으면
					it.setY(499);
					break;
				}
			} else if (l.getWhat().equals("tal")) {
				if (it.getX() >= l.getX() + 80 && it.getX() <= l.getX() + 240) {
					// 범위에 키 큰 유령이 있으면
					talGround(it);
					break;
				}
			}
		}
	}

	private void landinit() {
		gr = -50;
		savegr = 0;
		tr = -90;
		savetr = 0;
		ia.setTime(300); // 아이템 생성 주기 원상복구
	}

	// 키 큰 유령
	private void talGround(ItemSuper it) {
		ia.setTime(110); // 아이템 생성을 더 빠르게
		if (tr < 90) {
			savetr = savetr + tr;
			it.setY(it.getY() + savetr);
			tr = tr + 20;
		} else {
			landinit();
		}
	}

	// 유령
	private void ghoRound(ItemSuper it) {
		ia.setTime(110);
		if (gr < 50) {
			savegr = savegr + gr;
			it.setY(it.getY() + savegr);
			gr = gr + 20;
		} else {
			landinit();
		}
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

}
