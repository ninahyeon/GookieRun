package dto;

import javax.swing.ImageIcon;

import game.Game;
import game.Main;

public class SpiderDTO extends GroundSuper {

	public SpiderDTO(Game game) {
		super(game);
		setLand(new ImageIcon(Main.class.getResource("../img/spider.png")).getImage());
		setY(600 - getLand().getHeight(null));
		setWhat("spi");
	}

	@Override
	public void hitRange() {
		setMonster_startX(getX() + 35);
		setMonster_endX(getX() + 320);
		setMonster_startY(getY() + 0);
		setMonster_endY(getY() + 500);
	}
}
