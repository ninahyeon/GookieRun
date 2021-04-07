package jdbc;

//DAO �ߺ� �ҽ� �����ϴ� ����Ŭ����
//��Ƽ ������ ȯ�濡 ����ȭ�� LazyHolder �̱��� ó��
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
		// DB �ν�
		try {
			Class.forName(JDBC_DRIVER);
			System.out.println("����̹� �ε� ����");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Connection getConn() {
		try {
			conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
			System.out.println("���� ����");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

}
