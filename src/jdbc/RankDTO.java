package jdbc;

import java.io.Serializable;

public class RankDTO implements Serializable {
	private String id = null;
	private int point = 0;
	private int level = 0;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String prt() {
		String prt = id + "\t" + level + "\t" + point;
		return prt;
	}

}
