// PaymentFailView
//	- 모든 실패 메세지를 콘솔에 보여주는 view입니다.
// 	- 추후 인터페이스로 디벨롭하는 것도 좋을 것 같습니다.

package view;

public class PaymentFailView {
	public static void printMsg(String msg) {
		System.out.println("view: 문제가 발생했습니다 - " + msg);
	}
}