import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Scanner;

/**
 * 
 */

/**
 *@author CamilleDayao
 *@Description: to create PAS System to manage customer automobile insurance policies and as well as accident claims for an insurance company. 
 */

public class CustomerAccount{						//instance variables
	private String firstName;
	private String lastName;
	private String address;
	public String stringAccNum;
	private String AccfirstName;
	private String AcclastName; 
													//for format
	private final String hdg_fmt= "%-5s %-10s %-10s %-12s %-35s %-30s %-20s";
	private final  String Data_fmt = "%-5s %-10s %-10s %-12s %-35s %-30s %-20s";
	private String seperator = "--------------------------------------------------------------"
		  		+ "--------------------------------------------------------------------";
	PASAppHelperClass help = new PASAppHelperClass();	
	
	Scanner scan = new Scanner(System.in);				// initialization of scanner
	
	public String AccountNumber() {						// this method will generate a 4 digits random number and will serve as account number
		  Random randomNum = new Random();
		  int number = randomNum.nextInt(9999);		// this will convert any number sequence into 4 character.		
		  stringAccNum = String.format("%04d", number);
		  return stringAccNum;
	}

	public String firstName() {												//this method will prompt user to first name
		boolean repeatInput = true;											// initial value of repeat variable
		while(repeatInput) {											
			System.out.print("Please Enter First Name: \n");
			String firstName = scan.nextLine();
			this.firstName = firstName;
			if(help.isInputNull(firstName) == false){						//the method will validate if the input value is within the range
				repeatInput = false;												//if the statement satify all the requirement, then it will return false to stop the loop
			}
			else {
				repeatInput =true;											//if statement didn't meet the requirement, it will return true and continue with loop
			}
		}
		return firstName;
	}
		
	public String lastName() {												//this method will prompt user to last name
		boolean repeatInput = true;											// initial value of repeat variable
		while(repeatInput) {											
			System.out.print("Please Enter Last Name: \n");
			String lastName = scan.nextLine();
			this.lastName = lastName;
			if(help.isInputNull(lastName) == false){						//the method will validate if the input value is within the range
				repeatInput = false;												//if the statement satify all the requirement, then it will return false to stop the loop
			}
			else {
				repeatInput =true;											//if statement didn't meet the requirement, it will return true and continue with loop
			}
		}
		return lastName;
	}
	
	public String Address() {												//this method will prompt user to input address
		
		boolean repeatInput = true;											// initial value of repeat variable
		while(repeatInput) {											
			System.out.print("Please Enter Complete Address: \n");
			String address = scan.nextLine();
			this.address = address;
			if(help.isInputNull(address) == false){							//the method will validate if the input value is within the range
				repeatInput = false;										//if the statement satify all the requirement, then it will return false to stop the loop
			}
			else {
				repeatInput =true;											//if statement didn't meet the requirement, it will return true and continue with loop
			}
		}
		return address;
	}
	
	public void Summary() {								// this method will display the summary of all the date gathered above
		System.out.println("-------------------------------------------"+"\nReminder: Please take note ACCOUNT NUMBER! \n"+ "ACCOUNT NUMBER: " + "#"+   stringAccNum+ "\n"
		+ "Name: "+ firstName + " " + lastName+"\n"+ "Address: " + address + " \n" + "-------------------------------------------"+"\n");
	}
	
	public void storeDB() {								//this method will be use to store the details on database as under customer account
		try {
			Connection conn = DriverManager.getConnection(			 // Construct a database 'Connection' object called 'conn'
			"jdbc:mysql://localhost:3306/sample_only","root", "root123"); 
			Statement stmt = conn.createStatement();			//Construct a 'Statement' object called 'stmt' inside the Connection created

			//SQL query string to input the values on database. The SQL query will be executed  via the 'Statement'.
			String CustomerAccountquery = "INSERT INTO customer_account (account_number,first_name,last_name,address) VALUES ('"+stringAccNum+"','"+firstName+"','"+lastName+"','"+address+"')";
			stmt.executeUpdate(CustomerAccountquery);
			System.out.println("YEHEY! DATA HAS BEEN SAVE SUCCESSFULLY! \n");
						
		}catch(SQLException ex) {
			System.out.println("System cannot process the input! FAILED to save data. Please try again!\n");		//Close conn and stmt
		}
	}
	
