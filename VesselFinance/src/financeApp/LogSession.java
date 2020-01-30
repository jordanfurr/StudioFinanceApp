package financeApp;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.jdatepicker.JDatePicker;

public class LogSession extends JPanel implements ActionListener, ItemListener {
	private static final long serialVersionUID = 1L;
	
	GridBagLayout gBag = new GridBagLayout();
	GridBagConstraints gbcTitle,gbcBlankA,gbcBlankB,gbcArtLbl,gbcNewArtLbl,gbcProjLbl,gbcNewProjLbl,gbcDateLbl,gbcHoursLbl,gbcAt,gbcPer,gbcTypeLbl,gbcNotesLbl,gbcArtists,gbcNewArt,gbcProj,gbcNewProj,gbcDateP,gbcHours,gbcPrices,gbcTypes,gbcNotes,gbcSubmit,gbcCancel;
	
	JLabel title,lblArtist,lblNewArtist,lblProj,lblNewProj,lblDate,lblHours,lblAt,lblPer,lblType,lblNotes,blankA,blankB;	
	JComboBox<String> artists, projects, prices, types;
	JDatePicker dateP = new JDatePicker();
	JTextField hoursField = new JTextField();
	JTextField newArtField = new JTextField();
	JTextField newProjField = new JTextField();
	JTextArea notesField = new JTextArea(3,20);
	JScrollPane scrollPane = new JScrollPane(notesField);
	JButton btnSubmit = new JButton("Submit");
	JButton btnCancel = new JButton("Cancel");
	
	String artist,project,date,hours,rate,cost,type,notes;
	
	
	public LogSession() {
		populateBoxes();
		setupPanel();
		registerListeners();
	} //end constructor()
	
	public LogSession(int ArtistIndex, int ProjectIndex) {
		/*
		 * This constructor will be used when the user clicks
		 * the "Log Another Session" button on the confirmation screen.
		 * 
		 *  TODO: Update this to refill artist and project fields
		 *  with previously used data, while doing everything else
		 *  the normal constructor does.
		 */
	} //end polymorphic constructor()

	private void populateBoxes() {
		String[] artistsArray = populateArtistsArray();
		artists = new JComboBox<>(artistsArray);
		artists.setPrototypeDisplayValue("Project Name Length");
		
		String[] projectsArray = populateProjectsArray();
		projects = new JComboBox<>(projectsArray);
		projects.setPrototypeDisplayValue("Project Name Length");
		
		String[] pricesArray = {"15", "20", "25", "30", "35", "40", "45", "50", "55", "60"};
		prices = new JComboBox<>(pricesArray);
		prices.setSelectedIndex(3);
		
		String[] typesArray = {"Tracking", "Mixing", "Production", "Other"};
		types = new JComboBox<>(typesArray);		
	}

	private String[] populateArtistsArray() {
		List<String> arrList = Sql.select("SELECT Name FROM Artists");
		int arrSize = arrList.size();
		String[] arr = new String[arrSize];
		
		for (int i=0; i<arrSize; i++) {
			arr[i] = arrList.get(i);
		}
		
		arr = Sql.sortThenAddElement(arr, "NEW ARTIST", false);
		return arr;
	}
	
	private String[] populateProjectsArray() {
		String[] arr;
		String artistSelected = (String)artists.getSelectedItem();
		List<String> idList = Sql.select("SELECT ID FROM Artists WHERE Name = '"+artistSelected+"'");
		try {
			String id = idList.get(0);
			
			List<String> arrList = Sql.select("SELECT Name FROM Projects WHERE ArtID = " + id);
			int arrSize = arrList.size();
			arr = new String[arrSize];
			
			for (int i=0; i<arrSize; i++) {
				arr[i] = arrList.get(i);
			}
	
			arr = Sql.sortThenAddElement(arr, "NEW PROJECT", false);
		} catch (IndexOutOfBoundsException e) {
			arr = new String[] {"NEW PROJECT"};
		}
		return arr;
	}
	
