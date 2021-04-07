package dto;

import javax.swing.ImageIcon;

import game.Game;
import game.Main;

public class CookieDTO extends ItemSuper {
	private int point = 5;

	public CookieDTO(Game game) {
		super(game);
		setItem(new ImageIcon(Main.class.getResource("../img/cookie.png")).getImage());
	}

	@Override
	public void after_hit() {
		game.setPoint(game.getPoint() + point);
		point = 0;
	}
}
