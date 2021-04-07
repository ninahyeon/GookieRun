package dto;

import javax.swing.ImageIcon;

import game.Game;
import game.Main;

public class PotionDTO extends ItemSuper {
	private int power = 10;

	public PotionDTO(Game game) {
		super(game);
		setItem(new ImageIcon(Main.class.getResource("../img/potion.png")).getImage());
	}

	@Override
	public void after_hit() {
		// 포션 먹으면 체력 10 증가!
		if (game.pb.getNow_power() + power <= game.pb.getMax_power()) {
			game.pb.setNow_power(game.pb.getNow_power() + power);
			// 한 아이템이 여러번 파워업해줄 것 방지, 한 번 맞은 아이템은 파워를 0으로 바꿈
			power = 0;
		} else {
			game.pb.setNow_power(game.pb.getMax_power());
		}
	}
}
