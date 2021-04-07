package game;

import client_gui.Run;

public class PowerBar extends Thread {
	private int level;
	private double max_power = 0;
	private double now_power = 0;
	private double p_per = 0;

	public PowerBar(Run run) {
		// 레벨 1의 체력은 50, 레벨업마다 10씩 체력 증가
		this.level = run.getLevel();
		max_power = level * 10 + 40;
		now_power = max_power;
	}

	@Override
	public void run() {
		while (now_power >= -5) {
			try {
				// 1초에 체력 1씩 깎임
				now_power--;
				if (now_power <= 0) {
					p_per = 0;
				}
				p_per = now_power / max_power * 100;
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public double getP_per() {
		return p_per;
	}

	public void setP_per(double p_per) {
		this.p_per = p_per;
	}

	public double getNow_power() {
		return now_power;
	}

	public void setNow_power(double now_power) {
		this.now_power = now_power;
	}

	public double getMax_power() {
		return max_power;
	}

	public void setMax_power(double max_power) {
		this.max_power = max_power;
	}

}
