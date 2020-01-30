package financeApp;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	int defaultWidth = 1000;
	int defaultHeight = 900;
	int bigWidth = 1000;
	int bigHeight = 900;
	Container cont;
	JPanel pnlFrame = new JPanel(); //using a JPanel as the "frame" to place all other JPanels on
	CardLayout cards = new CardLayout();
	MainMenu menuUI = new MainMenu();
	LogSession logS_UI;
	LogPayment logP_UI;
	ConfirmSession confirmSessionUI;
	ConfirmPayment confirmPaymentUI;
	GenReport genReportUI;
	Report reportUI;
	
	
	public Frame() {
		super("The Vessel Finance");
		setupGUI();		
	} //end Frame constructor
	
	private void setupGUI() {
		pnlFrame.setLayout(cards);
		pnlFrame.add(menuUI, "menuUI");
		//pnlFrame.add(logS_UI, "logS_UI");
		
		cont = this.getContentPane();
		cont.setLayout(new GridLayout());
		cont.add(pnlFrame);

		cards.show(pnlFrame, "menuUI");	
		
		this.setSize(defaultWidth,defaultHeight);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void menuUI() {
		this.setSize(defaultWidth,defaultHeight);
		cards.show(pnlFrame, "menuUI");
	}

	public void logS_UI() {
		logS_UI = new LogSession();
		try {
			pnlFrame.remove(logS_UI);
		}catch(NullPointerException e) { /* do nothing */ }
		pnlFrame.add(logS_UI, "logS_UI");
		cards.show(pnlFrame, "logS_UI");
	}

	public void logP_UI() {
		logP_UI = new LogPayment();
		try {
			pnlFrame.remove(logP_UI);
		}catch(NullPointerException e) { /* do nothing */ }
		pnlFrame.add(logP_UI, "logP_UI");
		cards.show(pnlFrame, "logP_UI");
		
	}
	
	public void confirmSessionUI(String artist, int sessionCost) {
		confirmSessionUI = new ConfirmSession(artist, sessionCost);
		try {
			pnlFrame.remove(confirmSessionUI);
		} catch (NullPointerException e) {/*do nothing*/}
		pnlFrame.add(confirmSessionUI, "confirmSessionUI");
		cards.show(pnlFrame, "confirmSessionUI");
	}

	public void confirmPaymentUI(String artist) {
		confirmPaymentUI = new ConfirmPayment(artist);
		try {
			pnlFrame.remove(confirmPaymentUI);
		} catch (NullPointerException e) {/*do nothing*/}
		pnlFrame.add(confirmPaymentUI, "confirmPaymentUI");
		cards.show(pnlFrame, "confirmPaymentUI");
	}

	public void genReportUI() {
		this.setSize(defaultWidth,defaultHeight);
		genReportUI = new GenReport();
		try {
			pnlFrame.remove(genReportUI);
		} catch (NullPointerException e) {/*do nothing*/}
		pnlFrame.add(genReportUI, "genReportUI");
		cards.show(pnlFrame, "genReportUI");
	}

	public void reportUI(String artist, String year) {
		this.setSize(bigWidth, bigHeight);
		reportUI = new Report(artist, year);
		try {
			pnlFrame.remove(reportUI);
		} catch (NullPointerException e) {/*do nothing*/}
		pnlFrame.add(reportUI,  "reportUI");
		cards.show(pnlFrame, "reportUI");		
	}

	
	
}