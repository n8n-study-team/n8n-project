// PaymentLogsDAO
// 	- DB와 연결하고 쿼리를 수행하는 역할만 하는 클래스입니다.
// 	- 비즈니스 로직을 담당하는 클래스가 아닙니다.

package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

import util.DBUtil;
import model.domain.RanDataDTO;

@Slf4j
public class PaymentLogsDAO {

	// 랜덤 거래 내역을 만들어 DB에 적재하는 메서드
	public static boolean simulatePayment(RanDataDTO ranData) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DBUtil.getConnection();

			// DB에 로그 적재 (INSERT)
			String insertQuery = "INSERT INTO payment_logs (card_number, amount, merchant_name, status, message) VALUES (?, ?, ?, ?, ?)";

			pstmt = con.prepareStatement(insertQuery);
			pstmt.setString(1, ranData.getCardNum());
			pstmt.setInt(2, ranData.getAmount());
			pstmt.setString(3, ranData.getMerchant());
			pstmt.setString(4, ranData.getStatus());
			pstmt.setString(5, ranData.getMessage()); // 에러 메시지 or 성공 메시지

			// 쿼리 실행 후 리턴 값이 1이면 성공, 0이면 예외 발생
			int queryResult = pstmt.executeUpdate();

			if (queryResult == 1) {
				if (ranData.isFail()) {
					log.warn("🚨 [결제실패] {}원 / 사유: {}", ranData.getAmount(), ranData.getErrorCode());
				} else {
					log.info("✅ [결제성공] {}원 / 가맹점: {}", ranData.getAmount(), ranData.getErrorCode());
				}

				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e; // 이 메소드 호출한 곳으로 예외 던진다는 의미
		} finally {
			// 자원 해제
			DBUtil.close(con, pstmt);
			log.info("DB 자원 해제");
		}

		return false;
	}
}