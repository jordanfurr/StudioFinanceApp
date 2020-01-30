package financeApp;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainMenu extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	GridBagLayout gBag = new GridBagLayout();
	GridBagConstraints gbcTitle, gbcBlank, gbcLogS, gbcLogP, gbcGen, gbcAdmin;
	
	JLabel title = new JLabel("THE VESSEL SHIP MATE");
	JLabel blank = new JLabel(" ");
	JButton btnLogS = new JButton("Log a Session");
	JButton btnLogP = new JButton("Log a Payment");
	JButton btnGen = new JButton("Generate a Report");
	JButton btnAdmin = new JButton("Admin");

	public MainMenu() {
		setupPanel();
		registerListeners();

	}
	
	private void setupPanel() {
		setupConstraints();
		this.setLayout(gBag);
		
		this.add(title, gbcTitle);
		this.add(blank, gbcBlank);
		this.add(btnLogS, gbcLogS);
		this.add(btnLogP, gbcLogP);
		this.add(btnGen, gbcGen);
		this.add(btnAdmin, gbcAdmin);
		
	}

	private void setupConstraints() {
		title.setHorizontalAlignment(JLabel.CENTER);
		gbcTitle = new GridBagConstraints();
		gbcBlank = new GridBagConstraints();
		gbcLogS = new GridBagConstraints();
		gbcLogP = new GridBagConstraints();
		gbcGen = new GridBagConstraints();
		gbcAdmin = new GridBagConstraints();
	
		gbcBlank.gridy = 1;
		gbcLogS.gridy = 2;
		gbcLogP.gridy = 3;
		gbcGen.gridy = 4;
		gbcAdmin.gridy = 5;
		
		gbcTitle.fill = GridBagConstraints.HORIZONTAL;
		gbcAdmin.fill = gbcGen.fill = gbcLogP.fill = gbcLogS.fill = gbcTitle.fill;
	}

	private void registerListeners() {
		btnLogS.addActionListener(this);
		btnLogP.addActionListener(this);
		btnGen.addActionListener(this);
		btnAdmin.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnLogS) {
			Control.logS_UI();
		}
		else if (e.getSource() == btnLogP) {
			Control.logP_UI();
		}
		else if (e.getSource() == btnGen) {
			Control.genReportUI();
		}
		
	}
}