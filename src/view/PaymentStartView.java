// PaymentStartView
//	- 해당 앱의 진입점으로 이곳에서 모든 것이 시작됩니다.

package view;

import controller.PaymentController;

public class PaymentStartView {
	
	public static void main(String[] args) {
		System.out.println("view: 간이 카드 결제 시스템을 가동합니다.");
		PaymentController.startSystem();
	}
}
