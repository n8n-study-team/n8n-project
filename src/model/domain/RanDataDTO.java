// RanDataDTO
// 	- 랜덤 값을 저장하는데 사용할 데이터 필드만 명시하는 클래스입니다.

package model.domain;

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
public class RanDataDTO{
	private String cardNum;
	private int amount;
	private String merchant;
	private boolean isFail;
	private String status;
	private String errorCode;
	private String message;
}
