package financeApp;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConfirmPayment extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	GridBagLayout gBag = new GridBagLayout();
	GridBagConstraints gbcTitle,gbcBlankA,gbcBalance,gbcBlankB,gbcReport,gbcMain;
	
	JLabel title,blankA,lblBalance,blankB;
	JButton btnReport = new JButton("View Artist Report");
	JButton btnMain = new JButton("Main Menu");
	
	public ConfirmPayment(String artist) {
		int balance = Sql.getBalance(artist);
		setupLabels(balance, artist);
		setupPanel();
		registerListeners();
	}

	private void setupPanel() {
		setupConstraints();
		this.setLayout(gBag);
		
		this.add(title, gbcTitle);		
		this.add(blankA, gbcBlankA);
		this.add(lblBalance, gbcBalance);		
		this.add(blankB, gbcBlankB);		
		this.add(btnReport, gbcReport);		
		this.add(btnMain, gbcMain);		
	}

	private void setupConstraints() {
		title.setHorizontalAlignment(JLabel.CENTER);
		
		gbcTitle = new GridBagConstraints();
		gbcBlankA = new GridBagConstraints();
		gbcBalance = new GridBagConstraints();
		gbcBlankB = new GridBagConstraints();
		gbcReport = new GridBagConstraints();
		gbcMain = new GridBagConstraints();
		
		gbcBlankA.gridy = 1;
		gbcBalance.gridy = 2;
		gbcBlankB.gridy = 3;
		gbcReport.gridy = 4;
		gbcMain.gridy = 5;
	}

	private void setupLabels(int balance, String artist) {
		title = new JLabel("PAYMENT ADDED!");
		lblBalance = new JLabel("Remaining balance for " + artist + ": $" + Integer.toString(balance));
		blankA = new JLabel(" ");
		blankB = new JLabel(" ");	
	}
	
	private void registerListeners() {
		btnMain.addActionListener(this);
		
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnMain) {
			Control.menuUI();
		}	
	}
}