	private void repopulateProjectsArray() {
		projects.removeAllItems();
		String[]arr = populateProjectsArray();
		for (int i=0; i<arr.length; i++) {
			projects.addItem(arr[i]);
		}
	}

	private void setupPanel() {
		setupLabels();
		setupConstraints();
		this.setLayout(gBag);
		
		this.add(title, gbcTitle);
		this.add(blankA, gbcBlankA);
		this.add(lblArtist, gbcArtLbl);
		this.add(artists, gbcArtists);
		this.add(lblNewArtist, gbcNewArtLbl);
		this.add(newArtField, gbcNewArt);
		this.add(lblProj, gbcProjLbl);
		this.add(projects, gbcProj);
		this.add(lblNewProj, gbcNewProjLbl);
		this.add(newProjField, gbcNewProj);
		this.add(lblDate, gbcDateLbl);
		this.add(dateP, gbcDateP);
		this.add(lblHours, gbcHoursLbl);
		this.add(hoursField, gbcHours);
		this.add(lblAt, gbcAt);
		this.add(prices, gbcPrices);
		this.add(lblPer, gbcPer);
		this.add(lblType, gbcTypeLbl);
		this.add(types, gbcTypes);
		this.add(lblNotes, gbcNotesLbl);
		this.add(scrollPane, gbcNotes);
		this.add(blankB, gbcBlankB);
		this.add(btnSubmit, gbcSubmit);
		this.add(btnCancel, gbcCancel);
		
		newArtField.setEnabled(false);
		newProjField.setEnabled(false);
		
		newArtistProjectCheck();
	}

	private void newArtistProjectCheck() {
		if ((String)artists.getSelectedItem() == "NEW ARTIST") {
			newArtField.setEnabled(true);
			lblNewArtist.setText("New Artist Name: ");
			projects.removeAllItems();
			projects.addItem("NEW PROJECT");
		}else {
			repopulateProjectsArray();
			newArtField.setEnabled(false);
			lblNewArtist.setText(" ");
		}
		if ((String)projects.getSelectedItem() == "NEW PROJECT") {
			newProjField.setEnabled(true);
			lblNewProj.setText("New Project Name: ");
		}else {
			newProjField.setEnabled(false);
			lblNewProj.setText(" ");
		}	
	}

	private void setupLabels() {
		title = new JLabel("LOG A SESSION");
		lblArtist = new JLabel("Artist: ");
		lblNewArtist = new JLabel(" ");
		lblProj = new JLabel("Project: ");
		lblNewProj = new JLabel(" ");
		lblDate = new JLabel("Date: ");
		lblHours = new JLabel("Hours: ");
		lblAt = new JLabel(" at $");
		lblPer = new JLabel(" per hour");
		lblType = new JLabel("Session Type: ");
		lblNotes = new JLabel("Notes: ");
		blankA = new JLabel(" ");
		blankB = new JLabel(" ");		
	}

