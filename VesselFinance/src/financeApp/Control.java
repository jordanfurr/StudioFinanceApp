package financeApp;

public class Control {
	static Frame frame;
	
	public static void main(String[] args) {
		new Control();
	}
	
	public Control() {
		frame = new Frame();
	}
	
	public static void menuUI() {
		frame.menuUI();
	}
	
	public static void logS_UI() {
		frame.logS_UI();
	}

	public static void logP_UI() {
		frame.logP_UI();
	}
	
	public static void confirmSessionUI(String artist, int sessionCost) {
		frame.confirmSessionUI(artist, sessionCost);
	}
	
	public static void confirmPaymentUI(String artist) {
		frame.confirmPaymentUI(artist);
	}
	
	public static void genReportUI() {
		frame.genReportUI();
	}

	public static void reportUI(String artist, String year) {
		frame.reportUI(artist, year);
		
	}
}
