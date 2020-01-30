package financeApp;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConfirmSession extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	GridBagLayout gBag = new GridBagLayout();
	GridBagConstraints gbcTitle,gbcBlankA,gbcDues,gbcBalance,gbcBlankB,gbcReport,gbcLogAnother,gbcLogP,gbcMain;

	JLabel title,blankA,lblDues,lblBalance,blankB;
	JButton btnReport = new JButton("View Artist Report");
	JButton btnLogAnother = new JButton("Log Another Session");
	JButton btnLogP = new JButton("Log a Payment");
	JButton btnMain = new JButton("Main Menu");
	
	String dues,balance;
	
	public ConfirmSession(String artist, int sessionCost) {
		int balance = Sql.getBalance(artist);
		setupLabels(sessionCost, balance, artist);
		setupPanel();
		registerListeners();
	}

	private void setupPanel() {
		setupConstraints();
		this.setLayout(gBag);
		
		this.add(title, gbcTitle);		
		this.add(blankA, gbcBlankA);		
		this.add(lblDues, gbcDues);		
		this.add(lblBalance, gbcBalance);		
		this.add(blankB, gbcBlankB);		
		this.add(btnReport, gbcReport);		
		this.add(btnLogAnother, gbcLogAnother);		
		this.add(btnLogP, gbcLogP);		
		this.add(btnMain, gbcMain);		
	}

	private void setupConstraints() {
		title.setHorizontalAlignment(JLabel.CENTER);
		
		gbcTitle = new GridBagConstraints();
		gbcBlankA = new GridBagConstraints();
		gbcDues = new GridBagConstraints();
		gbcBalance = new GridBagConstraints();
		gbcBlankB = new GridBagConstraints();
		gbcReport = new GridBagConstraints();
		gbcLogAnother = new GridBagConstraints();
		gbcLogP = new GridBagConstraints();
		gbcMain = new GridBagConstraints();
		
		gbcBlankA.gridy = 1;
		gbcDues.gridy = 2;
		gbcBalance.gridy = 3;
		gbcBlankB.gridy = 4;
		gbcReport.gridy = 5;
		gbcLogAnother.gridy= 6;
		gbcLogP.gridy = 7;
		gbcMain.gridy = 8;
	}

	private void setupLabels(int sessionCost, int balance, String artist) {
		title = new JLabel("SESSION ADDED!");
		lblDues = new JLabel("Session dues: $" + Integer.toString(sessionCost));
		lblBalance = new JLabel("Total balance for " + artist + ": $" + Integer.toString(balance));
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
