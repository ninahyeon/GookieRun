package dto;

import javax.swing.ImageIcon;

import game.Game;
import game.Main;

public class GhostDTO extends GroundSuper {

	public GhostDTO(Game game) {
		super(game);
		setLand(new ImageIcon(Main.class.getResource("../img/ghost.png")).getImage());
		setY(600 - getLand().getHeight(null));
		setWhat("gho");
	}

	@Override
	public void hitRange() {
		setMonster_startX(getX() + 150);
		setMonster_endX(getX() + 210);
		setMonster_startY(getY() + 20);
		setMonster_endY(getY() + 115);
	}
}
