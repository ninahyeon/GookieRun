package jdbc;

import java.util.ArrayList;

public class RankDAO extends SuperDAO {
	private RankDAO() {
		super();
	}

	public static RankDAO getInstance() {
		return LazyHolder.IT;
	}

	private static class LazyHolder {
		private static final RankDAO IT = new RankDAO();
	}

	public void insert(RankDTO r) {
		if (getConn() != null) {
			try {
				String sql = "insert into ranking (id,point) values (?,?)";
				setPstmt(conn.prepareStatement(sql));
				getPstmt().setString(1, r.getId());
				getPstmt().setInt(2, r.getPoint());
				int rs = getPstmt().executeUpdate();
				System.out.println(rs + "건의 랭킹 업로드");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (conn != null)
					conn = null;
				if (getPstmt() != null)
					setPstmt(null);

			}
		}
	}

	public ArrayList<RankDTO> rank() {
		ArrayList<RankDTO> rList = new ArrayList<>();
		if (getConn() != null) {
			try {
				String sql = "select m.id, m.level, r.point from member m, ranking r where m.id=r.id order by point desc";
				setStmt(conn.createStatement());
				setRs(getStmt().executeQuery(sql));
				while (getRs().next()) {
					RankDTO r = new RankDTO();
					r.setId(getRs().getString("id"));
					r.setLevel(getRs().getInt("level"));
					r.setPoint(getRs().getInt("point"));
					rList.add(r);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (conn != null)
					conn = null;
				if (getStmt() != null)
					setStmt(null);
			}
		}
		return rList;
	}
}
