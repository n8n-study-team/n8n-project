// DBUtil 
// 	- DB 자원을 활용하기 위한 기본적인 준비를 담당합니다.(DB 설정파일 읽기, DB 자원 해제 등)
// 	- DB와 연결하는 로직이 아닙니다.

package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBUtil {
	private static Properties dbinfo = new Properties();
	
	private DBUtil() {}
	
	// mysql driver 로드
	static {
		try {
			dbinfo.load(new FileInputStream("dbinfo.properties"));	
			Class.forName(dbinfo.getProperty("jdbc.driver"));
			log.info("mysql driver 로드 성공");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// mysql DB 연동
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(
				dbinfo.getProperty("jdbc.url"),
				dbinfo.getProperty("jdbc.id"),
				dbinfo.getProperty("jdbc.pw"));
	}

	// DML(insert, delete, update) close
	public static void close(Connection con, Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (con != null) {
				con.close();
				con = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// DQL(select) close
	public static void close(Connection con, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (con != null) {
				con.close();
				con = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
