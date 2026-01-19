// PaymentService
// 	- 비즈니스 로직을 담당하는 클래스로 랜덤 카드 결제 로그를 생성하는 직접적인 로직을 나타냅니다.

package model.service;

import lombok.extern.slf4j.Slf4j;
import util.RandomUtil;
import model.domain.RanDataDTO;

@Slf4j
public class PaymentService {
	public static RanDataDTO genRandomPayment() {
		// 랜덤 데이터 생성
		String cardNum = RandomUtil.generateRandomCardNumber();
		int amount = (RandomUtil.random.nextInt(100) + 1) * 1000; // 1,000 ~ 100,000원
		String merchant = RandomUtil.getRandomMerchant();

		// 고의적인 에러 발생 로직 (20% 확률로 실패)
		boolean isFail = RandomUtil.random.nextInt(5) == 0; // 0~4 중 0이 나오면 실패

		String status = isFail ? "FAIL" : "SUCCESS";
		String errorCode = isFail ? RandomUtil.getRandomErrorCode() : null;
		String message = isFail ? "결제 승인 거절됨 (" + errorCode + ")" : "결제 정상 승인";
		
		RanDataDTO ranData = new RanDataDTO(cardNum, amount, merchant, isFail, status, errorCode, message);
		
		return ranData;
	}
}
