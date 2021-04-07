package game;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Countdwn extends Thread {
	private Image cnt = null;

	@Override
	public void run() {
		cntDown();
	}

	private void cntDown() {
		// 맨 처음 카운트 다운 3초
		try {
			for (int i = 3; i >= 0; i--) {
				if (i == 3) {
					cnt = new ImageIcon(Main.class.getResource("../img/3.png")).getImage();
					System.out.println(i);
				} else if (i == 2) {
					cnt = new ImageIcon(Main.class.getResource("../img/2.png")).getImage();
					System.out.println(i);
				} else if (i == 1) {
					cnt = new ImageIcon(Main.class.getResource("../img/1.png")).getImage();
					System.out.println(i);
				} else if (i == 0) {
					cnt = null;
				}
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public Image getCnt() {
		return cnt;
	}

	public void setCnt(Image cnt) {
		this.cnt = cnt;
	}
}
