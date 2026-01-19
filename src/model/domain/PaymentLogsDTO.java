// PaymentLogsDTO
// 	- DB의 payment_logs 테이블의 칼럼과 매핑되는 필드를 갖는 클래스입니다.

package model.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PaymentLogsDTO{
	private Integer logId;         // log_id (PK, auto_increment)
    private String cardNumber;     // card_number
    private BigDecimal amount;     // decimal(10,0) -> 오차 없이 계산되는 BigDecimal 객체 사용
    private String merchantName;   // merchant_name
    private String status;         // status (SUCCESS, FAIL)
    private String message;        // message (에러 상세 내용)
    private LocalDateTime createdAt; // timestamp
    private Boolean isAlerted;     // tinyint(1) -> 자바에서는 Boolean으로 매핑됨
}
