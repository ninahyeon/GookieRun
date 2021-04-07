package dto;

import java.awt.Image;

import javax.swing.ImageIcon;

import game.Game;
import game.Main;

public abstract class GroundSuper extends Thread {
	private Game game = null;
	private Image land = null;
	private int x = 800;
	private int y = 375;
	private String what = null; // � �������� �����ϴ� ����(�Ϲ� ��, ����, �Ź� ��)

	private int monster_startX = 0;
	private int monster_endX = 0;
	private int monster_startY = 0;
	private int monster_endY = 0;

	private int power = 0;

	GroundSuper(Game game) {
		this.game = game;
		power = game.clientSet.getLevel() * 5;
	}

	// 0.06�ʿ� x�� -10�� �̵�!
	@Override
	public void run() {
		try {
			while (game.pb.getNow_power() >= 0) {
				Thread.sleep(60);
				x = x - 10;
				// �̵��� ������ ����� �ε������� Ȯ��
				if (!game.goo.isHit()) { // ������ hit ������ false �϶��� �ε��� ������ �ϵ��� �����ߴ�.
					hit();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ���� ��ֹ��� �ε����� ��!
	public void hit() {
		hitRange();
		Image goo = game.goo.getGookie();
		int gooY = game.goo.getY() + (goo.getHeight(null) / 2); // ���� ���� ��� ��
		int leftX = game.goo.getX(); // ���� ���� x��
		int rightX = game.goo.getX() + goo.getWidth(null); // ���� �� x��
		if (gooY >= monster_startY && gooY <= monster_endY) {
			if ((leftX >= monster_startX && leftX <= monster_endX)
					|| (rightX >= monster_startX && rightX <= monster_endX)) {
				game.pb.setNow_power(game.pb.getNow_power() - power);
				trans();
			}
		}
	}

	private void trans() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int blink = 0;
				// �ε����� �� gookie�� hit ������ true�� ��������
				game.goo.setHit(true);
				try {
					while (blink < 5) {
						// ���� �����Ÿ����� ¦��/Ȧ�� �̹��� ����
						if (blink % 2 == 0) {
							game.goo.setGookie(
									new ImageIcon(Main.class.getResource("../img/attacked_clear.png")).getImage());
						} else {
							game.goo.setGookie(new ImageIcon(Main.class.getResource("../img/attacked.png")).getImage());
						}
						blink++;
						Thread.sleep(300);
					}
					game.goo.setHit(false); // ������ hit �� �ٽ� false�� ���� �� �̹��� �����·�
					game.goo.setGookie(new ImageIcon(Main.class.getResource("../img/gookie.png")).getImage());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	abstract public void hitRange();

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public Image getLand() {
		return land;
	}

	public void setLand(Image land) {
		this.land = land;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getWhat() {
		return what;
	}

	public void setWhat(String what) {
		this.what = what;
	}

	public int getMonster_startX() {
		return monster_startX;
	}

	public void setMonster_startX(int monster_startX) {
		this.monster_startX = monster_startX;
	}

	public int getMonster_endX() {
		return monster_endX;
	}

	public void setMonster_endX(int monster_endX) {
		this.monster_endX = monster_endX;
	}

	public int getMonster_startY() {
		return monster_startY;
	}

	public void setMonster_startY(int monster_startY) {
		this.monster_startY = monster_startY;
	}

	public int getMonster_endY() {
		return monster_endY;
	}

	public void setMonster_endY(int monster_endY) {
		this.monster_endY = monster_endY;
	}

}
