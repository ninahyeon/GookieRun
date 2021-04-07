package game;

public class ItemAdd extends Thread {
	private Game game = null;
	private int time = 300;
	private int amount = 0;

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	ItemAdd(Game game) {
		this.game = game;
	}

	@Override
	public void run() {
		addItem();
	}

	private void addItem() {
		// 새로운 아이템 추가 주기 0.3초
		while (amount >= 0 && game.pb.getNow_power() >= 0) {
			try {
				game.itemadd();
				amount--;
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

}
