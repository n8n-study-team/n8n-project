import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CardPaymentSystem {

    // 1. Docker MySQL 접속 정보 (호스트에서 접속하므로 localhost)
	// 테스트 용도이므로 여기에 작성함, 링크 및 user, pw 반드시 수정할 것
    private static final String DB_URL = "jdbc:mysql://localhost:3307/fisa_card_db?serverTimezone=Asia/Seoul&characterEncoding=UTF-8";
    private static final String DB_USER = "fisa";
    private static final String DB_PW = "fisa123";

    // 로깅 (Console 출력용)
    private static final Logger logger = LoggerFactory.getLogger(CardPaymentSystem.class);
    private static final Random random = new Random();

    public static void main(String[] args) {
        logger.info("======= 간이 카드 결제 시스템 가동 =======");

        try {
            // 무한 루프를 돌면서 결제 시뮬레이션 (3초 간격)
            while (true) {
                simulatePayment();
                Thread.sleep(3000); // 3초 대기
            }
        } catch (InterruptedException e) {
            logger.error("시스템 종료", e);
        }
    }

    private static void simulatePayment() {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PW);

            // 2. 랜덤 데이터 생성
            String cardNum = generateRandomCardNumber();
            int amount = (random.nextInt(100) + 1) * 1000; // 1,000 ~ 100,000원
            String merchant = getRandomMerchant();
            
            // 3. 고의적인 에러 발생 로직 (20% 확률로 실패)
            boolean isFail = random.nextInt(5) == 0; // 0~4 중 0이 나오면 실패
            
            String status = isFail ? "FAIL" : "SUCCESS";
            String errorCode = isFail ? getRandomErrorCode() : null;
            String message = isFail ? "결제 승인 거절됨 (" + errorCode + ")" : "결제 정상 승인";

            // 4. DB에 로그 적재 (INSERT)
            String sql = "INSERT INTO payment_logs (card_number, amount, merchant_name, status, message) VALUES (?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cardNum);
            pstmt.setInt(2, amount);
            pstmt.setString(3, merchant);
            pstmt.setString(4, status);
            pstmt.setString(5, message); // 에러 메시지 or 성공 메시지

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                if (isFail) {
                    logger.warn("🚨 [결제실패] {}원 / 사유: {}", amount, errorCode);
                } else {
                    logger.info("✅ [결제성공] {}원 / 가맹점: {}", amount, merchant);
                }
            }

        } catch (SQLException e) {
            logger.error("DB 연결 또는 쿼리 실행 중 오류 발생", e);
        } finally {
            // 자원 해제
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // --- 아래는 랜덤 데이터 생성을 위한 도우미 메서드들 ---

    private static String generateRandomCardNumber() {
        return String.format("%04d-****-****-%04d", 
                random.nextInt(9000) + 1000, 
                random.nextInt(9000) + 1000);
    }

    private static String getRandomMerchant() {
        String[] merchants = {"스타벅스", "GS25", "쿠팡", "배달의민족", "카카오택시", "FISA구내식당"};
        return merchants[random.nextInt(merchants.length)];
    }

    private static String getRandomErrorCode() {
        String[] errors = {"잔액부족", "한도초과", "도난분실카드", "통신시간초과", "CVC오류"};
        return errors[random.nextInt(errors.length)];
    }
}