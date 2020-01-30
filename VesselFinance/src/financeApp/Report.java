package financeApp;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class Report extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	GridBagLayout gBag = new GridBagLayout();
	GridBagConstraints gbcSeshLbl,gbcSessionsSP,gbcBlankA,gbcPayLbl,gbcPaymentsSP,gbcBlankB,gbcTotalLbl,gbcData,gbcBlankC,gbcMain;
	
	int SESSIONS_TABLE_COLUMNS = 9;
	int PAYMENTS_TABLE_COLUMNS = 5;
	int totalPaid;
	float totalCost;
	String balance, hours;
	JLabel lblSesh, blankA, lblPay, blankB, lblTotal, lblData, blankC;
	JTable sessionsTable, paymentsTable;
	JScrollPane sessionsSP, paymentsSP;
	JButton btnMain = new JButton("Main Menu");

	public Report(String artist, String year) {
		if (year.equals("ALL TIME")) {
			populateAllTime(artist);
		}
		//TODO: what if year does NOT equal all time?
		setupLabels();
		setupPanel();
		registerListeners();
	}

	private void setupLabels() {
		balance = getBalance();
		
		lblSesh = new JLabel("SESSIONS");
		blankA = new JLabel(" ");
		lblPay = new JLabel("PAYMENTS");
		blankB = new JLabel(" ");
		lblTotal = new JLabel("TOTALS");
		lblData = new JLabel("Hours: "+hours+"        Cost: $"+String.format("%.02f", totalCost)+"        Balance: $"+balance);
		blankC = new JLabel(" ");
		
	}

	private String getBalance() {
		float balance = totalCost - totalPaid;
		String returnStr = String.format("%.02f", balance);
		return returnStr;
	}

	private void populateAllTime(String artist) {
		List<String> sessionsTableList, paymentsTableList;
		
		if (artist.equals("ALL ARTISTS")) {
			sessionsTableList = Sql.select("SELECT * FROM Sessions");
			paymentsTableList = Sql.select("SELECT * FROM Payments");
		} else {
			int artId = Sql.getArtIdFromName(artist);
			sessionsTableList = Sql.select("SELECT * FROM Sessions WHERE ArtID = '"+artId+"'");
			paymentsTableList = Sql.select("SELECT * FROM Payments WHERE ArtID = '"+artId+"'");
		}
		
		//FIRST POPULATE THE SESSIONS TABLE
		int sessionRows = sessionsTableList.size()/SESSIONS_TABLE_COLUMNS;
		List<String> cleanSessionsList = cleanSessionsData(sessionsTableList, sessionRows, SESSIONS_TABLE_COLUMNS);
		
		String[][] sessionsTableData = createTwoDArray(cleanSessionsList, sessionRows, SESSIONS_TABLE_COLUMNS -2);			
		calculateTotalCostAndHours(sessionsTableData);
		String[] sessionsColumnNames = {"ARTIST","DATE","HOURS","COST","TYPE","NOTES","PROJECT"};
		
		TableModel tModelSessions = new DefaultTableModel(sessionsTableData, sessionsColumnNames) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		sessionsTable = new JTable (tModelSessions);
		
		sessionsTable.getColumn("ARTIST").setPreferredWidth(90);
		sessionsTable.getColumn("DATE").setPreferredWidth(80);
		sessionsTable.getColumn("HOURS").setPreferredWidth(50);
		sessionsTable.getColumn("COST").setPreferredWidth(50);
		sessionsTable.getColumn("TYPE").setPreferredWidth(50);
		sessionsTable.getColumn("NOTES").setPreferredWidth(200);
		sessionsTable.getColumn("PROJECT").setPreferredWidth(100);
		
		sessionsTable.setPreferredScrollableViewportSize(new Dimension(900, 300));
		sessionsTable.setFillsViewportHeight(true);		   
	    sessionsSP = new JScrollPane(sessionsTable);
	    sessionsSP.setPreferredSize(new Dimension(900, 300));	
	    
	    //NOW POPULATE THE PAYMENTS TABLE
	    int paymentRows = paymentsTableList.size()/PAYMENTS_TABLE_COLUMNS;
	    List<String> cleanPaymentsList = cleanPaymentsData(paymentsTableList, paymentRows, PAYMENTS_TABLE_COLUMNS);
	    
	    String[][] paymentsTableData = createTwoDArray(cleanPaymentsList, paymentRows, PAYMENTS_TABLE_COLUMNS-1);
	    calculateTotalPaid(paymentsTableData);
	    String[] paymentsColumnNames = {"ARTIST", "DATE", "AMOUNT", "NOTES"};
	    
	    TableModel tModelPayments = new DefaultTableModel(paymentsTableData, paymentsColumnNames) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		}; 
		
		paymentsTable = new JTable (tModelPayments);
		
		paymentsTable.getColumn("ARTIST").setPreferredWidth(90);
		paymentsTable.getColumn("DATE").setPreferredWidth(80);
		paymentsTable.getColumn("AMOUNT").setPreferredWidth(50);
		paymentsTable.getColumn("NOTES").setPreferredWidth(200);
		
		paymentsTable.setPreferredScrollableViewportSize(new Dimension(900, 300));
		paymentsTable.setFillsViewportHeight(true);
		paymentsSP = new JScrollPane(paymentsTable);
		paymentsSP.setPreferredSize(new Dimension(900, 300));
	}

	private void calculateTotalPaid(String[][] paymentsTableData) {
		totalPaid = 0;
		for (int i=0; i<paymentsTableData.length; i++) {
			totalPaid += Integer.parseInt(paymentsTableData[i][2]);
		}
	}

	
	private void calculateTotalCostAndHours(String[][] sessionsTableData) {
		totalCost = 0;
		float hoursFloat = 0;
		
		for (int i=0; i<sessionsTableData.length; i++) {
			totalCost += Float.parseFloat(sessionsTableData[i][3]);
		}	
		
		for (int j=0; j<sessionsTableData.length; j++) {
			hoursFloat += Float.parseFloat(sessionsTableData[j][2]);
		}
		
		hours = Float.toString(hoursFloat);
	}

	private void setupPanel() {
		setupConstraints();
		this.setLayout(gBag);
		
		this.add(lblSesh, gbcSeshLbl);
		this.add(sessionsSP, gbcSessionsSP);
		this.add(blankA, gbcBlankA);
		this.add(lblPay, gbcPayLbl);
		this.add(paymentsSP, gbcPaymentsSP);
		this.add(blankB, gbcBlankB);
		this.add(lblTotal, gbcTotalLbl);
		this.add(lblData, gbcData);
		this.add(blankC, gbcBlankC);
		this.add(btnMain, gbcMain);
		
	}

	private void setupConstraints() {
		gbcSeshLbl = new GridBagConstraints();
		gbcSessionsSP = new GridBagConstraints();
		gbcBlankA = new GridBagConstraints();
		gbcPayLbl = new GridBagConstraints();
		gbcPaymentsSP = new GridBagConstraints();
		gbcBlankB = new GridBagConstraints();
		gbcTotalLbl = new GridBagConstraints();
		gbcData = new GridBagConstraints();
		gbcBlankC = new GridBagConstraints();
		gbcMain = new GridBagConstraints();
		
		gbcSessionsSP.fill = GridBagConstraints.BOTH;
		gbcPaymentsSP.fill = GridBagConstraints.BOTH;
		
		gbcSessionsSP.gridy = 1;
		gbcBlankA.gridy = 2;
		gbcPayLbl.gridy = 3;
		gbcPaymentsSP.gridy = 4;
		gbcBlankB.gridy = 5;
		gbcTotalLbl.gridy = 6;
		gbcData.gridy = 7;
		gbcBlankC.gridy = 8;
		gbcMain.gridy = 9;
		
	}

	private List<String> cleanSessionsData(List<String> sessionsTableList, int rows, int columns) {
		List<String> returnList = sessionsTableList;
		
		//convert artistID to artist name REMOVES ID AT THE SAME TIME!!
		for (int i=0; i<rows; i++) {
			returnList.set(i*columns, Sql.getNameFromArtID(sessionsTableList.get(i*columns+7)));
		}
		
		//convert projectID to project name
		for (int k=0; k<rows; k++) {
			returnList.set(k*columns + 8, Sql.getNameFromProjID(sessionsTableList.get(k*columns+8)));
		}
		
		//remove artistID column
		int modifier = 0;
		for (int j=0; j<rows; j++) {
			returnList.remove((j*columns + 7) - modifier);
			modifier++;
		}
		
		//remove rate column
		modifier = 0;
		for (int m=0; m<rows; m++) {
			returnList.remove((m*(columns-1) + 3) - modifier);
			modifier++;
		}
		
		return returnList;
	}
	
	private List<String> cleanPaymentsData(List<String> paymentsTableList, int rows, int columns) {
		List<String> returnList = paymentsTableList;
		
		//convert artistID to artist name REMOVES ID AT THE SAME TIME!!
		for (int i=0; i<rows; i++) {
			returnList.set(i*columns, Sql.getNameFromArtID(paymentsTableList.get(i*columns+4)));
		}
		
		//remove artistID column
		int modifier = 0;
		for (int j=0; j<rows; j++) {
			returnList.remove((j*columns + 4) - modifier);
			modifier++;
		}
		
		return returnList;
	}

	private String[][] createTwoDArray(List<String> tableList, int rows, int columns) {
		String[][] returnArr = new String[rows][columns];
		for (int i=0; i<rows; i++) {
			for (int j=0; j<columns; j++) {
				returnArr[i][j] = tableList.get(i*columns + j);
			}
		}
		return returnArr;
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
