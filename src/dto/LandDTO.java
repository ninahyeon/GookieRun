package dto;

import javax.swing.ImageIcon;

import game.Game;
import game.Main;

public class LandDTO extends GroundSuper {

	public LandDTO(Game game) {
		super(game);
		setLand(new ImageIcon(Main.class.getResource("../img/ground.png")).getImage());
		setY(600 - getLand().getHeight(null));
		setWhat("lan");
	}

	@Override
	public void hitRange() {
		setMonster_startX(0);
		setMonster_endX(0);
		setMonster_startY(0);
		setMonster_endY(0);
	}

}