	/* This method is for account search (option 5)  where the user will be prompt to input first name and last name of the customer to search 
	for. This method will display the name of the user as welcome greetings and will also check if the first name is existing in database.
	The method will return a boolean value. If the first name and last name is existing on database the method will return true, else false.
	*/
	public boolean searchAccName() {					
		boolean searchnameResult = true;
		System.out.print("Please Enter Account First Name: ");			// to prompt user to input first name
		AccfirstName = scan.nextLine();
		System.out.print("Please Enter Account Last Name: ");			// to prompt user to input last name
		AcclastName = scan.nextLine();
		System.out.println();
		
		try {
			Connection conn = DriverManager.getConnection(			
			"jdbc:mysql://localhost:3306/sample_only","root", "root123"); 
			Statement stmt = conn.createStatement();
			String checkName = "select * from customer_account where first_name = '"+AccfirstName+"' and last_name ='"+AcclastName+"';"; 
			ResultSet getCheckName = stmt.executeQuery(checkName);
			if(getCheckName.next()) {
				System.out.println("Hello! " + AccfirstName + " " + AcclastName + "\n");
				searchnameResult = true;
			}
			else {
				System.out.println("ACCOUNT NAME DOESN'T EXIST!\n");
				searchnameResult=false;
				}
			}
		catch(SQLException ex) {
//		    ex.printStackTrace();
		    System.out.println("System cannot process the input! Please try again!\n");
		    searchnameResult=false;
			}
		return searchnameResult;
	}
	
	/* This method will serve as a basis how we can search the customer account. The method has a boolean return value, it will return a true if the query exist.
	 * this is a preparation to search for a customer details whether the customer has a a policy or no. If the customer has a policy and has a active customer account
	 * it will return true, if not, false
	*/
	public boolean checkResult() {
		boolean checkResult = true;
		try {
			Connection conn = DriverManager.getConnection(			
			"jdbc:mysql://localhost:3306/sample_only","root", "root123"); 
			Statement stmt = conn.createStatement();
			System.out.printf(seperator +"\n"+hdg_fmt,"No.", "Account No.", "First Name", "Last Name", "Address", "List of Policies","List of Policy Holder\n");
			System.out.println(seperator );
			String getAccName = "select * from customer_account join policy on customer_account.account_number = policy.account_number where customer_account.first_name = '"+AccfirstName+"' and customer_account.last_name ='"+AcclastName+"';"; 
			ResultSet rsetSearchName = stmt.executeQuery(getAccName);
			if(rsetSearchName.next()){
				checkResult = true;
			}
			else {
				checkResult=false;
			}
		}
		catch(SQLException ex) {
			    ex.printStackTrace();
		}
		return checkResult;
	}
	
	//This method will search for the account if the account has a policy. The query is a join statement to get the details from a joined table.
	public void searchAcc() {
		try {
			Connection conn = DriverManager.getConnection(			
			"jdbc:mysql://localhost:3306/sample_only","root", "root123"); 
			Statement stmt = conn.createStatement();
		
			int rowCount =0;
			String getAccName = "select * from customer_account join policy on customer_account.account_number = policy.account_number where customer_account.first_name = '"+AccfirstName+"' and customer_account.last_name ='"+AcclastName+"';"; 
			ResultSet rsetSearchName = stmt.executeQuery(getAccName);
			while(rsetSearchName.next()){
				rowCount++;
				String accNum = rsetSearchName.getString("account_number");
				String accFirstName = rsetSearchName.getString("first_name");
				String accLastName = rsetSearchName.getString("last_name");
				String accAddress = rsetSearchName.getString("address");
				String policies = rsetSearchName.getString("policy_number");
				String polHolder = rsetSearchName.getString("policy_holder");
			
				System.out.printf(Data_fmt,rowCount, accNum, accFirstName, accLastName, accAddress, policies, polHolder);
				System.out.println("");
		}
			System.out.println("\n");
		}
		catch(SQLException ex) {
		    ex.printStackTrace();
		}
	}
	
	//This method will search for the account if the account doesnt have a policy. The query is a simple query to get the details from the customer account table
	public void searchAccNoPolicy() {
		try {
			Connection conn = DriverManager.getConnection(			
			"jdbc:mysql://localhost:3306/sample_only","root", "root123"); 
			Statement stmt = conn.createStatement();
			String policies = "--";
			String polHolder = "--";
			int rowCount =0;
			String getAccName = "select * from customer_account where first_name = '"+AccfirstName+"' and last_name = '"+AcclastName+"';";
			ResultSet rsetSearchName = stmt.executeQuery(getAccName);
			while(rsetSearchName.next()){
				rowCount++;
				String accNum = rsetSearchName.getString("account_number");
				String accFirstName = rsetSearchName.getString("first_name");
				String accLastName = rsetSearchName.getString("last_name");
				String accAddress = rsetSearchName.getString("address");
				
				System.out.printf(Data_fmt,rowCount, accNum, accFirstName, accLastName, accAddress, policies, polHolder);
				System.out.println("\n\n");
			}

			}catch(SQLException ex) {
			    ex.printStackTrace();
			}
	}
	
	public void closeScan() {
		scan.close();
	}
}
