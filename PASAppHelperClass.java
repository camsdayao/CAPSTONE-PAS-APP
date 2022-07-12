
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Scanner;


/**
 *@author CamilleDayao
 *@Description: to create PAS System to manage customer automobile insurance policies and as well as accident claims for an insurance company. 
 *
 */
public class PASAppHelperClass{
	private final String resultHdn = "--------------------------------------------------------";
	final String menu = "Automobile Insurance Policy and Claims Administration system (PAS) APP";
	private final String hdg_fmt_cancel= "%-13s%-13s%-23s%-17s%-20s%-17s%-17s";
	private String Data_fmt_cancel = "%-13s%-13s%-23s%-17s%-20s%-17s%-17s";
	private double policyNum;
	int intPolicyNum;
	private LocalDate dateGlobal;
	Scanner scan = new Scanner(System.in);
	
	private LocalDate validDate;
	private double doubleInput;
	private String expiDate;
	private String effectDate;
	private String seperator = "-------------------------------------------------------"
		  		+ "---------------------------------------------------------------";
	 
	//method to display the main menu
	public void Menu() {			
		System.out.print("\n======================= Automobile Insurance Policy and Claims Administration system (PAS) APP ======================= \n\n" +
				"1. Create a new Customer Account\n" + 
				"2. Get a policy quote and buy the policy.\n" + 
				"3. Cancel a specific policy (i.e change the expiration date of a policy to an earlier date than originally specified)\n" + 
				"4. File an accident claim against a policy.\n" + 
				"5. Search for a Customer account \n" + 
				"6. Search for and display a specific policy. \n" + 
				"7. Search for and display a specific claim. \n" +
				"8. Exit the PAS System \n"
				+ "======================================================================================================================\n\n"+ 
				"Please choose the NUMBER (1-8) of the following options: ");
	}
	
	public void InvalidMenu() {			//method to display the invalid message if user input is invalid
		System.out.println(resultHdn+ "\nNOTE: Invalid Input! Please select OPTION (1-8) only!! \n" + resultHdn + "\n");
	}
	public void opt1display() {			//method to show the welcome message of option#1
	System.out.println(" \n\n=======================  1. CREATE NEW CUSTOMER ACCOUNT  ======================= \n");
	}
	public void opt2display() {			//method to show the welcome message of option#2
		System.out.println("\n\n=======================  2. QUOTE POLICY /  BUY POLICY  ======================= \n");
	}
	public void opt3display() {			//method to show the welcome message of option#3
		System.out.println("\n\n=======================  CANCEL A POLICY  ======================= \n");
	}
	public void opt4display() {			//method to show the welcome message of option#4
		System.out.println("\n\n=======================  4. FILE A CLAIM  ======================= \n");
	}
	public void opt5display() {			//method to show the welcome message of option#5
		System.out.println("\n\n=======================  5. SEARCH CUSTOMER ACCOUNT  ======================= \n");
	}
	public void opt6display() {			//method to show the welcome message of option#6
		System.out.println("\n\n=======================  6. SEARCH POLICY  ======================= \n");
	}
	public void opt7display() {			//method to show the welcome message of option#7
		System.out.println("\n\n=======================  7. SEARCH CLAIM  ======================= \n");
	}
	
