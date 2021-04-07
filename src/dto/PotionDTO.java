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
		// ���� ������ ü�� 10 ����!
		if (game.pb.getNow_power() + power <= game.pb.getMax_power()) {
			game.pb.setNow_power(game.pb.getNow_power() + power);
			// �� �������� ������ �Ŀ������� �� ����, �� �� ���� �������� �Ŀ��� 0���� �ٲ�
			power = 0;
		} else {
			game.pb.setNow_power(game.pb.getMax_power());
		}
	}
}
