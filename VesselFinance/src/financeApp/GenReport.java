package financeApp;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GenReport extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	GridBagLayout gBag = new GridBagLayout();
	GridBagConstraints gbcTitle,gbcBlankA,gbcArtLbl,gbcArt,gbcYearLbl,gbcYear,gbcBlankB,gbcSubmit,gbcCancel;
	
	JLabel title,blankA,lblArt,lblYear,blankB;
	JComboBox<String> artists, years;
	JButton btnSubmit = new JButton("Generate");
	JButton btnCancel = new JButton("Cancel");
	
	public GenReport() {
		populateBoxes();
		setupPanel();
		registerListeners();
	}
	
	private void populateBoxes() {
		String[] artistsArray = populateArtistsArray();
		artists = new JComboBox<>(artistsArray);
		artists.setPrototypeDisplayValue("Project Name Length");
		
		//String[] yearsArray = Sql.getYearsUsed();
		String[] yearsArray = populateYearsArray();
		years = new JComboBox<>(yearsArray);
		years.setPrototypeDisplayValue("Project Name Length");
	}

	private String[] populateYearsArray() {
		String[] returnArr = Sql.getYearsUsed();
		
		returnArr = Sql.sortThenAddElement(returnArr, "ALL TIME", true);
		return returnArr;
	}

	private String[] populateArtistsArray() {
		List<String> arrList = Sql.select("SELECT Name FROM Artists");
		int arrSize = arrList.size();
		String[] arr = new String[arrSize];
		
		for (int i=0; i<arrSize; i++) {
			arr[i] = arrList.get(i);
		}
		
		arr = Sql.sortThenAddElement(arr, "ALL ARTISTS", true);
		return arr;
	}

	private void setupPanel() {
		setupLabels();
		setupConstraints();
		this.setLayout(gBag);
		
		this.add(title, gbcTitle);
		this.add(blankA, gbcBlankA);
		this.add(lblArt, gbcArtLbl);
		this.add(artists, gbcArt);
		this.add(lblYear, gbcYearLbl);
		this.add(years, gbcYear);
		this.add(blankB, gbcBlankB);
		this.add(btnSubmit, gbcSubmit);
		this.add(btnCancel, gbcCancel);	
	}

	private void setupLabels() {
		title = new JLabel("GENERATE A REPORT");
		lblArt = new JLabel("Artist: ");
		lblYear = new JLabel("Year: ");
		blankA = new JLabel(" ");
		blankB = new JLabel(" ");
	}

	private void setupConstraints() {
		title.setHorizontalAlignment(JLabel.CENTER);
		
		gbcTitle = new GridBagConstraints();
		gbcBlankA = new GridBagConstraints();
		gbcArtLbl = new GridBagConstraints();
		gbcArt = new GridBagConstraints();
		gbcYearLbl = new GridBagConstraints();
		gbcYear = new GridBagConstraints();
		gbcBlankB = new GridBagConstraints();
		gbcSubmit = new GridBagConstraints();
		gbcCancel = new GridBagConstraints();
		
		gbcBlankA.gridy = 1;
		gbcArtLbl.gridy = gbcArt.gridy = 2;
		gbcYearLbl.gridy = gbcYear.gridy = 3;
		gbcBlankB.gridy = 4;
		gbcSubmit.gridy = gbcCancel.gridy = 5;
		
		gbcTitle.gridwidth = 2;
	}

	private void registerListeners() {
		btnSubmit.addActionListener(this);
		btnCancel.addActionListener(this);
		
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSubmit) {
			String artist = (String)artists.getSelectedItem();
			String year = (String)years.getSelectedItem();
			Control.reportUI(artist, year);
		}
		else if (e.getSource() == btnCancel) {
			Control.menuUI();
		} 
		
	}

}
