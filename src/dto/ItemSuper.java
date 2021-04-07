package dto;

import java.awt.Image;

import javax.swing.ImageIcon;

import game.Game;
import game.Main;

public abstract class ItemSuper extends Thread {
	public Game game = null;
	private GookieDTO goo = null;
	private Image item = null;
	private int x = 800;
	private int y = 450;
	private int point = 0;

	public ItemSuper(Game game) {
		this.game = game;
		this.goo = game.goo;
	}

	@Override
	public void run() {
		try {
			while (game.pb.getNow_power() >= 0) {
				x = x - 10;
				if (!goo.isHit()) {
					hit();
				}
				Thread.sleep(60);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// 아이템이 국희 좌표 안에 포함 되었을 때!
	public void hit() {
		int gooH = goo.getGookie().getHeight(null);
		int gooW = goo.getGookie().getWidth(null);
		int gooX = goo.getX();
		int gooY = goo.getY();
		if (y >= gooY && y <= gooY + gooH && x >= gooX && x <= gooX + gooW) {
			// 먹은 아이템은 별모양으로 바뀜
			item = new ImageIcon(Main.class.getResource("../img/star.png")).getImage();
			after_hit();
		}
	}

	abstract public void after_hit();

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public Image getItem() {
		return item;
	}

	public void setItem(Image item) {
		this.item = item;
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

}
