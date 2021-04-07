package game;

public class GroundAdd extends Thread {
	private Game game = null;

	GroundAdd(Game game) {
		this.game = game;
	}

	@Override
	public void run() {
		addGround();
	}

	private void addGround() {
		// 새로운 지형 추가 주기 1.8초
		while (game.pb.getNow_power() >= 0) {
			try {
				game.groundAdd();
				Thread.sleep(2100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
