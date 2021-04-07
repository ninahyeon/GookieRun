package dto;

import java.awt.Image;

import javax.swing.ImageIcon;

import game.Main;

public class GookieDTO {
	private Image gookie = new ImageIcon(Main.class.getResource("../img/gookie.png")).getImage();
	private int x = 100;
	private int y = 447;
	private boolean hit = false;

	public boolean isHit() {
		return hit;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}

	public Image getGookie() {
		return gookie;
	}

	public void setGookie(Image gookie) {
		this.gookie = gookie;
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
