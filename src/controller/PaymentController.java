package controller;

import java.sql.SQLException;

import lombok.extern.slf4j.Slf4j;
import model.dao.PaymentLogsDAO;
import view.PaymentFailView;
import view.PaymentSuccessView;

@Slf4j
public class PaymentController {
	public static void startSystem() {

		log.info("간이 카드 결제 시스템 가동됨");

		try {
			// 무한 루프를 돌면서 결제 시뮬레이션 (3초 간격)
			while (true) {
				boolean r = PaymentLogsDAO.simulatePayment();
				if (r == true) {
					PaymentSuccessView.printMsg("DB 저장 성공");
					Thread.sleep(3000); // 3초 대기
				}else {
					PaymentFailView.printMsg("DB 저장이 정상적으로 완료되지 않았습니다. 다시 시도합니다.");
				}
			}
		} catch (InterruptedException e) {
			log.error("시스템 종료", e);
			PaymentFailView.printMsg(e.getMessage());
		} catch (SQLException e) {
			log.error("DB 예외 발생.", e);
			PaymentFailView.printMsg(e.getMessage());
		}
	}
}
