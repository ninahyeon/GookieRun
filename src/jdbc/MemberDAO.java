package jdbc;

import java.sql.SQLException;
import java.util.ArrayList;

//멀티스레드 환경에 최적화된 레이지홀더 싱글톤 사용.
public class MemberDAO extends SuperDAO {
	private MemberDAO() {
		super();
	}

	public static MemberDAO getInstance() {
		return LazyHolder.IT;
	}

	private static class LazyHolder {
		private static final MemberDAO IT = new MemberDAO();
	}

	public void coinUpdate(MemberDTO m) {
		if (getConn() != null) {
			try {
				String sql = "update member set coin=? where id=?";
				setPstmt(conn.prepareStatement(sql));
				getPstmt().setInt(1, m.getCoin());
				getPstmt().setString(2, m.getId());
				getPstmt().executeUpdate();
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

	public int levelUp(MemberDTO m) {
		int rs = 0;
		if (getConn() != null) {
			try {
				String sql = "update member set coin = ?, level =? where id = ?";
				setPstmt(conn.prepareStatement(sql));
				getPstmt().setInt(1, m.getCoin());
				getPstmt().setInt(2, m.getLevel());
				getPstmt().setString(3, m.getId());
				rs = getPstmt().executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (conn != null)
					conn = null;
				if (getPstmt() != null)
					setPstmt(null);

			}
		}
		return rs;
	}

	public ArrayList<MemberDTO> selectAll() {
		ArrayList<MemberDTO> mList = new ArrayList<>();
		if (getConn() != null) {
			try {
				String sql = "select * from member";
				setStmt(conn.createStatement());
				setRs(getStmt().executeQuery(sql));
				while (getRs().next()) {
					MemberDTO m = new MemberDTO();
					m.setId(getRs().getString("id"));
					m.setPw(getRs().getString("pw"));
					m.setCoin(getRs().getInt("coin"));
					m.setLevel(getRs().getInt("level"));
					mList.add(m);
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
		return mList;
	}

	public MemberDTO selectOne(String id) {
		MemberDTO m = new MemberDTO();
		if (getConn() != null) {
			try {
				String sql = "select * from member where id = ?";
				setPstmt(conn.prepareStatement(sql));
				getPstmt().setString(1, id);
				setRs(getPstmt().executeQuery());
				if (getRs().next()) {
					m.setId(getRs().getString("id"));
					m.setPw(getRs().getString("pw"));
					m.setCoin(getRs().getInt("coin"));
					m.setLevel(getRs().getInt("level"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (conn != null)
					conn = null;
				if (getPstmt() != null)
					setPstmt(null);
			}
		}
		return m;
	}

	public int insert(MemberDTO m) {
		int rs = -1;
		if (getConn() != null) {
			try {
				String sql = "insert into member (id,pw,coin,level) values (?,?,?,?)";
				setPstmt(conn.prepareStatement(sql));
				getPstmt().setString(1, m.getId());
				getPstmt().setString(2, m.getPw());
				getPstmt().setInt(3, m.getCoin());
				getPstmt().setInt(4, m.getLevel());
				rs = getPstmt().executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (conn != null)
					conn = null;
				if (getPstmt() != null)
					setPstmt(null);

			}
		}
		return rs;
	}
}
