package financeApp;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Sql {
	static Connection c = null;

	
	public static void main(String[] args) {
		testing();		
		close();
	} //end main() 
	
	
	private static void testing() {		
		/*
		String drop = "DROP TABLE IF EXISTS Artists";
		String sql = "CREATE TABLE Artists " +
				"(ID 		INT PRIMARY KEY		NOT NULL," +
				" Name			VARCHAR(30)		NOT NULL)";
		createTable(sql, drop);
		
		drop = "DROP TABLE IF EXISTS Projects";
		sql = "CREATE TABLE Projects " +
				"(ID 		INT PRIMARY KEY		NOT NULL," +
				" Name			VARCHAR(30)		NOT NULL," +
				" ArtID			INT				NOT NULL)";	
		createTable(sql, drop);
		
		drop = "DROP TABLE IF EXISTS Sessions";
		sql = "CREATE TABLE Sessions " +
				"(ID		INT PRIMARY KEY		NOT NULL," +
				" Date			VARCHAR(19)		NOT NULL," +
				" Hours			REAL			NOT NULL," +
				" Price			INT				NOT NULL," +
				" SessionCost	INT				NOT NULL," +
				" Type			VARCHAR(11)		NOT NULL," +
				" Notes			VARCHAR(200)	NOT NULL," +
				" ArtID			INT				NOT NULL," +
				" ProjID		INT				NOT NULL)";
		createTable(sql, drop);
		
		drop = "DROP TABLE IF EXISTS Payments";
		sql = "CREATE TABLE Payments " +
				"(ID		INT PRIMARY KEY		NOT NULL," +
				" Date			VARCHAR(19)		NOT NULL," +
				" Amount		INT				NOT NULL," +
				" Notes			VARCHAR(200)	NOT NULL," +
				" ArtID			INT				NOT NULL)";
		createTable(sql, drop);
	
		int test = convertDateToValue("May 27, 2018");
		int testB = convertDateToValue("Jun 5, 2018");
		System.out.println(test);
		System.out.println(testB);
		*/
	} //end testing()

	public static void testPrint() {
		String sql = "SELECT * FROM Artists";
		System.out.println("Artists: "+select(sql));
		sql = "SELECT * FROM Projects";
		System.out.println("Projects: "+select(sql));	
		sql = "SELECT * FROM Sessions";
		System.out.println("Sessions: "+select(sql));
		sql = "SELECT * FROM Payments";
		System.out.println("Payments: "+select(sql));
	}

	private static void connect() {		
		try {
			//String path = "jdbc:sqlite:C:\Users\Jordan\Google Drive\VESSEL_FINANCE_APP\VesselFinance";
			String path = "jdbc:sqlite:vesselDBtest.db"; //DB FILE - CHANGE WHEN DONE TESTING
			//String path = "jdbc:sqlite:vesselDBtestB.db";
			c = DriverManager.getConnection(path);
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}	
	} //end connect()
	
	private static void close() {
		try {
			if (c != null) {
				c.close();
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	} //end close()

	private static void createTable(String sql, String drop) {
		connect();
		try {
			PreparedStatement pStmt = c.prepareStatement(drop);
			pStmt.executeUpdate();
			pStmt.close();
			pStmt = c.prepareStatement(sql);
			pStmt.executeUpdate();
			pStmt.close();
			System.out.println("Table created");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		close();
	} //end createTable()
	
	private static void sqlCommand(String sql) {
		connect();
		try {
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		close();
	} //end sqlCommand
	
	public static List<String> select(String sql) {
		connect();
		List<String> resultsList = new ArrayList<String>();
		
		try {
			Statement stmt = c.createStatement();
			ResultSet results = stmt.executeQuery(sql);
			ResultSetMetaData rmd = results.getMetaData();
			
			while (results.next()) {
				for (int i=1; i<=rmd.getColumnCount(); i++) {
					resultsList.add(results.getString(i));
				}
			}

			results.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {	
			close();
		}
		return resultsList;
	} //end select()
	
	public static void addSession(String[] data) {
		boolean added = false;
		int artId, projId;
		
		artId = getArtIdFromName(data[0]);
		projId = getProjIdFromName(data[1], artId);
		
		connect();
		List<String> sizeList = select("SELECT ID FROM Sessions");
		int arrSize = sizeList.size();
		
		connect();
		while (!added) {
			int newId = arrSize + 1;
			String sql = "INSERT INTO Sessions (ID,Date,Hours,Price,SessionCost,Type,Notes,ArtID,ProjID)" +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
			
			try {
				PreparedStatement stmt = c.prepareStatement(sql);
				stmt.setInt(1, newId);
				stmt.setString(2, data[2]);
				stmt.setFloat(3, Float.parseFloat(data[3]));
				stmt.setInt(4, Integer.parseInt(data[4]));
				stmt.setInt(5, Integer.parseInt(data[5]));
				stmt.setString(6, data[6]);
				stmt.setString(7, data[7]);
				stmt.setInt(8, artId);
				stmt.setInt(9, projId);
				stmt.executeUpdate();
				stmt.close();
				added = true;
				System.out.println("Session added");
			} catch (SQLException e) {
				if (e.getErrorCode() == 19) {
					arrSize += 1;
				}else{
					System.out.println(e.getErrorCode());
					System.out.println(e.getMessage());
				}
			}
		}
	} //end addSession

	public static void addPayment(String[] data) {
		boolean added = false;
		int artId;
		
		artId = getArtIdFromName(data[0]);
		
		connect();
		List<String> sizeList = select("SELECT ID FROM Payments");
		int arrSize = sizeList.size();
		
		connect();
		while (!added) {
			int newId = arrSize + 1;
			String sql = "INSERT INTO Payments (ID,Date,Amount,Notes,ArtID)" +
					"VALUES (?, ?, ?, ?, ?);";
			try {
				PreparedStatement stmt = c.prepareStatement(sql);
				stmt.setInt(1, newId);
				stmt.setString(2, data[2]);
				stmt.setInt(3, Integer.parseInt(data[1]));
				stmt.setString(4, data[3]);
				stmt.setInt(5, artId);
				stmt.executeUpdate();
				stmt.close();
				added = true;
				System.out.println("Session added");
			} catch (SQLException e) {
				if (e.getErrorCode() == 19) {
					arrSize += 1;
				}else{
					System.out.println(e.getErrorCode());
					System.out.println(e.getMessage());
				}
			}
		}
	}

	private static int getProjIdFromName(String projectName, int artId) {
		int projId;
		
		if (projectInDB(projectName)) {
			connect();
			List<String> projIdList = select("SELECT ID FROM Projects WHERE Name = '"+projectName+"';");
			projId = Integer.parseInt(projIdList.get(0));
		}
		else {
			projId = addNewProject(projectName, artId);
		}
		return projId;
	}

	private static int getTotalCostFromArtistName(String artistName) {
		int totalCost = 0;
		
		int artId = getArtIdFromName(artistName);
		List<String> costsList = select("SELECT SessionCost FROM Sessions WHERE ArtID ="+artId+";");
		int arrSize = costsList.size();
		int[] costsArr = new int[arrSize];
		
		for (int i=0; i<arrSize; i++) {
			costsArr[i] = Integer.parseInt(costsList.get(i));
		}
		
		for (int j=0; j<arrSize; j++) {
			totalCost += costsArr[j];
		}
		
		return totalCost;
	}
	
	private static int getTotalPaidFromArtistName(String artistName) {
		int totalPaid = 0;
		
		int artId = getArtIdFromName(artistName);
		List<String> paymentsList = select("SELECT Amount FROM Payments WHERE ArtID ="+artId+";");
		int arrSize = paymentsList.size();
		int[] paymentsArr = new int[arrSize];
		
		for (int i=0; i<arrSize; i++) {
			paymentsArr[i] = Integer.parseInt(paymentsList.get(i));
		}
		
		for (int j=0; j<arrSize; j++) {
			totalPaid += paymentsArr[j];
		}
		
		return totalPaid;
	}
	
	public static int getBalance(String artist) {
		int totalCost = Sql.getTotalCostFromArtistName(artist);
		int totalPaid = Sql.getTotalPaidFromArtistName(artist);
		
		int balance = totalCost - totalPaid;

		return balance;
	}
	
	public static String[] getYearsUsed() {
		List<String> sessionsDatesList = select("SELECT Date FROM Sessions");
		List<String> yearsList = new ArrayList<>();
		for (String i : sessionsDatesList) {
			if (!yearsList.contains(trimDateToYear(i))) {
				yearsList.add(trimDateToYear(i));
			}
		}
		
		List<String> paymentsDatesList = select("SELECT Date FROM Payments");
		for (String k : paymentsDatesList) {
			if (!yearsList.contains(trimDateToYear(k))) {
				yearsList.add(trimDateToYear(k));
			}
		}
		
		int arrSize = yearsList.size();
		String[] yearsArr = new String[arrSize];
		
		for (int j=0; j<arrSize; j++) {
			yearsArr[j] = yearsList.get(j);
		}
		
		//TODO: NEXT FOR LOOP IS TESTING
		for (int l=0; l<yearsArr.length; l++) {
			System.out.println(yearsArr[l]);
		}
		return yearsArr;
	}
	
	private static boolean artistInDB(String artName) {
		Statement stmt;
		ResultSet results;
		boolean rtnBool = false;
		
		connect();
		String sql = "SELECT Name FROM Artists";
		try {
			stmt = c.createStatement();
			results = stmt.executeQuery(sql);
			while (results.next()) {
				if (results.getString(1).equals(artName)) {
					rtnBool = true;
				}
			}
			results.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		close();
		return rtnBool;
	} //end artistInDB()
	
	private static boolean projectInDB(String projName) {
		Statement stmt;
		ResultSet results;
		boolean rtnBool = false;
		
		connect();
		String sql = "SELECT Name FROM Projects";
		try {
			stmt = c.createStatement();
			results = stmt.executeQuery(sql);
			while (results.next()) {
				if (results.getString(1).equals(projName)) {
					rtnBool = true;
				}
			}
			results.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		close();
		return rtnBool;
	} //end projectInDB()

	public static void updateName(String newName, String oldName) {
		connect();
		String sql = "UPDATE Artists SET Name = ? WHERE Name = ?;";
		try {
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, newName);
			stmt.setString(2, oldName);
			stmt.executeUpdate();
			System.out.println("name updated");
			stmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close();
		}
	} //end updateName()
	
	public static int addNewArtist(String artistName) {
		boolean added = false;
		int artId = 999;
		
		connect();
		List<String> arrList = select("SELECT Name FROM Artists");
		int arrSize = arrList.size();
		
		connect();
		while (!added) {
			int newId = arrSize + 1;
			String sql = "INSERT INTO Artists (ID,Name)" +
					"VALUES (?, ?);";
			try {
				PreparedStatement stmt = c.prepareStatement(sql);
				stmt.setInt(1, newId);
				stmt.setString(2, artistName);
				stmt.executeUpdate();
				stmt.close();
				added = true;
				artId = newId;
			} catch (SQLException e) {
				if (e.getErrorCode() == 19) {
					arrSize += 1;
				}else{
					System.out.println(e.getErrorCode());
					System.out.println(e.getMessage());
				}
			}
		}
		close();
		return artId;
	} //end addArtist()
	
	public static int addNewProject(String projName, int artId) {
		boolean added = false;
		int projId = 999;
		
		connect();
		List<String> arrList = select("SELECT Name FROM Projects");
		int arrSize = arrList.size();
		
		connect();
		while (!added) {
			int newId = arrSize + 1;
			String sql = "INSERT INTO Projects (ID,Name,ArtID)" +
					"VALUES (?, ?, ?);";
			try {
				PreparedStatement stmt = c.prepareStatement(sql);
				stmt.setInt(1, newId);
				stmt.setString(2, projName);
				stmt.setInt(3, artId);
				stmt.executeUpdate();
				stmt.close();
				added = true;
				projId = newId;
			} catch (SQLException e) {
				if (e.getErrorCode() == 19) {
					arrSize += 1;
				}else{
					System.out.println(e.getErrorCode());
					System.out.println(e.getMessage());
				}
			}
		}
		close();
		return projId;
	} //end addArtist()

	public static void deleteArtist(String artistName) {
		deleteArtistProjects(artistName);
		//TODO: Delete all sessions, projects(DONE), and payments associated with the artist
		
		connect();
		String sql = "DELETE from Artists WHERE Name = ?;";
		try {
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, artistName);
			stmt.executeUpdate();
			System.out.println(artistName + " deleted from Artists table");
			stmt.close();
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close();
		}
	} //end deleteArtist()
	
	private static void deleteArtistProjects(String artistName) {
		List<String> arrList = select("SELECT ID FROM Artists where Name = '"+artistName+"';");
		
		connect();
		int id = Integer.parseInt(arrList.get(0));
		String sql = "DELETE FROM Projects WHERE ArtID = ?";
		try {
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close();
		}
	} //end deleteArtistProjects()
	
///////////////// UTILITY METHODS //////////////////////////
	
	private static String trimDateToYear(String date) {
		date = date.split(", ")[1];
		return date;
	}
	
	public static String[] sortThenAddElement(String[] unsortedArr, String element, boolean topOfList) {
		Arrays.sort(unsortedArr); //sorts the array in place
		
		String[] returnArr = new String[unsortedArr.length+1];
		
		int modifier, indexToPlace;
		if (topOfList) {
			modifier = 1;
			indexToPlace = 0;
		} else {
			modifier = 0;
			indexToPlace = returnArr.length - 1;
		}
		
		for (int i=0; i<unsortedArr.length; i++) {
			returnArr[i+modifier] = unsortedArr[i];
		}
		
		returnArr[indexToPlace] = element;
		return returnArr;
	}
	
	public static int convertDateToValue(String date) {
		String[] dateArr = date.split(" ");
		int returnInt = 0;
		int monthModifier = 0;
		
		if (!dateArr[0].equals("Jan")) {
			monthModifier = 31;
			if (!dateArr[0].equals("Feb")) {
				monthModifier += 29;
				if (!dateArr[0].equals("Mar")) {
					monthModifier += 31;
					if (!dateArr[0].equals("Apr")) {
						monthModifier += 30;
						if (!dateArr[0].equals("May")) {
							monthModifier += 31;
							if (!dateArr[0].equals("Jun")) {
								monthModifier += 30;
								if (!dateArr[0].equals("Jul")) {
									monthModifier += 31;
									if (!dateArr[0].equals("Aug")) {
										monthModifier += 31;
										if (!dateArr[0].equals("Sep")) {
											monthModifier += 30;
											if (!dateArr[0].equals("Oct")) {
												monthModifier += 31;
												if (!dateArr[0].equals("Nov")) {
													monthModifier += 30;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		int day = Integer.parseInt(dateArr[1].split(",")[0]);
		int year = Integer.parseInt(dateArr[2]);
		
		returnInt = year + monthModifier + day;
		
		for (int i=0; i<dateArr.length; i++){
			System.out.println(dateArr[i]);
		}

		return returnInt;	
	}
	
	public static int getArtIdFromName(String artistName) {
		int artId;
		
		if (artistInDB(artistName)) {
			connect();
			List<String> artIdList = select("SELECT ID FROM Artists WHERE Name = '"+artistName+"';");
			artId = Integer.parseInt(artIdList.get(0));
		}
		else {
			artId = addNewArtist(artistName);
		}
		return artId;
	}
	
	public static String getNameFromArtID (String artID) {
		List<String> nameList = select("SELECT Name FROM Artists WHERE ID = " + artID + ";");		
		return nameList.get(0);	
	}
	
	public static String getNameFromProjID (String projID) {
		List<String> nameList = select("SELECT Name FROM Projects WHERE ID = " + projID + ";");
		return nameList.get(0);
	}


}
