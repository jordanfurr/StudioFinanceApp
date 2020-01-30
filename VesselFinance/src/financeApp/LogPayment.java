package financeApp;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.jdatepicker.JDatePicker;

public class LogPayment extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	GridBagLayout gBag = new GridBagLayout();
	GridBagConstraints gbcTitle,gbcBlankA,gbcArtLbl,gbcArt,gbcAmountLbl,gbcAmount,gbcDateLbl,gbcDate,gbcNotesLbl,gbcNotes,gbcBlankB,gbcSubmit,gbcCancel;
	
	JLabel title,blankA,lblArt,lblAmount,lblDate,lblNotes,blankB; 
	JComboBox<String> artists;
	JDatePicker dateP = new JDatePicker();
	JTextField amount = new JTextField();
	JTextArea notesField = new JTextArea(3,20);
	JScrollPane scrollPane = new JScrollPane(notesField);
	JButton btnSubmit = new JButton("Submit");
	JButton btnCancel = new JButton("Cancel");
	
	String artist,dollars,date,notes;
	
	public LogPayment() {
		populateArtists();
		setupPanel();
		registerListeners();
	}

	private void populateArtists() {
		List<String> arrList = Sql.select("SELECT Name FROM Artists");
		System.out.println("populating Artists");
		int arrSize = arrList.size();
		String[] arr = new String[arrSize];
		
		for (int i=0; i<arrSize; i++) {
			arr[i] = arrList.get(i);
		}
		
		Arrays.sort(arr);
		artists = new JComboBox<>(arr);
		artists.setPrototypeDisplayValue("Project Name Length");
	}

	private void setupPanel() {
		setupLabels();
		setupConstraints();
		this.setLayout(gBag);
		
		this.add(title, gbcTitle);
		this.add(blankA, gbcBlankA);
		this.add(lblArt, gbcArtLbl);
		this.add(artists, gbcArt);
		this.add(lblAmount, gbcAmountLbl);
		this.add(amount, gbcAmount);
		this.add(lblDate, gbcDateLbl);
		this.add(dateP, gbcDate);
		this.add(lblNotes, gbcNotesLbl);
		this.add(scrollPane, gbcNotes);
		this.add(blankB, gbcBlankB);
		this.add(btnSubmit, gbcSubmit);
		this.add(btnCancel, gbcCancel);
	}
	
	private void setupLabels() {
		title = new JLabel("LOG PAYMENT");
		blankA = new JLabel(" ");
		lblArt = new JLabel("Artist: ");
		lblAmount = new JLabel("Amount: $");
		lblDate = new JLabel("Date: ");
		lblNotes = new JLabel("Notes: ");
		blankB = new JLabel(" ");	
	}
	
	private void setupConstraints() {
		title.setHorizontalAlignment(JLabel.CENTER);
		
		gbcTitle = new GridBagConstraints();
		gbcBlankA = new GridBagConstraints();
		gbcArtLbl = new GridBagConstraints();
		gbcArt = new GridBagConstraints();
		gbcDateLbl = new GridBagConstraints();
		gbcDate = new GridBagConstraints();
		gbcAmountLbl = new GridBagConstraints();
		gbcAmount = new GridBagConstraints();
		gbcNotesLbl = new GridBagConstraints();
		gbcNotes = new GridBagConstraints();
		gbcBlankB = new GridBagConstraints();
		gbcSubmit = new GridBagConstraints();
		gbcCancel = new GridBagConstraints();
		
		gbcBlankA.gridy = 1;
		gbcArtLbl.gridy = gbcArt.gridy = 2;
		gbcAmountLbl.gridy = gbcAmount.gridy = 3;
		gbcDateLbl.gridy = gbcDate.gridy = 4;
		gbcNotesLbl.gridy = gbcNotes.gridy = 5;
		gbcBlankB.gridy = 6;
		gbcSubmit.gridy = gbcCancel.gridy = 7;
		
		gbcTitle.gridwidth = 2;
		gbcAmount.fill = gbcDate.fill = GridBagConstraints.HORIZONTAL;
		gbcNotes.fill = GridBagConstraints.BOTH;
		notesField.setLineWrap(true);
		notesField.setWrapStyleWord(true);		
	}
	
	private void registerListeners() {
		btnSubmit.addActionListener(this);
		btnCancel.addActionListener(this);	
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSubmit) {
			pullInfo();
			if (safeToSubmit()) {
				submitInfo();
			}
		}
		else if (e.getSource() == btnCancel) {
			Control.menuUI();
		}	
	}

	private void pullInfo() {
		artist = (String)artists.getSelectedItem();
		dollars = amount.getText();
		date = dateP.getFormattedTextField().getText();
		notes = notesField.getText();
	}

	private boolean safeToSubmit() {
		if (dollars.trim().equals("") || date.trim().equals("")) {
			return false;
		}
		return true;
	}

	private void submitInfo() {
		String[] data = {artist,dollars,date,notes};
		Sql.addPayment(data);
		Sql.testPrint(); //TODO TESTING, DELETE
		
		Control.confirmPaymentUI(artist);
	}

}
