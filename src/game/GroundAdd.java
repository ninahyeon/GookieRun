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
		// ���ο� ���� �߰� �ֱ� 1.8��
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
