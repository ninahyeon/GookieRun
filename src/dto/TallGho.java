package dto;

import javax.swing.ImageIcon;

import game.Game;
import game.Main;

public class TallGho extends GroundSuper {

	public TallGho(Game game) {
		super(game);
		setLand(new ImageIcon(Main.class.getResource("../img/tallGho.png")).getImage());
		setY(600 - getLand().getHeight(null));
		setWhat("tal");
	}

	@Override
	public void hitRange() {
		setMonster_startX(getX() + 155);
		setMonster_endX(getX() + 205);
		setMonster_startY(getY() + 20);
		setMonster_endY(getY() + 250);
	}
}