	public boolean getNameUsingPolicyNumber() {				// method use to get the name using policy number as welcome display
		boolean cancelResult = true;
		try {
			Connection conn = DriverManager.getConnection(			
				"jdbc:mysql://localhost:3306/sample_only","root", "root123"); 
				Statement stmt = conn.createStatement();
		
		String stringPolNum = null;
		boolean repeatInput = true;		// initial
		while(repeatInput) {
			System.out.print("Please Enter your POLICY NUMBER: ");
			stringPolNum = scan.nextLine();
			if(isNumberValid(stringPolNum, 000000, 999999)){
				repeatInput = false;
			}
			else {
				repeatInput =true;
			}
		}
		policyNum = Double.parseDouble(stringPolNum);
		if(checkDecimal(policyNum) == true) {
			intPolicyNum = (int)policyNum;
		}
		
		
		String PHNameQuery = "select holder_firstname from policy_holder where policy_number='" +intPolicyNum+"'";
		 ResultSet rsetgetPHName = stmt.executeQuery(PHNameQuery);
		 if(rsetgetPHName.next()) {
			 String policyPHName = rsetgetPHName.getString("holder_firstname");
			 System.out.println("Hello, " + policyPHName + "!\n");
			 cancelResult = true;
		 }
		 else {
			 System.out.println("Policy Number doesn't exist! \n");
			 cancelResult = false;
		 	}
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
		return cancelResult;
	}
	
	public double getPolicyNum() {								//getter method for policyNum
		return policyNum;
	}
	
	public void viewUpdatedDate() {								//method to view the updated details in option3 which is the cancel policy
		System.out.print("Please enter POLICY NUMBER to view updated details: ");
		String inputView = scan.nextLine();
		if(isNumberValid(inputView, 000000, 999999) == true) {
			try {
				Connection conn = DriverManager.getConnection(			
					"jdbc:mysql://localhost:3306/sample_only","root", "root123"); 
					Statement stmt = conn.createStatement();
		 String policyDetails = "select * from policy where policy_number = '"+inputView+"';";
		 ResultSet rsetgetDetails = stmt.executeQuery(policyDetails);
		 System.out.printf(seperator +" \n" + hdg_fmt_cancel, "Account No.", "Policy No.", "Policy Holder","Effective Date", "Expiration Date","No. of Vehicle","Premium\n"+ seperator + "\n" );
		 while(rsetgetDetails.next()) {
			 String polNum = rsetgetDetails.getString("policy_number");
			 String cEffectDate = rsetgetDetails.getString("effective_date");
			 String cExpiDate = rsetgetDetails.getString("expiration_date");
			 String polHolder = rsetgetDetails.getString("policy_holder");
			 String vehicle = rsetgetDetails.getString("vehicle");
			 String premium = rsetgetDetails.getString("premium");
			 String accountNum = rsetgetDetails.getString("account_number");
		
			 
			System.out.printf(Data_fmt_cancel, accountNum, polNum, polHolder,cEffectDate,cExpiDate, vehicle, premium);
			System.out.println("\n");
		 }
			}catch(SQLException ex) {
				System.out.println("POLICY NUMBER DOESN'T EXIST!");
				//ex.printStackTrace();
			}
		}
	}

	
	public void loader() {											//method to as loader before returning to main menu
		System.out.println("Press Enter to return to main menu...");
		String scanload = scan.nextLine();
		if(scanload.equals("")) {
			System.out.println("THANK YOU!");
			System.out.println();	
		}
	}
	
	
	//THE NEXT METHODS ARE USE FOR DIFFERENT VALIDATION
	
	
	//validate and check the input if the input is a number
	public boolean isNumberValid(String inputNum, int validMin, int validMax) {			//integer validation
		boolean valid = false; //initial
		if(inputNum.equals("")) {
			valid = false;
		}
		else {
			try {
				double doubleInput = Double.parseDouble(inputNum);
				double doublevalidMin = Double.valueOf(validMin);
				double doublevalidMax = Double.valueOf(validMax);
				
				if(doubleInput >= doublevalidMin && doubleInput <= doublevalidMax) {
					this.doubleInput = doubleInput;
					valid = true;
				}
				else {
					valid = false;
					System.out.println("NOTICE!!: INVALID INPUT! Please input within the range!\n");
				}
			}
			catch(Exception e) {
				System.out.println("NOTICE: INVALID INPUT! Please try again!\n");
				valid = false;
			}
		}
		return valid;
	}
	
	public double getNumValue() {					//method to get the value in integer validation method
		return doubleInput;
	}
	
	public boolean checkDecimal(double doubleVal) {		//method to check if the user input is a decimal or not
	boolean decimal = true;
	if(doubleInput - Math.floor(doubleInput) != 0){
		System.out.println("Reminder: This is a decimal value. The system will get only the whole number in the input.\n");
		decimal = false;
		} 
	else {
		decimal = true;
		}
	return decimal;
	}
	
	
	public int isDateValid(String inputDate) {			//method for date validation
	int result = 0; 									//initial value
	if(inputDate.equals("")) {
		result = 0;
	}
	else {
		try {
			LocalDate validDate = LocalDate.parse(inputDate);
			LocalDate LocaldateNow = LocalDate.now();
			
			if(validDate.compareTo(LocaldateNow) ==0) {				// to compare present 
				this.validDate = validDate;
				result = 1;
			}
			else if(validDate.compareTo(LocaldateNow) < 0) {		// to compare to past
				this.validDate = validDate;
				result = 2;
			}
			else if(validDate.compareTo(LocaldateNow) > 0) {		//to compare to future
				this.validDate = validDate;
				result =3;
			}
		}
		catch(Exception e) {
//			System.out.println("NOTICE: Invalid Date! Please try again! \n");
			result = 0;
			}
		}
	return result;
	}
	
	public LocalDate getDate() {					//method to get the date in date validation method
		return dateGlobal;
		}
	
	/*validation of the new expiration date in option#3 Cancel a policy. The user will not be able to saved a date if the input date is
	before effective date and after expiration date
	*/
	public boolean validateNewExpiDate(String inputDate) {		
	boolean result = false; 		//initial
	if(inputDate.equals("")) {
	}
	else {
		try {
			LocalDate validDate = LocalDate.parse(inputDate);
			LocalDate localExpiDate = LocalDate.parse(expiDate);
			LocalDate localEffectDate = LocalDate.parse(effectDate);
			
			if(validDate.compareTo(localEffectDate) >0 && validDate.compareTo(localExpiDate) <0 ) {				// valid input
				result = true;
			}
			else if(validDate.compareTo(localEffectDate) <0 || validDate.compareTo(localExpiDate) >0  ){		//invalid input
				result =false;
				System.out.println("NOTICE: Invalid date! Please input date before expiration date and after effective date!");
			}
		}
		catch(Exception e) {
			System.out.println("NOTICE: Invalid Date! Please try again! \n");
			result = false;
			}
		}
	return result;
	}
	
	public LocalDate getDatevalue() {				//method to get the value from the previous method
		return validDate;
	}

	public void getExpiandEffectDate(String expiDate, String effectDate) {
	this.expiDate = expiDate;
	this.effectDate = effectDate;	
	}
	
	public boolean isInputNull(String input) {				//validation to check if input is empty
		boolean nullResult = true;
		if(input.equals("")) {
			System.out.println("NOTICE: EMPTY FIELD! Please input needed value!");
			nullResult = true;
		}
		else {
			nullResult = false;
		}
		return nullResult;
	}
	
	public void closeScan() {								//method to call all the scanner and close
		CustomerAccount cusAcc = new CustomerAccount();
		cusAcc.closeScan();
		Policy pol = new Policy();
		pol.closeScan();
		PolicyHolder polHolder = new PolicyHolder();
		polHolder.closeScan();
		Claim claim = new Claim();
		claim.closeScan();
	}
}


