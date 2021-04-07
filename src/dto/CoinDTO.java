package dto;

import javax.swing.ImageIcon;

import game.Game;
import game.Main;

public class CoinDTO extends ItemSuper {
	private int coin = 3;

	public CoinDTO(Game game) {
		super(game);
		setItem(new ImageIcon(Main.class.getResource("../img/coin.png")).getImage());
	}

	@Override
	public void after_hit() {
		game.setCoin(game.getCoin() + coin);
		coin=0;
	}
}
