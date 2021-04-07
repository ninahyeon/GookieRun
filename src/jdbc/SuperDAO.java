package jdbc;

//DAO 중복 소스 관리하는 수퍼클래스
//멀티 스레드 환경에 최적화된 LazyHolder 싱글톤 처리
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SuperDAO {
	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final String DB_URL = "jdbc:mysql://localhost/gookie?&serverTimezone=UTC";
	private final String USER_NAME = "root";
	private final String PASSWORD = "1111";

	Connection conn = null;
	private PreparedStatement pstmt = null;
	private Statement stmt = null;
	private ResultSet rs = null;

	public PreparedStatement getPstmt() {
		return pstmt;
	}

	public void setPstmt(PreparedStatement pstmt) {
		this.pstmt = pstmt;
	}

	public Statement getStmt() {
		return stmt;
	}

	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

	SuperDAO() {
		// DB 인식
		try {
			Class.forName(JDBC_DRIVER);
			System.out.println("드라이버 로드 성공");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Connection getConn() {
		try {
			conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
			System.out.println("연결 성공");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

}
