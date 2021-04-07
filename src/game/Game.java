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
	public ArrayList<GroundSuper> lList = new ArrayList<>(); // ��������Ʈ(�Ϲ� ��, ����, Ű ū ����, �Ź�)
	public ArrayList<ItemSuper> iList = new ArrayList<>(); // �����۸���Ʈ
	public Countdwn cdwn = null;
	public GookieDTO goo = null;
	public Run run = null;
	public PowerBar pb = null;
	public ClientSet clientSet;

	// ������ ȹ�� ������ ����
	private int coin = 0;
	private int point = 0;

	// ���� ��Ʈ�� ����
	private Random r = new Random();
	private int ran = -1;
	private int land = 0;

	// ���������� ������ �׸������� ����
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

	// ������ ��ü���� �����
	private void delOb() {
		// ���ο� �� ��ü �������� for�� �����⿣ �ʹ� ���ſ� �� ���Ƽ�
		// ����Ʈ�� ù��° ��ü�� �ݺ������� ����.
		if (lList.get(0).getX() <= lList.get(0).getLand().getWidth(null) * -1) {
			lList.remove(0);
		}
		if (iList.size() > 0) {
			if (iList.get(0).getX() <= -40) {
				iList.remove(0);
			}
		}
	}

	// �� ��ü & ���� ��ü ����
	public void groundAdd() {
		GroundSuper l = null;
		int k = r.nextInt(4);
		if (land == 2) {
			// ���� �������� ������ ���� ������ ������
			ia = new ItemAdd(this);
			// �������� ������ ������ ���� �ٸ��� ����
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
			// �Ϲ� ��
			l = new LandDTO(this);
			// �� ó�� ������ ��
			if (land <= 1) {
				l.setLand(new ImageIcon(Main.class.getResource("../img/first.png")).getImage());
				l.setX(0);
			}
		} else if (ran == 1) {
			// ����
			l = new GhostDTO(this);
		} else if (ran == 2) {
			// Ű ū ����
			l = new TallGho(this);
		} else if (ran == 3) {
			// �Ź�
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

	// ������ �������� y��ǥ�� ������ ���� �����ϴ� �޼���
	private void itemDisplay(ItemSuper it) {
		for (int i = 0; i < lList.size(); i++) {
			GroundSuper l = lList.get(i);
			if (l.getWhat().equals("gho")) {
				if (it.getX() >= l.getX() + 120 && it.getX() <= l.getX() + 240) {
					// ������ ������ ������
					ghoRound(it);
					break;
				}
			} else if (l.getWhat().equals("spi")) {
				if (it.getX() >= l.getX() && it.getX() <= l.getX() + 360) {
					// ������ �Ź̰� ������
					it.setY(499);
					break;
				}
			} else if (l.getWhat().equals("tal")) {
				if (it.getX() >= l.getX() + 80 && it.getX() <= l.getX() + 240) {
					// ������ Ű ū ������ ������
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
		ia.setTime(300); // ������ ���� �ֱ� ���󺹱�
	}

	// Ű ū ����
	private void talGround(ItemSuper it) {
		ia.setTime(110); // ������ ������ �� ������
		if (tr < 90) {
			savetr = savetr + tr;
			it.setY(it.getY() + savetr);
			tr = tr + 20;
		} else {
			landinit();
		}
	}

	// ����
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
