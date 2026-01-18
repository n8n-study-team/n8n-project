package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

import lombok.extern.slf4j.Slf4j;
import model.domain.PaymentLogsDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

import util.DBUtil;
import util.GenRandom;

@Slf4j
public class PaymentLogsDAO {
	
	// payment_logs 테이블의 모든 행을 가져오는 메서드(SELECT * FROM payment_logs)
	public static ArrayList<PaymentLogsDTO> paymentAll() throws SQLException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		ArrayList<PaymentLogsDTO> allPayment = null;

		try {
			con = DBUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM dept");

			allPayment = new ArrayList<PaymentLogsDTO>();
			
			while (rs.next()) {
				allPayment.add(new PaymentLogsDTO(
						rs.getInt("log_id"), 
						rs.getString("card_number"), 
						rs.getBigDecimal("amount"),
						rs.getString("merchant_name"),
						rs.getString("status"),
						rs.getString("message"),
						rs.getObject("created_at", LocalDateTime.class),
						rs.getBoolean("is_alerted")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e; // 이 메소드 호출한 곳으로 예외 던진다는 의미
		} finally {
			DBUtil.close(con, stmt, rs);
			log.info("paymentAll 메서드에서 DB 자원 해제");
		}

		return allPayment;
	}
	
	// 랜덤 거래 내역을 만들어 DB에 적재하는 메서드
	public static void simulatePayment() throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBUtil.getConnection();

            // 랜덤 데이터 생성
            String cardNum = GenRandom.generateRandomCardNumber();
            int amount = (GenRandom.random.nextInt(100) + 1) * 1000; // 1,000 ~ 100,000원
            String merchant = GenRandom.getRandomMerchant();
            
            // 고의적인 에러 발생 로직 (20% 확률로 실패)
            boolean isFail = GenRandom.random.nextInt(5) == 0; // 0~4 중 0이 나오면 실패
            
            String status = isFail ? "FAIL" : "SUCCESS";
            String errorCode = isFail ? GenRandom.getRandomErrorCode() : null;
            String message = isFail ? "결제 승인 거절됨 (" + errorCode + ")" : "결제 정상 승인";

            // DB에 로그 적재 (INSERT)
            String insertQuery = "INSERT INTO payment_logs (card_number, amount, merchant_name, status, message) VALUES (?, ?, ?, ?, ?)";
            
            pstmt = con.prepareStatement(insertQuery);
            pstmt.setString(1, cardNum);
            pstmt.setInt(2, amount);
            pstmt.setString(3, merchant);
            pstmt.setString(4, status);
            pstmt.setString(5, message); // 에러 메시지 or 성공 메시지
            
            // 쿼리 실행 후 리턴 값이 1이면 성공, 0이면 예외 발생
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                if (isFail) {
                    log.warn("🚨 [결제실패] {}원 / 사유: {}", amount, errorCode);
                } else {
                    log.info("✅ [결제성공] {}원 / 가맹점: {}", amount, merchant);
                }
            }

        } catch (SQLException e) {
        		e.printStackTrace();
			throw e; // 이 메소드 호출한 곳으로 예외 던진다는 의미
        } finally {
        		// 자원 해제
        		DBUtil.close(con, pstmt);
        		log.info("simulatePayment 메서드에서 DB 자원 해제");
        }
    }
}