	private void setupConstraints() {
		title.setHorizontalAlignment(JLabel.CENTER);
		
		gbcTitle = new GridBagConstraints();
		gbcBlankA = new GridBagConstraints();
		gbcArtLbl = new GridBagConstraints();
		gbcArtists = new GridBagConstraints();
		gbcNewArtLbl = new GridBagConstraints();
		gbcNewArt = new GridBagConstraints();
		gbcProjLbl = new GridBagConstraints();
		gbcProj = new GridBagConstraints();
		gbcNewProjLbl = new GridBagConstraints();
		gbcNewProj = new GridBagConstraints();
		gbcDateLbl = new GridBagConstraints();
		gbcDateP = new GridBagConstraints();
		gbcHoursLbl = new GridBagConstraints();
		gbcHours = new GridBagConstraints();
		gbcAt = new GridBagConstraints();
		gbcPrices = new GridBagConstraints();
		gbcPer = new GridBagConstraints();
		gbcTypeLbl = new GridBagConstraints();
		gbcTypes = new GridBagConstraints();
		gbcNotesLbl = new GridBagConstraints();
		gbcNotes = new GridBagConstraints();
		gbcBlankB = new GridBagConstraints();
		gbcSubmit = new GridBagConstraints();
		gbcCancel = new GridBagConstraints();
		
		gbcBlankA.gridy = 1;
		gbcArtLbl.gridy = gbcArtists.gridy = 2;
		gbcNewArtLbl.gridy = gbcNewArt.gridy = 3;
		gbcProjLbl.gridy = gbcProj.gridy = 4;
		gbcNewProjLbl.gridy = gbcNewProj.gridy = 5;
		gbcDateLbl.gridy = gbcDateP.gridy = 6;
		gbcHoursLbl.gridy = gbcHours.gridy = gbcAt.gridy = gbcPrices.gridy = gbcPer.gridy = 7;
		gbcTypeLbl.gridy = gbcTypes.gridy = 8;
		gbcNotesLbl.gridy = gbcNotes.gridy = 9;
		gbcBlankB.gridy = 10;
		gbcSubmit.gridy = gbcCancel.gridy = 11;
		
		gbcTitle.gridwidth = 5;
		gbcHours.fill = gbcNewArt.fill = gbcNewProj.fill = gbcDateP.fill = GridBagConstraints.HORIZONTAL;
		gbcNotes.fill = GridBagConstraints.BOTH;
		gbcNotes.gridwidth = 4;
		notesField.setLineWrap(true);
		notesField.setWrapStyleWord(true);		
	}

	private void registerListeners() {
		btnSubmit.addActionListener(this);
		btnCancel.addActionListener(this);
		artists.addItemListener(this);
		projects.addItemListener(this);
	} 

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSubmit) {
			pullInfo();
			if (safeToSumbit()) {
				submitInfo();
			}
		}
		else if (e.getSource() == btnCancel) {
			Control.menuUI();
		}	
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == artists) {
			if ((String)artists.getSelectedItem() == "NEW ARTIST") {
				newArtField.setEnabled(true);
				lblNewArtist.setText("New Artist Name: ");
				projects.removeAllItems();
				projects.addItem("NEW PROJECT");
			}else {
				repopulateProjectsArray();
				newArtField.setEnabled(false);
				lblNewArtist.setText(" ");
			}
		}
		else if (e.getSource() == projects) {
			if ((String)projects.getSelectedItem() == "NEW PROJECT") {
				newProjField.setEnabled(true);
				lblNewProj.setText("New Project Name: ");
			}else {
				newProjField.setEnabled(false);
				lblNewProj.setText(" ");
			}
		}
		else {
			System.out.println(e.getSource());
		}
		
	}
	
	private void pullInfo() {
		artist = (String)artists.getSelectedItem();
		project = (String)projects.getSelectedItem();
		date = dateP.getFormattedTextField().getText();
		hours = hoursField.getText();
		rate = (String)prices.getSelectedItem();
		type = (String)types.getSelectedItem();
		notes = notesField.getText();
		
		if (artist == "NEW ARTIST") {
			artist = newArtField.getText();
		}
		if (project == "NEW PROJECT") {
			project = newProjField.getText();
		}
	}
		
	private boolean safeToSumbit() {
		if (artist.trim().equals("") || project.trim().equals("") ||
				date.trim().equals("") || hours.trim().equals(""))
		{
			return false;
		}
		return true;
	}

	private void submitInfo() {
		int sessionCost = Math.round(Float.parseFloat(hours) * Integer.parseInt(rate));
		cost = Integer.toString(sessionCost);
		System.out.println(sessionCost); //TODO TESTING, DELETE
		
		String[] data = {artist,project,date,hours,rate,cost,type,notes};
		Sql.addSession(data);
		Sql.testPrint(); //TODO TESTING, DELETE
		
		Control.confirmSessionUI(artist,sessionCost);
	}
	
}
