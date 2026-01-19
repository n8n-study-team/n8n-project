// RandomUtil
// 	- 랜덤 결제 정보를 생성하기 위한 메서드를 정의합니다.

package util;

import java.util.Random;

public class RandomUtil {
	
	public static final Random random = new Random();
	
	private RandomUtil() {}
	
    public static String generateRandomCardNumber() {
        return String.format("%04d-****-****-%04d", 
                random.nextInt(9000) + 1000, 
                random.nextInt(9000) + 1000);
    }

    public static String getRandomMerchant() {
        String[] merchants = {"스타벅스", "GS25", "쿠팡", "배달의민족", "카카오택시", "FISA구내식당"};
        return merchants[random.nextInt(merchants.length)];
    }

    public static String getRandomErrorCode() {
        String[] errors = {"잔액부족", "한도초과", "도난분실카드", "통신시간초과", "CVC오류"};
        return errors[random.nextInt(errors.length)];
    }
}
